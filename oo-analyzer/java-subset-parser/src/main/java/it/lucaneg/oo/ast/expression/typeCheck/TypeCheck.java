package it.lucaneg.oo.ast.expression.typeCheck;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.BooleanType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TypeCheck extends Expression {

    private final Type type;

    private Expression expression;
    
    public TypeCheck(String source, int line, int pos, Type type, Expression expression) {
    	super(source, line, pos);
    	this.type = type;
    	this.expression = expression;
    }

	@Override
	public String toString() {
		return getExpression() + " is " + getType();
	}
	
	@Override
	protected Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		Type target = expression.computeExpressionType(helper);

    	if (target == type)
    		typeCheckError("Type checkinng " + target + " with itself is useless");
    	// type must be a subclass of fromType
    	else if (!type.canBeAssignedTo(target))
    		typeCheckError(target + " can never be a " + type);

		return BooleanType.INSTANCE;
	}

	@Override
	public Expression transformStringJoins(CheckerHelper helper) {
		expression = expression.transformStringJoins(helper);
		return this;
	}
}