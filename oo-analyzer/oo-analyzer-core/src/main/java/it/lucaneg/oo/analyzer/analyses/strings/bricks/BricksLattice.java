package it.lucaneg.oo.analyzer.analyses.strings.bricks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringLattice;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractLattice;

/**
 * Bricks domain, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class BricksLattice extends BaseStringLattice<BricksLattice> {

	/**
	 * The unique top element
	 */
	private static final BricksLattice TOP = new BricksLattice(Brick.TOP) {
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
			return "\u0372";
		}
	};

	/**
	 * The unique bottom element
	 */
	private static final BricksLattice BOTTOM = new BricksLattice() {
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
	
	private final List<Brick> bricks;

	private BricksLattice() {
		this.bricks = new ArrayList<>();
	}
	
	public BricksLattice(String string) {
		this();
		bricks.add(new Brick(string, 1, 1));
	}
	
	public BricksLattice(List<Brick> bricks) {
		this();
		this.bricks.addAll(bricks);
		normalize();
	}
	
	public BricksLattice(Brick... bricks) {
		this();
		for (Brick b : bricks)
			this.bricks.add(b);
		normalize();
	}
	
	public List<Brick> getBricks() {
		return bricks;
	}
	
	private void normalize() {
		List<Brick> normal;
		List<Brick> work = new ArrayList<>(bricks);
		
		do {
			normal = new ArrayList<>(work);
			
			rule1(work);
			rule2(work);
			rule3(work);
			rule4(work);
			rule5(work);
		} while (!work.equals(normal));
		
		bricks.clear();
		bricks.addAll(normal);
	}

	private void rule1(List<Brick> work) { 
		Iterator<Brick> it = work.iterator();
		while (it.hasNext()) {
			Brick current = it.next();
			if (current.getStrings().isEmpty() && current.getMin() == 0 && current.getMax() == 0)
				it.remove();
		}
	}
	
	private void rule2(List<Brick> work) { 
		for (int i = 0; i < work.size() - 1; i++) {
			Brick first = work.get(i);
			Brick second = work.get(i + 1);
			
			if (first.getMin() == 1 && first.getMax() == 1
					&& second.getMin() == 1 && second.getMax() == 1) {
				if (first == Brick.TOP && second == Brick.TOP) 
					work.set(i, Brick.TOP);
				else if (first == Brick.BOTTOM && second == Brick.BOTTOM) 
					work.set(i, Brick.BOTTOM);
				else if (first != Brick.TOP && first != Brick.BOTTOM
					&& second != Brick.TOP && second != Brick.BOTTOM) {
					work.set(i, new Brick(cartesian(first.getStrings(), second.getStrings()), 1, 1));
					work.remove(i + 1);
				}
			}
		}
	}
	
	private void rule3(List<Brick> work) { 
		for (int i = 0; i < work.size(); i++) {
			Brick current = work.get(i);
			
			if (current.getMin() == current.getMax()
					&& current != Brick.TOP && current != Brick.BOTTOM) {
				Set<String> result = current.getStrings();
				for (int k = 1; k < current.getMax(); k++)
					result = cartesian(result, current.getStrings());
				
				work.set(i, new Brick(result, 1, 1));
			}
		}
	}
	
	private void rule4(List<Brick> work) { 
		for (int i = 0; i < work.size() - 1; i++) {
			Brick first = work.get(i);
			Brick second = work.get(i + 1);
			
			if (first.getStrings().equals(second.getStrings())) {
				if (first == Brick.TOP) // checking first is enough since they have the same strings
					work.set(i, Brick.TOP);
				else if (first == Brick.BOTTOM) // checking first is enough since they have the same strings
					work.set(i, Brick.BOTTOM);
				else 
					work.set(i, new Brick(first.getStrings(), first.getMin() + second.getMin(), first.getMax() + second.getMax()));
				work.remove(i + 1);
			}
		}
	}
	
	private void rule5(List<Brick> work) { 
		for (int i = 0; i < work.size(); i++) {
			Brick current = work.get(i);
			
			if (current.getMin() >= 1 && current.getMin() != current.getMax()) {
				Set<String> result = current.getStrings();
				for (int k = 1; k < current.getMin(); k++)
					result = cartesian(result, current.getStrings());
				
				work.set(i, new Brick(result, 1, 1));
				work.add(i + 1, new Brick(current.getStrings(), 0, current.getMax() - current.getMin()));
			}
		}
	}

	private Set<String> cartesian(Set<String> first, Set<String> second) { 
		Set<String> result = new TreeSet<>();
		for (String s1 : first)
			for (String s2 : second)
				result.add(s1 + s2);
		return result;
	}
	
	private static List<Brick> padList(List<Brick> l1, List<Brick> l2) {
		Brick e = new Brick("", 0, 0);
		int n1 = l1.size();
		int n2 = l2.size();
		int n = n2 - n1;
		List<Brick> lnew = new ArrayList<>();
		int emptyBricksAdded = 0;
		
		for (int i = 0; i < n2; i++) 
			if (emptyBricksAdded >= n) 
				lnew.add(l1.get(i - emptyBricksAdded));
			else if (i >= l1.size() || !l1.get(i - emptyBricksAdded).equals(l2.get(i))) {
				lnew.add(e);
				emptyBricksAdded++;
			} else 
				lnew.add(l1.get(i - emptyBricksAdded));

		return lnew;		
	}
	
	@Override
	public String toString() {
		return StringUtils.join(bricks, " ");
	}

	@Override
	public BricksLattice bottom() {
		return getBottom();
	}
	
	public static BricksLattice getBottom() {
		return BOTTOM;
	}

	@Override
	public BricksLattice top() {
		return getTop();
	}
	
	public static BricksLattice getTop() {
		return TOP;
	}
	
	public boolean containsTop() {
		return bricks.parallelStream().anyMatch(b -> b == Brick.TOP);
	}

	@Override
	protected BricksLattice lubAux(BricksLattice other) {
		List<Brick> first = bricks;
		List<Brick> second = other.bricks;
		
		if (first.size() > second.size())
			second = padList(second, first);
		else if (second.size() > first.size())
			first = padList(first, second);
		
		List<Brick> lub = new ArrayList<>();
		for (int i = 0; i < first.size(); i++)
			lub.add(first.get(i).lub(second.get(i)));
		
		return new BricksLattice(lub);
	}
	
	boolean inRelationWith(BricksLattice other) {
		if (other == TOP || this == BOTTOM)
			return true;
		
		List<Brick> first = bricks;
		List<Brick> second = other.bricks;
		
		if (first.size() > second.size())
			second = padList(second, first);
		else if (second.size() > first.size())
			first = padList(first, second);
		
		for (int i = 0; i < first.size(); i++)
			if (!first.get(i).inRelationWith(second.get(i)))
				return false;
		
		return true;
	}
	
	@Override
	protected BricksLattice wideningAux(BricksLattice other) {
		boolean rel = inRelationWith(other);
		if (!rel && !other.inRelationWith(this))
			return TOP;
		
		if (bricks.size() > BricksAnalysis.K_L || other.bricks.size() > BricksAnalysis.K_L)
			return TOP;
		
		List<Brick> first = bricks;
		List<Brick> second = other.bricks;
		
		if (first.size() > second.size())
			second = padList(second, first);
		else if (second.size() > first.size())
			first = padList(first, second);
		
		if (!rel) {
			List<Brick> tmp = first;
			first = second;
			second = tmp;
		}
		
		List<Brick> widen = new ArrayList<>();
		for (int i = 0; i < first.size(); i++)
			widen.add(first.get(i).widening(second.get(i)));
		
		return new BricksLattice(widen);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bricks == null) ? 0 : bricks.hashCode());
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
		BricksLattice other = (BricksLattice) obj;
		if (bricks == null) {
			if (other.bricks != null)
				return false;
		} else if (!bricks.equals(other.bricks))
			return false;
		return true;
	}

	@Override
	public Boolean isEqualTo(AbstractLattice<?> other) {
		if (!(other instanceof BricksLattice))
			return null;
		
		BricksLattice o = (BricksLattice) other;
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return equals(o);
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
