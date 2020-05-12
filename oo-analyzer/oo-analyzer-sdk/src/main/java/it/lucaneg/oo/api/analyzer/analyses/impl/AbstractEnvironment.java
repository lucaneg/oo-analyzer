package it.lucaneg.oo.api.analyzer.analyses.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;

import it.lucaneg.oo.api.analyzer.analyses.Environment;
import it.lucaneg.oo.api.analyzer.program.MLocalVariable;
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
import it.lucaneg.oo.ast.types.Type;

public abstract class AbstractEnvironment<L extends AbstractLattice<L>, E extends AbstractEnvironment<L, E>>
		extends AbstractExpressionSatisfiabilityEvaluator implements Environment<L, E> {

	/**
	 * The approximations of each local variable
	 */
	protected final Map<MLocalVariable, L> approximations;

	/**
	 * Builds an empty environment
	 */
	protected AbstractEnvironment() {
		approximations = new HashMap<>();
	}

	/**
	 * Builds an environment by copying (shallow copy) the given one
	 * 
	 * @param approximations
	 */
	protected AbstractEnvironment(Map<MLocalVariable, L> approximations) {
		this.approximations = new HashMap<>(approximations);
	}

	@Override
	public final L at(MLocalVariable var) {
		if (hasApproximationFor(var))
			return approximations.get(var);

		throw new IllegalArgumentException("No approximation found for " + var);
	}

	@Override
	public final void set(MLocalVariable var, L approx) {
		approximations.put(var, approx);
	}

	@Override
	public final boolean hasApproximationFor(MLocalVariable var) {
		return approximations.containsKey(var);
	}

	@Override
	public final boolean hasApproximationFor(String varName) {
		return approximations.entrySet().stream().anyMatch(entry -> entry.getKey().getName().equals(varName));
	}

	@Override
	public final L at(String varName) {
		if (hasApproximationFor(varName))
			return approximations.entrySet().stream().filter(entry -> entry.getKey().getName().equals(varName))
					.findFirst().get().getValue();

		throw new IllegalArgumentException("No approximation found for " + varName);
	}

	@Override
	public final void remove(MLocalVariable var) {
		if (hasApproximationFor(var))
			approximations.remove(var);
		else
			throw new IllegalArgumentException("No approximation found for " + var);
	}

	@Override
	public final void remove(String varName) {
		if (hasApproximationFor(varName)) {
			MLocalVariable var = approximations.entrySet().stream()
					.filter(entry -> entry.getKey().getName().equals(varName)).findFirst().get().getKey();
			approximations.remove(var);
		} else
			throw new IllegalArgumentException("No approximation found for " + varName);
	}

	@Override
	public final E except(Collection<String> vars) {
		E result = copy();
		for (String v : vars)
			if (result.hasApproximationFor(v))
				result.remove(v);
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final E join(E other, BiFunction<L, L, L> elementJoiner) {
		if (other == null)
			return (E) this;

		E join = copy();

		for (Map.Entry<MLocalVariable, L> entry : other.approximations.entrySet())
			if (join.hasApproximationFor(entry.getKey()))
				join.set(entry.getKey(), elementJoiner.apply(entry.getValue(), join.at(entry.getKey())));
			else
				join.set(entry.getKey(), entry.getValue());

		return join;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((approximations == null) ? 0 : approximations.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEnvironment<?, ?> other = (AbstractEnvironment<?, ?>) obj;
		if (approximations == null) {
			if (other.approximations != null)
				return false;
		} else if (!approximations.equals(other.approximations))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return approximations.toString();
	}

	@Override
	public final Iterator<Pair<MLocalVariable, L>> iterator() {
		return approximations.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())).iterator();
	}
	
	/**
	 * Yields the default abstract value for a variable of the given type. This
	 * method should always return bottom, except for the types that are targeted by
	 * this analysis. On such types, this method should return top.
	 * 
	 * @param t the type
	 * @return the appropriate default value
	 */
	protected abstract L defaultLatticeForType(Type t);
	
	/**
	 * Yields the bottom element of the lattice.
	 * 
	 * @return the bottom element
	 */
	protected abstract L latticeBottom();

	/**
	 * Yields the top element of the lattice.
	 * 
	 * @return the top element
	 */
	protected abstract L latticeTop();
	
	@Override
	public final L eval(Expression e) {
		// type expression is not needed since it is used only during parsing
		if (e instanceof ArrayAccess)
			return evalArrayAccess((ArrayAccess) e);
		else if (e instanceof Call)
			return evalCall((Call) e);
		else if (e instanceof Cast)
			return evalCast((Cast) e);
		else if (e instanceof FieldAccess)
			return evalFieldAccess((FieldAccess) e);
		else if (e instanceof Minus)
			return evalMinus((Minus) e);
		else if (e instanceof NewArray)
			return evalNewArray((NewArray) e);
		else if (e instanceof NewObject)
			return evalNewObject((NewObject) e);
		else if (e instanceof Not)
			return evalNot((Not) e);
		else if (e instanceof TypeCheck)
			return evalTypeCheck((TypeCheck) e);
		else if (e instanceof Variable)
			return evalVariable((Variable) e);
		else if (e instanceof Literal)
			return evalLiteral((Literal) e);
		else if (e instanceof Addition)
			return evalAddition((Addition) e);
		else if (e instanceof Division)
			return evalDivision((Division) e);
		else if (e instanceof Module)
			return evalModule((Module) e);
		else if (e instanceof Multiplication)
			return evalMultiplication((Multiplication) e);
		else if (e instanceof Subtraction)
			return evalSubtraction((Subtraction) e);
		else if (e instanceof And)
			return evalAnd((And) e);
		else if (e instanceof Or)
			return evalOr((Or) e);
		else if (e instanceof Equal)
			return evalEqual((Equal) e);
		else if (e instanceof NotEqual)
			return evalNotEqual((NotEqual) e);
		else if (e instanceof Greater)
			return evalGreater((Greater) e);
		else if (e instanceof GreaterOrEqual)
			return evalGreaterOrEqual((GreaterOrEqual) e);
		else if (e instanceof Less)
			return evalLess((Less) e);
		else if (e instanceof LessOrEqual)
			return evalLessOrEqual((LessOrEqual) e);
		else 
			return defaultLatticeForType(e.getStaticType());
	}

	protected L evalArrayAccess(ArrayAccess array) {
		return defaultLatticeForType(array.getStaticType());
	}

	protected L evalCall(Call call) {
		return defaultLatticeForType(call.getStaticType());
	}

	protected L evalCast(Cast as) {
		return defaultLatticeForType(as.getStaticType());
	}

	protected L evalFieldAccess(FieldAccess field) {
		return defaultLatticeForType(field.getStaticType());
	}

	protected L evalMinus(Minus minus) {
		return defaultLatticeForType(minus.getStaticType());
	}

	protected L evalNewArray(NewArray newarr) {
		return defaultLatticeForType(newarr.getStaticType());
	}

	protected L evalNewObject(NewObject newobj) {
		return defaultLatticeForType(newobj.getStaticType());
	}

	protected L evalNot(Not not) {
		return defaultLatticeForType(not.getStaticType());
	}

	protected L evalTypeCheck(TypeCheck is) {
		return defaultLatticeForType(is.getStaticType());
	}

	protected final L evalVariable(Variable var) {
		String name = ((Variable) var).getName();
		if (hasApproximationFor(name))
			return at(name);
		
		return defaultLatticeForType(var.getStaticType());
	}

	protected L evalLiteral(Literal literal) {
		return defaultLatticeForType(literal.getStaticType());
	}

	protected L evalAddition(Addition add) {
		return defaultLatticeForType(add.getStaticType());
	}

	protected L evalDivision(Division div) {
		return defaultLatticeForType(div.getStaticType());
	}

	protected L evalModule(Module mod) {
		return defaultLatticeForType(mod.getStaticType());
	}

	protected L evalMultiplication(Multiplication mul) {
		return defaultLatticeForType(mul.getStaticType());
	}

	protected L evalSubtraction(Subtraction sub) {
		return defaultLatticeForType(sub.getStaticType());
	}

	protected L evalAnd(And and) {
		return defaultLatticeForType(and.getStaticType());
	}

	protected L evalOr(Or or) {
		return defaultLatticeForType(or.getStaticType());
	}

	protected L evalEqual(Equal eq) {
		return defaultLatticeForType(eq.getStaticType());
	}

	protected L evalNotEqual(NotEqual ne) {
		return defaultLatticeForType(ne.getStaticType());
	}

	protected L evalGreater(Greater gt) {
		return defaultLatticeForType(gt.getStaticType());
	}

	protected L evalGreaterOrEqual(GreaterOrEqual ge) {
		return defaultLatticeForType(ge.getStaticType());
	}

	protected L evalLess(Less lt) {
		return defaultLatticeForType(lt.getStaticType());
	}

	protected L evalLessOrEqual(LessOrEqual le) {
		return defaultLatticeForType(le.getStaticType());
	}
}
