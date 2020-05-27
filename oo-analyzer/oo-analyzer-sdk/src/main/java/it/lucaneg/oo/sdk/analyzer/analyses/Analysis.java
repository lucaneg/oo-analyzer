package it.lucaneg.oo.sdk.analyzer.analyses;

import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.Program;

/**
 * An abstract interpretation analysis of a program.
 * 
 * @author Luca Negrini
 */
public interface Analysis<L extends Lattice<L>, E extends Environment<L, E>> extends SemanticDomain<L, E> {
	
	/**
	 * The character to use to represent top values
	 */
	public static final char TOP_CHAR = '\u0372';
	
	/**
	 * The character to use to represent infinity
	 */
	public static final char INFINITY_CHAR = '\u221E';

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

	/**
	 * Yields the expression evaluator for this analysis.
	 * 
	 * @return the expression evaluator
	 */
	ExpressionEvaluator<L, E> getExpressionEvaluator();

	/**
	 * Yields the expression satisfiability evaluator for this analysis.
	 * 
	 * @return the expression satisfiability evaluator
	 */
	SatisfiabilityEvaluator<L, E> getSatisfiabilityEvaluator();
}