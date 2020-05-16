package it.lucaneg.oo.analyzer.analyses.bool;

import it.lucaneg.oo.ast.expression.comparison.Equal;
import it.lucaneg.oo.ast.expression.comparison.NotEqual;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractSatisfiabilityEvaluator;

public class BooleanSatisfiabilityEvaluator extends AbstractSatisfiabilityEvaluator {

	@Override
	protected Satisfiability satisfiesNotEqual(NotEqual e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		BooleanLattice left = (BooleanLattice) evaluator.eval(e.getLeft(), env);
		BooleanLattice right = (BooleanLattice) evaluator.eval(e.getRight(), env);
		
		if (left.isTop() || right.isTop())
			return Satisfiability.UNKNOWN;
		
		if (left.isBottom() || right.isBottom())
			return Satisfiability.UNKNOWN;
		
		if (left == right)
			return Satisfiability.SATISFIED;
		
		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesEqual(Equal e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		BooleanLattice left = (BooleanLattice) evaluator.eval(e.getLeft(), env);
		BooleanLattice right = (BooleanLattice) evaluator.eval(e.getRight(), env);
		
		if (left.isTop() || right.isTop())
			return Satisfiability.UNKNOWN;
		
		if (left.isBottom() || right.isBottom())
			return Satisfiability.UNKNOWN;
		
		if (left == right)
			return Satisfiability.SATISFIED;
		
		return Satisfiability.NOT_SATISFIED;
	}
}
