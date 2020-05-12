package it.lucaneg.oo.ast.expression.literal;

import it.lucaneg.oo.ast.expression.Expression;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class Literal extends Expression {

	public Literal(String source, int line, int pos) {
		super(source, line, pos);
	}

}