package it.lucaneg.oo.analyzer.analyses.strings.dfa;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringExpressionEvaluator;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;
import it.univr.fsm.machine.Automaton;

public class DfaExpressionEvaluator extends BaseStringExpressionEvaluator<DfaLattice> {
	@Override
	protected DfaLattice latticeBottom() {
		return DfaLattice.getBottom();
	}

	@Override
	protected DfaLattice latticeTop() {
		return DfaLattice.getTop();
	}

	@Override
	protected DfaLattice provideApproxFor(StringLiteral literal) {
		return new DfaLattice(Automaton.makeAutomaton(literal.getValue()));
	}

	@Override
	protected DfaLattice evalSubstring(DfaLattice receiver, int begin, int end) {
		return new DfaLattice(Automaton.substring(receiver.getString(), begin, end));
	}

	@Override
	protected DfaLattice evalConcat(DfaLattice receiver, DfaLattice parameter) {
		return new DfaLattice(Automaton.concat(receiver.getString(), parameter.getString()));
	}

	@Override
	protected DfaLattice evalReplace(DfaLattice receiver, DfaLattice first, DfaLattice second) {
		return new DfaLattice(Automaton.replace(receiver.getString(), first.getString(), second.getString()));
	}
}
