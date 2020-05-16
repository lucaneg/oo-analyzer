package it.lucaneg.oo.analyzer.analyses.strings.charinclusion;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringEnvironment;

/**
 * Char Inclusion environment, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi "A suite of abstract
 * domains for static analysis of string values." in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class CharInclusionEnvironment extends BaseStringEnvironment<CharInclusionLattice, CharInclusionEnvironment> {

	private CharInclusionEnvironment(CharInclusionEnvironment other) {
		super(other.approximations);
	}

	public CharInclusionEnvironment() {
		super();
	}

	@Override
	public CharInclusionEnvironment copy() {
		return new CharInclusionEnvironment(this);
	}

	@Override
	protected CharInclusionLattice latticeBottom() {
		return CharInclusionLattice.getBottom();
	}

	@Override
	protected CharInclusionLattice latticeTop() {
		return CharInclusionLattice.getTop();
	}
}
