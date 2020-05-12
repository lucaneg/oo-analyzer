package it.lucaneg.oo.api.analyzer.fixpoint;

import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import it.lucaneg.oo.api.analyzer.analyses.Denotation;
import it.lucaneg.oo.api.analyzer.analyses.Environment;
import it.lucaneg.oo.api.analyzer.analyses.Lattice;
import it.lucaneg.oo.api.analyzer.program.MCodeBlock;
import it.lucaneg.oo.api.analyzer.program.MLocalVariable;
import it.lucaneg.oo.api.analyzer.program.instructions.BranchingStatement;
import it.lucaneg.oo.api.analyzer.program.instructions.Statement;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.expression.logical.Not;

/**
 * A fixpoint algorithm that applies widening when the approximation reaches a
 * given size.
 * 
 * @author Luca Negrini
 */
public class SizeBasedFixpoint<L extends Lattice<L>, E extends Environment<L, E>> extends MCodeBlock implements Fixpoint<L, E> {

	/**
	 * The function that knows when to apply widening
	 */
	private final Function<L, Boolean> sizeEvaluator;

	/**
	 * Builds the fixpoint engine.
	 * 
	 * @param source        the block of code to iterate over
	 * @param sizeEvaluator the function that knows when to apply widening
	 */
	public SizeBasedFixpoint(MCodeBlock source, Function<L, Boolean> sizeEvaluator) {
		super(source);
		this.sizeEvaluator = sizeEvaluator;
	}

	@Override
	public Denotation<L, E> fixpoint(E initialstate, BiFunction<Statement, E, E> semantics,
			BiFunction<Expression, E, E> assume) {
		Denotation<L, E> result = new Denotation<>(getVertexCount());

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

			if (previousApprox != null)
				for (Pair<MLocalVariable, L> approx : newApprox) {
					MLocalVariable var = approx.getLeft();
					L el = approx.getRight();
					if (previousApprox.hasApproximationFor(var))
						if (sizeEvaluator.apply(el)) 
							newApprox.set(var, el.widening(previousApprox.at(var)));
						else
							newApprox.set(var, el.lub(previousApprox.at(var)));
				}
			
			newApprox = newApprox.join(previousApprox, Lattice::lub);
			
			if (!newApprox.equals(previousApprox)) {
				for (Statement instr : followersOf(current))
					nextInstructions.push(instr);

				result.set(current, newApprox);
			}
		}

		return result;
	}
}
