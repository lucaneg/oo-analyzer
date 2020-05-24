package it.lucaneg.oo.ast.expression.typeCheck;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Cast extends Expression {

    private final Type type;

    private Expression expression;
    
    public Cast(String source, int line, int pos, Type type, Expression expression) {
    	super(source, line, pos);
    	this.type = type;
    	this.expression = expression;
    }

	@Override
	public String toString() {
		return getExpression() + " as " + getType();
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		Type fromType = expression.computeExpressionType(helper);

    	if (fromType == type)
    		typeCheckError("You do not need to cast a " + fromType + " into itself");
    	// type must be a subclass of fromType
    	else if (!type.canBeAssignedTo(fromType))
    		typeCheckError(fromType + " cannot be cast into " + type);

    	return type;
	}

	@Override
	public Expression transformStringJoins(CheckerHelper helper) {
		expression = expression.transformStringJoins(helper);
		return this;
	}
}