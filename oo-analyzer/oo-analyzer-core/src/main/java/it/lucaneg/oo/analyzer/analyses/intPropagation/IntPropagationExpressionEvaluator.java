package it.lucaneg.oo.analyzer.analyses.intPropagation;

import static it.lucaneg.oo.analyzer.analyses.intPropagation.IntPropagationLattice.getBottom;
import static it.lucaneg.oo.analyzer.analyses.intPropagation.IntPropagationLattice.getTop;

import it.lucaneg.oo.ast.expression.arithmetic.Addition;
import it.lucaneg.oo.ast.expression.arithmetic.Division;
import it.lucaneg.oo.ast.expression.arithmetic.Minus;
import it.lucaneg.oo.ast.expression.arithmetic.Module;
import it.lucaneg.oo.ast.expression.arithmetic.Multiplication;
import it.lucaneg.oo.ast.expression.arithmetic.Subtraction;
import it.lucaneg.oo.ast.expression.literal.IntLiteral;
import it.lucaneg.oo.ast.expression.literal.Literal;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractExpressionEvaluator;

public class IntPropagationExpressionEvaluator extends AbstractExpressionEvaluator<IntPropagationLattice> {

	@Override
	protected IntPropagationLattice evalLiteral(Literal literal, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		if (literal instanceof IntLiteral)
			return new IntPropagationLattice(((IntLiteral) literal).getValue());
		
		return (IntPropagationLattice) parent.eval(literal, env);
	}
	
	@Override
	protected IntPropagationLattice evalMinus(Minus minus, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		IntPropagationLattice eval = eval(minus.getExpression(), env, parent);
		if (eval != getBottom() && eval != getTop())
			return eval.multiply(new IntPropagationLattice(-1));
		
		return eval;
	}

	@Override
	protected IntPropagationLattice evalAddition(Addition add, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		IntPropagationLattice left = eval(add.getLeft(), env);
		IntPropagationLattice right = eval(add.getRight(), env);
		return left.plus(right);
	}
	
	@Override
	protected IntPropagationLattice evalSubtraction(Subtraction sub, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		IntPropagationLattice left = eval(sub.getLeft(), env);
		IntPropagationLattice right = eval(sub.getRight(), env);
		return left.minus(right);
	}
	
	@Override
	protected IntPropagationLattice evalDivision(Division div, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		IntPropagationLattice left = eval(div.getLeft(), env);
		IntPropagationLattice right = eval(div.getRight(), env);
		return left.divide(right);
	}
	
	@Override
	protected IntPropagationLattice evalMultiplication(Multiplication mul, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		IntPropagationLattice left = eval(mul.getLeft(), env);
		IntPropagationLattice right = eval(mul.getRight(), env);
		return left.multiply(right);
	}
	
	@Override
	protected IntPropagationLattice evalModule(Module mod, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		IntPropagationLattice left = eval(mod.getLeft(), env);
		IntPropagationLattice right = eval(mod.getRight(), env);
		return left.module(right);
	}
}
