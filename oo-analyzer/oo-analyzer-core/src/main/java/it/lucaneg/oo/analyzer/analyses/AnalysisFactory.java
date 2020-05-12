package it.lucaneg.oo.analyzer.analyses;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import it.lucaneg.logutils.EnrichedLogger;
import it.lucaneg.oo.sdk.analyzer.analyses.Analysis;

public class AnalysisFactory {
	
	private static final EnrichedLogger logger = new EnrichedLogger(AnalysisFactory.class);
	
	@SuppressWarnings("rawtypes")
	private static final Map<String, Constructor<? extends Analysis>> analysesMap = new HashMap<>();

	static {
		Reflections reflections = new Reflections(AnalysisFactory.class.getPackageName(), new SubTypesScanner());
		@SuppressWarnings("rawtypes")
		Set<Class<? extends Analysis>> instances = reflections.getSubTypesOf(Analysis.class);  
		for (@SuppressWarnings("rawtypes") Class<? extends Analysis> analysis : instances) 
			if (!Modifier.isAbstract(analysis.getModifiers()))
				try {
					Analysis<?, ?> instance = analysis.getConstructor().newInstance();
					analysesMap.put(instance.getName(), analysis.getConstructor());
				} catch (NoSuchMethodException | SecurityException e) {
					logger.warn("No empty constructor found for analysis class " + analysis.getName(), e);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					logger.warn("unable to instantiate analysis class " + analysis.getName(), e);
				}
	}

	/**
	 * Yields the names of all the {@link Analysis} instances.
	 * 
	 * @return the names of the instances
	 */
	public static Collection<String> getAllInstancesNames() {
		return analysesMap.keySet();
	}

	/**
	 * Yields the {@link Analysis} instance with the given name.
	 * 
	 * @param analysis the name of the analysis
	 * @return the instance
	 */
	public static Analysis<?, ?> getInstance(String analysis) {
		if (!analysesMap.containsKey(analysis))
			throw new IllegalArgumentException("Unknown analysis: " + analysis);
		
		try {
			return analysesMap.get(analysis).newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new IllegalStateException("Unable to instantiate analysis instance " + analysis, e);
		}
	}
}
