package it.lucaneg.oo.ast.types;

public class VoidType extends Type {

	public final static VoidType INSTANCE = new VoidType();

	private VoidType() {}

	@Override
	public String toString() {
		return "void";
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		return false;
	}

	@Override
	public boolean canBeAssignedToSpecial(Type other) {
		return this == other;
	}
}