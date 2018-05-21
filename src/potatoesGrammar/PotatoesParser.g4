parser grammar PotatoesParser;
options{
	tokenVocab = PotatoesLexer;
}

@header{
	package potatoesGrammar;
}


// MAIN RULES------------------------------------------------------------------
program	:	code* EOF	
		;
		
code	: (declaration EOL)* 
		| (assignment EOL)* 
		| function*
		;
// CLASS-----------------------------------------------------------------------
		
statement			: declaration EOL			#statement_declaration
					| assignment EOL			#statement_assignment
					| control_flow_statement	#statement_controlFlowStatement
					| function_call EOL			#statement_function_call
					;
					
declaration			: array_declaration			#declaration_array
					| var_declaration			#declaration_var
					;

assignment			: array_declaration assignment_operator values_list	#assignment_array
					| var_declaration assignment_operator operation		#assigment_var_declaration_operation
					| var_declaration assignment_operator NOT? var		#assignment_varDeclaration_var
					| var_declaration assignment_operator value			#assignment_varDeclaration_value
					| var_declaration assignment_operator function_call	#assigment_var_declaration_functionCall
					| var INCREMENT										#assigment_varIncrement
					| var DECREMENT 									#assigment_varDecrement
					| var assignment_operator operation					#assigment_var_operation
					| var assignment_operator NOT? var					#assignment_var_var
					| var assignment_operator value						#assignment_var_value
					| var assignment_operator values_list				#assigment_var_valueList
					| var assignment_operator function_call				#assigment_var_functionCall
					;
// [LM] add instanceof operator ?? very usefiul in array, of Numbers	
assignment_operator	: EQUAL
					| ADD_EQUAL
					| SUB_EQUAL
					| MULT_EQUAL
					| DIV_EQUAL
					| MOD_EQUAL
					;

// FUNCTIONS-------------------------------------------------------------------
function			: FUN (ID | MAIN) PARENTHESIS_BEGIN (type var (COMMA type var)* )*
					  PARENTHESIS_END COLON type 
					  SCOPE_BEGIN statement* function_return? SCOPE_END
					;

function_return		: RETURN (var | value | operation) EOL
					;
					
function_call		: ID PARENTHESIS_BEGIN ((var|value|operation) (COMMA (var|value|operation))* )*
					  PARENTHESIS_END
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
			  (SCOPE_BEGIN statement* SCOPE_END | EOL)
 			;
 			
when		: WHEN PARENTHESIS_BEGIN (var) PARENTHESIS_END
			  SCOPE_BEGIN when_case* SCOPE_END
 			;
 			
when_case	: value ARROW SCOPE_BEGIN? statement* SCOPE_END?
 			;
		
condition	: IF PARENTHESIS_BEGIN logical_operation PARENTHESIS_END
			  SCOPE_BEGIN statement* SCOPE_END
			  (ELSE IF PARENTHESIS_BEGIN logical_operation PARENTHESIS_END
			  SCOPE_BEGIN statement* SCOPE_END)*
			  (ELSE SCOPE_BEGIN statement* SCOPE_END )?
			  
 			;

// LOGICAL OPERATIONS----------------------------------------------------------
logical_operation	: logical_operand (logical_operator logical_operand)?
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
operation	: PARENTHESIS_BEGIN operation PARENTHESIS_END		#operation_parenthesis
			| operation op=(MULTIPLY | DIVIDE) operation		#operation_mult_div
			| operation op=(ADD | SUBTRACT) operation			#operation_add_sub
			| operation POWER NUMBER							#operation_power
			| operation MODULUS NUMBER 							#operation_modulus
			| operation INCREMENT								#operation_increment
			| operation DECREMENT 								#operation_decrement
			| operation compare_operator operation				#operation_comparison
			| var												#operation_expr
			| function_call										#operation_functionCall
			| NUMBER											#operation_NUMBER
			;

// STRUCTURES------------------------------------------------------------------
array_declaration	: ARRAY diamond_begin type diamond_end var
					;
					
diamond_begin		: LESS_THAN
					;
					
diamond_end			: GREATER_THAN
					; 
		 
// VARS------------------------------------------------------------------------ 
var					: ID
					;

// [LM] - to the tester: please verify what happens if declaration is: x z;
//						 where x and z are both variables (because user created
//						 types can only be solved into ID's)
var_declaration		: type var
					| ID var	// to declare personalized type variables
					;			
				
type				: NUMBER_TYPE
					| BOOLEAN_TYPE
					| STRING_TYPE
					| VOID_TYPE
					| array_declaration
					;
	
value				: NUMBER
					| BOOLEAN
					| STRING
					;
		
values_list			: value (COMMA value)*
					;
				

		
		
		
		