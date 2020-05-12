package it.luceng.oo.analyzer.core;

import java.util.ArrayList;
import java.util.List;

import it.lucaneg.oo.api.analyzer.checks.Finding;
import lombok.Getter;
import lombok.Setter;

public class JsonAnalysisReport {

	@Getter
	private final List<JsonFinding> findings;
	
	public JsonAnalysisReport() {
		this.findings = new ArrayList<>();
	}
	
	public JsonAnalysisReport(List<Finding> findings) {
		this();
        for (Finding finding : findings)
        	this.findings.add(new JsonFinding(finding.getFilename(), finding.getLine(), finding.getCol(), finding.getProducer(), finding.getMessage()));
	}
	
	public void addFinding(JsonFinding finding) {
		this.findings.add(finding);
	}
	
	@Override
	public String toString() {
		return "JsonAnalysisReport [findings=" + findings + "]";
	}

	@Getter
	@Setter
	public static class JsonFinding implements Comparable<JsonFinding> {
		private String filename;

		private int line;

		private int col;

		private String producer;
		
		private String message;

		public JsonFinding() {}
		
		public JsonFinding(String filename, int line, int col, String producer, String message) {
			this.filename = filename;
			this.line = line;
			this.col = col;
			this.producer = producer;
			this.message = message;
		}
		
		@Override
		public final int compareTo(JsonFinding o) {
			int cmp;

			if ((cmp = filename.compareTo(o.filename)) != 0)
				return cmp;

			if ((cmp = line - o.line) != 0)
				return cmp;

			if ((cmp = col - o.col) != 0)
				return cmp;

			if ((cmp = producer.compareTo(o.producer)) != 0)
				return cmp;

			return message.compareTo(o.message);
		}
		
		@Override
		public String toString() {
			return "JsonFinding [filename=" + filename + ", line=" + line + ", col=" + col + ", producer=" + producer
					+ ", message=" + message + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + col;
			result = prime * result + ((filename == null) ? 0 : filename.hashCode());
			result = prime * result + line;
			result = prime * result + ((message == null) ? 0 : message.hashCode());
			result = prime * result + ((producer == null) ? 0 : producer.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			JsonFinding other = (JsonFinding) obj;
			if (col != other.col)
				return false;
			if (filename == null) {
				if (other.filename != null)
					return false;
			} else if (!filename.equals(other.filename))
				return false;
			if (line != other.line)
				return false;
			if (message == null) {
				if (other.message != null)
					return false;
			} else if (!message.equals(other.message))
				return false;
			if (producer == null) {
				if (other.producer != null)
					return false;
			} else if (!producer.equals(other.producer))
				return false;
			return true;
		}
	}
}
