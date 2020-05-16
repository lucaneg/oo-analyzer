package it.lucaneg.oo.analyzer.analyses.bool;

import it.lucaneg.oo.ast.expression.comparison.Equal;
import it.lucaneg.oo.ast.expression.comparison.Greater;
import it.lucaneg.oo.ast.expression.comparison.GreaterOrEqual;
import it.lucaneg.oo.ast.expression.comparison.Less;
import it.lucaneg.oo.ast.expression.comparison.LessOrEqual;
import it.lucaneg.oo.ast.expression.comparison.NotEqual;
import it.lucaneg.oo.ast.expression.literal.FalseLiteral;
import it.lucaneg.oo.ast.expression.literal.Literal;
import it.lucaneg.oo.ast.expression.literal.TrueLiteral;
import it.lucaneg.oo.ast.expression.logical.And;
import it.lucaneg.oo.ast.expression.logical.Not;
import it.lucaneg.oo.ast.expression.logical.Or;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractLattice;

public class BooleanExpressionEvaluator extends AbstractExpressionEvaluator<BooleanLattice> {

	@Override
	protected BooleanLattice evalNot(Not not, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		BooleanLattice eval = (BooleanLattice) parent.eval(not, env);
		if (eval.isBottom() || eval.isTop())
			return eval;
		return eval == BooleanLattice.getTrue() ? BooleanLattice.getFalse() : BooleanLattice.getTrue();
	}

	@Override
	protected BooleanLattice evalLiteral(Literal literal, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		if (literal instanceof FalseLiteral)
			return BooleanLattice.getFalse();
		
		if (literal instanceof TrueLiteral)
			return BooleanLattice.getTrue();

		return BooleanLattice.getBottom();
	}

	@Override
	protected BooleanLattice evalAnd(And and, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		BooleanLattice left = (BooleanLattice) parent.eval(and.getLeft(), env);
		BooleanLattice right = (BooleanLattice) parent.eval(and.getRight(), env);
		
		if (left.isTop() || right.isTop())
			return BooleanLattice.getTop();
		
		if (left.isBottom())
			return right;
		
		if (right.isBottom())
			return left;
		
		if (left == BooleanLattice.getFalse() || right == BooleanLattice.getFalse())
			return BooleanLattice.getFalse();
		
		return BooleanLattice.getTrue();
	}

	@Override
	protected BooleanLattice evalOr(Or or, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		BooleanLattice left = (BooleanLattice) parent.eval(or.getLeft(), env);
		BooleanLattice right = (BooleanLattice) parent.eval(or.getRight(), env);
		
		if (left.isTop() || right.isTop())
			return BooleanLattice.getTop();
		
		if (left.isBottom())
			return right;
		
		if (right.isBottom())
			return left;
		
		if (left == BooleanLattice.getFalse() && right == BooleanLattice.getFalse())
			return BooleanLattice.getFalse();
		
		return BooleanLattice.getTrue();
	}

	@Override
	protected BooleanLattice evalEqual(Equal eq, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		BooleanLattice left = (BooleanLattice) parent.eval(eq.getLeft(), env);
		BooleanLattice right = (BooleanLattice) parent.eval(eq.getRight(), env);
		
		if (left.isTop() || right.isTop())
			return BooleanLattice.getTop();
		
		if (left.isBottom() || right.isBottom())
			return BooleanLattice.getBottom();
		
		Boolean res = left.isEqualTo(right);
		if (res == null)
			return BooleanLattice.getTop();
		if (res)
			return BooleanLattice.getTrue();
		
		return BooleanLattice.getFalse();
	}

	@Override
	protected BooleanLattice evalNotEqual(NotEqual ne, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		AbstractLattice<?> left = (AbstractLattice<?>) parent.eval(ne.getLeft(), env);
		AbstractLattice<?> right = (AbstractLattice<?>) parent.eval(ne.getRight(), env);
		
		if (left.isTop() || right.isTop())
			return BooleanLattice.getTop();
		
		if (left.isBottom() || right.isBottom())
			return BooleanLattice.getBottom();
		
		Boolean res = left.isEqualTo(right);
		if (res == null)
			return BooleanLattice.getTop();
		if (!res)
			return BooleanLattice.getTrue();
		
		return BooleanLattice.getFalse();
	}
	
	@Override
	protected BooleanLattice evalGreater(Greater gt, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		AbstractLattice<?> left = (AbstractLattice<?>) parent.eval(gt.getLeft(), env);
		AbstractLattice<?> right = (AbstractLattice<?>) parent.eval(gt.getRight(), env);
		
		if (left.isTop() || right.isTop())
			return BooleanLattice.getTop();
		
		if (left.isBottom() || right.isBottom())
			return BooleanLattice.getBottom();
		
		Boolean res = left.isGreaterThan(right);
		if (res == null)
			return BooleanLattice.getTop();
		if (res)
			return BooleanLattice.getTrue();
		
		return BooleanLattice.getFalse();
	}
	
	@Override
	protected BooleanLattice evalGreaterOrEqual(GreaterOrEqual ge, Environment<?, ?> env,
			ExpressionEvaluator<?> parent) {
		AbstractLattice<?> left = (AbstractLattice<?>) parent.eval(ge.getLeft(), env);
		AbstractLattice<?> right = (AbstractLattice<?>) parent.eval(ge.getRight(), env);
		
		if (left.isTop() || right.isTop())
			return BooleanLattice.getTop();
		
		if (left.isBottom() || right.isBottom())
			return BooleanLattice.getBottom();
		
		Boolean res = left.isLessThen(right);
		if (res == null)
			return BooleanLattice.getTop();
		if (!res)
			return BooleanLattice.getTrue();
		
		return BooleanLattice.getFalse();
	}
	
	@Override
	protected BooleanLattice evalLess(Less lt, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		AbstractLattice<?> left = (AbstractLattice<?>) parent.eval(lt.getLeft(), env);
		AbstractLattice<?> right = (AbstractLattice<?>) parent.eval(lt.getRight(), env);
		
		if (left.isTop() || right.isTop())
			return BooleanLattice.getTop();
		
		if (left.isBottom() || right.isBottom())
			return BooleanLattice.getBottom();
		
		Boolean res = left.isLessThen(right);
		if (res == null)
			return BooleanLattice.getTop();
		if (res)
			return BooleanLattice.getTrue();
		
		return BooleanLattice.getFalse();
	}
	
	@Override
	protected BooleanLattice evalLessOrEqual(LessOrEqual le, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		AbstractLattice<?> left = (AbstractLattice<?>) parent.eval(le.getLeft(), env);
		AbstractLattice<?> right = (AbstractLattice<?>) parent.eval(le.getRight(), env);
		
		if (left.isTop() || right.isTop())
			return BooleanLattice.getTop();
		
		if (left.isBottom() || right.isBottom())
			return BooleanLattice.getBottom();
		
		Boolean res = left.isGreaterThan(right);
		if (res == null)
			return BooleanLattice.getTop();
		if (!res)
			return BooleanLattice.getTrue();
		
		return BooleanLattice.getFalse();
	}
}
