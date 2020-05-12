package it.luceng.oo.analyzer.analyses;

import java.io.IOException;
import java.io.Writer;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.api.analyzer.analyses.Analysis;
import it.lucaneg.oo.api.analyzer.analyses.Denotation;
import it.lucaneg.oo.api.analyzer.program.MCodeMember;
import it.lucaneg.oo.api.analyzer.program.Program;
import it.luceng.oo.analyzer.core.FileManager;

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
						code.getCode().dump(writer, analysis.getName(), st -> denotation.at(st).toString());
					} catch (IOException e) {
						logger.error("Unable to dump cfg-" + code.toStringForFileName() + ".dot", e);
					}
		});
	}
}
