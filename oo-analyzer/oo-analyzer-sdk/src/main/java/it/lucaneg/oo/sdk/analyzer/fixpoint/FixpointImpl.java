package it.lucaneg.oo.sdk.analyzer.fixpoint;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.sdk.analyzer.analyses.Denotation;
import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.Lattice;
import it.lucaneg.oo.sdk.analyzer.analyses.TokenList;
import it.lucaneg.oo.sdk.analyzer.analyses.TokenList.GeneralLoopToken;
import it.lucaneg.oo.sdk.analyzer.analyses.TokenList.LoopIterationToken;
import it.lucaneg.oo.sdk.analyzer.analyses.TokenList.StartingToken;
import it.lucaneg.oo.sdk.analyzer.analyses.TokenList.Token;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;
import it.lucaneg.oo.sdk.analyzer.program.instructions.BranchingStatement;
import it.lucaneg.oo.sdk.analyzer.program.instructions.LoopStatement;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

/**
 * A fixpoint algorithm that applies widening after a given number of lub
 * iterations.
 * 
 * @author Luca Negrini
 */
public class FixpointImpl<L extends Lattice<L>, E extends Environment<L, E>> extends MCodeBlock
		implements Fixpoint<L, E> {

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
					.computeIfAbsent(current,
							st -> new AtomicInteger(wideningThreshold * predecessorsOf(current).size()))
					.getAndDecrement();
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
		Queue<Pair<Statement, TokenList>> nextInstructions = new LinkedList<>();
		nextInstructions.add(Pair.of(getRootNode(), new TokenList()));
		Map<Pair<Statement, TokenList>, Boolean> seen = new HashMap<>();

		E previousApprox = null, newApprox;
		while (nextInstructions.size() != 0) {
			Pair<Statement, TokenList> current = nextInstructions.poll();
			if (current == null)
				throw new IllegalStateException("Unknown instruction encountered during fixpoint execution");

			E entrystate = createEntryState(assume, result, current, initialstate);

			if (entrystate == null)
				throw new IllegalStateException(current + " does not have an entry state");

			Statement st = current.getLeft();
			TokenList tok = current.getRight();
			if (entrystate.isUnreachable()) {
				// this is unreachable code
//				newApprox = entrystate.only(st.getVariables());
				// make sure this is unreachable
//				newApprox.makeUnreachable();
				// avoid the propagation of unreachable environments
				continue;
			} else {
				// remove dead variables
				entrystate = entrystate.only(st.getVariables());

				previousApprox = result.hasEnvironmentFor(st) ? result.at(st).get(tok) : null;
				newApprox = semantics(semantics, applyNarrowing, previousApprox, st, entrystate);

				// make sure this is reachable
				newApprox.makeReachable();
			}

			if (seen.put(current, Boolean.TRUE) == null || !newApprox.equals(previousApprox)) {
				for (Statement instr : followersOf(st)) {
					if (tok.headIsGeneralLoop() && isPartOfLoop((LoopStatement) ((GeneralLoopToken) tok.getHead()).getStatement(), instr))
						nextInstructions.add(Pair.of(instr, tok));
					else if (st instanceof LoopStatement) {
						if (isPartOfLoop((LoopStatement) st, instr)) {
							if (tok.getHead() instanceof GeneralLoopToken) {
								((GeneralLoopToken) tok.getHead()).iterate();
								nextInstructions.add(Pair.of(instr, tok));
							} else if (tok.getHead() instanceof LoopIterationToken)
								nextInstructions.add(Pair.of(instr, tok.pop().push(mkLoopIterationToken((LoopStatement) st, tok.getHead()))));
							else {
								Token lastLoop = tok.lastLoopToken();
								if (lastLoop == null || lastLoop.getStatement() != st)
									nextInstructions.add(Pair.of(instr, tok.push(mkLoopIterationToken((LoopStatement) st, tok.getHead()))));
								else 
									nextInstructions.add(Pair.of(instr, tok.push(mkLoopIterationToken((LoopStatement) st, lastLoop))));
							}
						} else 
							nextInstructions.add(Pair.of(instr, tok));
					} else if (st instanceof BranchingStatement)
						nextInstructions.add(Pair.of(instr, tok.push(mkConditionalToken((BranchingStatement) st, instr))));
					else  
						nextInstructions.add(Pair.of(instr, tok));
				}

				result.set(st, tok, newApprox);
			}
		}
	}

	private Token mkLoopIterationToken(LoopStatement st, Token head) {
		if (head instanceof GeneralLoopToken) {
			((GeneralLoopToken) head).iterate();
			return head;
		}

		if (!(head instanceof LoopIterationToken))
			return new TokenList.LoopIterationToken(st, 1);

		LoopIterationToken li = (LoopIterationToken) head;
		if (li.getIteration() < 5)
			return new TokenList.LoopIterationToken(st, li.getIteration() + 1);
		
		return new TokenList.GeneralLoopToken(st);
	}

	private Token mkConditionalToken(BranchingStatement st, Statement instr) {
		return new TokenList.ConditionalToken(st, isStartOfTrueBlockFor(st, instr));
	}

	private E semantics(BiFunction<Statement, E, E> semantics, boolean applyNarrowing, E previousApprox,
			Statement current, E entrystate) {
		E newApprox;

		try {
			newApprox = semantics.apply(current, entrystate);
		} catch (Exception e) {
			logger.error("Exception while analyzing instruction '" + current + "' in method "
					+ current.getContainer().toString(), e);
			throw new RuntimeException(e);
		}

		if (applyNarrowing)
			newApprox = narrowing(previousApprox, newApprox, current);
		else
			newApprox = lubOrWidening(previousApprox, newApprox, current);

		return newApprox;
	}

	private E createEntryState(BiFunction<Expression, E, E> assume, Denotation<L, E> result, Pair<Statement,TokenList> current, E initialstate) {
		Statement st = current.getLeft();
		TokenList tokens = current.getRight();
		E entrystate = st == getRootNode() ? initialstate : null;
		
		for (Statement pred : predecessorsOf(st)) {
			E state;
			if (tokens.getHead() instanceof GeneralLoopToken && !((GeneralLoopToken) tokens.getHead()).isFirstIteration()) {
				if (result.hasEnvironmentFor(pred)) {
					if (result.at(pred).get(tokens) != null)
						state = result.at(pred).get(tokens);
					else
						state = result.at(pred).get(tokens.pop());
				} else 
					state = null;
			} else if (pred instanceof LoopStatement) {
				if (isPartOfLoop((LoopStatement) pred, st)) {
					if (!(tokens.pop().getHead() instanceof StartingToken) && isPartOfLoop((LoopStatement) pred, tokens.pop().getHead().getStatement()))
						state = result.hasEnvironmentFor(pred) ? result.at(pred).get(tokens.pop()) : null;
					else if (tokens.getHead() instanceof LoopIterationToken)
						if (((LoopIterationToken) tokens.getHead()).getIteration() == 1)
							state = result.hasEnvironmentFor(pred) ? result.at(pred).get(tokens.pop()) : null;
						else
							state = result.hasEnvironmentFor(pred)
									? result.at(pred)
											.get(tokens.pop()
													.push(new LoopIterationToken((LoopStatement) pred,
															((LoopIterationToken) tokens.getHead()).getIteration() - 1)))
									: null;
					else if (!((GeneralLoopToken) tokens.getHead()).isFirstIteration())
						state = result.hasEnvironmentFor(pred) ? result.at(pred).get(tokens) : null;
					else
						state = result.hasEnvironmentFor(pred)
								? result.at(pred).get(tokens.pop().push(new LoopIterationToken((LoopStatement) pred, 5)))
								: null;
				} else 
					state = result.hasEnvironmentFor(pred) ? result.at(pred).get(tokens) : null;
			} else if (pred instanceof BranchingStatement)
				state = result.hasEnvironmentFor(pred) ? result.at(pred).get(tokens.pop()) : null;
			else
				state = result.hasEnvironmentFor(pred) ? result.at(pred).get(tokens) : null;

			if (state != null && pred instanceof BranchingStatement) {
				BranchingStatement branch = (BranchingStatement) pred;
				if (isStartOfTrueBlockFor(branch, st))
					// beginning of the true block: filter the environment
					state = assume.apply(branch.getCondition(), state);
				else if (isStartOfFalseBlockFor(branch, st))
					// beginning of the false block: negate and filter the environment
					state = assume.apply(branch.getCondition().negate().simplify(), state);
				else
					throw new IllegalStateException(
							st + " is a follower of " + branch + " but it is neither the then nor the else block");
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
