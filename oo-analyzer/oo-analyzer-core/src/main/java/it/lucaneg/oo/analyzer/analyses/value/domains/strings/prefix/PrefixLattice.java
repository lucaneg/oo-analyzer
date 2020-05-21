package it.lucaneg.oo.analyzer.analyses.value.domains.strings.prefix;

import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.strings.AbstractStringLattice;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability;

/**
 * Prefix domain, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class PrefixLattice extends AbstractStringLattice<PrefixLattice> {

	/**
	 * The unique top element
	 */
	private static final PrefixLattice TOP = new PrefixLattice("") {
		@Override
		public boolean equals(Object obj) {
			return this == obj;
		}

		@Override
		public int hashCode() {
			return "TOP".hashCode();
		}
		
//		@Override
//		public String toString() {
//			return String.valueOf(Analysis.TOP_CHAR);
//		}
	};

	/**
	 * The unique bottom element
	 */
	private static final PrefixLattice BOTTOM = new PrefixLattice(null) {
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
	public PrefixLattice(String prefix) {
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
	protected PrefixLattice lubAux(PrefixLattice other) {
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
		
		return new PrefixLattice(common);
	}

	@Override
	protected PrefixLattice wideningAux(PrefixLattice other) {
		return lubAux(other);
	}

	@Override
	public PrefixLattice bottom() {
		return getBottom();
	}

	@Override
	public PrefixLattice top() {
		return getTop();
	}

	/**
	 * Yields the unique bottom element
	 * 
	 * @return the bottom element
	 */
	public static PrefixLattice getBottom() {
		return BOTTOM;
	}

	/**
	 * Yields the unique top element
	 * 
	 * @return the top element
	 */
	public static PrefixLattice getTop() {
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
		PrefixLattice other = (PrefixLattice) obj;
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
	public PrefixLattice mk(String string) {
		return new PrefixLattice(string);
	}

	@Override
	public Satisfiability contains(PrefixLattice other) {
		if (other.prefix.length() == 1 && prefix.contains(other.prefix))
			return Satisfiability.SATISFIED;
		return Satisfiability.UNKNOWN;
	}

	@Override
	public Satisfiability startsWith(PrefixLattice other) {
		if (!isTop() && !other.isTop() && getPrefix().startsWith(other.getPrefix()))
			return Satisfiability.SATISFIED;

		return Satisfiability.UNKNOWN;
	}

	@Override
	public Satisfiability endsWith(PrefixLattice other) {
		return Satisfiability.UNKNOWN;
	}

	@Override
	public Satisfiability isEquals(PrefixLattice other) {
		return Satisfiability.UNKNOWN;
	}

	@Override
	public PrefixLattice concat(PrefixLattice other) {
		return this;
	}

	@Override
	public PrefixLattice substring(int begin, int end) {
		if (isTop())
			return this;

		if (end <= getPrefix().length())
			return new PrefixLattice(getPrefix().substring(begin, end));
		else if (begin < getPrefix().length())
			return new PrefixLattice(getPrefix().substring(begin));

		return new PrefixLattice("");
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractIntegerLattice indexOf(PrefixLattice str, AbstractIntegerLattice singleton) {
		return (AbstractIntegerLattice) singleton.mk(-1).widening(singleton.mk(Integer.MAX_VALUE));
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractIntegerLattice length(AbstractIntegerLattice singleton) {
		return (AbstractIntegerLattice) singleton.mk(prefix.length()).widening(singleton.mk(Integer.MAX_VALUE));
	}

	@Override
	public PrefixLattice replace(PrefixLattice toReplace, PrefixLattice str) {
		if (isTop() || toReplace.isTop() || str.isTop())
			return getTop();

		String replace = toReplace.getPrefix();
		String string = str.getPrefix();
		String target = getPrefix();

		if (!target.contains(replace))
			return this;

		return new PrefixLattice(target.replace(replace, string));
	}

	@Override
	public Boolean isEqualTo(PrefixLattice other) {
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return getPrefix().equals(other.getPrefix());
	}

	@Override
	public Boolean isLessThen(PrefixLattice other) {
		return null;
	}

	@Override
	public Boolean isGreaterThan(PrefixLattice other) {
		return null;
	}
}
