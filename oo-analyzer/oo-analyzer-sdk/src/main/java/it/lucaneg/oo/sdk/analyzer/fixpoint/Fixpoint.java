package it.lucaneg.oo.sdk.analyzer.fixpoint;

import java.util.function.BiFunction;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.sdk.analyzer.analyses.Denotation;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.Lattice;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

/**
 * A fixpoint engine.
 * 
 * @param <L> the type of abstract information computed by the fixpoint
 * @param <E> the type of environment that the computation uses to store
 *            abstract approximations
 * 
 * @author Luca Negrini
 */
public interface Fixpoint<L extends Lattice<L>, E extends Environment<L, E>> {

	/**
	 * Executes a fixpoint computation over the statements linked to this engine.
	 * 
	 * @param initialstate the state at the beginning of the fixpoint
	 * @param semantics    the function that provides the semantics of statements
	 * @param assume       the function that refines environments when a branching
	 *                     statement in encountered
	 * @return a denotation of the whole block
	 */
	Denotation<L, E> fixpoint(E initialstate, BiFunction<Statement, E, E> semantics,
			BiFunction<Expression, E, E> assume);
}
