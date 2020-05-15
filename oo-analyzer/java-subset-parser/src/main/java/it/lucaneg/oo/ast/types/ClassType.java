package it.lucaneg.oo.ast.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class ClassType extends ReferenceType {
	
	private final String name;

	private ClassType superclass;

	private ClassType(String name) {
		// we record its name
		this.name = name;

		// we record this object for future lookup
		memory.put(name, this);

		if (name.equals("Object"))
			setObjectType(this);
		else if (name.equals("String"))
			setStringType(this);
	}

	public final String getName() {
		return name;
	}
	
	public void setSuperclass(ClassType superclass) {
		this.superclass = superclass;
	}

	@Override
	public String toString() {
		return name.toString();
	}

	@Override
	public final boolean canBeAssignedTo(Type other) {
		return other instanceof ClassType && this.subclass((ClassType) other);
	}
	
	public boolean subclass(ClassType other) {
		return this == other || (superclass != null && superclass.subclass(other));
	}
	
	@Override
	public Type leastCommonSupertype(Type other) {
		// between a class type and an array type, the least common supertype is
		// Object
		if (other instanceof ArrayType)
			return getObjectType();
		else if (other instanceof ClassType) {
			// we look in our superclasses for a superclass of other
			for (ClassType cursor = this; cursor != null; cursor = cursor.superclass)
				if (other.canBeAssignedTo(cursor))
					return cursor;

			// last chance, always valid
			return getObjectType();
		}
		// the supertype of a class type and null or an unused type is the class
		// itself
		else if (other == NilType.INSTANCE)
			return this;
		// otherwise, there is no common supertype
		else
			return null;
	}

	private final static Map<String, ClassType> memory = new HashMap<>();

	public static ClassType mk(String name) {
		ClassType result;

		// we first check to see if we already built this class type
		if ((result = memory.get(name)) != null)
			return result;
		else
			return new ClassType(name);
	}
	
	public static ClassType get(String name) {
		ClassType result;

		// we first check to see if we already built this class type
		if ((result = memory.get(name)) != null)
			return result;
		else
			return null;
	}
	
	public static Collection<ClassType> all() { 
		return memory.values();
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() ^ name.hashCode();
	}
}