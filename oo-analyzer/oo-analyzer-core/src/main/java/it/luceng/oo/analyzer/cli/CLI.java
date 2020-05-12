package it.luceng.oo.analyzer.cli;

import java.io.File;
import java.net.URL;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import it.lucaneg.logutils.EnrichedLogger;
import it.luceng.oo.analyzer.analyses.AnalysisFactory;
import it.luceng.oo.analyzer.checks.ChecksFactory;
import it.luceng.oo.analyzer.core.Analyzer;
import it.luceng.oo.analyzer.core.ExitCode;
import it.luceng.oo.analyzer.options.AnalysisOptions;

public class CLI {
	
	private static final EnrichedLogger logger = new EnrichedLogger(CLI.class);
	
	private static final Option HELP;
	private static final Option CALLGRAPH;
	private static final Option INPUT;
	private static final Option OUTPUT;
	private static final Option CHECKS;
	private static final Option ANALYSES;
	
	static {
		HELP = Option.builder("h")
					.longOpt("help")
					.desc("Displays this help")
					.build();
		INPUT = Option.builder("i")
					.longOpt("input-folder")
					.desc("[REQUIRED] Sets the input folder containing the files to analyze")
					//.required() otherwise we get an exception even with -h
					.hasArg()
					.argName("folder")
					.build();
		OUTPUT = Option.builder("o")
					.longOpt("output-folder")
					.desc("Sets the output folder where all files will be dumped")
					.hasArg()
					.argName("folder")
					.build();
		CALLGRAPH = Option.builder("cg")
						.longOpt("dump-call-graph")
						.desc("If set, the analyzer will dump the call graph of each method")
						.build();
		CHECKS = Option.builder("c")
				.longOpt("checks")
				.desc("Sets the checks to be executed. Possible values:\n- " + String.join("\n- ", ChecksFactory.getAllInstancesNames()))
				.hasArgs()
				.argName("check")
				.build();
		ANALYSES = Option.builder("a")
				.longOpt("analyses")
				.desc("Sets the analyses to be executed. Possible values:\n- " + String.join("\n- ", AnalysisFactory.getAllInstancesNames()))
				.hasArgs()
				.argName("analysis")
				.build();
	}

	public static void main(String[] args) {
		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);

		if(context.getConfiguration() instanceof DefaultConfiguration) {
			try {
				// Force a reconfiguration using the embedded configuration file
				URL internalLogConfigFile = CLI.class.getResource("/log4j-default.xml");
				context.setConfigLocation(internalLogConfigFile.toURI());
			} catch (Exception e) {
				System.err.println("Unable to reconfigure logging system using embedded configuration");
			}
		}		
		
		Options options = new Options();
		options.addOption(INPUT);
		options.addOption(CALLGRAPH);
		options.addOption(HELP);
		options.addOption(OUTPUT);
		options.addOption(CHECKS);
		options.addOption(ANALYSES);
		
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
		
		if (!cmd.hasOption(INPUT.getLongOpt())) {
			printUsage(options);
			logger.error("Skipping further processing since option --" + INPUT.getLongOpt() + " was not given");
			return;
		}
		
		Analyzer analyzer = logger.mkTimerLogger("Creating the analyzer").execFunction(CLI::createAnalyzer, cmd);
		ExitCode exit = logger.mkTimerLogger("Overall Analysis").execSupplier(analyzer::run);
		
		logger.info("Analysis completed with status: " + exit.getDescription());
		logger.info("Outputs have been dumped to: " + analyzer.getOptions().getOutputFolder());
		System.exit(exit.getCode());
	}

	private static Analyzer createAnalyzer(CommandLine cmd) {
		AnalysisOptions opt = new AnalysisOptions();
		opt.setInputFolder(new File(cmd.getOptionValue(INPUT.getLongOpt())));
		if (cmd.hasOption(CALLGRAPH.getLongOpt()))
			opt.setDumpCallGraph(true);
		if (cmd.hasOption(OUTPUT.getLongOpt()))
			opt.setOutputFolder(new File(cmd.getOptionValue(OUTPUT.getLongOpt())));
		for (String check : cmd.getOptionValues(CHECKS.getLongOpt()))
			opt.getChecks().add(ChecksFactory.getInstance(check));
		for (String analysis : cmd.getOptionValues(ANALYSES.getLongOpt()))
			opt.getAnalyses().add(AnalysisFactory.getInstance(analysis));
		
		logger.info(opt.dump());
		return new Analyzer(opt);
	}
	
	private static void printUsage(Options options) {
		HelpFormatter hf = new HelpFormatter();
		hf.printHelp(CLI.class.getSimpleName() + " [OPTIONS]", options);
	}
}
