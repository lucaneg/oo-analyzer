package it.lucaneg.oo.ast.expression;

import it.lucaneg.oo.ast.expression.dereference.Argument;
import it.lucaneg.oo.ast.expression.dereference.Index;
import it.lucaneg.oo.ast.expression.dereference.Receiver;
import it.lucaneg.oo.ast.statement.Assignable;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Variable extends Expression implements Receiver, Argument, Index, Assignable {

	private final String name;

	public Variable(String source, int line, int pos, String name) {
		super(source, line, pos);
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		Type result = helper.getVar(name);
		if (result == null)
			typeCheckError("undefined variable " + name);
		return result;
	}
}