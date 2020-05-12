package it.lucaneg.oo.sdk.analyzer.program;

import it.lucaneg.oo.ast.types.VoidType;
import lombok.EqualsAndHashCode;

/**
 * A constructor that is defined in a class that has been submitted to the analysis.
 * 
 * @author Luca Negrini
 */
@EqualsAndHashCode(callSuper = true)
public class MConstructor extends MCodeMember {

	/**
	 * Builds the constructor.
	 * 
	 * @param clazz      the class that defines the method
	 * @param line       the line where the method is defined
	 * @param formals    the formal parameters of the method
	 */
	public MConstructor(MClass clazz, int line, MFormalParameter[] formals) {
		super(clazz, line, "<constructor>", VoidType.INSTANCE, formals);
	}
	
	@Override
	public String toStringForFileName() {
		return getDefiningClass().getName() + "___constructor(" + getParameters() + ")";
	}
}
