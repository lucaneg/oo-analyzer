package it.lucaneg.oo.analyzer.analyses.strings.bricks;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringSatisfiabilityEvaluator;

public class BricksSatisfiabilityEvaluator extends BaseStringSatisfiabilityEvaluator<BricksLattice> {

	@Override
	protected BricksLattice latticeBottom() {
		return BricksLattice.getBottom();
	}

	@Override
	protected BricksLattice latticeTop() {
		return BricksLattice.getTop();
	}

	@Override
	protected Satisfiability satisfiesEndsWith(BricksLattice rec, BricksLattice par) {
		if (!par.inRelationWith(rec))
			return Satisfiability.NOT_SATISFIED;

		if (rec.equals(par))
			return Satisfiability.SATISFIED;

		int parlen = par.getBricks().size();
		int reclen = rec.getBricks().size();
		for (int i = 0; i < par.getBricks().size(); i++)
			if (!par.getBricks().get(parlen - 1 - i).equals(rec.getBricks().get(reclen - 1 - i)))
				return Satisfiability.NOT_SATISFIED;

		return Satisfiability.SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesStartsWith(BricksLattice rec, BricksLattice par) {
		if (!par.inRelationWith(rec))
			return Satisfiability.NOT_SATISFIED;

		if (rec.equals(par))
			return Satisfiability.SATISFIED;

		for (int i = 0; i < par.getBricks().size(); i++)
			if (!par.getBricks().get(i).equals(rec.getBricks().get(i)))
				return Satisfiability.NOT_SATISFIED;

		return Satisfiability.SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesContains(BricksLattice rec, BricksLattice par) {
		if (par.inRelationWith(rec))
			return Satisfiability.SATISFIED;
		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesEquals(BricksLattice rec, BricksLattice par) {
		if (rec.equals(par))
			return Satisfiability.SATISFIED;

		return Satisfiability.NOT_SATISFIED;
	}
}
