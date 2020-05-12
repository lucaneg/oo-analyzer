package it.lucaneg.oo.api.analyzer.analyses;

import it.lucaneg.oo.ast.expression.Expression;

/**
 * An object that evaluates expressions.
 * 
 * @author Luca Negrini
 */
public interface ExpressionSatisfiabilityEvaluator {

	/**
	 * The satisfiability of an expression.
	 * 
	 * @author Luca Negrini
	 */
	public enum Satisfiability {
		/**
		 * Represent the fact that an expression is satisfied.
		 */
		SATISFIED {
			@Override
			public Satisfiability negate() {
				return NOT_SATISFIED;
			}

			@Override
			public Satisfiability and(Satisfiability other) {
				return other;
			}

			@Override
			public Satisfiability or(Satisfiability other) {
				return this;
			}
		},

		/**
		 * Represent the fact that an expression is not satisfied.
		 */
		NOT_SATISFIED {
			@Override
			public Satisfiability negate() {
				return SATISFIED;
			}

			@Override
			public Satisfiability and(Satisfiability other) {
				return this;
			}

			@Override
			public Satisfiability or(Satisfiability other) {
				return other;
			}
		},

		/**
		 * Represent the fact that it is not possible to determine whether or not an
		 * expression is satisfied.
		 */
		UNKNOWN {
			@Override
			public Satisfiability negate() {
				return this;
			}

			@Override
			public Satisfiability and(Satisfiability other) {
				if (other == NOT_SATISFIED)
					return other;

				return this;
			}

			@Override
			public Satisfiability or(Satisfiability other) {
				if (other == SATISFIED)
					return other;

				return this;
			}
		};

		/**
		 * Negates the current satisfiability, getting the opposite result.
		 * 
		 * @return the negation
		 */
		public abstract Satisfiability negate();

		/**
		 * Performs a logical and between this satisfiability and the given one.
		 * 
		 * @param other the other satisfiability
		 * @return the logical and between the two satisfiabilities
		 */
		public abstract Satisfiability and(Satisfiability other);

		/**
		 * Performs a logical or between this satisfiability and the given one.
		 * 
		 * @param other the other satisfiability
		 * @return the logical or between the two satisfiabilities
		 */
		public abstract Satisfiability or(Satisfiability other);
		
		public static Satisfiability fromBoolean(boolean bool) {
			if (bool)
				return SATISFIED;
			return NOT_SATISFIED;
		}
	}

	/**
	 * Evaluates if an expression is satisfiable.
	 * 
	 * @param e the expression that needs to be satisfied
	 * @return {@link Satisfiability#SATISFIED} if the expression is satisfied,
	 *         {@link Satisfiability#NOT_SATISFIED} if the expression is not
	 *         satisfied, {@link Satisfiability#UNKNOWN} if it was not possible to
	 *         determine whether or not the expression is satisfiable
	 */
	Satisfiability satisfies(Expression e);
}
