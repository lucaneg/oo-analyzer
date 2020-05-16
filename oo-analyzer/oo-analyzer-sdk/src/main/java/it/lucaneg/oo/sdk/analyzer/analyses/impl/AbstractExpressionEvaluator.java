package it.lucaneg.oo.sdk.analyzer.analyses.impl;

import it.lucaneg.oo.ast.expression.Expression;
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
import it.lucaneg.oo.ast.expression.creation.NewArray;
import it.lucaneg.oo.ast.expression.creation.NewObject;
import it.lucaneg.oo.ast.expression.dereference.ArrayAccess;
import it.lucaneg.oo.ast.expression.dereference.Call;
import it.lucaneg.oo.ast.expression.dereference.FieldAccess;
import it.lucaneg.oo.ast.expression.literal.Literal;
import it.lucaneg.oo.ast.expression.logical.And;
import it.lucaneg.oo.ast.expression.logical.Not;
import it.lucaneg.oo.ast.expression.logical.Or;
import it.lucaneg.oo.ast.expression.typeCheck.Cast;
import it.lucaneg.oo.ast.expression.typeCheck.TypeCheck;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionEvaluator;

@SuppressWarnings("unchecked")
public abstract class AbstractExpressionEvaluator<L extends AbstractLattice<L>> implements ExpressionEvaluator<L> {

	@Override
	public L eval(Expression e, Environment<?, ?> env) {
		return eval(e, env, new AbstractExpressionEvaluator<L>() {});
	}
	
	@Override
	public final L eval(Expression e, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		// type expression is not needed since it is used only during parsing
		if (e instanceof ArrayAccess)
			return evalArrayAccess((ArrayAccess) e, env, parent);
		else if (e instanceof Call)
			return evalCall((Call) e, env, parent);
		else if (e instanceof Cast)
			return evalCast((Cast) e, env, parent);
		else if (e instanceof FieldAccess)
			return evalFieldAccess((FieldAccess) e, env, parent);
		else if (e instanceof Minus)
			return evalMinus((Minus) e, env, parent);
		else if (e instanceof NewArray)
			return evalNewArray((NewArray) e, env, parent);
		else if (e instanceof NewObject)
			return evalNewObject((NewObject) e, env, parent);
		else if (e instanceof Not)
			return evalNot((Not) e, env, parent);
		else if (e instanceof TypeCheck)
			return evalTypeCheck((TypeCheck) e, env, parent);
		else if (e instanceof Variable)
			return evalVariable((Variable) e, env, parent);
		else if (e instanceof Literal)
			return evalLiteral((Literal) e, env, parent);
		else if (e instanceof Addition)
			return evalAddition((Addition) e, env, parent);
		else if (e instanceof Division)
			return evalDivision((Division) e, env, parent);
		else if (e instanceof Module)
			return evalModule((Module) e, env, parent);
		else if (e instanceof Multiplication)
			return evalMultiplication((Multiplication) e, env, parent);
		else if (e instanceof Subtraction)
			return evalSubtraction((Subtraction) e, env, parent);
		else if (e instanceof And)
			return evalAnd((And) e, env, parent);
		else if (e instanceof Or)
			return evalOr((Or) e, env, parent);
		else if (e instanceof Equal)
			return evalEqual((Equal) e, env, parent);
		else if (e instanceof NotEqual)
			return evalNotEqual((NotEqual) e, env, parent);
		else if (e instanceof Greater)
			return evalGreater((Greater) e, env, parent);
		else if (e instanceof GreaterOrEqual)
			return evalGreaterOrEqual((GreaterOrEqual) e, env, parent);
		else if (e instanceof Less)
			return evalLess((Less) e, env, parent);
		else if (e instanceof LessOrEqual)
			return evalLessOrEqual((LessOrEqual) e, env, parent);
		else
			return (L) env.defaultLatticeForType(e.getStaticType());
	}

	protected L evalArrayAccess(ArrayAccess array, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(array.getStaticType());
	}

	protected L evalCall(Call call, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(call.getStaticType());
	}

	protected L evalCast(Cast as, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(as.getStaticType());
	}

	protected L evalFieldAccess(FieldAccess field, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(field.getStaticType());
	}

	protected L evalMinus(Minus minus, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(minus.getStaticType());
	}

	protected L evalNewArray(NewArray newarr, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(newarr.getStaticType());
	}

	protected L evalNewObject(NewObject newobj, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(newobj.getStaticType());
	}

	protected L evalNot(Not not, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(not.getStaticType());
	}

	protected L evalTypeCheck(TypeCheck is, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(is.getStaticType());
	}

	protected final L evalVariable(Variable var, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		String name = ((Variable) var).getName();
		if (env.hasApproximationFor(name))
			return (L) env.at(name);

		return (L) env.defaultLatticeForType(var.getStaticType());
	}

	protected L evalLiteral(Literal literal, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(literal.getStaticType());
	}

	protected L evalAddition(Addition add, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(add.getStaticType());
	}

	protected L evalDivision(Division div, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(div.getStaticType());
	}

	protected L evalModule(Module mod, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(mod.getStaticType());
	}

	protected L evalMultiplication(Multiplication mul, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(mul.getStaticType());
	}

	protected L evalSubtraction(Subtraction sub, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(sub.getStaticType());
	}

	protected L evalAnd(And and, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(and.getStaticType());
	}

	protected L evalOr(Or or, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(or.getStaticType());
	}

	protected L evalEqual(Equal eq, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(eq.getStaticType());
	}

	protected L evalNotEqual(NotEqual ne, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(ne.getStaticType());
	}

	protected L evalGreater(Greater gt, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(gt.getStaticType());
	}

	protected L evalGreaterOrEqual(GreaterOrEqual ge, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(ge.getStaticType());
	}

	protected L evalLess(Less lt, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(lt.getStaticType());
	}

	protected L evalLessOrEqual(LessOrEqual le, Environment<?, ?> env, ExpressionEvaluator<?> parent) {
		return (L) env.defaultLatticeForType(le.getStaticType());
	}
}
