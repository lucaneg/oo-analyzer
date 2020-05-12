package it.lucaneg.oo.ast.expression.literal;

import it.lucaneg.oo.ast.expression.dereference.Argument;
import it.lucaneg.oo.ast.types.ClassType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class StringLiteral extends Literal implements Argument {

	private final String value;
	
	public StringLiteral(String source, int line, int pos, String value) {
		super(source, line, pos);
		
		if (value.startsWith("\""))
			value = value.substring(1);
		if (value.endsWith("\""))
			value = value.substring(0, value.length() - 1);
		
		this.value = value;
	}

	@Override
	public String toString() {
		return "\"" + getValue() + "\"";
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		return ClassType.get("String");
	}
}