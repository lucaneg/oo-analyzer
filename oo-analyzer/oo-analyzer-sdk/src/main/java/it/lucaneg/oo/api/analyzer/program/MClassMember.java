package it.lucaneg.oo.api.analyzer.program;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A member of class that has been submitted to the analysis.
 * 
 * @author Luca Negrini
 */
@EqualsAndHashCode
public abstract class MClassMember {

	/**
	 * The class that defines this member
	 */
	@EqualsAndHashCode.Exclude
	private final MClass clazz;
	
	/**
	 * The name of the class that defines this member
	 */
	private final String className;

	/**
	 * The line where this member is defined
	 */
	@Getter
	private final int line;

	protected MClassMember(MClass clazz, int line) {
		this.clazz = clazz;
		this.line = line;
		this.className = clazz.getName();
	}

	/**
	 * Yields the class where this member is defined.
	 * 
	 * @return the defining class
	 */
	public MClass getDefiningClass() {
		return clazz;
	}

	/**
	 * Yields the name of the file where the class that defines this member is
	 * defined.
	 * 
	 * @return the file name
	 */
	public String getFileName() {
		return clazz.getFileName();
	}
}
