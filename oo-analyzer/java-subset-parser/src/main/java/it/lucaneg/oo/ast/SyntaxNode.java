package it.lucaneg.oo.ast;

import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class SyntaxNode {

	private final String source;

	private final int line;

	private final int pos;

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract String toString();
	
	protected void typeCheckError(String msg) throws TypeCheckException {
		throw new TypeCheckException(source + ":" + line  + ":" + pos + " - " + msg);  
	}
}
