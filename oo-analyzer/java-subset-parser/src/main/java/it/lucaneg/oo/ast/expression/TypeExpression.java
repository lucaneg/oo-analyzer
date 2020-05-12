package it.lucaneg.oo.ast.expression;

import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TypeExpression extends Expression {
	
	private final Type type;

	public TypeExpression(String source, int line, int pos, Type type) {
		super(source, line, pos);
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type.toString(); 
	}

	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		return type;
	}
}
