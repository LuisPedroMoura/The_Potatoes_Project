/* Types Grammar - Lexer
 * Inês Justo (84804), Luis Pedro Moura (83808)
 * Maria João Lavoura (84681), Pedro Teixeira (84715)
 */
 
lexer grammar TypesLexer;

@header{
/* Types Grammar - Lexer
 * Inês Justo (84804), Luis Pedro Moura (83808)
 * Maria João Lavoura (84681), Pedro Teixeira (84715)
 */
package typesGrammar;
}

// Reserved words --------------------------------------------------------------
PREFIXES		: 'prefixes' ;
TYPES			: 'types' ;
 
// Reserved chars --------------------------------------------------------------
SCOPE_OPEN		: '{' ; 
SCOPE_CLOSE		: '}' ; 
QUOTE_MARK		: '"' ;
COLON			: ':' ;
NEW_LINE		: '\r'? '\n';

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
ID				: LETTER (LETTER | DIGIT)*;
STRING			: QUOTE_MARK (ESC | . )*? QUOTE_MARK;
NUMBER			: '0' | ('-' | '+')? INT ('.' DIGIT+)? ;

fragment LETTER	: [a-zA-Z] |'_';
fragment ESC	: '\\"' | '\\\\' ;

fragment DIGIT	: [0-9];
fragment INT	: '0' | [1-9] DIGIT* ;

				
// Comments. Others ------------------------------------------------------------
COMMENTS		: '//' .*? '\n' -> skip;
WS				: [ \t]+ -> skip;

ERROR			: . ;