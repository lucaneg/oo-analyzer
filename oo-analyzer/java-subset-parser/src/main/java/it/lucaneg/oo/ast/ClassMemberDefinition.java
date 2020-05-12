package it.lucaneg.oo.ast;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public abstract class ClassMemberDefinition extends SyntaxNode {

	private final String name;
	
	protected ClassMemberDefinition(String source, int line, int pos, String name) {
		super(source, line, pos);
		this.name = name;
	}
}
