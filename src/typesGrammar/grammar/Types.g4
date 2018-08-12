/***************************************************************************************
*	Title: PotatoesProject - Types Grammar Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Author of version 1.0: Pedro Teixeira (https://pedrovt.github.io),
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

grammar Types;

@header{
package typesGrammar.grammar;
}

// -----------------------------------------------------------------------------
// Parser
typesFile	: declaration* EOF 
			;
			
declaration : typesDeclaration
			| structureDeclaration
			| prefixDeclaration
			;
	  			
// -----------------------------------------------------------------------------
// Types
typesDeclaration	: 'types' '{' (type EOL)* '}' 
					;
					
type	: ID STRING 				 			#Type_Basic
		| ID STRING ':' typesDerivation			#Type_Derived
		| ID STRING? ':' typesEquivalence 		#Type_Equivalent
		| ID '[' ID ']' ':' typesEquivalence	#Type_Class
	  	;
	  		
typesEquivalence	: equivalentType ('|' equivalentType)*							
					;

equivalentType : '(' value ')' ID;
	
typesDerivation	: '(' typesDerivation ')'								#Type_Op_Parenthesis
					| typesDerivation op=('*' | '/') typesDerivation	#Type_Op_MultDiv
					| <assoc=right> ID '^' NUMBER						#Type_Op_Power
					| ID												#Type_Op_ID
					;

// -----------------------------------------------------------------------------
// Structures

structureDeclaration	: 'structures' '{' (structure EOL)* '}'
						;

structure 	: ID STRING ':' typesAssociation
			;
			
typesAssociation	: equivalentType ('&' equivalentType)*
					;

// -----------------------------------------------------------------------------
// Prefixes
prefixDeclaration	: 'prefixes' '{' (prefix EOL)* '}' 
					;
					
prefix		: ID STRING ':' value
	  		;

// -----------------------------------------------------------------------------
// Value
value		: '(' value ')' 						#Value_Parenthesis
			| '-' value								#Value_Simetric
			| <assoc=right> value '^' value			#Value_Power
			| value op=('/' | '*') value			#Value_MultDiv
			| value op=('+' | '-') value 			#Value_AddSub
			| NUMBER								#Value_Number
			;
			
// -----------------------------------------------------------------------------
// Lexer
EOL					: ';';

ID					: [a-zA-Z] [a-zA-Z0-9_]*;

fragment QUOTE_MARK	: '"' ;
fragment ESC		: '\\"' | '\\\\' ;
STRING				: QUOTE_MARK (ESC | . )*? QUOTE_MARK;

fragment DIGIT		: [0-9];
fragment INT		: '0' | [1-9] DIGIT* ;
NUMBER				: '0' | INT ('.' DIGIT+)? ;

COMMENTS			: '//' .*? '\n' -> skip;
WS					: [ \n\r\t]+ 	-> skip;
ERROR				: . ;