/* Types Grammar
 * Ines Justo (84804), Luis Pedro Moura (83808)
 * Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 */

grammar Types;

@header{
/* Types Grammar
 * Inês Justo (84804), Luis Pedro Moura (83808)
 * Maria João Lavoura (84681), Pedro Teixeira (84715)
 */
package typesGrammar;
}

typesFile	: prefixDeclar? typesDeclar	  EOF
			| typesDeclar   prefixDeclar? EOF
			;

// Prefixes ------------------------
prefixDeclar: 'prefixes' '{' prefix* '}' 
			;
					
prefix		: ID STRING ':' valueOp
	  		;
	  			
// Types ------------------------
typesDeclar	: 'types' '{' type* '}' 
			;
					
type		: ID STRING 				 			#TypeBasic
			| ID STRING ':' typeOp 					#TypeDerived
			| ID STRING ':' typeOpOr 				#TypeDerivedOr
	  		;
	  		
typeOpOr	: typeOpOrAlt ('|' typeOpOrAlt)*					
			;

typeOpOrAlt : valueOp ID;
	
typeOp		: '(' typeOp ')'						#TypeOpParenthesis
			| typeOp op=('*' | '/') typeOp			#TypeOpMultDiv
			| <assoc=right> ID '^' NUMBER			#TypeOpPower
			| ID									#TypeOpID
			;

// Value ------------------------		
valueOp		: '(' valueOp ')' 						#ValueOpParenthesis
			| <assoc=right> valueOp '^' valueOp		#ValueOpOpPower
			| valueOp op=('/' | '*') valueOp		#ValueOpMultDiv
			| valueOp op=('+' | '-') valueOp 		#ValueOpAddSub
			| NUMBER								#ValueOpNumber
			;
			
// -----------------------------------------------------------------------------
//NEW_LINE			: '\r'? '\n';
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