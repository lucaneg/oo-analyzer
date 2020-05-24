package it.lucaneg.oo.parser.typecheck;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.ast.ClassDefinition;
import it.lucaneg.oo.ast.CodeDefinition;
import it.lucaneg.oo.ast.ConstructorDefinition;
import it.lucaneg.oo.ast.FieldDefinition;
import it.lucaneg.oo.ast.FormalDefinition;
import it.lucaneg.oo.ast.MethodDefinition;
import it.lucaneg.oo.ast.types.ClassType;
import it.lucaneg.oo.ast.types.VoidType;
import lombok.Getter;

public class TypeChecker {

	private static final EnrichedLogger logger = new EnrichedLogger(TypeChecker.class);

	@Getter
	private final Map<String, ClassDefinition> classes;

	public TypeChecker(Collection<ClassDefinition> classes) {
		this.classes = classes.stream().collect(Collectors.toMap(c -> c.getName(), c -> c));
	}

	public void typeCheck() throws TypeCheckException {
		// first step: create a type hierarchy
		TypeCheckException ex = logger.mkTimerLogger("Building tipe hierarchy").execSupplier(() -> buildTypeHierarchy());
		if (ex != null)
			throw ex;

		// check ambiguity: no colliding fields/methods in the same class, no classes
		// with the same name
		ex = logger.mkTimerLogger("Checking fields and method ambiguity").execSupplier(() -> ambiguity());
		if (ex != null)
			throw ex;
		
		// last step: validate code
		ex = logger.mkTimerLogger("Type checking code").execSupplier(() -> code());
		if (ex != null)
			throw ex;
	}

	private TypeCheckException buildTypeHierarchy() {
		for (ClassDefinition clazz : classes.values())
			// ensure each class has its own type
			ClassType.mk(clazz.getName());

		if (ClassType.getObjectType() == null)
			return new TypeCheckException("Object class is missing");
		
		for (ClassType type : ClassType.all())
			// ensure each type has its own class
			if (!classes.containsKey(type.getName()))
				return new TypeCheckException(type.getName() + " is used as type but has not been provided");

		for (ClassDefinition clazz : classes.values()) {
			ClassType thisclass = ClassType.get(clazz.getName());
			ClassType superclass = ClassType.get(clazz.getSuperclassName());
			if (superclass == null && thisclass != ClassType.getObjectType())
				return new TypeCheckException(clazz.getSuperclassName() + " is missing (superclass of " + thisclass.getName() + ")");

			thisclass.setSuperclass(superclass);
			clazz.setSuperclass(classes.get(clazz.getSuperclassName()));
		}

		return null;
	}

	private TypeCheckException ambiguity() {
		Collection<String> duplicates = duplicates(classes.values(), ClassDefinition::getName); 
		if (!duplicates.isEmpty())
			return new TypeCheckException("There are classes with the same name: " + duplicates);
		
		for (ClassDefinition clazz : classes.values()) {
			duplicates = duplicates(clazz.getFields(), FieldDefinition::getName);
			if (!duplicates.isEmpty())
				return new TypeCheckException("There are fields with the same name in class " + clazz.getName() + ": " + duplicates);
			
			duplicates = duplicates(clazz.getMethods(), CodeDefinition::codeSignatureForResolution);
			if (!duplicates.isEmpty())
				return new TypeCheckException("There are methods with the same signature in class " + clazz.getName() + ": " + duplicates);

			duplicates = duplicates(clazz.getConstructors(), CodeDefinition::codeSignatureForResolution);
			if (!duplicates.isEmpty())
				return new TypeCheckException("There are constructors with the same signature in class " + clazz.getName() + ": " + duplicates);
		}

		return null;
	}

	private <T> Collection<String> duplicates(Collection<T> collection, Function<T, String> selector) {
		Set<String> duplicates = new LinkedHashSet<>();
		Set<String> uniques = new HashSet<>();

		for (T t : collection) {
			String key = selector.apply(t);
			if (!uniques.add(key))
				duplicates.add(key);
		}
		
		return duplicates;
	}
	
	private TypeCheckException code() {
		TypeCheckException ret;
		for (ClassDefinition clazz : classes.values()) {
			for (MethodDefinition method : clazz.getMethods())
				if ((ret = validateMethod(method, clazz)) != null)
					return ret;
			
			for (ConstructorDefinition constructor : clazz.getConstructors())
				if ((ret = validateCode(constructor, clazz)) != null)
					return ret; 
		}
		
		return null;
	}
	
	private TypeCheckException validateMethod(MethodDefinition code, ClassDefinition clazz) {
		TypeCheckException ret;
		if ((ret = validateCode(code, clazz)) != null)
			return ret;
		
		String errorPrefix = clazz.getName() + "::" + code.codeSignature() + ": ";
		ClassDefinition superclass = classes.get(clazz.getSuperclassName());
		if (superclass != null) {
			MethodDefinition overridden = superclass.methodLookup(code.getName(), code.getFormalsTypes());

			if (overridden != null)
				// it does override a method of a superclass. We check
				// that its return type has been refined. We use the
				// canBeAssignedToSpecial method so that
				// void can be overridden into void
				if (!code.getReturnType().canBeAssignedToSpecial(overridden.getReturnType()))
					return new TypeCheckException(errorPrefix + "illegal return type for overriding method. Was " + overridden.getReturnType());
		}

		// we check that there is no dead-code in the body of the method
		boolean stopping;
		try {
			stopping = code.getCode().allPathsEndWithReturn();
		} catch (TypeCheckException e) {
			// this should happen in the validateCall() method, thus it is not really needed
			return new TypeCheckException(errorPrefix + "type check failed", e);
		}
		if (code.getReturnType() != VoidType.INSTANCE && !stopping)
			return new TypeCheckException(errorPrefix + "not all paths return a value");
		
		return null;
	}
	
	private TypeCheckException validateCode(CodeDefinition code, ClassDefinition clazz) {
		String errorPrefix = clazz.getName() + "::" + code.codeSignature() + ": ";
		CheckerHelper helper = new CheckerHelper(this, code.getReturnType());

		// the main method is the only static method, where there is no this variable
//		if (code instanceof MethodDefinition && !((MethodDefinition) code).isMain())
//			helper = helper.putVar("this", ClassType.get(clazz.getName())); TODO

		// we enrich the type-checker with the formal parameters
		if (code.getFormals() != null)
			for (FormalDefinition def : code.getFormals())
				helper = helper.putVar(def.getName(), def.getType());

		try {
			// we type-check the body of the method in the resulting type-checker
			code.getCode().typeCheck(helper);
			// we check that there is no dead-code in the body of the method
			code.getCode().allPathsEndWithReturn();
			// we transform additions of string elements into string join expressions
			code.getCode().transformStringJoins(helper);
		} catch (TypeCheckException e) {
			return new TypeCheckException(errorPrefix + "type check failed", e);
		}
		
		return null;
	}
}
