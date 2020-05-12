package it.lucaneg.oo.api.analyzer.program;

import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import it.lucaneg.oo.api.analyzer.datastructures.Graph;
import it.lucaneg.oo.api.analyzer.program.instructions.BranchingStatement;
import it.lucaneg.oo.api.analyzer.program.instructions.Skip;
import it.lucaneg.oo.api.analyzer.program.instructions.Statement;

/**
 * A block of code. The code is represented as a graph in order to be already in
 * the form of a control flow graph.
 * 
 * @author Luca Negrini
 */
public class MCodeBlock extends Graph<Statement> {

	/**
	 * Builds the block.
	 */
	public MCodeBlock() {
		super();
	}

	/**
	 * Constructor for cloning another block.
	 * 
	 * @param other the original block
	 */
	protected MCodeBlock(MCodeBlock other) {
		this.branches.putAll(other.branches);
		this.edges.putAll(other.edges);
		this.start = other.start;
		this.end = other.end;
	}

	/**
	 * The first instruction of the block
	 */
	private Statement start;

	/**
	 * The last instruction of the block
	 */
	private Statement end;

	/**
	 * The branches contained in this block, each one mapped to a pair of statements
	 * where the left one represents the beginning of the true block, while the
	 * right one represents the beginning of the false block
	 */
	private final Map<BranchingStatement, Pair<Statement, Statement>> branches = new IdentityHashMap<>();

	/**
	 * Yields the first instruction of this block of code
	 * 
	 * @return the first instruction
	 */
	public Statement getFirstInstruction() {
		return getRootNode();
	}

	/**
	 * Adds the given statement at the end of this block of code
	 * 
	 * @param statement the statement to add
	 */
	public void add(Statement statement) {
		addVertex(statement);
		if (start == null)
			start = statement;

		if (end != null)
			addEdge(end, statement);

		end = statement;
	}

	/**
	 * Adds the given branch at the end of this block of code. The branch is added
	 * as every other statement through {@link #add(Statement)}, while the other
	 * components are manually attached to the block in order to implement the
	 * semantic of an if statement. The join statement will represent the end of the
	 * block after the insertion, where subsequent instruction can be concatenated.
	 * 
	 * @param branch  the branching statement
	 * @param ifTrue  the block that will be executed if the branch condition
	 *                evaluates to true
	 * @param ifFalse the block that will be executed if the branch condition
	 *                evaluates to false
	 * @param join    a placeholder instruction that is used to join together the
	 *                two branches
	 */
	public void addBranch(BranchingStatement branch, MCodeBlock ifTrue, MCodeBlock ifFalse, Skip join) {
		// normally add the branch
		add(branch);

		// add all the true block
		edges.putAll(ifTrue.edges);
		addEdge(branch, ifTrue.start);

		// add all the false block
		edges.putAll(ifFalse.edges);
		addEdge(branch, ifFalse.start);

		// add the joining skip
		addVertex(join);

		// connect the blocks to the skip
		addEdge(ifTrue.end, join);
		addEdge(ifFalse.end, join);

		// mark the skip as final
		end = join;

		// save the entry points of the branch
		branches.put(branch, Pair.of(ifTrue.start, ifFalse.start));

		// keep track of all branches in child blocks
		branches.putAll(ifTrue.branches);
		branches.putAll(ifFalse.branches);
	}

	/**
	 * Adds the given loop at the end of this block of code. The branch is added as
	 * every other statement through {@link #add(Statement)}, while the other
	 * components are manually attached to the block in order to implement the
	 * semantic of a while loop statement. The join statement will represent the end
	 * of the block after the insertion, where subsequent instruction can be
	 * concatenated.
	 * 
	 * @param branch   the branching statement
	 * @param loopBody the body of the loop
	 * @param join     a placeholder instruction that is used to join together the
	 *                 two branches
	 */
	public void addLoop(BranchingStatement branch, MCodeBlock loopBody, Skip join) {
		// normally add the branch
		add(branch);

		// add all the loop body
		edges.putAll(loopBody.edges);
		addEdge(branch, loopBody.start);

		// close the loop
		addEdge(loopBody.end, branch);

		// add the joining skip
		addVertex(join);

		// connect the branch to the skip
		addEdge(branch, join);

		// mark the skip as final
		end = join;

		// save the entry points of the branch
		branches.put(branch, Pair.of(loopBody.start, join));

		// keep track of all branches in loop body
		branches.putAll(loopBody.branches);
	}

	/**
	 * Joins to code block into a single one, merging statements and branches.
	 * 
	 * @param other the other block of code
	 */
	public void concat(MCodeBlock other) {
		if (other.getVertexCount() == 0)
			// nothing to concat
			return;

		if (getVertexCount() == 0) {
			// this block is empty: we mirror the other block
			edges.putAll(other.edges);
			branches.putAll(other.branches);
			start = other.start;
			end = other.end;
		} else {
			edges.putAll(other.edges);
			branches.putAll(other.branches);
			addEdge(end, other.start);
			end = other.end;
		}
	}

	/**
	 * Yields true if and only if {@code follower} is the first statement of the
	 * true branch of {@code st}.
	 * 
	 * @param st       the branching statement
	 * @param follower the follower
	 * @return true only if that condition holds
	 */
	protected boolean isStartOfTrueBlockFor(BranchingStatement st, Statement follower) {
		return branches.containsKey(st) && branches.get(st).getLeft() == follower;
	}

	/**
	 * Yields true if and only if {@code follower} is the first statement of the
	 * false branch of {@code st}.
	 * 
	 * @param st       the branching statement
	 * @param follower the follower
	 * @return true only if that condition holds
	 */
	protected boolean isStartOfFalseBlockFor(BranchingStatement st, Statement follower) {
		return branches.containsKey(st) && branches.get(st).getRight() == follower;
	}

	@Override
	protected Statement getRootNode() {
		return start;
	}

	@Override
	protected String provideVertexShapeIfNeeded(Statement vertex) {
		if (vertex == start || vertex == end)
			return "peripheries=2,";

		if (vertex instanceof BranchingStatement)
			return "shape = rect,";

		return "";
	}

	@Override
	protected String provideEdgeLabelIfNeeded(Statement source, Statement destination) {
		if (source instanceof BranchingStatement) {
			if (isStartOfTrueBlockFor((BranchingStatement) source, destination))
				return "true";
			else if (isStartOfFalseBlockFor((BranchingStatement) source, destination))
				return "false";
		}

		return "";
	}
}
