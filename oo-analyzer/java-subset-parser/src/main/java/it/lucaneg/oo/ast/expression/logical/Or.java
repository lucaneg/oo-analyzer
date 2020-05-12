package it.lucaneg.oo.ast.expression.logical;

import it.lucaneg.oo.ast.expression.Expression;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Or extends BooleanBinaryExpression {
	
	public Or(String source, int line, int pos, Expression left, Expression right) {
		super(source, line, pos, left, right);
	}

	@Override
	public String toString() {
		return getLeft() + " || " + getRight();
	}
}