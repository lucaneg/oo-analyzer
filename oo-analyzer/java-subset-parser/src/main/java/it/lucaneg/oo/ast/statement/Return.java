package it.lucaneg.oo.ast.statement;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.types.Type;
import it.lucaneg.oo.ast.types.VoidType;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Return extends Statement {

	private Expression returned;

	public Return(String source, int line, int pos, Expression returned) {
		super(source, line, pos);
		this.returned = returned;
	}
	
	@Override
	public String toString() {
		if (getReturned() != null)
			return "return " + getReturned();
		else 
			return "return";
	}
	
	@Override
	public CheckerHelper typeCheck(CheckerHelper helper) throws TypeCheckException {
		Type expectedReturnType = helper.getReturnType();

		if (returned == null && expectedReturnType != VoidType.INSTANCE)
			typeCheckError("missing return value");

		Type returnedType;
		if (returned != null && (returnedType = returned.computeExpressionType(helper)) != null &&
				!returnedType.canBeAssignedTo(expectedReturnType))
			typeCheckError("illegal return type: " + expectedReturnType + " expected");

		return helper;
	}
	
	@Override
	public boolean allPathsEndWithReturn() throws TypeCheckException {
		return true;
	}
}