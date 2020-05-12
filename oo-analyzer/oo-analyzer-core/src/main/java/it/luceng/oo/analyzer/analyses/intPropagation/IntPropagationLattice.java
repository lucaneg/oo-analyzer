package it.luceng.oo.analyzer.analyses.intPropagation;

import it.lucaneg.oo.api.analyzer.analyses.impl.AbstractLattice;

public class IntPropagationLattice extends AbstractLattice<IntPropagationLattice> {

	private static final IntPropagationLattice TOP = new IntPropagationLattice() {
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
			return "T";
		}
	};
	
	private static final IntPropagationLattice BOTTOM = new IntPropagationLattice() {
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
	
	public static IntPropagationLattice getBottom() {
		return BOTTOM;
	}
	
	public static IntPropagationLattice getTop() {
		return TOP;
	}
	
	private final int value;
	
	private IntPropagationLattice() {
		this(-1);
	}
	
	public IntPropagationLattice(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	protected IntPropagationLattice lubAux(IntPropagationLattice other) {
		return value == other.value ? this : top();
	}

	@Override
	protected IntPropagationLattice wideningAux(IntPropagationLattice other) {
		// finite height
		return lubAux(other);
	}

	@Override
	public IntPropagationLattice bottom() {
		return BOTTOM;
	}

	@Override
	public IntPropagationLattice top() {
		return TOP;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
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
		IntPropagationLattice other = (IntPropagationLattice) obj;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(value); 
	}

	public IntPropagationLattice plus(IntPropagationLattice other) {
		IntPropagationLattice base = handleBaseCases(other, false);
		if (base != null)
			return base;
		
		return new IntPropagationLattice(value + other.value);
	}

	public IntPropagationLattice minus(IntPropagationLattice other) {
		IntPropagationLattice base = handleBaseCases(other, false);
		if (base != null)
			return base;
		
		return new IntPropagationLattice(value - other.value);
	}

	public IntPropagationLattice multiply(IntPropagationLattice other) {
		IntPropagationLattice base = handleBaseCases(other, false);
		if (base != null)
			return base;
		
		return new IntPropagationLattice(value * other.value);
	}

	public IntPropagationLattice divide(IntPropagationLattice other) {
		IntPropagationLattice base = handleBaseCases(other, true);
		if (base != null)
			return base;
		
		return new IntPropagationLattice(value / other.value);
	}
	
	public IntPropagationLattice module(IntPropagationLattice other) {
		IntPropagationLattice base = handleBaseCases(other, true);
		if (base != null)
			return base;
		
		return new IntPropagationLattice(value % other.value);
	}
	
	private IntPropagationLattice handleBaseCases(IntPropagationLattice other, boolean checkForZero) {
		if (this == top() || other == top())
			return top();
		
		if (this == bottom() || other == bottom())
			return bottom();
		
		if (checkForZero && other.value == 0)
			return bottom();
		
		return null;
	}

	public int compare(IntPropagationLattice other) {
		if (this == top() && other == top())
			return 0;
		
		if (this == bottom() && other == bottom())
			return 0;
		
		if (this == top())
			return 1; 
		
		if (this == bottom())
			return -1; 
		
		if (other == top())
			return -1;
		
		if (other == bottom())
			return 1;
		
		return value - other.value;
	}
}
