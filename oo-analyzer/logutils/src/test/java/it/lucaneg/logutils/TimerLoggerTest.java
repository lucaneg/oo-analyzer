package it.lucaneg.logutils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.lucaneg.logutils.timings.TimeFormat;
import it.lucaneg.logutils.timings.TimerLogger;

public class TimerLoggerTest {
	private static final EnrichedLogger logger = new EnrichedLogger(TimerLoggerTest.class);
	
	@Test
	public void testSupplier() {
		Integer result = logger.mkTimerLogger("Test supplier logging", TimeFormat.MILLIS).execSupplier(this::supplier);
		assertEquals(5, result.intValue());
	}
	
	@Test
	public void testFunction() {
		TimerLogger fLog = logger.mkTimerLogger("Test supplier logging", TimeFormat.MILLIS);
		for (double d = 0; d < 3; d++) {
			Integer result = fLog.execFunction((v) -> function(v), d);
			assertEquals(5 + (int) d, result.intValue());
		}
	}
	
	private int function(double par) {
		logAction();
		return 5 + (int) par;
	}
	
	private int supplier() {
		logAction();
		return 5;
	}

	private void logAction() {
		logger.mkTimerLogger("Test action logging", TimeFormat.MILLIS).execAction(this::action);
	}
	
	private void action() {
		System.out.println("Going to sleep...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Waking up!"); 
	}
}
