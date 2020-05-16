package it.lucaneg.oo.analyzer.analyses.intPropagation;

import java.util.Collection;

import org.apache.commons.lang3.tuple.Pair;

import it.lucaneg.oo.analyzer.util.ExpressionUtils;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractAnalysis;
import it.lucaneg.oo.sdk.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.sdk.analyzer.fixpoint.IterationBasedFixpoint;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;
import it.lucaneg.oo.sdk.analyzer.program.instructions.ILocalWrite;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

public class IntPropagationAnalysis extends AbstractAnalysis<IntPropagationLattice, IntPropagationEnvironment> {

	private final IntPropagationExpressionEvaluator evaluator = new IntPropagationExpressionEvaluator();

	private final IntPropagationSatisfiabilityEvaluator satisfiability = new IntPropagationSatisfiabilityEvaluator();
	
	@Override
	public ExpressionEvaluator<IntPropagationLattice> getExpressionEvaluator() {
		return evaluator;
	}
	
	@Override
	public SatisfiabilityEvaluator getSatisfiabilityEvaluator() {
		return satisfiability;
	}
	
	@Override
	public IntPropagationEnvironment smallStepSemantics(Statement st, IntPropagationEnvironment env) {
		if (st instanceof ILocalWrite) 
			return handleLocalWrite(st, env);
		else
			// TODO handle more statements
			return env;
	}
	
	private IntPropagationEnvironment handleLocalWrite(Statement st, IntPropagationEnvironment env) {
		ILocalWrite varOperation = (ILocalWrite) st;
		IntPropagationEnvironment result = env.copy();
		
		if (varOperation.getVariable().getType() != IntType.INSTANCE)
			result.set(varOperation.getVariable(), IntPropagationLattice.getBottom());
		else 
			// the value gets always overwritten
			result.set(varOperation.getVariable(), evaluator.eval(varOperation.getExpression(), env));
		
		return result;
	}

	@Override
	public IntPropagationEnvironment assume(Expression expr, IntPropagationEnvironment env) {
		if (satisfiability.satisfies(expr, env, evaluator) == Satisfiability.SATISFIED)
			return env;
		
		Collection<String> vars = ExpressionUtils.getAllVariablesNames(expr);

		IntPropagationEnvironment result = env.except(vars), temp;
		if (satisfiability.satisfies(expr, env, evaluator) == Satisfiability.NOT_SATISFIED)
			// the expression can never be satisfied
			return mkEmptyEnvironment();
		
		for (Pair<MLocalVariable, IntPropagationLattice> approx : env) {
			// we re-add all variables that do not violate the expression
			// TODO problem: i < j with i = 10 and j = 1. Depending on the order
			// of processing, one variable will be arbitrarily picked over the other
			temp = result.copy();
			temp.set(approx.getKey(), approx.getValue());
			if (satisfiability.satisfies(expr, temp, evaluator) != Satisfiability.NOT_SATISFIED)
				result.set(approx.getKey(), approx.getValue()); 
		}
		
		return result;
	}

	@Override
	public IntPropagationEnvironment mkEmptyEnvironment() {
		return new IntPropagationEnvironment();
	}
	
	@Override
	protected Fixpoint<IntPropagationLattice, IntPropagationEnvironment> mkFixpoint(MCodeBlock code) {
		return new IterationBasedFixpoint<>(code, 0);
	}
}
