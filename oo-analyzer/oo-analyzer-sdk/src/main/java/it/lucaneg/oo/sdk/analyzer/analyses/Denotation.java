package it.lucaneg.oo.sdk.analyzer.analyses;

import java.util.HashMap;
import java.util.Map;

import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

/**
 * A denotation of method, containing approximations (in the form of
 * {@link Environment} instances) for each statement.
 * 
 * @author Luca Negrini
 */
public final class Denotation<L extends Lattice<L>, E extends Environment<L, E>> {

	/**
	 * The map between statements and their approximations
	 */
	private final Map<Statement, Map<TokenList, E>> environments;

	/**
	 * Builds an empty denotation
	 */
	public Denotation() {
		environments = new HashMap<>();
	}

	/**
	 * Builds an empty denotation that is already set up to contain the given number
	 * of approximations
	 * 
	 * @param size the initial number of approximations
	 */
	public Denotation(int size) {
		environments = new HashMap<>(size);
	}

	/**
	 * Yields the approximation for the given statement. If the given statement is
	 * not (yet) part of this denotation, this method will throw an
	 * {@link IllegalArgumentException}.
	 * 
	 * @param st the statement
	 * @return the approximation of the statement
	 * @throws IllegalArgumentException if the statement is not (yet) part of this
	 *                                  denotation
	 */
	public Map<TokenList, E> at(Statement st) {
		if (hasEnvironmentFor(st))
			return environments.get(st);

		throw new IllegalArgumentException("No denotation found for " + st);
	}
	
	public E lubAt(Statement st) {
		if (hasEnvironmentFor(st)) {
			Map<TokenList, E> map = environments.get(st);
			E result = null;
			for (E env : map.values())
				if (result == null)
					result = env;
				else 
					result = result.join(env, Lattice::lub);
			return result;
		}

		throw new IllegalArgumentException("No denotation found for " + st);
	}

	/**
	 * Sets the approximation of the given statement.
	 * 
	 * @param st  the statement
	 * @param env the approximation of the statement
	 */
	public void set(Statement st, TokenList tokens, E env) {
		environments.computeIfAbsent(st, s -> new HashMap<>()).put(tokens, env);
	}

	/**
	 * Yields true if and only if this denotation contains an approximation for the
	 * given statement.
	 * 
	 * @param st the statement
	 * @return true only if that condition holds
	 */
	public boolean hasEnvironmentFor(Statement st) {
		return environments.containsKey(st);
	}
}
