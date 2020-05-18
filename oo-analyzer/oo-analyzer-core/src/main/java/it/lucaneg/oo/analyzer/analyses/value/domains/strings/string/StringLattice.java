package it.lucaneg.oo.analyzer.analyses.value.domains.strings.string;

import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.strings.AbstractStringLattice;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability;
import it.unive.strings.AutomatonString;
import it.unive.strings.AutomatonString.Interval;

/**
 * A lattice for string elements represented through automata
 * 
 * @author Luca Negrini
 */
public class StringLattice extends AbstractStringLattice<StringLattice> {

	/**
	 * The unique top element
	 */
	private static final StringLattice TOP = new StringLattice() {
		@Override
		public boolean equals(Object obj) {
			return this == obj;
		}

		@Override
		public int hashCode() {
			return "TOP".hashCode();
		}
	};

	/**
	 * The unique bottom element
	 */
	private static final StringLattice BOTTOM = new StringLattice(null) {
		@Override
		public boolean equals(Object obj) {
			return this == obj;
		}

		@Override
		public int hashCode() {
			return "BOTTOM".hashCode();
		}

		@Override
		public String toString() {
			return "_|_";
		}
	};

	/**
	 * The effective domain element
	 */
	private final AutomatonString string;

	/**
	 * Builds a lattice element containing the top string
	 */
	private StringLattice() {
		this(new AutomatonString());
	}

	/**
	 * Builds a lattice element containing the given string
	 * 
	 * @param string the string
	 */
	public StringLattice(AutomatonString string) {
		this.string = string;
	}

	/**
	 * Yields the string represented by this lattice element
	 * 
	 * @return the string
	 */
	public AutomatonString getString() {
		return string;
	}

	@Override
	protected StringLattice lubAux(StringLattice other) {
		return decide(other);
	}

	@Override
	protected StringLattice wideningAux(StringLattice other) {
		return decide(other);
	}

	private StringLattice decide(StringLattice other) {
		int MAX = 10;
		if (string.size() > MAX || other.string.size() > MAX)
			return new StringLattice(string.widen(other.string));
		else
			return new StringLattice(string.lub(other.string));
	}

	@Override
	public StringLattice bottom() {
		return getBottom();
	}

	@Override
	public StringLattice top() {
		return getTop();
	}

	/**
	 * Yields the unique bottom element
	 * 
	 * @return the bottom element
	 */
	public static StringLattice getBottom() {
		return BOTTOM;
	}

	/**
	 * Yields the unique top element
	 * 
	 * @return the top element
	 */
	public static StringLattice getTop() {
		return TOP;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((string == null) ? 0 : string.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringLattice other = (StringLattice) obj;
		if (string == null) {
			if (other.string != null)
				return false;
		} else if (!string.equals(other.string))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return string.toString();
	}

	@Override
	public StringLattice mk(String string) {
		return new StringLattice(new AutomatonString(string));
	}

	@Override
	public Satisfiability contains(StringLattice other) {
		if (getString().contains(other.getString()))
			return Satisfiability.SATISFIED;

		if (getString().mayContain(other.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public Satisfiability startsWith(StringLattice other) {
		if (getString().startsWith(other.getString()))
			return Satisfiability.SATISFIED;

		if (getString().mayStartWith(other.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public Satisfiability endsWith(StringLattice other) {
		if (getString().endsWith(other.getString()))
			return Satisfiability.SATISFIED;

		if (getString().mayEndWith(other.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public Satisfiability isEquals(StringLattice other) {
		if (getString().isEqualTo(other.getString()))
			return Satisfiability.SATISFIED;

		if (getString().mayBeEqualTo(other.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public StringLattice concat(StringLattice other) {
		return new StringLattice(getString().concat(other.getString()));
	}

	@Override
	public StringLattice substring(int begin, int end) {
		return new StringLattice(getString().substring(begin, end));
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractIntegerLattice indexOf(StringLattice str, AbstractIntegerLattice singleton) {
		Interval indexOf = getString().indexOf(str.getString());
		AbstractIntegerLattice base = singleton.mk(indexOf.getLower());
		if (indexOf.topIsInfinity())
			return (AbstractIntegerLattice) base.widening(singleton.mk(Integer.MAX_VALUE));
		else
			return (AbstractIntegerLattice) base.lub(singleton.mk(indexOf.getUpper()));
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractIntegerLattice<?> length(AbstractIntegerLattice<?> singleton) {
		Interval length = getString().length();
		AbstractIntegerLattice base = singleton.mk(length.getLower());
		if (length.topIsInfinity())
			return (AbstractIntegerLattice) base.widening(singleton.mk(Integer.MAX_VALUE));
		else
			return (AbstractIntegerLattice) base.lub(singleton.mk(length.getUpper()));
	}

	@Override
	public StringLattice replace(StringLattice toReplace, StringLattice str) {
		return new StringLattice(getString().replace(toReplace.getString(), str.getString()));
	}

	@Override
	public Boolean isEqualTo(StringLattice other) {
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return getString().isEqualTo(other.getString());
	}

	@Override
	public Boolean isLessThen(StringLattice other) {
		return null;
	}

	@Override
	public Boolean isGreaterThan(StringLattice other) {
		return null;
	}
}
