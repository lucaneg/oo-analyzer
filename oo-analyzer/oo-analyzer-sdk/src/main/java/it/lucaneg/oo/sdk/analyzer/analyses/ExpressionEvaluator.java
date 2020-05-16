package it.lucaneg.oo.sdk.analyzer.analyses;

import it.lucaneg.oo.ast.expression.Expression;

/**
 * An expression evaluator that is able to assign approximations to expressions
 * relying on some environment.
 * 
 * @author Luca Negrini
 */
public interface ExpressionEvaluator<L extends Lattice<L>> {

	/**
	 * Yields the approximation that is the result of evaluating the given
	 * expression with the abstract information contained in the given environment.
	 * This method should be used only if the evaluation happens on a single
	 * abstract domain. If this is not the case, use
	 * {@link #eval(Expression, Environment, ExpressionEvaluator)}.
	 * 
	 * @param e   the expression to evaluate
	 * @param env an environment that knows about local variables
	 * @return the approximation of the expression
	 */
	L eval(Expression e, Environment<?, ?> env);

	/**
	 * Yields the approximation that is the result of evaluating the given
	 * expression with the abstract information contained in the given environment.
	 * This method should be used only if the evaluation happens on a hierarchy or a
	 * composition of abstract domains. If this is not the case, use
	 * {@link #eval(Expression, Environment)}.
	 * 
	 * @param e      the expression to evaluate
	 * @param env    an environment that knows about local variables
	 * @param parent the parent evaluator that can be invoked to evaluate
	 *               expressions that this evaluator cannot handle
	 * @return the approximation of the expression
	 */
	L eval(Expression e, Environment<?, ?> env, ExpressionEvaluator<?> parent);
}
