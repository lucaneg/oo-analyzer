package it.lucaneg.oo.ast.statement;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Assignment extends Statement {

    private final Assignable target;

    private Expression expression;

	public Assignment(String source, int line, int pos, Assignable target, Expression expression) {
		super(source, line, pos);
		this.target = target;
		this.expression = expression;
	}

	@Override
	public String toString() {
		return target + " = " + expression;
	}
	
    @Override
	public CheckerHelper typeCheck(CheckerHelper helper) throws TypeCheckException {
    	Type left = target.asExpression().computeExpressionType(helper);
    	Type right = expression.computeExpressionType(helper);

    	if (!right.canBeAssignedTo(left))
    		typeCheckError(right + " cannot be assigned to " + left);

    	return helper;
    }

	@Override
	public boolean allPathsEndWithReturn() {
		return false;
	}
	
	@Override
	protected CheckerHelper transformStringJoins(CheckerHelper helper) {
		expression = expression.transformStringJoins(helper);
		return helper;
	}
}