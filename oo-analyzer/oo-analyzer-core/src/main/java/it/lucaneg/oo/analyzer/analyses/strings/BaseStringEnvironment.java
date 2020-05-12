package it.lucaneg.oo.analyzer.analyses.strings;

import java.util.Map;

import it.lucaneg.oo.ast.expression.Variable;
import it.lucaneg.oo.ast.expression.dereference.Call;
import it.lucaneg.oo.ast.expression.literal.IntLiteral;
import it.lucaneg.oo.ast.expression.literal.Literal;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;
import it.lucaneg.oo.ast.types.ClassType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractEnvironment;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractLattice;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;
import it.lucaneg.oo.sdk.analyzer.program.Program;

public abstract class BaseStringEnvironment<L extends AbstractLattice<L>, E extends BaseStringEnvironment<L, E>> extends AbstractEnvironment<L, E> {
	
	/**
	 * The class type representing the String type
	 */
	private static ClassType stringType;

	/**
	 * Yields the class type representing the String type
	 * 
	 * @return the String class type
	 */
	protected static ClassType getStringType() {
		if (stringType != null)
			return stringType;

		return stringType = ClassType.get(Program.STRING_CLASS);
	}
	
	protected BaseStringEnvironment() {
		super();
	}
	
	/**
	 * Builds a string environment by shallow copying the given one
	 * 
	 * @param other the original environment
	 */
	protected BaseStringEnvironment(Map<MLocalVariable, L> approximations) {
		super(approximations);
	}
	
	@Override
	protected final L defaultLatticeForType(Type t) {
		return t == BaseStringEnvironment.getStringType() ? latticeTop() : latticeBottom();
	}
	
	@Override
	protected final L evalLiteral(Literal literal) {
		if (literal instanceof StringLiteral)
			return provideApproxFor((StringLiteral) literal);

		return super.evalLiteral(literal);
	}

	protected abstract L provideApproxFor(StringLiteral literal); 
	
	@Override
	protected final L evalCall(Call call) {
		if (call.getReceiver().asExpression().getStaticType() != getStringType())
			return super.evalCall(call);
		
		if (!(call.getReceiver().asExpression() instanceof Variable)) 
			return latticeTop();
		
		L rec = evalVariable((Variable) call.getReceiver());

		if (call.getName().equals("concat")) {
			L par;
			if (call.getActuals()[0].asExpression() instanceof Variable)
				par = evalVariable((Variable) call.getActuals()[0]);
			else if (call.getActuals()[0].asExpression() instanceof Literal) 
				par = evalLiteral((StringLiteral) call.getActuals()[0]);
			else
				return latticeTop();
				
			if (par.isBottom())
				// we the parameter is a non-string value
				// that gets converted to a string. We consider
				// its conversion as top
				par = latticeTop();
			
			return evalConcat(rec, par);
		} else if (call.getName().equals("substring")) {
			if (!(call.getActuals()[0].asExpression() instanceof IntLiteral && call.getActuals()[1].asExpression() instanceof IntLiteral))
				return latticeTop();

			int begin = ((IntLiteral) call.getActuals()[0].asExpression()).getValue();
			int end = ((IntLiteral) call.getActuals()[1].asExpression()).getValue();
			return evalSubstring(rec, begin, end);
		} else if (call.getName().equals("replace")) {
			L first, second;
			if (call.getActuals()[0].asExpression() instanceof Variable)
				first = evalVariable((Variable) call.getActuals()[0]);
			else if (call.getActuals()[0].asExpression() instanceof Literal) 
				first = evalLiteral((StringLiteral) call.getActuals()[0]);
			else
				return latticeTop();
				
			if (first.isBottom())
				// we the parameter is a non-string value
				// that gets converted to a string. We consider
				// its conversion as top
				first = latticeTop();
			
			if (call.getActuals()[1].asExpression() instanceof Variable)
				second = evalVariable((Variable) call.getActuals()[1]);
			else if (call.getActuals()[1].asExpression() instanceof Literal) 
				second = evalLiteral((StringLiteral) call.getActuals()[1]);
			else
				return latticeTop();
				
			if (second.isBottom())
				// we the parameter is a non-string value
				// that gets converted to a string. We consider
				// its conversion as top
				second = latticeTop();
			
			return evalReplace(rec, first, second);
		}

		return rec;
	}

	protected abstract L evalSubstring(L receiver, int begin, int end); 

	protected abstract L evalConcat(L receiver, L parameter);
	
	protected abstract L evalReplace(L receiver, L first, L second);
	
	@Override
	public final Satisfiability satisfiesCall(Call call) {
		if (call.getReceiver().asExpression().getStaticType() != getStringType())
			return Satisfiability.UNKNOWN;

		L rec = latticeTop();
		if (call.getReceiver().asExpression() instanceof Variable)
			rec = evalVariable((Variable) call.getReceiver());

		if (call.getActuals().length == 1 && call.getActuals()[0].asExpression().getStaticType() == getStringType()) {
			L par = latticeTop();
			if (call.getActuals()[0].asExpression() instanceof Variable)
				par = evalVariable((Variable) call.getActuals()[0]);
			else if (call.getActuals()[0].asExpression() instanceof StringLiteral)
				par = evalLiteral((StringLiteral) call.getActuals()[0]);

			if (call.getName().equals("equals"))
				return satisfiesEquals(rec, par);
			else if (call.getName().equals("contains"))
				return satisfiesContains(rec, par);
			else if (call.getName().equals("startsWith"))
				return satisfiesStartsWith(rec, par);
			else if (call.getName().equals("endsWith"))
				return satisfiesEndsWith(rec, par);
		}

		return Satisfiability.UNKNOWN;
	}

	protected abstract Satisfiability satisfiesEndsWith(L rec, L par);

	protected abstract Satisfiability satisfiesStartsWith(L rec, L par);

	protected abstract Satisfiability satisfiesContains(L rec, L par);

	protected abstract Satisfiability satisfiesEquals(L rec, L par);
}
