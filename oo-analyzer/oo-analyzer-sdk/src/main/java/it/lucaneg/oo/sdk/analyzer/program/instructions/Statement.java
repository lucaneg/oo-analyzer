package it.lucaneg.oo.sdk.analyzer.program.instructions;

import java.util.Collection;

import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class Statement {

	/**
	 * The line where this statement happens
	 */
	private final int line;

	/**
	 * The position inside the line where this statement begins
	 */
	private final int position;

	/**
	 * The method or constructor that contains this statement
	 */
	private final MCodeMember container;

	/**
	 * The local variables defined when this statement is reached
	 */
	private final Collection<MLocalVariable> variables;

	/**
	 * Builds the statement.
	 * 
	 * @param container the method or constructor that contains this statement
	 * @param line      the line where this statement happens
	 * @param position  the position inside the line where this statement begins
	 * @param variables the local variables defined when this statement is reached
	 */
	protected Statement(MCodeMember container, int line, int position, Collection<MLocalVariable> variables) {
		this.line = line;
		this.position = position;
		this.container = container;
		this.variables = variables;
	}

	/**
	 * Yields the name of the file where the class that contains this statement is
	 * defined.
	 * 
	 * @return the file name
	 */
	public String getFileName() {
		return container.getFileName();
	}

	@Override
	public abstract String toString();
}
