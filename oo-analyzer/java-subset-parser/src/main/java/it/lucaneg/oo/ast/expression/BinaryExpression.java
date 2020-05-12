package it.lucaneg.oo.ast.expression;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class BinaryExpression extends Expression {

	private final Expression left;

	private final Expression right;

	public BinaryExpression(String source, int line, int pos, Expression left, Expression right) {
		super(source, line, pos);
		this.left = left;
		this.right = right;
	}
}