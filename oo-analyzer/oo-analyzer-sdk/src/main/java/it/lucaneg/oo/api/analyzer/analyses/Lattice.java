package it.lucaneg.oo.api.analyzer.analyses;

/**
 * A lattice structure, that is, an object that can be used as domain for static
 * analyses. An instance of this class represents an element of such domain.
 * 
 * @author Luca Negrini
 */
public interface Lattice<L extends Lattice<L>> {

	/**
	 * Performs the least upper bound operation between this lattice element and the
	 * given one.
	 * 
	 * @param other the other element of the domain
	 * @return the least upper bound between this element and the given one
	 */
	L lub(L other);

	/**
	 * Performs the widening operation between this lattice element and the given
	 * one. For finite lattices, and for the ones that satisfy ACC, this method
	 * should behave exactly as {@link Lattice#lub(Lattice)}
	 * 
	 * @param other the other element of the domain
	 * @return the element obtained by applying the widening operator
	 */
	L widening(L other);

	/**
	 * Yields the bottom element of the lattice.
	 * 
	 * @return the bottom element
	 */
	L bottom();

	/**
	 * Yields the top element of the lattice.
	 * 
	 * @return the top element
	 */
	L top();

	/**
	 * Yields true if and only if this element is the top element of the lattice.
	 * 
	 * @return true only if that condition holds
	 */
	boolean isTop();

	/**
	 * Yields true if and only if this element is the bottom element of the lattice.
	 * 
	 * @return true only if that condition holds
	 */
	boolean isBottom();
}
