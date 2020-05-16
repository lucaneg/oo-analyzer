package it.lucaneg.oo.analyzer.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.lucaneg.oo.analyzer.analyses.AnalysisFactory;
import it.lucaneg.oo.analyzer.checks.ChecksFactory;
import it.lucaneg.oo.analyzer.core.ExitCode;
import it.lucaneg.oo.analyzer.core.JsonAnalysisReport;
import it.lucaneg.oo.analyzer.options.AnalysisOptions;
import it.lucaneg.oo.sdk.analyzer.checks.Finding;

public abstract class AnalysisBasedTest {
	protected List<Finding> runAnalysis(String analysis) {
		AnalysisOptions options = new AnalysisOptions();
		options.setInputFolder(new File("oo-inputs/strings")); 
		options.getAnalyses().add(AnalysisFactory.getInstance(analysis));
		options.getChecks().add(ChecksFactory.getInstance("assertion"));
		TestAnalyzer analyzer = new TestAnalyzer(options);
		
		assertEquals("Analyzer did not exit with a success exit code", ExitCode.SUCCESS, analyzer.run());
		
		return analyzer.getFindings();
	}
	
	protected void checkResult(String label, JsonAnalysisReport expectedReport, JsonAnalysisReport actualReport) { 
		FindingsDiffBuilder diff = new FindingsDiffBuilder(expectedReport.getFindings(), actualReport.getFindings());
		diff.computeDiff((f1, f2) -> f1.compareTo(f2));
		diff.printDiff(label, true);
			
		
		assertTrue("Missing some expected findings", diff.getOnlyExpected().isEmpty());
		assertTrue("Found new findings", diff.getOnlyActual().isEmpty());
	}
	
	protected JsonAnalysisReport parseReport(String name) {
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
			return mapper.readValue(this.getClass().getResource("/expected/" + name + ".json"), JsonAnalysisReport.class);
		} catch (IOException e) {
			fail("Unable to load expected results: " + name + ".json"); 
			return null;
		}
	}
}
