lexer grammar PotatoesLexer;

@header{
	package potatoesGrammar;
}

HEADER_BEGIN : 'header*';
HEADER_END   : '**';

EOL : ';';

PACKAGE : 'package';
IMPORT  : 'import';

CLASS : 'class';
FUN   : 'fun';
ARRAY : 'Array';

SCOPE_BEGIN       : '{';
SCOPE_END         : '}';
PARENTHESIS_BEGIN : '(';
PARENTHESIS_END   : ')';
DIAMOND_BEGIN     : '<';
DIAMOND_END       : '>';

EQUAL  : '=';
EQUALS : '==';
COLON  : ':';
COMMA  : ',';

VOID_TYPE    : 'void';
NUMBER_TYPE  : 'Number';
BOOLEAN_TYPE : 'boolean';
STRING_TYPE  : 'String';

IF     : 'if';
FOR    : 'for';
WHILE  : 'while';
WHEN   : 'when';
ARROW  : '->';

STATIC : 'static';
RETURN : 'return';

LESS_THAN        : '<';
LESS_OR_EQUAL    : '<=';
GREATER_THAN     : '>';
GREATER_OR_EQUAL : '>=';
DIFFERENT        : '!=';

ID              : LETTER (LETTER | DIGIT)*;
fragment LETTER : 'a'..'z'|'A'..'Z'|'_';
BOOLEAN         : 'false' | 'true';
NUMBER          : (DIGIT+)
		        | (DIGIT+ '.' DIGIT+ | '.' DIGIT+)
		        ;
fragment DIGIT  : '0'..'9';
STRING          : '"' (ESC | . )*? '"';
fragment ESC    : '//"' | '\\\\';

LINE_COMMENT   : '//' .*? '\n' -> skip;
COMMENT        : '/*' .*? '*/' -> skip;

WS : [ \t\n\r]+ -> skip;
