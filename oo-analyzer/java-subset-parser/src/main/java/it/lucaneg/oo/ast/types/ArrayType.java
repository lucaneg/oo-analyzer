package it.lucaneg.oo.ast.types;

import java.util.HashMap;
import java.util.Map;

public class ArrayType extends ReferenceType {

	private final static Map<Type, ArrayType> memory = new HashMap<>();

	private final Type elementsType;

	private ArrayType(Type elementsType) {
		this.elementsType = elementsType;
	}

	public static ArrayType mk(Type elementsType) {
		ArrayType result = memory.get(elementsType);
		if (result == null)
			memory.put(elementsType, result = new ArrayType(elementsType));

		return result;
	}

	public static ArrayType mk(Type elementsType, int dimensions) {
		if (dimensions == 1)
			return mk(elementsType);
		else
			return ArrayType.mk(ArrayType.mk(elementsType, dimensions - 1));
	}

	public Type getElementsType() {
		return elementsType;
	}

	@Override
	public String toString() {
		return elementsType + "[]";
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		if (other instanceof ArrayType)
			return elementsType.canBeAssignedToSpecial(((ArrayType) other).elementsType);
		else
			return other == getObjectType();
	}

	@Override
	public Type leastCommonSupertype(Type other) {
		// between array and class, the least common supertype is Object
		if (other instanceof ClassType)
			return getObjectType();
		else if (other instanceof ArrayType)
			// an array of primitive types can only be compared with itself.
			// Otherwise, the least common supertype is Object
			if (elementsType instanceof PrimitiveType)
				return this == other ? this : getObjectType();
			else {
				Type lcs = elementsType.leastCommonSupertype(((ArrayType) other).elementsType);

				return lcs == null ? getObjectType() : mk(lcs);
			}

		// the least common supertype of an array and null is the array
		if (other == NilType.INSTANCE)
			return this;
		else
			// no common supertype exists
			return null;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() ^ elementsType.hashCode();
	}
}