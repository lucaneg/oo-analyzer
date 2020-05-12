package it.lucaneg.oo.ast.expression.literal;

import it.lucaneg.oo.ast.expression.dereference.Argument;
import it.lucaneg.oo.ast.types.BooleanType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class FalseLiteral extends Literal implements Argument {

	public FalseLiteral(String source, int line, int pos) {
		super(source, line, pos);
	}

	@Override
	public String toString() {
		return "false";
	}

	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		return BooleanType.INSTANCE;
	}
}