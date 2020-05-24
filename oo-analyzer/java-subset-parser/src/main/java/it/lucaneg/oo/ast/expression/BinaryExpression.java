package it.lucaneg.oo.ast.expression;

import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class BinaryExpression extends Expression {

	private Expression left;

	private Expression right;

	public BinaryExpression(String source, int line, int pos, Expression left, Expression right) {
		super(source, line, pos);
		this.left = left;
		this.right = right;
	}

	@Override
	public Expression transformStringJoins(CheckerHelper helper) {
		left = left.transformStringJoins(helper);
		right = right.transformStringJoins(helper);
		return this;
	}
}