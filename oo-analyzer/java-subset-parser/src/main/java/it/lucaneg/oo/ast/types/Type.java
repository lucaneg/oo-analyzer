package it.lucaneg.oo.ast.types;

public abstract class Type {

	private static ClassType objectType;

	public static final ClassType getObjectType() {
		return objectType;
	}

	protected static final void setObjectType(ClassType objectType) {
		Type.objectType = objectType;
	}
	
	private static ClassType stringType;

	public static final ClassType getStringType() {
		return stringType;
	}

	protected static final void setStringType(ClassType stringType) {
		Type.stringType = stringType;
	}

	protected Type() {}

	@Override
	public abstract String toString();

	public abstract boolean canBeAssignedTo(Type other);

	public final boolean subtypeOf(Type other) {
		return canBeAssignedTo(other);
	}

	public boolean canBeAssignedToSpecial(Type other) {
		// primitive types and void should redefine this
		return canBeAssignedTo(other);
	}

	/**
	 * Computes the least common supertype of a given type and this type.
	 * That is, a common supertype which is the least amongst all possible supertypes.
	 */
	public Type leastCommonSupertype(Type other) {
		// this works fine for primitive types. Class and array types
		// will have to redefine this behaviour
		if (this.canBeAssignedTo(other))
			return other;
		else if (other.canBeAssignedTo(this))
			return this;
		else
			// there is no least common supertype
			return null;
	}
	
	@Override
	public int hashCode() {
		return getClass().getName().hashCode();
	}
}