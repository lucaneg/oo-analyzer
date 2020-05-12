package it.lucaneg.oo.analyzer.analyses.strings.charinclusion;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringAnalysis;
import it.lucaneg.oo.sdk.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.sdk.analyzer.fixpoint.IterationBasedFixpoint;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;

/**
 * Char Inclusion analysis, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class CharInclusionAnalysis extends BaseStringAnalysis<CharInclusionLattice, CharInclusionEnvironment> {

	@Override
	protected CharInclusionLattice latticeBottom() {
		return CharInclusionLattice.getBottom();
	}

	@Override
	public CharInclusionEnvironment mkEmptyEnvironment() {
		return new CharInclusionEnvironment();
	}
	
	@Override
	protected Fixpoint<CharInclusionLattice, CharInclusionEnvironment> mkFixpoint(MCodeBlock code) {
		return new IterationBasedFixpoint<>(code, 0);
	}
}
