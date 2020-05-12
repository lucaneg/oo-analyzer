package it.luceng.oo.analyzer.analyses.intPropagation;

import static it.luceng.oo.analyzer.analyses.intPropagation.IntPropagationLattice.getBottom;
import static it.luceng.oo.analyzer.analyses.intPropagation.IntPropagationLattice.getTop;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.api.analyzer.analyses.impl.AbstractEnvironment;
import it.lucaneg.oo.ast.expression.arithmetic.Addition;
import it.lucaneg.oo.ast.expression.arithmetic.Division;
import it.lucaneg.oo.ast.expression.arithmetic.Minus;
import it.lucaneg.oo.ast.expression.arithmetic.Module;
import it.lucaneg.oo.ast.expression.arithmetic.Multiplication;
import it.lucaneg.oo.ast.expression.arithmetic.Subtraction;
import it.lucaneg.oo.ast.expression.comparison.ComparisonBinaryExpression;
import it.lucaneg.oo.ast.expression.comparison.Equal;
import it.lucaneg.oo.ast.expression.comparison.Greater;
import it.lucaneg.oo.ast.expression.comparison.GreaterOrEqual;
import it.lucaneg.oo.ast.expression.comparison.Less;
import it.lucaneg.oo.ast.expression.comparison.LessOrEqual;
import it.lucaneg.oo.ast.expression.comparison.NotEqual;
import it.lucaneg.oo.ast.expression.literal.IntLiteral;
import it.lucaneg.oo.ast.expression.literal.Literal;
import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.ast.types.Type;

public class IntPropagationEnvironment extends AbstractEnvironment<IntPropagationLattice, IntPropagationEnvironment> {
	
	private static final EnrichedLogger logger = new EnrichedLogger(IntPropagationEnvironment.class);

	public IntPropagationEnvironment() {
	}
	
	private IntPropagationEnvironment(IntPropagationEnvironment other) {
		super(other.approximations);
	}
	
	@Override
	protected Satisfiability satisfiesComparisonBinaryExpression(ComparisonBinaryExpression e) {
		IntPropagationLattice left = eval(((ComparisonBinaryExpression) e).getLeft());
		IntPropagationLattice right = eval(((ComparisonBinaryExpression) e).getRight());
		
		if (left == getTop() || right == getTop() || left == getBottom() || right == getBottom())
			// TODO sure about this? 
			return Satisfiability.UNKNOWN;
		
		if (e instanceof Equal)
			return Satisfiability.fromBoolean(left.compare(right) == 0);
		else if (e instanceof NotEqual)
			return Satisfiability.fromBoolean(left.compare(right) != 0);
		else if (e instanceof Greater)
			return Satisfiability.fromBoolean(left.compare(right) > 0);
		else if (e instanceof GreaterOrEqual)
			return Satisfiability.fromBoolean(left.compare(right) >= 0);
		else if (e instanceof Less)
			return Satisfiability.fromBoolean(left.compare(right) < 0);
		else if (e instanceof LessOrEqual)
			return Satisfiability.fromBoolean(left.compare(right) <= 0);
		
		logger.warn("Unknown arithmetic operation: " + e.getClass().getName()); 
		return Satisfiability.UNKNOWN;
	}
	
	@Override
	protected IntPropagationLattice evalLiteral(Literal literal) {
		if (literal instanceof IntLiteral)
			return new IntPropagationLattice(((IntLiteral) literal).getValue());
		
		return super.evalLiteral(literal);
	}
	
	@Override
	protected IntPropagationLattice evalMinus(Minus minus) {
		IntPropagationLattice eval = eval(minus.getExpression());
		if (eval != getBottom() && eval != getTop())
			return eval.multiply(new IntPropagationLattice(-1));
		
		return eval;
	}

	@Override
	protected IntPropagationLattice evalAddition(Addition add) {
		IntPropagationLattice left = eval(add.getLeft());
		IntPropagationLattice right = eval(add.getRight());
		return left.plus(right);
	}
	
	@Override
	protected IntPropagationLattice evalSubtraction(Subtraction sub) {
		IntPropagationLattice left = eval(sub.getLeft());
		IntPropagationLattice right = eval(sub.getRight());
		return left.minus(right);
	}
	
	@Override
	protected IntPropagationLattice evalDivision(Division div) {
		IntPropagationLattice left = eval(div.getLeft());
		IntPropagationLattice right = eval(div.getRight());
		return left.divide(right);
	}
	
	@Override
	protected IntPropagationLattice evalMultiplication(Multiplication mul) {
		IntPropagationLattice left = eval(mul.getLeft());
		IntPropagationLattice right = eval(mul.getRight());
		return left.multiply(right);
	}
	
	@Override
	protected IntPropagationLattice evalModule(Module mod) {
		IntPropagationLattice left = eval(mod.getLeft());
		IntPropagationLattice right = eval(mod.getRight());
		return left.module(right);
	}
	
	@Override
	public IntPropagationEnvironment copy() {
		return new IntPropagationEnvironment(this);
	}
	
	@Override
	protected IntPropagationLattice latticeBottom() {
		return IntPropagationLattice.getBottom();
	}
	
	@Override
	protected IntPropagationLattice latticeTop() {
		return IntPropagationLattice.getTop();
	}
	
	@Override
	protected IntPropagationLattice defaultLatticeForType(Type t) {
		return t == IntType.INSTANCE ? latticeTop() : latticeBottom();
	}
}
