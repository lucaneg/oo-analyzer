package it.lucaneg.oo.ast.expression.comparison;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.BooleanType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class EqualityComparisonExpression extends ComparisonBinaryExpression {

	public EqualityComparisonExpression(String source, int line, int pos, Expression left, Expression right) {
		super(source, line, pos, left, right);
	}

	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		Type leftType = getLeft().computeExpressionType(helper);
		Type rightType = getRight().computeExpressionType(helper);

		// we must be able to assign the left-hand side to the right-hand side or vice versa
		if (!leftType.canBeAssignedTo(rightType) && !rightType.canBeAssignedTo(leftType))
			typeCheckError("the arguments are of incompatible types");

		// the result is always boolean
		return BooleanType.INSTANCE;
	}

}
