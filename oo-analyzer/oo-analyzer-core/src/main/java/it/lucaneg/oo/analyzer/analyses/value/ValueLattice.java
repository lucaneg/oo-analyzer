package it.lucaneg.oo.analyzer.analyses.value;

import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractLattice;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ValueLattice extends AbstractLattice<ValueLattice> {

	private static final ValueLattice TOP = new ValueLattice() {
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

	private static final ValueLattice BOTTOM = new ValueLattice() {
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

	private final SingleValueLattice innerElement;

	private ValueLattice() {
		innerElement = null;
	}

	public ValueLattice(SingleValueLattice innerElement) {
		this.innerElement = innerElement;
	}

	public SingleValueLattice getInnerElement() {
		return innerElement;
	}

	@Override
	public ValueLattice bottom() {
		return getBottom();
	}

	/**
	 * Yields the unique bottom element.
	 * 
	 * @return the bottom element
	 */
	public static ValueLattice getBottom() {
		return BOTTOM;
	}

	@Override
	public ValueLattice top() {
		return getTop();
	}

	/**
	 * Yields the unique top element.
	 * 
	 * @return the top element
	 */
	public static ValueLattice getTop() {
		return TOP;
	}

	@Override
	protected ValueLattice lubAux(ValueLattice other) {
		if (innerElement.getClass() == other.innerElement.getClass())
			innerElement.lub((SingleValueLattice) other.innerElement);
		return getTop();
	}

	@Override
	protected ValueLattice wideningAux(ValueLattice other) {
		if (innerElement.getClass() == other.innerElement.getClass())
			innerElement.widening((SingleValueLattice) other.innerElement);
		return getTop();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((innerElement == null) ? 0 : innerElement.hashCode());
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
		ValueLattice other = (ValueLattice) obj;
		if (innerElement == null) {
			if (other.innerElement != null)
				return false;
		} else if (!innerElement.equals(other.innerElement))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return innerElement.toString();
	}
}
