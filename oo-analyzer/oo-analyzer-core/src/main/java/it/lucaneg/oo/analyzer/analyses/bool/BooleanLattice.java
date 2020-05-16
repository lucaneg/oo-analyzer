package it.lucaneg.oo.analyzer.analyses.bool;

import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractLattice;

public class BooleanLattice extends AbstractLattice<BooleanLattice> {

	private static final BooleanLattice TOP = new BooleanLattice() {
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

	private static final BooleanLattice BOTTOM = new BooleanLattice() {
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

	private static final BooleanLattice TRUE = new BooleanLattice(Boolean.TRUE);

	private static final BooleanLattice FALSE = new BooleanLattice(Boolean.FALSE);

	/**
	 * The boolean value
	 */
	private final Boolean value;

	private BooleanLattice() {
		this.value = null;
	}

	/**
	 * Builds the lattice element.
	 * 
	 * @param value the value
	 */
	private BooleanLattice(Boolean value) {
		this.value = value;
	}

	/**
	 * Returns the abstract boolean value.
	 * 
	 * @return the abstract boolean value
	 */
	public Boolean getValue() {
		return value;
	}

	@Override
	public BooleanLattice bottom() {
		return getBottom();
	}

	/**
	 * Yields the unique bottom element.
	 * 
	 * @return the bottom element
	 */
	public static BooleanLattice getBottom() {
		return BOTTOM;
	}

	@Override
	public BooleanLattice top() {
		return getTop();
	}

	/**
	 * Yields the unique top element.
	 * 
	 * @return the top element
	 */
	public static BooleanLattice getTop() {
		return TOP;
	}

	/**
	 * Yields the unique false value.
	 * 
	 * @return the false value
	 */
	public static BooleanLattice getFalse() {
		return FALSE;
	}

	/**
	 * Yields the unique true value.
	 * 
	 * @return the true value
	 */
	public static BooleanLattice getTrue() {
		return TRUE;
	}

	@Override
	protected BooleanLattice lubAux(BooleanLattice other) {
		if (this == other)
			return this;
		return getTop();
	}

	@Override
	protected BooleanLattice wideningAux(BooleanLattice other) {
		return lubAux(other);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		BooleanLattice other = (BooleanLattice) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (this == FALSE)
			return "false";
		else if (this == TRUE)
			return "true";
		else
			return getTop().toString();
	}
	
	@Override
	public Boolean isEqualTo(AbstractLattice<?> other) {
		if (!(other instanceof BooleanLattice))
			return null;
		
		BooleanLattice o = (BooleanLattice) other;
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return this == o;
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
