package it.lucaneg.oo.api.analyzer.program.instructions;

import java.util.Collection;

import it.lucaneg.oo.api.analyzer.program.MCodeMember;
import it.lucaneg.oo.api.analyzer.program.MLocalVariable;
import it.lucaneg.oo.ast.expression.Expression;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A command, that is, a statement that simply evaluates an expression that
 * either returns no value, or whose value gets discarded.
 * 
 * @author Luca Negrini
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class Command extends Statement {

	/**
	 * The expression that gets evaluated
	 */
	private final Expression expression;

	/**
	 * Builds the command.
	 * 
	 * @param container  the method or constructor that contains this statement
	 * @param line       the line where this statement happens
	 * @param position   the position inside the line where this statement begins
	 * @param variables  the local variables defined when this statement is reached
	 * @param expression the expression that gets evaluated
	 */
	public Command(MCodeMember container, int line, int position, Collection<MLocalVariable> variables,
			Expression expression) {
		super(container, line, position, variables);
		this.expression = expression;
	}

	@Override
	public String toString() {
		return expression.toString();
	}
}
