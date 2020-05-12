package it.lucaneg.oo.api.analyzer.program.instructions;

import java.util.Collection;

import it.lucaneg.oo.api.analyzer.program.MCodeMember;
import it.lucaneg.oo.api.analyzer.program.MLocalVariable;
import lombok.EqualsAndHashCode;

/**
 * A skip statement, that is, a statement that does nothing when it is executed.
 * 
 * @author Luca Negrini
 */
@EqualsAndHashCode(callSuper = true)
public class Skip extends Statement {

	/**
	 * Builds the skip.
	 * 
	 * @param container the method or constructor that contains this statement
	 * @param line      the line where this statement happens
	 * @param position  the position inside the line where this statement begins
	 * @param variables the local variables defined when this statement is reached
	 */
	public Skip(MCodeMember container, int line, int position, Collection<MLocalVariable> variables) {
		super(container, line, position, variables);
	}

	@Override
	public String toString() {
		return "skip";
	}
}
