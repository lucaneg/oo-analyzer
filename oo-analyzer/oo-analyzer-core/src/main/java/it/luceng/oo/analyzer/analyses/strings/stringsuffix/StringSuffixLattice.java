package it.luceng.oo.analyzer.analyses.strings.stringsuffix;

import it.lucaneg.oo.api.analyzer.analyses.impl.AbstractLattice;

/**
 * Suffix lattice, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class StringSuffixLattice extends AbstractLattice<StringSuffixLattice> {

	/**
	 * The unique top element
	 */
	private static final StringSuffixLattice TOP = new StringSuffixLattice(null) {
		@Override
		public boolean equals(Object obj) {
			return this == obj;
		}

		@Override
		public int hashCode() {
			return "TOP".hashCode();
		}
		
		@Override
		public String toString() {
			return "\u0372";
		}
	};

	/**
	 * The unique bottom element
	 */
	private static final StringSuffixLattice BOTTOM = new StringSuffixLattice(null) {
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
	private final String suffix;

	/**
	 * Builds a lattice element containing the given suffix
	 * 
	 * @param suffix the suffix
	 */
	public StringSuffixLattice(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Yields the suffix represented by this lattice element
	 * 
	 * @return the string
	 */
	public String getSuffix() {
		return suffix;
	}

	@Override
	protected StringSuffixLattice lubAux(StringSuffixLattice other) {
		if (suffix.equals(other.suffix))
			return this;
		
		String shorter = suffix, longer = other.suffix;
		if (suffix.length() > other.suffix.length()) {
			shorter = other.suffix;
			longer = suffix;
		}
		
		StringBuilder common = new StringBuilder();
		for (int i = 0; i < shorter.length(); i++) 
			if (shorter.charAt(shorter.length() - 1 - i) == longer.charAt(longer.length() - 1 - i))
				common.append(shorter.charAt(shorter.length() - 1 - i));
			else 
				break;
		
		common.reverse();
		return new StringSuffixLattice(common.toString());
	}

	@Override
	protected StringSuffixLattice wideningAux(StringSuffixLattice other) {
		return lubAux(other);
	}

	@Override
	public StringSuffixLattice bottom() {
		return getBottom();
	}

	@Override
	public StringSuffixLattice top() {
		return getTop();
	}

	/**
	 * Yields the unique bottom element
	 * 
	 * @return the bottom element
	 */
	public static StringSuffixLattice getBottom() {
		return BOTTOM;
	}

	/**
	 * Yields the unique top element
	 * 
	 * @return the top element
	 */
	public static StringSuffixLattice getTop() {
		return TOP;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
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
		StringSuffixLattice other = (StringSuffixLattice) obj;
		if (suffix == null) {
			if (other.suffix != null)
				return false;
		} else if (!suffix.equals(other.suffix))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "\"" + suffix + "\"";
	}

}
