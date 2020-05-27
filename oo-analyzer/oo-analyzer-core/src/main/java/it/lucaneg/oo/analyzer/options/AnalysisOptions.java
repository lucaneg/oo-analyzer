package it.lucaneg.oo.analyzer.options;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import it.lucaneg.oo.sdk.analyzer.analyses.Analysis;
import it.lucaneg.oo.sdk.analyzer.checks.Check;

public class AnalysisOptions {
	private static final File BASE_OUTPUT_DIR = new File("analyses");
	
	private boolean dumpCallGraph;
	
	private boolean dumpSingleTrace;

	private File inputFolder;
	
	private File outputFolder;
	
	private final Set<Check> checks;

	private final Set<Analysis<?, ?>> analyses;

	public AnalysisOptions() {
		checks = new HashSet<>();
		analyses = new HashSet<>();

		do {
			outputFolder = new File(BASE_OUTPUT_DIR, UUID.randomUUID().toString());
		} while (outputFolder.exists());
	}

	public boolean dumpCallGraph() {
		return dumpCallGraph;
	}
	
	public boolean dumpSingleTrace() {
		return dumpSingleTrace;
	}

	public void setDumpCallGraph(boolean dumpCallGraph) {
		this.dumpCallGraph = dumpCallGraph;
	}
	
	public void setDumpSingleTrace(boolean dumpSingleTrace) {
		this.dumpSingleTrace = dumpSingleTrace;
	}

	public File getInputFolder() {
		return inputFolder;
	}

	public void setInputFolder(File inputFolder) {
		this.inputFolder = inputFolder;
	}

	public File getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(File outputFolder) {
		this.outputFolder = outputFolder;
	}

	public Set<Check> getChecks() {
		return checks;
	}

	public Set<Analysis<?, ?>> getAnalyses() {
		return analyses;
	}

	public String dump() {
		return "Analysis Options: "
				+ "\n\tinput folder: '" + inputFolder + "'"
				+ "\n\toutput folder: '" + outputFolder + "'"
				+ "\n\tdump call graph: " + dumpBoolean(dumpCallGraph)
				+ "\n\tdump single trace: " + dumpBoolean(dumpSingleTrace)
				+ "\n\tchecks to execute: [" + String.join(", ", checks.stream().map(Check::getName).toArray(String[]::new)) + "]"
				+ "\n\tanalyses to execute: [" + String.join(", ", analyses.stream().map(Analysis::getName).toArray(String[]::new)) + "]";
	}

	private static String dumpBoolean(boolean value) {
		return value ? "yes" : "no";
	}

	@Override
	public String toString() {
		return dump();
	}
}
