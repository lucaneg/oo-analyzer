package it.lucaneg.oo.ast.statement;

import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Nop extends Statement {
	
	public Nop(String source, int line, int pos) {
		super(source, line, pos);
	}

	@Override
	public String toString() {
		return "nop";
	}
	
	@Override
	public CheckerHelper typeCheck(CheckerHelper helper) throws TypeCheckException {
		return helper;
	}
	
	@Override
	public boolean allPathsEndWithReturn() throws TypeCheckException {
		return false;
	}
}