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

typesFile	: NEW_LINE* (prefixDeclar)?  (typesDeclar)	  NEW_LINE*
			| NEW_LINE* (typesDeclar)    (prefixDeclar)?  NEW_LINE*
			;

// Prefixes -------------------------------------------------------------------
prefixDeclar: PREFIXES SCOPE_OPEN NEW_LINE*
			  (prefix NEW_LINE+)* 
			  SCOPE_CLOSE NEW_LINE*
			;
					
prefix		: ID STRING COLON prefixOp		
	  		;
	  			
prefixOp	: PAR_OPEN prefixOp PAR_CLOSE					#PrefixOpParenthesis
			| prefixOp op=(MULTIPLY | DIVIDE  ) prefixOp	#PrefixOpMultDiv
			| prefixOp op=(ADD 		| SUBTRACT) prefixOp	#PrefixOpAddSub
			| <assoc=right> prefixOp POWER      prefixOp	#PrefixOpPower
			| ID											#PrefixOpID
			| NUMBER										#PrefixOpNUMBER
			;

// Types ----------------------------------------------------------------------
typesDeclar	: TYPES SCOPE_OPEN NEW_LINE*
			  (type NEW_LINE+)* 
			  SCOPE_CLOSE NEW_LINE* 
			;
					
type		: ID STRING 				 					#TypeBasic
			| ID STRING (COLON typeOp) 						#TypeDerived
	  		;
	  			
typeOp		: PAR_OPEN typeOp PAR_CLOSE													#TypeOpParenthesis
			| factor typeOp (OR factor typeOp)*				#TypeOpOr
			| typeOp op=(MULTIPLY | DIVIDE) typeOp			#TypeOpMultDiv
			| <assoc=right> typeOp POWER NUMBER				#TypeOpPower
			| ID											#TypeOpID
			| NUMBER										#TypeOpNUMBER
			;
			
factor 		: PAR_OPEN factor PAR_CLOSE 					#FactorParenthesis
			| factor op=(DIVIDE | MULTIPLY) factor			#FactorMultDiv
			| factor op=(ADD 	| SUBTRACT) factor 			#FactorAddSub
			| NUMBER										#FactorNUMBER
			;