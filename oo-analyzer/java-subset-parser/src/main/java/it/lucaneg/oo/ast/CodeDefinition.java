package it.lucaneg.oo.ast;

import it.lucaneg.oo.ast.statement.CodeBlock;
import it.lucaneg.oo.ast.statement.Statement;
import it.lucaneg.oo.ast.types.Type;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class CodeDefinition extends ClassMemberDefinition {
	
	private final Type returnType;
	
	private final FormalDefinition[] formals;
	
	private final CodeBlock code;

	protected CodeDefinition(String source, int line, int pos, String name, Type returnType, FormalDefinition[] formals, CodeBlock code) {
		super(source, line, pos, name);
		this.returnType = returnType;
		this.formals = formals;
		this.code = code;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(getName()).append("(");
		FormalDefinition[] pars = getFormals();
		for (int i = 0; i < pars.length; i++)
			if (i != pars.length - 1)
				sb.append(pars[i].toString()).append(", ");
			else 
				sb.append(pars[i].toString());
		
		return sb.append(")").toString();
	}
	
	public String codeDump(String linePrefix) {
		StringBuilder sb = new StringBuilder();
		for (Statement st : code.getStatements())
			sb.append(linePrefix).append(st).append("\n");
		
		return sb.toString();
	}
	
	public String codeSignature() {
		return codeSignatureForResolution() + returnType;
	}
	
	public String codeSignatureForResolution() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(getName()).append("(");
		FormalDefinition[] pars = getFormals();
		for (int i = 0; i < pars.length; i++)
			if (i != pars.length - 1)
				sb.append(pars[i].getType().toString()).append(", ");
			else 
				sb.append(pars[i].getType().toString());
		
		return sb.append(")").toString();
	}
	
	public Type[] getFormalsTypes() {
		Type[] result = new Type[formals.length];
		for (int i = 0; i < result.length; i++) 
			result[i] = formals[i].getType();
		
		return result;
	}
}
