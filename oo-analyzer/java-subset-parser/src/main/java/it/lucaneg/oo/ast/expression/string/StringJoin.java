package it.lucaneg.oo.ast.expression.string;

import it.lucaneg.oo.ast.expression.BinaryExpression;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class StringJoin extends BinaryExpression {

	public StringJoin(String source, int line, int pos, Expression left, Expression right) {
		super(source, line, pos, left, right);
	}

	@Override
	public String toString() {
		return getLeft() + " + " + getRight();
	}

	@Override
	protected Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		Type leftType = getLeft().computeExpressionType(helper);
		Type rightType = getRight().computeExpressionType(helper);

		if (leftType != Type.getStringType()) 
			typeCheckError("first argument is not string");
		if (rightType != Type.getStringType())
			typeCheckError("second argument is not string");

		return Type.getStringType();
	}
}