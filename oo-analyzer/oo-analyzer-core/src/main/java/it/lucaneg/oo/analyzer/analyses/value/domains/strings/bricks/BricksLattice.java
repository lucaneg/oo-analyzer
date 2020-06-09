package it.lucaneg.oo.analyzer.analyses.value.domains.strings.bricks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.strings.AbstractStringLattice;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability;

/**
 * Bricks domain, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class BricksLattice extends AbstractStringLattice<BricksLattice> {

	/**
	 * The K_L index, that is, the limit on the length of the list of bricks after
	 * which the widening returns top
	 */
	static final int K_L = 20;
	
	/**
	 * The K_I index, that is, the limit on the number of repetitions (max - min)
	 * after which the widening turns max into infinity
	 */
	static final int K_I = 20;

	/**
	 * The K_S index, that is, the limit on the number of strings in the brick 
	 * after which the widening returns top
	 */
	static final int K_S = 50;
	
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
		
//		@Override
//		public String toString() {
//			return String.valueOf(Analysis.TOP_CHAR);
//		}
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
	}
	
	public BricksLattice(Brick... bricks) {
		this();
		for (Brick b : bricks)
			this.bricks.add(b);
	}
	
	public List<Brick> getBricks() {
		return bricks;
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
			second = BricksPadder.padList(second, first);
		else if (second.size() > first.size())
			first = BricksPadder.padList(first, second);
		
		List<Brick> lub = new ArrayList<>();
		for (int i = 0; i < first.size(); i++)
			lub.add(first.get(i).lub(second.get(i)));
		
 		return BricksNormalizer.normalize(new BricksLattice(lub));
	}
	
	private boolean lessOrEqual(BricksLattice other) {
		if (other == TOP || this == BOTTOM)
			return true;
		
		List<Brick> first = bricks;
		List<Brick> second = other.bricks;
		
		if (first.size() > second.size())
			second = BricksPadder.padList(second, first);
		else if (second.size() > first.size())
			first = BricksPadder.padList(first, second);
		
		for (int i = 0; i < first.size(); i++)
			if (!first.get(i).lessOrEqual(second.get(i)))
				return false;
		
		return true;
	}
	
	@Override
	protected BricksLattice wideningAux(BricksLattice other) {
		boolean rel = lessOrEqual(other);
		if (!rel && !other.lessOrEqual(this))
			return TOP;
		
		if (bricks.size() > K_L || other.bricks.size() > K_L)
			return TOP;
		
		List<Brick> l1 = bricks;
		List<Brick> l2 = other.bricks;
		
		if (l1.size() > l2.size())
			l2 = BricksPadder.padList(l2, l1);
		else if (l2.size() > l1.size())
			l1 = BricksPadder.padList(l1, l2);
		
		if (!rel) {
			// we need l1 <= l2 thus we exchange those
			List<Brick> tmp = l1;
			l1 = l2;
			l2 = tmp;
		}
		
		List<Brick> widen = new ArrayList<>();
		for (int i = 0; i < l1.size(); i++)
			widen.add(l1.get(i).widening(l2.get(i)));
		
		return BricksNormalizer.normalize(new BricksLattice(widen));
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
	public BricksLattice mk(String string) {
		return new BricksLattice(string);
	}

	@Override
	public Satisfiability contains(BricksLattice other) {
		// paper implementation:
		if (other.getBricks().size() != 1)
			return Satisfiability.UNKNOWN;
		
		if (other.getBricks().get(0).getStrings().size() != 1)
			return Satisfiability.UNKNOWN;
		
		if (other.getBricks().get(0).getStrings().iterator().next().length() != 1)
			return Satisfiability.UNKNOWN;
		
		String c = other.getBricks().get(0).getStrings().iterator().next();
		
		boolean res = bricks.stream()
				.filter(b -> b.getMin() > 0)
				.map(b -> b.getStrings())
				.anyMatch(set -> set.stream().allMatch(s -> s.contains(c)));
		if (res)
			return Satisfiability.SATISFIED;
		
		res = bricks.stream()
				.map(b -> b.getStrings())
				.allMatch(set -> set.stream().allMatch(s -> !s.contains(c)));
		if (res)
			return Satisfiability.NOT_SATISFIED;
		
		return Satisfiability.UNKNOWN;
		//return process(other, String::contains);
	}

	@Override
	public Satisfiability startsWith(BricksLattice other) {
		return process(other, String::startsWith);
	}

	@Override
	public Satisfiability endsWith(BricksLattice other) {
		return process(other, String::endsWith);
	}

	private Satisfiability process(BricksLattice other, BiFunction<String, String, Boolean> comparer) {
		if (isTop() || other.isTop())
			return Satisfiability.UNKNOWN;
		
		if (contains(other) == Satisfiability.NOT_SATISFIED)
			return Satisfiability.NOT_SATISFIED;

		if (equals(other))
			return Satisfiability.SATISFIED;
		
		if (other.getBricks().size() == 1 
				&& other.getBricks().get(0).getMin() == 1 
				&& other.getBricks().get(0).getMax() == 1 
				&& other.getBricks().get(0).getStrings().size() == 1) {
			boolean may = false; 
			boolean must = false;
			String search = other.getBricks().get(0).getStrings().iterator().next();
			for (Brick b : getBricks()) {
				int count = 0;
				for (String a : b.getStrings())
					if (comparer.apply(a, search))
						count++;
				if (count > 0)
					may = true;
				if (count == b.getStrings().size() && b.getMin() > 0)
					must = true;
			}
			
			if (must)
				return Satisfiability.SATISFIED;
			
			if (may)
				return Satisfiability.UNKNOWN;
		
			return Satisfiability.NOT_SATISFIED;
		}

		return Satisfiability.UNKNOWN;
	}

	@Override
	public Satisfiability isEquals(BricksLattice other) {
		if (isTop() || other.isTop())
			return Satisfiability.UNKNOWN;
		
		if (equals(other))
			return Satisfiability.SATISFIED;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public BricksLattice concat(BricksLattice other) {
//		if (isTop() || other.isTop())
//			return getTop();
		List<Brick> result = new ArrayList<>(getBricks());
		result.addAll(other.getBricks());
		return new BricksLattice(result);
	}

	@Override
	public BricksLattice substring(int begin, int end) {
		if (isTop())
			return getTop();

		Brick first = BricksNormalizer.normalize(this).getBricks().get(0);
		if (first == Brick.TOP || first.getMin() != 1 || first.getMax() != 1)
			return getTop();
		
		if (first.getStrings().parallelStream().anyMatch(s -> s.length() < end))
			return getTop();

		List<Brick> result = new ArrayList<>();
		Set<String> strings = first.getStrings().parallelStream().map(s -> s.substring(begin, end))
				.collect(Collectors.toSet());
		result.add(0, new Brick(strings, 1, 1));

		return new BricksLattice(result);
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractIntegerLattice indexOf(BricksLattice str, AbstractIntegerLattice singleton) {
		return (AbstractIntegerLattice) singleton.mk(-1).widening(singleton.mk(Integer.MAX_VALUE));
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractIntegerLattice length(AbstractIntegerLattice singleton) {
		return (AbstractIntegerLattice) singleton.mk(0).widening(singleton.mk(Integer.MAX_VALUE));
	}

	@Override
	public BricksLattice replace(BricksLattice toReplace, BricksLattice str) {
		if (isTop() || toReplace.isTop() || str.isTop())
			return getTop();

		if (toReplace.containsTop())
			return getTop();

		// too many corner cases
		return getTop();
//		BricksLattice receiver = this;
//		for (int i = 0; i < receiver.getBricks().size() - toReplace.getBricks().size(); i++)
//			if (receiver.getBricks().get(i).equals(toReplace.getBricks().get(0))) {
//				for (int j = 1; j < toReplace.getBricks().size(); j++)
//					if (!receiver.getBricks().get(i + j).equals(toReplace.getBricks().get(j)))
//						// we stop at the first non-matching brick
//						break;
//				
//				// we reached the end of the search bricks, time to replace
//				List<Brick> res = new ArrayList<>();
//				for (int j = 0; j < i; j++)
//					res.add(receiver.getBricks().get(j));
//				res.addAll(str.getBricks());
//				for (int j = i + str.getBricks().size(); j < receiver.getBricks().size(); j++)
//					res.add(receiver.getBricks().get(j));
//				receiver = new BricksLattice(res);
//				i += str.getBricks().size();
//			}
//		
//		return receiver;
	}

	@Override
	public Boolean isEqualTo(BricksLattice other) {
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return equals(other);
	}

	@Override
	public Boolean isLessThen(BricksLattice other) {
		return null;
	}

	@Override
	public Boolean isGreaterThan(BricksLattice other) {
		return null;
	}
}
