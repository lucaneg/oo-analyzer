package it.lucaneg.oo.ast.types;

public class IntType extends PrimitiveType {

	public final static IntType INSTANCE = new IntType();

	private IntType() {}

	@Override
	public String toString() {
		return "int";
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		// an int can only be assigned to an int or to a float
		return other == this || other == FloatType.INSTANCE;
	}
}