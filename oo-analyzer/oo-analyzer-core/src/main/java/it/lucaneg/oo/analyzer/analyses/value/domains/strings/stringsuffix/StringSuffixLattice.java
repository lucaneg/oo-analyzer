package it.lucaneg.oo.analyzer.analyses.value.domains.strings.stringsuffix;

import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.strings.AbstractStringLattice;
import it.lucaneg.oo.sdk.analyzer.analyses.Analysis;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability;

/**
 * Suffix lattice, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class StringSuffixLattice extends AbstractStringLattice<StringSuffixLattice> {

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
			return String.valueOf(Analysis.TOP_CHAR);
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

	@Override
	public StringSuffixLattice mk(String string) {
		return new StringSuffixLattice(string);
	}

	@Override
	public Satisfiability contains(StringSuffixLattice other) {
		if (other.suffix.length() == 1)
			return Satisfiability.fromBoolean(suffix.contains(other.suffix));
		return Satisfiability.UNKNOWN;
	}

	@Override
	public Satisfiability startsWith(StringSuffixLattice other) {
		return Satisfiability.UNKNOWN;
	}

	@Override
	public Satisfiability endsWith(StringSuffixLattice other) {
		if (!isTop() && !other.isTop() && getSuffix().endsWith(other.getSuffix()))
			return Satisfiability.SATISFIED;

		return Satisfiability.UNKNOWN;
	}

	@Override
	public Satisfiability isEquals(StringSuffixLattice other) {
		return Satisfiability.UNKNOWN;
	}

	@Override
	public StringSuffixLattice concat(StringSuffixLattice other) {
		return other;
	}

	@Override
	public StringSuffixLattice substring(int begin, int end) {
		return new StringSuffixLattice("");
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractIntegerLattice indexOf(StringSuffixLattice str, AbstractIntegerLattice singleton) {
		return (AbstractIntegerLattice) singleton.mk(-1).widening(singleton.mk(Integer.MAX_VALUE));
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractIntegerLattice length(AbstractIntegerLattice singleton) {
		return (AbstractIntegerLattice) singleton.mk(suffix.length()).widening(singleton.mk(Integer.MAX_VALUE));
	}

	@Override
	public StringSuffixLattice replace(StringSuffixLattice toReplace, StringSuffixLattice str) {
		if (isTop() || toReplace.isTop() || str.isTop())
			return getTop();

		String replace = toReplace.getSuffix();
		String string = str.getSuffix();
		String target = getSuffix();

		if (!target.contains(replace))
			return this;

		return new StringSuffixLattice(target.replace(replace, string));
	}

	@Override
	public Boolean isEqualTo(StringSuffixLattice other) {
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return getSuffix().equals(other.getSuffix());
	}

	@Override
	public Boolean isLessThen(StringSuffixLattice other) {
		return null;
	}

	@Override
	public Boolean isGreaterThan(StringSuffixLattice other) {
		return null;
	}
}
