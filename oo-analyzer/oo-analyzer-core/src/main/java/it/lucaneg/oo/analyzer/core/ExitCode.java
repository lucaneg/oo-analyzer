package it.lucaneg.oo.analyzer.core;

public enum ExitCode {
	SUCCESS(0, "Success"),
	PARSE_ERROR(-1, "Error while parsing an input file"),
	NO_INPUTS(-2, "No input files found"),
	SETUP_ERROR(-3, "Error during the setup of the analysis");
	
	private final int code;
	private final String description;
	
	private ExitCode(int code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}
}
