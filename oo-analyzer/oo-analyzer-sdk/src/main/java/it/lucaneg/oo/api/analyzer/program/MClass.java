package it.lucaneg.oo.api.analyzer.program;

import java.util.Collection;
import java.util.HashSet;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A class that has been submitted to the analysis.
 * 
 * @author Luca Negrini
 */
@Getter
@EqualsAndHashCode
public class MClass {

	/**
	 * The name of the class
	 */
	private final String name;

	/**
	 * The name of the file where the class is defined
	 */
	private final String fileName;

	/**
	 * The fields defined by this class
	 */
	private final Collection<MField> fields;

	/**
	 * The constructors defined by this class
	 */
	private final Collection<MConstructor> constructors;

	/**
	 * The methods defined by this class
	 */
	private final Collection<MMethod> methods;

	/**
	 * The superclass of this class
	 */
	@EqualsAndHashCode.Exclude
	private final MClass superclass;

	/**
	 * The instances of this class (including the class itself)
	 */
	@EqualsAndHashCode.Exclude
	private final Collection<MClass> instances;

	/**
	 * Builds the class.
	 * 
	 * @param name       the name of the class
	 * @param fileName   the name of the file where the class is defined
	 * @param superclass the superclass of the class
	 */
	public MClass(String name, String fileName, MClass superclass) {
		this.name = name;
		this.fileName = fileName;
		this.superclass = superclass;
		this.fields = new HashSet<>();
		this.constructors = new HashSet<>();
		this.methods = new HashSet<>();
		this.instances = new HashSet<>();

		addAsInstance(this);
	}

	/**
	 * Registers the given class as an instance of this class
	 * 
	 * @param other the class to register
	 */
	public void addAsInstance(MClass other) {
		instances.add(other);
	}

	/**
	 * Adds a field to this class
	 * 
	 * @param field the field to add
	 */
	public void addField(MField field) {
		fields.add(field);
	}

	/**
	 * Adds a constructor to this class
	 * 
	 * @param constructor the constructor to add
	 */
	public void addConstructor(MConstructor constructor) {
		constructors.add(constructor);
	}

	/**
	 * Adds a method to this class
	 * 
	 * @return the method to add
	 */
	public void addMethod(MMethod method) {
		methods.add(method);
	}

	@Override
	public String toString() {
		return name + (superclass != null ? " extends " + superclass.getName() : "") + " [" + fields.size()
				+ " fields, " + constructors.size() + " constructors, " + methods.size() + " methods, "
				+ instances.size() + " instances]";
	}

	/**
	 * Yields true if and only of this class is the object class
	 * 
	 * @return true only if that condition holds
	 */
	public boolean isObject() {
		return getName().equals(Program.OBJECT_CLASS);
	}
	
	/**
	 * Yields true if and only of this class is the object class
	 * 
	 * @return true only if that condition holds
	 */
	public boolean isString() {
		return getName().equals(Program.STRING_CLASS);
	}
}
