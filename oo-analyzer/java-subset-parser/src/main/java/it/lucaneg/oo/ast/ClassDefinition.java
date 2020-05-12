package it.lucaneg.oo.ast;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import it.lucaneg.oo.ast.types.Type;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ClassDefinition extends SyntaxNode {
	private final String name;

	private final String superclassName;
	
	@Setter
	private ClassDefinition superclass;

	private final Collection<FieldDefinition> fields;

	private final Collection<MethodDefinition> methods;

	private final Collection<ConstructorDefinition> constructors;

	public ClassDefinition(String source, int line, int pos, String name, String superclassName) {
		super(source, line, pos);

		this.name = name;
		this.superclassName = superclassName == null && !name.equals("Object") ? "Object" : superclassName;

		this.fields = new HashSet<>();
		this.methods = new HashSet<>();
		this.constructors = new HashSet<>();
	}

	public boolean addField(FieldDefinition e) {
		return fields.add(e);
	}

	public boolean addMethod(MethodDefinition e) {
		return methods.add(e);
	}

	public boolean addConstructor(ConstructorDefinition e) {
		return constructors.add(e);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("class ").append(getName()); 
		if (getSuperclassName() != null)
			sb.append(" extends ").append(getSuperclassName());
		
		sb.append(" fields:").append(fields).append(", constructors:").append(constructors).append(", methods:").append(methods);
		
		return sb.toString();
	}
	
	public String fullDump() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("class ").append(getName()); 
		if (getSuperclassName() != null)
			sb.append(" extends ").append(getSuperclassName());
		
		sb.append("\n");
		
		for (FieldDefinition field : fields)
			sb.append("\t").append(field).append("\n");
		
		for (ConstructorDefinition ctor : constructors)
			sb.append("\t").append(ctor).append("\n").append(ctor.codeDump("\t\t")).append("\n");
		
		for (MethodDefinition method : methods)
			sb.append("\t").append(method).append("\n").append(method.codeDump("\t\t")).append("\n");
		
		return sb.toString();
	}
	
	public final MethodDefinition methodLookup(String name, Type[] formals) {
		// we check all methods in this signature having the given name
		Optional<MethodDefinition> result = methods.stream().filter(m -> m.getName().equals(name) && Arrays.equals(formals, m.getFormalsTypes())).findAny();
		if (result.isPresent())
			return result.get();

		// otherwise, we look up in the superclass, if any
		return superclass == null ? null : superclass.methodLookup(name, formals);
	}
	
	public final Set<MethodDefinition> methodsLookup(String name, Type[] formals) {
		// the set of candidates is initially the set of all methods
		// called name and defined in this class
		Set<MethodDefinition> candidates = methods.stream().filter(m -> m.getName().equals(name)).collect(Collectors.toSet());
		if (candidates == null)
			candidates = new HashSet<>();

		if (superclass != null) {
			Set<MethodDefinition> superCandidates = superclass.methodsLookup(name, formals);
			Set<MethodDefinition> toBeRemoved = new HashSet<>();

			Type[] sigFormals, sig2Formals;
			for (MethodDefinition sig : superCandidates) {
				sigFormals = sig.getFormalsTypes();

				for (MethodDefinition sig2 : candidates) {
					sig2Formals = sig2.getFormalsTypes();

					if (Arrays.equals(sigFormals, sig2Formals))
						toBeRemoved.add(sig);
				}
			}

			superCandidates.removeAll(toBeRemoved);
			candidates.addAll(superCandidates);
		}

		return mostSpecific(candidates, formals);
	}
	
	public ConstructorDefinition constructorLookup(Type[] formals) {
		Optional<ConstructorDefinition> result = constructors.stream().filter(c -> Arrays.equals(formals, c.getFormalsTypes())).findAny();
		if (result.isPresent())
			return result.get();
		return null;
	}
	
	public final Set<ConstructorDefinition> constructorsLookup(Type[] formals) {
		// we return the most specific constructors amongst those available
		// for this class and whose formal parameters are compatible with
		// formals
		return mostSpecific(constructors, formals);
	}
	
	public final FieldDefinition fieldLookup(String name) {
		Optional<FieldDefinition> result = fields.stream().filter(f -> f.getName().equals(name)).findAny();
		if (result.isPresent())
			return result.get();
		return superclass == null ? null : superclass.fieldLookup(name);
	}
	
	private static <T extends CodeDefinition> Set<T> mostSpecific(Collection<T> sigs, Type[] formals) {
		Set<T> result = new HashSet<>();
		Set<T> toBeRemoved = new HashSet<>();

		for (T sig : sigs)
			if (canBeAssignedTo(formals, sig.getFormalsTypes()))
				result.add(sig);

		// we remove a candidate if it is less general than another
		for (T sig : result)
			for (T sig2 : result)
				if (sig != sig2 && canBeAssignedTo(sig.getFormalsTypes(), sig2.getFormalsTypes()))
					toBeRemoved.add(sig2);

		result.removeAll(toBeRemoved);

		return result;
	}
	
	private static boolean canBeAssignedTo(Type[] formals, Type[] candidate) {
		if (formals.length != candidate.length)
			return false;
		
		for (int i = 0; i < candidate.length; i++) 
			if (!formals[i].canBeAssignedTo(candidate[i]))
				return false;
		
		return true;
	}
}
