package it.lucaneg.oo.ast.expression.literal;

import it.lucaneg.oo.ast.expression.dereference.Argument;
import it.lucaneg.oo.ast.types.FloatType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FloatLiteral extends Literal implements Argument {

	private final float value;
	
	public FloatLiteral(String source, int line, int pos, float value) {
		super(source, line, pos);
		this.value = value;
	}

	@Override
	public String toString() {
		return String.valueOf(getValue());
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		return FloatType.INSTANCE;
	}
}