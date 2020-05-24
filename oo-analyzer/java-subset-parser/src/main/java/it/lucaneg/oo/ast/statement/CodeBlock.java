package it.lucaneg.oo.ast.statement;

import java.util.Arrays;

import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CodeBlock extends Statement {
	
	private final Statement[] statements;

	public CodeBlock(String source, int line, int pos, Statement[] statements) {
		super(source, line, pos);
		this.statements = statements;
	}

	@Override
	public String toString() {
		return Arrays.toString(statements);
	}
	
	@Override
	public CheckerHelper typeCheck(CheckerHelper helper) throws TypeCheckException {
		CheckerHelper last = helper;
		for (Statement st : statements)
			last = st.typeCheck(last);
		
		return last;
	}

	@Override
	public boolean allPathsEndWithReturn() throws TypeCheckException {
		boolean last = false;
		for (int i = 0; i < statements.length; i++) {
			if (i != statements.length - 1 && statements[i].allPathsEndWithReturn())
				typeCheckError("dead-code after this statement");
			else
				last = statements[i].allPathsEndWithReturn();
		}
		
		return last;
	}
	
	public CheckerHelper transformStringJoins(CheckerHelper helper) {
		CheckerHelper last = helper;
		for (Statement st : statements)
			last = st.transformStringJoins(last);
		
		return last;
	}
}
