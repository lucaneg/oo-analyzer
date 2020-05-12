package it.lucaneg.oo.ast;

import it.lucaneg.oo.ast.statement.CodeBlock;
import it.lucaneg.oo.ast.types.VoidType;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ConstructorDefinition extends CodeDefinition {
	
	public ConstructorDefinition(String source, int line, int pos, String name, FormalDefinition[] formals, CodeBlock code) {
		super(source, line, pos, name, VoidType.INSTANCE, formals, code);
	}

}
