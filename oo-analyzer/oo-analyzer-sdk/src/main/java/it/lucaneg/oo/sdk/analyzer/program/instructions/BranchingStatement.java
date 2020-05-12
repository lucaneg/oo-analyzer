package it.lucaneg.oo.sdk.analyzer.program.instructions;

import java.util.Collection;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A branching statement, that is, a statements that evaluates a condition and
 * then decides which execution path to take.
 * 
 * @author Luca Negrini
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class BranchingStatement extends Statement {

	/**
	 * The condition of the branching statement
	 */
	private final Expression condition;

	/**
	 * Builds the brancing statement.
	 * 
	 * @param container the method or constructor that contains this statement
	 * @param line      the line where this statement happens
	 * @param position  the position inside the line where this statement begins
	 * @param variables the local variables defined when this statement is reached
	 * @param condition the condition that gets evaluated
	 */
	public BranchingStatement(MCodeMember container, int line, int position, Collection<MLocalVariable> variables,
			Expression condition) {
		super(container, line, position, variables);
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "branch if: " + condition;
	}
}
