package it.lucaneg.oo.ast.expression.comparison;

import it.lucaneg.oo.ast.expression.Expression;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class NotEqual extends EqualityComparisonExpression {

	public NotEqual(String source, int line, int pos, Expression left, Expression right) {
		super(source, line, pos, left, right);
	}

	@Override
	public String toString() {
		return getLeft() + " != " + getRight();
	}
	
	public Equal flip() {
		Equal eq = new Equal(getSource(), getLine(), getPos(), getLeft(), getRight());
		eq.cloneStaticType(this);
		return eq;
	}
}