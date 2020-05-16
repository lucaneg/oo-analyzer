package it.lucaneg.oo.analyzer.analyses.strings.stringprefix;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringLattice;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractLattice;

/**
 * Prefix domain, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class StringPrefixLattice extends BaseStringLattice<StringPrefixLattice> {

	/**
	 * The unique top element
	 */
	private static final StringPrefixLattice TOP = new StringPrefixLattice(null) {
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
	private static final StringPrefixLattice BOTTOM = new StringPrefixLattice(null) {
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
	private final String prefix;

	/**
	 * Builds a lattice element containing the given prefix
	 * 
	 * @param prefix the prefix
	 */
	public StringPrefixLattice(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Yields the prefix represented by this lattice element
	 * 
	 * @return the string
	 */
	public String getPrefix() {
		return prefix;
	}

	@Override
	protected StringPrefixLattice lubAux(StringPrefixLattice other) {
		if (prefix.equals(other.prefix))
			return this;
		
		String shorter = prefix, longer = other.prefix;
		if (prefix.length() > other.prefix.length()) {
			shorter = other.prefix;
			longer = prefix;
		}
		
		String common = "";
		for (int i = 0; i < shorter.length(); i++) 
			if (shorter.charAt(i) == longer.charAt(i))
				common += shorter.charAt(i);
			else 
				break;
		
		return new StringPrefixLattice(common);
	}

	@Override
	protected StringPrefixLattice wideningAux(StringPrefixLattice other) {
		return lubAux(other);
	}

	@Override
	public StringPrefixLattice bottom() {
		return getBottom();
	}

	@Override
	public StringPrefixLattice top() {
		return getTop();
	}

	/**
	 * Yields the unique bottom element
	 * 
	 * @return the bottom element
	 */
	public static StringPrefixLattice getBottom() {
		return BOTTOM;
	}

	/**
	 * Yields the unique top element
	 * 
	 * @return the top element
	 */
	public static StringPrefixLattice getTop() {
		return TOP;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
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
		StringPrefixLattice other = (StringPrefixLattice) obj;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "\"" + prefix + "\"";
	}

	@Override
	public Boolean isEqualTo(AbstractLattice<?> other) {
		if (!(other instanceof StringPrefixLattice))
			return null;
		
		StringPrefixLattice o = (StringPrefixLattice) other;
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return getPrefix().equals(o.getPrefix());
	}
	
	@Override
	public Boolean isGreaterThan(AbstractLattice<?> other) {
		return null;
	}
	
	@Override
	public Boolean isLessThen(AbstractLattice<?> other) {
		return null;
	}
}
