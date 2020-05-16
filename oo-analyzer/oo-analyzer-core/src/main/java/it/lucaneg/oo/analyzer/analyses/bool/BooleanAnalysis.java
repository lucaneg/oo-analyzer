package it.lucaneg.oo.analyzer.analyses.bool;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractAnalysis;
import it.lucaneg.oo.sdk.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.sdk.analyzer.fixpoint.IterationBasedFixpoint;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;
import it.lucaneg.oo.sdk.analyzer.program.instructions.ILocalWrite;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

public class BooleanAnalysis extends AbstractAnalysis<BooleanLattice, BooleanEnvironment> {

	private final BooleanExpressionEvaluator evaluator = new BooleanExpressionEvaluator();

	private final BooleanSatisfiabilityEvaluator satisfiability = new BooleanSatisfiabilityEvaluator();
	
	@Override
	public BooleanExpressionEvaluator getExpressionEvaluator() {
		return evaluator;
	}
	
	@Override
	public BooleanSatisfiabilityEvaluator getSatisfiabilityEvaluator() {
		return satisfiability;
	}
	
	@Override
	public BooleanEnvironment smallStepSemantics(Statement st, BooleanEnvironment env) {
		if (st instanceof ILocalWrite) 
			return handleLocalWrite(st, env);
		else
			// TODO handle more statements
			return env;
	}
	
	private BooleanEnvironment handleLocalWrite(Statement st, BooleanEnvironment env) {
		ILocalWrite varOperation = (ILocalWrite) st;
		BooleanEnvironment result = env.copy();
		
		if (varOperation.getVariable().getType() != IntType.INSTANCE)
			result.set(varOperation.getVariable(), BooleanLattice.getBottom());
		else 
			// the value gets always overwritten
			result.set(varOperation.getVariable(), evaluator.eval(varOperation.getExpression(), env));
		
		return result;
	}

	@Override
	public BooleanEnvironment assume(Expression expr, BooleanEnvironment env) {
		return env; // TODO
	}

	@Override
	public BooleanEnvironment mkEmptyEnvironment() {
		return new BooleanEnvironment();
	}
	
	@Override
	protected Fixpoint<BooleanLattice, BooleanEnvironment> mkFixpoint(MCodeBlock code) {
		return new IterationBasedFixpoint<>(code, 5);
	}
}
