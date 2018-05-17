/* Units Grammar - Lexer
 * Inês Justo (84804), Luis Pedro Moura (83808)
 * Maria João Lavoura (84681), Pedro Teixeira (84715)
 */
 
lexer grammar UnitsLexer;

@header{
	package unitsGrammar;
}

// Reserved words --------------------------------------------------------------
CONSTANTS		: 'constants' ;
UNITS			: 'units' ;
 
// Reserved chars --------------------------------------------------------------
SCOPE_OPEN		: '{' ; 
SCOPE_CLOSE		: '}' ; 
ARG_OPEN		: '[' ;
ARG_CLOSE		: ']' ;
COLON			: ':' ;
NEW_LINE		: '\n';

// Reserved chars : operators --------------------------------------------------
PAR_OPEN		: '(' ;
PAR_CLOSE		: ')' ;
DIVIDE			: '/' ;
MULTIPLY		: '*' ;
ADD				: '+' ;
SUBTRACT		: '-' ;
OR				: '|' ;
POWER			: '^' ;

// Numbers and Identifiers -----------------------------------------------------
ID				: [a-zA-Z]+ ;

fragment INT	: '0' | [1-9][0-9]? ;
NUMBER			: '0' | ('-' | '+')? INT ('.'[0-9]+)? ;

COMMENTS		: '//' .*? '\n' -> skip;
WS				: [ \t\r\n]+ -> skip;

ERROR			: . ;