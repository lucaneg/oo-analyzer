package it.lucaneg.oo.analyzer.analyses.value.domains.bools;

import it.lucaneg.oo.analyzer.analyses.value.SingleAbstractValue;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability;

public abstract class AbstractBooleanLattice<L extends AbstractBooleanLattice<L>> extends SingleAbstractValue<L> {

	/**
	 * Yields the unique true value.
	 * 
	 * @return the true value
	 */
	public abstract L getTrue();

	/**
	 * Yields the unique false value.
	 * 
	 * @return the false value
	 */
	public abstract L getFalse();

	/**
	 * Transform a boolean abstraction into a satisfiability level
	 * 
	 * @return the {@link Satisfiability} corresponding to this boolean abstraction
	 */
	public abstract Satisfiability toSatisfiability();
}
