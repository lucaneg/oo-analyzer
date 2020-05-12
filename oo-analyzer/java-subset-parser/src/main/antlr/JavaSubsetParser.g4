/**
 * The parser for the java subset
 * 
 * @author Luca Negrini
 */

parser grammar JavaSubsetParser;

@header {
    package it.lucaneg.antlr;
}

options { tokenVocab=JavaSubsetLexer; }

/*
 * GENERAL TOKENS
 */
arraySqDeclaration: (LBRACK RBRACK)+;

/*
 * TYPES
 */
primitiveType: BOOLEAN | INT | FLOAT;
classType: IDENTIFIER (DOT IDENTIFIER)*;
arrayType: (classType | primitiveType) arraySqDeclaration;
referenceType: classType | arrayType;
type: referenceType | primitiveType;
typeOrVoid: type | VOID;

/*
 * PARAMETER LIST
 */
formals: LPAREN (formal (COMMA formal)*)? RPAREN;
formal: ftype=type name=IDENTIFIER;

/*
 * LOCALS 
 */
localDeclaration: type name=IDENTIFIER (ASSIGN expression)?;

/*
 * LITERALS
 */
literal: SUB? LITERAL_DECIMAL | SUB? LITERAL_FLOAT | LITERAL_STRING | LITERAL_BOOL | LITERAL_NULL;

/*
 * CALL PARAMETERS
 */
arguments: LPAREN (arg (COMMA arg)*)? RPAREN;
arg: literal | IDENTIFIER | THIS | fieldAccess | arrayAccess;
 
/*
 * EXPRESSIONS
 */
expression: LPAREN paren=expression RPAREN
	| basicExpr
	| NOT nested=expression
	| left=expression (MUL | DIV | MOD) right=expression
	| left=expression (ADD | SUB) right=expression
	| left=expression (GT | GE | LT | LE) right=expression
	| left=expression (EQUAL | NOTEQUAL) right=expression
	| left=expression (AND | OR) right=expression
	| SUB nested=expression
	| nested=expression AS ctype=type
	| nested=expression IS ctype=type
	| NEW (newBasicArrayExpr | newReferenceType)
	| arrayAccess
	| fieldAccess
	| methodCall;
basicExpr: THIS
    | SUPER
    | IDENTIFIER
    | literal;
newBasicArrayExpr: primitiveType arrayCreatorRest;
newReferenceType: IDENTIFIER (arguments | arrayCreatorRest);
arrayCreatorRest: (LBRACK index RBRACK)+;
arrayAccess: IDENTIFIER (LBRACK index RBRACK)+;
index: LITERAL_DECIMAL | IDENTIFIER;
receiver: THIS | SUPER | IDENTIFIER;
fieldAccess: receiver DOT name=IDENTIFIER;
methodCall: receiver DOT name=IDENTIFIER arguments;

/*
 * STATEMENT
 */
statement: block
	| assignment SEMI
    | ASSERT expression SEMI
    | IF condition=parExpr then=statement (ELSE otherwise=statement)?
    | loop
    | RETURN expression? SEMI
    | skip=SEMI
    | command=expression SEMI;
assignment: (IDENTIFIER | fieldAccess | arrayAccess) ASSIGN expression;
parExpr: LPAREN expression RPAREN;
loop: forLoop | whileLoop;
forLoop: FOR LPAREN forDeclaration RPAREN statement;
whileLoop: WHILE parExpr statement;   
    
forDeclaration: forInit? SEMI expression? SEMI assignment?;
forInit: localDeclaration | assignment;

/*
 * BLOCK
 */
block: LBRACE blockStatement* RBRACE;
blockStatement: localDeclaration SEMI | statement;

/*
 * CLASS MEMBERS
 */
memberDeclarations: (methodDeclaration | fieldDeclaration | constructorDeclaration)*;
fieldDeclaration : ftype=type name=IDENTIFIER SEMI;
constructorDeclaration: name=IDENTIFIER pars=formals code=block;
methodDeclaration: ret=typeOrVoid name=IDENTIFIER pars=formals code=block;

/*
 * CLASS
 */
classDeclaration: CLASS name=IDENTIFIER (EXTENDS superclass=IDENTIFIER)? LBRACE declarations=memberDeclarations RBRACE; 