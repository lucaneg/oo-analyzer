package it.lucaneg.oo.ast.expression.dereference;

import java.util.Arrays;
import java.util.Set;

import it.lucaneg.oo.ast.MethodDefinition;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.ClassType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Call extends Expression {

	private final Receiver receiver;

	private final String name;

	private final Argument[] actuals;
	
	public Call(String source, int line, int pos, Receiver receiver, String name, Argument[] actuals) {
		super(source, line, pos);
		this.receiver = receiver;
		this.name = name;
		this.actuals = actuals;
	}

	@Override
	public String toString() {
		return getReceiver() + "." + getName() + Arrays.toString(getActuals()).replace('[', '(').replace(']', ')');
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		Type receiverType = receiver.asExpression().computeExpressionType(helper);
		Type[] actualsTypes = new Type[actuals.length];
		for (int i = 0; i < actualsTypes.length; i++) 
			actualsTypes[i] = actuals[i].asExpression().computeExpressionType(helper);

		if (!(receiverType instanceof ClassType))
			typeCheckError("class type required");

		// we collect the set of methods which are compatible with the
		// static types of the parameters, and have no other compatible method
		// that is more specific than them
		Set<MethodDefinition> methods = helper.typeToDefinition((ClassType) receiverType).methodsLookup(name, actualsTypes);

		if (methods.isEmpty())
			// there is no matching method!
			typeCheckError("no matching method for call to \"" + name + "\"");
		else if (methods.size() >= 2)
			// more than two matching methods, and none of them is
			// more specific of the other? Ambiguous call
			typeCheckError("call to method \"" + name + "\" is ambiguous");

		return ((MethodDefinition) methods.iterator().next()).getReturnType();
	}
}