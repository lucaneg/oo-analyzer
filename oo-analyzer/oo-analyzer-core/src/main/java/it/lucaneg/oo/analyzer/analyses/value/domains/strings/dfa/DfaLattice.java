package it.lucaneg.oo.analyzer.analyses.value.domains.strings.dfa;

import java.util.HashSet;

import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.strings.AbstractStringLattice;
import it.lucaneg.oo.sdk.analyzer.analyses.SatisfiabilityEvaluator.Satisfiability;
import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;

/**
 * A lattice for string elements represented through dfa
 * TODO vincenzo
 * 
 * @author Luca Negrini
 */
public class DfaLattice extends AbstractStringLattice<DfaLattice> {

	/**
	 * The unique top element
	 */
	private static final DfaLattice TOP = new DfaLattice(Automaton.makeTopLanguage()) {
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
	private static final DfaLattice BOTTOM = new DfaLattice(Automaton.makeEmptyLanguage()) {
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
	private final Automaton string;

	/**
	 * Builds a lattice element containing the given string
	 * 
	 * @param string the string
	 */
	public DfaLattice(Automaton string) {
		this.string = string;
	}

	/**
	 * Yields the string represented by this lattice element
	 * 
	 * @return the string
	 */
	public Automaton getString() {
		return string;
	}

	@Override
	protected DfaLattice lubAux(DfaLattice other) {
		return new DfaLattice(Automaton.union(string, other.string));
	}

	@Override
	protected DfaLattice wideningAux(DfaLattice other) {
		return new DfaLattice(lubAux(other).string.widening(5));
	}

	@Override
	public DfaLattice bottom() {
		return getBottom();
	}

	@Override
	public DfaLattice top() {
		return getTop();
	}

	/**
	 * Yields the unique bottom element
	 * 
	 * @return the bottom element
	 */
	public static DfaLattice getBottom() {
		return BOTTOM;
	}

	/**
	 * Yields the unique top element
	 * 
	 * @return the top element
	 */
	public static DfaLattice getTop() {
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
		DfaLattice other = (DfaLattice) obj;
		if (string == null) {
			if (other.string != null)
				return false;
		} else if (!string.equals(other.string))
			return false;
		return true;
	}

	@Override
	public String toString() {
		string.minimize();
		if (string.isSingleString())
			return string.getSingleString();
		return string.automatonPrint();
	}

	@Override
	public DfaLattice mk(String string) {
		return new DfaLattice(Automaton.makeAutomaton(string));
	}

	@Override
	public Satisfiability contains(DfaLattice other) {
		int includes = Automaton.includes(getString(), other.getString());

		if (includes == 1)
			return Satisfiability.SATISFIED;

		if (includes == -1)
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public Satisfiability startsWith(DfaLattice other) {
		int includes = Automaton.startsWith(getString(), other.getString());

		if (includes == 1)
			return Satisfiability.SATISFIED;

		if (includes == -1)
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public Satisfiability endsWith(DfaLattice other) {
		int includes = Automaton.endsWith(getString(), other.getString());

		if (includes == 1)
			return Satisfiability.SATISFIED;

		if (includes == -1)
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public Satisfiability isEquals(DfaLattice other) {
		if (getString().equals(other.getString()))
			return Satisfiability.UNKNOWN;

		return Satisfiability.NOT_SATISFIED;
	}

	@Override
	public DfaLattice concat(DfaLattice other) {
		return new DfaLattice(Automaton.concat(getString(), other.getString()));
	}

	@Override
	public DfaLattice substring(int begin, int end) {
		return new DfaLattice(Automaton.substring(getString(), begin, end));
	}
	
	@Override
	public AbstractIntegerLattice<?> indexOf(DfaLattice str, AbstractIntegerLattice<?> singleton) {
		return indexOf(singleton, str.string);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private AbstractIntegerLattice<?> mkInterval(AbstractIntegerLattice<?> singleton, Integer low, Integer high) {
		AbstractIntegerLattice base = singleton.mk(low);
		if (high == null)
			return (AbstractIntegerLattice) base.widening(singleton.mk(Integer.MAX_VALUE));
		else
			return (AbstractIntegerLattice) base.lub(singleton.mk(high));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractIntegerLattice<?> indexOf(AbstractIntegerLattice<?> singleton, Automaton search) {
		if (string.hasCycle())
			return mkInterval(singleton, -1, null);

		if (string.isSingleString() && search.isSingleString()) {
			String first = string.getSingleString();
			String second = search.getSingleString();

			if (first.contains(second)) {
				int i = first.indexOf(second);
				return mkInterval(singleton, i, i);
			} else {
				return mkInterval(singleton, -1, -1);
			}

		} else if (!string.hasCycle() && !search.hasCycle()) {
			HashSet<String> first = string.getLanguage();
			HashSet<String> second = search.getLanguage();

			AbstractIntegerLattice result = null;
			for (String f1 : first) {
				for (String f2 : second) {
					AbstractIntegerLattice partial;

					if (f1.contains(f2)) {
						int i = f1.indexOf(f2);
						partial = mkInterval(singleton, i, i);
					} else {
						partial = mkInterval(singleton, -1, -1);
					}
					result = result == null ? partial : (AbstractIntegerLattice) partial.lub(result);
				}
			}

			return result;
		}

		Automaton build = string.isSingleString() ? Automaton.makeRealAutomaton(string.getSingleString())
				: string.clone();
		Automaton search_clone = search.isSingleString() ? Automaton.makeRealAutomaton(search.getSingleString())
				: search.clone();

		Automaton original = string.isSingleString() ? Automaton.makeRealAutomaton(string.getSingleString())
				: string.clone();

		HashSet<Integer> indexesOf = new HashSet<>();

		for (State s : build.getStates()) {
			if (s.isInitialState())
				s.setInitialState(false);
			s.setFinalState(true);
		}

		for (State q : build.getStates()) {
			q.setInitialState(true);

			if (!Automaton.isEmptyLanguageAccepted(Automaton.intersection(build, search_clone)))
				indexesOf.add(original.maximumDijkstra(q).size() - 1);

			q.setInitialState(false);
		}

		// No state in the automaton can read search
		if (indexesOf.isEmpty())
			return mkInterval(singleton, -1, -1);
		else if (search_clone.recognizesExactlyOneString() && original.recognizesExactlyOneString())
			return mkInterval(singleton, indexesOf.stream().mapToInt(i -> i).min().getAsInt(),
					indexesOf.stream().mapToInt(i -> i).max().getAsInt());
		else
			return mkInterval(singleton, -1, indexesOf.stream().mapToInt(i -> i).max().getAsInt());

	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractIntegerLattice<?> length(AbstractIntegerLattice<?> singleton) {
		int length = Automaton.length(getString());
		if (length == -1) {
			AbstractIntegerLattice inf = singleton.mk(Integer.MAX_VALUE);
			AbstractIntegerLattice mk = singleton.mk(0);
			return (AbstractIntegerLattice) mk.widening(inf);
		}
		return singleton.mk(length);
	}

	@Override
	public DfaLattice replace(DfaLattice toReplace, DfaLattice str) {
		return new DfaLattice(Automaton.replace(getString(), toReplace.getString(), str.getString()));
	}

	@Override
	public Boolean isEqualTo(DfaLattice other) {
		if (isTop() || other.isTop() || isBottom() || other.isBottom())
			return null;
		return getString().equals(other.getString());
	}

	@Override
	public Boolean isLessThen(DfaLattice other) {
		return null;
	}

	@Override
	public Boolean isGreaterThan(DfaLattice other) {
		return null;
	}
}
