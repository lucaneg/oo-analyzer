package it.luceng.oo.analyzer.analyses.strings.bricks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.lucaneg.oo.ast.expression.literal.StringLiteral;
import it.luceng.oo.analyzer.analyses.strings.BaseStringEnvironment;

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
		// TODO
		return latticeTop();
	}

	@Override
	protected Satisfiability satisfiesEndsWith(BricksLattice rec, BricksLattice par) {
		if (par.inRelationWith(rec))
			return Satisfiability.SATISFIED;
		return Satisfiability.NOT_SATISFIED; // TODO
	}

	@Override
	protected Satisfiability satisfiesStartsWith(BricksLattice rec, BricksLattice par) {
		if (par.inRelationWith(rec))
			return Satisfiability.SATISFIED;
		return Satisfiability.NOT_SATISFIED; // TODO
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
