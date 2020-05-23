package it.lucaneg.oo.sdk.analyzer.analyses;

import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;

import it.lucaneg.oo.sdk.analyzer.program.instructions.BranchingStatement;
import it.lucaneg.oo.sdk.analyzer.program.instructions.LoopStatement;
import it.lucaneg.oo.sdk.analyzer.program.instructions.Statement;

public class TokenList {

	public static abstract class Token {
		private final Statement st;

		protected Token(Statement st) {
			this.st = st;
		}
		
		public Statement getStatement() {
			return st;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((st == null) ? 0 : st.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Token other = (Token) obj;
			if (st == null) {
				if (other.st != null)
					return false;
			} else if (!st.equals(other.st))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return String.valueOf(st.getIndex());
		}
	}
	
	public static class StartingToken extends Token {

		public StartingToken() {
			super(null);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = prime * getClass().getName().hashCode();
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "\u03B5";
		}
	}
	
	public static class ConditionalToken extends Token {
		private final boolean trueBranch;

		public ConditionalToken(BranchingStatement st, boolean trueBranch) {
			super(st);
			this.trueBranch = trueBranch;
		}
		
		public boolean isTrueBranch() {
			return trueBranch;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + (trueBranch ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			ConditionalToken other = (ConditionalToken) obj;
			if (trueBranch != other.trueBranch)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "C" + super.toString() + "-"+ (trueBranch ? "T" : "F");
		}
	}
	
	public static class LoopIterationToken extends Token {
		private final int iteration;

		public LoopIterationToken(LoopStatement st, int iteration) {
			super(st);
			this.iteration = iteration;
		}
		
		public int getIteration() {
			return iteration;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + iteration;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			LoopIterationToken other = (LoopIterationToken) obj;
			if (iteration != other.iteration)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "L" + super.toString() + "-"+ iteration;
		}
	}
	
	public static class GeneralLoopToken extends Token {

		private boolean firstIteration;
		
		public GeneralLoopToken(LoopStatement st) {
			super(st);
			firstIteration = true;
		}
		
		public void iterate() {
			firstIteration = false;
		}

		public boolean isFirstIteration() {
			return firstIteration;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + getClass().getName().hashCode();
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "L" + super.toString() + "-G";
		}
	}
	
	private final LinkedList<Token> tokens;
	
	public TokenList() {
		tokens = new LinkedList<>();
		tokens.add(new StartingToken());
	}
	
	private TokenList(TokenList other) {
		tokens = new LinkedList<>(other.tokens);
	}
	
	public TokenList push(Token token) {
		TokenList res = new TokenList(this);
		res.tokens.addFirst(token);
		return res;
	}
	
	public TokenList pop() {
		TokenList res = new TokenList(this);
		res.tokens.removeFirst();
		return res;
	}
	
	public Token getHead() {
		return tokens.getFirst();
	}
	
	public boolean headIsGeneralLoop() {
		return getHead() instanceof GeneralLoopToken && !((GeneralLoopToken) getHead()).isFirstIteration();
	}
	
	public Token lastLoopToken() {
		for (Token tok : tokens)
			if (tok instanceof GeneralLoopToken || tok instanceof LoopIterationToken)
				return tok;
		
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tokens == null) ? 0 : tokens.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TokenList other = (TokenList) obj;
		if (tokens == null) {
			if (other.tokens != null)
				return false;
		} else if (!tokens.equals(other.tokens))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "<" + StringUtils.join(tokens, "::") + ">";
	}
}
