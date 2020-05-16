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
	ExpressionEvaluator<L> getExpressionEvaluator();

	/**
	 * Yields the expression satisfiability evaluator for this analysis.
	 * 
	 * @return the expression satisfiability evaluator
	 */
	SatisfiabilityEvaluator getSatisfiabilityEvaluator();
}