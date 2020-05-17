package it.lucaneg.oo.analyzer.cli;

public interface Configurable {

	void setup(String... parameters) throws SetupException;
}
