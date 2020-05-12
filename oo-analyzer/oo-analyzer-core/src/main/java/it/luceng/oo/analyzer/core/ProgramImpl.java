package it.luceng.oo.analyzer.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.lucaneg.oo.api.analyzer.program.MClass;
import it.lucaneg.oo.api.analyzer.program.MCodeMember;
import it.lucaneg.oo.api.analyzer.program.Program;

public class ProgramImpl implements Program {

	private final Collection<MClass> classes = new HashSet<>();
	
	public void addClass(MClass clazz) {
		classes.add(clazz);
	}

	@Override
	public Collection<MCodeMember> getAllCodeMembers() {
		return classes.stream().flatMap(c -> Stream.concat(c.getConstructors().stream(), c.getMethods().stream())).collect(Collectors.toList());
	}

}
