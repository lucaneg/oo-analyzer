package it.lucaneg.oo.sdk.analyzer.fixpoint;

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.BiFunction;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.sdk.analyzer.analyses.Denotation;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.Lattice;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;
import it.lucaneg.oo.sdk.analyzer.program.instructions.BranchingStatement;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

public abstract class AbstractFixpoint<L extends Lattice<L>, E extends Environment<L,E>> extends MCodeBlock implements Fixpoint<L,E> {

	private static final EnrichedLogger logger = new EnrichedLogger(AbstractFixpoint.class);
	
	protected AbstractFixpoint(MCodeBlock source) {
		super(source);
	}
	
	protected abstract void setup();
	
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
		setup();
		Queue<Statement> nextInstructions = new LinkedList<>();
		nextInstructions.add(getRootNode());
		Map<Statement, Boolean> seen = new IdentityHashMap<>();
		E previousApprox = null, newApprox;
		while (nextInstructions.size() != 0) {
			Statement current = nextInstructions.poll();
			if (current == null)
				throw new IllegalStateException("Unknown instruction encountered during fixpoint execution");

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

			if (entrystate == null)
				throw new IllegalStateException(
						"We should have always processed at least one entry point before queueing a statement in the worklist");

			if (entrystate.isUnreachable()) {
				// this is unreachable code
				// TODO write this better
				newApprox = entrystate.only(current.getVariables());
				// make sure this is unreachable
				newApprox.makeUnreachable();
			} else {
				// remove dead variables
				entrystate = entrystate.only(current.getVariables());
				
				previousApprox = result.hasEnvironmentFor(current) ? result.at(current) : null;
				try {
					newApprox = semantics.apply(current, entrystate);
				} catch (Exception e) {
					logger.error("Exception while analyzing instruction '" + current + "' in method " + current.getContainer().toString(), e);
					throw new RuntimeException(e);
				}
	
				if (applyNarrowing) 
					newApprox = updateWithNarrowing(previousApprox, newApprox, current);
				else
					newApprox = update(previousApprox, newApprox, current);

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

	protected abstract E update(E previousApprox, E newApprox, Statement current);
	
	protected E updateWithNarrowing(E previousApprox, E newApprox, Statement current) {
		return update(previousApprox, newApprox, current);
	}
}
