package it.lucaneg.oo.api.analyzer.analyses;

import it.lucaneg.oo.api.analyzer.program.instructions.Statement;
import it.lucaneg.oo.ast.expression.Expression;

/**
 * A semantic domain, that is, an object that can transform and improve
 * {@link Environment} objects when traversing program points
 * 
 * @author Luca Negrini
 */
public interface SemanticDomain<L extends Lattice<L>, E extends Environment<L, E>> {

	/**
	 * Applies the semantic of the given statement to the given environment,
	 * returning a new instance of the latter. Such environment will contain the
	 * same information of the given one, except for the denotations of program
	 * variables modified (directly or indirectly) by the given statement. Note that
	 * conditional statements should not have any effect on the environment.
	 * 
	 * @param st  the statement that is modifying the environment
	 * @param env the environment that gets updated
	 * @return a new environment with updated denotations
	 */
	E smallStepSemantics(Statement st, E env);

	/**
	 * Refines the information contained in the given environment, assuming that the
	 * given expression holds. The given environment is never modified. Instead, a
	 * new one will be returned.
	 * 
	 * @param expr the expression that holds and that is used to refine the
	 *             environment
	 * @param env  the environment to refine
	 * @return a new environment, refined by the given expression
	 */
	E assume(Expression expr, E env);
}
