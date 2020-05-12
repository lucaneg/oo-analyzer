package it.lucaneg.logutils.iterations;

public interface ITicker {
	public int getTicks();

	public void on();

	public void tick();

	public void off();
}