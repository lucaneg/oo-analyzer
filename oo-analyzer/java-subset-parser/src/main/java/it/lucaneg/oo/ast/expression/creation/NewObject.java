package it.lucaneg.oo.ast.expression.creation;

import java.util.Arrays;
import java.util.Set;

import it.lucaneg.oo.ast.ConstructorDefinition;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.expression.dereference.Argument;
import it.lucaneg.oo.ast.types.ClassType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class NewObject extends Expression {

	private final String className;

	private final Argument[] actuals;
	
	public NewObject(String source, int line, int pos, String className, Argument[] actuals) {
		super(source, line, pos);
		this.className = className;
		this.actuals = actuals;
	}

	@Override
	public String toString() {
		return "new " + getClassName() + Arrays.toString(getActuals()).replace('[', '(').replace(']', ')');
	}
	
	@Override
	public Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException {
		ClassType target = ClassType.get(className);

		Type[] actualsTypes = new Type[actuals.length];
		for (int i = 0; i < actualsTypes.length; i++) 
			actualsTypes[i] = actuals[i].asExpression().computeExpressionType(helper);

		// we collect the set of constructors which are compatible
		// with the static types of the parameters, and have no other
		// compatible constructor which is more specific than them
		Set<ConstructorDefinition> constructors = helper.typeToDefinition(target).constructorsLookup(actualsTypes);
		
		if (constructors.isEmpty())
			typeCheckError("no matching constructor for \"" + className + "\"");
		else if (constructors.size() >= 2)
			typeCheckError("call to constructor of \"" + className + "\" is ambiguous");

		return target;
	}
}