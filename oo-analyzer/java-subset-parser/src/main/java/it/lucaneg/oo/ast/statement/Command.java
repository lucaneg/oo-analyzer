package it.lucaneg.oo.ast.statement;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Command extends Statement {

	private Expression expression;

	public Command(String source, int line, int pos, Expression expression) {
		super(source, line, pos);
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		return "return " + getExpression();
	}

	@Override
	public CheckerHelper typeCheck(CheckerHelper helper) throws TypeCheckException {
		expression.computeExpressionType(helper);
		return helper;
	}
	
	@Override
	public boolean allPathsEndWithReturn() throws TypeCheckException {
		return false;
	}
}