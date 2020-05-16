package it.lucaneg.oo.analyzer.analyses.strings.charinclusion;

import java.util.Set;
import java.util.TreeSet;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringExpressionEvaluator;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;

public class CharInclusionExpressionEvaluator extends BaseStringExpressionEvaluator<CharInclusionLattice> {

	@Override
	protected CharInclusionLattice latticeBottom() {
		return CharInclusionLattice.getBottom();
	}

	@Override
	protected CharInclusionLattice latticeTop() {
		return CharInclusionLattice.getTop();
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
	protected CharInclusionLattice evalReplace(CharInclusionLattice receiver, CharInclusionLattice first,
			CharInclusionLattice second) {
		if (receiver.isTop() || first.isTop() || second.isTop())
			return latticeTop();

		if (!receiver.getIncludedChars().containsAll(first.getIncludedChars()))
			// no replace for sure
			return receiver;
		Set<Character> included = new TreeSet<>(receiver.getIncludedChars());
		Set<Character> possibly = new TreeSet<>(receiver.getPossiblyIncludedChars());
		// since we do not know if the replace will happen, we move everything to the
		// possibly included characters
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
		if (receiver.isTop())
			return latticeTop();

		Set<Character> included = new TreeSet<>(receiver.getIncludedChars());
		Set<Character> possibly = new TreeSet<>(receiver.getPossiblyIncludedChars());

		if (parameter.isTop()) 
			possibly.add(CharInclusionLattice.TOP_CHAR);
		else {
			included.addAll(parameter.getIncludedChars());
			possibly.addAll(parameter.getPossiblyIncludedChars());
		}
		
		return new CharInclusionLattice(included, possibly);
	}
}
