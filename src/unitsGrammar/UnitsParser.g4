parser grammar UnitsParser;
options{
	tokenVocab = UnitsLexer;
}

@header{
	package unitsGrammar;
}

program	: const_declaration? units_declaration
		| units_declaration? const_declaration
		;

// CONSTANTS ------------------------------------------------------------------
const_declaration	: CONSTANTS SCOPE_OPEN (constant NEW_LINE)* SCOPE_CLOSE
					;
					
constant	: CONST_ID ARG_OPEN ID ARG_CLOSE COLON const_op
	  		;
	  			
const_op	: PAR_OPEN const_op PAR_CLOSE					#const_op_parenthesis
			| const_op op=(MULTIPLY | DIVIDE) const_op		#const_op_mult_div
			| const_op op=(ADD | SUBTRACT) const_op			#const_op_add_sub
			| const_op POWER<assoc=right> NUMBER			#const_op_power
			| CONST_ID										#const_op_CONST_ID
			| NUMBER										#const_op_NUMBER
			;

// UNITS ----------------------------------------------------------------------
units_declaration	: UNITS SCOPE_OPEN (unit NEW_LINE)* SCOPE_CLOSE
					;
					
unit		: UNITS_ID ARG_OPEN ID ARG_CLOSE COLON units_op?
	  		;
	  			
units_op	: PAR_OPEN units_op PAR_CLOSE					#units_op_parenthesis
			| units_op OR units_op							#units_op_OR
			| units_op op=(MULTIPLY | DIVIDE) units_op		#units_op_mult_div
			| units_op POWER<assoc=right> NUMBER			#units_op_power
			| UNITS_ID										#units_op_UNITS_ID
			| NUMBER										#units_op_NUMBER
			;
