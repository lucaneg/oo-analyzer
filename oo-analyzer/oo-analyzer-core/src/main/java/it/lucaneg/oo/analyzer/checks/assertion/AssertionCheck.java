package it.lucaneg.oo.analyzer.checks.assertion;

import static it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability.NOT_SATISFIED;
import static it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability.UNKNOWN;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.analyzer.analyses.value.ValueAnalysis;
import it.lucaneg.oo.analyzer.analyses.value.ValueEnvironment;
import it.lucaneg.oo.sdk.analyzer.analyses.Analysis;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability;
import it.lucaneg.oo.sdk.analyzer.checks.impl.AbstractCheck;
import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.Program;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Assert;

public class AssertionCheck extends AbstractCheck {
	
	private static final EnrichedLogger logger = new EnrichedLogger(AssertionCheck.class);
	
	private ValueAnalysis values;
	
	@Override
	public void run(Program program, Map<Class<? extends Analysis<?, ?>>, Analysis<?, ?>> analyses) {
		values = (ValueAnalysis) analyses.get(ValueAnalysis.class);
		for (MCodeMember codeMember : logger.mkIterationLogger(getName(), "methods").iterate(program.getAllSubmittedCodeMembers())) {
			codeMember.getCode()
				.nodes()
				.stream()
				.filter(st -> st instanceof Assert)
				.map(st -> (Assert) st)
				.forEach(ass -> check(codeMember, ass));
		}
	}

	private void check(MCodeMember codeMember, Assert ass) {
		SortedSet<String> failing = new TreeSet<>();
		SortedSet<String> possiblyFailing = new TreeSet<>();
		if (!values.of(codeMember).hasEnvironmentFor(ass))
			// unreachable
			return;
		ValueEnvironment eval = values.of(codeMember).lubAt(ass);
		Satisfiability result = values.getSatisfiabilityEvaluator().satisfies(ass.getAssertion(), eval, values.getExpressionEvaluator());
		if (result == NOT_SATISFIED)
			failing.add(values.getName());
		else if (result == UNKNOWN)
			possiblyFailing.add(values.getName());

		if (!failing.isEmpty()) 
			registerFinding(new FailingAssertFinding(ass.getFileName(), ass.getLine(), ass.getPosition(), getName(), failing));
		
		if (!possiblyFailing.isEmpty()) 
			registerFinding(new PossiblyFailingAssertFinding(ass.getFileName(), ass.getLine(), ass.getPosition(), getName(), possiblyFailing));
	}
}
