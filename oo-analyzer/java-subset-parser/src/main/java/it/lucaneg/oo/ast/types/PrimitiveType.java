package it.lucaneg.oo.ast.types;

public abstract class PrimitiveType extends Type {

	protected PrimitiveType() {}

	@Override
	public boolean canBeAssignedToSpecial(Type other) {
		return this == other;
	}

	public boolean isOpen() {
		return false;
	}
}