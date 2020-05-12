package it.lucaneg.oo.api.analyzer.analyses.impl;

import it.lucaneg.oo.api.analyzer.analyses.ExpressionSatisfiabilityEvaluator;
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

public abstract class AbstractExpressionSatisfiabilityEvaluator implements ExpressionSatisfiabilityEvaluator {

	public final Satisfiability satisfies(Expression e) {
		if (e instanceof And)
			return satisfies(((And) e).getLeft()).and(satisfies(((And) e).getRight()));
		else if (e instanceof Or)
			return satisfies(((Or) e).getLeft()).or(satisfies(((Or) e).getRight()));
		else if (e instanceof Not)
			return satisfies(((Not) e).getExpression()).negate();
		else if (e instanceof NewObject)
			return satisfiesNewObject((NewObject) e);
		else if (e instanceof NewArray)
			return satisfiesNewArray((NewArray) e);
		else if (e instanceof Minus)
			return satisfiesMinus((Minus) e);
		else if (e instanceof Call)
			return satisfiesCall((Call) e);
		else if (e instanceof Literal)
			return satisfiesLiteral((Literal) e);
		else if (e instanceof Cast)
			return satisfiesCast((Cast) e);
		else if (e instanceof TypeCheck)
			return satisfiesTypeCheck((TypeCheck) e);
		else if (e instanceof ArithmeticBinaryExpression)
			return satisfiesArithmeticBinaryExpression((ArithmeticBinaryExpression) e);
		else if (e instanceof ComparisonBinaryExpression)
			return satisfiesComparisonBinaryExpression((ComparisonBinaryExpression) e);
		else if (e instanceof ArrayAccess)
			return satisfiesArrayAccess((ArrayAccess) e);
		else if (e instanceof FieldAccess)
			return satisfiesFieldAccess((FieldAccess) e);
		else if (e instanceof Variable)
			return satisfiesVariable((Variable) e);
		else
			return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link NewObject} expression is satisfied
	 * 
	 * @param e the new object
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesNewObject(NewObject e) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link NewArray} expression is satisfied
	 * 
	 * @param e the new array
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesNewArray(NewArray e) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Minus} expression is satisfied
	 * 
	 * @param e the minus
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesMinus(Minus e) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Call} expression is satisfied
	 * 
	 * @param e the call
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesCall(Call e) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Literal} expression is satisfied
	 * 
	 * @param e the literal
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesLiteral(Literal e) {
		if (e instanceof TrueLiteral)
			return Satisfiability.SATISFIED;

		if (e instanceof FalseLiteral)
			return Satisfiability.NOT_SATISFIED;

		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Cast} expression is satisfied
	 * 
	 * @param e the cast
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesCast(Cast e) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link TypeCheck} expression is satisfied
	 * 
	 * @param e the type check
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesTypeCheck(TypeCheck e) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link ArithmeticBinaryExpression} expression is
	 * satisfied
	 * 
	 * @param e the arithmetic expression
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesArithmeticBinaryExpression(ArithmeticBinaryExpression e) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link ComparisonBinaryExpression} expression is
	 * satisfied
	 * 
	 * @param e the comparison
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesComparisonBinaryExpression(ComparisonBinaryExpression e) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link ArrayAccess} expression is satisfied
	 * 
	 * @param e the array access
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesArrayAccess(ArrayAccess e) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link FieldAccess} expression is satisfied
	 * 
	 * @param e the field access
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesFieldAccess(FieldAccess e) {
		return Satisfiability.UNKNOWN;
	}

	/**
	 * Determines whether or not a {@link Variable} expression is satisfied
	 * 
	 * @param e the variable
	 * @return the satisfiability
	 */
	protected Satisfiability satisfiesVariable(Variable e) {
		return Satisfiability.UNKNOWN;
	}
}
