package it.lucaneg.oo.analyzer.analyses.strings.string;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringEnvironment;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;
import it.unive.strings.AutomatonString;

/**
 * An environment for string elements represented through automata
 * 
 * @author Luca Negrini
 */
public class StringEnvironment extends BaseStringEnvironment<StringLattice, StringEnvironment> {

	/**
	 * Builds an empty string environment
	 */
	public StringEnvironment() {
		super();
	}

	/**
	 * Builds a string environment by shallow copying the given one
	 * 
	 * @param other the original environment
	 */
	private StringEnvironment(StringEnvironment other) {
		super(other.approximations);
	}

	@Override
	public StringEnvironment copy() {
		return new StringEnvironment(this);
	}

	@Override
	protected StringLattice latticeBottom() {
		return StringLattice.getBottom();
	}

	@Override
	protected StringLattice latticeTop() {
		return StringLattice.getTop();
	}
	
	@Override
	protected StringLattice provideApproxFor(StringLiteral literal) {
		return new StringLattice(new AutomatonString(literal.getValue()));
	}

	@Override
	protected StringLattice evalSubstring(StringLattice receiver, int begin, int end) {
		return new StringLattice(receiver.getString().substring(begin, end));
	}

	@Override
	protected StringLattice evalConcat(StringLattice receiver, StringLattice parameter) {
		return new StringLattice(receiver.getString().concat(parameter.getString()));
	}
	
	@Override
	protected StringLattice evalReplace(StringLattice receiver, StringLattice first, StringLattice second) {
		return new StringLattice(receiver.getString().replace(first.getString(), second.getString()));
	}

	@Override
	protected Satisfiability satisfiesContains(StringLattice rec, StringLattice par) {
		if (rec.getString().contains(par.getString()))
			return Satisfiability.SATISFIED;
		
		return Satisfiability.UNKNOWN;
	}
	
	@Override
	protected Satisfiability satisfiesEndsWith(StringLattice rec, StringLattice par) {
		if (rec.getString().endsWith(par.getString()))
			return Satisfiability.SATISFIED;
		
		return Satisfiability.UNKNOWN;
	}
	
	@Override
	protected Satisfiability satisfiesEquals(StringLattice rec, StringLattice par) {
		if (rec.getString().isEqualTo(par.getString()))
			return Satisfiability.SATISFIED;
		
		return Satisfiability.UNKNOWN;
	}
	
	@Override
	protected Satisfiability satisfiesStartsWith(StringLattice rec, StringLattice par) {
		if (rec.getString().startsWith(par.getString()))
			return Satisfiability.SATISFIED;
		
		return Satisfiability.UNKNOWN;
	}
}
