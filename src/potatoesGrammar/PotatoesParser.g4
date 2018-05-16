parser grammar PotatoesParser;
options{
	tokenVocab = potatoesLexer;
}

@header{
	package potatoesGrammar;
}


// MAIN RULES------------------------------------------------------------------
program	:	code* EOF	
		;
		
code	:	headerDeclaration? classDeclaration? classContent
		|	classDeclaration classContent
		;
	
// HEADER----------------------------------------------------------------------
// HEADER_BEGIN = 'header*' ; HEADER_END = '**'
headerDeclaration	: HEADER_BEGIN  javaCode HEADER_END
					;
					
// EOL = ';'
javaCode			: .*? 
					;
		

// CLASS-----------------------------------------------------------------------

// CLASS = class
classDeclaration: CLASS STRING
				;	
		
		
// SCOPE_BEGIN = { ; SCOPE_END = } ; STATIC = static
classContent	: SCOPE_BEGIN (declaration | function)* SCOPE_END
				;

statement		: declaration EOL
				| assignment EOL
				| control_flow_statement
				| function_call EOL
				;

assignment		: (declaration | var | array) assignment_operator (var | values_list | value)
				;
// [LM] add instanceof operator ?? very usefiul in array, of Numbers	
assignment_operator	: EQUAL
					| ADD_EQUAL
					| SUB_EQUAL
					| MULT_EQUAL
					| DIV_EQUAL
					| MOD_EQUAL
					;
			
declaration		: type var
				;

// FUNCTIONS-------------------------------------------------------------------
function			: function_signature function_content    
					;

function_signature	: FUN ID PARENTHESIS_BEGIN (type var (COMMA type var)* )*
					  PARENTHESIS_END COLON type 
					  SCOPE_BEGIN function_content* SCOPE_END
					;

function_content	: statement* function_return?
					;

function_return		: RETURN var EOL
					;
					
function_call		: ID PARENTHESIS_BEGIN (type var (COMMA type var)* )*
					  PARENTHESIS_END
					;
					
// STRUCTURES------------------------------------------------------------------
array	: ARRAY DIAMOND_BEGIN type DIAMOND_END ID
		;

// CONTROL FLOW STATMENTS------------------------------------------------------
 control_flow_statement	: condition
 						| for_loop
 						| while_loop
 						| when
 						;	
 
for_loop	: FOR PARENTHESIS_BEGIN
			  assignment? EOL logical_operation EOL operation PARENTHESIS_END
			  SCOPE_BEGIN statement* SCOPE_END
 			;
 			
while_loop	: WHILE PARENTHESIS_BEGIN logical_operation PARENTHESIS_END
			  SCOPE_BEGIN statement* SCOPE_END
 			;
 			
when		: WHEN PARENTHESIS_BEGIN (var) PARENTHESIS_END
			  SCOPE_BEGIN when_case* SCOPE_END
 			;
 			
when_case	: value ARROW statement EOL
 			;
		
condition	: IF PARENTHESIS_BEGIN logical_operation PARENTHESIS_END
			  SCOPE_BEGIN statement* SCOPE_END
 			;

// LOGICAL OPERATIONS----------------------------------------------------------
logical_operation	: logical_operand (AND | OR) logical_operand
					;
					
logical_operand 	: NOT? comparison
					| NOT? var	// boolean var
					| NOT? value // true or false
					;
					
logical_operator	: AND
					| OR
					;
						
comparison			: operation compare_operator operation
					;
			
compare_operator	: EQUALS
					| NOT_EQUAL
					| LESS_THAN
					| LESS_OR_EQUAL
					| GREATER_THAN
					| GREATER_OR_EQUAL
					;			

// OPERATIONS------------------------------------------------------------------
operation	: expr (MULTIPLY | DIVIDE) expr
			| expr (ADD | SUBTRACT)	expr
			| expr POWER
			| expr MODULUS INT
			| expr INCREMENT
			| expr DECREMENT 
			| PARENTHESIS_BEGIN expr PARENTHESIS_END
			| expr
			;
			
expr		: var
			| value
			; 
 
// VARS------------------------------------------------------------------------
 
var	: ID
	;				
				
// NUMBER_TYPE = number (in lowercase)
type		: NUMBER_TYPE
			| BOOLEAN_TYPE
			| STRING_TYPE
			| VOID_TYPE
			| array
			;
	
value		: NUMBER
			| BOOLEAN
			| STRING
			| INT
			;
		
values_list	: value (COMMA value)*
			;
				

		
		
		
		