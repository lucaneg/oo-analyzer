package it.lucaneg.oo.analyzer.analyses.value.domains.strings.charinclusion;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.strings.AbstractStringLattice;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability;

/**
 * Char Inclusion domain, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class CharInclusionLattice extends AbstractStringLattice<CharInclusionLattice> {
	
	public static final char TOP_CHAR = '\u0372';

	/**
	 * The unique top element
	 */
	private static final CharInclusionLattice TOP = new CharInclusionLattice(null, null) {
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
			return String.valueOf(TOP_CHAR);
		}
	};

	/**
	 * The unique bottom element
	 */
	private static final CharInclusionLattice BOTTOM = new CharInclusionLattice(null, null) {
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
	
	private final Set<Character> includedChars, possiblyIncludedChars;
	
	CharInclusionLattice(Set<Character> includedChars, Set<Character> possiblyIncludedChars) {
		this.includedChars = includedChars;
		this.possiblyIncludedChars = possiblyIncludedChars;
	}
	
	public CharInclusionLattice() {
		includedChars = new TreeSet<>();
		possiblyIncludedChars = new TreeSet<>();
	}
	
	public CharInclusionLattice(String string) {
		this();
		string.chars().forEach(c -> {
			includedChars.add((char) c);
			possiblyIncludedChars.add((char) c);
		});
	}
	
	public Set<Character> getIncludedChars() {
		return includedChars;
	}
	
	public Set<Character> getPossiblyIncludedChars() {
		return possiblyIncludedChars;
	}
	
	@Override
	protected CharInclusionLattice lubAux(CharInclusionLattice other) {
		Set<Character> included = new TreeSet<>(includedChars);
		Set<Character> possibly = new TreeSet<>(possiblyIncludedChars);
		included.retainAll(other.includedChars);
		possibly.addAll(other.possiblyIncludedChars);
		return new CharInclusionLattice(included, possibly);
	}

	CharInclusionLattice join(CharInclusionLattice other) {
		Set<Character> included = new TreeSet<>(includedChars);
		Set<Character> possibly = new TreeSet<>(possiblyIncludedChars);
		included.retainAll(other.includedChars);
		possibly.addAll(other.possiblyIncludedChars);
		return new CharInclusionLattice(included, possibly);
	}

	@Override
	protected CharInclusionLattice wideningAux(CharInclusionLattice other) {
		return lubAux(other);
	}
	
	@Override
	public CharInclusionLattice bottom() {
		return getBottom();
	}
	
	public static CharInclusionLattice getBottom() {
		return BOTTOM;
	}

	@Override
	public CharInclusionLattice top() {
		return getTop();
	}
	
	public static CharInclusionLattice getTop() {
		return TOP;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((includedChars == null) ? 0 : includedChars.hashCode());
		result = prime * result + ((possiblyIncludedChars == null) ? 0 : possiblyIncludedChars.hashCode());
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
		CharInclusionLattice other = (CharInclusionLattice) obj;
		if (includedChars == null) {
			if (other.includedChars != null)
				return false;
		} else if (!includedChars.equals(other.includedChars))
			return false;
		if (possiblyIncludedChars == null) {
			if (other.possiblyIncludedChars != null)
				return false;
		} else if (!possiblyIncludedChars.equals(other.possiblyIncludedChars))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return includedChars.toString() + "(" + possiblyIncludedChars + ")";
	}

	@Override
	public CharInclusionLattice mk(String string) {
		return new CharInclusionLattice(string);
	}

	@Override
	public Satisfiability contains(CharInclusionLattice other) {
		if (isTop() || other.isTop())
			return Satisfiability.UNKNOWN;

		if (other.getIncludedChars().size() == 1 && other.getPossiblyIncludedChars().equals(other.getIncludedChars())
				&& getIncludedChars().containsAll(other.getIncludedChars()))
			// only case when we can return true
			return Satisfiability.SATISFIED;

		Set<Character> includedChars = new HashSet<>(other.getIncludedChars());
		includedChars.removeAll(getPossiblyIncludedChars());
		if (!includedChars.isEmpty())
			return Satisfiability.NOT_SATISFIED;

		return Satisfiability.UNKNOWN;
	}

	@Override
	public Satisfiability startsWith(CharInclusionLattice other) {
		if (isTop() || other.isTop())
			return Satisfiability.UNKNOWN;

		Set<Character> includedChars = new HashSet<>(other.getIncludedChars());
		includedChars.removeAll(other.getPossiblyIncludedChars());
		if (!includedChars.isEmpty())
			return Satisfiability.NOT_SATISFIED;

		return Satisfiability.UNKNOWN;
	}

	@Override
	public Satisfiability endsWith(CharInclusionLattice other) {
		if (isTop() || other.isTop())
			return Satisfiability.UNKNOWN;

		Set<Character> includedChars = new HashSet<>(other.getIncludedChars());
		includedChars.removeAll(other.getPossiblyIncludedChars());
		if (!includedChars.isEmpty())
			return Satisfiability.NOT_SATISFIED;

		return Satisfiability.UNKNOWN;
	}

	@Override
	public Satisfiability isEquals(CharInclusionLattice other) {
		if (isTop() || other.isTop())
			return Satisfiability.UNKNOWN;

		if (!getIncludedChars().containsAll(other.getIncludedChars()))
			return Satisfiability.NOT_SATISFIED;

		return Satisfiability.UNKNOWN;
	}

	@Override
	public CharInclusionLattice concat(CharInclusionLattice other) {
		if (isTop())
			return getTop();

		Set<Character> included = new TreeSet<>(getIncludedChars());
		Set<Character> possibly = new TreeSet<>(getPossiblyIncludedChars());

		if (other.isTop()) 
			possibly.add(CharInclusionLattice.TOP_CHAR);
		else {
			included.addAll(other.getIncludedChars());
			possibly.addAll(other.getPossiblyIncludedChars());
		}
		
		return new CharInclusionLattice(included, possibly);
	}

	@Override
	public CharInclusionLattice substring(int begin, int end) {
		if (isTop())
			return getTop();

		return new CharInclusionLattice(new TreeSet<>(), getPossiblyIncludedChars());
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractIntegerLattice indexOf(CharInclusionLattice str, AbstractIntegerLattice singleton) {
		return (AbstractIntegerLattice) singleton.mk(-1).widening(singleton.mk(Integer.MAX_VALUE));
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractIntegerLattice length(AbstractIntegerLattice singleton) {
		return (AbstractIntegerLattice) singleton.mk(0).widening(singleton.mk(Integer.MAX_VALUE));
	}

	@Override
	public CharInclusionLattice replace(CharInclusionLattice toReplace, CharInclusionLattice str) {
		if (isTop() || toReplace.isTop() || str.isTop())
			return getTop();

		if (!getIncludedChars().containsAll(toReplace.getIncludedChars()))
			// no replace for sure
			return this;
		Set<Character> included = new TreeSet<>(getIncludedChars());
		Set<Character> possibly = new TreeSet<>(getPossiblyIncludedChars());
		// since we do not know if the replace will happen, we move everything to the
		// possibly included characters
		included.removeAll(toReplace.getIncludedChars());
		possibly.addAll(toReplace.getIncludedChars());

		included.removeAll(toReplace.getPossiblyIncludedChars());
		Set<Character> tmp = new TreeSet<>(toReplace.getPossiblyIncludedChars());
		tmp.retainAll(getIncludedChars()); // just the ones that we removed before
		possibly.addAll(tmp);

		// add the second string
		possibly.addAll(str.getIncludedChars());
		possibly.addAll(str.getPossiblyIncludedChars());

		return new CharInclusionLattice(included, possibly);
	}

	@Override
	public Boolean isEqualTo(CharInclusionLattice other) {
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return equals(other);
	}

	@Override
	public Boolean isLessThen(CharInclusionLattice other) {
		return null;
	}

	@Override
	public Boolean isGreaterThan(CharInclusionLattice other) {
		return null;
	}
}
