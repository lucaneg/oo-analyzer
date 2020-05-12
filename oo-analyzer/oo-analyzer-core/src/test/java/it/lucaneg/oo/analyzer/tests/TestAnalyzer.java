package it.lucaneg.oo.analyzer.tests;

import java.net.URL;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.api.analyzer.analyses.Analysis;
import it.lucaneg.oo.api.analyzer.checks.Check;
import it.lucaneg.oo.api.analyzer.checks.Finding;
import it.luceng.oo.analyzer.cli.CLI;
import it.luceng.oo.analyzer.core.ExitCode;
import it.luceng.oo.analyzer.core.FileParser;
import it.luceng.oo.analyzer.core.ModelBuilder;
import it.luceng.oo.analyzer.options.AnalysisOptions;

public class TestAnalyzer {
	
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
	
	private static final EnrichedLogger logger = new EnrichedLogger(TestAnalyzer.class);
	
	private final AnalysisOptions options;
	
	private final SortedSet<Finding> findings;
	
	public TestAnalyzer(AnalysisOptions options) {
		this.options = options;
		this.findings = new TreeSet<>();
	}
	
	public AnalysisOptions getOptions() {
		return options;
	}
	
	public ExitCode run() {
		// first, we parse the code
		FileParser fileParser = new FileParser(options);
		ExitCode ret = fileParser.parseInputs();
		if (ret != ExitCode.SUCCESS)
			return ret;
		
		// then, we build the model
		ModelBuilder modelBuilder = new ModelBuilder();
		ret = modelBuilder.buildModel(fileParser.getParsedClasses());
		if (ret != ExitCode.SUCCESS)
			return ret;
		
		// then, we run the analyses
		logger.mkTimerLogger("Executing analyses").execAction(() -> {
			for (Analysis<?, ?> toRun : options.getAnalyses()) {
				toRun.run(modelBuilder.getProgram());
			}
		});
			
		
		// at last, we run the checks
		logger.mkTimerLogger("Executing checks").execAction(() -> {
			for (Check toRun : options.getChecks()) 
				toRun.run(modelBuilder.getProgram(), options.getAnalyses());
		});
		
		// time to dump the findings
		Set<Finding> collect = options.getChecks()
				.stream()
				.filter(Check::foundSomething)
				.flatMap(c -> c.getFindings().stream())
				.sorted()
				.collect(Collectors.toSet());
		if (!collect.isEmpty())
			logger.info("There are " + collect.size() + " findings:\n\n" + StringUtils.join(collect, "\n") + "\n");
		else 
			logger.info("None of the executed checks found something");
		
		findings.addAll(collect);
		
		return ExitCode.SUCCESS;
	}
	
	public SortedSet<Finding> getFindings() {
		return findings;
	}
}
