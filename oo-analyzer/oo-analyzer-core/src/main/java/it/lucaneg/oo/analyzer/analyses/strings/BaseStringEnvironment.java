package it.lucaneg.oo.analyzer.analyses.strings;

import java.util.Map;

import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractEnvironment;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractLattice;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;

public abstract class BaseStringEnvironment<L extends AbstractLattice<L>, E extends BaseStringEnvironment<L, E>>
		extends AbstractEnvironment<L, E> {

	protected BaseStringEnvironment() {
		super();
	}

	/**
	 * Builds a string environment by shallow copying the given one
	 * 
	 * @param other the original environment
	 */
	protected BaseStringEnvironment(Map<MLocalVariable, L> approximations) {
		super(approximations);
	}

	@Override
	public final L defaultLatticeForType(Type t) {
		return t == Type.getStringType() ? latticeTop() : latticeBottom();
	}
}
