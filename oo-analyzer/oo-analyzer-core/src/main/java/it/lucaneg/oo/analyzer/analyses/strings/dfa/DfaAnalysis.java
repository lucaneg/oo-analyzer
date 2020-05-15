package it.lucaneg.oo.analyzer.analyses.strings.dfa;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringAnalysis;
import it.lucaneg.oo.sdk.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.sdk.analyzer.fixpoint.SizeBasedFixpoint;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;

/**
 * An analysis of string elements represented through dfa
 * TODO vincenzo
 * 
 * @author Luca Negrini
 */
public class DfaAnalysis extends BaseStringAnalysis<DfaLattice, DfaEnvironment> {

	private static final int WIDENING_SIZE_THRESHOLD = 5;

	@Override
	public DfaEnvironment mkEmptyEnvironment() {
		return new DfaEnvironment();
	}
	
	@Override
	protected DfaLattice latticeBottom() {
		return DfaLattice.getBottom();
	}
	
	@Override
	protected Fixpoint<DfaLattice, DfaEnvironment> mkFixpoint(MCodeBlock code) {
		return new SizeBasedFixpoint<>(code, this::decide);
	}
	
	private boolean decide(DfaLattice env) {
		return !env.isTop() && !env.isBottom() && env.getString().getStates().size() > WIDENING_SIZE_THRESHOLD;
	}
}
