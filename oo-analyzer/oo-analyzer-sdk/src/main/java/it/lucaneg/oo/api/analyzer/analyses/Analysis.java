package it.lucaneg.oo.api.analyzer.analyses;

import it.lucaneg.oo.api.analyzer.program.MCodeMember;
import it.lucaneg.oo.api.analyzer.program.Program;

/**
 * An abstract interpretation analysis of a program.
 * 
 * @author Luca Negrini
 */
public interface Analysis<L extends Lattice<L>, E extends Environment<L, E>> extends SemanticDomain<L, E> {

	/**
	 * Yields the name of the analysis.
	 * 
	 * @return the name of the analysis
	 */
	String getName();

	/**
	 * Executes the analysis on the given program, generating a denotation for each
	 * method.
	 * 
	 * @param program the target of the analysis
	 */
	void run(Program program);

	/**
	 * Yields the denotation of the given method.
	 * 
	 * @param code the method
	 * @return the denotation of the given method
	 */
	Denotation<L, E> of(MCodeMember code);
}