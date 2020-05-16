package it.lucaneg.oo.analyzer.analyses.strings.string;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringExpressionEvaluator;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;
import it.unive.strings.AutomatonString;

public class StringExpressionEvaluator extends BaseStringExpressionEvaluator<StringLattice> {
	@Override
	protected StringLattice latticeBottom() {
		return StringLattice.getBottom();
	}

	@Override
	protected StringLattice latticeTop() {
		return StringLattice.getTop();
	}

	@Override
	protected StringLattice provideApproxFor(StringLiteral literal) {
		return new StringLattice(new AutomatonString(literal.getValue()));
	}

	@Override
	protected StringLattice evalSubstring(StringLattice receiver, int begin, int end) {
		return new StringLattice(receiver.getString().substring(begin, end));
	}

	@Override
	protected StringLattice evalConcat(StringLattice receiver, StringLattice parameter) {
		return new StringLattice(receiver.getString().concat(parameter.getString()));
	}

	@Override
	protected StringLattice evalReplace(StringLattice receiver, StringLattice first, StringLattice second) {
		return new StringLattice(receiver.getString().replace(first.getString(), second.getString()));
	}
}
