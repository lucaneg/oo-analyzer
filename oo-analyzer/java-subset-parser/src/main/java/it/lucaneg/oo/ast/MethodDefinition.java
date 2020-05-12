package it.lucaneg.oo.ast;

import it.lucaneg.oo.ast.statement.CodeBlock;
import it.lucaneg.oo.ast.types.Type;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class MethodDefinition extends CodeDefinition {

	public MethodDefinition(String source, int line, int pos, String name, Type returnType, FormalDefinition[] formals, CodeBlock code) {
		super(source, line, pos, name, returnType, formals, code);
	}

	@Override
	public String toString() {
		return getReturnType() + " " + super.toString();
	}
	
	public boolean isMain() {
		return getName().equals("main");
	}
}
