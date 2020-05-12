package it.lucaneg.oo.analyzer.tests;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.api.analyzer.checks.Finding;

public class FindingsDiffBuilder {
	
	private static final EnrichedLogger logger = new EnrichedLogger(FindingsDiffBuilder.class);

	private final Collection<Finding> expected;
	private final Collection<Finding> actual;

	private final Collection<Finding> onlyExpected = new ArrayList<>();
	private final Collection<Finding> onlyActual = new ArrayList<>();
	private final Map<Finding, Finding> common = new HashMap<>();

	public FindingsDiffBuilder(Collection<Finding> expected, Collection<Finding> actual) {
		this.expected = expected;
		this.actual = actual;
	}

	public void computeDiff(Comparator<Finding> compararator) {
		Deque<Finding> expectedQueue = new ArrayDeque<Finding>();
		Deque<Finding> actualQueue = new ArrayDeque<Finding>();

		Finding[] expectedArray = expected.toArray(new Finding[expected.size()]);
		Finding[] actualArray = actual.toArray(new Finding[actual.size()]);
		Arrays.sort(expectedArray, compararator);
		Arrays.sort(actualArray, compararator);
		expectedQueue.addAll(Arrays.asList(expectedArray));
		actualQueue.addAll(Arrays.asList(actualArray));

		computeQueueDiff(expectedQueue, actualQueue, compararator);
	}

	private void computeQueueDiff(Deque<Finding> expectedQueue, Deque<Finding> actualQueue, Comparator<Finding> matcher) {
		Finding currentExp = null;
		Finding currentAct = null;

		while (!(expectedQueue.isEmpty() && actualQueue.isEmpty())) {
			currentExp = expectedQueue.peek();
			currentAct = actualQueue.peek();

			if (currentExp == null) {
				if (currentAct == null)
					break;
				else {
					onlyActual.add(currentAct);
					actualQueue.remove();
					continue;
				}
			} else {
				if (currentAct == null) {
					onlyExpected.add(currentExp);
					expectedQueue.remove();
					continue;
				}
			}

			int cmp = matcher.compare(currentExp, currentAct);
			if (cmp == 0) {
				// unchanged
				common.put(currentExp, currentAct);
				expectedQueue.remove();
				actualQueue.remove();
			} else if (cmp < 0) {
				// lost
				onlyExpected.add(currentExp);
				expectedQueue.remove();
			} else {
				// new
				onlyActual.add(currentAct);
				actualQueue.remove();
			}
		}
	}

	public void printDiff(boolean deltasOnly) {
		// Keep the output compact if there are no differences
		if (onlyExpected.size() == 0 && onlyActual.size() == 0 && deltasOnly) {
			logger.info("Warnings sets contain the same warnings");
			return;
		}

		int i = 1;

		logger.warn("Warnings only in the expected set (" + onlyExpected.size() + ")");
		for (Finding f : onlyExpected) 
			logger.warn("#" + i++ + "\t" + f);
		
		logger.warn("Warnings only in the actual set (" + onlyActual.size() + ")");
		for (Finding f : onlyActual) 
			logger.warn("#" + i++ + "\t" + f);

		if (!deltasOnly) {
			logger.warn("Common Warnings (" + common.size() + "):");
			for (Map.Entry<Finding, Finding> pair : common.entrySet())
				logger.warn("#" + i++ + " - " + pair.getKey());
		}
	}

	public Collection<Finding> getOnlyActual() {
		return onlyActual;
	}

	public Collection<Finding> getOnlyExpected() {
		return onlyExpected;
	}

	public Map<Finding, Finding> getCommon() {
		return common;
	}
}
