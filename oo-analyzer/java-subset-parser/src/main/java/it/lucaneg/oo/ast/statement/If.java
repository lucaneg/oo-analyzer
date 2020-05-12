package it.lucaneg.oo.ast.statement;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class If extends Statement {

	private final Expression condition;

	private final Statement then;

	private final Statement _else;

	public If(String source, int line, int pos, Expression condition, Statement then) {
		this(source, line, pos, condition, then, new Nop(source, line, pos));
	}
	
	public If(String source, int line, int pos, Expression condition, Statement then, Statement _else) {
		super(source, line, pos);
		this.condition = condition;
		this.then = then;
		this._else = _else;
	}

	@Override
	public String toString() {
		return "if " + condition + " then {" + then + "} else {" + _else + "}";
	}
	
	@Override
	public CheckerHelper typeCheck(CheckerHelper helper) throws TypeCheckException {
		condition.mustBeBoolean(helper);
		then.typeCheck(helper);
		_else.typeCheck(helper);
		return helper;
	}

	@Override
	public boolean allPathsEndWithReturn() throws TypeCheckException {
		return then.allPathsEndWithReturn() & _else.allPathsEndWithReturn();
	}
}