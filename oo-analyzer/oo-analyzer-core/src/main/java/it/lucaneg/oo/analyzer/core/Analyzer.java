package it.lucaneg.oo.analyzer.core;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.analyzer.analyses.AnalysisDumper;
import it.lucaneg.oo.analyzer.options.AnalysisOptions;
import it.lucaneg.oo.sdk.analyzer.analyses.Analysis;
import it.lucaneg.oo.sdk.analyzer.checks.Check;
import it.lucaneg.oo.sdk.analyzer.checks.Finding;
import it.lucaneg.oo.sdk.analyzer.program.Program;

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
			for (Analysis<?, ?> toRun : options.getAnalyses())
				toRun.run(modelBuilder.getProgram());
		});
		
		postAnalyses(modelBuilder.getProgram());
			
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
		
		postChecks(collect);
		
		return ExitCode.SUCCESS;
	}

	protected void postChecks(List<Finding> findings) {
		try (Writer writer = manager.mkOutputFile("findings.json")) {
			ObjectMapper mapper = new ObjectMapper();
	        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	        JsonAnalysisReport report = new JsonAnalysisReport(findings);
	        mapper.writeValue(writer, report);
		} catch (IOException e) {
			logger.error("Unable to dump findings file", e);
		}
	}

	protected void postAnalyses(Program program) {
		// we dump the results of the analyses
		logger.mkTimerLogger("Dumping analyses graphs").execAction(() -> {
			for (Analysis<?, ?> toRun : options.getAnalyses())
				AnalysisDumper.dumpDotFiles(program, toRun, manager);
		});
	}
}
