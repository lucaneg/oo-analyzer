package it.lucaneg.oo.analyzer.analyses.strings.bricks;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringEnvironment;

/**
 * Bricks environment, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi "A suite of abstract
 * domains for static analysis of string values." in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class BricksEnvironment extends BaseStringEnvironment<BricksLattice, BricksEnvironment> {

	public BricksEnvironment() {
		super();
	}

	private BricksEnvironment(BricksEnvironment other) {
		super(other.approximations);
	}

	@Override
	public BricksEnvironment copy() {
		return new BricksEnvironment(this);
	}

	@Override
	protected BricksLattice latticeBottom() {
		return BricksLattice.getBottom();
	}

	@Override
	protected BricksLattice latticeTop() {
		return BricksLattice.getTop();
	}

}
