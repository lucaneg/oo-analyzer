package it.lucaneg.oo.analyzer.analyses.strings.stringprefix;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringExpressionEvaluator;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;

public class StringPrefixExpressionEvaluator extends BaseStringExpressionEvaluator<StringPrefixLattice> {

	@Override
	protected StringPrefixLattice latticeBottom() {
		return StringPrefixLattice.getBottom();
	}

	@Override
	protected StringPrefixLattice latticeTop() {
		return StringPrefixLattice.getTop();
	}

	@Override
	protected StringPrefixLattice provideApproxFor(StringLiteral literal) {
		return new StringPrefixLattice(literal.getValue());
	}

	@Override
	protected StringPrefixLattice evalSubstring(StringPrefixLattice receiver, int begin, int end) {
		if (receiver.isTop())
			return receiver;

		if (begin > end || begin < 0)
			return latticeBottom();

		if (end <= receiver.getPrefix().length())
			return new StringPrefixLattice(receiver.getPrefix().substring(begin, end));
		else if (begin < receiver.getPrefix().length())
			return new StringPrefixLattice(receiver.getPrefix().substring(begin));

		return StringPrefixLattice.getTop();
	}

	@Override
	protected StringPrefixLattice evalReplace(StringPrefixLattice receiver, StringPrefixLattice first,
			StringPrefixLattice second) {
		if (receiver.isTop() || first.isTop() || second.isTop())
			return receiver;

		String toReplace = first.getPrefix();
		String str = second.getPrefix();
		String target = receiver.getPrefix();

		if (!target.contains(toReplace))
			return receiver;

		return new StringPrefixLattice(target.replace(toReplace, str));
	}

	@Override
	protected StringPrefixLattice evalConcat(StringPrefixLattice receiver, StringPrefixLattice parameter) {
		return receiver;
	}
}
