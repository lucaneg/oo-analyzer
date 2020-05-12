package it.lucaneg.oo.parser.typecheck;

import java.util.HashMap;
import java.util.Map;

import it.lucaneg.oo.ast.ClassDefinition;
import it.lucaneg.oo.ast.types.ClassType;
import it.lucaneg.oo.ast.types.Type;
import lombok.Getter;

public class CheckerHelper {

	@Getter
	private final Type returnType;

	private final Map<String, Type> env;
	
	private final TypeChecker root;

	private CheckerHelper(TypeChecker root, Type returnType, Map<String, Type> env) {
		this.root = root;
		this.returnType = returnType;
		this.env = env;
	}

	public CheckerHelper(TypeChecker root, Type returnType) {
		this(root, returnType, new HashMap<>());
	}

	public CheckerHelper putVar(String var, Type type) {
		Map<String, Type> copy = new HashMap<>(env);
		copy.put(var, type);
		return new CheckerHelper(root, returnType, copy);
	}

	public Type getVar(String var) {
		return env.get(var);
	}
	
	public ClassDefinition typeToDefinition(ClassType type) {
		return root.getClasses().get(type.getName());
	}
}