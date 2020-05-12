package it.lucaneg.oo.analyzer.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import it.lucaneg.oo.analyzer.options.AnalysisOptions;

public class FileManager {
	
	private final AnalysisOptions options;

	public FileManager(AnalysisOptions options) {
		this.options = options;
	}

	public Writer mkOutputFile(String name) throws IOException {
		return mkOutputFile(name, false);
	}
	
	public Writer mkOutputFile(String name, boolean bom) throws IOException {
		File file = new File(options.getOutputFolder(), name);
		
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		
		FileWriter writer = new FileWriter(file);
		if (bom) 
			writer.write('\ufeff');

		return writer;
	}
}
