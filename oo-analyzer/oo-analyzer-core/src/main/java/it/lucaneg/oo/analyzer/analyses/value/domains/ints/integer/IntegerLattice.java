package it.lucaneg.oo.analyzer.analyses.value.domains.ints.integer;

import java.util.ArrayList;
import java.util.List;

import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;

public class IntegerLattice extends AbstractIntegerLattice<IntegerLattice> {

	private static final IntegerLattice TOP = new IntegerLattice() {
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
	
	private static final IntegerLattice BOTTOM = new IntegerLattice() {
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
	
	public static IntegerLattice getBottom() {
		return BOTTOM;
	}
	
	public static IntegerLattice getTop() {
		return TOP;
	}
	
	private final Integer value;
	
	private IntegerLattice() {
		this.value = null;
	}
	
	public IntegerLattice(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	protected IntegerLattice lubAux(IntegerLattice other) {
		return value == other.value ? this : top();
	}

	@Override
	protected IntegerLattice wideningAux(IntegerLattice other) {
		// finite height
		return lubAux(other);
	}

	@Override
	public IntegerLattice bottom() {
		return BOTTOM;
	}

	@Override
	public IntegerLattice top() {
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
		IntegerLattice other = (IntegerLattice) obj;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(value); 
	}

	@Override
	public IntegerLattice plus(IntegerLattice other) {
		IntegerLattice base = handleBaseCases(other, false);
		if (base != null)
			return base;
		
		return new IntegerLattice(value + other.value);
	}

	@Override
	public IntegerLattice diff(IntegerLattice other) {
		IntegerLattice base = handleBaseCases(other, false);
		if (base != null)
			return base;
		
		return new IntegerLattice(value - other.value);
	}

	@Override
	public IntegerLattice mul(IntegerLattice other) {
		IntegerLattice base = handleBaseCases(other, false);
		if (base != null)
			return base;
		
		return new IntegerLattice(value * other.value);
	}

	// TODO @Override
	public IntegerLattice divide(IntegerLattice other) {
		IntegerLattice base = handleBaseCases(other, true);
		if (base != null)
			return base;
		
		return new IntegerLattice(value / other.value);
	}
	
	// TODO @Override
	public IntegerLattice module(IntegerLattice other) {
		IntegerLattice base = handleBaseCases(other, true);
		if (base != null)
			return base;
		
		return new IntegerLattice(value % other.value);
	}
	
	private IntegerLattice handleBaseCases(IntegerLattice other, boolean checkForZero) {
		if (this == top() || other == top())
			return top();
		
		if (this == bottom() || other == bottom())
			return bottom();
		
		if (checkForZero && other.value == 0)
			return bottom();
		
		return null;
	}

	@Override
	public Boolean isEqualTo(IntegerLattice other) {
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return value == other.value;
	}
	
	@Override
	public Boolean isGreaterThan(IntegerLattice other) {
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return value > other.value;
	}
	
	@Override
	public Boolean isLessThen(IntegerLattice other) {
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return value < other.value;
	}

	@Override
	public List<Integer> getIntergers() {
		List<Integer> result = new ArrayList<>();
		if (isBottom() || isTop())
			return result;
		result.add(value);
		return result;
	}

	@Override
	public boolean isZero() {
		return value == 0;
	}

	@Override
	public boolean contains(int n) {
		return n == value;
	}

	@Override
	public IntegerLattice minusOne() {
		return new IntegerLattice(-1);
	}

	@Override
	public IntegerLattice mk(int value) {
		return new IntegerLattice(value);
	}
	
	@Override
	public boolean isFinite() {
		return true;
	}

	@Override
	public IntegerLattice makeGreaterThan(IntegerLattice other) {
		if (!other.isFinite())
			return this;
		if (value > other.value)
			return this;
		return getTop();
	}

	@Override
	public IntegerLattice makeGreaterOrEqualThan(IntegerLattice other) {
		if (!other.isFinite())
			return this;
		if (value >= other.value)
			return this;
		return getTop();
	}

	@Override
	public IntegerLattice makeLessThan(IntegerLattice other) {
		if (!other.isFinite())
			return this;
		if (value < other.value)
			return this;
		return getTop();
	}

	@Override
	public IntegerLattice makeLessOrEqualThan(IntegerLattice other) {
		if (!other.isFinite())
			return this;
		if (value <= other.value)
			return this;
		return getTop();
	}
}
