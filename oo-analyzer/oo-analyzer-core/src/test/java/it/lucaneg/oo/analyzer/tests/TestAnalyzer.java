package it.lucaneg.oo.analyzer.tests;

import java.net.URL;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import it.lucaneg.oo.analyzer.cli.CLI;
import it.lucaneg.oo.analyzer.core.Analyzer;
import it.lucaneg.oo.analyzer.options.AnalysisOptions;
import it.lucaneg.oo.sdk.analyzer.checks.Finding;
import it.lucaneg.oo.sdk.analyzer.program.Program;

public class TestAnalyzer extends Analyzer {
	
	static {
		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);

		if(context.getConfiguration() instanceof DefaultConfiguration) {
			try {
				// Force a reconfiguration using the embedded configuration file
				URL internalLogConfigFile = CLI.class.getResource("/log4j-test.xml");
				context.setConfigLocation(internalLogConfigFile.toURI());
			} catch (Exception e) {
				System.err.println("Unable to reconfigure logging system using embedded configuration");
			}
		}	
	}
	
	private List<Finding> findings;
	
	public TestAnalyzer(AnalysisOptions options) {
		super(options);
	}
	
	@Override
	protected void postAnalyses(Program program) {
		// do nothing: we do not dump graphs
	}
	
	@Override
	protected void postChecks(List<Finding> findings) {
		this.findings = findings;
	}
	
	public List<Finding> getFindings() {
		return findings;
	}
}
