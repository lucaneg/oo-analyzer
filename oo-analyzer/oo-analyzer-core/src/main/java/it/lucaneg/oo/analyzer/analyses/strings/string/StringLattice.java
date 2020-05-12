package it.lucaneg.oo.analyzer.analyses.strings.string;

import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractLattice;
import it.unive.strings.AutomatonString;

/**
 * A lattice for string elements represented through automata
 * 
 * @author Luca Negrini
 */
public class StringLattice extends AbstractLattice<StringLattice> {

	/**
	 * The unique top element
	 */
	private static final StringLattice TOP = new StringLattice() {
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
	private static final StringLattice BOTTOM = new StringLattice(null) {
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
	private final AutomatonString string;

	/**
	 * Builds a lattice element containing the top string
	 */
	private StringLattice() {
		this(new AutomatonString());
	}

	/**
	 * Builds a lattice element containing the given string
	 * 
	 * @param string the string
	 */
	public StringLattice(AutomatonString string) {
		this.string = string;
	}

	/**
	 * Yields the string represented by this lattice element
	 * 
	 * @return the string
	 */
	public AutomatonString getString() {
		return string;
	}

	@Override
	protected StringLattice lubAux(StringLattice other) {
		return new StringLattice(string.lub(other.string));
	}

	@Override
	protected StringLattice wideningAux(StringLattice other) {
		return new StringLattice(string.widen(other.string));
	}

	@Override
	public StringLattice bottom() {
		return getBottom();
	}

	@Override
	public StringLattice top() {
		return getTop();
	}

	/**
	 * Yields the unique bottom element
	 * 
	 * @return the bottom element
	 */
	public static StringLattice getBottom() {
		return BOTTOM;
	}

	/**
	 * Yields the unique top element
	 * 
	 * @return the top element
	 */
	public static StringLattice getTop() {
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
		StringLattice other = (StringLattice) obj;
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
