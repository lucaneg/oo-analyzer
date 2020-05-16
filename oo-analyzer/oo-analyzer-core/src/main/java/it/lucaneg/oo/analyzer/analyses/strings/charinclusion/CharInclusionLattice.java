package it.lucaneg.oo.analyzer.analyses.strings.charinclusion;

import java.util.Set;
import java.util.TreeSet;

import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractLattice;

/**
 * Char Inclusion domain, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class CharInclusionLattice extends AbstractLattice<CharInclusionLattice> {
	
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
}
