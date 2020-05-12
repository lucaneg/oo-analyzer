package it.lucaneg.oo.ast.expression.arithmetic;

import it.lucaneg.oo.ast.expression.BinaryExpression;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.FloatType;
import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class ArithmeticBinaryExpression extends BinaryExpression {

	public ArithmeticBinaryExpression(String source, int line, int pos, Expression left, Expression right) {
		super(source, line, pos, left, right);
	}

	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		Type leftType = getLeft().computeExpressionType(helper);
		Type rightType = getRight().computeExpressionType(helper);

		if (leftType != IntType.INSTANCE && leftType != FloatType.INSTANCE) 
			typeCheckError("first argument is not numerical");
		if (rightType != IntType.INSTANCE && rightType != FloatType.INSTANCE)
			typeCheckError("second argument is not numerical");
		
		return leftType.leastCommonSupertype(rightType);
	}
}