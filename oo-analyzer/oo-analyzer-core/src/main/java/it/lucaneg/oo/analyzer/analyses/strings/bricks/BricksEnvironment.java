package it.lucaneg.oo.analyzer.analyses.strings.bricks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringEnvironment;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;

/**
 * Bricks environment, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class BricksEnvironment extends BaseStringEnvironment<BricksLattice, BricksEnvironment> {

	public BricksEnvironment() {
		super();
	}
	
	private BricksEnvironment(BricksEnvironment other) {
		super(other.approximations);
	}
	
	@Override
	public BricksEnvironment copy() {
		return new BricksEnvironment(this);
	}

	@Override
	protected BricksLattice provideApproxFor(StringLiteral literal) {
		return new BricksLattice(literal.getValue()); 
	}

	@Override
	protected BricksLattice evalSubstring(BricksLattice receiver, int begin, int end) {
		if (receiver.isTop())
			return latticeTop();
		
		if (receiver.getBricks().isEmpty())
			return latticeBottom();
		
		Brick first = receiver.getBricks().get(0);
		if (first == Brick.TOP || first.getMin() != 1 || first.getMax() != 1)
			return latticeTop();
		
		if (first.getStrings().parallelStream().anyMatch(s -> s.length() < end))
			return latticeTop();

		List<Brick> result = new ArrayList<>(receiver.getBricks());
		Set<String> strings = first.getStrings().parallelStream().map(s -> s.substring(begin, end)).collect(Collectors.toSet());
		result.set(0, new Brick(strings, 1, 1));
		
		return new BricksLattice(result); 
	}

	@Override
	protected BricksLattice evalConcat(BricksLattice receiver, BricksLattice parameter) {
		if (receiver.isTop() || parameter.isTop())
			return latticeTop();
		List<Brick> result = new ArrayList<>(receiver.getBricks());
		result.addAll(parameter.getBricks());
		return new BricksLattice(result);
	}

	@Override
	protected BricksLattice evalReplace(BricksLattice receiver, BricksLattice first, BricksLattice second) {
		if (receiver.isTop() || first.isTop() || second.isTop())
			return latticeTop();

		if (first.containsTop())
			return latticeTop();
		
		return latticeTop();
//		for (int i = 0; i < receiver.getBricks().size() - first.getBricks().size(); i++)
//			if (receiver.getBricks().get(i).equals(first.getBricks().get(0))) {
//				for (int j = 1; j < first.getBricks().size(); j++)
//					if (!receiver.getBricks().get(i + j).equals(first.getBricks().get(j)))
//						// we stop at the first non-matching brick
//						break;
//				
//				// we reached the end of the search bricks, time to replace
//				List<Brick> res = new ArrayList<>();
//				for (int j = 0; j < i; j++)
//					res.add(receiver.getBricks().get(j));
//				res.addAll(second.getBricks());
//				for (int j = i + second.getBricks().size(); j < receiver.getBricks().size(); j++)
//					res.add(receiver.getBricks().get(j));
//				receiver = new BricksLattice(res);
//				i += second.getBricks().size();
//			}
//		
//		return receiver;
	}

	@Override
	protected Satisfiability satisfiesEndsWith(BricksLattice rec, BricksLattice par) {
		if (!par.inRelationWith(rec))
			return Satisfiability.NOT_SATISFIED;
		
		if (rec.equals(par))
			return Satisfiability.SATISFIED;

		int parlen = par.getBricks().size();
		int reclen = rec.getBricks().size();
		for (int i = 0; i < par.getBricks().size(); i++)
			if (!par.getBricks().get(parlen - 1 - i).equals(rec.getBricks().get(reclen - 1 - i)))
				return Satisfiability.NOT_SATISFIED;
		
		return Satisfiability.SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesStartsWith(BricksLattice rec, BricksLattice par) {
		if (!par.inRelationWith(rec))
			return Satisfiability.NOT_SATISFIED;
		
		if (rec.equals(par))
			return Satisfiability.SATISFIED;

		for (int i = 0; i < par.getBricks().size(); i++)
			if (!par.getBricks().get(i).equals(rec.getBricks().get(i)))
				return Satisfiability.NOT_SATISFIED;
		
		return Satisfiability.SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesContains(BricksLattice rec, BricksLattice par) {
		if (par.inRelationWith(rec))
			return Satisfiability.SATISFIED;
		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesEquals(BricksLattice rec, BricksLattice par) {
		if (rec.equals(par))
			return Satisfiability.SATISFIED;
		
		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	protected BricksLattice latticeBottom() {
		return BricksLattice.getBottom();
	}

	@Override
	protected BricksLattice latticeTop() {
		return BricksLattice.getTop();
	}

}
