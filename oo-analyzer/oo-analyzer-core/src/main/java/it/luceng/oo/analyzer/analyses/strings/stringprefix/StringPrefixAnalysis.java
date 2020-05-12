package it.luceng.oo.analyzer.analyses.strings.stringprefix;

import it.lucaneg.oo.api.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.api.analyzer.fixpoint.IterationBasedFixpoint;
import it.lucaneg.oo.api.analyzer.program.MCodeBlock;
import it.luceng.oo.analyzer.analyses.strings.BaseStringAnalysis;

/**
 * Prefix analysis, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class StringPrefixAnalysis extends BaseStringAnalysis<StringPrefixLattice, StringPrefixEnvironment> {

	@Override
	public StringPrefixEnvironment mkEmptyEnvironment() {
		return new StringPrefixEnvironment();
	}
	
	@Override
	protected StringPrefixLattice latticeBottom() {
		return StringPrefixLattice.getBottom();
	}
	
	@Override
	protected Fixpoint<StringPrefixLattice, StringPrefixEnvironment> mkFixpoint(MCodeBlock code) {
		return new IterationBasedFixpoint<>(code, 0);
	}
}
