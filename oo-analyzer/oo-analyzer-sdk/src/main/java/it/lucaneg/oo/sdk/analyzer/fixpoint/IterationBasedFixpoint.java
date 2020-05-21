package it.lucaneg.oo.sdk.analyzer.fixpoint;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.Lattice;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

/**
 * A fixpoint algorithm that applies widening after a given number of lub
 * iterations.
 * 
 * @author Luca Negrini
 */
public class IterationBasedFixpoint<L extends Lattice<L>, E extends Environment<L, E>> extends AbstractFixpoint<L, E> {

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
	public IterationBasedFixpoint(MCodeBlock source, int wideningThreshold) {
		super(source);
		this.wideningThreshold = wideningThreshold;
	}
	
	@Override
	protected void setup() {
		remainingLubBeforeWidening = new HashMap<>(getVertexCount());
	}
	
	@Override
	protected E update(E previousApprox, E newApprox, Statement current) {
		if (previousApprox == null)
			return newApprox;
		else if (wideningThreshold == 0)
			newApprox = previousApprox.join(newApprox, Lattice::lub);
		else {
			// +1 since the first time we do lub against null and should not be considered
			int remainingLubs = remainingLubBeforeWidening
					.computeIfAbsent(current, st -> new AtomicInteger(wideningThreshold * predecessorsOf(current).size())).getAndDecrement();
			if (remainingLubs > 0)
				newApprox = previousApprox.join(newApprox, Lattice::lub);
			else //if (remainingLubs == 0)
				// we apply the widening here
				newApprox = previousApprox.join(newApprox, Lattice::widening);
//			else if (remainingLubs == -1)
//				// widening already applied, we perform one last try with narrowing before stopping 
//				newApprox = newApprox.join(previousApprox, Lattice::narrowing);
		}
		return newApprox;
	}
	
	@Override
	protected E updateWithNarrowing(E previousApprox, E newApprox, Statement current) {
		return previousApprox.join(newApprox, Lattice::narrowing);
	}
}
