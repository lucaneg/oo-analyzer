package it.lucaneg.oo.analyzer.analyses.strings.string;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringEnvironment;

/**
 * An environment for string elements represented through automata
 * 
 * @author Luca Negrini
 */
public class StringEnvironment extends BaseStringEnvironment<StringLattice, StringEnvironment> {

	/**
	 * Builds an empty string environment
	 */
	public StringEnvironment() {
		super();
	}

	/**
	 * Builds a string environment by shallow copying the given one
	 * 
	 * @param other the original environment
	 */
	private StringEnvironment(StringEnvironment other) {
		super(other.approximations);
	}

	@Override
	public StringEnvironment copy() {
		return new StringEnvironment(this);
	}

	@Override
	protected StringLattice latticeBottom() {
		return StringLattice.getBottom();
	}

	@Override
	protected StringLattice latticeTop() {
		return StringLattice.getTop();
	}
}
