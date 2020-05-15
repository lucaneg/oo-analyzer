package it.lucaneg.oo.sdk.analyzer.analyses.impl;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.expression.Variable;
import it.lucaneg.oo.ast.expression.arithmetic.ArithmeticBinaryExpression;
import it.lucaneg.oo.ast.expression.arithmetic.Minus;
import it.lucaneg.oo.ast.expression.comparison.ComparisonBinaryExpression;
import it.lucaneg.oo.ast.expression.creation.NewArray;
import it.lucaneg.oo.ast.expression.creation.NewObject;
import it.lucaneg.oo.ast.expression.dereference.ArrayAccess;
import it.lucaneg.oo.ast.expression.dereference.Call;
import it.lucaneg.oo.ast.expression.dereference.FieldAccess;
import it.lucaneg.oo.ast.expression.literal.FalseLiteral;
import it.lucaneg.oo.ast.expression.literal.Literal;
import it.lucaneg.oo.ast.expression.literal.TrueLiteral;
import it.lucaneg.oo.ast.expression.logical.And;
import it.lucaneg.oo.ast.expression.logical.Not;
import it.lucaneg.oo.ast.expression.logical.Or;
import it.lucaneg.oo.ast.expression.typeCheck.Cast;
import it.lucaneg.oo.ast.expression.typeCheck.TypeCheck;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionSatisfiabilityEvaluator;

public abstract class AbstractExpressionSatisfiabilityEvaluator	implements ExpressionSatisfiabilityEvaluator {

	@Override
	public final Satisfiability satisfies(Expression e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		if (e instanceof And)
			return satisfies(((And) e).getLeft(), env, evaluator).and(satisfies(((And) e).getRight(), env, evaluator));
		else if (e instanceof Or)
			return satisfies(((Or) e).getLeft(), env, evaluator).or(satisfies(((Or) e).getRight(), env, evaluator));
		else if (e instanceof Not)
			return satisfies(((Not) e).getExpression(), env, evaluator).negate();
		else if (e instanceof NewObject)
			return satisfiesNewObject((NewObject) e, env, evaluator);
		else if (e instanceof NewArray)
			return satisfiesNewArray((NewArray) e, env, evaluator);
		else if (e instanceof Minus)
			return satisfiesMinus((Minus) e, env, evaluator);
		else if (e instanceof Call)
			return satisfiesCall((Call) e, env, evaluator);
		else if (e instanceof Literal)
			return satisfiesLiteral((Literal) e, env, evaluator);
		else if (e instanceof Cast)
			return satisfiesCast((Cast) e, env, evaluator);
		else if (e instanceof TypeCheck)
			return satisfiesTypeCheck((TypeCheck) e, env, evaluator);
		else if (e instanceof ArithmeticBinaryExpression)
			return satisfiesArithmeticBinaryExpression((ArithmeticBinaryExpression) e, env, evaluator);
		else if (e instanceof ComparisonBinaryExpression)
			return satisfiesComparisonBinaryExpression((ComparisonBinaryExpression) e, env, evaluator);
		else if (e instanceof ArrayAccess)
			return satisfiesArrayAccess((ArrayAccess) e, env, evaluator);
		else if (e instanceof FieldAccess)
			return satisfiesFieldAccess((FieldAccess) e, env, evaluator);
		else if (e instanceof Variable)
			return satisfiesVariable((Variable) e, env, evaluator);
		else
			return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link NewObject} expression is satisfied
	 * 
	 * @param e   the new object
	 * @param env the environment
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesNewObject(NewObject e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link NewArray} expression is satisfied
	 * 
	 * @param e   the new array
	 * @param env the environment
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesNewArray(NewArray e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Minus} expression is satisfied
	 * 
	 * @param e   the minus
	 * @param env the environment
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesMinus(Minus e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Call} expression is satisfied
	 * 
	 * @param e   the call
	 * @param env the environment
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesCall(Call e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Literal} expression is satisfied
	 * 
	 * @param e   the literal
	 * @param env the environment
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesLiteral(Literal e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		if (e instanceof TrueLiteral)
			return Satisfiability.SATISFIED;

		if (e instanceof FalseLiteral)
			return Satisfiability.NOT_SATISFIED;

		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Cast} expression is satisfied
	 * 
	 * @param e   the cast
	 * @param env the environment
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesCast(Cast e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link TypeCheck} expression is satisfied
	 * 
	 * @param e the type check
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesTypeCheck(TypeCheck e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link ArithmeticBinaryExpression} expression is
	 * satisfied
	 * 
	 * @param e   the arithmetic expression
	 * @param env the environment
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesArithmeticBinaryExpression(ArithmeticBinaryExpression e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link ComparisonBinaryExpression} expression is
	 * satisfied
	 * 
	 * @param e   the comparison
	 * @param env the environment
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesComparisonBinaryExpression(ComparisonBinaryExpression e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link ArrayAccess} expression is satisfied
	 * 
	 * @param e   the array access
	 * @param env the environment
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesArrayAccess(ArrayAccess e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link FieldAccess} expression is satisfied
	 * 
	 * @param e   the field access
	 * @param env the environment
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesFieldAccess(FieldAccess e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Variable} expression is satisfied
	 * 
	 * @param e   the variable
	 * @param env the environment
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesVariable(Variable e, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		return Satisfiability.UNKNOWN;
	}
}
