package it.lucaneg.oo.ast;

import it.lucaneg.oo.ast.types.Type;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FieldDefinition extends ClassMemberDefinition {

	private final Type type;

	public FieldDefinition(String source, int line, int pos, String name, Type type) {
		super(source, line, pos, name);
		this.type = type;
	}

	@Override
	public String toString() {
		return getType().toString() + " " + getName();
	}
}
