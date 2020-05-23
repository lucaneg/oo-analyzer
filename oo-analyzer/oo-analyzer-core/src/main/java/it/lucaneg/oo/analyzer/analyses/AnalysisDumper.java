package it.lucaneg.oo.analyzer.analyses;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.analyzer.core.FileManager;
import it.lucaneg.oo.sdk.analyzer.analyses.Analysis;
import it.lucaneg.oo.sdk.analyzer.analyses.Denotation;
import it.lucaneg.oo.sdk.analyzer.program.MCodeMember;
import it.lucaneg.oo.sdk.analyzer.program.Program;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

public class AnalysisDumper {

	private static final EnrichedLogger logger = new EnrichedLogger(AnalysisDumper.class);

	/**
	 * Dumps the denotations computed by the given analysis in dot files.
	 * 
	 * @param program  the program under analysis
	 * @param analysis the analysis to dump
	 * @param manager  the manager that knows where and how to create files
	 */
	public static final void dumpDotFiles(Program program, Analysis<?, ?> analysis, FileManager manager) {
		logger.mkTimerLogger("Dumpig analysis results").execAction(() -> {
			for (MCodeMember code : program.getAllCodeMembers())
				if (code.getDefiningClass().isObject() || code.getDefiningClass().isString()) 
					continue;
				else 
					try (Writer writer = manager.mkOutputFile(analysis.getName() + "-" + code.toStringForFileName() + ".dot", true)) {
						Denotation<?, ?> denotation = analysis.of(code);
						code.getCode().dump(writer, analysis.getName(), st -> sortAndDump(denotation, st));
					} catch (IOException e) {
						logger.error("Unable to dump cfg-" + code.toStringForFileName() + ".dot", e);
					}
		});
	}

	private static String sortAndDump(Denotation<?, ?> denotation, Statement st) {
		if (!denotation.hasEnvironmentFor(st))
			return "unreahcable code";
		List<?> sorted = denotation.at(st).entrySet()
				.stream()
				.sorted((e1, e2) -> e1.getKey().toString().compareTo(e2.getKey().toString()))
				.collect(Collectors.toList());
		return StringUtils.join(sorted, "\n");
	}
}
