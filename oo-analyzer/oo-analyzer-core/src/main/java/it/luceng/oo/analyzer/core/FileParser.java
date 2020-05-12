package it.luceng.oo.analyzer.core;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.api.analyzer.program.Program;
import it.lucaneg.oo.ast.ClassDefinition;
import it.lucaneg.oo.parser.OOParser;
import it.lucaneg.oo.parser.ParsingException;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import it.luceng.oo.analyzer.options.AnalysisOptions;

public class FileParser {
	private static final String STRING_CLASS_FILE = Program.STRING_CLASS + ".java";

	private static final String OBJECT_CLASS_FILE = Program.OBJECT_CLASS + ".java";

	private static final EnrichedLogger logger = new EnrichedLogger(FileParser.class);

	private final AnalysisOptions options;
	
	private final Collection<ClassDefinition> classes;

	public FileParser(AnalysisOptions options) {
		this.options = options;
		this.classes =  new HashSet<>();
	}
	
	public Collection<ClassDefinition> getParsedClasses() {
		return classes;
	}

	public ExitCode parseInputs() {
		File[] allFiles = options.getInputFolder().listFiles(f -> f.isFile() && f.getName().endsWith(".java"));

		ExitCode ret;
		if ((ret = ensureAtLeastOneInputFile(allFiles)) != ExitCode.SUCCESS)
			return ret;

		boolean needsString = true, needsObject = true;
		for (File input : allFiles)
			if (input.getName().equals(STRING_CLASS_FILE))
				needsString = false;
			else if (input.getName().equals(OBJECT_CLASS_FILE))
				needsObject = false;

		// we add string and object since they could be needed by the classes
		if (needsObject && (ret = addToTheAnalysis(OBJECT_CLASS_FILE)) != ExitCode.SUCCESS)
			return ret;
		else 
			allFiles = ArrayUtils.add(allFiles, new File(options.getInputFolder(), OBJECT_CLASS_FILE));
		if (needsString && (ret = addToTheAnalysis(STRING_CLASS_FILE)) != ExitCode.SUCCESS)
			return ret;
		else 
			allFiles = ArrayUtils.add(allFiles, new File(options.getInputFolder(), STRING_CLASS_FILE));

		ret = parseFiles(allFiles);

		// we clean up the input folder from what we have added
		if (needsObject)
			removeFromInputs(OBJECT_CLASS_FILE);
		if (needsString)
			removeFromInputs(STRING_CLASS_FILE);

		return ret;
	}

	private ExitCode ensureAtLeastOneInputFile(File[] allFiles) {
		if (allFiles == null || allFiles.length == 0) {
			logger.warn("No input files (*.java) found in " + options.getInputFolder());
			return ExitCode.NO_INPUTS;
		}
		
		return ExitCode.SUCCESS;
	}

	private ExitCode addToTheAnalysis(String name) {
		logger.info(name + " not found in the input folder. The standard implementation will be added to the analysis");

		File sourceFile = new File("oo-lib/" + name);
		File destinationFile = new File(options.getInputFolder(), name);

		try {
			FileUtils.copyFile(sourceFile, destinationFile);
		} catch (IOException e) {
			logger.error("Failure while adding " + name + " to the analysis", e);
			return ExitCode.SETUP_ERROR;
		}

		return ExitCode.SUCCESS;
	}
	
	private ExitCode parseFiles(File[] allFiles) {
		return logger.mkTimerLogger("Parsing input files").execSupplier(() -> {
			try {
				classes.addAll(OOParser.parseFiles(allFiles));
			} catch (ParsingException | TypeCheckException e) {
				logger.error("Error while parsing input files", e);
				return ExitCode.PARSE_ERROR;
			}
			
			return ExitCode.SUCCESS;
		});
	}

	private void removeFromInputs(String name) {
		File toRemove = new File(options.getInputFolder(), name);

		try {
			FileUtils.forceDelete(toRemove);
		} catch (IOException e) {
			logger.warn("Unable to remove " + name + " (added before parsing) from the input folder", e);
		}
	}
}
