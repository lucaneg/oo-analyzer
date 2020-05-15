package it.lucaneg.oo.analyzer.analyses.strings.dfa;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringEnvironment;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;
import it.univr.fsm.machine.Automaton;

/**
 * An environment for string elements represented through dfa
 * TODO vincenzo
 * 
 * @author Luca Negrini
 */
public class DfaEnvironment extends BaseStringEnvironment<DfaLattice, DfaEnvironment> {

	/**
	 * Builds an empty string environment
	 */
	public DfaEnvironment() {
		super();
	}

	/**
	 * Builds a string environment by shallow copying the given one
	 * 
	 * @param other the original environment
	 */
	private DfaEnvironment(DfaEnvironment other) {
		super(other.approximations);
	}

	@Override
	public DfaEnvironment copy() {
		return new DfaEnvironment(this);
	}

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
