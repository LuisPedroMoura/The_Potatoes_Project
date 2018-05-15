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
classDeclaration: CLASS STRING
				;	
		
		
/*
 * SCOPE_BEGIN = {
 * SCOPE_END = }
 * STATIC = static
 */
classContent	: SCOPE_BEGIN (declaration | function)* SCOPE_END
				;



statement	: STATIC attribute //private by default
			| declaration
			| condition
			| assignment
			| control_flow_statement
			| function_call
			;

assignment	: (var | array) EQUAL (var | values_list) EOL
			;
			
declaration	: type var
			| type var EQUAL (var | values_list)
			;


/*
 *-------------------------------------------------------------------
 *FUNCTIONS----------------------------------------------------------
 *-------------------------------------------------------------------
 */
function	: function_signature function_content    
			;

function_signature	:	FUN ID PARENTHESIS_BEGIN (type var (COMMA type var)* )*
						PARENTHESIS_END COLON type 
						SCOPE_BEGIN function_content* SCOPE_END
					;

function_content	:	statement* function_return?
					;

function_return		:	RETURN var EOL
					;
					
/*
 *-------------------------------------------------------------------
 *STRUCTURES---------------------------------------------------------
 *-------------------------------------------------------------------
 */	
/*
 * Array<number>
 */
array	:	ARRAY DIAMOND_BEGIN type DIAMOND_END ID
		;

/*
 *-------------------------------------------------------------------
 *CONDITION----------------------------------------------------------
 *-------------------------------------------------------------------
 */


					
/*
 *-------------------------------------------------------------------
 *CONDITION----------------------------------------------------------
 *-------------------------------------------------------------------
 */
 
 control_flow_statement	: condition
 						| for_loop
 						| while_loop
 						| when
 						;	
 
 
 		
 for_loop	: FOR PARENTHESIS_BEGIN assignment? EOL comparison EOL operation PARENTHESIS_END SCOPE_BEGIN statement* SCOPE_END
 			;
 			
 while_loop	: WHILE PARENTHESIS_BEGIN (comparison | var | value) PARENTHESIS_END SCOPE_BEGIN statement* SCOPE_END
 			;
 			
 when		: WHEN PARENTHESIS_BEGIN (var) PARENTHESIS_END SCOPE_BEGIN when_case* SCOPE_END
 			;
 			
when_case	: value ARROW statement EOL
 			;
// [LM] var and value on the comparison to use 'true', 'false'or var with boolean value			
condition	: IF PARENTHESIS_BEGIN (comparison | var | value) PARENTHESIS_END SCOPE_BEGIN statement* SCOPE_END
 			;
 			
comparison	: (value | var) compare_operator (value | var)
			;
			
compare_operator	: EQUALS
					| LESS_THAN
					| LESS_OR_EQUAL
					| GREATER_THAN
					| GREATER_OR_EQUAL
					| DIFFERENT
					;			
 

 
			
/*
 *-------------------------------------------------------------------
 *-------------------------------------------------------------------
 *-------------------------------------------------------------------
 */

attribute	: var
			| array
			;
			

/*
 *-------------------------------------------------------------------
 *VARS---------------------------------------------------------------
 *-------------------------------------------------------------------
 */
 
var	: ID
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
	|	VOID_TYPE
	|	array
	;
	
/*
 * NUMBER = INT / DOUBLE ... WHATEVER :P ... ETC
 */
value	:	NUMBER
		|	BOOLEAN
		|	STRING
		;
		
values_list	: value (COMMA value)*
			;
				

		
		
		
		