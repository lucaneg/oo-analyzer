package it.lucaneg.oo.sdk.analyzer.analyses;

import it.lucaneg.oo.ast.expression.Expression;

/**
 * An expression evaluator that is able to assign approximations to expressions
 * relying on some environment.
 * 
 * @author Luca Negrini
 */
public interface ExpressionEvaluator<L extends Lattice<L>, E extends Environment<L, E>> {

	/**
	 * Yields the approximation that is the result of evaluating the given
	 * expression with the abstract information contained in the given environment.
	 * 
	 * @param e   the expression to evaluate
	 * @param env an environment that knows about local variables
	 * @return the approximation of the expression
	 */
	L eval(Expression e, E env);
}
