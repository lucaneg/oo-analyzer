package it.lucaneg.oo.analyzer.tests.strings;

import java.util.List;

import org.junit.Test;

import it.lucaneg.oo.analyzer.tests.AnalysisBasedTest;
import it.lucaneg.oo.api.analyzer.checks.Finding;
import it.luceng.oo.analyzer.core.JsonAnalysisReport;

public class StringAnalysesBaseTest extends AnalysisBasedTest {
	
	@Test
	public void charInclusion() {
		List<Finding> actual = runAnalysis("charInclusion");
		JsonAnalysisReport actualReport = new JsonAnalysisReport(actual);
		JsonAnalysisReport expectedReport = parseReport("strings/charInclusion");		
		checkResult(expectedReport, actualReport);
	}
	
	@Test
	public void stringPrefix() {
		List<Finding> actual = runAnalysis("stringPrefix");
		JsonAnalysisReport actualReport = new JsonAnalysisReport(actual);
		JsonAnalysisReport expectedReport = parseReport("strings/stringPrefix");		
		checkResult(expectedReport, actualReport);
	}
	
	@Test
	public void stringSuffix() {
		List<Finding> actual = runAnalysis("stringSuffix");
		JsonAnalysisReport actualReport = new JsonAnalysisReport(actual);
		JsonAnalysisReport expectedReport = parseReport("strings/stringSuffix");		
		checkResult(expectedReport, actualReport);
	}
	
	@Test
	public void bricks() {
		List<Finding> actual = runAnalysis("bricks");
		JsonAnalysisReport actualReport = new JsonAnalysisReport(actual);
		JsonAnalysisReport expectedReport = parseReport("strings/bricks");		
		checkResult(expectedReport, actualReport);
	}
	
	@Test
	public void string() {
		List<Finding> actual = runAnalysis("string");
		JsonAnalysisReport actualReport = new JsonAnalysisReport(actual);
		JsonAnalysisReport expectedReport = parseReport("strings/string");		
		checkResult(expectedReport, actualReport);
	}
}
