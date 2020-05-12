package it.lucaneg.oo.ast.expression.comparison;

import it.lucaneg.oo.ast.expression.BinaryExpression;
import it.lucaneg.oo.ast.expression.Expression;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class ComparisonBinaryExpression extends BinaryExpression {
	
	public ComparisonBinaryExpression(String source, int line, int pos, Expression left, Expression right) {
		super(source, line, pos, left, right);
	}

}