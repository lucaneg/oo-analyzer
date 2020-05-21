package it.lucaneg.oo.analyzer.analyses.value.domains.strings.bricks;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import it.lucaneg.oo.sdk.analyzer.analyses.Analysis;

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
	static final Brick TOP = new Brick(String.valueOf(Analysis.TOP_CHAR), 0, -1) {
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
	static final Brick BOTTOM = new Brick("_|_", 0, 0) {
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
	/**
	 * -1 means infinity
	 */
	private final int max; 
	
	Brick(int min, int max) {
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
	
	boolean lessOrEqual(Brick other) {
		if (this == BOTTOM || other == TOP)
			return true;
		
		return other.strings.containsAll(strings) && min >= other.min && max <= other.max;
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
		int max = (this.max == -1 || other.max == -1) ? -1 : Math.max(this.max, other.max);

		if (max == -1 || max - min > BricksLattice.K_I)
			return new Brick(result, 0, -1);
		
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
