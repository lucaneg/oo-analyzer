package it.lucaneg.oo.api.analyzer.program;

import java.util.Collection;

/**
 * A representation of the program under analysis.
 * 
 * @author Luca Negrini
 */
public interface Program {

	/**
	 * The name of the string class
	 */
	public static final String STRING_CLASS = "String";

	/**
	 * The name of the object class
	 */
	public static final String OBJECT_CLASS = "Object";

	/**
	 * Yields an unmodifiable view of all the methods and constructors that are part
	 * of the program to analyze.
	 * 
	 * @return the collection of method and constructors
	 */
	Collection<MCodeMember> getAllCodeMembers();
}
