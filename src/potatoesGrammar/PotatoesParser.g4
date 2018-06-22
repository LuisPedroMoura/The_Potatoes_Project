parser grammar PotatoesParser;
options{
	tokenVocab = PotatoesLexer;
}

@header{
	package potatoesGrammar;
}


// MAIN RULES------------------------------------------------------------------
program	:	code+ EOF	
		;
		
code	: declaration EOL	#class_contentDeclaration 
		| assignment EOL	#class_contentAssignment
		| function			#class_contentFunction
		;	
// CLASS-----------------------------------------------------------------------	
		
statement			: declaration EOL			#statement_declaration
					| assignment EOL			#statement_assignment
					| control_flow_statement	#statement_controlFlowStatement
					| function_call EOL			#statement_function_call
					// [IJ] - o return tem de estar dentro da statement
					//      - controlar posteriormente os erros
					| function_return			#statement_function_return
					| print						#statement_print
					;
					
declaration			: array_declaration			#declaration_array
					| var_declaration			#declaration_var
					;


assignment			: var_declaration assignment_operator NOT? var		#assignment_varDeclaration_var //boolean vars
					| var_declaration assignment_operator value			#assignment_varDeclaration_value
					| var_declaration assignment_operator comparison	#assignment_var_declaration_comparison
					| var_declaration assignment_operator operation		#assignment_var_declaration_operation
					| array_declaration assignment_operator values_list	#assignment_array
					| var_declaration assignment_operator function_call	#assignment_var_declaration_functionCall
					
					| var assignment_operator NOT? var					#assignment_var_var //boolean vars
					| var assignment_operator value						#assignment_var_value
					| var assignment_operator comparison				#assignment_var_comparison
					| var assignment_operator operation					#assignment_var_operation
					| var assignment_operator values_list				#assignment_var_valueList
					| var assignment_operator function_call				#assingment_var_functionCall
					
					| array_access assignment_operator NOT? var			#assignment_var_var //boolean vars
					| array_access assignment_operator value			#assignment_var_value
					| array_access assignment_operator comparison		#assignment_var_comparison
					| array_access assignment_operator operation		#assignment_var_operation
					| array_access assignment_operator values_list		#assignment_var_valueList
					| array_access assignment_operator function_call	#assingment_var_functionCall
					;

assignment_operator	: EQUAL
					;

// FUNCTIONS-------------------------------------------------------------------
function			: FUN MAIN SCOPE_BEGIN statement* SCOPE_END
					| FUN ID PARENTHESIS_BEGIN (type var (COMMA type var)* )*
					  PARENTHESIS_END COLON type 
					  SCOPE_BEGIN statement* SCOPE_END
					;

function_return		: RETURN (var|value|operation) EOL
					;
					
function_call		: ID PARENTHESIS_BEGIN ((var|value|operation|array_access) (COMMA (var|value|operation|array_access))* )*
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
			  SCOPE_BEGIN statement* SCOPE_END //[MJ] must have scopes for the sake of simplicity 
 			;
 			
while_loop	: WHILE PARENTHESIS_BEGIN logical_operation PARENTHESIS_END
			  (SCOPE_BEGIN statement* SCOPE_END | EOL)
 			;
 			
when		: WHEN PARENTHESIS_BEGIN var PARENTHESIS_END
			  SCOPE_BEGIN when_case* SCOPE_END
 			;
 			
when_case	: value ARROW SCOPE_BEGIN? statement* SCOPE_END?
 			;
		
//[MJ] must have scopes for the sake of simplicity 
condition	: IF PARENTHESIS_BEGIN logical_operation PARENTHESIS_END
			  SCOPE_BEGIN statement* SCOPE_END
			  (ELSE IF PARENTHESIS_BEGIN logical_operation PARENTHESIS_END
			  SCOPE_BEGIN statement* SCOPE_END)*
			  (ELSE SCOPE_BEGIN statement* SCOPE_END)?
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
operation	: PARENTHESIS_BEGIN operation PARENTHESIS_END 	#operation_parenthesis
			| operation op=(MULTIPLY | DIVIDE) operation 	#operation_mult_div
			| operation  op=(ADD | SUBTRACT) operation		#operation_add_sub
			| <assoc=right> operation POWER NUMBER			#operation_power
			| operation MODULUS NUMBER 						#operation_modulus
			| var											#operation_expr
			| function_call									#operation_functionCall
			| array_access									#operation_array_access
			| array_length									#operation_array_length
			| NUMBER										#operation_number
			;

// STRUCTURES------------------------------------------------------------------
array_declaration	: array_type var
					;
					
array_type			:  ARRAY diamond_begin type diamond_end
					;					
					
diamond_begin		: LESS_THAN
					;
					
diamond_end			: GREATER_THAN
					;
					
array_access		: ID SQUARE_BRACKET_BEGIN  (var|NUMBER) SQUARE_BRACKET_END
					;
					
array_length		: var LENGTH
					;

// PRINTS----------------------------------------------------------------------

print	: (PRINT|PRINTLN) PARENTHESIS_BEGIN ((value | var) (ADD (value | var))* ) PARENTHESIS_END EOL
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
					| ID	// to declare personalized type variables
					| array_type
					;
	
value				: NUMBER
					| BOOLEAN
					| STRING
					;
		
values_list			: (value|var) (COMMA (value|var))*
					;
				

		
		
		
		