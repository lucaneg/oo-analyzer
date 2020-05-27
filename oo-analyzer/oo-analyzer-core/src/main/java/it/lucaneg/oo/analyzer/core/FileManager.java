package it.lucaneg.oo.analyzer.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

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
		
		Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8.newEncoder());
		if (bom) 
			writer.write('\ufeff');

		return writer;
	}
}
