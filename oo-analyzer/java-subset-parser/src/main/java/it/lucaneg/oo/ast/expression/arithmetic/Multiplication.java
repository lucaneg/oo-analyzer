package it.lucaneg.oo.ast.expression.arithmetic;

import it.lucaneg.oo.ast.expression.Expression;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Multiplication extends ArithmeticBinaryExpression {

	public Multiplication(String source, int line, int pos, Expression left, Expression right) {
		super(source, line, pos, left, right);
	}

	@Override
	public String toString() {
		return getLeft() + " * " + getRight();
	}
}