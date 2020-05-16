package it.lucaneg.oo.analyzer.analyses.strings.stringsuffix;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringSatisfiabilityEvaluator;

public class StringSuffixSatisfiabilityEvaluator extends BaseStringSatisfiabilityEvaluator<StringSuffixLattice> {

	@Override
	protected StringSuffixLattice latticeBottom() {
		return StringSuffixLattice.getBottom();
	}

	@Override
	protected StringSuffixLattice latticeTop() {
		return StringSuffixLattice.getTop();
	}

	@Override
	protected Satisfiability satisfiesContains(StringSuffixLattice rec, StringSuffixLattice par) {
		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesEndsWith(StringSuffixLattice rec, StringSuffixLattice par) {
		if (!rec.isTop() && !par.isTop() && rec.getSuffix().endsWith(par.getSuffix()))
			return Satisfiability.SATISFIED;

		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesEquals(StringSuffixLattice rec, StringSuffixLattice par) {
		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesStartsWith(StringSuffixLattice rec, StringSuffixLattice par) {
		return Satisfiability.UNKNOWN;
	}
}
