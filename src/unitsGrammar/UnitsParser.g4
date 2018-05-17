/* Units Grammar - Parser
 * Inês Justo (84804), Luis Pedro Moura (83808)
 * Maria João Lavoura (84681), Pedro Teixeira (84715)
 */

parser grammar UnitsParser;
options{
	tokenVocab = UnitsLexer;
}

@header{
	package unitsGrammar;
}

program	: const_declaration? NEW_LINE units_declaration
		| units_declaration? NEW_LINE const_declaration
		;

// Constants ------------------------------------------------------------------
const_declaration : CONSTANTS SCOPE_OPEN 
				    (constant NEW_LINE)* 
				    SCOPE_CLOSE
				  ;
					
constant	: ID ARG_OPEN ID ARG_CLOSE COLON const_op
	  		;
	  			
const_op	: PAR_OPEN const_op PAR_CLOSE				#const_op_parenthesis
			| const_op op=(MULTIPLY | DIVIDE) const_op	#const_op_mult_div
			| const_op op=(ADD | SUBTRACT) 	  const_op	#const_op_add_sub
			| <assoc=right> const_op POWER    const_op	#const_op_power
			| ID										#const_op_ID
			| NUMBER									#const_op_NUMBER
			;

// Units ----------------------------------------------------------------------
units_declaration	: UNITS SCOPE_OPEN 
					  (unit NEW_LINE)* 
					  SCOPE_CLOSE
					;
					
unit		: ID ARG_OPEN ID ARG_CLOSE COLON units_op?
	  		;
	  			
units_op	: PAR_OPEN units_op PAR_CLOSE				#units_op_parenthesis
			| units_op OR units_op						#units_op_or
			| units_op op=(MULTIPLY | DIVIDE) units_op	#units_op_mult_div
			| <assoc=right> units_op POWER NUMBER		#units_op_power
			| ID										#units_op_ID
			| NUMBER									#units_op_NUMBER
			;