package it.lucaneg.oo.ast.expression.literal;

import it.lucaneg.oo.ast.expression.dereference.Argument;
import it.lucaneg.oo.ast.expression.dereference.Receiver;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ThisLiteral extends Literal implements Receiver, Argument {

	private final Type type;
	
	public ThisLiteral(String source, int line, int pos, Type type) {
		super(source, line, pos);
		this.type = type;
	}

	@Override
	public String toString() {
		return "this";
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		return type;
	}
}