package it.lucaneg.oo.analyzer.analyses;

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
	private static final Map<String, Class<? extends Analysis>> analysesMap = new HashMap<>();

	private static boolean isInitialized = false;
	
	private static void fillAnalyses() {
		Reflections reflections = new Reflections(AnalysisFactory.class.getPackageName(), new SubTypesScanner());
		@SuppressWarnings("rawtypes")
		Set<Class<? extends Analysis>> instances = reflections.getSubTypesOf(Analysis.class);  
		for (@SuppressWarnings("rawtypes") Class<? extends Analysis> analysis : instances) 
			if (!Modifier.isAbstract(analysis.getModifiers()))
				try {
					Analysis<?, ?> instance = analysis.getConstructor().newInstance();
					analysesMap.put(instance.getName(), analysis);
				} catch (NoSuchMethodException | SecurityException e) {
					logger.warn("No empty constructor found for analysis class " + analysis.getName(), e);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					logger.warn("unable to instantiate analysis class " + analysis.getName(), e);
				}
		
		isInitialized = true;
	}

	/**
	 * Yields the names of all the {@link Analysis} instances.
	 * 
	 * @return the names of the instances
	 */
	public static Collection<String> getAllInstancesNames() {
		if (!isInitialized)
			fillAnalyses();
		
		return analysesMap.keySet();
	}

	/**
	 * Yields the {@link Analysis} instance with the given name.
	 * 
	 * @param analysis the name of the analysis
	 * @return the instance
	 */
	public static Analysis<?, ?> getInstance(String analysis) {
		if (!isInitialized)
			fillAnalyses();
		
		if (!analysesMap.containsKey(analysis))
			throw new IllegalArgumentException("Unknown analysis: " + analysis);
		
		try {
			return analysesMap.get(analysis).getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) { 
			throw new IllegalStateException("Unable to instantiate analysis " + analysis, e);
		}
	}
	
	/**
	 * Yields the class of the {@link Analysis} with the given name.
	 * 
	 * @param analysis the name of the analysis
	 * @return the class
	 */
	public static Class<?> getClassOf(String analysis) {
		if (!isInitialized)
			fillAnalyses();
		
		if (!analysesMap.containsKey(analysis))
			throw new IllegalArgumentException("Unknown analysis: " + analysis);
		
		return (Class<?>) analysesMap.get(analysis);
	}
}
