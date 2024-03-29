package it.lucaneg.oo.analyzer.tests.strings;

import java.util.List;

import org.junit.Test;

import it.lucaneg.oo.analyzer.core.JsonAnalysisReport;
import it.lucaneg.oo.analyzer.tests.AnalysisBasedTest;
import it.lucaneg.oo.sdk.analyzer.checks.Finding;

public class StringAnalysesBaseTest extends AnalysisBasedTest {
	
	@Test
	public void charInclusion() {
		List<Finding> actual = runAnalysis("charInclusion");
		JsonAnalysisReport actualReport = new JsonAnalysisReport(actual);
		JsonAnalysisReport expectedReport = parseReport("strings/charInclusion");		
		checkResult("charInclusion", expectedReport, actualReport);
	}
	
	@Test
	public void stringPrefix() {
		List<Finding> actual = runAnalysis("stringPrefix");
		JsonAnalysisReport actualReport = new JsonAnalysisReport(actual);
		JsonAnalysisReport expectedReport = parseReport("strings/stringPrefix");		
		checkResult("stringPrefix", expectedReport, actualReport);
	}
	
	@Test
	public void stringSuffix() {
		List<Finding> actual = runAnalysis("stringSuffix");
		JsonAnalysisReport actualReport = new JsonAnalysisReport(actual);
		JsonAnalysisReport expectedReport = parseReport("strings/stringSuffix");		
		checkResult("stringSuffix", expectedReport, actualReport);
	}
	
	@Test
	public void bricks() {
		List<Finding> actual = runAnalysis("bricks");
		JsonAnalysisReport actualReport = new JsonAnalysisReport(actual);
		JsonAnalysisReport expectedReport = parseReport("strings/bricks");		
		checkResult("bricks", expectedReport, actualReport);
	}
	
	@Test
	public void string() {
		List<Finding> actual = runAnalysis("string");
		JsonAnalysisReport actualReport = new JsonAnalysisReport(actual);
		JsonAnalysisReport expectedReport = parseReport("strings/string");		
		checkResult("string", expectedReport, actualReport);
	}
}
