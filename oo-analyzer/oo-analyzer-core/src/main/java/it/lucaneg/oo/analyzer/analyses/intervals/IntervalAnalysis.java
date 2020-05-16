package it.lucaneg.oo.analyzer.analyses.intervals;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractAnalysis;
import it.lucaneg.oo.sdk.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.sdk.analyzer.fixpoint.IterationBasedFixpoint;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;
import it.lucaneg.oo.sdk.analyzer.program.instructions.ILocalWrite;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

public class IntervalAnalysis extends AbstractAnalysis<IntervalLattice, IntervalEnvironment> {

	private final IntervalExpressionEvaluator evaluator = new IntervalExpressionEvaluator();

	private final IntervalSatisfiabilityEvaluator satisfiability = new IntervalSatisfiabilityEvaluator();
	
	@Override
	public IntervalExpressionEvaluator getExpressionEvaluator() {
		return evaluator;
	}
	
	@Override
	public IntervalSatisfiabilityEvaluator getSatisfiabilityEvaluator() {
		return satisfiability;
	}
	
	@Override
	public IntervalEnvironment smallStepSemantics(Statement st, IntervalEnvironment env) {
		if (st instanceof ILocalWrite) 
			return handleLocalWrite(st, env);
		else
			// TODO handle more statements
			return env;
	}
	
	private IntervalEnvironment handleLocalWrite(Statement st, IntervalEnvironment env) {
		ILocalWrite varOperation = (ILocalWrite) st;
		IntervalEnvironment result = env.copy();
		
		if (varOperation.getVariable().getType() != IntType.INSTANCE)
			result.set(varOperation.getVariable(), IntervalLattice.getBottom());
		else 
			// the value gets always overwritten
			result.set(varOperation.getVariable(), evaluator.eval(varOperation.getExpression(), env));
		
		return result;
	}

	@Override
	public IntervalEnvironment assume(Expression expr, IntervalEnvironment env) {
		return env; // TODO
	}

	@Override
	public IntervalEnvironment mkEmptyEnvironment() {
		return new IntervalEnvironment();
	}
	
	@Override
	protected Fixpoint<IntervalLattice, IntervalEnvironment> mkFixpoint(MCodeBlock code) {
		return new IterationBasedFixpoint<>(code, 5);
	}
}
