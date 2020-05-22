package it.lucaneg.oo.analyzer.analyses.value;

import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractValue;

public abstract class SingleAbstractValue<L extends SingleAbstractValue<L>> extends AbstractValue<L> {

	/**
	 * Compares this lattice element with the given one, testing if they are equal.
	 * This models the {@code ==} comparison inside the original code. This method
	 * should return {@code null} to express a top boolean.
	 * 
	 * @param other the other lattice element
	 * @return whether or not this lattice is equal to the other, or {@code null}
	 */
	public abstract Boolean isEqualTo(L other);

	/**
	 * Compares this lattice element with the given one, testing if this lattice is
	 * less than the given one. This models the {@code <} comparison inside the
	 * original code. This method should return {@code null} to express a top
	 * boolean.
	 * 
	 * @param other the other lattice element
	 * @return whether or not this lattice is less than the other, or {@code null}
	 */
	public abstract Boolean isLessThen(L other);

	/**
	 * Compares this lattice element with the given one, testing if this lattice is
	 * greater than the given one. This models the {@code >} comparison inside the
	 * original code. This method should return {@code null} to express a top
	 * boolean.
	 * 
	 * @param other the other lattice element
	 * @return whether or not this lattice is greater than the other, or
	 *         {@code null}
	 */
	public abstract Boolean isGreaterThan(L other);
}
