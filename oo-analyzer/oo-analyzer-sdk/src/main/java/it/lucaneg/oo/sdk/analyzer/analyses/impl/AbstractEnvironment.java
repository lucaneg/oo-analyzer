package it.lucaneg.oo.sdk.analyzer.analyses.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;

import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;

public abstract class AbstractEnvironment<L extends AbstractValue<L>, E extends AbstractEnvironment<L, E>> implements Environment<L, E> {

	private boolean unreachable;
	
	/**
	 * The approximations of each local variable
	 */
	protected final Map<MLocalVariable, L> approximations;

	/**
	 * Builds an empty environment
	 */
	protected AbstractEnvironment() {
		approximations = new HashMap<>();
		unreachable = false;
	}

	/**
	 * Builds an environment by copying (shallow copy) the given one
	 * 
	 * @param other
	 */
	protected AbstractEnvironment(AbstractEnvironment<L, E> other) {
		this.approximations = new HashMap<>(other.approximations);
		this.unreachable = other.unreachable;
	}
	
	@Override
	public final boolean isUnreachable() {
		return unreachable;
	}
	
	@Override
	public void makeUnreachable() {
		this.unreachable = true;
	}
	
	@Override
	public final void makeReachable() {
		this.unreachable = false;		
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
	public void set(String name, L approx) {
		if (hasApproximationFor(name)) {
			Optional<MLocalVariable> var = approximations.keySet().stream().filter(key -> key.getName().equals(name)).findFirst();
			set(var.get(), approx);
			return;
		}
		
		throw new IllegalArgumentException("Cannot set approximation for unknown variable " + name);
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
	public final E only(Collection<MLocalVariable> vars) {
		E result = copy();
		for (Pair<MLocalVariable, L> pair : this)
			if (!vars.contains(pair.getLeft()))
				result.remove(pair.getLeft());
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final E join(E other, BiFunction<L, L, L> elementJoiner) {
		if (other == null)
			return (E) this;

		E join = copy();

		if (isUnreachable() && other.isUnreachable())
			join.makeUnreachable();
		else 
			join.makeReachable();
		
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
		return isUnreachable() ? "{unreachable code}" : approximations.toString();
	}

	@Override
	public final Iterator<Pair<MLocalVariable, L>> iterator() {
		return approximations.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())).iterator();
	}
}
