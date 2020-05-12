package it.lucaneg.oo.sdk.analyzer.checks.impl;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import it.lucaneg.oo.sdk.analyzer.checks.Check;
import it.lucaneg.oo.sdk.analyzer.checks.Finding;

/**
 * A base implementation of the check interface.
 * 
 * @author Luca Negrini
 */
public abstract class AbstractCheck implements Check {

	/**
	 * The set of findings raised by this check
	 */
	private final SortedSet<Finding> findings = new TreeSet<>();

	@Override
	public final String getName() {
		String simpleName = getClass().getSimpleName();
		if (simpleName.endsWith("Check"))
			simpleName = simpleName.substring(0, simpleName.length() - "Check".length());
		return simpleName.toLowerCase().substring(0, 1) + simpleName.substring(1);
	}

	/**
	 * Registers a new finding.
	 * 
	 * @param finding the finding to register
	 */
	protected final void registerFinding(Finding finding) {
		findings.add(finding);
	}

	@Override
	public final SortedSet<Finding> getFindings() {
		return Collections.unmodifiableSortedSet(findings);
	}

	@Override
	public final boolean foundSomething() {
		return !getFindings().isEmpty();
	}
}
