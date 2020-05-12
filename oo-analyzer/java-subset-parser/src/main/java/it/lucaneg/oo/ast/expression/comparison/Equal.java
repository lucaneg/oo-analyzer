package it.lucaneg.oo.ast.expression.comparison;

import it.lucaneg.oo.ast.expression.Expression;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Equal extends EqualityComparisonExpression {

	public Equal(String source, int line, int pos, Expression left, Expression right) {
		super(source, line, pos, left, right);
	}

	@Override
	public String toString() {
		return getLeft() + " == " + getRight();
	}
}