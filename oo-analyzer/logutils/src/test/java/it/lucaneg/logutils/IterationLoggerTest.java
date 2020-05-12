package it.lucaneg.logutils;

import org.junit.Test;

import it.lucaneg.logutils.iterations.Ticker;

public class IterationLoggerTest {
	
	private static final EnrichedLogger logger = new EnrichedLogger(IterationLoggerTest.class);

	@Test
	public void testIterations() {
		Integer[] array = generateArray();
			
		int sum = 0;
		for (Integer i : logger.mkIterationLogger("Iteration test", "integers").iterate(array))
			sum += i;
		
		logger.info("SUM THROUGH ITERATION: " + sum);
	}
	
	@Test
	public void testStream() {
		Integer[] array = generateArray();
			
		int sum = logger.mkIterationLogger("Stream test", "integers").stream(array).reduce((a, b) -> a + b).get();
		logger.info("SUM THROUGH STREAM: " + sum);
	}
	
	@Test
	public void testManual() {
		Integer[] array = generateArray();
			
		int sum = 0;
		Ticker ticker = logger.mkIterationLogger("Manual test", "integers").getManualTicker();
		ticker.on();
		for (Integer i : array) {
			sum += i;
			ticker.tick();
		}
		ticker.off();
		
		logger.info("MANUAL SUM: " + sum);
	}

	private Integer[] generateArray() {
		Integer[] array = new Integer[(int) (Math.random()*1000)];
		for (int i = 0; i < array.length; i++)
			array[i] = (int) (Math.random() * 20);
		return array;
	}
}
