package it.lucaneg.oo.analyzer.analyses.strings.dfa;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringEnvironment;

/**
 * An environment for string elements represented through dfa
 * TODO vincenzo
 * 
 * @author Luca Negrini
 */
public class DfaEnvironment extends BaseStringEnvironment<DfaLattice, DfaEnvironment> {

	/**
	 * Builds an empty string environment
	 */
	public DfaEnvironment() {
		super();
	}

	/**
	 * Builds a string environment by shallow copying the given one
	 * 
	 * @param other the original environment
	 */
	private DfaEnvironment(DfaEnvironment other) {
		super(other.approximations);
	}

	@Override
	public DfaEnvironment copy() {
		return new DfaEnvironment(this);
	}

	@Override
	protected DfaLattice latticeBottom() {
		return DfaLattice.getBottom();
	}

	@Override
	protected DfaLattice latticeTop() {
		return DfaLattice.getTop();
	}
}
