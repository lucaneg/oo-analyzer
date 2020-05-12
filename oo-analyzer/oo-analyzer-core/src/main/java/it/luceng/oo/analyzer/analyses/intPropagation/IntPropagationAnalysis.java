package it.luceng.oo.analyzer.analyses.intPropagation;

import java.util.Collection;

import org.apache.commons.lang3.tuple.Pair;

import it.lucaneg.oo.api.analyzer.analyses.ExpressionSatisfiabilityEvaluator.Satisfiability;
import it.lucaneg.oo.api.analyzer.analyses.impl.AbstractAnalysis;
import it.lucaneg.oo.api.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.api.analyzer.fixpoint.IterationBasedFixpoint;
import it.lucaneg.oo.api.analyzer.program.MCodeBlock;
import it.lucaneg.oo.api.analyzer.program.MLocalVariable;
import it.lucaneg.oo.api.analyzer.program.instructions.ILocalWrite;
import it.lucaneg.oo.api.analyzer.program.instructions.Statement;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.IntType;
import it.luceng.oo.analyzer.util.ExpressionUtils;

public class IntPropagationAnalysis extends AbstractAnalysis<IntPropagationLattice, IntPropagationEnvironment> {

	@Override
	public IntPropagationEnvironment smallStepSemantics(Statement st, IntPropagationEnvironment env) {
		if (st instanceof ILocalWrite) 
			return handleLocalWrite(st, env);
		else
			// TODO handle more statements
			return env;
	}
	
	private IntPropagationEnvironment handleLocalWrite(Statement st, IntPropagationEnvironment env) {
		ILocalWrite varOperation = (ILocalWrite) st;
		IntPropagationEnvironment result = env.copy();
		
		if (varOperation.getVariable().getType() != IntType.INSTANCE)
			result.set(varOperation.getVariable(), IntPropagationLattice.getBottom());
		else 
			// the value gets always overwritten
			result.set(varOperation.getVariable(), env.eval(varOperation.getExpression()));
		
		return result;
	}

	@Override
	public IntPropagationEnvironment assume(Expression expr, IntPropagationEnvironment env) {
		if (env.satisfies(expr) == Satisfiability.SATISFIED)
			return env;
		
		Collection<String> vars = ExpressionUtils.getAllVariablesNames(expr);

		IntPropagationEnvironment result = env.except(vars), temp;
		if (result.satisfies(expr) == Satisfiability.NOT_SATISFIED)
			// the expression can never be satisfied
			return mkEmptyEnvironment();
		
		for (Pair<MLocalVariable, IntPropagationLattice> approx : env) {
			// we re-add all variables that do not violate the expression
			// TODO problem: i < j with i = 10 and j = 1. Depending on the order
			// of processing, one variable will be arbitrarily picked over the other
			temp = result.copy();
			temp.set(approx.getKey(), approx.getValue());
			if (temp.satisfies(expr) != Satisfiability.NOT_SATISFIED)
				result.set(approx.getKey(), approx.getValue()); 
		}
		
		return result;
	}

	@Override
	public IntPropagationEnvironment mkEmptyEnvironment() {
		return new IntPropagationEnvironment();
	}
	
	@Override
	protected Fixpoint<IntPropagationLattice, IntPropagationEnvironment> mkFixpoint(MCodeBlock code) {
		return new IterationBasedFixpoint<>(code, 0);
	}
}
