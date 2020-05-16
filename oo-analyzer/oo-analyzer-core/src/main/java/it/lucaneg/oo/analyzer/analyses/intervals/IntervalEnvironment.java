package it.lucaneg.oo.analyzer.analyses.intervals;

import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractEnvironment;

public class IntervalEnvironment extends AbstractEnvironment<IntervalLattice, IntervalEnvironment> {
	
	public IntervalEnvironment() {
	}
	
	private IntervalEnvironment(IntervalEnvironment other) {
		super(other.approximations);
	}
	
	@Override
	public IntervalEnvironment copy() {
		return new IntervalEnvironment(this);
	}
	
	@Override
	protected IntervalLattice latticeBottom() {
		return IntervalLattice.getBottom();
	}
	
	@Override
	protected IntervalLattice latticeTop() {
		return IntervalLattice.getTop();
	}
	
	@Override
	public IntervalLattice defaultLatticeForType(Type t) {
		return t == IntType.INSTANCE ? latticeTop() : latticeBottom();
	}
}
