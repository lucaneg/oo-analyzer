package it.lucaneg.oo.analyzer.analyses.strings.charinclusion;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringEnvironment;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;

/**
 * Char Inclusion environment, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class CharInclusionEnvironment extends BaseStringEnvironment<CharInclusionLattice, CharInclusionEnvironment> {

	private CharInclusionEnvironment(CharInclusionEnvironment other) {
		super(other.approximations);
	}
	
	public CharInclusionEnvironment() {
		super();
	}
	
	@Override
	public CharInclusionEnvironment copy() {
		return new CharInclusionEnvironment(this);
	}

	@Override
	protected CharInclusionLattice provideApproxFor(StringLiteral literal) {
		return new CharInclusionLattice(literal.getValue());
	}

	@Override
	protected CharInclusionLattice evalSubstring(CharInclusionLattice receiver, int begin, int end) {
		if (receiver.isTop())
			return latticeTop();
		
		return new CharInclusionLattice(new TreeSet<>(), receiver.getPossiblyIncludedChars());
	}
	
	@Override
	protected CharInclusionLattice evalReplace(CharInclusionLattice receiver, CharInclusionLattice first, CharInclusionLattice second) {
		if (receiver.isTop() || first.isTop() || second.isTop())
			return latticeTop();
		
		if (!receiver.getIncludedChars().containsAll(first.getIncludedChars()))
			// no replace for sure
			return receiver;
		Set<Character> included = new TreeSet<>(receiver.getIncludedChars());
		Set<Character> possibly = new TreeSet<>(receiver.getPossiblyIncludedChars());
		// since we do not know if the replace will happen, we move everything to the possibly included characters
		included.removeAll(first.getIncludedChars());
		possibly.addAll(first.getIncludedChars());

		included.removeAll(first.getPossiblyIncludedChars());
		Set<Character> tmp = new TreeSet<>(first.getPossiblyIncludedChars());
		tmp.retainAll(receiver.getIncludedChars()); // just the ones that we removed before
		possibly.addAll(tmp);
		
		// add the second string
		possibly.addAll(second.getIncludedChars());
		possibly.addAll(second.getPossiblyIncludedChars());
		
		return new CharInclusionLattice(included, possibly);
	}

	@Override
	protected CharInclusionLattice evalConcat(CharInclusionLattice receiver, CharInclusionLattice parameter) {
		if (receiver.isTop() || parameter.isTop())
			return latticeTop();
		
		Set<Character> included = new TreeSet<>(receiver.getIncludedChars());
		Set<Character> possibly = new TreeSet<>(receiver.getPossiblyIncludedChars());
		included.addAll(parameter.getIncludedChars());
		possibly.addAll(parameter.getPossiblyIncludedChars());
		return new CharInclusionLattice(included, possibly);
	}

	@Override
	protected Satisfiability satisfiesEndsWith(CharInclusionLattice rec, CharInclusionLattice par) {
		if (rec.isTop() || par.isTop())
			return Satisfiability.UNKNOWN;
		
		Set<Character> includedChars = new HashSet<>(rec.getIncludedChars());
		includedChars.retainAll(par.getIncludedChars());
		if (!includedChars.isEmpty())
			return Satisfiability.NOT_SATISFIED;
		
		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesStartsWith(CharInclusionLattice rec, CharInclusionLattice par) {
		if (rec.isTop() || par.isTop())
			return Satisfiability.UNKNOWN;
		
		Set<Character> includedChars = new HashSet<>(rec.getIncludedChars());
		includedChars.retainAll(par.getIncludedChars());
		if (!includedChars.isEmpty())
			return Satisfiability.NOT_SATISFIED;
		
		
		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesContains(CharInclusionLattice rec, CharInclusionLattice par) {
		if (rec.isTop() || par.isTop())
			return Satisfiability.UNKNOWN;
		
		if (par.getIncludedChars().size() == 1 
				&& par.getPossiblyIncludedChars().equals(par.getIncludedChars())
				&& rec.getIncludedChars().containsAll(par.getIncludedChars()))
				// only case when we can return true
			return Satisfiability.SATISFIED;
		
		Set<Character> includedChars = new HashSet<>(rec.getIncludedChars());
		includedChars.retainAll(par.getIncludedChars());
		if (!includedChars.isEmpty())
			return Satisfiability.NOT_SATISFIED;
		
		
		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesEquals(CharInclusionLattice rec, CharInclusionLattice par) {
		if (rec.isTop() || par.isTop())
			return Satisfiability.UNKNOWN;
		
		if (!rec.getIncludedChars().containsAll(par.getIncludedChars()))
			return Satisfiability.NOT_SATISFIED;
		
		return Satisfiability.UNKNOWN;
	}

	@Override
	protected CharInclusionLattice latticeBottom() {
		return CharInclusionLattice.getBottom();
	}

	@Override
	protected CharInclusionLattice latticeTop() {
		return CharInclusionLattice.getTop();
	}
}
