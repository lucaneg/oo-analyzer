package it.lucaneg.oo.analyzer.analyses.strings.stringprefix;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringSatisfiabilityEvaluator;

public class StringPrefixSatisfiabilityEvaluator extends BaseStringSatisfiabilityEvaluator<StringPrefixLattice> {

	@Override
	protected StringPrefixLattice latticeBottom() {
		return StringPrefixLattice.getBottom();
	}

	@Override
	protected StringPrefixLattice latticeTop() {
		return StringPrefixLattice.getTop();
	}

	@Override
	protected Satisfiability satisfiesContains(StringPrefixLattice rec, StringPrefixLattice par) {
		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesEndsWith(StringPrefixLattice rec, StringPrefixLattice par) {
		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesEquals(StringPrefixLattice rec, StringPrefixLattice par) {
		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesStartsWith(StringPrefixLattice rec, StringPrefixLattice par) {
		if (!rec.isTop() && !par.isTop() && rec.getPrefix().startsWith(par.getPrefix()))
			return Satisfiability.SATISFIED;

		return Satisfiability.UNKNOWN;
	}
}
