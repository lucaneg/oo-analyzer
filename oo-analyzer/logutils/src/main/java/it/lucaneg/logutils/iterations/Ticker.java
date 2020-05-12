package it.lucaneg.logutils.iterations;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import it.lucaneg.logutils.timings.TimeFormat;

public final class Ticker implements ITicker {
	private boolean on;
	private int ticks;
	private final int expectedTicks, updateEvery;
	private final Level level;
	
	private long startTime;
	
	private final String message, objects;
	private final Logger logger;

	Ticker(Logger logger, Level level, String message, String objects) {
		this(logger, level, message, objects, -1);
	}

	Ticker(Logger logger, Level level, String message, String objects, int expectedTicks) {
		this(logger, level, message, objects, expectedTicks, -1);
	}

	Ticker(Logger logger, Level level, String message, String objects, int expectedTicks, double updatePercentage) {
		this.level = level;
		on = false;
		ticks = 0;
		this.expectedTicks = expectedTicks;
		updateEvery = updatePercentage < 0 || expectedTicks < 1 ? 1
				: Math.max((int) Math.floor(expectedTicks * updatePercentage), 1);
		
		this.logger = logger;
		this.message = message;
		this.objects = objects;
	}

	@Override
	public final int getTicks() {
		return ticks;
	}

	@Override
	public final void on() {
		if (on)
			throw new UnsupportedOperationException("This ticker is already on");
		on = true;
		startTime = System.nanoTime();
		logger.log(level, message + " [start]");
	}

	@Override
	public final synchronized void tick() {
		ticks++;
		if (on)
			step();
	}

	@Override
	public final void off() {
		if (!on)
			return;

		on = false;
		logger.log(level, message + " [stop] [" + ticks + " " + objects + " in " + TimeFormat.UP_TO_SECONDS.format(System.nanoTime() - startTime) + "]");
	}

	private void step() {
		if (getTicks() % (updateEvery) != 0)
			return;

		String msg = message + ": ";
		if (expectedTicks > 0)
			msg += getTicks() + "/" + expectedTicks;
		else
			msg += "in progress (" + getTicks() + ")";

		logger.log(level, msg);
	}
}
