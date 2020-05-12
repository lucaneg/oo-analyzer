package it.lucaneg.oo.sdk.analyzer.program;

import it.lucaneg.oo.ast.types.Type;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * A code member of a class that is part of the program to analyze, that is, a
 * constructor or method.
 * 
 * @author Luca Negrini
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class MCodeMember extends MClassMember {

	/**
	 * The name of the code member
	 */
	private final String name;

	/**
	 * The return type of the code member
	 */
	private final Type returnType;

	/**
	 * The formal parameters of the code member
	 */
	private final MFormalParameter[] parameters;

	/**
	 * The block that contains the code of the code member
	 */
	@Setter
	private MCodeBlock code;

	/**
	 * Builds the code member
	 * 
	 * @param clazz      the class that defines the code member
	 * @param line       the line where the code member is defined
	 * @param name       the name of the code member
	 * @param returnType the return type of the code member
	 * @param formals    the formal parameters of the code member
	 */
	protected MCodeMember(MClass clazz, int line, String name, Type returnType, MFormalParameter[] formals) {
		super(clazz, line);

		this.name = name;
		this.returnType = returnType;
		this.parameters = formals;
	}

	@Override
	public String toString() {
		return returnType + " " + getDefiningClass().getName() + "::" + name + "(" + parameters + ")";
	}

	/**
	 * Yields a string signature of the code member that can be used as a file name.
	 * 
	 * @return the string signature
	 */
	public abstract String toStringForFileName();
}
