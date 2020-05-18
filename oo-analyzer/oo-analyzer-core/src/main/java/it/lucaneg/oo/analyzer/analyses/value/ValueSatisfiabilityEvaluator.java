package it.lucaneg.oo.analyzer.analyses.value;

import it.lucaneg.oo.analyzer.analyses.value.domains.bools.AbstractBooleanLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.strings.AbstractStringLattice;
import it.lucaneg.oo.ast.expression.Variable;
import it.lucaneg.oo.ast.expression.comparison.Equal;
import it.lucaneg.oo.ast.expression.comparison.Greater;
import it.lucaneg.oo.ast.expression.comparison.Less;
import it.lucaneg.oo.ast.expression.dereference.Call;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;
import it.lucaneg.oo.ast.expression.typeCheck.TypeCheck;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractSatisfiabilityEvaluator;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ValueSatisfiabilityEvaluator extends AbstractSatisfiabilityEvaluator<ValueLattice, ValueEnvironment> {

	@Override
	protected Satisfiability satisfiesCall(Call e, ValueEnvironment env,
			ExpressionEvaluator<ValueLattice, ValueEnvironment> evaluator) {
		if (e.getReceiver().asExpression().getStaticType() != Type.getStringType())
			return Satisfiability.UNKNOWN;

		ValueLattice rec;
		if (e.getReceiver().asExpression() instanceof Variable)
			rec = evaluator.eval((Variable) e.getReceiver(), env);
		else
			// TODO
			return Satisfiability.UNKNOWN;

		if (e.getActuals().length == 1 && e.getActuals()[0].asExpression().getStaticType() == Type.getStringType()) {
			ValueLattice par;
			if (e.getActuals()[0].asExpression() instanceof Variable)
				par = evaluator.eval((Variable) e.getActuals()[0], env);
			else if (e.getActuals()[0].asExpression() instanceof StringLiteral)
				par = evaluator.eval((StringLiteral) e.getActuals()[0], env);
			else
				// TODO
				return Satisfiability.UNKNOWN;

			if (!(rec.getInnerElement() instanceof AbstractStringLattice<?>)
					|| !(par.getInnerElement() instanceof AbstractStringLattice<?>))
				return Satisfiability.UNKNOWN;

			AbstractStringLattice receiver = (AbstractStringLattice) rec.getInnerElement();
			AbstractStringLattice parameter = (AbstractStringLattice) par.getInnerElement();
			
			if (receiver.isTop() || parameter.isTop())
				return Satisfiability.UNKNOWN;
			
			if (e.getName().equals("equals"))
				return receiver.isEquals(parameter);
			else if (e.getName().equals("contains"))
				return receiver.contains(parameter);
			else if (e.getName().equals("startsWith"))
				return receiver.startsWith(parameter);
			else if (e.getName().equals("endsWith"))
				return receiver.endsWith(parameter);
		}

		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesTypeCheck(TypeCheck e, ValueEnvironment env,
			ExpressionEvaluator<ValueLattice, ValueEnvironment> evaluator) {
		return Satisfiability.UNKNOWN; // TODO
	}

	@Override
	protected Satisfiability satisfiesVariable(Variable e, ValueEnvironment env,
			ExpressionEvaluator<ValueLattice, ValueEnvironment> evaluator) {
		ValueLattice eval = (ValueLattice) env.at(e.getName());
		if (eval.getInnerElement() instanceof AbstractBooleanLattice<?>)
			return ((AbstractBooleanLattice<?>) eval.getInnerElement()).toSatisfiability();

		return Satisfiability.UNKNOWN;
	}

	@Override
	protected Satisfiability satisfiesLess(Less e, ValueEnvironment env,
			ExpressionEvaluator<ValueLattice, ValueEnvironment> evaluator) {
		ValueLattice left = evaluator.eval(e.getLeft(), env);
		ValueLattice right = evaluator.eval(e.getRight(), env);

		Satisfiability result = baseCases(left, right);
		if (result != null)
			return result;

		if (left.getInnerElement().getClass() != right.getInnerElement().getClass())
			// different types
			return Satisfiability.NOT_SATISFIED;

		Boolean res = left.getInnerElement().isLessThen(right.getInnerElement());
		if (res == null)
			return Satisfiability.UNKNOWN;
		return res ? Satisfiability.SATISFIED : Satisfiability.NOT_SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesGreater(Greater e, ValueEnvironment env,
			ExpressionEvaluator<ValueLattice, ValueEnvironment> evaluator) {
		ValueLattice left = evaluator.eval(e.getLeft(), env);
		ValueLattice right = evaluator.eval(e.getRight(), env);

		Satisfiability result = baseCases(left, right);
		if (result != null)
			return result;

		if (left.getInnerElement().getClass() != right.getInnerElement().getClass())
			// different types
			return Satisfiability.NOT_SATISFIED;

		Boolean res = left.getInnerElement().isGreaterThan(right.getInnerElement()); 
		if (res == null)
			return Satisfiability.UNKNOWN;
		return res ? Satisfiability.SATISFIED : Satisfiability.NOT_SATISFIED;
	}

	@Override
	protected Satisfiability satisfiesEqual(Equal e, ValueEnvironment env,
			ExpressionEvaluator<ValueLattice, ValueEnvironment> evaluator) {
		ValueLattice left = evaluator.eval(e.getLeft(), env);
		ValueLattice right = evaluator.eval(e.getRight(), env);

		Satisfiability result = baseCases(left, right);
		if (result != null)
			return result;

		if (left.getInnerElement().getClass() != right.getInnerElement().getClass())
			// different types
			return Satisfiability.NOT_SATISFIED;

		Boolean res = left.getInnerElement().isEqualTo(right.getInnerElement());
		if (res == null)
			return Satisfiability.UNKNOWN;
		return res ? Satisfiability.SATISFIED : Satisfiability.NOT_SATISFIED;
	}

	private Satisfiability baseCases(ValueLattice... elements) {
		for (ValueLattice el : elements)
			if (el.isTop() || el.isBottom() || el.getInnerElement().isTop())
				return Satisfiability.UNKNOWN;

		return null;
	}
}
