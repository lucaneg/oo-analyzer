package it.lucaneg.oo.analyzer.analyses.strings.stringprefix;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringEnvironment;

/**
 * Prefix environment, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi "A suite of abstract
 * domains for static analysis of string values." in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class StringPrefixEnvironment extends BaseStringEnvironment<StringPrefixLattice, StringPrefixEnvironment> {

	/**
	 * Builds an empty string prefix environment
	 */
	public StringPrefixEnvironment() {
		super();
	}

	/**
	 * Builds a string prefix environment by shallow copying the given one
	 * 
	 * @param other the original environment
	 */
	private StringPrefixEnvironment(StringPrefixEnvironment other) {
		super(other.approximations);
	}

	@Override
	public StringPrefixEnvironment copy() {
		return new StringPrefixEnvironment(this);
	}

	@Override
	protected StringPrefixLattice latticeBottom() {
		return StringPrefixLattice.getBottom();
	}

	@Override
	protected StringPrefixLattice latticeTop() {
		return StringPrefixLattice.getTop();
	}
}
