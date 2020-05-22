package it.lucaneg.oo.sdk.analyzer.analyses.impl;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.expression.Variable;
import it.lucaneg.oo.ast.expression.comparison.Equal;
import it.lucaneg.oo.ast.expression.comparison.Greater;
import it.lucaneg.oo.ast.expression.comparison.GreaterOrEqual;
import it.lucaneg.oo.ast.expression.comparison.Less;
import it.lucaneg.oo.ast.expression.comparison.LessOrEqual;
import it.lucaneg.oo.ast.expression.comparison.NotEqual;
import it.lucaneg.oo.ast.expression.dereference.Call;
import it.lucaneg.oo.ast.expression.literal.FalseLiteral;
import it.lucaneg.oo.ast.expression.literal.Literal;
import it.lucaneg.oo.ast.expression.literal.TrueLiteral;
import it.lucaneg.oo.ast.expression.logical.And;
import it.lucaneg.oo.ast.expression.logical.Not;
import it.lucaneg.oo.ast.expression.logical.Or;
import it.lucaneg.oo.ast.expression.typeCheck.TypeCheck;
import it.lucaneg.oo.ast.types.BooleanType;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator;

public abstract class AbstractSatisfiabilityEvaluator<L extends AbstractValue<L>, E extends AbstractEnvironment<L, E>>
		implements SatisfiabilityEvaluator<L, E> {

	@Override
	public final Satisfiability satisfies(Expression e, E env, ExpressionEvaluator<L, E> evaluator) {
		if (e.getStaticType() != BooleanType.INSTANCE)
			throw new IllegalArgumentException("Cannot evaliate satisfiability of non-boolean expression " + e);

		if (e instanceof And)
			return satisfies(((And) e).getLeft(), env, evaluator).and(satisfies(((And) e).getRight(), env, evaluator));
		else if (e instanceof Or)
			return satisfies(((Or) e).getLeft(), env, evaluator).or(satisfies(((Or) e).getRight(), env, evaluator));
		else if (e instanceof Not)
			return satisfies(((Not) e).getExpression(), env, evaluator).negate();
		else if (e instanceof Call)
			return satisfiesCall((Call) e, env, evaluator);
		else if (e instanceof Literal)
			return satisfiesLiteral((Literal) e, env, evaluator);
		else if (e instanceof TypeCheck)
			return satisfiesTypeCheck((TypeCheck) e, env, evaluator);
		else if (e instanceof Equal)
			return satisfiesEqual((Equal) e, env, evaluator);
		else if (e instanceof NotEqual) {
			NotEqual ne = (NotEqual) e;
			return satisfiesEqual(ne.flip(), env, evaluator).negate();
		} else if (e instanceof Greater)
			return satisfiesGreater((Greater) e, env, evaluator);
		else if (e instanceof Less)
			return satisfiesLess((Less) e, env, evaluator);
		else if (e instanceof GreaterOrEqual) {
			GreaterOrEqual ge = (GreaterOrEqual) e;
			return satisfiesLess(ge.flip(), env, evaluator).negate();
		} else if (e instanceof LessOrEqual) {
			LessOrEqual le = (LessOrEqual) e;
			return satisfiesGreater(le.flip(), env, evaluator).negate();
		} else if (e instanceof Variable)
			return satisfiesVariable((Variable) e, env, evaluator);
		else
			return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Less} expression is satisfied
	 * 
	 * @param e         the less
	 * @param env       the environment
	 * @param evaluator the expression evaluator
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesLess(Less e, E env, ExpressionEvaluator<L, E> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Greater} expression is satisfied
	 * 
	 * @param e         the greater
	 * @param env       the environment
	 * @param evaluator the expression evaluator
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesGreater(Greater e, E env, ExpressionEvaluator<L, E> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Equal} expression is satisfied
	 * 
	 * @param e         the equal
	 * @param env       the environment
	 * @param evaluator the expression evaluator
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesEqual(Equal e, E env, ExpressionEvaluator<L, E> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Call} expression is satisfied
	 * 
	 * @param e         the call
	 * @param env       the environment
	 * @param evaluator the expression evaluator
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesCall(Call e, E env, ExpressionEvaluator<L, E> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Literal} expression is satisfied
	 * 
	 * @param e         the literal
	 * @param env       the environment
	 * @param evaluator the expression evaluator
	 * @return the satisfiability
	 */
	protected final Satisfiability satisfiesLiteral(Literal e, E env, ExpressionEvaluator<L, E> evaluator) {
		if (e instanceof TrueLiteral)
			return Satisfiability.SATISFIED;

		if (e instanceof FalseLiteral)
			return Satisfiability.NOT_SATISFIED;

		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link TypeCheck} expression is satisfied
	 * 
	 * @param e         the type check
	 * @param env       the environment
	 * @param evaluator the expression evaluator
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesTypeCheck(TypeCheck e, E env, ExpressionEvaluator<L, E> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Variable} expression is satisfied
	 * 
	 * @param e         the variable
	 * @param env       the environment
	 * @param evaluator the expression evaluator
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesVariable(Variable e, E env, ExpressionEvaluator<L, E> evaluator) {
		return Satisfiability.UNKNOWN;
	}
}
