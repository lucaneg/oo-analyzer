package it.lucaneg.oo.ast.expression.arithmetic;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.expression.string.StringJoin;
import it.lucaneg.oo.ast.types.FloatType;
import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.ast.types.ReferenceType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Addition extends ArithmeticBinaryExpression {

	public Addition(String source, int line, int pos, Expression left, Expression right) {
		super(source, line, pos, left, right);
	}

	@Override
	public String toString() {
		return getLeft() + " + " + getRight();
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		Type leftType = getLeft().computeExpressionType(helper);
		Type rightType = getRight().computeExpressionType(helper);
		
		if (leftType instanceof ReferenceType && rightType instanceof ReferenceType) {
			if (leftType != Type.getStringType()) 
				typeCheckError("first argument is not string");
			if (rightType != Type.getStringType())
				typeCheckError("second argument is not string");
	
			return Type.getStringType();
		}

		if (leftType != IntType.INSTANCE && leftType != FloatType.INSTANCE) 
			typeCheckError("first argument is not numerical");
		if (rightType != IntType.INSTANCE && rightType != FloatType.INSTANCE)
			typeCheckError("second argument is not numerical");
		
		return leftType.leastCommonSupertype(rightType);
	}

	@Override
	public Expression transformStringJoins(CheckerHelper helper) {
		super.transformStringJoins(helper); // propagate to left and right
		if (getStaticType() == Type.getStringType()) {
			StringJoin join = new StringJoin(getSource(), getLine(), getPos(), getLeft(), getRight());
			join.cloneStaticType(join);
			return join;
		}
		return this;
	}
}