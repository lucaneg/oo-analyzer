package it.luceng.oo.analyzer.analyses.strings.string;

import it.lucaneg.oo.api.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.api.analyzer.fixpoint.SizeBasedFixpoint;
import it.lucaneg.oo.api.analyzer.program.MCodeBlock;
import it.luceng.oo.analyzer.analyses.strings.BaseStringAnalysis;

/**
 * An analysis of string elements represented through automata
 * 
 * @author Luca Negrini
 */
public class StringAnalysis extends BaseStringAnalysis<StringLattice, StringEnvironment> {

	@Override
	public StringEnvironment mkEmptyEnvironment() {
		return new StringEnvironment();
	}
	
	@Override
	protected StringLattice latticeBottom() {
		return StringLattice.getBottom();
	}
	
	@Override
	protected Fixpoint<StringLattice, StringEnvironment> mkFixpoint(MCodeBlock code) {
		return new SizeBasedFixpoint<>(code, this::decide);
	}
	
	private boolean decide(StringLattice env) {
		return !env.isTop() && !env.isBottom() && env.getString().size() > 5;
	}
}
