package it.lucaneg.oo.analyzer.analyses.value;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import it.lucaneg.oo.analyzer.analyses.value.domains.bools.AbstractBooleanLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.strings.AbstractStringLattice;
import it.lucaneg.oo.analyzer.cli.Configurable;
import it.lucaneg.oo.analyzer.cli.Parameter;
import it.lucaneg.oo.analyzer.cli.RequiresParameters;
import it.lucaneg.oo.analyzer.cli.SetupException;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.expression.Variable;
import it.lucaneg.oo.ast.expression.comparison.ComparisonBinaryExpression;
import it.lucaneg.oo.ast.expression.comparison.Equal;
import it.lucaneg.oo.ast.expression.comparison.Greater;
import it.lucaneg.oo.ast.expression.comparison.GreaterOrEqual;
import it.lucaneg.oo.ast.expression.comparison.Less;
import it.lucaneg.oo.ast.expression.comparison.LessOrEqual;
import it.lucaneg.oo.ast.expression.comparison.NotEqual;
import it.lucaneg.oo.ast.expression.dereference.Call;
import it.lucaneg.oo.ast.expression.literal.IntLiteral;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;
import it.lucaneg.oo.ast.expression.logical.Not;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractAnalysis;
import it.lucaneg.oo.sdk.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.sdk.analyzer.fixpoint.FixpointImpl;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;
import it.lucaneg.oo.sdk.analyzer.program.instructions.ILocalWrite;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

@RequiresParameters({
		@Parameter(name = "bools", description = "domain for boolean values", provider = AbstractBooleanLattice.class),
		@Parameter(name = "ints", description = "domain for integer values", provider = AbstractIntegerLattice.class),
		@Parameter(name = "strings", description = "domain for string values", provider = AbstractStringLattice.class)
})
public class ValueAnalysis extends AbstractAnalysis<ValueLattice, ValueEnvironment> implements Configurable {

	private static final int WIDENING_THRESHOLD = 5;

	private ValueExpressionEvaluator evaluator = new ValueExpressionEvaluator();

	private ValueSatisfiabilityEvaluator satisfiability = new ValueSatisfiabilityEvaluator();

	private AbstractBooleanLattice<?> booleanSingleton;

	private AbstractIntegerLattice<?> intSingleton;

	private AbstractStringLattice<?> stringSingleton;
	
	@Override
	@SuppressWarnings("rawtypes")
	public void setup(String... parameters) throws SetupException {
		if (parameters == null || parameters.length != 3)
			throw new SetupException("Incorrect number of parameters");
		
		Reflections reflections = new Reflections(SingleValueLattice.class, new SubTypesScanner());
		Set<Class<? extends SingleValueLattice>> instances = reflections.getSubTypesOf(SingleValueLattice.class); 
		try {
			for (Class<? extends SingleValueLattice> candidate : instances) 
				if (!Modifier.isAbstract(candidate.getModifiers()) && !candidate.isAnonymousClass())
					if (candidate.getSimpleName().equals(parameters[0]))
						booleanSingleton = (AbstractBooleanLattice<?>) candidate.getMethod("getTop").invoke(null);
					else if (candidate.getSimpleName().equals(parameters[1]))
						intSingleton = (AbstractIntegerLattice<?>) candidate.getMethod("getTop").invoke(null);
					else if (candidate.getSimpleName().equals(parameters[2]))
						stringSingleton = (AbstractStringLattice<?>) candidate.getMethod("getTop").invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new SetupException("Unable to setup value analysis", e);
		}
		
		if (booleanSingleton == null)
			throw new SetupException("Boolean domain " + parameters[0] + " not found");
		if (intSingleton == null)
			throw new SetupException("Int domain " + parameters[1] + " not found");
		if (stringSingleton == null)
			throw new SetupException("String domain " + parameters[2] + " not found");
		
		evaluator.setBooleanSingleton(booleanSingleton);
		evaluator.setIntSingleton(intSingleton);
		evaluator.setStringSingleton(stringSingleton);
	}

	@Override
	public ValueExpressionEvaluator getExpressionEvaluator() {
		return evaluator;
	}

	@Override
	public ValueSatisfiabilityEvaluator getSatisfiabilityEvaluator() {
		return satisfiability;
	}

	@Override
	public ValueEnvironment smallStepSemantics(Statement st, ValueEnvironment env) {
		if (st instanceof ILocalWrite)
			return handleLocalWrite(st, env);
		else
			// TODO handle more statements
			return env;
	}

	private ValueEnvironment handleLocalWrite(Statement st, ValueEnvironment env) {
		ILocalWrite varOperation = (ILocalWrite) st;
		ValueEnvironment result = env.copy();

		if (!ValueEnvironment.canProcessType(varOperation.getVariable().getType()))
			result.set(varOperation.getVariable(), ValueLattice.getBottom());
		else
			// the value gets always overwritten
			result.set(varOperation.getVariable(), evaluator.eval(varOperation.getExpression(), env));

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ValueEnvironment assume(Expression expr, ValueEnvironment env) {
		 // TODO
		if (satisfiability.satisfies(expr, env, evaluator) == Satisfiability.NOT_SATISFIED) {
			ValueEnvironment copy = env.copy();
			for (Pair<MLocalVariable, ValueLattice> pair : env)
				// unreachable code
				copy.set(pair.getLeft(), ValueLattice.getBottom());
			copy.makeUnreachable();
			return copy;
		}
		
		if (expr instanceof Call && ((Call) expr).getReceiver().asExpression().getStaticType() == Type.getStringType()) {
			Call call = (Call) expr;
			if (call.getName().equals("equals")) {
				ValueEnvironment copy = env.copy();
				if (call.getReceiver() instanceof Variable) {
					Variable var = (Variable) call.getReceiver();
					Expression par = call.getActuals()[0].asExpression();
					if (par instanceof Variable)
						copy.set(var.getName(), copy.at(((Variable) par).getName()));
					else if (par instanceof StringLiteral)
						copy.set(var.getName(), new ValueLattice(stringSingleton.mk(((StringLiteral) par).getValue())));
					return copy;
				}
			}
		} else if (expr instanceof ComparisonBinaryExpression) { 
			ComparisonBinaryExpression comp = (ComparisonBinaryExpression) expr;
			ValueLattice var;
			AbstractIntegerLattice constant;
			String varName;
			if (comp.getLeft() instanceof IntLiteral && comp.getRight() instanceof Variable) {
				constant = intSingleton.mk(((IntLiteral) comp.getLeft()).getValue());
				varName = ((Variable) comp.getRight()).getName();
				var = env.at(varName); 
			} else if (comp.getRight() instanceof IntLiteral && comp.getLeft() instanceof Variable) {
				constant = intSingleton.mk(((IntLiteral) comp.getRight()).getValue());
				varName = ((Variable) comp.getLeft()).getName();
				var = env.at(varName);
			} else 
				return env;
			
			ValueEnvironment copy = env.copy();
			if (comp instanceof Equal)
				copy.set(varName, new ValueLattice(constant));
			else if (comp instanceof Greater)
				copy.set(varName, new ValueLattice(((AbstractIntegerLattice) var.getInnerElement()).makeGreaterThan(constant)));
			else if (comp instanceof GreaterOrEqual)
				copy.set(varName, new ValueLattice(((AbstractIntegerLattice) var.getInnerElement()).makeGreaterOrEqualThan(constant)));
			else if (comp instanceof Less)
				copy.set(varName, new ValueLattice(((AbstractIntegerLattice) var.getInnerElement()).makeLessThan(constant)));
			else if (comp instanceof LessOrEqual)
				copy.set(varName, new ValueLattice(((AbstractIntegerLattice) var.getInnerElement()).makeLessOrEqualThan(constant)));
			else 
				return env;
				
			return copy;
		} else if (expr instanceof Not) {
			Expression e = ((Not) expr).getExpression();
			if (e instanceof Call && ((Call) e).getReceiver().asExpression().getStaticType() == Type.getStringType()) {
				Call call = (Call) e;
				if (call.getName().equals("equals")) {
					ValueEnvironment copy = env.copy();
					if (call.getReceiver().asExpression() instanceof Variable) {
						Expression par = call.getActuals()[0].asExpression();
						if (par instanceof Variable && satisfiability.satisfies(e, env, evaluator) == Satisfiability.SATISFIED)
							copy.set(call.getName(), ValueLattice.getBottom());
						else if (par instanceof StringLiteral && satisfiability.satisfies(e, env, evaluator) == Satisfiability.SATISFIED)
							copy.set(call.getName(), ValueLattice.getBottom());
						return copy;
					}
				}
			} else if (e instanceof ComparisonBinaryExpression) { 
				ComparisonBinaryExpression comp = (ComparisonBinaryExpression) e;
				ValueLattice var;
				AbstractIntegerLattice constant;
				String varName;
				if (comp.getLeft() instanceof IntLiteral && comp.getRight() instanceof Variable) {
					constant = intSingleton.mk(((IntLiteral) comp.getLeft()).getValue());
					varName = ((Variable) comp.getRight()).getName();
					var = env.at(varName); 
				} else if (comp.getRight() instanceof IntLiteral && comp.getLeft() instanceof Variable) {
					constant = intSingleton.mk(((IntLiteral) comp.getRight()).getValue());
					varName = ((Variable) comp.getLeft()).getName();
					var = env.at(varName);
				} else 
					return env;
				
				ValueEnvironment copy = env.copy();
				if (comp instanceof NotEqual)
					copy.set(varName, new ValueLattice(constant));
				else if (comp instanceof LessOrEqual)
					copy.set(varName, new ValueLattice(((AbstractIntegerLattice) var.getInnerElement()).makeGreaterThan(constant)));
				else if (comp instanceof Less)
					copy.set(varName, new ValueLattice(((AbstractIntegerLattice) var.getInnerElement()).makeGreaterOrEqualThan(constant)));
				else if (comp instanceof GreaterOrEqual)
					copy.set(varName, new ValueLattice(((AbstractIntegerLattice) var.getInnerElement()).makeLessThan(constant)));
				else if (comp instanceof Greater)
					copy.set(varName, new ValueLattice(((AbstractIntegerLattice) var.getInnerElement()).makeLessOrEqualThan(constant)));
				else 
					return env;

				return copy;
			} 
		}
		
		return env;
	}

	@Override
	public ValueEnvironment mkEmptyEnvironment() {
		return new ValueEnvironment(booleanSingleton, intSingleton, stringSingleton);
	}

	@Override
	protected Fixpoint<ValueLattice, ValueEnvironment> mkFixpoint(MCodeBlock code) {
		return new FixpointImpl<>(code, WIDENING_THRESHOLD);
	}

}
