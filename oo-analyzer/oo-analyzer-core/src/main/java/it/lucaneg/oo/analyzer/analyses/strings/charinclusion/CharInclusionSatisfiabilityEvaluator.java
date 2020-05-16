package it.lucaneg.oo.analyzer.analyses.strings.charinclusion;

import java.util.HashSet;
import java.util.Set;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringSatisfiabilityEvaluator;

public class CharInclusionSatisfiabilityEvaluator extends BaseStringSatisfiabilityEvaluator<CharInclusionLattice> {

	@Override
	protected CharInclusionLattice latticeBottom() {
		return CharInclusionLattice.getBottom();
	}

	@Override
	protected CharInclusionLattice latticeTop() {
		return CharInclusionLattice.getTop();
	}

	@Override
	protected Satisfiability satisfiesEndsWith(CharInclusionLattice rec, CharInclusionLattice par) {
		if (rec.isTop() || par.isTop())
			return Satisfiability.UNKNOWN;

		Set<Character> includedChars = new HashSet<>(par.getIncludedChars());
		includedChars.removeAll(par.getPossiblyIncludedChars());
		if (!includedChars.isEmpty())
			return Satisfiability.NOT_SATISFIED;

		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesStartsWith(CharInclusionLattice rec, CharInclusionLattice par) {
		if (rec.isTop() || par.isTop())
			return Satisfiability.UNKNOWN;

		Set<Character> includedChars = new HashSet<>(par.getIncludedChars());
		includedChars.removeAll(par.getPossiblyIncludedChars());
		if (!includedChars.isEmpty())
			return Satisfiability.NOT_SATISFIED;

		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesContains(CharInclusionLattice rec, CharInclusionLattice par) {
		if (rec.isTop() || par.isTop())
			return Satisfiability.UNKNOWN;

		if (par.getIncludedChars().size() == 1 && par.getPossiblyIncludedChars().equals(par.getIncludedChars())
				&& rec.getIncludedChars().containsAll(par.getIncludedChars()))
			// only case when we can return true
			return Satisfiability.SATISFIED;

		Set<Character> includedChars = new HashSet<>(par.getIncludedChars());
		includedChars.removeAll(rec.getPossiblyIncludedChars());
		if (!includedChars.isEmpty())
			return Satisfiability.NOT_SATISFIED;

		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesEquals(CharInclusionLattice rec, CharInclusionLattice par) {
		if (rec.isTop() || par.isTop())
			return Satisfiability.UNKNOWN;

		if (!rec.getIncludedChars().containsAll(par.getIncludedChars()))
			return Satisfiability.NOT_SATISFIED;

		return Satisfiability.UNKNOWN;
	}

}
