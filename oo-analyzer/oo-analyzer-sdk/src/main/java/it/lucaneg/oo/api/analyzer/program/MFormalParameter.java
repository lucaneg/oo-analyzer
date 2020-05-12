package it.lucaneg.oo.api.analyzer.program;

import it.lucaneg.oo.ast.types.Type;
import lombok.Getter;
import lombok.EqualsAndHashCode;

/**
 * A formal parameter of a method or constructor.
 * 
 * @author Luca Negrini
 */
@Getter
@EqualsAndHashCode
public class MFormalParameter {
	/**
	 * The type of the parameter
	 */
	private final Type type;

	/**
	 * The name of the parameter
	 */
	private final String name;

	/**
	 * Builds the formal parameter.
	 * 
	 * @param type the type of the parameter
	 * @param name the name of the parameter
	 */
	public MFormalParameter(Type type, String name) {
		super();
		this.type = type;
		this.name = name;
	}

	@Override
	public String toString() {
		return type + " " + name;
	}
}
