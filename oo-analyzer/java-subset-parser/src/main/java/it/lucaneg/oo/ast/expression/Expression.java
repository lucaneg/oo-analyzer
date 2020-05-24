package it.lucaneg.oo.ast.expression;

import it.lucaneg.oo.ast.SyntaxNode;
import it.lucaneg.oo.ast.expression.logical.Not;
import it.lucaneg.oo.ast.types.BooleanType;
import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false)
public abstract class Expression extends SyntaxNode {
	
	@Getter
	private Type staticType;
	
	public Expression(String source, int line, int pos) {
		super(source, line, pos);
	}
	
	public void mustBeBoolean(CheckerHelper checker) throws TypeCheckException  {
		if (computeExpressionType(checker) != BooleanType.INSTANCE)
			typeCheckError("boolean expected");
	}

	public void mustBeInt(CheckerHelper checker) throws TypeCheckException {
		if (computeExpressionType(checker) != IntType.INSTANCE)
			typeCheckError("integer expected");
	}
	
	public void mustBeString(CheckerHelper checker) throws TypeCheckException {
		if (computeExpressionType(checker) != Type.getStringType())
			typeCheckError("string expected");
	}
	
	public Type computeExpressionType(CheckerHelper helper) throws TypeCheckException { 
		return staticType == null ? staticType = computeExpressionTypeInternal(helper) : staticType;
	}

	protected abstract Type computeExpressionTypeInternal(CheckerHelper helper) throws TypeCheckException;
	
	public final void cloneStaticType(Expression other) {
		staticType = other.staticType;
	}
	
	public Not negate() {
		if (staticType != BooleanType.INSTANCE)
			throw new IllegalArgumentException("Cannot negate a non-boolean expression");
		
		Not not = new Not(getSource(), getLine(), getPos(), this);
		not.cloneStaticType(this);
		return not;
	}

	public Expression transformStringJoins(CheckerHelper helper) {
		return this;
	}
}
