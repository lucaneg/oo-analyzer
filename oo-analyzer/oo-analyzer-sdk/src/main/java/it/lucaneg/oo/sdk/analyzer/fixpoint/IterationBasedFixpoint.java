package it.lucaneg.oo.sdk.analyzer.fixpoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.expression.logical.Not;
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
public class IterationBasedFixpoint<L extends Lattice<L>, E extends Environment<L, E>> extends MCodeBlock
		implements Fixpoint<L, E> {

	/**
	 * The threshold to reach before applying widening
	 */
	private final int wideningThreshold;

	/**
	 * Builds the fixpoint engine.
	 * 
	 * @param source            the block of code to iterate over
	 * @param wideningThreshold the threshold to reach before applying widening (0
	 *                          means never widen)
	 */
	public IterationBasedFixpoint(MCodeBlock source, int wideningThreshold) {
		super(source);
		this.wideningThreshold = wideningThreshold;
	}

	@Override
	public Denotation<L, E> fixpoint(E initialstate, BiFunction<Statement, E, E> semantics,
			BiFunction<Expression, E, E> assume) {
		Denotation<L, E> result = new Denotation<>(getVertexCount());
		Map<Statement, AtomicInteger> remainingLubBeforeWidening = new HashMap<>(getVertexCount());

		Stack<Statement> nextInstructions = new Stack<>();
		nextInstructions.push(getRootNode());

		E previousApprox, newApprox;
		while (nextInstructions.size() != 0) {
			Statement current = nextInstructions.pop();
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
						state = assume.apply(new Not(branch.getCondition().getSource(), branch.getCondition().getLine(),
								branch.getCondition().getPos(), branch.getCondition()), state);
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

			previousApprox = result.hasEnvironmentFor(current) ? result.at(current) : null;
			newApprox = semantics.apply(current, entrystate);

			if (wideningThreshold == 0)
				newApprox = newApprox.join(previousApprox, Lattice::lub);
			else {
				// +1 since the first time we do lub against null and should not be considered
				int remainingLubs = remainingLubBeforeWidening
						.computeIfAbsent(current, st -> new AtomicInteger(wideningThreshold + 1)).getAndDecrement();
				if (remainingLubs > 0)
					newApprox = newApprox.join(previousApprox, Lattice::lub);
				else
					newApprox = newApprox.join(previousApprox, Lattice::widening);
			}

			if (!newApprox.equals(previousApprox)) {
				for (Statement instr : followersOf(current))
					nextInstructions.push(instr);

				result.set(current, newApprox);
			}
		}

		return result;
	}
}
