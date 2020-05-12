package it.lucaneg.oo.ast.statement;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class While extends Statement {

	private final Expression condition;

	private final Statement body;

	public While(String source, int line, int pos, Expression condition, Statement body) {
		super(source, line, pos);
		this.condition = condition;
		this.body = body;
	}

	@Override
	public String toString() {
		return "while " + condition + " {" + body + "}";
	}
	
	@Override
	public CheckerHelper typeCheck(CheckerHelper helper) throws TypeCheckException {
		condition.mustBeBoolean(helper);
		body.typeCheck(helper);
		return helper;
	}

	@Override
	public boolean allPathsEndWithReturn() throws TypeCheckException {
		body.allPathsEndWithReturn();
		return false;
	}
}