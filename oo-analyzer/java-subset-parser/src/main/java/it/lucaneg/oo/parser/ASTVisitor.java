package it.lucaneg.oo.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import it.lucaneg.antlr.JavaSubsetParser.ArgContext;
import it.lucaneg.antlr.JavaSubsetParser.ArgumentsContext;
import it.lucaneg.antlr.JavaSubsetParser.ArrayAccessContext;
import it.lucaneg.antlr.JavaSubsetParser.ArrayTypeContext;
import it.lucaneg.antlr.JavaSubsetParser.AssignmentContext;
import it.lucaneg.antlr.JavaSubsetParser.BasicExprContext;
import it.lucaneg.antlr.JavaSubsetParser.BlockContext;
import it.lucaneg.antlr.JavaSubsetParser.BlockStatementContext;
import it.lucaneg.antlr.JavaSubsetParser.ClassDeclarationContext;
import it.lucaneg.antlr.JavaSubsetParser.ClassTypeContext;
import it.lucaneg.antlr.JavaSubsetParser.ConstructorDeclarationContext;
import it.lucaneg.antlr.JavaSubsetParser.ExpressionContext;
import it.lucaneg.antlr.JavaSubsetParser.FieldAccessContext;
import it.lucaneg.antlr.JavaSubsetParser.FieldDeclarationContext;
import it.lucaneg.antlr.JavaSubsetParser.ForLoopContext;
import it.lucaneg.antlr.JavaSubsetParser.FormalContext;
import it.lucaneg.antlr.JavaSubsetParser.FormalsContext;
import it.lucaneg.antlr.JavaSubsetParser.IndexContext;
import it.lucaneg.antlr.JavaSubsetParser.LiteralContext;
import it.lucaneg.antlr.JavaSubsetParser.LocalDeclarationContext;
import it.lucaneg.antlr.JavaSubsetParser.LoopContext;
import it.lucaneg.antlr.JavaSubsetParser.MethodCallContext;
import it.lucaneg.antlr.JavaSubsetParser.MethodDeclarationContext;
import it.lucaneg.antlr.JavaSubsetParser.NewBasicArrayExprContext;
import it.lucaneg.antlr.JavaSubsetParser.NewReferenceTypeContext;
import it.lucaneg.antlr.JavaSubsetParser.PrimitiveTypeContext;
import it.lucaneg.antlr.JavaSubsetParser.ReceiverContext;
import it.lucaneg.antlr.JavaSubsetParser.ReferenceTypeContext;
import it.lucaneg.antlr.JavaSubsetParser.StatementContext;
import it.lucaneg.antlr.JavaSubsetParser.TypeContext;
import it.lucaneg.antlr.JavaSubsetParser.TypeOrVoidContext;
import it.lucaneg.antlr.JavaSubsetParser.WhileLoopContext;
import it.lucaneg.antlr.JavaSubsetParserBaseVisitor;
import it.lucaneg.oo.ast.ClassDefinition;
import it.lucaneg.oo.ast.ConstructorDefinition;
import it.lucaneg.oo.ast.FieldDefinition;
import it.lucaneg.oo.ast.FormalDefinition;
import it.lucaneg.oo.ast.MethodDefinition;
import it.lucaneg.oo.ast.SyntaxNode;
import it.lucaneg.oo.ast.expression.Expression;
import it.lucaneg.oo.ast.expression.TypeExpression;
import it.lucaneg.oo.ast.expression.Variable;
import it.lucaneg.oo.ast.expression.arithmetic.Addition;
import it.lucaneg.oo.ast.expression.arithmetic.Division;
import it.lucaneg.oo.ast.expression.arithmetic.Minus;
import it.lucaneg.oo.ast.expression.arithmetic.Module;
import it.lucaneg.oo.ast.expression.arithmetic.Multiplication;
import it.lucaneg.oo.ast.expression.arithmetic.Subtraction;
import it.lucaneg.oo.ast.expression.comparison.Equal;
import it.lucaneg.oo.ast.expression.comparison.Greater;
import it.lucaneg.oo.ast.expression.comparison.GreaterOrEqual;
import it.lucaneg.oo.ast.expression.comparison.Less;
import it.lucaneg.oo.ast.expression.comparison.LessOrEqual;
import it.lucaneg.oo.ast.expression.comparison.NotEqual;
import it.lucaneg.oo.ast.expression.creation.NewArray;
import it.lucaneg.oo.ast.expression.creation.NewObject;
import it.lucaneg.oo.ast.expression.dereference.Argument;
import it.lucaneg.oo.ast.expression.dereference.ArrayAccess;
import it.lucaneg.oo.ast.expression.dereference.Call;
import it.lucaneg.oo.ast.expression.dereference.FieldAccess;
import it.lucaneg.oo.ast.expression.dereference.Index;
import it.lucaneg.oo.ast.expression.dereference.Receiver;
import it.lucaneg.oo.ast.expression.literal.FalseLiteral;
import it.lucaneg.oo.ast.expression.literal.FloatLiteral;
import it.lucaneg.oo.ast.expression.literal.IntLiteral;
import it.lucaneg.oo.ast.expression.literal.Literal;
import it.lucaneg.oo.ast.expression.literal.NullLiteral;
import it.lucaneg.oo.ast.expression.literal.StringLiteral;
import it.lucaneg.oo.ast.expression.literal.SuperLiteral;
import it.lucaneg.oo.ast.expression.literal.ThisLiteral;
import it.lucaneg.oo.ast.expression.literal.TrueLiteral;
import it.lucaneg.oo.ast.expression.logical.And;
import it.lucaneg.oo.ast.expression.logical.Not;
import it.lucaneg.oo.ast.expression.logical.Or;
import it.lucaneg.oo.ast.expression.typeCheck.Cast;
import it.lucaneg.oo.ast.expression.typeCheck.TypeCheck;
import it.lucaneg.oo.ast.statement.Assert;
import it.lucaneg.oo.ast.statement.Assignment;
import it.lucaneg.oo.ast.statement.CodeBlock;
import it.lucaneg.oo.ast.statement.Command;
import it.lucaneg.oo.ast.statement.For;
import it.lucaneg.oo.ast.statement.If;
import it.lucaneg.oo.ast.statement.LocalDeclaration;
import it.lucaneg.oo.ast.statement.Nop;
import it.lucaneg.oo.ast.statement.Return;
import it.lucaneg.oo.ast.statement.Statement;
import it.lucaneg.oo.ast.statement.While;
import it.lucaneg.oo.ast.types.ArrayType;
import it.lucaneg.oo.ast.types.BooleanType;
import it.lucaneg.oo.ast.types.ClassType;
import it.lucaneg.oo.ast.types.FloatType;
import it.lucaneg.oo.ast.types.IntType;
import it.lucaneg.oo.ast.types.VoidType;

public class ASTVisitor extends JavaSubsetParserBaseVisitor<SyntaxNode> {

	private final String source;
	
	private ClassDefinition partialResult;
	
	public ASTVisitor(String source) {
		this.source = source;
	}
	
	private static int getLine(ParserRuleContext ctx) {
		return ctx.getStart().getLine();
	}
	
	private static int getColumn(ParserRuleContext ctx) {
		return ctx.getStart().getCharPositionInLine();
	}
	
	@Override
	public SyntaxNode visit(ParseTree tree) {
		return visitClassDeclaration((ClassDeclarationContext) tree);
	}

	@Override
	public ClassDefinition visitClassDeclaration(ClassDeclarationContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		String name = ctx.name.getText();
		String superclass = ctx.superclass == null ? null : ctx.superclass.getText();
		
		partialResult = new ClassDefinition(source, line, col, name, superclass);
		
		for (FieldDeclarationContext field : ctx.declarations.fieldDeclaration()) 
			partialResult.addField(visitFieldDeclaration(field));
		for (ConstructorDeclarationContext constructor : ctx.declarations.constructorDeclaration()) 
			partialResult.addConstructor(visitConstructorDeclaration(constructor));
		for (MethodDeclarationContext method : ctx.declarations.methodDeclaration()) 
			partialResult.addMethod(visitMethodDeclaration(method));
		
		return partialResult;
	}

	@Override
	public FieldDefinition visitFieldDeclaration(FieldDeclarationContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		String name = ctx.name.getText();
		return new FieldDefinition(source, line, col, name, visitType(ctx.ftype).getType());
	}
	
	@Override
	public MethodDefinition visitMethodDeclaration(MethodDeclarationContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		String name = ctx.name.getText();
		TypeExpression type = visitTypeOrVoid(ctx.ret);
		FormalDefinition[] formals = parseFormals(ctx.formals());
		CodeBlock code = visitBlock(ctx.code);
		return new MethodDefinition(source, line, col, name, type.getType(), formals, code);
	}

	@Override
	public ConstructorDefinition visitConstructorDeclaration(ConstructorDeclarationContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		String name = ctx.name.getText();
		FormalDefinition[] formals = parseFormals(ctx.formals());
		CodeBlock code = visitBlock(ctx.code);
		return new ConstructorDefinition(source, line, col, name, formals, code);
	}

	private FormalDefinition[] parseFormals(FormalsContext formalsContext) {
		List<FormalDefinition> formals = new ArrayList<>();
		for (FormalContext formal : formalsContext.formal())
			formals.add(visitFormal(formal));
		
		return formals.toArray(new FormalDefinition[formals.size()]);
	}
	
	@Override
	public CodeBlock visitBlock(BlockContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		List<Statement> statements = new ArrayList<>();
		
		for (BlockStatementContext bst : ctx.blockStatement()) 
			if (bst.localDeclaration() != null)
				statements.add(visitLocalDeclaration(bst.localDeclaration()));
			else 
				statements.add(visitStatement(bst.statement()));
		
		return new CodeBlock(source, line, col, statements.toArray(new Statement[statements.size()]));
	}
	
	@Override
	public LocalDeclaration visitLocalDeclaration(LocalDeclarationContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		TypeExpression type = visitType(ctx.type());
		String name = ctx.name.getText();
		Expression init = null;
		
		if (ctx.expression() != null)
			init = visitExpression(ctx.expression());
		
		return new LocalDeclaration(source, line, col, type.getType(), name, init);
	}
	
	@Override
	public Expression visitExpression(ExpressionContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		
		if (ctx.paren != null)
			return visitExpression(ctx.paren);
		else if (ctx.basicExpr() != null)
			return visitBasicExpr(ctx.basicExpr());
		else if (ctx.NOT() != null)
			return new Not(source, line, col, visitExpression(ctx.nested));
		else if (ctx.AND() != null)
			return new And(source, line, col, visitExpression(ctx.left), visitExpression(ctx.right));
		else if (ctx.OR() != null)
			return new Or(source, line, col, visitExpression(ctx.left), visitExpression(ctx.right));
		else if (ctx.EQUAL() != null)
			return new Equal(source, line, col, visitExpression(ctx.left), visitExpression(ctx.right));
		else if (ctx.NOTEQUAL() != null)
			return new NotEqual(source, line, col, visitExpression(ctx.left), visitExpression(ctx.right));
		else if (ctx.GT() != null)
			return new Greater(source, line, col, visitExpression(ctx.left), visitExpression(ctx.right));
		else if (ctx.GE() != null)
			return new GreaterOrEqual(source, line, col, visitExpression(ctx.left), visitExpression(ctx.right));
		else if (ctx.LT() != null)
			return new Less(source, line, col, visitExpression(ctx.left), visitExpression(ctx.right));
		else if (ctx.LE() != null)
			return new LessOrEqual(source, line, col, visitExpression(ctx.left), visitExpression(ctx.right));
		else if (ctx.ADD() != null)
			return new Addition(source, line, col, visitExpression(ctx.left), visitExpression(ctx.right));
		else if (ctx.SUB() != null && ctx.nested == null)
			return new Subtraction(source, line, col, visitExpression(ctx.left), visitExpression(ctx.right));
		else if (ctx.MUL() != null)
			return new Multiplication(source, line, col, visitExpression(ctx.left), visitExpression(ctx.right));
		else if (ctx.DIV() != null)
			return new Division(source, line, col, visitExpression(ctx.left), visitExpression(ctx.right));
		else if (ctx.MOD() != null)
			return new Module(source, line, col, visitExpression(ctx.left), visitExpression(ctx.right));
		else if (ctx.SUB() != null && ctx.nested != null)
			return new Minus(source, line, col, visitExpression(ctx.nested));
		else if (ctx.AS() != null)
			return new Cast(source, line, col, visitType(ctx.ctype).getType(), visitExpression(ctx.nested));
		else if (ctx.IS() != null)
			return new TypeCheck(source, line, col, visitType(ctx.ctype).getType(), visitExpression(ctx.nested));
		else if (ctx.NEW() != null && ctx.newBasicArrayExpr() != null)
			return visitNewBasicArrayExpr(ctx.newBasicArrayExpr());
		else if (ctx.NEW() != null && ctx.newReferenceType() != null)
			return visitNewReferenceType(ctx.newReferenceType());
		else if (ctx.arrayAccess() != null)
			return visitArrayAccess(ctx.arrayAccess());
		else if (ctx.fieldAccess() != null)
			return visitFieldAccess(ctx.fieldAccess());
		else //if (ctx.methodCall() != null)
			return visitMethodCall(ctx.methodCall());
	}
	
	@Override
	public NewArray visitNewBasicArrayExpr(NewBasicArrayExprContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		TypeExpression type = visitPrimitiveType(ctx.primitiveType());
		return new NewArray(source, line, col, type.getType(), parseIndexes(ctx.arrayCreatorRest().index()));
	}
	
	@Override
	public Expression visitNewReferenceType(NewReferenceTypeContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		
		if (ctx.arguments() != null)
			return new NewObject(source, line, col, ctx.IDENTIFIER().getText(), parseArgs(ctx.arguments()));
		else
			return new NewArray(source, line, col, ClassType.mk(ctx.IDENTIFIER().getText()), parseIndexes(ctx.arrayCreatorRest().index()));
	}
	
	@Override
	public ArrayAccess visitArrayAccess(ArrayAccessContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		Variable receiver = buildVar(ctx.IDENTIFIER());
		Index[] indexes = parseIndexes(ctx.index());
		return new ArrayAccess(source, line, col, receiver, indexes); 
	}

	private Index[] parseIndexes(List<IndexContext> list) {
		List<Index> indexes = new ArrayList<>();
		for (IndexContext index : list)
			if (index.LITERAL_DECIMAL() != null)
				indexes.add(new IntLiteral(source, getLine(index), getColumn(index), Integer.parseInt(index.LITERAL_DECIMAL().getText())));
			else 
				indexes.add(buildVar(index.IDENTIFIER()));
		return indexes.toArray(new Index[indexes.size()]);
	}
	
	@Override
	public FieldAccess visitFieldAccess(FieldAccessContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		Receiver receiver = (Receiver) visitReceiver(ctx.receiver());
		String name = ctx.name.getText();
		return new FieldAccess(source, line, col, receiver, name);
	}
	
	@Override
	public Call visitMethodCall(MethodCallContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		Receiver receiver = (Receiver) visitReceiver(ctx.receiver());
		String name = ctx.name.getText();
		Argument[] args = parseArgs(ctx.arguments());
		return new Call(source, line, col, receiver, name, args);
	}
	
	private Argument[] parseArgs(ArgumentsContext argumentsContext) { 
		List<Argument> args = new ArrayList<>();
		for (ArgContext arg : argumentsContext.arg())
			args.add((Argument) visitArg(arg));
		
		return args.toArray(new Argument[args.size()]);
	}
	
	@Override
	public Expression visitArg(ArgContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		if (ctx.THIS() != null)
			return new ThisLiteral(source, line, col, ClassType.mk(partialResult.getName()));
		else if (ctx.IDENTIFIER() != null)
			return buildVar(ctx.IDENTIFIER());
		else if (ctx.literal() != null)
			return visitLiteral(ctx.literal());
		else if (ctx.fieldAccess() != null)
			return visitFieldAccess(ctx.fieldAccess());
		else 
			return visitArrayAccess(ctx.arrayAccess());
	}
	
	@Override
	public Expression visitReceiver(ReceiverContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		if (ctx.THIS() != null)
			return new ThisLiteral(source, line, col, ClassType.mk(partialResult.getName()));
		else if (ctx.SUPER() != null)
			return new SuperLiteral(source, line, col, ClassType.mk(partialResult.getSuperclassName()));
		else // if (ctx.IDENTIFIER() != null)
			return buildVar(ctx.IDENTIFIER());
	}
	
	@Override
	public Expression visitBasicExpr(BasicExprContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		if (ctx.THIS() != null)
			return new ThisLiteral(source, line, col, ClassType.mk(partialResult.getName()));
		else if (ctx.SUPER() != null)
			return new SuperLiteral(source, line, col, ClassType.mk(partialResult.getSuperclassName()));
		else if (ctx.IDENTIFIER() != null)
			return buildVar(ctx.IDENTIFIER());
		else 
			return visitLiteral(ctx.literal());
	}
	
	@Override
	public Literal visitLiteral(LiteralContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		if (ctx.LITERAL_DECIMAL() != null)
			if (ctx.SUB() == null)
				return new IntLiteral(source, line, col, Integer.parseInt(ctx.LITERAL_DECIMAL().getText()));
			else
				return new IntLiteral(source, line, col, -Integer.parseInt(ctx.LITERAL_DECIMAL().getText()));
		else if (ctx.LITERAL_FLOAT() != null)
			if (ctx.SUB() == null)
				return new FloatLiteral(source, line, col, Float.parseFloat(ctx.LITERAL_FLOAT().getText()));
			else
				return new FloatLiteral(source, line, col, -Float.parseFloat(ctx.LITERAL_FLOAT().getText()));
		else if (ctx.LITERAL_BOOL() != null)
			if (Boolean.parseBoolean(ctx.LITERAL_BOOL().getText()))
				return new TrueLiteral(source, line, col);
			else
				return new FalseLiteral(source, line, col);
		else if (ctx.LITERAL_STRING() != null)
			return new StringLiteral(source, line, col, ctx.LITERAL_STRING().getText());
		else
			return new NullLiteral(source, line, col);
	}
	
	@Override
	public Statement visitStatement(StatementContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		
		if (ctx.block() != null)
			return visitBlock(ctx.block());
		if (ctx.assignment() != null)
			return visitAssignment(ctx.assignment());
		else if (ctx.ASSERT() != null)
			return new Assert(source, line, col, visitExpression(ctx.expression()));
		else if (ctx.IF() != null)
			if (ctx.otherwise == null)
				return new If(source, line, col, visitExpression(ctx.condition.expression()), visitStatement(ctx.then));
			else 
				return new If(source, line, col, visitExpression(ctx.condition.expression()), visitStatement(ctx.then), visitStatement(ctx.otherwise));
		else if (ctx.loop() != null)
			return visitLoop(ctx.loop());
		else if (ctx.RETURN() != null)
			return new Return(source, line, col, ctx.expression() == null ? null : visitExpression(ctx.expression()));
		else if (ctx.command != null)
			return new Command(source, line, col, visitExpression(ctx.command));
		else
			return new Nop(source, line, col);
	}
	
	@Override
	public Statement visitLoop(LoopContext ctx) {
		if (ctx.forLoop() != null)
			return visitForLoop(ctx.forLoop());
		else 
			return visitWhileLoop(ctx.whileLoop());
	}
	
	@Override
	public For visitForLoop(ForLoopContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		Statement body = visitStatement(ctx.statement());
		Statement init;
		if (ctx.forDeclaration().forInit().localDeclaration() != null)
			init = visitLocalDeclaration(ctx.forDeclaration().forInit().localDeclaration());
		else 
			init = visitAssignment(ctx.forDeclaration().forInit().assignment());
		Expression cond = visitExpression(ctx.forDeclaration().expression());
		Assignment update = visitAssignment(ctx.forDeclaration().assignment());
		return new For(source, line, col, init, cond, update, body);
	}
	
	@Override
	public While visitWhileLoop(WhileLoopContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		return new While(source, line, col, visitExpression(ctx.parExpr().expression()), visitStatement(ctx.statement()));
	}
	
	@Override
	public Assignment visitAssignment(AssignmentContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		Expression expr = visitExpression(ctx.expression());
		
		if (ctx.IDENTIFIER() != null)
			return new Assignment(source, line, col, buildVar(ctx.IDENTIFIER()), expr);
		else if (ctx.fieldAccess() != null)
			return new Assignment(source, line, col, visitFieldAccess(ctx.fieldAccess()), expr);
		else 
			return new Assignment(source, line, col, visitArrayAccess(ctx.arrayAccess()), expr);
	}

	private Variable buildVar(TerminalNode identifier) {
		return new Variable(source, identifier.getSymbol().getLine(), identifier.getSymbol().getCharPositionInLine(), identifier.getText());
	}
	
	@Override
	public FormalDefinition visitFormal(FormalContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		TypeExpression type = visitType(ctx.type());
		String name = ctx.name.getText();
		return new FormalDefinition(name, line, col, type.getType(), name);
	}
	
	@Override
	public TypeExpression visitTypeOrVoid(TypeOrVoidContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		if (ctx.type() != null)
			return visitType(ctx.type());
		else 
			return new TypeExpression(source, line, col, VoidType.INSTANCE);
	}

	@Override
	public TypeExpression visitType(TypeContext ctx) {
		if (ctx.primitiveType() != null)
			return visitPrimitiveType(ctx.primitiveType());
		else 
			return visitReferenceType(ctx.referenceType());
	}
	
	@Override
	public TypeExpression visitPrimitiveType(PrimitiveTypeContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		if (ctx.BOOLEAN() != null)
			return new TypeExpression(source, line, col, BooleanType.INSTANCE);
		else if (ctx.FLOAT() != null)
			return new TypeExpression(source, line, col, FloatType.INSTANCE);
		else
			return new TypeExpression(source, line, col, IntType.INSTANCE);
	}
	
	@Override
	public TypeExpression visitReferenceType(ReferenceTypeContext ctx) {
		if (ctx.classType() != null)
			return visitClassType(ctx.classType());
		else
			return visitArrayType(ctx.arrayType());
	}
	
	@Override
	public TypeExpression visitArrayType(ArrayTypeContext ctx) {
		TypeExpression nested;
		if (ctx.primitiveType() != null)
			nested = visitPrimitiveType(ctx.primitiveType());
		else 
			nested = visitClassType(ctx.classType());
		
		int line = getLine(ctx);
		int col = getColumn(ctx);
		return new TypeExpression(source, line, col, ArrayType.mk(nested.getType(), ctx.arraySqDeclaration().getChildCount() / 2));
	}
	
	@Override
	public TypeExpression visitClassType(ClassTypeContext ctx) {
		int line = getLine(ctx);
		int col = getColumn(ctx);
		return new TypeExpression(source, line, col, ClassType.mk(ctx.getText()));
	}
}
