package it.lucaneg.oo.analyzer.analyses.value.domains.strings.dfa;

import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.strings.AbstractStringLattice;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability;
import it.univr.fsm.machine.Automaton;

/**
 * A lattice for string elements represented through dfa
 * TODO vincenzo
 * 
 * @author Luca Negrini
 */
public class DfaLattice extends AbstractStringLattice<DfaLattice> {

	/**
	 * The unique top element
	 */
	private static final DfaLattice TOP = new DfaLattice(Automaton.makeTopLanguage()) {
		@Override
		public boolean equals(Object obj) {
			return this == obj;
		}

		@Override
		public int hashCode() {
			return "TOP".hashCode();
		}
	};

	/**
	 * The unique bottom element
	 */
	private static final DfaLattice BOTTOM = new DfaLattice(Automaton.makeEmptyLanguage()) {
		@Override
		public boolean equals(Object obj) {
			return this == obj;
		}

		@Override
		public int hashCode() {
			return "BOTTOM".hashCode();
		}

		@Override
		public String toString() {
			return "_|_";
		}
	};

	/**
	 * The effective domain element
	 */
	private final Automaton string;

	/**
	 * Builds a lattice element containing the given string
	 * 
	 * @param string the string
	 */
	public DfaLattice(Automaton string) {
		this.string = string;
	}

	/**
	 * Yields the string represented by this lattice element
	 * 
	 * @return the string
	 */
	public Automaton getString() {
		return string;
	}

	@Override
	protected DfaLattice lubAux(DfaLattice other) {
		return new DfaLattice(Automaton.union(string, other.string));
	}

	@Override
	protected DfaLattice wideningAux(DfaLattice other) {
		return new DfaLattice(lubAux(other).string.widening(5));
	}

	@Override
	public DfaLattice bottom() {
		return getBottom();
	}

	@Override
	public DfaLattice top() {
		return getTop();
	}

	/**
	 * Yields the unique bottom element
	 * 
	 * @return the bottom element
	 */
	public static DfaLattice getBottom() {
		return BOTTOM;
	}

	/**
	 * Yields the unique top element
	 * 
	 * @return the top element
	 */
	public static DfaLattice getTop() {
		return TOP;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((string == null) ? 0 : string.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DfaLattice other = (DfaLattice) obj;
		if (string == null) {
			if (other.string != null)
				return false;
		} else if (!string.equals(other.string))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return string.toString();
	}

	@Override
	public DfaLattice mk(String string) {
		return new DfaLattice(Automaton.makeAutomaton(string));
	}

	@Override
	public Satisfiability contains(DfaLattice other) {
		int includes = Automaton.includes(getString(), other.getString());

		if (includes == 1)
			return Satisfiability.SATISFIED;

		if (includes == -1)
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public Satisfiability startsWith(DfaLattice other) {
		int includes = Automaton.startsWith(getString(), other.getString());

		if (includes == 1)
			return Satisfiability.SATISFIED;

		if (includes == -1)
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public Satisfiability endsWith(DfaLattice other) {
		int includes = Automaton.endsWith(getString(), other.getString());

		if (includes == 1)
			return Satisfiability.SATISFIED;

		if (includes == -1)
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public Satisfiability isEquals(DfaLattice other) {
		if (getString().equals(other.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public DfaLattice concat(DfaLattice other) {
		return new DfaLattice(Automaton.concat(getString(), other.getString()));
	}

	@Override
	public DfaLattice substring(int begin, int end) {
		return new DfaLattice(Automaton.substring(getString(), begin, end));
	}
	
	@Override
	public AbstractIntegerLattice<?> indexOf(DfaLattice str, AbstractIntegerLattice<?> singleton) {
		return singleton.mk(Automaton.indexOf(getString(), str.getString()));
	}
	
	@Override
	public AbstractIntegerLattice<?> length(AbstractIntegerLattice<?> singleton) {
		return singleton.mk(Automaton.length(getString()));
	}

	@Override
	public DfaLattice replace(DfaLattice toReplace, DfaLattice str) {
		return new DfaLattice(Automaton.replace(getString(), toReplace.getString(), str.getString()));
	}

	@Override
	public Boolean isEqualTo(DfaLattice other) {
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return getString().equals(other.getString());
	}

	@Override
	public Boolean isLessThen(DfaLattice other) {
		return null;
	}

	@Override
	public Boolean isGreaterThan(DfaLattice other) {
		return null;
	}
}
