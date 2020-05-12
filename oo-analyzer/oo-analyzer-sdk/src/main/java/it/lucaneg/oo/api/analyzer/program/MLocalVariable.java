package it.lucaneg.oo.api.analyzer.program;

import it.lucaneg.oo.ast.types.Type;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A local variable defined in a method submitted to the analysis.
 * 
 * @author Luca Negrini
 */
@Getter
@EqualsAndHashCode
public class MLocalVariable {

	/**
	 * The type of the local variable
	 */
	private final Type type;

	/**
	 * The name of the local variable
	 */
	private final String name;

	/**
	 * Builds the local variable.
	 * 
	 * @param type the type of the variable
	 * @param name the name of the variable
	 */
	public MLocalVariable(Type type, String name) {
		this.type = type;
		this.name = name;
	}

	@Override
	public String toString() {
		return name + " [" + type + "]";
	}
}
