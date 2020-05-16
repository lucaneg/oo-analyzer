package it.lucaneg.oo.analyzer.analyses.intervals;

import it.lucaneg.oo.ast.expression.comparison.Equal;
import it.lucaneg.oo.ast.expression.comparison.Greater;
import it.lucaneg.oo.ast.expression.comparison.Less;
import it.lucaneg.oo.ast.expression.comparison.NotEqual;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractSatisfiabilityEvaluator;

public class IntervalSatisfiabilityEvaluator extends AbstractSatisfiabilityEvaluator {

	@Override
	protected Satisfiability satisfiesLess(Less e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN; // TODO
	}

	@Override
	protected Satisfiability satisfiesGreater(Greater e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN; // TODO
	}

	@Override
	protected Satisfiability satisfiesNotEqual(NotEqual e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		IntervalLattice left = (IntervalLattice) evaluator.eval(e.getLeft(), env);
		IntervalLattice right = (IntervalLattice) evaluator.eval(e.getRight(), env);
		
		if (left.isTop() || right.isTop())
			return Satisfiability.UNKNOWN;
		
		if (left.isBottom() || right.isBottom())
			return Satisfiability.UNKNOWN;
		
		if (!left.equals(right))
			return Satisfiability.SATISFIED;
		
		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesEqual(Equal e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		IntervalLattice left = (IntervalLattice) evaluator.eval(e.getLeft(), env);
		IntervalLattice right = (IntervalLattice) evaluator.eval(e.getRight(), env);
		
		if (left.isTop() || right.isTop())
			return Satisfiability.UNKNOWN;
		
		if (left.isBottom() || right.isBottom())
			return Satisfiability.UNKNOWN;
		
		if (left.equals(right))
			return Satisfiability.SATISFIED;
		
		return Satisfiability.NOT_SATISFIED;
	}
}
