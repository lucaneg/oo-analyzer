package it.lucaneg.oo.analyzer.analyses.value.domains.strings.bricks;

import java.util.ArrayList;
import java.util.List;

public class BricksPadder {
	static List<Brick> padList(List<Brick> first, List<Brick> second) {
		List<Brick> l1 = new ArrayList<>(first), l2 = new ArrayList<>(second);
		Brick e = new Brick(0, 0);
		int n1 = l1.size();
		int n2 = l2.size();
		int n = n2 - n1;
		List<Brick> lnew = new ArrayList<>();
		int emptyBricksAdded = 0;
		
		for (int i = 0; i < n2; i++) 
			if (emptyBricksAdded >= n) {
				lnew.add(l1.get(0));
				l1.remove(0);
			} else if (l1.isEmpty() || !l1.get(0).equals(l2.get(i))) {
				lnew.add(e);
				emptyBricksAdded++;
			} else {
				lnew.add(l1.get(0));
				l1.remove(0);
			}

		return lnew;		
	}
}
