package it.lucaneg.oo.api.analyzer.program;

import it.lucaneg.oo.ast.types.Type;
import lombok.EqualsAndHashCode;

/**
 * A method that is defined in a class that has been submitted to the analysis.
 * 
 * @author Luca Negrini
 */
@EqualsAndHashCode(callSuper = true)
public final class MMethod extends MCodeMember {

	/**
	 * Builds the method.
	 * 
	 * @param clazz      the class that defines the method
	 * @param line       the line where the method is defined
	 * @param name       the name of the method
	 * @param returnType the return type of the method
	 * @param formals    the formal parameters of the method
	 */
	public MMethod(MClass clazz, int line, String name, Type returnType, MFormalParameter[] formals) {
		super(clazz, line, name, returnType, formals);
	}

	@Override
	public String toStringForFileName() {
		return getDefiningClass().getName() + "___" + getName() + "(" + getParameters() + ")" + getReturnType();
	}
}
