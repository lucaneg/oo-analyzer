package it.lucaneg.oo.analyzer.analyses.value;

import it.lucaneg.oo.analyzer.analyses.value.domains.bools.AbstractBooleanLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.strings.AbstractStringLattice;
import it.lucaneg.oo.ast.types.BooleanType;
import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractEnvironment;

public class ValueEnvironment extends AbstractEnvironment<ValueLattice, ValueEnvironment> {

	private final AbstractBooleanLattice<?> booleanSingleton;

	private final AbstractIntegerLattice<?> intSingleton;

	private final AbstractStringLattice<?> stringSingleton;
	
	public ValueEnvironment(AbstractBooleanLattice<?> booleanSingleton, AbstractIntegerLattice<?> intSingleton,
			AbstractStringLattice<?> stringSingleton) {
		this.booleanSingleton = booleanSingleton;
		this.intSingleton = intSingleton;
		this.stringSingleton = stringSingleton;
	}

	private ValueEnvironment(ValueEnvironment other) {
		super(other);
		this.booleanSingleton = other.booleanSingleton;
		this.intSingleton = other.intSingleton;
		this.stringSingleton = other.stringSingleton;
	}
	
	@Override
	public ValueEnvironment copy() {
		return new ValueEnvironment(this);
	}
	
	@Override
	public ValueLattice defaultLatticeForType(Type t) {
		if (t == IntType.INSTANCE)
			return new ValueLattice(intSingleton.top());
		
		if (t == BooleanType.INSTANCE)
			return new ValueLattice(booleanSingleton.top());
		
		if (t == Type.getStringType())
			return new ValueLattice(stringSingleton.top());
		
		return ValueLattice.getBottom();
	}

	public static boolean canProcessType(Type t) {
		return t == IntType.INSTANCE || t == BooleanType.INSTANCE || t == Type.getStringType();
	}
}
