package it.lucaneg.oo.ast.expression.comparison;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.expression.NumericalComparisonExpression;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class GreaterOrEqual extends NumericalComparisonExpression {

	public GreaterOrEqual(String source, int line, int pos, Expression left, Expression right) {
		super(source, line, pos, left, right);
	}

	@Override
	public String toString() {
		return getLeft() + " >= " + getRight();
	}
	
	public Less flip() {
		Less lt = new Less(getSource(), getLine(), getPos(), getLeft(), getRight());
		lt.cloneStaticType(this);
		return lt;
	}
}