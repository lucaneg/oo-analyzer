package it.lucaneg.oo.ast;

import it.lucaneg.oo.ast.types.Type;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class FormalDefinition extends SyntaxNode {

	private final Type type;
	
	private final String name;
	
	public FormalDefinition(String source, int line, int pos, Type type, String name) {
		super(source, line, pos);
		this.type = type;
		this.name = name;
	}

	@Override
	public String toString() {
		return type.toString() + " " + name;
	}

}
