package it.lucaneg.oo.ast.expression;

import it.lucaneg.oo.ast.expression.comparison.ComparisonBinaryExpression;
import it.lucaneg.oo.ast.types.BooleanType;
import it.lucaneg.oo.ast.types.FloatType;
import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;

public abstract class NumericalComparisonExpression extends ComparisonBinaryExpression {

	public NumericalComparisonExpression(String source, int line, int pos, Expression left, Expression right) {
		super(source, line, pos, left, right);
	}

	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		Type leftType = getLeft().computeExpressionType(helper);
		Type rightType = getRight().computeExpressionType(helper);

		if ((leftType != IntType.INSTANCE && leftType != FloatType.INSTANCE	) ||
				(rightType != IntType.INSTANCE && rightType != FloatType.INSTANCE))
			typeCheckError("numerical arguments required");

		return BooleanType.INSTANCE;
	}
}
