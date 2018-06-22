lexer grammar PotatoesLexer;

@header{
	package potatoesGrammar;
}

// END OF LINE
EOL               : ';';

// SCOPE-------------------------------------------------------------------
SCOPE_BEGIN       : '{';
SCOPE_END         : '}';

// ASSIGNMENT OPERATOR-----------------------------------------------------
EQUAL             : '=';

// FUNCTIONS---------------------------------------------------------------
FUN 			  : 'fun';

PARENTHESIS_BEGIN : '(';
PARENTHESIS_END   : ')';

COMMA             : ',';

RETURN            : 'return';

// CONTROL FLOW------------------------------------------------------------
IF                : 'if';
ELSE			  : 'else';
FOR               : 'for';
WHILE             : 'while';
WHEN              : 'when';

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

// OPERATIONS--------------------------------------------------------------
MULTIPLY          : '*';
DIVIDE            : '/';
ADD               : '+';
SUBTRACT		  : '-';
POWER             : '^';
MODULUS           : '%';

// STRUCTURES--------------------------------------------------------------
ARRAY			  : 'Array';
LENGTH			  : 'length';
DIAMOND_BEGIN	  : '<';
DIAMOND_END		  : '>';

// VARS-------------------------------------------------------------------- 
ID                : [a-z] [a-zA-Z0-9_]*;

// TYPE AGROUPMENT---------------------------------------------------------
NUMBER            : '0'
				  | [0-9] ('.'[0-9]+)?
				  | [1-9][0-9]* ('.'[0-9]+)?
				  ;

STRING            : '"' (ESC | . )*? '"';
fragment ESC      : '//"' | '\\\\';

// COMMENTS----------------------------------------------------------------
LINE_COMMENT      : '//' .*? '\n' -> skip;
COMMENT           : '/*' .*? '*/' -> skip;

// WHITESPACE--------------------------------------------------------------
WS                : [ \t\n\r]+ -> skip;