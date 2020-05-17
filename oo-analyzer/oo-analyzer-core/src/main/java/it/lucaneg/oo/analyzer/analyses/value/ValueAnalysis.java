package it.lucaneg.oo.analyzer.analyses.value;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import it.lucaneg.oo.analyzer.analyses.value.domains.bools.AbstractBooleanLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.ints.AbstractIntegerLattice;
import it.lucaneg.oo.analyzer.analyses.value.domains.strings.AbstractStringLattice;
import it.lucaneg.oo.analyzer.cli.Configurable;
import it.lucaneg.oo.analyzer.cli.Parameter;
import it.lucaneg.oo.analyzer.cli.RequiresParameters;
import it.lucaneg.oo.analyzer.cli.SetupException;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractAnalysis;
import it.lucaneg.oo.sdk.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.sdk.analyzer.fixpoint.IterationBasedFixpoint;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;
import it.lucaneg.oo.sdk.analyzer.program.instructions.ILocalWrite;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

@RequiresParameters({
		@Parameter(name = "bools", description = "domain for boolean values", provider = AbstractBooleanLattice.class),
		@Parameter(name = "ints", description = "domain for integer values", provider = AbstractIntegerLattice.class),
		@Parameter(name = "strings", description = "domain for string values", provider = AbstractStringLattice.class)
})
public class ValueAnalysis extends AbstractAnalysis<ValueLattice, ValueEnvironment> implements Configurable {

	private static final int WIDENING_THRESHOLD = 5;

	private ValueExpressionEvaluator evaluator = new ValueExpressionEvaluator();

	private ValueSatisfiabilityEvaluator satisfiability = new ValueSatisfiabilityEvaluator();

	private AbstractBooleanLattice<?> booleanSingleton;

	private AbstractIntegerLattice<?> intSingleton;

	private AbstractStringLattice<?> stringSingleton;
	
	@Override
	@SuppressWarnings("rawtypes")
	public void setup(String... parameters) throws SetupException {
		if (parameters == null || parameters.length != 3)
			throw new SetupException("Incorrect number of parameters");
		
		Reflections reflections = new Reflections(SingleValueLattice.class, new SubTypesScanner());
		Set<Class<? extends SingleValueLattice>> instances = reflections.getSubTypesOf(SingleValueLattice.class); 
		try {
			for (Class<? extends SingleValueLattice> candidate : instances) 
				if (!Modifier.isAbstract(candidate.getModifiers()) && !candidate.isAnonymousClass())
					if (candidate.getSimpleName().equals(parameters[0]))
						booleanSingleton = (AbstractBooleanLattice<?>) candidate.getMethod("getTop").invoke(null);
					else if (candidate.getSimpleName().equals(parameters[1]))
						intSingleton = (AbstractIntegerLattice<?>) candidate.getMethod("getTop").invoke(null);
					else if (candidate.getSimpleName().equals(parameters[2]))
						stringSingleton = (AbstractStringLattice<?>) candidate.getMethod("getTop").invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new SetupException("Unable to setup value analysis", e);
		}
		
		if (booleanSingleton == null)
			throw new SetupException("Boolean domain " + parameters[0] + " not found");
		if (intSingleton == null)
			throw new SetupException("Int domain " + parameters[1] + " not found");
		if (stringSingleton == null)
			throw new SetupException("String domain " + parameters[2] + " not found");
		
		evaluator.setBooleanSingleton(booleanSingleton);
		evaluator.setIntSingleton(intSingleton);
		evaluator.setStringSingleton(stringSingleton);
	}

	@Override
	public ValueExpressionEvaluator getExpressionEvaluator() {
		return evaluator;
	}

	@Override
	public ValueSatisfiabilityEvaluator getSatisfiabilityEvaluator() {
		return satisfiability;
	}

	@Override
	public ValueEnvironment smallStepSemantics(Statement st, ValueEnvironment env) {
		if (st instanceof ILocalWrite)
			return handleLocalWrite(st, env);
		else
			// TODO handle more statements
			return env;
	}

	private ValueEnvironment handleLocalWrite(Statement st, ValueEnvironment env) {
		ILocalWrite varOperation = (ILocalWrite) st;
		ValueEnvironment result = env.copy();

		if (!ValueEnvironment.canProcessType(varOperation.getVariable().getType()))
			result.set(varOperation.getVariable(), ValueLattice.getBottom());
		else
			// the value gets always overwritten
			result.set(varOperation.getVariable(), evaluator.eval(varOperation.getExpression(), env));

		return result;
	}

	@Override
	public ValueEnvironment assume(Expression expr, ValueEnvironment env) {
		return env; // TODO
	}

	@Override
	public ValueEnvironment mkEmptyEnvironment() {
		return new ValueEnvironment(booleanSingleton, intSingleton, stringSingleton);
	}

	@Override
	protected Fixpoint<ValueLattice, ValueEnvironment> mkFixpoint(MCodeBlock code) {
		return new IterationBasedFixpoint<>(code, WIDENING_THRESHOLD);
	}

}
