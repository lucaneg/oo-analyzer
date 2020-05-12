package it.lucaneg.oo.analyzer.tests.strings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import it.lucaneg.oo.analyzer.tests.FindingsDiffBuilder;
import it.lucaneg.oo.analyzer.tests.TestAnalyzer;
import it.lucaneg.oo.api.analyzer.checks.Finding;
import it.luceng.oo.analyzer.analyses.AnalysisFactory;
import it.luceng.oo.analyzer.checks.ChecksFactory;
import it.luceng.oo.analyzer.checks.assertion.FailingAssertFinding;
import it.luceng.oo.analyzer.checks.assertion.PossiblyFailingAssertFinding;
import it.luceng.oo.analyzer.core.ExitCode;
import it.luceng.oo.analyzer.options.AnalysisOptions;

public class StringAnalysesBaseTest {
	
	private SortedSet<Finding> runAnalysis(String analysis) {
		AnalysisOptions options = new AnalysisOptions();
		options.setInputFolder(new File("oo-inputs/strings")); 
		options.getAnalyses().add(AnalysisFactory.getInstance(analysis));
		options.getChecks().add(ChecksFactory.getInstance("assertion"));
		TestAnalyzer analyzer = new TestAnalyzer(options);
		
		assertEquals("Analyzer did not exit with a success exit code", ExitCode.SUCCESS, analyzer.run());
		
		return analyzer.getFindings();
	}
	
	private void checkResult(SortedSet<Finding> expected, SortedSet<Finding> actual) {
		FindingsDiffBuilder diff = new FindingsDiffBuilder(expected, actual);
		diff.computeDiff((f1, f2) -> f1.compareTo(f2));
		diff.printDiff(true);
		
		assertTrue("Missing some expected findings", diff.getOnlyExpected().isEmpty());
		assertTrue("Found new findings", diff.getOnlyActual().isEmpty());
	}
	
	@Test
	public void charInclusion() {
		SortedSet<Finding> actual = runAnalysis("charInclusion");
		SortedSet<Finding> expected = new TreeSet<>();
		expected.add(new PossiblyFailingAssertFinding("strings.java", 5, 2, "assertion", "charInclusion"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 7, 2, "assertion", "charInclusion"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 8, 2, "assertion", "charInclusion"));
		expected.add(new FailingAssertFinding("strings.java", 9, 2, "assertion", "charInclusion"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 10, 2, "assertion", "charInclusion"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 20, 2, "assertion", "charInclusion"));
		expected.add(new FailingAssertFinding("strings.java", 32, 2, "assertion", "charInclusion"));
		expected.add(new FailingAssertFinding("strings.java", 70, 2, "assertion", "charInclusion"));
		expected.add(new FailingAssertFinding("strings.java", 71, 2, "assertion", "charInclusion"));
		expected.add(new FailingAssertFinding("strings.java", 72, 2, "assertion", "charInclusion"));
		expected.add(new FailingAssertFinding("strings.java", 73, 2, "assertion", "charInclusion"));
		expected.add(new FailingAssertFinding("strings.java", 74, 2, "assertion", "charInclusion"));
		expected.add(new FailingAssertFinding("strings.java", 77, 2, "assertion", "charInclusion"));
		expected.add(new FailingAssertFinding("strings.java", 78, 2, "assertion", "charInclusion"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 79, 2, "assertion", "charInclusion"));
		checkResult(expected, actual);
	}
	
	@Test
	public void stringPrefix() {
		SortedSet<Finding> actual = runAnalysis("stringPrefix");
		SortedSet<Finding> expected = new TreeSet<>();
		expected.add(new PossiblyFailingAssertFinding("strings.java", 5, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 7, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 8, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 9, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 10, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 20, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 32, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 44, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 59, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 70, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 71, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 73, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 74, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 77, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 78, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 79, 2, "assertion", "stringPrefix"));
		checkResult(expected, actual);
	}
	
	@Test
	public void stringSuffix() {
		SortedSet<Finding> actual = runAnalysis("stringSuffix");
		SortedSet<Finding> expected = new TreeSet<>();
		expected.add(new PossiblyFailingAssertFinding("strings.java", 5, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 7, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 8, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 9, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 10, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 20, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 32, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 44, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 59, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 70, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 71, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 72, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 73, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 74, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 77, 2, "assertion", "stringPrefix"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 78, 2, "assertion", "stringPrefix"));
		checkResult(expected, actual);
	}
	
	@Test
	public void bricks() {
		SortedSet<Finding> actual = runAnalysis("bricks");
		SortedSet<Finding> expected = new TreeSet<>();
		expected.add(new FailingAssertFinding("strings.java", 7, 2, "assertion", "bricks"));
		expected.add(new FailingAssertFinding("strings.java", 9, 2, "assertion", "bricks"));
		expected.add(new FailingAssertFinding("strings.java", 71, 2, "assertion", "bricks"));
		expected.add(new FailingAssertFinding("strings.java", 73, 2, "assertion", "bricks"));
		expected.add(new FailingAssertFinding("strings.java", 74, 2, "assertion", "bricks"));
		expected.add(new FailingAssertFinding("strings.java", 77, 2, "assertion", "bricks"));
		expected.add(new FailingAssertFinding("strings.java", 78, 2, "assertion", "bricks"));
		checkResult(expected, actual);
	}
	
	@Test
	public void string() {
		SortedSet<Finding> actual = runAnalysis("string");
		SortedSet<Finding> expected = new TreeSet<>();
		expected.add(new PossiblyFailingAssertFinding("strings.java", 7, 2, "assertion", "string"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 9, 2, "assertion", "string"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 20, 2, "assertion", "string"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 32, 2, "assertion", "string"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 71, 2, "assertion", "string"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 73, 2, "assertion", "string"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 74, 2, "assertion", "string"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 77, 2, "assertion", "string"));
		expected.add(new PossiblyFailingAssertFinding("strings.java", 78, 2, "assertion", "string"));
		checkResult(expected, actual);
	}
}
