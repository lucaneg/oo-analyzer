package it.lucaneg.oo.analyzer.analyses.strings.stringsuffix;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringExpressionEvaluator;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;

public class StringSuffixExpressionEvaluator extends BaseStringExpressionEvaluator<StringSuffixLattice> {

	@Override
	protected StringSuffixLattice latticeBottom() {
		return StringSuffixLattice.getBottom();
	}

	@Override
	protected StringSuffixLattice latticeTop() {
		return StringSuffixLattice.getTop();
	}

	@Override
	protected StringSuffixLattice provideApproxFor(StringLiteral literal) {
		return new StringSuffixLattice(literal.getValue());
	}

	@Override
	protected StringSuffixLattice evalSubstring(StringSuffixLattice receiver, int begin, int end) {
		return latticeTop();
	}

	@Override
	protected StringSuffixLattice evalReplace(StringSuffixLattice receiver, StringSuffixLattice first,
			StringSuffixLattice second) {
		if (receiver.isTop() || first.isTop() || second.isTop())
			return receiver;

		String toReplace = first.getSuffix();
		String str = second.getSuffix();
		String target = receiver.getSuffix();

		if (!target.contains(toReplace))
			return receiver;

		return new StringSuffixLattice(target.replace(toReplace, str));
	}

	@Override
	protected StringSuffixLattice evalConcat(StringSuffixLattice receiver, StringSuffixLattice parameter) {
		return parameter;
	}
}
