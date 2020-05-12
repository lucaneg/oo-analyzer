package it.lucaneg.oo.sdk.analyzer.program.instructions;

import java.util.Collection;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.expression.dereference.ArrayAccess;
import it.lucaneg.oo.ast.expression.dereference.Index;
import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A statement that writes something into an array element.
 * 
 * @author Luca Negrini
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class ArrayStore extends Assignment {

	/**
	 * The receiver of the array store
	 */
	private final ArrayAccess arraySpot;

	/**
	 * Builds the array store.
	 * 
	 * @param container  the method or constructor that contains this statement
	 * @param line       the line where this statement happens
	 * @param position   the position inside the line where this statement begins
	 * @param variables  the local variables defined when this statement is reached
	 * @param arraySpot  the location where the value gets written
	 * @param expression the expression that gets written
	 */
	public ArrayStore(MCodeMember container, int line, int position, Collection<MLocalVariable> variables,
			ArrayAccess arraySpot, Expression expression) {
		super(container, line, position, variables, expression);
		this.arraySpot = arraySpot;
	}

	@Override
	public String toString() {
		String result = arraySpot.getArray().toString();
		for (Index ind : arraySpot.getIndexes())
			result += "[" + ind.toString() + "]";
		return result + " = " + getExpression().toString();
	}
}
