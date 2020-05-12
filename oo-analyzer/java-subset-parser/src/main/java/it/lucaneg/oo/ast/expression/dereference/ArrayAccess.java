package it.lucaneg.oo.ast.expression.dereference;

import java.util.Arrays;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.expression.Variable;
import it.lucaneg.oo.ast.statement.Assignable;
import it.lucaneg.oo.ast.types.ArrayType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ArrayAccess extends Expression implements Argument, Assignable {

	private Variable array;

	private Index[] indexes;

	public ArrayAccess(String source, int line, int pos, Variable array, Index[] indexes) {
		super(source, line, pos);
		this.array = array;
		this.indexes = indexes;
	}

	@Override
	public String toString() {
		return getArray() + Arrays.toString(getIndexes());
	}

	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		Type arrayType = array.computeExpressionTypeInternal(helper);

		for (Index index : indexes)
			index.asExpression().mustBeInt(helper);

		// the array expression must have array type
		if (!(arrayType instanceof ArrayType))
			typeCheckError("array type required");

		// we return the static type of the elements of the array
		return ((ArrayType) arrayType).getElementsType();
	}
}