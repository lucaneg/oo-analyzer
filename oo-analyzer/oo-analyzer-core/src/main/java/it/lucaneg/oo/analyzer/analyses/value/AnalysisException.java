package it.lucaneg.oo.analyzer.analyses.value;

public class AnalysisException extends Exception {

	private static final long serialVersionUID = -3529621819343140026L;

	public AnalysisException() {
		super();
	}

	public AnalysisException(String message, Throwable cause) {
		super(message, cause);
	}

	public AnalysisException(String message) {
		super(message);
	}

	public AnalysisException(Throwable cause) {
		super(cause);
	}

}
