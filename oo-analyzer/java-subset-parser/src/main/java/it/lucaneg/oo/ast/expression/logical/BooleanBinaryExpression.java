package it.lucaneg.oo.ast.expression.logical;

import it.lucaneg.oo.ast.expression.BinaryExpression;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.BooleanType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class BooleanBinaryExpression extends BinaryExpression {
	
	public BooleanBinaryExpression(String source, int line, int pos, Expression left, Expression right) {
		super(source, line, pos, left, right);
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		getLeft().mustBeBoolean(helper);
		getRight().mustBeBoolean(helper);
		return BooleanType.INSTANCE;
	}

}