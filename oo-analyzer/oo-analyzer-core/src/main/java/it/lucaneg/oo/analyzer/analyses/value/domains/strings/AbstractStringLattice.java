package it.lucaneg.oo.analyzer.analyses.value.domains.strings;

import it.lucaneg.oo.analyzer.analyses.value.SingleValueLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability;

public abstract class AbstractStringLattice<L extends AbstractStringLattice<L>> extends SingleValueLattice<L> {

	public abstract L mk(String string);
	
	public L mkTopString() {
		return top();
	}
	
	public abstract Satisfiability contains(L other);  

	public abstract Satisfiability startsWith(L other);  
	
	public abstract Satisfiability endsWith(L other);  
	
	public abstract Satisfiability isEquals(L other);  
	
	public abstract L concat(L other);
	
	public abstract L substring(int begin, int end);
	
	public abstract L replace(L toReplace, L str);

	public abstract AbstractIntegerLattice<?> length(AbstractIntegerLattice<?> singleton);
	
	public abstract AbstractIntegerLattice<?> indexOf(L str, AbstractIntegerLattice<?> singleton);
}
