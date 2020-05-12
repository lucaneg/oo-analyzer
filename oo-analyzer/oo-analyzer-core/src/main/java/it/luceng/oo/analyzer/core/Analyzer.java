package it.luceng.oo.analyzer.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.api.analyzer.analyses.Analysis;
import it.lucaneg.oo.api.analyzer.checks.Check;
import it.lucaneg.oo.api.analyzer.checks.Finding;
import it.luceng.oo.analyzer.analyses.AnalysisDumper;
import it.luceng.oo.analyzer.core.JsonAnalysisReport.JsonFinding;
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
			for (Analysis<?, ?> toRun : options.getAnalyses())
				toRun.run(modelBuilder.getProgram());
		});
		
		// we dump the results of the analyses
		logger.mkTimerLogger("Dumping analyses graphs").execAction(() -> {
			for (Analysis<?, ?> toRun : options.getAnalyses())
				AnalysisDumper.dumpDotFiles(modelBuilder.getProgram(), toRun, manager);
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
		
		try (Writer writer = manager.mkOutputFile("findings.json")) {
			ObjectMapper mapper = new ObjectMapper();
	        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	        
	        JsonAnalysisReport original = new JsonAnalysisReport();
	        for (Finding finding : collect)
	        	original.addFinding(new JsonFinding(finding.getFilename(), finding.getLine(), finding.getCol(), finding.getProducer(), finding.getMessage()));
	        
	        mapper.writeValue(writer, original);
		} catch (IOException e) {
			logger.error("Unable to dump findings file", e);
		}
		
		return ExitCode.SUCCESS;
	}
	
	public static void main(String[] args) throws JsonSyntaxException, JsonIOException, FileNotFoundException, JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        JsonAnalysisReport original = new JsonAnalysisReport();
        original.getFindings().add(new JsonFinding("a", 1, 2, "ff", "uuu"));
        original.getFindings().add(new JsonFinding("a", 1, 78, "fa;djsgfbaf", "adfadf"));
        original.getFindings().add(new JsonFinding("d", 44, 2, "ffdfaaf", "llllll"));
        original.getFindings().add(new JsonFinding("a", 15639, 2, "ffxxxx", "qpiruwt"));
        System.out.println(original);
        
        
        String str = mapper.writeValueAsString(original);
        System.out.println(str);
        
        JsonAnalysisReport readValue = mapper.readValue(str, JsonAnalysisReport.class);
		
		System.out.println(readValue);
	}
}
