package it.lucaneg.oo.sdk.analyzer.program;

import it.lucaneg.oo.ast.types.Type;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A field defined in a class that has been submitted to the analysis.
 * 
 * @author Luca Negrini
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class MField extends MClassMember {

	/**
	 * The name of the field
	 */
	private final String name;

	/**
	 * The type of the field
	 */
	private final Type type;

	/**
	 * Builds the field.
	 * 
	 * @param clazz the class that defines the field
	 * @param line  the line where the field is defined
	 * @param name  the name of the field
	 * @param type  the type of the field
	 */
	public MField(MClass clazz, int line, String name, Type type) {
		super(clazz, line);

		this.name = name;
		this.type = type;
	}

	@Override
	public String toString() {
		return type + " " + getDefiningClass().getName() + "::" + name;
	}
}
