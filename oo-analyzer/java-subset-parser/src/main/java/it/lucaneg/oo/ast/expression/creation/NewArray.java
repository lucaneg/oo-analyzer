package it.lucaneg.oo.ast.expression.creation;

import java.util.Arrays;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.expression.dereference.Index;
import it.lucaneg.oo.ast.types.ArrayType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class NewArray extends Expression {

	private final Type elementsType;

	private final Index[] sizes;
	
	public NewArray(String source, int line, int pos, Type elementsType, Index[] sizes) {
		super(source, line, pos);
		this.elementsType = elementsType;
		this.sizes = sizes;
	}

	@Override
	public String toString() {
		return "new " + getElementsType() + Arrays.toString(getSizes());
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		for (Index size : sizes)
			size.asExpression().mustBeInt(helper);
		return ArrayType.mk(elementsType);
	}
}