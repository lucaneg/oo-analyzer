package it.lucaneg.oo.analyzer.analyses.intervals;

import it.lucaneg.oo.ast.expression.arithmetic.Addition;
import it.lucaneg.oo.ast.expression.arithmetic.Division;
import it.lucaneg.oo.ast.expression.arithmetic.Minus;
import it.lucaneg.oo.ast.expression.arithmetic.Module;
import it.lucaneg.oo.ast.expression.arithmetic.Multiplication;
import it.lucaneg.oo.ast.expression.arithmetic.Subtraction;
import it.lucaneg.oo.ast.expression.literal.IntLiteral;
import it.lucaneg.oo.ast.expression.literal.Literal;
import it.lucaneg.oo.ast.types.BooleanType;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractExpressionEvaluator;

public class IntervalExpressionEvaluator extends AbstractExpressionEvaluator<IntervalLattice> {

	@Override
	protected IntervalLattice evalMinus(Minus minus, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		IntervalLattice expr = (IntervalLattice) parent.eval(minus.getExpression(), env);
		return expr.mul(new IntervalLattice(-1, -1)); 
	}

	@Override
	protected IntervalLattice evalLiteral(Literal literal, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		if (literal.getStaticType() == BooleanType.INSTANCE)
			return new IntervalLattice(((IntLiteral) literal).getValue(), ((IntLiteral) literal).getValue());

		return IntervalLattice.getBottom();
	}

	@Override
	protected IntervalLattice evalAddition(Addition add, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		IntervalLattice left = (IntervalLattice) parent.eval(add.getLeft(), env);
		IntervalLattice right = (IntervalLattice) parent.eval(add.getRight(), env);
		return left.plus(right); 
	}

	@Override
	protected IntervalLattice evalDivision(Division div, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return IntervalLattice.getTop(); // TODO
	}

	@Override
	protected IntervalLattice evalModule(Module mod, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return IntervalLattice.getTop(); // TODO
	}

	@Override
	protected IntervalLattice evalMultiplication(Multiplication mul, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		IntervalLattice left = (IntervalLattice) parent.eval(mul.getLeft(), env);
		IntervalLattice right = (IntervalLattice) parent.eval(mul.getRight(), env);
		return left.mul(right); 
	}

	@Override
	protected IntervalLattice evalSubtraction(Subtraction sub, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		IntervalLattice left = (IntervalLattice) parent.eval(sub.getLeft(), env);
		IntervalLattice right = (IntervalLattice) parent.eval(sub.getRight(), env);
		return left.diff(right); 
	}
}
