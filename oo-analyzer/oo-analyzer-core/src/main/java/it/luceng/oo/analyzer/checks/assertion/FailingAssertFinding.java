package it.luceng.oo.analyzer.checks.assertion;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import it.lucaneg.oo.api.analyzer.checks.Finding;

public class FailingAssertFinding extends Finding {

	private final SortedSet<String> analysesNames;
	
	public FailingAssertFinding(String filename, int line, int col, String producer, String... analysesNames) {
		super(filename, line, col, producer);
		this.analysesNames = new TreeSet<>();
		for (String name : analysesNames)
			this.analysesNames.add(name);
	}
	
	public FailingAssertFinding(String filename, int line, int col, String producer, SortedSet<String> analysesNames) {
		super(filename, line, col, producer);
		this.analysesNames = analysesNames;
	}

	@Override
	public String getMessage() {
		if (analysesNames.size() == 1)
			return "according to " + analysesNames.iterator().next() + " analysis, this assert never holds";
		else 
			return "according to " + StringUtils.join(analysesNames, ", ") + " analyses, this assert never holds"; 
	}

	@Override
	protected int compareToAux(Finding o) {
		FailingAssertFinding other = (FailingAssertFinding) o;
		int cmp;

		if ((cmp = analysesNames.size() - other.analysesNames.size()) != 0)
			return cmp;
		
		for (Iterator<String> first = analysesNames.iterator(), second = analysesNames.iterator(); first.hasNext() && second.hasNext();) 
			if ((cmp = first.next().compareTo(second.next())) != 0)
				return cmp;
		
		return 0;
	}
}
