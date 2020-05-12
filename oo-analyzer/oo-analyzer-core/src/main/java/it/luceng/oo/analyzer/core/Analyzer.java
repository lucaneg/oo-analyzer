package it.luceng.oo.analyzer.core;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.api.analyzer.analyses.Analysis;
import it.lucaneg.oo.api.analyzer.checks.Check;
import it.lucaneg.oo.api.analyzer.checks.Finding;
import it.luceng.oo.analyzer.analyses.AnalysisDumper;
import it.luceng.oo.analyzer.options.AnalysisOptions;

public class Analyzer {
	
	private static final EnrichedLogger logger = new EnrichedLogger(Analyzer.class);
	
	private final AnalysisOptions options;
	
	private final FileManager manager;

	public Analyzer(AnalysisOptions options) {
		this.options = options;
		this.manager = new FileManager(options);
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
		
		if (options.dumpCallGraph())
			// we dump the first output: the cfg that we built
			modelBuilder.dumpDotFiles(manager);
		
		// then, we run the analyses
		logger.mkTimerLogger("Executing analyses").execAction(() -> {
			for (Analysis<?, ?> toRun : options.getAnalyses()) {
				toRun.run(modelBuilder.getProgram());
				AnalysisDumper.dumpDotFiles(modelBuilder.getProgram(), toRun, manager);
			}
		});
			
		
		// at last, we run the checks
		logger.mkTimerLogger("Executing checks").execAction(() -> {
			for (Check toRun : options.getChecks()) 
				toRun.run(modelBuilder.getProgram(), options.getAnalyses());
		});
		
		// time to dump the findings
		List<Finding> collect = options.getChecks()
				.stream()
				.filter(Check::foundSomething)
				.flatMap(c -> c.getFindings().stream())
				.sorted()
				.collect(Collectors.toList());
		if (!collect.isEmpty())
			logger.info("There are " + collect.size() + " findings:\n\n" + StringUtils.join(collect, "\n") + "\n");
		else 
			logger.info("None of the executed checks found something");
		
		return ExitCode.SUCCESS;
	}
}
