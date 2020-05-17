package it.lucaneg.oo.analyzer.analyses.value.domains.ints;

import java.util.List;

import it.lucaneg.oo.analyzer.analyses.value.SingleValueLattice;

public abstract class AbstractIntegerLattice<L extends AbstractIntegerLattice<L>> extends SingleValueLattice<L> {
	/**
	 * Plus operation.
	 * 
	 * @param other second operand
	 * @return the sum 
	 */
	public abstract L plus(L other);
	/**
	 * Difference operation.
	 * 
	 * @param other second operand
	 * @return the difference
	 */
	public abstract L diff(L other);

	/**
	 * Multiplication operation.
	 * 
	 * @param other second operand
	 * @return the multiplication 
	 */
	public abstract L mul(L other);

	/**
	 * Yields the list of integers contained in this approximation, if it is finite.
	 * Otherwise, an empty list is returned.
	 * 
	 * @return list of integers into this approximation
	 */
	public abstract List<Integer> getIntergers();

	/**
	 * Check if this approximation represents the zero element
	 * 
	 * @return true if that condition holds
	 */
	public abstract boolean isZero();

	/**
	 * Check if the parameter n is into this approximation.
	 * 
	 * @param n integer parameter
	 * @return true if n is contained into this approximation
	 */
	public abstract boolean contains(int n);
	
	public abstract L minusOne();
	
	public abstract L mk(int value);
	
	public abstract boolean isFinite();
}
