package it.lucaneg.oo.analyzer.analyses.value.domains.strings.bricks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class BricksNormalizer {
	static BricksLattice normalize(BricksLattice bricks) {
		List<Brick> normal;
		List<Brick> work = new ArrayList<>(bricks.getBricks());
		
		do {
			normal = new ArrayList<>(work);
			
			rule1(work);
			rule2(work);
			rule3(work);
			rule4(work);
			rule5(work);
		} while (!work.equals(normal));
		
		return new BricksLattice(normal);
	}
	
	private static void rule1(List<Brick> work) { 
		Iterator<Brick> it = work.iterator();
		while (it.hasNext()) {
			Brick current = it.next();
			if (current.getStrings().isEmpty() && current.getMin() == 0 && current.getMax() == 0)
				it.remove();
		}
	}
	
	private static void rule2(List<Brick> work) { 
		for (int i = 0; i < work.size() - 1; i++) {
			Brick first = work.get(i);
			Brick second = work.get(i + 1);
			
			if (first.getMin() == 1 && first.getMax() == 1 && second.getMin() == 1 && second.getMax() == 1) {
				work.set(i, new Brick(cartesianConcat(first.getStrings(), second.getStrings()), 1, 1));
				work.remove(i + 1);
			}
		}
	}
	
	private static void rule3(List<Brick> work) { 
		for (int i = 0; i < work.size(); i++) {
			Brick current = work.get(i);
			
			if (current.getMin() == current.getMax() && current != Brick.BOTTOM) {
				Set<String> result = current.getStrings();
				for (int k = 1; k < current.getMax(); k++)
					result = cartesianConcat(result, current.getStrings());
				
				work.set(i, new Brick(result, 1, 1));
			}
		}
	}
	
	private static void rule4(List<Brick> work) { 
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
	
	private static void rule5(List<Brick> work) { 
		for (int i = 0; i < work.size(); i++) {
			Brick current = work.get(i);
			
			if (current.getMin() >= 1 && current.getMin() != current.getMax()) {
				Set<String> result = current.getStrings();
				for (int k = 1; k < current.getMin(); k++)
					result = cartesianConcat(result, current.getStrings());
				
				work.set(i, new Brick(result, 1, 1));
				work.add(i + 1, new Brick(current.getStrings(), 0, current.getMax() - current.getMin()));
				i++;
			}
		}
	}

	private static Set<String> cartesianConcat(Set<String> first, Set<String> second) { 
		Set<String> result = new TreeSet<>();
		for (String s1 : first)
			for (String s2 : second)
				result.add(s1 + s2);
		return result;
	}
}
