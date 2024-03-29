package it.lucaneg.oo.sdk.analyzer.analyses.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.sdk.analyzer.analyses.Analysis;
import it.lucaneg.oo.sdk.analyzer.analyses.Denotation;
import it.lucaneg.oo.sdk.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;
import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.MFormalParameter;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;
import it.lucaneg.oo.sdk.analyzer.program.Program;

public abstract class AbstractAnalysis<L extends AbstractValue<L>, E extends AbstractEnvironment<L, E>> implements Analysis<L, E> {

	private static final EnrichedLogger logger = new EnrichedLogger(AbstractAnalysis.class);

	@Override
	public final String getName() {
		String simpleName = getClass().getSimpleName();
		if (simpleName.endsWith("Analysis"))
			simpleName = simpleName.substring(0, simpleName.length() - "Analysis".length());
		return simpleName.toLowerCase().substring(0, 1) + simpleName.substring(1);
	}

	/**
	 * The denotations computed by the analysis
	 */
	private final Map<MCodeMember, Denotation<L, E>> denotations;

	/**
	 * Builds a new analysis.
	 */
	protected AbstractAnalysis() {
		denotations = new HashMap<>();
	}

	/**
	 * Builds an empty environment that will be used as a singleton for all the
	 * methods
	 * 
	 * @return the empty environment
	 */
	public abstract E mkEmptyEnvironment();

	@Override
	public final void run(Program program) {
		// TODO interprocedurality, fields
		for (MCodeMember code : logger.mkIterationLogger(getName() + " analysis", "methods").iterate(program.getAllSubmittedCodeMembers())) {
			E env = mkEmptyEnvironment();
			for (MFormalParameter t : code.getParameters())
				env.set(new MLocalVariable(t.getType(), t.getName()), env.defaultLatticeForType(t.getType()));
			
			Fixpoint<L, E> engine = mkFixpoint(code.getCode());
			logger.mkTimerLogger("method " + code.toString()).execAction(Level.DEBUG, () -> denotations.put(code, engine.fixpoint(env, this::smallStepSemantics, this::assume)));
		}
	}
	
	protected abstract Fixpoint<L, E> mkFixpoint(MCodeBlock code);

	@Override
	public final Denotation<L, E> of(MCodeMember code) {
		return denotations.get(code);
	}
}
