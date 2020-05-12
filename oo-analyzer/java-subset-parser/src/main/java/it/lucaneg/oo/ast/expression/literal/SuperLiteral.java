package it.lucaneg.oo.ast.expression.literal;

import it.lucaneg.oo.ast.expression.dereference.Receiver;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SuperLiteral extends Literal implements Receiver {

	private final Type type;
	
	public SuperLiteral(String source, int line, int pos, Type type) {
		super(source, line, pos);
		this.type = type;
	}

	@Override
	public String toString() {
		return "super";
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		return type;
	}
}