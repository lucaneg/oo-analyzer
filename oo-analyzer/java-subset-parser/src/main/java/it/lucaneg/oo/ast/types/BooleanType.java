package it.lucaneg.oo.ast.types;

public class BooleanType extends PrimitiveType {

	public final static BooleanType INSTANCE = new BooleanType();

	private BooleanType() {}

	@Override
	public String toString() {
		return "boolean";
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		return other == BooleanType.INSTANCE;
	}
}