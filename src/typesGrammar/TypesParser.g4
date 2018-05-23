/* Types Grammar - Parser
 * Inês Justo (84804), Luis Pedro Moura (83808)
 * Maria João Lavoura (84681), Pedro Teixeira (84715)
 */

parser grammar TypesParser;
options{
	tokenVocab = TypesLexer;
}

@header{
/* Types Grammar - Parser
 * Inês Justo (84804), Luis Pedro Moura (83808)
 * Maria João Lavoura (84681), Pedro Teixeira (84715)
 */
package typesGrammar;
}

program	: NEW_LINE* (prefix_declar)?    units_declaration NEW_LINE*
		| NEW_LINE*  units_declaration (prefix_declar)?   NEW_LINE*
		;

// Constants ------------------------------------------------------------------
prefix_declar : PREFIXES SCOPE_OPEN NEW_LINE*
				(prefix NEW_LINE+)* 
				SCOPE_CLOSE NEW_LINE*
			  ;
					
prefix		: ID ARG_OPEN ID ARG_CLOSE COLON prefix_op		
	  		;
	  			
prefix_op	: PAR_OPEN prefix_op PAR_CLOSE					#prefixOpParenthesis
			| prefix_op op=(MULTIPLY | DIVIDE) prefix_op	#prefixOpMultDiv
			| prefix_op op=(ADD | SUBTRACT)    prefix_op	#prefixOpAddSub
			| <assoc=right> prefix_op POWER    prefix_op	#prefixOpPower
			| ID											#prefixOpID
			| NUMBER										#prefixOpNUMBER
			;

// Units ----------------------------------------------------------------------
units_declaration	: TYPES SCOPE_OPEN NEW_LINE*
					  (unit NEW_LINE+)* 
					  SCOPE_CLOSE NEW_LINE* 
					;
					
unit		: ID ARG_OPEN ID ARG_CLOSE					#UnitBasic
			| ID ARG_OPEN ID ARG_CLOSE (COLON units_op)	#UnitDerived
	  		;
	  			
units_op	: PAR_OPEN units_op PAR_CLOSE				#UnitsOpParenthesis
			| units_op OR units_op						#UnitsOpOr
			| units_op op=(MULTIPLY | DIVIDE) units_op	#UnitsOpMultDiv
			| <assoc=right> units_op POWER NUMBER		#UnitsOpPower
			| ID										#UnitsOpID
			| NUMBER									#UnitsOpNUMBER
			;