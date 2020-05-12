package it.lucaneg.oo.parser;

public class ParsingException extends Exception {

	private static final long serialVersionUID = 4950907533241537847L;

	public ParsingException() {
		super();
	}

	public ParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParsingException(String message) {
		super(message);
	}

	public ParsingException(Throwable cause) {
		super(cause);
	}
}
