package it.lucaneg.oo.sdk.analyzer.analyses.impl;

import it.lucaneg.oo.sdk.analyzer.analyses.Lattice;

public abstract class AbstractValue<L extends AbstractValue<L>> implements Lattice<L> {

	@Override
	@SuppressWarnings("unchecked")
	public final L lub(L other) {
		if (other == null || other.isBottom())
			return (L) this;

		if (isBottom())
			return other;

		if (isTop() || other.isTop())
			return top();
		
		if (equals(other))
			return (L) this;

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
		
		if (equals(other))
			return (L) this;

		return wideningAux(other);
	}
	
	@Override
	public L narrowing(L other) {
		return lub(other);
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

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract String toString();
}
