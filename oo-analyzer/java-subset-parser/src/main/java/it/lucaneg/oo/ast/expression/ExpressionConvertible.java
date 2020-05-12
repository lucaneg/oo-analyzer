package it.lucaneg.oo.ast.expression;

public interface ExpressionConvertible {
	default Expression asExpression() {
		return this instanceof Expression ? (Expression) this : null;
	}
}
