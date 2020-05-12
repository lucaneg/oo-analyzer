/**
 * The lexer for the java subset
 * 
 * @author Luca Negrini
 */

lexer grammar JavaSubsetLexer;

@lexer::header {package it.lucaneg.antlr;}

 // =========================== KEYWORDS ===========================  
 
 // basic types
 BOOLEAN: 'boolean';
 FLOAT: 'float';
 INT: 'int';
 VOID: 'void';
 
 // branching
 IF: 'if';
 ELSE: 'else';
 
 // loop
 FOR: 'for';
 WHILE: 'while';
 
 // cast
 IS: 'is';
 AS: 'as';
 
 // class declaration
 CLASS: 'class';
 EXTENDS: 'extends';
 
 // method body    
 RETURN: 'return';
 THIS: 'this';
 SUPER: 'super';
 NEW: 'new';
 ASSERT: 'assert';

 // =========================== LITERALS ===========================  
 LITERAL_DECIMAL: '0' | [1-9]Digits?;
 LITERAL_FLOAT: (Digits '.' Digits? | '.' Digits) [fF]?;
 LITERAL_BOOL: 'true' | 'false';
 LITERAL_STRING: '"' (~["\\\r\n] | EscapeSequence)* '"';
 LITERAL_NULL: 'null';

 // =========================== SYMBOLS ===========================  
 
 // parenthesis
 LPAREN: '(';
 RPAREN: ')';
 LBRACE: '{';
 RBRACE: '}';
 LBRACK: '[';
 RBRACK: ']';
 
 // separators
 SEMI: ';';
 COMMA: ',';
 DOT: '.';

 // operators
 ASSIGN: '=';
 GT: '>';
 LT: '<';
 NOT: '!';
 EQUAL: '==';
 LE: '<=';
 GE: '>=';
 NOTEQUAL: '!=';
 AND: '&&';
 OR: '||';
 ADD: '+';
 SUB: '-';
 MUL: '*';
 DIV: '/';
 MOD: '%';

 // =========================== WHITESPACE ===========================  
 WS: [ \t\r\n\u000C]+ -> channel ( HIDDEN );

 // =========================== COMMENTS ===========================  
 COMMENT: '/*' .*? '*/' -> channel ( HIDDEN );
 LINE_COMMENT: '//' ~[\r\n]* -> channel ( HIDDEN );

 // =========================== IDENTIFIERS ===========================  
 IDENTIFIER: Letter LetterOrDigit*;

 // =========================== RULES ===========================  

 fragment EscapeSequence: '\\' [btnfr"'\\] | '\\' ([0-3]? [0-7])? [0-7];
 fragment Digits: [0-9]+;
 fragment LetterOrDigit: Letter	| [0-9];
 fragment Letter: [a-zA-Z$_];