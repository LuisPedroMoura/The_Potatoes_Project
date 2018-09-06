/***************************************************************************************
*	Title: PotatoesProject - Units Grammar Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Author of version 1.0: Pedro Teixeira (https://pedrovt.github.io),
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

grammar Units;

@header{
package unitsGrammar.grammar;
}

// -----------------------------------------------------------------------------
// Parser
unitsFile	: declaration* EOF 
			;
			
declaration : unitsDeclaration
			| structureDeclaration
			| prefixDeclaration
			;
	  			
// -----------------------------------------------------------------------------
// Units
unitsDeclaration	: 'units' '{' defineDimensionless? (unit EOL)* '}' 
					;

defineDimensionless	: 'define' 'dimensionless' ':' ID EOL
					;
	
unit	: ID STRING 				 			#Unit_Basic
		| ID STRING 	':' unitsDerivation		#Unit_Derived
		| ID STRING?	':' unitsEquivalence 	#Unit_Equivalent
		| ID '[' ID ']' ':' unitsEquivalence	#Unit_Class
	  	;
	  		
unitsEquivalence	: equivalentUnit ('|' equivalentUnit)*							
					;
					
equivalentUnit		: '(' value ')' ID;
	
unitsDerivation		: '(' unitsDerivation ')'							#Unit_Op_Parenthesis
					| unitsDerivation op=('*' | '/') unitsDerivation	#Unit_Op_MultDiv
					| <assoc=right> unitsDerivation '^' NUMBER			#Unit_Op_Power
					| ID												#Unit_Op_ID
					;

// -----------------------------------------------------------------------------
// Structures

structureDeclaration	: 'structures' '{' (structure EOL)* '}'
						;

structure 	: ID STRING ':' unitsAssociation
			;
			
unitsAssociation	: equivalentUnit ('&' equivalentUnit)*
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