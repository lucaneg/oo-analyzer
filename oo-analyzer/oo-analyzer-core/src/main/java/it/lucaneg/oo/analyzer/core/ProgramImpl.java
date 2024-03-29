package it.lucaneg.oo.analyzer.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.lucaneg.oo.sdk.analyzer.program.MClass;
import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.Program;

public class ProgramImpl implements Program {

	private final Collection<MClass> classes = new HashSet<>();
	
	public void addClass(MClass clazz) {
		classes.add(clazz);
	}

	@Override
	public final Collection<MCodeMember> getAllCodeMembers() {
		return classes.stream().flatMap(c -> Stream.concat(c.getConstructors().stream(), c.getMethods().stream())).collect(Collectors.toList());
	}

	@Override
	public final Collection<MCodeMember> getAllSubmittedCodeMembers() {
		return classes.stream()
				.flatMap(c -> Stream.concat(c.getConstructors().stream(), c.getMethods().stream()))
				.filter(code -> !code.getDefiningClass().isObject() && !code.getDefiningClass().isString())
				.collect(Collectors.toList());
	}
}
