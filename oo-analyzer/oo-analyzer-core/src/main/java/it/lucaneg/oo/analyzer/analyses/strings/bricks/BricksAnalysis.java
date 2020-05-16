package it.lucaneg.oo.analyzer.analyses.strings.bricks;

import it.lucaneg.oo.analyzer.analyses.strings.BaseStringAnalysis;
import it.lucaneg.oo.sdk.analyzer.fixpoint.Fixpoint;
import it.lucaneg.oo.sdk.analyzer.fixpoint.SizeBasedFixpoint;
import it.lucaneg.oo.sdk.analyzer.program.MCodeBlock;

public class BricksAnalysis extends BaseStringAnalysis<BricksLattice, BricksEnvironment> {

	/**
	 * The K_L index, that is, the limit on the length of the list of bricks after
	 * which the widening returns top
	 */
	static final int K_L = 5;
	
	/**
	 * The K_I index, that is, the limit on the number of repetitions (max - min)
	 * after which the widening turns max into infinity
	 */
	static final int K_I = 5;

	/**
	 * The K_S index, that is, the limit on the number of strings in the brick 
	 * after which the widening returns top
	 */
	static final int K_S = 5;
	
	private static final int MAX_LENGTH_BEFORE_WIDENING = 20;
	
	private final BricksExpressionEvaluator evaluator = new BricksExpressionEvaluator();

	private final BricksSatisfiabilityEvaluator satisfiability = new BricksSatisfiabilityEvaluator();

	@Override
	public BricksExpressionEvaluator getExpressionEvaluator() {
		return evaluator;
	}

	@Override
	public BricksSatisfiabilityEvaluator getSatisfiabilityEvaluator() {
		return satisfiability;
	}
	
	@Override
	protected BricksLattice latticeBottom() {
		return BricksLattice.getBottom();
	}

	@Override
	public BricksEnvironment mkEmptyEnvironment() {
		return new BricksEnvironment();
	}

	@Override
	protected Fixpoint<BricksLattice, BricksEnvironment> mkFixpoint(MCodeBlock code) {
		return new SizeBasedFixpoint<>(code, this::decide);
	}

	private boolean decide(BricksLattice approx) {
		int length = 0;
		for (Brick b : approx.getBricks())
			if (b.getMax() == -1)
				return true;
			else if (b == Brick.TOP || b == Brick.BOTTOM)
				length++;
			else
				length += b.getStrings().parallelStream().mapToInt(s -> s.length()).sum() * b.getMax();
		
		return length > MAX_LENGTH_BEFORE_WIDENING;
	}
}
