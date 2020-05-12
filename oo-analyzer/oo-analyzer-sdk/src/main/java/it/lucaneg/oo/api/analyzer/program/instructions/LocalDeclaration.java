package it.lucaneg.oo.api.analyzer.program.instructions;

import java.util.Collection;

import it.lucaneg.oo.api.analyzer.program.MCodeMember;
import it.lucaneg.oo.api.analyzer.program.MLocalVariable;
import it.lucaneg.oo.ast.expression.Expression;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A declaration of a local variable, optionally with an initalization
 * expression.
 * 
 * @author Luca Negrini
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class LocalDeclaration extends Statement implements ILocalWrite {

	/**
	 * The local variable that gets defined
	 */
	private final MLocalVariable variable;

	/**
	 * The expression used to initialize, if any
	 */
	private final Expression expression;

	/**
	 * Builds the local declaration.
	 * 
	 * @param container  the method or constructor that contains this statement
	 * @param line       the line where this statement happens
	 * @param position   the position inside the line where this statement begins
	 * @param variables  the local variables defined when this statement is reached
	 * @param variable   the variable that gets defined
	 * @param expression the initialization expression, if any
	 */
	public LocalDeclaration(MCodeMember container, int line, int position, Collection<MLocalVariable> variables,
			MLocalVariable variable, Expression expression) {
		super(container, line, position, variables);
		this.variable = variable;
		this.expression = expression;
	}

	@Override
	public String toString() {
		return variable.getType() + " " + variable.getName() + " = " + expression.toString();
	}
}
