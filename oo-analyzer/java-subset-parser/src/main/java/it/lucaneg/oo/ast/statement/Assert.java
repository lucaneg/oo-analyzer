package it.lucaneg.oo.ast.statement;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Assert extends Statement {

	private Expression expression;

	public Assert(String source, int line, int pos, Expression expression) {
		super(source, line, pos);
		this.expression = expression;
	}

	@Override
	public String toString() {
		return "assert " + getExpression();
	}
	
	@Override
	public CheckerHelper typeCheck(CheckerHelper helper) throws TypeCheckException { 
		expression.mustBeBoolean(helper);
		expression.computeExpressionType(helper);
		return helper;
	}

	@Override
	public boolean allPathsEndWithReturn() {
		return false;
	}
	
	@Override
	protected CheckerHelper transformStringJoins(CheckerHelper helper) {
		expression = expression.transformStringJoins(helper);
		return helper;
	}
}
