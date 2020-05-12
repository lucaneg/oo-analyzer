package it.luceng.oo.analyzer.analyses.strings.stringsuffix;

import it.lucaneg.oo.api.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.api.analyzer.fixpoint.IterationBasedFixpoint;
import it.lucaneg.oo.api.analyzer.program.MCodeBlock;
import it.luceng.oo.analyzer.analyses.strings.BaseStringAnalysis;

/**
 * Suffix analysis, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class StringSuffixAnalysis extends BaseStringAnalysis<StringSuffixLattice, StringSuffixEnvironment> {

	@Override
	public StringSuffixEnvironment mkEmptyEnvironment() {
		return new StringSuffixEnvironment();
	}
	
	@Override
	protected StringSuffixLattice latticeBottom() {
		return StringSuffixLattice.getBottom();
	}
	
	@Override
	protected Fixpoint<StringSuffixLattice, StringSuffixEnvironment> mkFixpoint(MCodeBlock code) {
		return new IterationBasedFixpoint<>(code, 0);
	}
}
