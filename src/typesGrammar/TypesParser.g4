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

typesFile	: NEW_LINE* prefixDeclar?  typesDeclar	  NEW_LINE*
			| NEW_LINE* typesDeclar    prefixDeclar?  NEW_LINE*
			;

// Prefixes -------------------------------------------------------------------
prefixDeclar: PREFIXES SCOPE_OPEN NEW_LINE*
			  (prefix NEW_LINE+)* 
			  SCOPE_CLOSE NEW_LINE+
			;
					
prefix		: ID STRING COLON valueOp
	  		;
	  			
prefixOp	: PAR_OPEN prefixOp PAR_CLOSE					#PrefixOpParenthesis
			| prefixOp op=(MULTIPLY | DIVIDE  ) prefixOp	#PrefixOpMultDiv
			| prefixOp op=(ADD 		| SUBTRACT) prefixOp	#PrefixOpAddSub
			| ID											#PrefixOpID
			| NUMBER										#PrefixOpNUMBER
			;

// Types ----------------------------------------------------------------------
typesDeclar	: TYPES SCOPE_OPEN NEW_LINE*
			  (type NEW_LINE+)* 
			  SCOPE_CLOSE NEW_LINE+ 
			;
					
type		: ID STRING 				 					#TypeBasic
			| ID STRING (COLON typeOp | typeOpOr) 			#TypeDerived
	  		;
	  		
typeOpOr	: valueOp ID (OR valueOp ID)*					
			;

	  			
typeOp		: PAR_OPEN typeOp PAR_CLOSE						#TypeOpParenthesis
			| typeOp op=(MULTIPLY | DIVIDE) typeOp			#TypeOpMultDiv
			| <assoc=right> ID POWER NUMBER					#TypeOpPower
			| ID											#TypeOpID
			;
			
valueOp		: PAR_OPEN valueOp PAR_CLOSE 					#ValueOpParenthesis
			| valueOp op=(DIVIDE | MULTIPLY) valueOp		#ValueOpMultDiv
			| valueOp op=(ADD 	 | SUBTRACT) valueOp 		#ValueOpAddSub
			| <assoc=right> valueOp POWER 	 valueOp		#ValueOpOpPower
			| NUMBER										#ValueOpNumber
			;