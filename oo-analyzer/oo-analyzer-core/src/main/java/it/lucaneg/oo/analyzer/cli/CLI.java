package it.lucaneg.oo.analyzer.cli;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Option.Builder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.analyzer.analyses.AnalysisFactory;
import it.lucaneg.oo.analyzer.checks.ChecksFactory;
import it.lucaneg.oo.analyzer.core.Analyzer;
import it.lucaneg.oo.analyzer.core.ExitCode;
import it.lucaneg.oo.analyzer.options.AnalysisOptions;
import it.lucaneg.oo.sdk.analyzer.analyses.Analysis;

public class CLI {
	
	private static final EnrichedLogger logger = new EnrichedLogger(CLI.class);
	
	private static Option HELP;
	private static Option CALLGRAPH;
	private static Option INPUT;
	private static Option OUTPUT;
	private static Option CHECKS;

	public static void main(String[] args) {
		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);

		if (context.getConfiguration() instanceof DefaultConfiguration) {
			// Force a reconfiguration using the embedded configuration file
			try {
				URL internalLogConfigFile = CLI.class.getResource("/log4j-default.xml");
				if (internalLogConfigFile != null)
					context.setConfigLocation(internalLogConfigFile.toURI());
				else 
					try (InputStream internalLogConfigStream = CLI.class.getResourceAsStream("/log4j-default.xml")) {
						context.setConfiguration(new XmlConfiguration(context, new ConfigurationSource(internalLogConfigStream)));
					}
			} catch (Exception e) {
				System.err.println("Unable to reconfigure logging system using embedded configuration");
			}
		}		
		
		Options options = new Options(); 
		Map<Option, String> analyses = fillOptions(options);
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			printUsage(options);
			logger.error("Skipping further processing due to: " + e, e);
			return;
		}
		
		if (cmd.hasOption(HELP.getLongOpt())) {
			printUsage(options);
			logger.info("Skipping further processing since option --" + HELP.getLongOpt() + " was given");
			return;
		}
		
		if (!cmd.getArgList().isEmpty()) {
			printUsage(options);
			logger.error("Arguments are not supported: " + cmd.getArgList());
			return;
		}
		
		if (!cmd.hasOption(INPUT.getLongOpt())) {
			printUsage(options);
			logger.error("Skipping further processing since option --" + INPUT.getLongOpt() + " was not given");
			return;
		}
		
		Analyzer analyzer = logger.mkTimerLogger("Creating the analyzer").execFunction(CLI::createAnalyzer, cmd, analyses);
		ExitCode exit = logger.mkTimerLogger("Overall Analysis").execSupplier(analyzer::run);
		
		logger.info("Analysis completed with status: " + exit.getDescription());
		logger.info("Outputs have been dumped to: " + analyzer.getOptions().getOutputFolder());
		System.exit(exit.getCode());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<Option, String> fillOptions(Options options) {
		Map<Option, String> analyses = new IdentityHashMap<>();
		options.addOption(INPUT = Option.builder("i")
				.longOpt("input-folder")
				.desc("[REQUIRED] Sets the input folder containing the files to analyze")
				.hasArg()
				.argName("folder")
				.build());
		options.addOption(CALLGRAPH = Option.builder("cg")
				.longOpt("dump-call-graph")
				.desc("If set, the analyzer will dump the call graph of each method")
				.build());
		options.addOption(HELP = Option.builder("h")
				.longOpt("help")
				.desc("Displays this help")
				.build());
		options.addOption(OUTPUT = Option.builder("o")
				.longOpt("output-folder")
				.desc("Sets the output folder where all files will be dumped")
				.hasArg()
				.argName("folder")
				.build());
		options.addOption(CHECKS = Option.builder("c")
				.longOpt("checks")
				.desc("Sets the checks to be executed")
				.hasArgs()
				.argName("check")
				.build());
		
		for (String name : AnalysisFactory.getAllInstancesNames()) {
			Builder builder = Option.builder().longOpt(name);
			
			String desc = "Excecutes the " + name + " analysis";
			Class<?> clazz = AnalysisFactory.getClassOf(name);
			if (clazz.isAnnotationPresent(RequiresParameters.class)) {
				Parameter[] annotations = clazz.getAnnotation(RequiresParameters.class).value();
				int params = annotations.length;
				desc += ". Parameters, in order:\n";
				for (int i = 0; i < params; i++) {
					desc += "- '" + annotations[i].name() + "': " + annotations[i].description();
					Class root = annotations[i].provider();
					Reflections reflections = new Reflections(root.getPackageName(), new SubTypesScanner());
					Set<Class<?>> instances = reflections.getSubTypesOf(root);
					Set<String> names = instances.stream()
							.filter(c -> !Modifier.isAbstract(c.getModifiers()) && !c.isAnonymousClass())
							.map(c -> c.getSimpleName())
							.collect(Collectors.toSet());  
					desc += " [" + StringUtils.join(names, ", ") + "]\n";
				}
				builder.numberOfArgs(params)
					.argName("parameters")
					.desc(desc.trim());
			} else 
				builder.desc(desc);

			Option opt = builder.build();
			options.addOption(opt);
			analyses.put(opt, name);
		}
		
		
		return analyses;
	}

	private static Analyzer createAnalyzer(CommandLine cmd, Map<Option, String> analyses) {
		AnalysisOptions opt = new AnalysisOptions();
		opt.setInputFolder(new File(cmd.getOptionValue(INPUT.getLongOpt())));
		if (cmd.hasOption(CALLGRAPH.getLongOpt()))
			opt.setDumpCallGraph(true);
		if (cmd.hasOption(OUTPUT.getLongOpt()))
			opt.setOutputFolder(new File(cmd.getOptionValue(OUTPUT.getLongOpt())));
		for (String check : cmd.getOptionValues(CHECKS.getLongOpt()))
			opt.getChecks().add(ChecksFactory.getInstance(check));
		for (Entry<Option, String> analysis : analyses.entrySet())
			if (cmd.hasOption(analysis.getKey().getLongOpt())) {
				try {
					Analysis<?, ?> instance = AnalysisFactory.getInstance(analysis.getValue());
					if (instance instanceof Configurable)
						((Configurable) instance).setup(cmd.getOptionValues(analysis.getKey().getLongOpt()));
					opt.getAnalyses().add(instance);
				} catch (SetupException e) {
					logger.error("Unable to configure analysis " + analysis.getValue() + ", it won't be executed", e);
				}
			}
		
		logger.info(opt.dump());
		return new Analyzer(opt);
	}
	
	private static void printUsage(Options options) {
		HelpFormatter hf = new HelpFormatter();
		String checks = "Available checks:\n- " + String.join("\n- ", ChecksFactory.getAllInstancesNames());
		hf.printHelp(400, CLI.class.getSimpleName() + " [OPTIONS]", "OO analyzer - a simple analyzer for a subset of java", options, checks);
	}
}
