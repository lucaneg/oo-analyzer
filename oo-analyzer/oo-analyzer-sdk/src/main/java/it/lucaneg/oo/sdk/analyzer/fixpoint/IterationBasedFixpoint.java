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
		return newApprox;
	}
}
