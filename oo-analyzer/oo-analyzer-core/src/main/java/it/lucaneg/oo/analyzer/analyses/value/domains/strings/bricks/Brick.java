package it.lucaneg.oo.analyzer.analyses.value.domains.strings.bricks;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

/**
 * A Brick, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class Brick {
	
	/**
	 * The unique top element
	 */
	static final Brick TOP = new Brick("\u0372", 1, 1) {
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
//			return "\u0372";
//		}
	};

	/**
	 * The unique bottom element
	 */
	static final Brick BOTTOM = new Brick("_|_", 1, 1) {
		@Override
		public boolean equals(Object obj) {
			return this == obj;
		}

		@Override
		public int hashCode() {
			return "BOTTOM".hashCode();
		}

//		@Override
//		public String toString() {
//			return "_|_";
//		}
	};
	
	private final Set<String> strings;
	private final int min;
	private final int max; // -1 means infinity
	
	private Brick(int min, int max) {
		this.strings = new TreeSet<>();
		this.min = min;
		this.max = max;
	}
	
	Brick(String string, int min, int max) {
		this(min, max);
		strings.add(string);
	}
	
	Brick(Set<String> strings, int min, int max) {
		this(min, max);
		this.strings.addAll(strings);
	}
	
	int getMax() {
		return max;
	}
	
	int getMin() {
		return min;
	}
	
	Set<String> getStrings() {
		return strings;
	}
	
	boolean inRelationWith(Brick other) {
		if (this == BOTTOM || other == TOP)
			return true;
		
		if (min < other.min)
			return false;
		
		if (max > other.max)
			return false;
		
		if (other.strings.containsAll(strings))
			return true;
		
		// Extra condition: if the strings are different, but each string is contained
		// in all of the strings of other, than other is more general than this
		for (String s : strings)
			for (String ss : other.strings)
				if (!ss.contains(s))
					return false;
		
		return true; 
	}
	
	Brick lub(Brick other) {
		if (this == TOP || other == TOP)
			return TOP;
		
		if (other == null || other == BOTTOM)
			return this;
		
		if (this == BOTTOM)
			return other;
		
		Set<String> result = new TreeSet<>(strings);
		result.addAll(other.strings);
		if (max == -1 || other.max == -1)
			return new Brick(result, Math.min(min, other.min), -1);
		return new Brick(result, Math.min(min, other.min), Math.max(max, other.max));
	}
	

	Brick widening(Brick other) {
		if (this == TOP || other == TOP)
			return TOP;
		
		Set<String> result = new TreeSet<>(strings);
		result.addAll(other.strings);
		if (result.size() > BricksLattice.K_S)
			return TOP;
		
		int min = Math.min(this.min, other.min);
		if (max == -1 || other.max == -1)
			return new Brick(result, min, -1);
		
		int max = Math.max(this.max, other.max);
		if (max - min > BricksLattice.K_I)
			return new Brick(result, min, -1);
		
		return new Brick(result, min, max);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + max;
		result = prime * result + min;
		result = prime * result + ((strings == null) ? 0 : strings.hashCode());
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
		Brick other = (Brick) obj;
		if (max != other.max)
			return false;
		if (min != other.min)
			return false;
		if (strings == null) {
			if (other.strings != null)
				return false;
		} else if (!strings.equals(other.strings))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[{" + StringUtils.join(strings, ", ") + "}](" + min + "," + (max == -1 ? "\u221e" : String.valueOf(max)) + ")";
	}
}
