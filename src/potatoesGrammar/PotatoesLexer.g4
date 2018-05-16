lexer grammar PotatoesLexer;

@header{
	package potatoesGrammar;
}

// [IJ] HEADER------------------------------------------------------------------
HEADER_BEGIN      : 'header*';
HEADER_END        : '**';

// [IJ] END OF LINE
EOL               : ';';


// [IJ] CLASS-------------------------------------------------------------------
CLASS             : 'class';
//STATIC : 'static';

SCOPE_BEGIN       : '{';
SCOPE_END         : '}';

// [IJ] ASSIGNMENT OPERATORS----------------------------------------------------
EQUAL             : '=';
ADD_EQUAL         : '+=';
SUB_EQUAL         : '-=';
MULT_EQUAL        : '*=';
DIV_EQUAL         : '/=';
MOD_EQUAL         : '%=';


// [IJ] FUNCTIONS---------------------------------------------------------------
FUN               : 'fun';

PARENTHESIS_BEGIN : '(';
PARENTHESIS_END   : ')';

COMMA             : ',';
COLON             : ':';

RETURN            : 'return';

// [IJ] CONTROL FLOW------------------------------------------------------------
IF                : 'if';
FOR               : 'for';
WHILE             : 'while';
WHEN              : 'when';
ARROW             : '->';

// [IJ] LOGICAL OPERATORS-------------------------------------------------------
NOT               : '!';
AND               : '&';
OR                : '|';

// [IJ] COMPARE OPERATORS-------------------------------------------------------
EQUALS            : '==';
NOT_EQUAL         : '!=';
LESS_THAN         : '<';
LESS_OR_EQUAL     : '<=';
GREATER_THAN      : '>';
GREATER_OR_EQUAL  : '>=';

// [IJ] OPERATIONS--------------------------------------------------------------
MULTIPLY          : '*';
DIVIDE            : '/';
ADD               : '+';
SUBTRACT          : '-';
POWER             : '^';
MODULUS           : '%';
INCREMENT         : '++';
DECREMENT         : '--';

// [IJ] STRUCTURES--------------------------------------------------------------
ARRAY             : 'Array';
DIAMOND_BEGIN     : '<';
DIAMOND_END       : '>';

// [IJ] VARS-------------------------------------------------------------------- 
ID                : LETTER (LETTER | DIGIT)*;
fragment LETTER   : 'a'..'z' | 'A'..'Z' | '_';

// [IJ] TYPE NAME---------------------------------------------------------------
NUMBER_TYPE       : 'Number';
BOOLEAN_TYPE      : 'boolean';
STRING_TYPE       : 'String';
VOID_TYPE         : 'void';

// [IJ] TYPE AGROUPMENT---------------------------------------------------------
NUMBER            : (INT) 
		          | (INT '.' INT | '.' INT)
		          ;
INT               : DIGIT+;
fragment DIGIT    : '0'..'9';
BOOLEAN           : 'false' | 'true';
STRING            : '"' (ESC | . )*? '"';
fragment ESC      : '//"' | '\\\\';

// [IJ] COMMENTS----------------------------------------------------------------
LINE_COMMENT      : '//' .*? '\n' -> skip;
COMMENT           : '/*' .*? '*/' -> skip;

// [IJ] WHITESPACE--------------------------------------------------------------
WS                : [ \t\n\r]+ -> skip;