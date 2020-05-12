package it.lucaneg.oo.analyzer.analyses.strings;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractAnalysis;
import it.lucaneg.oo.sdk.analyzer.analyses.impl.AbstractLattice;
import it.lucaneg.oo.sdk.analyzer.program.instructions.ILocalWrite;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

/**
 * A base class for analyses of string values
 * 
 * @author Luca Negrini
 */
public abstract class BaseStringAnalysis<L extends AbstractLattice<L>, E extends BaseStringEnvironment<L, E>> extends AbstractAnalysis<L, E> {
	
	@Override
	public E smallStepSemantics(Statement st, E env) {
		if (st instanceof ILocalWrite) 
			return handleLocalWrite(st, env);
		else
			// TODO handle more statements
			return env;
	}
	
	private E handleLocalWrite(Statement st, E env) {
		ILocalWrite varOperation = (ILocalWrite) st;
		E result = env.copy();
		
		if (varOperation.getVariable().getType() != BaseStringEnvironment.getStringType())
			result.set(varOperation.getVariable(), latticeBottom());
		else 
			// the value gets always overwritten
			result.set(varOperation.getVariable(), env.eval(varOperation.getExpression()));
		
		return result;
	}

	protected abstract L latticeBottom();
	
	@Override
	public E assume(Expression expr, E env) {
		return env;
	}
}
