package it.lucaneg.oo.sdk.analyzer.program.instructions;

import java.util.Collection;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A return statement, that is, a statement that halts the execution of the
 * current method and, optionally, returns some value to the calling method.
 * 
 * @author Luca Negrini
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class Return extends Statement {

	/**
	 * The expression returned, might be null if the containing method returns void
	 */
	private final Expression returned;

	/**
	 * Builds the return.
	 * 
	 * @param container the method or constructor that contains this statement
	 * @param line      the line where this statement happens
	 * @param position  the position inside the line where this statement begins
	 * @param variables the local variables defined when this statement is reached
	 * @param returned  the expression returned by this method, if any
	 */
	public Return(MCodeMember container, int line, int position, Collection<MLocalVariable> variables,
			Expression returned) {
		super(container, line, position, variables);
		this.returned = returned;
	}

	@Override
	public String toStringAux() {
		if (returned != null)
			return "return " + returned;
		else
			return "return";
	}

}
