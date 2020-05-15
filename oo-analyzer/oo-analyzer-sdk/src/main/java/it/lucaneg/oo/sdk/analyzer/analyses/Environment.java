package it.lucaneg.oo.sdk.analyzer.analyses;

import java.util.Collection;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;

import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;

/**
 * An environment produced during an analysis, that is, an object that maps
 * program variables to their approximations.
 * 
 * @author Luca Negrini
 */
public interface Environment<L extends Lattice<L>, E extends Environment<L, E>>
		extends Iterable<Pair<MLocalVariable, L>> {

	/**
	 * Yields the approximation of the given local variable.
	 * 
	 * @param var the local variable
	 * @return the approximation of the local variable
	 */
	L at(MLocalVariable var);

	/**
	 * Yields the approximation of a local variable with the given name.
	 * 
	 * @param varName the name of the local variable
	 * @return the approximation of the local variable
	 */
	L at(String varName);

	/**
	 * Sets the approximation of the given local variable.
	 * 
	 * @param var    the local variable
	 * @param approx the approximation of the local variable
	 */
	void set(MLocalVariable var, L approx);

	/**
	 * Yields true if and only if this environment has information about the given
	 * local variable.
	 * 
	 * @param var the local variable
	 * @return true only if that condition holds
	 */
	boolean hasApproximationFor(MLocalVariable var);

	/**
	 * Yields true if and only if this environment has information about a local
	 * variable with the given name.
	 * 
	 * @param varName the name of the local variable
	 * @return true only if that condition holds
	 */
	boolean hasApproximationFor(String varName);

	/**
	 * Removes the information regarding the given local variable.
	 * 
	 * @param var the local variable
	 */
	void remove(MLocalVariable var);

	/**
	 * Removes the information regarding a local variable with the given name.
	 * 
	 * @param varName the name of the local variable
	 */
	void remove(String varName);

	/**
	 * Yields an exact (shallow) copy of this environment.
	 * 
	 * @return the copy
	 */
	E copy();

	/**
	 * Yields a (shallow) copy of this environment where all information about
	 * variables whose names are contained in the given collection have been
	 * removed.
	 * 
	 * @param vars the names of the variables to remove
	 * @return a copy of this environment, except for the given variables
	 */
	E except(Collection<String> vars);

	/**
	 * Joins this environment with the given one, that is:
	 * <ul>
	 * <li>each variable contained only in one environment is copied as-is in the
	 * resulting environment</li>
	 * <li>each variable contained in both environments is added to the resulting
	 * environment after applying the elementJoiner function to obtain a merged
	 * element</li>
	 * </ul>
	 * 
	 * @param other
	 * @param elementJoiner
	 * @return
	 */
	E join(E other, BiFunction<L, L, L> elementJoiner);

	/**
	 * Yields the default abstract value for a variable of the given type. This
	 * method should always return bottom, except for the types that are targeted by
	 * this analysis. On such types, this method should return top.
	 * 
	 * @param t the type
	 * @return the appropriate default value
	 */
	L defaultLatticeForType(Type t);
}
