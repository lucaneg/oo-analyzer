package it.lucaneg.oo.ast.statement;

import it.lucaneg.oo.ast.SyntaxNode;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public abstract class Statement extends SyntaxNode {

	public Statement(String source, int line, int pos) {
		super(source, line, pos);
	}

	public abstract boolean allPathsEndWithReturn() throws TypeCheckException;
	
	public abstract CheckerHelper typeCheck(CheckerHelper helper) throws TypeCheckException;

	protected abstract CheckerHelper transformStringJoins(CheckerHelper helper); 
}
