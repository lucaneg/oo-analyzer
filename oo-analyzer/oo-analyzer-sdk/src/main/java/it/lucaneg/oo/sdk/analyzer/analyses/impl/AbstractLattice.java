package it.lucaneg.oo.sdk.analyzer.analyses.impl;

import it.lucaneg.oo.sdk.analyzer.analyses.Lattice;

public abstract class AbstractLattice<L extends AbstractLattice<L>> implements Lattice<L> {

	@Override
	@SuppressWarnings("unchecked")
	public final L lub(L other) {
		if (other == null || other.isBottom())
			return (L) this;

		if (isBottom())
			return other;

		if (isTop() || other.isTop())
			return top();

		return lubAux(other);
	}

	/**
	 * Performs the least upper bound between this lattice and the given one,
	 * assuming that neither of them is top, nor bottom, nor null.
	 * 
	 * @param other the other lattice
	 * @return the least upper bound
	 */
	protected abstract L lubAux(L other);

	@Override
	@SuppressWarnings("unchecked")
	public final L widening(L other) {
		if (other == null || other.isBottom())
			return (L) this;

		if (isBottom())
			return other;

		if (isTop() || other.isTop())
			return top();

		return wideningAux(other);
	}

	@Override
	public final boolean isTop() {
		return this == top();
	}

	@Override
	public final boolean isBottom() {
		return this == bottom();
	}

	/**
	 * Performs the widening between this lattice and the given one, assuming that
	 * neither of them is top, nor bottom, nor null.
	 * 
	 * @param other the other lattice
	 * @return the widening
	 */
	protected abstract L wideningAux(L other);

	/**
	 * Compares this lattice element with the given one, testing if they are equal.
	 * This models the {@code ==} comparison inside the original code. This method
	 * should return {@code null} to express a top boolean.
	 * 
	 * @param other the other lattice element
	 * @return whether or not this lattice is equal to the other, or {@code null}
	 */
	public abstract Boolean isEqualTo(AbstractLattice<?> other);

	/**
	 * Compares this lattice element with the given one, testing if this lattice is
	 * less than the given one. This models the {@code <} comparison inside the
	 * original code. This method should return {@code null} to express a top
	 * boolean.
	 * 
	 * @param other the other lattice element
	 * @return whether or not this lattice is less than the other, or {@code null}
	 */
	public abstract Boolean isLessThen(AbstractLattice<?> other);

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
	public abstract Boolean isGreaterThan(AbstractLattice<?> other);

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract String toString();
}
