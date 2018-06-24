/* Types Grammar
 * Ines Justo (84804), Luis Pedro Moura (83808)
 * Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 */

grammar Types;

@header{
/**
 * 
 * <b>Types Grammar</b><p>
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
package typesGrammar;
}

typesFile	: prefixDeclar? typesDeclar	  EOF
			| typesDeclar   prefixDeclar? EOF
			;

// Prefixes ------------------------
prefixDeclar: 'prefixes' '{' prefix* '}' 
			;
					
prefix		: ID STRING ':' value
	  		;
	  			
// Types ------------------------
typesDeclar	: 'types' '{' type* '}' 
			;
					
type		: ID STRING 				 			#Type_Basic
			| ID STRING ':' typeOp 					#Type_Derived
			| ID STRING ':' typeOpOr 				#Type_Derived_Or
	  		;
	  		
typeOpOr	: typeOpOrAlt ('|' typeOpOrAlt)*							
			;

typeOpOrAlt : '(' value ')' ID;
	
typeOp		: '(' typeOp ')'						#Type_Op_Parenthesis
			| typeOp op=('*' | '/') typeOp			#Type_Op_MultDiv
			| <assoc=right> ID '^' NUMBER			#Type_Op_Power
			| ID									#Type_Op_ID
			;

// Value ------------------------		
value		: '(' value ')' 						#Value_Parenthesis
			| <assoc=right> value '^' value			#Value_Power
			| value op=('/' | '*') value			#Value_MultDiv
			| value op=('+' | '-') value 			#Value_AddSub
			| NUMBER								#Value_Number
			;
			
// -----------------------------------------------------------------------------
ID					: LETTER (LETTER | DIGIT)*;
STRING				: QUOTE_MARK (ESC | . )*? QUOTE_MARK;
NUMBER				: '0' | ('-' | '+')? INT ('.' DIGIT+)? ;

fragment LETTER		: [a-zA-Z] |'_';
fragment QUOTE_MARK	: '"' ;
fragment ESC		: '\\"' | '\\\\' ;

fragment DIGIT		: [0-9];
fragment INT		: '0' | [1-9] DIGIT* ;

COMMENTS			: '//' .*? '\n' -> skip;
WS					: [ \n\r\t]+ 	-> skip;
ERROR				: . ;