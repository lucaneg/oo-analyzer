package it.lucaneg.oo.ast.expression.logical;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.BooleanType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Not extends Expression {

	private final Expression expression;
	
	public Not(String source, int line, int pos, Expression expression) {
		super(source, line, pos);
		this.expression = expression;
	}

	@Override
	public String toString() {
		return "!" + getExpression();
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		expression.mustBeBoolean(helper);
		return BooleanType.INSTANCE;
	}

	public Expression simplify() {
		if (expression instanceof Not)
			return ((Not) expression).getExpression();
		return this;
	}
}