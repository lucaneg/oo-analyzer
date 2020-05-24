package it.lucaneg.oo.ast.expression.arithmetic;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.FloatType;
import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Minus extends Expression {

	private Expression expression;
	
	public Minus(String source, int line, int pos, Expression expression) {
		super(source, line, pos);
		this.expression = expression;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return "-" + getExpression();
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		Type type = expression.computeExpressionType(helper);

		if (type != IntType.INSTANCE && type != FloatType.INSTANCE)
			typeCheckError("integer or float expected");

		return type;
	}

	@Override
	public Expression transformStringJoins(CheckerHelper helper) {
		expression = expression.transformStringJoins(helper);
		return this;
	}
}