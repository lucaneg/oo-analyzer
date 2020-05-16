package it.lucaneg.oo.analyzer.analyses.strings.stringprefix;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringAnalysis;
import it.lucaneg.oo.sdk.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.sdk.analyzer.fixpoint.IterationBasedFixpoint;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;

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

	private final StringPrefixExpressionEvaluator evaluator = new StringPrefixExpressionEvaluator();

	private final StringPrefixSatisfiabilityEvaluator satisfiability = new StringPrefixSatisfiabilityEvaluator();

	@Override
	public StringPrefixExpressionEvaluator getExpressionEvaluator() {
		return evaluator;
	}

	@Override
	public StringPrefixSatisfiabilityEvaluator getSatisfiabilityEvaluator() {
		return satisfiability;
	}
	
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
