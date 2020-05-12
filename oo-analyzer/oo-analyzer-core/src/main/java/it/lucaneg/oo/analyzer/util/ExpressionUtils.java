package it.lucaneg.oo.analyzer.util;

import java.util.Collection;
import java.util.HashSet;

import it.lucaneg.oo.ast.expression.BinaryExpression;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.expression.ExpressionConvertible;
import it.lucaneg.oo.ast.expression.Variable;
import it.lucaneg.oo.ast.expression.arithmetic.Minus;
import it.lucaneg.oo.ast.expression.creation.NewArray;
import it.lucaneg.oo.ast.expression.creation.NewObject;
import it.lucaneg.oo.ast.expression.dereference.ArrayAccess;
import it.lucaneg.oo.ast.expression.dereference.Call;
import it.lucaneg.oo.ast.expression.dereference.FieldAccess;
import it.lucaneg.oo.ast.expression.literal.Literal;
import it.lucaneg.oo.ast.expression.logical.Not;
import it.lucaneg.oo.ast.expression.typeCheck.Cast;

public class ExpressionUtils {
	public static Collection<String> getAllVariablesNames(Expression e) {
		Collection<String> result = new HashSet<>();

		if (e instanceof Not)
			result.addAll(getAllVariablesNames(((Not) e).getExpression()));
		else if (e instanceof NewObject)
			result.addAll(getAllVariablesNames(((NewObject) e).getActuals()));
		else if (e instanceof NewArray)
			result.addAll(getAllVariablesNames(((NewArray) e).getSizes()));
		else if (e instanceof Minus)
			result.addAll(getAllVariablesNames(((Minus) e).getExpression()));
		else if (e instanceof Call) {
			result.addAll(getAllVariablesNames(((Call) e).getReceiver().asExpression()));
			result.addAll(getAllVariablesNames(((Call) e).getActuals()));
		} else if (e instanceof Variable)
			result.add(((Variable) e).getName());
		else if (e instanceof FieldAccess)
			result.addAll(getAllVariablesNames(((FieldAccess) e).getReceiver().asExpression()));
		else if (e instanceof ArrayAccess) {
			result.addAll(getAllVariablesNames(((ArrayAccess) e).getArray()));
			result.addAll(getAllVariablesNames(((ArrayAccess) e).getIndexes()));
		} else if (e instanceof Cast)
			result.addAll(getAllVariablesNames(((Cast) e).getExpression()));
		else if (e instanceof BinaryExpression) {
			result.addAll(getAllVariablesNames(((BinaryExpression) e).getLeft()));
			result.addAll(getAllVariablesNames(((BinaryExpression) e).getRight()));
		} else if (e instanceof Literal) {
		} else
			throw new IllegalArgumentException("Unknown expression type: " + e.getClass().getName());

		return result;
	}

	private static <T extends ExpressionConvertible> Collection<String> getAllVariablesNames(T[] elements) { 
		Collection<String> result = new HashSet<>();
		for (T t : elements) 
			result.addAll(getAllVariablesNames(t.asExpression()));
		return result;
	}
}
