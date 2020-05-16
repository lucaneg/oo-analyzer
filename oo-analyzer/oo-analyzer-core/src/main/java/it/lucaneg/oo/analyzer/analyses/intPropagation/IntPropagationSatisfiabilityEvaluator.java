package it.lucaneg.oo.analyzer.analyses.intPropagation;

import static it.lucaneg.oo.analyzer.analyses.intPropagation.IntPropagationLattice.getBottom;
import static it.lucaneg.oo.analyzer.analyses.intPropagation.IntPropagationLattice.getTop;

import it.lucaneg.oo.ast.expression.comparison.ComparisonBinaryExpression;
import it.lucaneg.oo.ast.expression.comparison.Equal;
import it.lucaneg.oo.ast.expression.comparison.Greater;
import it.lucaneg.oo.ast.expression.comparison.Less;
import it.lucaneg.oo.ast.expression.comparison.NotEqual;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractSatisfiabilityEvaluator;

public class IntPropagationSatisfiabilityEvaluator extends AbstractSatisfiabilityEvaluator {
	
	@Override
	protected Satisfiability satisfiesEqual(Equal e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		IntPropagationLattice left = (IntPropagationLattice) evaluator.eval(((ComparisonBinaryExpression) e).getLeft(), env);
		IntPropagationLattice right = (IntPropagationLattice) evaluator.eval(((ComparisonBinaryExpression) e).getRight(), env);
		
		if (left == getTop() || right == getTop() || left == getBottom() || right == getBottom())
			return Satisfiability.UNKNOWN;
		
		return Satisfiability.fromBoolean(left.compare(right) == 0);
	}
	
	@Override
	protected Satisfiability satisfiesGreater(Greater e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		IntPropagationLattice left = (IntPropagationLattice) evaluator.eval(((ComparisonBinaryExpression) e).getLeft(), env);
		IntPropagationLattice right = (IntPropagationLattice) evaluator.eval(((ComparisonBinaryExpression) e).getRight(), env);
		
		if (left == getTop() || right == getTop() || left == getBottom() || right == getBottom())
			return Satisfiability.UNKNOWN;
		
		return Satisfiability.fromBoolean(left.compare(right) > 0);
	}
	
	@Override
	protected Satisfiability satisfiesLess(Less e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		IntPropagationLattice left = (IntPropagationLattice) evaluator.eval(((ComparisonBinaryExpression) e).getLeft(), env);
		IntPropagationLattice right = (IntPropagationLattice) evaluator.eval(((ComparisonBinaryExpression) e).getRight(), env);
		
		if (left == getTop() || right == getTop() || left == getBottom() || right == getBottom())
			return Satisfiability.UNKNOWN;
		
		return Satisfiability.fromBoolean(left.compare(right) < 0);
	}
	
	@Override
	protected Satisfiability satisfiesNotEqual(NotEqual e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		IntPropagationLattice left = (IntPropagationLattice) evaluator.eval(((ComparisonBinaryExpression) e).getLeft(), env);
		IntPropagationLattice right = (IntPropagationLattice) evaluator.eval(((ComparisonBinaryExpression) e).getRight(), env);
		
		if (left == getTop() || right == getTop() || left == getBottom() || right == getBottom())
			return Satisfiability.UNKNOWN;
		
		return Satisfiability.fromBoolean(left.compare(right) != 0);
	}
}
