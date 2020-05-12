package it.lucaneg.oo.sdk.analyzer.program.instructions;

import java.util.Collection;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.expression.dereference.FieldAccess;
import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A statement that writes an expression into a field.
 * 
 * @author Luca Negrini
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class FieldWrite extends Assignment {

	/**
	 * The field access that retrieves the field that gets assigned
	 */
	private final FieldAccess field;

	/**
	 * Builds the field write.
	 * 
	 * @param container  the method or constructor that contains this statement
	 * @param line       the line where this statement happens
	 * @param position   the position inside the line where this statement begins
	 * @param variables  the local variables defined when this statement is reached
	 * @param field      the field access that retrieves the field that gets written
	 * @param expression the expression that gets written into the field
	 */
	public FieldWrite(MCodeMember container, int line, int position, Collection<MLocalVariable> variables,
			FieldAccess field, Expression expression) {
		super(container, line, position, variables, expression);
		this.field = field;
	}

	@Override
	public String toString() {
		return field.toString() + " = " + getExpression().toString();
	}
}
