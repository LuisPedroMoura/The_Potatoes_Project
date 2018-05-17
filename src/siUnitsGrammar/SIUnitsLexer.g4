lexer grammar SIUnitsLexer;

@header{
	package SIUnitsGrammar;
}

// Reserved words
NUMERIC_TYPE	: 'numeric' ;
FACTOR			: 'factor' ;

/**
 * International System of Units - Lexicographic Conventions
 * NOTA: de momento esta informacao esta aqui para que todo o grupo esteja
 * sintonizado no que respeita as regras oficiais de representacao dos
 * numeros e unidades
 * - The value of a quantity is written as a number followed by a space 
 * (representing a multiplication sign) and a unit symbol
 * - Symbols are mathematical entities, not abbreviations, and as such do not
 * have an appended period/full stop (.)
 * - A prefix is part of the unit, and its symbol is prepended to the unit
 * symbol without a separator
 * - Compound prefixes are not allowed.
 * - Symbols for derived units formed by multiplication are joined with a
 * centre dot (nao consigo usar o simbolo :| ) or a non-breaking space; e.g.,
 * N(ponto)m or N m.
 * - Symbols for derived units formed by division are joined with a solidus (/),
 * or given as a negative exponent. E.g., the "metre per second" can be written
 * m/s, m s^-1
 * - The first letter of symbols for units derived from the name of a person is
 * written in upper case; otherwise, they are written in lower case.
 * - Symbols of units do not have a plural form; e.g., 25 kg, not 25 kgs.
 * - Spaces should be used as a thousands separator (1000000) in contrast to
 * commas or periods (1,000,000 or 1.000.000)
 * - Any line-break inside a number, inside a compound unit, or between number
 * and unit should be avoided.
 * - Since the value of "billion" and "trillion" can vary from language to
 * language, the dimensionless terms "ppb" (parts per billion) and "ppt"
 * (parts per trillion) should be avoided. No alternative is suggested in
 * the SI Brochure.
 */
PARENTHESIS_OPEN: '(' ;
PARENTHESIS_CLOSE: ')' ;
DIVIDE: '/' ;
MULTIPLY: '*' ;
ADD: '+';
SUBTRACT: '-' ;
POWER: '^' NUMBER ;
//DIMENTION:	PREFIX? (BASE_UNIT | DERIVED_UNIT) ;
ID: [a-zA-Z]* ;
fragment INT: '0' | [1-9][0-9]? ;
NUMBER:	'0' | ('-' | '+')? INT ('.'[0-9]+)? ;
ERROR: . ;
