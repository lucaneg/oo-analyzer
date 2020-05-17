package it.lucaneg.oo.analyzer.analyses.value;

import java.lang.reflect.InvocationTargetException;

import it.lucaneg.oo.analyzer.analyses.value.domains.bools.AbstractBooleanLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.strings.AbstractStringLattice;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractAnalysis;
import it.lucaneg.oo.sdk.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.sdk.analyzer.fixpoint.IterationBasedFixpoint;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;
import it.lucaneg.oo.sdk.analyzer.program.instructions.ILocalWrite;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

public class ValueAnalysis extends AbstractAnalysis<ValueLattice, ValueEnvironment> {

	private static final int WIDENING_THRESHOLD = 5;

	private ValueExpressionEvaluator evaluator = new ValueExpressionEvaluator();

	private ValueSatisfiabilityEvaluator satisfiability = new ValueSatisfiabilityEvaluator();
	
	private AbstractBooleanLattice<?> booleanSingleton;

	private AbstractIntegerLattice<?> intSingleton;

	private AbstractStringLattice<?> stringSingleton;

	public void setDomains(Class<? extends AbstractBooleanLattice<?>> booleans, 
			Class<? extends AbstractIntegerLattice<?>> ints, 
			Class<? extends AbstractStringLattice<?>> strings) throws AnalysisException {
		
		try {
			booleanSingleton = (AbstractBooleanLattice<?>) booleans.getMethod("getTop").invoke(null);
			intSingleton = (AbstractIntegerLattice<?>) booleans.getMethod("getTop").invoke(null);
			stringSingleton = (AbstractStringLattice<?>) booleans.getMethod("getTop").invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new AnalysisException("Unable to setup value analysis", e);
		}
		
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

	@Override
	public ValueEnvironment assume(Expression expr, ValueEnvironment env) {
		return env; // TODO
	}

	@Override
	public ValueEnvironment mkEmptyEnvironment() {
		return new ValueEnvironment(booleanSingleton, intSingleton, stringSingleton);
	}

	@Override
	protected Fixpoint<ValueLattice, ValueEnvironment> mkFixpoint(MCodeBlock code) {
		return new IterationBasedFixpoint<>(code, WIDENING_THRESHOLD);
	}

}
