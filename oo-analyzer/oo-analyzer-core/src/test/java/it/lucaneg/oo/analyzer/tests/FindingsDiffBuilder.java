package it.lucaneg.oo.analyzer.tests;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.analyzer.core.JsonAnalysisReport.JsonFinding;

public class FindingsDiffBuilder {
	
	private static final EnrichedLogger logger = new EnrichedLogger(FindingsDiffBuilder.class);

	private final Collection<JsonFinding> expected;
	private final Collection<JsonFinding> actual;

	private final Collection<JsonFinding> onlyExpected = new ArrayList<>();
	private final Collection<JsonFinding> onlyActual = new ArrayList<>();
	private final Map<JsonFinding, JsonFinding> common = new HashMap<>();

	public FindingsDiffBuilder(List<JsonFinding> expected, List<JsonFinding> actual) {
		this.expected = expected;
		this.actual = actual;
	}

	public void computeDiff(Comparator<JsonFinding> compararator) {
		Deque<JsonFinding> expectedQueue = new ArrayDeque<>();
		Deque<JsonFinding> actualQueue = new ArrayDeque<>();

		JsonFinding[] expectedArray = expected.toArray(new JsonFinding[expected.size()]);
		JsonFinding[] actualArray = actual.toArray(new JsonFinding[actual.size()]);
		Arrays.sort(expectedArray, compararator);
		Arrays.sort(actualArray, compararator);
		expectedQueue.addAll(Arrays.asList(expectedArray));
		actualQueue.addAll(Arrays.asList(actualArray));

		computeQueueDiff(expectedQueue, actualQueue, compararator);
	}

	private void computeQueueDiff(Deque<JsonFinding> expectedQueue, Deque<JsonFinding> actualQueue, Comparator<JsonFinding> matcher) {
		JsonFinding currentExp = null;
		JsonFinding currentAct = null;

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
		for (JsonFinding f : onlyExpected) 
			logger.warn("#" + i++ + "\t" + f);
		
		logger.warn("Warnings only in the actual set (" + onlyActual.size() + ")");
		for (JsonFinding f : onlyActual) 
			logger.warn("#" + i++ + "\t" + f);

		if (!deltasOnly) {
			logger.warn("Common Warnings (" + common.size() + "):");
			for (Map.Entry<JsonFinding, JsonFinding> pair : common.entrySet())
				logger.warn("#" + i++ + " - " + pair.getKey());
		}
	}

	public Collection<JsonFinding> getOnlyActual() {
		return onlyActual;
	}

	public Collection<JsonFinding> getOnlyExpected() {
		return onlyExpected;
	}

	public Map<JsonFinding, JsonFinding> getCommon() {
		return common;
	}
}
