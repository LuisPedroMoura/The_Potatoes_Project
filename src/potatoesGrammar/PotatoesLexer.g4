lexer grammar PotatoesLexer;

@header{
	package potatoesGrammar;
}

// HEADER------------------------------------------------------------------
HEADER_BEGIN      : 'header*';
HEADER_END        : '**';

// END OF LINE
EOL               : ';';

// CLASS-------------------------------------------------------------------
CLASS             : 'class';

SCOPE_BEGIN       : '{';
SCOPE_END         : '}';

// ASSIGNMENT OPERATORS----------------------------------------------------
EQUAL             : '=';
ADD_EQUAL         : '+=';
SUB_EQUAL         : '-=';
MULT_EQUAL        : '*=';
DIV_EQUAL         : '/=';
MOD_EQUAL         : '%=';

// FUNCTIONS---------------------------------------------------------------
FUN               : 'fun';

PARENTHESIS_BEGIN : '(';
PARENTHESIS_END   : ')';

COMMA             : ',';
COLON             : ':';

RETURN            : 'return';

// CONTROL FLOW------------------------------------------------------------
IF                : 'if';
FOR               : 'for';
WHILE             : 'while';
WHEN              : 'when';
ARROW             : '->';

// TYPE NAME---------------------------------------------------------------
NUMBER_TYPE       : 'number';
BOOLEAN_TYPE      : 'boolean';
STRING_TYPE       : 'string';
VOID_TYPE         : 'void';

// LOGICAL OPERATORS-------------------------------------------------------
NOT               : '!';
AND               : '&';
OR                : '|';

// COMPARE OPERATORS-------------------------------------------------------
EQUALS            : '==';
NOT_EQUAL         : '!=';
LESS_THAN         : '<';
LESS_OR_EQUAL     : '<=';
GREATER_THAN      : '>';
GREATER_OR_EQUAL  : '>=';

// OPERATIONS--------------------------------------------------------------
MULTIPLY          : '*';
DIVIDE            : '/';
ADD               : '+';
SUBTRACT          : '-';
POWER             : '^';
MODULUS           : '%';
INCREMENT         : '++';
DECREMENT         : '--';

// STRUCTURES--------------------------------------------------------------
ARRAY             : 'Array';
DIAMOND_BEGIN     : '<';
DIAMOND_END       : '>';

// VARS-------------------------------------------------------------------- 
ID                : LETTER (LETTER | DIGIT)*;
fragment LETTER   : [a-zA-Z_] ;

// TYPE AGROUPMENT---------------------------------------------------------
NUMBER            : '0' | ('-' | '+')? INT ('.'[0-9]+)? ;
INT               : '0' | [1-9][0-9]* ;
fragment DIGIT    : [0-9];
BOOLEAN           : 'false' | 'true';
STRING            : '"' (ESC | . )*? '"';
fragment ESC      : '//"' | '\\\\';

// COMMENTS----------------------------------------------------------------
LINE_COMMENT      : '//' .*? '\n' -> skip;
COMMENT           : '/*' .*? '*/' -> skip;

// WHITESPACE--------------------------------------------------------------
WS                : [ \t\n\r]+ -> skip;