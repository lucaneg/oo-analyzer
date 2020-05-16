package it.lucaneg.oo.analyzer.analyses.bool;

import it.lucaneg.oo.ast.types.BooleanType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractEnvironment;

public class BooleanEnvironment extends AbstractEnvironment<BooleanLattice, BooleanEnvironment> {
	
	public BooleanEnvironment() {
	}
	
	private BooleanEnvironment(BooleanEnvironment other) {
		super(other.approximations);
	}
	
	@Override
	public BooleanEnvironment copy() {
		return new BooleanEnvironment(this);
	}
	
	@Override
	protected BooleanLattice latticeBottom() {
		return BooleanLattice.getBottom();
	}
	
	@Override
	protected BooleanLattice latticeTop() {
		return BooleanLattice.getTop();
	}
	
	@Override
	public BooleanLattice defaultLatticeForType(Type t) {
		return t == BooleanType.INSTANCE ? latticeTop() : latticeBottom();
	}
}
