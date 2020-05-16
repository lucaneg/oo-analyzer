package it.lucaneg.oo.analyzer.analyses.strings.dfa;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringLattice;
import it.univr.fsm.machine.Automaton;

/**
 * A lattice for string elements represented through dfa
 * TODO vincenzo
 * 
 * @author Luca Negrini
 */
public class DfaLattice extends BaseStringLattice<DfaLattice> {

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
}
