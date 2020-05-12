package it.lucaneg.oo.sdk.analyzer.program.instructions;

import java.util.Collection;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * An assignment to a local variable.
 * 
 * @author Luca Negrini
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class LocalAssignment extends Assignment implements ILocalWrite {

	/**
	 * The local variable that gets assigned
	 */
	private final MLocalVariable variable;

	/**
	 * Builds the local assignment.
	 * 
	 * @param container  the method or constructor that contains this statement
	 * @param line       the line where this statement happens
	 * @param position   the position inside the line where this statement begins
	 * @param variables  the local variables defined when this statement is reached
	 * @param variable   the local variable that gets assigned
	 * @param expression the initialization expression
	 */
	public LocalAssignment(MCodeMember container, int line, int position, Collection<MLocalVariable> variables,
			MLocalVariable variable, Expression expression) {
		super(container, line, position, variables, expression);
		this.variable = variable;
	}

	@Override
	public String toString() {
		return variable.getName() + " := " + getExpression().toString();
	}
}
