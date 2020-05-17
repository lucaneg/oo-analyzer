package it.lucaneg.oo.analyzer.analyses.value.domains.ints.interval;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;

/**
 * Integer interval lattice.
 * 
 * @author Luca Negrini
 */
public class IntervalLattice extends AbstractIntegerLattice<IntervalLattice> {

	private static final IntervalLattice TOP = new IntervalLattice() {
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

	private static final IntervalLattice BOTTOM = new IntervalLattice() {
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
	 * Low value. Null means -infinity
	 */
	private final Integer low;

	/**
	 * High value. Null means +infinity
	 */
	private final Integer high;

	private IntervalLattice() {
		low = null;
		high = null;
	}

	/**
	 * Builds a new interval. For both parameters, a null value means infinity.
	 * 
	 * @param low  the lower bound
	 * @param high the upper bound
	 */
	public IntervalLattice(Integer low, Integer high) {
		this.low = low;
		this.high = high;
	}

	/**
	 * Returns the lower bound.
	 * 
	 * @return the lower bound
	 */
	public Integer getLow() {
		return low;
	}

	/**
	 * Yields true if and only if the lower bound of this interval is -infinity.
	 * 
	 * @return true only if that condition holds
	 */
	public boolean lowIsMinusInfinity() {
		return low == null;
	}

	/**
	 * Returns the higher bound.
	 * 
	 * @return the higher bound
	 */
	public Integer getHigh() {
		return high;
	}

	/**
	 * Yields true if and only if the upper bound of this interval is +infinity.
	 * 
	 * @return true only if that condition holds
	 */
	public boolean highIsPlusInfinity() {
		return high == null;
	}

	@Override
	public IntervalLattice bottom() {
		return getBottom();
	}

	/**
	 * Yields the unique bottom element.
	 * 
	 * @return the bottom element
	 */
	public static IntervalLattice getBottom() {
		return BOTTOM;
	}

	@Override
	public IntervalLattice top() {
		return getTop();
	}

	/**
	 * Yields the unique top element.
	 * 
	 * @return the top element
	 */
	public static IntervalLattice getTop() {
		return TOP;
	}

	@Override
	protected IntervalLattice lubAux(IntervalLattice other) {
		Integer newLow = lowIsMinusInfinity() || other.lowIsMinusInfinity() ? null : Math.min(low, other.low);
		Integer newHigh = highIsPlusInfinity() || other.highIsPlusInfinity() ? null : Math.max(high, other.high);
		return new IntervalLattice(newLow, newHigh);
	}

	@Override
	protected IntervalLattice wideningAux(IntervalLattice other) {
		Integer maxI1 = highIsPlusInfinity() ? Integer.MAX_VALUE : high;
		Integer maxI2 = other.highIsPlusInfinity() ? Integer.MAX_VALUE : other.high;
		Integer minI1 = lowIsMinusInfinity() ? Integer.MIN_VALUE : low;
		Integer minI2 = other.lowIsMinusInfinity() ? Integer.MIN_VALUE : other.low;

		Integer newLow, newHigh;
		if (minI2 < minI1)
			newLow = null;
		else
			newLow = low;

		if (maxI2 > maxI1)
			newHigh = null;
		else
			newHigh = high;

		return new IntervalLattice(newLow, newHigh);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((high == null) ? 0 : high.hashCode());
		result = prime * result + ((low == null) ? 0 : low.hashCode());
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
		IntervalLattice other = (IntervalLattice) obj;
		if (high == null) {
			if (other.high != null)
				return false;
		} else if (!high.equals(other.high))
			return false;
		if (low == null) {
			if (other.low != null)
				return false;
		} else if (!low.equals(other.low))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + (lowIsMinusInfinity() ? "-∞" : low) + ", " + (highIsPlusInfinity() ? "+∞" : high) + "]";
	}

	@Override
	public IntervalLattice plus(IntervalLattice other) {
		Integer newLow, newHigh;

		if (lowIsMinusInfinity() || other.lowIsMinusInfinity())
			newLow = null;
		else
			newLow = low + other.low;

		if (highIsPlusInfinity() || other.highIsPlusInfinity())
			newHigh = null;
		else
			newHigh = high + other.high;

		return new IntervalLattice(newLow, newHigh);
	}

	@Override
	public IntervalLattice diff(IntervalLattice other) {
		return plus(other.mul(new IntervalLattice(-1, -1)));
	}

	@Override
	public IntervalLattice mul(IntervalLattice other) {
		SortedSet<Integer> boundSet = new TreeSet<>();
		Integer low1 = low;
		Integer high1 = high;
		Integer low2 = other.low;
		Integer high2 = other.high;

		AtomicBoolean lowInf = new AtomicBoolean(false), highInf = new AtomicBoolean(false);

		// low1low2
		multiplyBounds(boundSet, low1, low2, lowInf, highInf);

		// x1y2
		multiplyBounds(boundSet, low1, high2, lowInf, highInf);

		// x2y1
		multiplyBounds(boundSet, high1, low2, lowInf, highInf);

		// x2y2
		multiplyBounds(boundSet, high1, high2, lowInf, highInf);

		return new IntervalLattice(lowInf.get() ? null : boundSet.first(), highInf.get() ? null : boundSet.last());
	}

	private void multiplyBounds(SortedSet<Integer> boundSet, Integer low1, Integer low2, AtomicBoolean lowInf,
			AtomicBoolean highInf) {
		if (low1 == null) {
			if (low2 == null)
				// -inf * -inf = +inf
				highInf.set(true);
			else {
				if (low2 > 0)
					// -inf * positive
					lowInf.set(true);
				else if (low2 < 0)
					// -inf * negative
					highInf.set(true);
				else
					boundSet.add(0);
			}
		} else if (low2 == null) {
			if (low1 > 0)
				// -inf * positive
				lowInf.set(true);
			else if (low1 < 0)
				// -inf * negative
				highInf.set(true);
			else
				boundSet.add(0);
		} else
			boundSet.add(low1 * low2);
	}

	@Override
	public List<Integer> getIntergers() {
		if (isFinite())
			return IntStream.range(low, high + 1).boxed().collect(Collectors.toList());

		return new ArrayList<>();
	}
	
	@Override
	public boolean isFinite() {
		return !lowIsMinusInfinity() && !highIsPlusInfinity();
	}

	@Override
	public boolean isZero() {
		return low == 0 && high == 0;
	}

	@Override
	public boolean contains(int n) {
		if (isFinite())
			return n >= low && n <= high;
		else if (lowIsMinusInfinity() && !highIsPlusInfinity())
			return n <= high;
		else if (!lowIsMinusInfinity() && highIsPlusInfinity())
			return n >= low;
		else
			return true;
	}
	
	@Override
	public Boolean isEqualTo(IntervalLattice other) {
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return equals(other);
	}
	
	@Override
	public Boolean isGreaterThan(IntervalLattice other) {
		return null; // TODO
	}
	
	@Override
	public Boolean isLessThen(IntervalLattice other) {
		return null; // TODO
	}
	
	@Override
	public IntervalLattice minusOne() {
		return new IntervalLattice(-1, -1);
	}
	
	@Override
	public IntervalLattice mk(int value) {
		return new IntervalLattice(value, value);
	}
}
