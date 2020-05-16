package it.lucaneg.oo.analyzer.analyses.strings;

import it.lucaneg.oo.ast.expression.Variable;
import it.lucaneg.oo.ast.expression.dereference.Call;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractLattice;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractSatisfiabilityEvaluator;

public abstract class BaseStringSatisfiabilityEvaluator<L extends AbstractLattice<L>> extends AbstractSatisfiabilityEvaluator {
	
	protected abstract L latticeBottom();

	protected abstract L latticeTop();
	
	@Override
	@SuppressWarnings("unchecked")
	public final Satisfiability satisfiesCall(Call call, Environment<?, ?> env, ExpressionEvaluator<?> evaluator) {
		if (call.getReceiver().asExpression().getStaticType() != Type.getStringType())
			return Satisfiability.UNKNOWN;

		L rec = latticeTop();
		if (call.getReceiver().asExpression() instanceof Variable)
			rec = (L) evaluator.eval((Variable) call.getReceiver(), env, evaluator);

		if (call.getActuals().length == 1 && call.getActuals()[0].asExpression().getStaticType() == Type.getStringType()) {
			L par = latticeTop();
			if (call.getActuals()[0].asExpression() instanceof Variable)
				par = (L) evaluator.eval((Variable) call.getActuals()[0], env, evaluator);
			else if (call.getActuals()[0].asExpression() instanceof StringLiteral)
				par = (L) evaluator.eval((StringLiteral) call.getActuals()[0], env, evaluator);

			if (call.getName().equals("equals"))
				return satisfiesEquals(rec, par);
			else if (call.getName().equals("contains"))
				return satisfiesContains(rec, par);
			else if (call.getName().equals("startsWith"))
				return satisfiesStartsWith(rec, par);
			else if (call.getName().equals("endsWith"))
				return satisfiesEndsWith(rec, par);
		}

		return Satisfiability.UNKNOWN;
	}

	protected abstract Satisfiability satisfiesEndsWith(L rec, L par);

	protected abstract Satisfiability satisfiesStartsWith(L rec, L par);

	protected abstract Satisfiability satisfiesContains(L rec, L par);

	protected abstract Satisfiability satisfiesEquals(L rec, L par);
}
