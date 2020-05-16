package it.lucaneg.oo.analyzer.analyses.strings.dfa;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringSatisfiabilityEvaluator;
import it.univr.fsm.machine.Automaton;

public class DfaSatisfiabilityEvaluator extends BaseStringSatisfiabilityEvaluator<DfaLattice> {
	@Override
	protected DfaLattice latticeBottom() {
		return DfaLattice.getBottom();
	}

	@Override
	protected DfaLattice latticeTop() {
		return DfaLattice.getTop();
	}

	@Override
	protected Satisfiability satisfiesContains(DfaLattice rec, DfaLattice par) {
		int includes = Automaton.includes(rec.getString(), par.getString());

		if (includes == 1)
			return Satisfiability.SATISFIED;

		if (includes == -1)
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesEndsWith(DfaLattice rec, DfaLattice par) {
		int includes = Automaton.endsWith(rec.getString(), par.getString());

		if (includes == 1)
			return Satisfiability.SATISFIED;

		if (includes == -1)
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesEquals(DfaLattice rec, DfaLattice par) {
		if (rec.getString().equals(par.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesStartsWith(DfaLattice rec, DfaLattice par) {
		int includes = Automaton.startsWith(rec.getString(), par.getString());

		if (includes == 1)
			return Satisfiability.SATISFIED;

		if (includes == -1)
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}
}
