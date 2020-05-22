package it.lucaneg.oo.sdk.analyzer.fixpoint;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.sdk.analyzer.analyses.Denotation;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.Lattice;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;
import it.lucaneg.oo.sdk.analyzer.program.instructions.BranchingStatement;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

/**
 * A fixpoint algorithm that applies widening after a given number of lub
 * iterations.
 * 
 * @author Luca Negrini
 */
public class FixpointImpl<L extends Lattice<L>, E extends Environment<L, E>> extends MCodeBlock implements Fixpoint<L,E> {

	private static final EnrichedLogger logger = new EnrichedLogger(FixpointImpl.class);
	
	/**
	 * The threshold to reach before applying widening
	 */
	private final int wideningThreshold;
	
	private Map<Statement, AtomicInteger> remainingLubBeforeWidening;

	/**
	 * Builds the fixpoint engine.
	 * 
	 * @param source            the block of code to iterate over
	 * @param wideningThreshold the threshold to reach before applying widening (0
	 *                          means never widen)
	 */
	public FixpointImpl(MCodeBlock source, int wideningThreshold) {
		super(source);
		this.wideningThreshold = wideningThreshold;
	}
	
	protected E lubOrWidening(E previousApprox, E newApprox, Statement current) {
		if (previousApprox == null)
			return newApprox;
		else if (wideningThreshold == 0)
			newApprox = newApprox.join(previousApprox, Lattice::lub);
		else {
			// we multiply by the number of predecessors since if we have more than one
			// the threshold will be reached faster
			int remainingLubs = remainingLubBeforeWidening
					.computeIfAbsent(current, st -> new AtomicInteger(wideningThreshold * predecessorsOf(current).size())).getAndDecrement();
			if (remainingLubs > 0)
				newApprox = newApprox.join(previousApprox, Lattice::lub);
			else
				newApprox = newApprox.join(previousApprox, Lattice::widening);
		}
		return newApprox;
	}
	
	protected E narrowing(E previousApprox, E newApprox, Statement current) {
		return newApprox.join(previousApprox, Lattice::narrowing);
	}
	
	@Override
	public final Denotation<L, E> fixpoint(E initialstate, BiFunction<Statement, E, E> semantics,
			BiFunction<Expression, E, E> assume) {
		Denotation<L, E> result = new Denotation<>(getVertexCount());

		loop(initialstate, semantics, assume, result, false);
		loop(initialstate, semantics, assume, result, true);

		return result;
	}

	private void loop(E initialstate, BiFunction<Statement, E, E> semantics, BiFunction<Expression, E, E> assume,
			Denotation<L, E> result, boolean applyNarrowing) {
		remainingLubBeforeWidening = new HashMap<>(getVertexCount());
		Queue<Statement> nextInstructions = new LinkedList<>();
		nextInstructions.add(getRootNode());
		Map<Statement, Boolean> seen = new IdentityHashMap<>();
		
		E previousApprox = null, newApprox;
		while (nextInstructions.size() != 0) {
			Statement current = nextInstructions.poll();
			if (current == null)
				throw new IllegalStateException("Unknown instruction encountered during fixpoint execution");

			E entrystate = createEntryState(assume, result, current, initialstate);

			if (entrystate == null)
				throw new IllegalStateException(current + " does not have an entry state");

			if (entrystate.isUnreachable()) {
				// this is unreachable code
				newApprox = entrystate.only(current.getVariables());
				// make sure this is unreachable
				newApprox.makeUnreachable();
			} else {
				// remove dead variables
				entrystate = entrystate.only(current.getVariables());
				
				previousApprox = result.hasEnvironmentFor(current) ? result.at(current) : null;
				newApprox = semantics(semantics, applyNarrowing, previousApprox, current, entrystate);

				// make sure this is reachable
				newApprox.makeReachable();
			}
			
			
			if (seen.put(current, Boolean.TRUE) == null || !newApprox.equals(previousApprox)) {
				for (Statement instr : followersOf(current)) 
					nextInstructions.add(instr);

				result.set(current, newApprox);
			}
		}
	}

	private E semantics(BiFunction<Statement, E, E> semantics, boolean applyNarrowing, E previousApprox, Statement current, E entrystate) {
		E newApprox;
		
		try {
			newApprox = semantics.apply(current, entrystate);
		} catch (Exception e) {
			logger.error("Exception while analyzing instruction '" + current + "' in method " + current.getContainer().toString(), e);
			throw new RuntimeException(e);
		}

		if (applyNarrowing) 
			newApprox = narrowing(previousApprox, newApprox, current);
		else
			newApprox = lubOrWidening(previousApprox, newApprox, current);
		
		return newApprox;
	}

	private E createEntryState(BiFunction<Expression, E, E> assume, Denotation<L, E> result, Statement current, E initialstate) {
		E entrystate = current == getRootNode() ? initialstate : null;
		
		for (Statement pred : predecessorsOf(current)) {
			E state = result.hasEnvironmentFor(pred) ? result.at(pred) : null;
			if (state != null && pred instanceof BranchingStatement) {
				BranchingStatement branch = (BranchingStatement) pred;
				if (isStartOfTrueBlockFor(branch, current))
					// beginning of the true block: filter the environment
					state = assume.apply(branch.getCondition(), state);
				else if (isStartOfFalseBlockFor(branch, current))
					// beginning of the false block: negate and filter the environment
					state = assume.apply(branch.getCondition().negate().simplify(), state);
				else
					throw new IllegalStateException(current + " is a follower of " + branch
							+ " but it is neither the then nor the else block");
			}

			if (state != null)
				if (entrystate == null)
					entrystate = state;
				else
					entrystate = entrystate.join(state, Lattice::lub);
		}
		
		return entrystate;
	}
}
