package it.lucaneg.oo.ast.types;

public class NilType extends PrimitiveType {

	public final static NilType INSTANCE = new NilType();

    private NilType() {}

    @Override
    public String toString() {
    	return "nil";
    }

    @Override
    public boolean canBeAssignedTo(Type other) {
    	return other == this || other instanceof ReferenceType;
    }

    @Override
    public Type leastCommonSupertype(Type other) {
    	if (other == this || other instanceof ReferenceType)
    		return other;
    	else
    		return null;
    }
}