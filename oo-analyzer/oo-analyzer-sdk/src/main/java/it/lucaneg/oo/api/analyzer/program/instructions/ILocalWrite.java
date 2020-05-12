package it.lucaneg.oo.api.analyzer.program.instructions;

import it.lucaneg.oo.api.analyzer.program.MLocalVariable;
import it.lucaneg.oo.ast.expression.Expression;

/**
 * Common interface for a statement that writes an expression into a local
 * variable.
 * 
 * @author Luca Negrini
 */
public interface ILocalWrite {
	/**
	 * Yields the local variable that gets assigned
	 * 
	 * @return the local variable
	 */
	MLocalVariable getVariable();

	/**
	 * Yields the expression that gets assigned to the variable
	 * 
	 * @return the expression
	 */
	Expression getExpression();
}
