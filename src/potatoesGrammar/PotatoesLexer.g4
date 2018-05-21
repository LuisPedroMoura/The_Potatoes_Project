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
MAIN			  : 'main' ;

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
ELSE			  : 'else';
FOR               : 'for';
WHILE             : 'while';
WHEN              : 'when';
ARROW             : '->';

// TYPE NAME---------------------------------------------------------------
NUMBER_TYPE       : 'number';
BOOLEAN_TYPE      : 'boolean';
STRING_TYPE       : 'string';
VOID_TYPE         : 'void';

// BOOLEAN VALUES----------------------------------------------------------
BOOLEAN           : 'false' | 'true';

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
SUBTRACT		  : '-';
POWER             : '^';
MODULUS           : '%';
INCREMENT         : '++';
DECREMENT         : '--';

// STRUCTURES--------------------------------------------------------------
ARRAY             : 'Array';

// These symbols are used, but where replaced with rules in Parser
//DIAMOND_BEGIN     : '<';
//DIAMOND_END       : '>';

// VARS-------------------------------------------------------------------- 
ID                : [a-z] [a-zA-Z0-9_]*;

// TYPE AGROUPMENT---------------------------------------------------------
NUMBER            : '0'
				  | ('-' | '+')? [0-9] ('.'[0-9]+)?
				  | ('-' | '+')? [1-9][0-9]* ('.'[0-9]+)?
				  ;

STRING            : '"' (ESC | . )*? '"';
fragment ESC      : '//"' | '\\\\';

// COMMENTS----------------------------------------------------------------
LINE_COMMENT      : '//' .*? '\n' -> skip;
COMMENT           : '/*' .*? '*/' -> skip;

// WHITESPACE--------------------------------------------------------------
WS                : [ \t\n\r]+ -> skip;