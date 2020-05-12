package it.lucaneg.oo.sdk.analyzer.program.instructions;

import java.util.Collection;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * An assignment that writes an expression into something (local variable,
 * field, array element, ...).
 * 
 * @author Luca Negrini
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class Assignment extends Statement {

	/**
	 * The expression that gets written
	 */
	private final Expression expression;

	/**
	 * Builds the assignment
	 * 
	 * @param container  the method or constructor that contains this statement
	 * @param line       the line where this statement happens
	 * @param position   the position inside the line where this statement begins
	 * @param variables  the local variables defined when this statement is reached
	 * @param expression the expression that gets written
	 */
	protected Assignment(MCodeMember container, int line, int position, Collection<MLocalVariable> variables,
			Expression expression) {
		super(container, line, position, variables);
		this.expression = expression;
	}
}
