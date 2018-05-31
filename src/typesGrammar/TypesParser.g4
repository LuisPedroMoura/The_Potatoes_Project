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

typesFile	: NEW_LINE* prefixDeclar? NEW_LINE+ typesDeclar	  NEW_LINE* EOF
			| NEW_LINE* typesDeclar   NEW_LINE+ prefixDeclar? NEW_LINE* EOF
			;

// Prefixes -------------------------------------------------------------------
prefixDeclar: PREFIXES SCOPE_OPEN NEW_LINE*
			  (prefix NEW_LINE+)* 
			  SCOPE_CLOSE 
			;
					
prefix		: ID STRING COLON valueOp
	  		;
	  			

// Types ----------------------------------------------------------------------
typesDeclar	: TYPES SCOPE_OPEN NEW_LINE*
			  (type NEW_LINE+)* 
			  SCOPE_CLOSE 
			;
					
type		: ID STRING 				 					#TypeBasic
			| ID STRING COLON typeOp 						#TypeDerived
			| ID STRING COLON typeOpOr 						#TypeDerivedOr
	  		;
	  		
typeOpOr	: typeOpOrAlt (OR typeOpOrAlt)*					
			;

typeOpOrAlt : valueOp ID;
	
typeOp		: PAR_OPEN typeOp PAR_CLOSE						#TypeOpParenthesis
			| typeOp op=(MULTIPLY | DIVIDE) typeOp			#TypeOpMultDiv
			| <assoc=right> ID POWER NUMBER					#TypeOpPower
			| ID											#TypeOpID
			;

// Value ----------------------------------------------------------------------		
valueOp		: PAR_OPEN valueOp PAR_CLOSE 					#ValueOpParenthesis
			| valueOp op=(DIVIDE | MULTIPLY) valueOp		#ValueOpMultDiv
			| valueOp op=(ADD 	 | SUBTRACT) valueOp 		#ValueOpAddSub
			| <assoc=right> valueOp POWER 	 valueOp		#ValueOpOpPower
			| NUMBER										#ValueOpNumber
			;