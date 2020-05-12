package it.lucaneg.oo.ast.expression.literal;

import it.lucaneg.oo.ast.expression.dereference.Argument;
import it.lucaneg.oo.ast.types.BooleanType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class TrueLiteral extends Literal implements Argument {

	public TrueLiteral(String source, int line, int pos) {
		super(source, line, pos);
	}

	@Override
	public String toString() {
		return "true";
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		return BooleanType.INSTANCE;
	}
}