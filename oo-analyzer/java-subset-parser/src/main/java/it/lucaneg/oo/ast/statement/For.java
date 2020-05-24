package it.lucaneg.oo.ast.statement;

import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.parser.typecheck.CheckerHelper;
import it.lucaneg.oo.parser.typecheck.TypeCheckException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class For extends Statement {

    private final Statement initialisation;

    private Expression condition;

    private final Assignment update;

    private final Statement body;
    
    public For(String source, int line, int pos, Statement initialisation, Expression condition, Assignment update,
    		Statement body) {
    	super(source, line, pos);
    	this.initialisation = initialisation;
    	this.condition = condition;
    	this.update = update;
    	this.body = body;
    }
    
    @Override
    public String toString() {
    	return "for (" + initialisation + "; " + condition + "; " + update + ") {" + body + "}";
    }
    
    @Override
	public CheckerHelper typeCheck(CheckerHelper helper) throws TypeCheckException { 
    	CheckerHelper initChecker = initialisation.typeCheck(helper);

    	condition.mustBeBoolean(initChecker);
    	update.typeCheck(initChecker);
    	body.typeCheck(initChecker);

    	return helper;
    }

    @Override
    public boolean allPathsEndWithReturn() throws TypeCheckException {
    	update.allPathsEndWithReturn();
    	body.allPathsEndWithReturn();

    	if (initialisation.allPathsEndWithReturn()) {
    		typeCheckError("dead-code after for loop initialisation");

    		return true;
    	}

    	return false;
    }
	
	@Override
	protected CheckerHelper transformStringJoins(CheckerHelper helper) {
		condition = condition.transformStringJoins(helper);
		helper = initialisation.transformStringJoins(helper);
		helper = body.transformStringJoins(helper);
		helper = update.transformStringJoins(helper);
		return helper;
	}
}