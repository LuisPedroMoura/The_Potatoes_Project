parser grammar PotatoesParser;
options{
	tokenVocab = potatoesLexer;
}

@header{
	package projeto.potatoesGrammar;
}



program	:	code* EOF	
		;
		
code	:	headerDeclaration? classDeclaration? classContent
		|	classDeclaration classContent
		;
	
/*
 *-------------------------------------------------------------------
 *HEADER-------------------------------------------------------------
 *-------------------------------------------------------------------
 */	
		
/* 
 * HEADER_BEGIN = 'header*'
 * HEADER_END = '**'
*/
headerDeclaration:	HEADER_BEGIN  javaCode HEADER_END
				 ;

/*
 * EOL = ';'
 */
javaCode: .*? 
		;
		

/*
 *-------------------------------------------------------------------
 *CLASS--------------------------------------------------------------
 *-------------------------------------------------------------------
 */	

/*
 * CLASS = class
 */
classDeclaration:	CLASS CLASS_NAME
				;	
		
		
/*
 * SCOPE_BEGIN = {
 * SCOPE_END = }
 * STATIC = static
 */
classContent:	SCOPE_BEGIN statement* SCOPE_END
			;

statement	:	STATIC attributes //private by default
			|	STATIC? functions //public by default
			;


/*
 *-------------------------------------------------------------------
 *FUNCTIONS----------------------------------------------------------
 *-------------------------------------------------------------------
 */
functions	: function_signature function_content    
			;

function_signature	:	FUN STRING PARENTHESIS_BEGIN (type STRING (COMMA type STRING)* )* PARENTHESIS_END COLON (type | VOID_TYPE)
					;

function_content	:	SCOPE_BEGIN function_content* SCOPE_END
					|	attributes
					|	operations
					|	conditions
					|	loops
					;


/*
 *-------------------------------------------------------------------
 *STRUCTURES---------------------------------------------------------
 *-------------------------------------------------------------------
 */	
/*
 * Array<number>
 */
arrays	:	arrayDeclaration  EOL
		|	arrayDeclaration arrayInitialization
		;
		
		
arrayDeclaration: ARRAY DIAMOND_BEGIN type DIAMOND_END STRING
				;
				
				
arrayInitialization	: EQUALS SCOPE_BEGIN value ( COMMA value)* SCOPE_END EOL
					| EQUALS EOL
					;
		


	

/*
 *-------------------------------------------------------------------
 *-------------------------------------------------------------------
 *-------------------------------------------------------------------
 */

attributes	:	vars
			|	arrays
			;
			

/*
 *-------------------------------------------------------------------
 *VARS---------------------------------------------------------------
 *-------------------------------------------------------------------
 */
 
 vars	:	varsDeclaration EOL
		|	varsDeclaration varsInitialization  
 		;

varsDeclaration	: type STRING
				;
				
varsInitialization	: EQUALS value EOL
					;
					

/*
 *-------------------------------------------------------------------
 *-------------------------------------------------------------------
 *-------------------------------------------------------------------
 */				
					
					
/*
 * NUMBER_TYPE = number ... em minusculas
 */
type:	NUMBER_TYPE
	|	BOOLEAN_TYPE
	|	STRING_TYPE
	;
	
/*
 * NUMBER = INT / DOUBLE ... WHATEVER :P ... ETC
 */
value	:	NUMBER
		|	BOOLEAN
		|	STRING
		;
				

		
		
		
		