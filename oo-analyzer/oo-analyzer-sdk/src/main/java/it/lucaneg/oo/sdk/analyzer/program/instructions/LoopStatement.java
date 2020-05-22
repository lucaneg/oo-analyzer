package it.lucaneg.oo.sdk.analyzer.program.instructions;

import java.util.Collection;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;
import lombok.EqualsAndHashCode;

/**
 * A loop statement, that is, a statements that is the guard of a loop.
 * 
 * @author Luca Negrini
 */
@EqualsAndHashCode(callSuper = true)
public class LoopStatement extends BranchingStatement {

	/**
	 * Builds the brancing statement.
	 * 
	 * @param container the method or constructor that contains this statement
	 * @param line      the line where this statement happens
	 * @param position  the position inside the line where this statement begins
	 * @param variables the local variables defined when this statement is reached
	 * @param condition the condition that gets evaluated
	 */
	public LoopStatement(MCodeMember container, int line, int position, Collection<MLocalVariable> variables,
			Expression condition) {
		super(container, line, position, variables, condition);
	}

	@Override
	public String toStringAux() {
		return "loop while: " + getCondition();
	}
}
