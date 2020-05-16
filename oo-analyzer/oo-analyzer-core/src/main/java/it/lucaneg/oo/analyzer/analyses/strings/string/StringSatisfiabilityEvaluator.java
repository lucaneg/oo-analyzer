package it.lucaneg.oo.analyzer.analyses.strings.string;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringSatisfiabilityEvaluator;

public class StringSatisfiabilityEvaluator extends BaseStringSatisfiabilityEvaluator<StringLattice> {
	@Override
	protected StringLattice latticeBottom() {
		return StringLattice.getBottom();
	}

	@Override
	protected StringLattice latticeTop() {
		return StringLattice.getTop();
	}

	@Override
	protected Satisfiability satisfiesContains(StringLattice rec, StringLattice par) {
		if (rec.getString().contains(par.getString()))
			return Satisfiability.SATISFIED;

		if (rec.getString().mayContain(par.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesEndsWith(StringLattice rec, StringLattice par) {
		if (rec.getString().endsWith(par.getString()))
			return Satisfiability.SATISFIED;

		if (rec.getString().mayEndWith(par.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesEquals(StringLattice rec, StringLattice par) {
		if (rec.getString().isEqualTo(par.getString()))
			return Satisfiability.SATISFIED;

		if (rec.getString().mayBeEqualTo(par.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesStartsWith(StringLattice rec, StringLattice par) {
		if (rec.getString().startsWith(par.getString()))
			return Satisfiability.SATISFIED;

		if (rec.getString().mayStartWith(par.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}
}
