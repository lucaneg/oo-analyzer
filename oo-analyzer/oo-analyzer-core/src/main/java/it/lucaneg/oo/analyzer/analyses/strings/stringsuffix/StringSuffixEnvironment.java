package it.lucaneg.oo.analyzer.analyses.strings.stringsuffix;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringEnvironment;

/**
 * Suffix environment, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi "A suite of abstract
 * domains for static analysis of string values." in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class StringSuffixEnvironment extends BaseStringEnvironment<StringSuffixLattice, StringSuffixEnvironment> {

	/**
	 * Builds an empty string suffix environment
	 */
	public StringSuffixEnvironment() {
		super();
	}

	/**
	 * Builds a string suffix environment by shallow copying the given one
	 * 
	 * @param other the original environment
	 */
	private StringSuffixEnvironment(StringSuffixEnvironment other) {
		super(other.approximations);
	}

	@Override
	public StringSuffixEnvironment copy() {
		return new StringSuffixEnvironment(this);
	}

	@Override
	protected StringSuffixLattice latticeBottom() {
		return StringSuffixLattice.getBottom();
	}

	@Override
	protected StringSuffixLattice latticeTop() {
		return StringSuffixLattice.getTop();
	}
}
