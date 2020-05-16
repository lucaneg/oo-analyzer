package it.lucaneg.oo.analyzer.analyses.intPropagation;

import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractEnvironment;

public class IntPropagationEnvironment extends AbstractEnvironment<IntPropagationLattice, IntPropagationEnvironment> {
	
	public IntPropagationEnvironment() {
	}
	
	private IntPropagationEnvironment(IntPropagationEnvironment other) {
		super(other.approximations);
	}
	
	@Override
	public IntPropagationEnvironment copy() {
		return new IntPropagationEnvironment(this);
	}
	
	@Override
	protected IntPropagationLattice latticeBottom() {
		return IntPropagationLattice.getBottom();
	}
	
	@Override
	protected IntPropagationLattice latticeTop() {
		return IntPropagationLattice.getTop();
	}
	
	@Override
	public IntPropagationLattice defaultLatticeForType(Type t) {
		return t == IntType.INSTANCE ? latticeTop() : latticeBottom();
	}
}
