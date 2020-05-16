package it.lucaneg.oo.sdk.analyzer.fixpoint;

import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import it.lucaneg.oo.sdk.analyzer.analyses.Environment;
import it.lucaneg.oo.sdk.analyzer.analyses.Lattice;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;
import it.lucaneg.oo.sdk.analyzer.program.MLocalVariable;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

/**
 * A fixpoint algorithm that applies widening when the approximation reaches a
 * given size.
 * 
 * @author Luca Negrini
 */
public class SizeBasedFixpoint<L extends Lattice<L>, E extends Environment<L, E>> extends AbstractFixpoint<L, E> {

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
	protected void setup() {
		// nothing to do
	}
	
	@Override
	protected E update(E previousApprox, E newApprox, Statement current) {
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
		
		return newApprox.join(previousApprox, Lattice::lub);
	}
}
