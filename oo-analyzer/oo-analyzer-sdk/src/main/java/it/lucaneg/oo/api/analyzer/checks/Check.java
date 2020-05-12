package it.lucaneg.oo.api.analyzer.checks;

import java.util.Collection;
import java.util.SortedSet;

import it.lucaneg.oo.api.analyzer.analyses.Analysis;
import it.lucaneg.oo.api.analyzer.program.Program;

/**
 * A Check, that is, an object that exploits information from the analyses to
 * raise findings on a program.
 * 
 * @author Luca Negrini
 */
public interface Check {

	/**
	 * Yields the name of the check.
	 * 
	 * @return the name
	 */
	String getName();

	/**
	 * Runs this check on the given program, allowing it to exploit the information
	 * provided by the given analyses.
	 * 
	 * @param program  the program to check
	 * @param analyses the analyses that can be exploited
	 */
	void run(Program program, Collection<Analysis<?, ?>> analyses);

	/**
	 * Yields all the findings that this check raised.
	 * 
	 * @return the findings
	 */
	SortedSet<Finding> getFindings();

	/**
	 * Yields true if and only this check issued at least one finding up to now.
	 * 
	 * @return true only if that condition holds
	 */
	boolean foundSomething();
}
