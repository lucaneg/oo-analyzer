package it.lucaneg.oo.ast.types;

public class FloatType extends PrimitiveType {

	public final static FloatType INSTANCE = new FloatType();

	private FloatType() {}

	@Override
	public String toString() {
		return "float";
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		// a float can only be assigned to another float
		return this == other;
	}
}