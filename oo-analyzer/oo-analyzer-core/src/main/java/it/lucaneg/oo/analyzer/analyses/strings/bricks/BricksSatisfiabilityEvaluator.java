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
		if (rec.isTop() || par.isTop())
			return Satisfiability.UNKNOWN;
		
		if (satisfiesContains(rec, par) == Satisfiability.NOT_SATISFIED)
			return Satisfiability.NOT_SATISFIED;

		if (rec.equals(par))
			return Satisfiability.SATISFIED;
		
		if (par.getBricks().size() == 1 
				&& par.getBricks().get(0).getMin() == 1 
				&& par.getBricks().get(0).getMax() == 1 
				&& par.getBricks().get(0).getStrings().size() == 1) {
			boolean may = false; 
			boolean must = false;
			String search = par.getBricks().get(0).getStrings().iterator().next();
			for (Brick b : rec.getBricks()) {
				int count = 0;
				for (String a : b.getStrings())
					if (a.endsWith(search))
						count++;
				if (count > 0)
					may = true;
				if (count == b.getStrings().size() && b.getMin() > 0)
					must = true;
			}
			
			if (must)
				return Satisfiability.SATISFIED;
			
			if (may)
				return Satisfiability.UNKNOWN;
		
			return Satisfiability.NOT_SATISFIED;
		}

		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesStartsWith(BricksLattice rec, BricksLattice par) {
		if (rec.isTop() || par.isTop())
			return Satisfiability.UNKNOWN;
		
		if (satisfiesContains(rec, par) == Satisfiability.NOT_SATISFIED)
			return Satisfiability.NOT_SATISFIED;

		if (rec.equals(par))
			return Satisfiability.SATISFIED;
		
		if (par.getBricks().size() == 1 
				&& par.getBricks().get(0).getMin() == 1 
				&& par.getBricks().get(0).getMax() == 1 
				&& par.getBricks().get(0).getStrings().size() == 1) {
			boolean may = false; 
			boolean must = false;
			String search = par.getBricks().get(0).getStrings().iterator().next();
			for (Brick b : rec.getBricks()) {
				int count = 0;
				for (String a : b.getStrings())
					if (a.startsWith(search))
						count++;
				if (count > 0)
					may = true;
				if (count == b.getStrings().size() && b.getMin() > 0)
					must = true;
			}
			
			if (must)
				return Satisfiability.SATISFIED;
			
			if (may)
				return Satisfiability.UNKNOWN;
		
			return Satisfiability.NOT_SATISFIED;
		}

		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesContains(BricksLattice rec, BricksLattice par) {
		if (rec.isTop() || par.isTop())
			return Satisfiability.UNKNOWN;
		
		if (par.inRelationWith(rec))
			return Satisfiability.SATISFIED;
		
		if (par.getBricks().size() == 1 
				&& par.getBricks().get(0).getMin() == 1 
				&& par.getBricks().get(0).getMax() == 1 
				&& par.getBricks().get(0).getStrings().size() == 1) {
			boolean may = false; 
			boolean must = false;
			String search = par.getBricks().get(0).getStrings().iterator().next();
			for (Brick b : rec.getBricks()) {
				int count = 0;
				for (String a : b.getStrings())
					if (a.contains(search))
						count++;
				if (count > 0)
					may = true;
				if (count == b.getStrings().size() && b.getMin() > 0)
					must = true;
			}
			
			if (must)
				return Satisfiability.SATISFIED;
			
			if (may)
				return Satisfiability.UNKNOWN;
		
			return Satisfiability.NOT_SATISFIED;
		}
		
		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesEquals(BricksLattice rec, BricksLattice par) {
		if (rec.isTop() || par.isTop())
			return Satisfiability.UNKNOWN;
		
		if (rec.equals(par))
			return Satisfiability.SATISFIED;

		return Satisfiability.NOT_SATISFIED;
	}
}
