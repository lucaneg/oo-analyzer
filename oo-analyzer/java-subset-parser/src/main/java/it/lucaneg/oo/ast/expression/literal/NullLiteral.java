package it.lucaneg.oo.ast.expression.literal;

import it.lucaneg.oo.ast.expression.dereference.Argument;
import it.lucaneg.oo.ast.types.NilType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class NullLiteral extends Literal implements Argument {

	public NullLiteral(String source, int line, int pos) {
		super(source, line, pos);
	}

	@Override
	public String toString() {
		return "null";
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		return NilType.INSTANCE;
	}
}