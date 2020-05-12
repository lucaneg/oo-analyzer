package it.lucaneg.oo.api.analyzer.checks;

import lombok.Getter;

/**
 * A finding is a problem discovered by a check in the program to analyze.
 * 
 * @author Luca Negrini
 */
@Getter
public abstract class Finding implements Comparable<Finding> {

	private final String filename;

	private final int line;

	private final int col;

	private final String producer;

	/**
	 * Builds the finding.
	 * 
	 * @param filename the name of the file where the finding is raised
	 * @param line     the line where the finiding is raised
	 * @param col      the column where the finding is raised
	 * @param producer the name of the check that issued the finding
	 */
	protected Finding(String filename, int line, int col, String producer) {
		this.filename = filename;
		this.line = line;
		this.col = col;
		this.producer = producer;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getClass().getName().hashCode();
		result = prime * result + col;
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + line;
		result = prime * result + ((producer == null) ? 0 : producer.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Finding other = (Finding) obj;
		if (col != other.col)
			return false;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (line != other.line)
			return false;
		if (producer == null) {
			if (other.producer != null)
				return false;
		} else if (!producer.equals(other.producer))
			return false;
		return true;
	}

	@Override
	public final String toString() {
		return filename + ":" + line + ":" + col + " - [" + producer + "] " + getMessage();
	}

	/**
	 * Yields the message of the finding.
	 * 
	 * @return the message
	 */
	public abstract String getMessage();

	@Override
	public final int compareTo(Finding o) {
		int cmp;

		if ((cmp = filename.compareTo(o.filename)) != 0)
			return cmp;

		if ((cmp = line - o.line) != 0)
			return cmp;

		if ((cmp = col - o.col) != 0)
			return cmp;

		if ((cmp = producer.compareTo(o.producer)) != 0)
			return cmp;

		if ((cmp = getClass().getName().compareTo(o.getClass().getName())) != 0)
			return cmp;

		return compareToAux(o);
	}

	/**
	 * Compares this finding with the given one, assuming that both are of the same
	 * class, issued by the same check and on the same program point.
	 * 
	 * @param o the other finding
	 * @return -1, 0 or 1
	 */
	protected abstract int compareToAux(Finding o);
}
