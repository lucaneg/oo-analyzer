package it.lucaneg.oo.analyzer.analyses.value.domains.strings.tarsis;

import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.strings.AbstractStringLattice;
import it.lucaneg.oo.sdk.analyzer.analyses.Analysis;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability;
import it.unive.strings.AutomatonString;
import it.unive.strings.AutomatonString.Interval;

/**
 * A lattice for string elements represented through automata
 * 
 * @author Luca Negrini
 */
public class TarsisLattice extends AbstractStringLattice<TarsisLattice> {

	/**
	 * The unique top element
	 */
	private static final TarsisLattice TOP = new TarsisLattice(null) {
		@Override
		public boolean equals(Object obj) {
			return this == obj;
		}

		@Override
		public int hashCode() {
			return "TOP".hashCode();
		}
		
		@Override
		public String toString() {
			return String.valueOf(Analysis.TOP_CHAR);
		}
	};

	/**
	 * The unique bottom element
	 */
	private static final TarsisLattice BOTTOM = new TarsisLattice(null) {
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
	public TarsisLattice() {
		this(new AutomatonString());
	}

	/**
	 * Builds a lattice element containing the given string
	 * 
	 * @param string the string
	 */
	public TarsisLattice(AutomatonString string) {
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
	protected TarsisLattice lubAux(TarsisLattice other) {
		return decide(other);
	}

	@Override
	protected TarsisLattice wideningAux(TarsisLattice other) {
		return decide(other);
	}

	private TarsisLattice decide(TarsisLattice other) {
		int MAX = 10;
		if (string.size() > MAX || other.string.size() > MAX)
			return new TarsisLattice(string.widen(other.string));
		else
			return new TarsisLattice(string.lub(other.string));
	}

	@Override
	public TarsisLattice bottom() {
		return getBottom();
	}

	@Override
	public TarsisLattice top() {
		return getTop();
	}

	/**
	 * Yields the unique bottom element
	 * 
	 * @return the bottom element
	 */
	public static TarsisLattice getBottom() {
		return BOTTOM;
	}

	/**
	 * Yields the unique top element
	 * 
	 * @return the top element
	 */
	public static TarsisLattice getTop() {
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
		TarsisLattice other = (TarsisLattice) obj;
		if (string == null) {
			if (other.string != null)
				return false;
		} else if (!string.equals(other.string))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "\"" + string.toString() + "\"";
	}

	@Override
	public TarsisLattice mk(String string) {
		return new TarsisLattice(new AutomatonString(string));
	}

	@Override
	public Satisfiability contains(TarsisLattice other) {
		if (getString().contains(other.getString()))
			return Satisfiability.SATISFIED;

		if (getString().mayContain(other.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public Satisfiability startsWith(TarsisLattice other) {
		if (getString().startsWith(other.getString()))
			return Satisfiability.SATISFIED;

		if (getString().mayStartWith(other.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public Satisfiability endsWith(TarsisLattice other) {
		if (getString().endsWith(other.getString()))
			return Satisfiability.SATISFIED;

		if (getString().mayEndWith(other.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public Satisfiability isEquals(TarsisLattice other) {
		if (getString().isEqualTo(other.getString()))
			return Satisfiability.SATISFIED;

		if (getString().mayBeEqualTo(other.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public TarsisLattice concat(TarsisLattice other) {
		return new TarsisLattice(getString().concat(other.getString()));
	}

	@Override
	public TarsisLattice substring(int begin, int end) {
		return new TarsisLattice(getString().substring(begin, end));
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractIntegerLattice indexOf(TarsisLattice str, AbstractIntegerLattice singleton) {
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
	public TarsisLattice replace(TarsisLattice toReplace, TarsisLattice str) {
		return new TarsisLattice(getString().replace(toReplace.getString(), str.getString()));
	}

	@Override
	public Boolean isEqualTo(TarsisLattice other) {
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return getString().isEqualTo(other.getString());
	}

	@Override
	public Boolean isLessThen(TarsisLattice other) {
		return null;
	}

	@Override
	public Boolean isGreaterThan(TarsisLattice other) {
		return null;
	}
	
	@Override
	public TarsisLattice mkTopString() {
		return new TarsisLattice();
	}
}
