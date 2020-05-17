package it.lucaneg.oo.analyzer.analyses.value;

import it.lucaneg.oo.analyzer.analyses.value.domains.bools.AbstractBooleanLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.strings.AbstractStringLattice;
import it.lucaneg.oo.ast.expression.Variable;
import it.lucaneg.oo.ast.expression.arithmetic.Addition;
import it.lucaneg.oo.ast.expression.arithmetic.Division;
import it.lucaneg.oo.ast.expression.arithmetic.Minus;
import it.lucaneg.oo.ast.expression.arithmetic.Module;
import it.lucaneg.oo.ast.expression.arithmetic.Multiplication;
import it.lucaneg.oo.ast.expression.arithmetic.Subtraction;
import it.lucaneg.oo.ast.expression.comparison.Equal;
import it.lucaneg.oo.ast.expression.comparison.Greater;
import it.lucaneg.oo.ast.expression.comparison.GreaterOrEqual;
import it.lucaneg.oo.ast.expression.comparison.Less;
import it.lucaneg.oo.ast.expression.comparison.LessOrEqual;
import it.lucaneg.oo.ast.expression.comparison.NotEqual;
import it.lucaneg.oo.ast.expression.dereference.Call;
import it.lucaneg.oo.ast.expression.literal.FalseLiteral;
import it.lucaneg.oo.ast.expression.literal.IntLiteral;
import it.lucaneg.oo.ast.expression.literal.Literal;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;
import it.lucaneg.oo.ast.expression.literal.TrueLiteral;
import it.lucaneg.oo.ast.expression.logical.And;
import it.lucaneg.oo.ast.expression.logical.Not;
import it.lucaneg.oo.ast.expression.logical.Or;
import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractExpressionEvaluator;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ValueExpressionEvaluator extends AbstractExpressionEvaluator<ValueLattice, ValueEnvironment> {

	private AbstractBooleanLattice<?> booleanSingleton;

	private AbstractIntegerLattice<?> intSingleton;

	private AbstractStringLattice<?> stringSingleton;
	
	public void setBooleanSingleton(AbstractBooleanLattice<?> booleanSingleton) {
		this.booleanSingleton = booleanSingleton;
	}
	
	public void setIntSingleton(AbstractIntegerLattice<?> intSingleton) {
		this.intSingleton = intSingleton;
	}
	
	public void setStringSingleton(AbstractStringLattice<?> stringSingleton) {
		this.stringSingleton = stringSingleton;
	}

	@Override
	protected ValueLattice evalCall(Call call, ValueEnvironment env) {
		if (call.getStaticType() != Type.getStringType())
			return env.defaultLatticeForType(call.getStaticType());
		
		if (call.getReceiver().asExpression().getStaticType() != Type.getStringType())
			return new ValueLattice(stringSingleton.top());
		
		if (!(call.getReceiver().asExpression() instanceof Variable)) 
			// TODO only variable supported rn
			return new ValueLattice(stringSingleton.top());
		
		ValueLattice original = eval(call.getReceiver().asExpression(), env);
		AbstractStringLattice rec = (AbstractStringLattice) original.getInnerElement();

		if (call.getName().equals("concat")) {
			AbstractStringLattice par;
			if (call.getActuals()[0].asExpression() instanceof Variable)
				par = (AbstractStringLattice) evalVariable((Variable) call.getActuals()[0], env).getInnerElement();
			else if (call.getActuals()[0].asExpression() instanceof Literal) 
				par = (AbstractStringLattice) evalLiteral((StringLiteral) call.getActuals()[0], env).getInnerElement();
			else
				// TODO no fields and array elements
				return new ValueLattice(stringSingleton.top());
				
			if (par.isBottom())
				// we the parameter is a non-string value
				// that gets converted to a string. We consider
				// its conversion as top
				par = stringSingleton.top();
			
			return new ValueLattice(rec.concat(par));
		} else if (call.getName().equals("substring")) {
			if (!(call.getActuals()[0].asExpression() instanceof IntLiteral && call.getActuals()[1].asExpression() instanceof IntLiteral))
				return new ValueLattice(stringSingleton.top());

			int begin = ((IntLiteral) call.getActuals()[0].asExpression()).getValue(); // TODO
			int end = ((IntLiteral) call.getActuals()[1].asExpression()).getValue(); // TODO
			
			if (begin > end || begin < 0)
				return ValueLattice.getBottom();
			
			return new ValueLattice(rec.substring(begin, end));
		} else if (call.getName().equals("replace")) {
			AbstractStringLattice first, second;
			if (call.getActuals()[0].asExpression() instanceof Variable)
				first = (AbstractStringLattice) evalVariable((Variable) call.getActuals()[0], env).getInnerElement();
			else if (call.getActuals()[0].asExpression() instanceof Literal) 
				first = (AbstractStringLattice) evalLiteral((StringLiteral) call.getActuals()[0], env).getInnerElement();
			else
				return new ValueLattice(stringSingleton.top());
				
			if (first.isBottom())
				// we the parameter is a non-string value
				// that gets converted to a string. We consider
				// its conversion as top
				first = stringSingleton.top();
			
			if (call.getActuals()[1].asExpression() instanceof Variable)
				second = (AbstractStringLattice) evalVariable((Variable) call.getActuals()[1], env).getInnerElement();
			else if (call.getActuals()[1].asExpression() instanceof Literal) 
				second = (AbstractStringLattice) evalLiteral((StringLiteral) call.getActuals()[1], env).getInnerElement();
			else
				return new ValueLattice(stringSingleton.top());
				
			if (second.isBottom())
				// we the parameter is a non-string value
				// that gets converted to a string. We consider
				// its conversion as top
				second = stringSingleton.top();
			
			return new ValueLattice(rec.replace(first, second));
		}

		return original;
	}

	@Override
	protected ValueLattice evalMinus(Minus minus, ValueEnvironment env) {
		ValueLattice expr = eval(minus.getExpression(), env);
		
		ValueLattice result = baseCases(intSingleton.top(), expr);
		if (result != null)
			return result;

		AbstractIntegerLattice approx = (AbstractIntegerLattice) expr.getInnerElement();
		return new ValueLattice(approx.mul(intSingleton.minusOne()));
	}

	@Override
	protected ValueLattice evalNot(Not not, ValueEnvironment env) {
		ValueLattice inner = eval(not.getExpression(), env);

		ValueLattice result = baseCases(booleanSingleton.top(), inner);
		if (result != null)
			return result;

		return inner.getInnerElement() == booleanSingleton.getFalse() ? new ValueLattice(booleanSingleton.getTrue())
				: new ValueLattice(booleanSingleton.getFalse());
	}

	@Override
	protected ValueLattice evalLiteral(Literal literal, ValueEnvironment env) {
		if (literal.getStaticType() == IntType.INSTANCE)
			return new ValueLattice(intSingleton.mk(((IntLiteral) literal).getValue()));
		if (literal instanceof FalseLiteral)
			return new ValueLattice(booleanSingleton.getFalse());
		if (literal instanceof TrueLiteral)
			return new ValueLattice(booleanSingleton.getTrue());
		if (literal.getStaticType() == Type.getStringType())
			return new ValueLattice(stringSingleton.mk(((StringLiteral) literal).getValue()));
		return (ValueLattice) env.defaultLatticeForType(literal.getStaticType());
	}

	@Override
	protected ValueLattice evalAddition(Addition add, ValueEnvironment env) {
		ValueLattice left = eval(add.getLeft(), env);
		ValueLattice right = eval(add.getRight(), env);
		
		ValueLattice result = baseCases(intSingleton.top(), left, right);
		if (result != null)
			return result;
		
		AbstractIntegerLattice lapprox = (AbstractIntegerLattice) left.getInnerElement();
		AbstractIntegerLattice rapprox = (AbstractIntegerLattice) right.getInnerElement();
		return new ValueLattice(lapprox.plus(rapprox));
	}

	@Override
	protected ValueLattice evalDivision(Division div, ValueEnvironment env) {
		ValueLattice left = eval(div.getLeft(), env);
		ValueLattice right = eval(div.getRight(), env);
		
		ValueLattice result = baseCases(intSingleton.top(), left, right);
		if (result != null)
			return result;
		
//		AbstractIntegerLattice lapprox = (AbstractIntegerLattice) left.getInnerElement();
//		AbstractIntegerLattice rapprox = (AbstractIntegerLattice) right.getInnerElement();
//		return new ValueLattice(lapprox.div(rapprox));
		return new ValueLattice(intSingleton.top()); // TODO
	}

	@Override
	protected ValueLattice evalModule(Module mod, ValueEnvironment env) {
		ValueLattice left = eval(mod.getLeft(), env);
		ValueLattice right = eval(mod.getRight(), env);
		
		ValueLattice result = baseCases(intSingleton.top(), left, right);
		if (result != null)
			return result;
		
//		AbstractIntegerLattice lapprox = (AbstractIntegerLattice) left.getInnerElement();
//		AbstractIntegerLattice rapprox = (AbstractIntegerLattice) right.getInnerElement();
//		return new ValueLattice(lapprox.mod(rapprox));
		return new ValueLattice(intSingleton.top()); // TODO
	}

	@Override
	protected ValueLattice evalMultiplication(Multiplication mul, ValueEnvironment env) {
		ValueLattice left = eval(mul.getLeft(), env);
		ValueLattice right = eval(mul.getRight(), env);
		
		ValueLattice result = baseCases(intSingleton.top(), left, right);
		if (result != null)
			return result;
		
		AbstractIntegerLattice lapprox = (AbstractIntegerLattice) left.getInnerElement();
		AbstractIntegerLattice rapprox = (AbstractIntegerLattice) right.getInnerElement();
		return new ValueLattice(lapprox.mul(rapprox));
	}

	@Override
	protected ValueLattice evalSubtraction(Subtraction sub, ValueEnvironment env) {
		ValueLattice left = eval(sub.getLeft(), env);
		ValueLattice right = eval(sub.getRight(), env);
		
		ValueLattice result = baseCases(intSingleton.top(), left, right);
		if (result != null)
			return result;
		
		AbstractIntegerLattice lapprox = (AbstractIntegerLattice) left.getInnerElement();
		AbstractIntegerLattice rapprox = (AbstractIntegerLattice) right.getInnerElement();
		return new ValueLattice(lapprox.diff(rapprox));
	}

	@Override
	protected ValueLattice evalAnd(And and, ValueEnvironment env) {
		ValueLattice left = eval(and.getLeft(), env);
		ValueLattice right = eval(and.getRight(), env);

		ValueLattice result = baseCases(booleanSingleton.top(), left, right);
		if (result != null)
			return result;

		if (left.getInnerElement() == booleanSingleton.getFalse()
				|| right.getInnerElement() == booleanSingleton.getFalse())
			return new ValueLattice(booleanSingleton.getFalse());

		return new ValueLattice(booleanSingleton.getTrue());
	}

	@Override
	protected ValueLattice evalOr(Or or, ValueEnvironment env) {
		ValueLattice left = eval(or.getLeft(), env);
		ValueLattice right = eval(or.getRight(), env);

		ValueLattice result = baseCases(booleanSingleton.top(), left, right);
		if (result != null)
			return result;

		if (left.getInnerElement() == booleanSingleton.getFalse()
				&& right.getInnerElement() == booleanSingleton.getFalse())
			return new ValueLattice(booleanSingleton.getFalse());

		return new ValueLattice(booleanSingleton.getTrue());
	}

	@Override
	protected ValueLattice evalEqual(Equal eq, ValueEnvironment env) {
		ValueLattice left = eval(eq.getLeft(), env);
		ValueLattice right = eval(eq.getRight(), env);

		ValueLattice result = baseCases(booleanSingleton.top(), left, right);
		if (result != null)
			return result;

		Boolean res = left.getInnerElement().isEqualTo(right.getInnerElement());
		if (res == null)
			return new ValueLattice(booleanSingleton.top());
		if (res)
			return new ValueLattice(booleanSingleton.getTrue());

		return new ValueLattice(booleanSingleton.getFalse());
	}

	@Override
	protected ValueLattice evalNotEqual(NotEqual ne, ValueEnvironment env) {
		ValueLattice left = eval(ne.getLeft(), env);
		ValueLattice right = eval(ne.getRight(), env);

		ValueLattice result = baseCases(booleanSingleton.top(), left, right);
		if (result != null)
			return result;

		Boolean res = left.getInnerElement().isEqualTo(right.getInnerElement());
		if (res == null)
			return new ValueLattice(booleanSingleton.top());
		if (!res)
			return new ValueLattice(booleanSingleton.getTrue());

		return new ValueLattice(booleanSingleton.getFalse());
	}

	@Override
	protected ValueLattice evalGreater(Greater gt, ValueEnvironment env) {
		ValueLattice left = eval(gt.getLeft(), env);
		ValueLattice right = eval(gt.getRight(), env);

		ValueLattice result = baseCases(booleanSingleton.top(), left, right);
		if (result != null)
			return result;

		Boolean res = left.getInnerElement().isGreaterThan(right.getInnerElement());
		if (res == null)
			return new ValueLattice(booleanSingleton.top());
		if (res)
			return new ValueLattice(booleanSingleton.getTrue());

		return new ValueLattice(booleanSingleton.getFalse());
	}

	@Override
	protected ValueLattice evalGreaterOrEqual(GreaterOrEqual ge, ValueEnvironment env) {
		ValueLattice left = eval(ge.getLeft(), env);
		ValueLattice right = eval(ge.getRight(), env);

		ValueLattice result = baseCases(booleanSingleton.top(), left, right);
		if (result != null)
			return result;

		Boolean res = left.getInnerElement().isLessThen(right.getInnerElement());
		if (res == null)
			return new ValueLattice(booleanSingleton.top());
		if (!res)
			return new ValueLattice(booleanSingleton.getTrue());

		return new ValueLattice(booleanSingleton.getFalse());
	}

	@Override
	protected ValueLattice evalLess(Less lt, ValueEnvironment env) {
		ValueLattice left = eval(lt.getLeft(), env);
		ValueLattice right = eval(lt.getRight(), env);

		ValueLattice result = baseCases(booleanSingleton.top(), left, right);
		if (result != null)
			return result;

		Boolean res = left.getInnerElement().isLessThen(right.getInnerElement());
		if (res == null)
			return new ValueLattice(booleanSingleton.top());
		if (res)
			return new ValueLattice(booleanSingleton.getTrue());

		return new ValueLattice(booleanSingleton.getFalse());
	}

	@Override
	protected ValueLattice evalLessOrEqual(LessOrEqual le, ValueEnvironment env) {
		ValueLattice left = eval(le.getLeft(), env);
		ValueLattice right = eval(le.getRight(), env);

		ValueLattice result = baseCases(booleanSingleton.top(), left, right);
		if (result != null)
			return result;

		Boolean res = left.getInnerElement().isGreaterThan(right.getInnerElement());
		if (res == null)
			return new ValueLattice(booleanSingleton.top());
		if (!res)
			return new ValueLattice(booleanSingleton.getTrue());

		return new ValueLattice(booleanSingleton.getFalse());
	}

	private ValueLattice baseCases(SingleValueLattice<?> top, ValueLattice... elements) {
		boolean bottom = false, innerTop = false;
		for (ValueLattice el : elements)
			if (el.isTop())
				return ValueLattice.getTop();
			else if (el.isBottom() || el.getInnerElement().getClass() != top.getClass())
				bottom = true;
			else if (el.getInnerElement().isTop())
				innerTop = true;

		if (bottom)
			return ValueLattice.getBottom();

		if (innerTop)
			return new ValueLattice(top);

		return null;
	}
}
