package it.lucaneg.oo.ast.expression.literal;

import it.lucaneg.oo.ast.expression.dereference.Argument;
import it.lucaneg.oo.ast.expression.dereference.Index;
import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class IntLiteral extends Literal implements Argument, Index {

	private int value;
	
	public IntLiteral(String source, int line, int pos, int value) {
		super(source, line, pos);
		this.value = value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(getValue());
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		return IntType.INSTANCE;
	}
}