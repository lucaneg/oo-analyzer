package it.lucaneg.oo.analyzer.analyses.strings;

import it.lucaneg.oo.ast.expression.Variable;
import it.lucaneg.oo.ast.expression.dereference.Call;
import it.lucaneg.oo.ast.expression.literal.IntLiteral;
import it.lucaneg.oo.ast.expression.literal.Literal;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractLattice;

public abstract class BaseStringExpressionEvaluator<L extends AbstractLattice<L>>
		extends AbstractExpressionEvaluator<L> {

	protected abstract L latticeBottom();

	protected abstract L latticeTop();
	
	@Override
	protected L evalLiteral(Literal literal, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		if (literal instanceof StringLiteral)
			return provideApproxFor((StringLiteral) literal);
		return super.evalLiteral(literal, env, parent);
	}

	protected abstract L provideApproxFor(StringLiteral literal);
		
	@Override
	protected final L evalCall(Call call, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		if (call.getReceiver().asExpression().getStaticType() != Type.getStringType())
			return super.evalCall(call, env, parent);
		
		if (!(call.getReceiver().asExpression() instanceof Variable)) 
			return latticeTop();
		
		L rec = evalVariable((Variable) call.getReceiver(), env, parent);

		if (call.getName().equals("concat")) {
			L par;
			if (call.getActuals()[0].asExpression() instanceof Variable)
				par = evalVariable((Variable) call.getActuals()[0], env, parent);
			else if (call.getActuals()[0].asExpression() instanceof Literal) 
				par = evalLiteral((StringLiteral) call.getActuals()[0], env, parent);
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

			int begin = ((IntLiteral) call.getActuals()[0].asExpression()).getValue(); // TODO
			int end = ((IntLiteral) call.getActuals()[1].asExpression()).getValue(); // TODO
			return evalSubstring(rec, begin, end);
		} else if (call.getName().equals("replace")) {
			L first, second;
			if (call.getActuals()[0].asExpression() instanceof Variable)
				first = evalVariable((Variable) call.getActuals()[0], env, parent);
			else if (call.getActuals()[0].asExpression() instanceof Literal) 
				first = evalLiteral((StringLiteral) call.getActuals()[0], env, parent);
			else
				return latticeTop();
				
			if (first.isBottom())
				// we the parameter is a non-string value
				// that gets converted to a string. We consider
				// its conversion as top
				first = latticeTop();
			
			if (call.getActuals()[1].asExpression() instanceof Variable)
				second = evalVariable((Variable) call.getActuals()[1], env, parent);
			else if (call.getActuals()[1].asExpression() instanceof Literal) 
				second = evalLiteral((StringLiteral) call.getActuals()[1], env, parent);
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
}
