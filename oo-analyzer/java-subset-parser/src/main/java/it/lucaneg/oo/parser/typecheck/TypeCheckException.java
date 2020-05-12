package it.lucaneg.oo.parser.typecheck;

public class TypeCheckException extends Exception {

	private static final long serialVersionUID = -3927024752231751565L;

	public TypeCheckException() {
		super();
	}

	public TypeCheckException(String message, Throwable cause) {
		super(message, cause);
	}

	public TypeCheckException(String message) {
		super(message);
	}

	public TypeCheckException(Throwable cause) {
		super(cause);
	}
}
