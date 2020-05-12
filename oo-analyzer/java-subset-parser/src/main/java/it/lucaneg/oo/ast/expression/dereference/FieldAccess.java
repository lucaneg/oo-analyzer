package it.lucaneg.oo.ast.expression.dereference;

import it.lucaneg.oo.ast.FieldDefinition;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.statement.Assignable;
import it.lucaneg.oo.ast.types.ClassType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FieldAccess extends Expression implements Argument, Assignable {

    private final Receiver receiver;

    private final String name;
    
    public FieldAccess(String source, int line, int pos, Receiver receiver, String name) {
    	super(source, line, pos);
    	this.receiver = receiver;
    	this.name = name;
    }

	@Override
	public String toString() {
		return getReceiver() + "::" + getName();
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		Type receiverType = receiver.asExpression().computeExpressionType(helper);

    	if (!(receiverType instanceof ClassType))
    		typeCheckError("class type required");

    	ClassType receiverClass = (ClassType) receiverType;
    	FieldDefinition field;
    	if ((field = helper.typeToDefinition(receiverClass).fieldLookup(name)) == null)
    		typeCheckError("unknown field " + name);

    	return field.getType();
	}
}