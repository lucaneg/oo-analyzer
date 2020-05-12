package it.luceng.oo.analyzer.checks;

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
import it.lucaneg.oo.api.analyzer.checks.Check;

public class ChecksFactory {

	private static final EnrichedLogger logger = new EnrichedLogger(ChecksFactory.class);

	private static final Map<String, Constructor<? extends Check>> checksMap = new HashMap<>();

	static {
		Reflections reflections = new Reflections(ChecksFactory.class.getPackageName(), new SubTypesScanner());
		Set<Class<? extends Check>> instances = reflections.getSubTypesOf(Check.class);
		for (Class<? extends Check> check : instances) 
			if (!Modifier.isAbstract(check.getModifiers()))
				try {
					Check instance = check.getConstructor().newInstance();
					checksMap.put(instance.getName(), check.getConstructor());
				} catch (NoSuchMethodException | SecurityException e) {
					logger.warn("No empty constructor found for check class " + check.getName(), e);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					logger.warn("unable to instantiate check class " + check.getName(), e);
				}
	}

	/**
	 * Yields the names of all the {@link Check} instances.
	 * 
	 * @return the names of the instances
	 */
	public static Collection<String> getAllInstancesNames() {
		return checksMap.keySet();
	}

	/**
	 * Yields the {@link Check} instance with the given name.
	 * 
	 * @param check the name of the check
	 * @return the instance
	 */
	public static Check getInstance(String check) {
		if (!checksMap.containsKey(check))
			throw new IllegalArgumentException("Unknown check: " + check);
		
		try {
			return checksMap.get(check).newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new IllegalStateException("Unable to instantiate check instance " + check, e);
		}
	}
}
