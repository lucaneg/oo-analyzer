package it.lucaneg.oo.ast.statement;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class LocalDeclaration extends Statement {

	private final Type type;
	
	private final String name;
	
	private final Expression initialization;
	
	public LocalDeclaration(String source, int line, int pos, Type type, String name, Expression initialization) {
		super(source, line, pos);
		this.type = type;
		this.name = name;
		this.initialization = initialization;
	}

	@Override
	public String toString() {
		return type + " " + name + (initialization == null ? "" : " = " + initialization);
	}

	@Override
	public CheckerHelper typeCheck(CheckerHelper helper) throws TypeCheckException { 
		if (initialization != null) {
			Type right = initialization.computeExpressionType(helper);
	
			if (!right.canBeAssignedTo(type))
				typeCheckError(right + " cannot be assigned to " + type);
		}
		
		return helper.putVar(name, type);
	}
	
	@Override
	public boolean allPathsEndWithReturn() throws TypeCheckException {
		return false;
	}
}
