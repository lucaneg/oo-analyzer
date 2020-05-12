package it.lucaneg.oo.analyzer.checks.assertion;

import static it.lucaneg.oo.sdk.analyzer.analyses.ExpressionSatisfiabilityEvaluator.Satisfiability.NOT_SATISFIED;
import static it.lucaneg.oo.sdk.analyzer.analyses.ExpressionSatisfiabilityEvaluator.Satisfiability.UNKNOWN;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.sdk.analyzer.analyses.Analysis;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionSatisfiabilityEvaluator;
import it.lucaneg.oo.sdk.analyzer.analyses.Lattice;
import it.lucaneg.oo.sdk.analyzer.analyses.ExpressionSatisfiabilityEvaluator.Satisfiability;
import it.lucaneg.oo.sdk.analyzer.checks.impl.AbstractCheck;
import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.Program;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Assert;

public class AssertionCheck extends AbstractCheck {
	
	private static final EnrichedLogger logger = new EnrichedLogger(AssertionCheck.class);
	
	@Override
	public void run(Program program, Collection<Analysis<?, ?>> analyses) {
		for (MCodeMember codeMember : logger.mkIterationLogger(getName(), "methods").iterate(program.getAllCodeMembers())) {
			codeMember.getCode()
				.nodes()
				.stream()
				.filter(st -> st instanceof Assert)
				.map(st -> (Assert) st)
				.forEach(ass -> check(analyses, codeMember, ass));
		}
	}

	private void check(Collection<Analysis<?, ?>> analyses, MCodeMember codeMember, Assert ass) {
		SortedSet<String> failing = new TreeSet<>();
		SortedSet<String> possiblyFailing = new TreeSet<>();
		for (Analysis<? extends Lattice<?>, ? extends Environment<?, ?>> analysis : analyses) {
			ExpressionSatisfiabilityEvaluator eval = (ExpressionSatisfiabilityEvaluator) analysis.of(codeMember).at(ass);
			Satisfiability result = eval.satisfies(ass.getAssertion());
			if (result == NOT_SATISFIED)
				failing.add(analysis.getName());
			else if (result == UNKNOWN)
				possiblyFailing.add(analysis.getName());
		}

		if (!failing.isEmpty()) 
			registerFinding(new FailingAssertFinding(ass.getFileName(), ass.getLine(), ass.getPosition(), getName(), failing));
		
		if (!possiblyFailing.isEmpty()) 
			registerFinding(new PossiblyFailingAssertFinding(ass.getFileName(), ass.getLine(), ass.getPosition(), getName(), possiblyFailing));
	}
}
