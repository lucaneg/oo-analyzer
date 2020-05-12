package it.lucaneg.oo.analyzer.analyses.strings.stringprefix;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringEnvironment;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;

/**
 * Prefix environment, as defined in:
 * 
 * Giulia Costantini, Pietro Ferrara, Agostino Cortesi
 * "A suite of abstract domains for static analysis of string values."
 * in Softw. Pract. Exp., Vol. 45
 * 
 * @author Luca Negrini
 */
public class StringPrefixEnvironment extends BaseStringEnvironment<StringPrefixLattice, StringPrefixEnvironment> {

	/**
	 * Builds an empty string prefix environment
	 */
	public StringPrefixEnvironment() {
		super();
	}

	/**
	 * Builds a string prefix environment by shallow copying the given one
	 * 
	 * @param other the original environment
	 */
	private StringPrefixEnvironment(StringPrefixEnvironment other) {
		super(other.approximations);
	}

	@Override
	public StringPrefixEnvironment copy() {
		return new StringPrefixEnvironment(this);
	}

	@Override
	protected StringPrefixLattice latticeBottom() {
		return StringPrefixLattice.getBottom();
	}

	@Override
	protected StringPrefixLattice latticeTop() {
		return StringPrefixLattice.getTop();
	}

	@Override
	protected StringPrefixLattice provideApproxFor(StringLiteral literal) {
		return new StringPrefixLattice(literal.getValue());
	}

	@Override
	protected StringPrefixLattice evalSubstring(StringPrefixLattice receiver, int begin, int end) {
		if (receiver.isTop())
			return receiver;
		
		if (begin > end || begin < 0)
			return latticeBottom();

		if (end <= receiver.getPrefix().length())
			return new StringPrefixLattice(receiver.getPrefix().substring(begin, end));
		else if (begin < receiver.getPrefix().length())
			return new StringPrefixLattice(receiver.getPrefix().substring(begin));
		
		return StringPrefixLattice.getTop();
	}
	
	@Override
	protected StringPrefixLattice evalReplace(StringPrefixLattice receiver, StringPrefixLattice first,
			StringPrefixLattice second) {
		if (receiver.isTop() || first.isTop() || second.isTop())
			return receiver;
		
		String toReplace = first.getPrefix();
		String str = second.getPrefix();
		String target = receiver.getPrefix();

		if (!target.contains(toReplace))
			return receiver;
		
		return new StringPrefixLattice(target.replace(toReplace, str));
	}

	@Override
	protected StringPrefixLattice evalConcat(StringPrefixLattice receiver, StringPrefixLattice parameter) {
		return receiver;
	}

	@Override
	protected Satisfiability satisfiesContains(StringPrefixLattice rec, StringPrefixLattice par) {
		return Satisfiability.UNKNOWN;
	}
	
	@Override
	protected Satisfiability satisfiesEndsWith(StringPrefixLattice rec, StringPrefixLattice par) {
		return Satisfiability.UNKNOWN;
	}
	
	@Override
	protected Satisfiability satisfiesEquals(StringPrefixLattice rec, StringPrefixLattice par) {
		return Satisfiability.UNKNOWN;
	}
	
	@Override
	protected Satisfiability satisfiesStartsWith(StringPrefixLattice rec, StringPrefixLattice par) {
		if (!rec.isTop() && !par.isTop() && rec.getPrefix().startsWith(par.getPrefix()))
			return Satisfiability.SATISFIED;
		
		return Satisfiability.UNKNOWN;
	}
}
