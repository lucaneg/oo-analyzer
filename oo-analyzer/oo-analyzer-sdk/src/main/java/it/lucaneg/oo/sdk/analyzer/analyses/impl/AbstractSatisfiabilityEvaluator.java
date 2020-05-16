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
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator;

public abstract class AbstractSatisfiabilityEvaluator implements SatisfiabilityEvaluator {

	@Override
	public final Satisfiability satisfies(Expression e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
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
		else if (e instanceof NotEqual)
			return satisfiesNotEqual((NotEqual) e, env, evaluator);
		else if (e instanceof Greater)
			return satisfiesGreater((Greater) e, env, evaluator);
		else if (e instanceof Less)
			return satisfiesLess((Less) e, env, evaluator);
		else if (e instanceof GreaterOrEqual) {
			GreaterOrEqual ge = (GreaterOrEqual) e;
			Less lt = new Less(ge.getSource(), ge.getLine(), ge.getPos(), ge.getLeft(), ge.getRight());
			return satisfiesLess(lt, env, evaluator).negate();
		} else if (e instanceof LessOrEqual) {
			LessOrEqual le = (LessOrEqual) e;
			Greater gt = new Greater(le.getSource(), le.getLine(), le.getPos(), le.getLeft(), le.getRight());
			return satisfiesGreater(gt, env, evaluator).negate();
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
	protected Satisfiability satisfiesLess(Less e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
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
	protected Satisfiability satisfiesGreater(Greater e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link NotEqual} expression is satisfied
	 * 
	 * @param e         the not equal
	 * @param env       the environment
	 * @param evaluator the expression evaluator
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesNotEqual(NotEqual e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
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
	protected Satisfiability satisfiesEqual(Equal e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
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
	protected Satisfiability satisfiesCall(Call e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
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
	protected final Satisfiability satisfiesLiteral(Literal e, Environment<?, ?> env,
			ExpressionEvaluator<?> evaluator) {
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
	protected Satisfiability satisfiesTypeCheck(TypeCheck e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
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
	protected Satisfiability satisfiesVariable(Variable e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN;
	}
}
