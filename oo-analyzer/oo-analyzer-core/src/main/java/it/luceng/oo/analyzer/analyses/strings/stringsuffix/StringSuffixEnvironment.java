package it.luceng.oo.analyzer.analyses.strings.stringsuffix;

import it.lucaneg.oo.ast.expression.literal.StringLiteral;
import it.luceng.oo.analyzer.analyses.strings.BaseStringEnvironment;

/**
 * Suffix environment, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class StringSuffixEnvironment extends BaseStringEnvironment<StringSuffixLattice, StringSuffixEnvironment> {

	/**
	 * Builds an empty string suffix environment
	 */
	public StringSuffixEnvironment() {
		super();
	}

	/**
	 * Builds a string suffix environment by shallow copying the given one
	 * 
	 * @param other the original environment
	 */
	private StringSuffixEnvironment(StringSuffixEnvironment other) {
		super(other.approximations);
	}

	@Override
	public StringSuffixEnvironment copy() {
		return new StringSuffixEnvironment(this);
	}

	@Override
	protected StringSuffixLattice latticeBottom() {
		return StringSuffixLattice.getBottom();
	}

	@Override
	protected StringSuffixLattice latticeTop() {
		return StringSuffixLattice.getTop();
	}

	@Override
	protected StringSuffixLattice provideApproxFor(StringLiteral literal) {
		return new StringSuffixLattice(literal.getValue());
	}

	@Override
	protected StringSuffixLattice evalSubstring(StringSuffixLattice receiver, int begin, int end) {
		return latticeTop();
	}
	
	@Override
	protected StringSuffixLattice evalReplace(StringSuffixLattice receiver, StringSuffixLattice first,
			StringSuffixLattice second) {
		if (receiver.isTop() || first.isTop() || second.isTop())
			return receiver;
		
		String toReplace = first.getSuffix();
		String str = second.getSuffix();
		String target = receiver.getSuffix();

		if (!target.contains(toReplace))
			return receiver;
		
		return new StringSuffixLattice(target.replace(toReplace, str));
	}

	@Override
	protected StringSuffixLattice evalConcat(StringSuffixLattice receiver, StringSuffixLattice parameter) {
		return parameter;
	}

	@Override
	protected Satisfiability satisfiesContains(StringSuffixLattice rec, StringSuffixLattice par) {
		return Satisfiability.UNKNOWN;
	}
	
	@Override
	protected Satisfiability satisfiesEndsWith(StringSuffixLattice rec, StringSuffixLattice par) {
		if (!rec.isTop() && !par.isTop() && rec.getSuffix().endsWith(par.getSuffix()))
			return Satisfiability.SATISFIED;
		
		return Satisfiability.UNKNOWN;
	}
	
	@Override
	protected Satisfiability satisfiesEquals(StringSuffixLattice rec, StringSuffixLattice par) {
		return Satisfiability.UNKNOWN;
	}
	
	@Override
	protected Satisfiability satisfiesStartsWith(StringSuffixLattice rec, StringSuffixLattice par) {
		return Satisfiability.UNKNOWN;
	}
}
