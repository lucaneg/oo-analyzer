package it.lucaneg.oo.sdk.analyzer.program.instructions;

import java.util.Collection;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * An assert statement that halts the execution if the given expression
 * evaluates to false.
 * 
 * @author Luca Negrini
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class Assert extends Statement {

	/**
	 * The expression that gets evaluated
	 */
	private final Expression assertion;

	/**
	 * Builds the assert.
	 * 
	 * @param container the method or constructor that contains this statement
	 * @param line      the line where this statement happens
	 * @param position  the position inside the line where this statement begins
	 * @param variables the local variables defined when this statement is reached
	 * @param assertion the expression that gets evaluated
	 */
	public Assert(MCodeMember container, int line, int position, Collection<MLocalVariable> variables,
			Expression assertion) {
		super(container, line, position, variables);
		this.assertion = assertion;
	}

	@Override
	public String toString() {
		return "assert " + assertion;
	}
}
