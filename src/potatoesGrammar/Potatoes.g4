grammar Potatoes;

@header{
	package potatoesGrammar;
}

//--------------------------------------------------------------------------
//PARSER--------------------------------------------------------------------
//--------------------------------------------------------------------------


// MAIN RULES------------------------------------------------------------------
program				: using code+ EOF	
					;
	
using				: USING STRING EOL
					;	
					
code				: varDeclaration EOL								#code_Declaration 
					| assignment EOL								#code_Assignment
					| function										#code_Function
					;	
// CLASS-----------------------------------------------------------------------	
		
statement			: varDeclaration EOL							#statement_Declaration
					| assignment EOL								#statement_Assignment
					| controlFlowStatement							#statement_Control_Flow_Statement
					| functionCall EOL								#statement_FunctionCall
					| functionReturn								#statement_Function_Return
					| print											#statement_Print
					;

assignment			: varDeclaration '=' '!' var					#assignment_Var_Declaration_Not_Boolean
					| varDeclaration '=' value						#assignment_Var_Declaration_Value
					| varDeclaration '=' comparison					#assignment_Var_Declaration_Comparison
					| varDeclaration '=' operation					#assignment_Var_Declaration_Operation
					| varDeclaration '=' functionCall				#assignment_Var_Declaration_FunctionCall
					
					| var '=' '!' var								#assignment_Var_Not_Boolean
					| var '=' value									#assignment_Var_Value
					| var '=' comparison							#assignment_Var_Comparison
					| var '=' operation								#assignment_Var_Operation
					| var '=' functionCall							#assingment_Var_FunctionCall
					
					;

// FUNCTIONS-------------------------------------------------------------------
function			: FUN MAIN '{' statement* '}'					#function_Main
					| FUN ID '(' (type var (',' type var)* )* ')' '{' statement* '}'	#function_ID
					;

functionReturn		: RETURN (var|value|operation) EOL
					;
					
functionCall		: ID '(' ((var|value|operation) (',' (var|value|operation))* )* ')'
					;

// CONTROL FLOW STATMENTS------------------------------------------------------
controlFlowStatement: condition
 					| forLoop
 					| whileLoop
 					| when
 					;	
 
forLoop				: FOR '(' assignment? EOL logicalOperation EOL assignment ')'
					  '{' statement* '}' //[MJ] must have scopes for the sake of simplicity 
 					;
 			
whileLoop			: WHILE '(' logicalOperation ')' ('{' statement* '}' | EOL)
					;
 			
when				: WHEN '(' var ')' '{' whenCase* '}'
					;
 			
whenCase			: value '->' '{' statement* '}'
 					;
		
//[MJ] must have scopes for the sake of simplicity 
condition			: ifCondition elseIfCondition* elseCondition?
					;

ifCondition			: IF '(' logicalOperation ')' '{' statement* '}'
					;
					
elseIfCondition		: ELSE IF '(' logicalOperation ')' '{' statement* '}'
					;
					
elseCondition		: ELSE '{' statement* '}'
					;


// LOGICAL OPERATIONS----------------------------------------------------------
logicalOperation	: '(' logicalOperation ')'								# logicalOperation_Parenthesis
					| logicalOperation op=('&&' | '||') logicalOperation	# logicalOperation_Operation
					| logicalOperand										# logicalOperation_logicalOperand
					;
					
logicalOperand 		: comparison						# logicalOperand_Comparison
					| '!' comparison					# logicalOperand_Not_Comparison
					| var								# logicalOperand_Var
					| '!' var							# logicalOperand_Not_Var
					| value								# logicalOperand_Value
					| '!' value							# logicalOperand_Not_Value
					;
						
comparison			: compareOperation compareOperator compareOperation
					;
					
compareOperation	: operation		# compareOperation_Operation
					| BOOLEAN		# compareOperation_BOOLEAN
					;
			
compareOperator		: '=='
					| '!='
					| '<'
					| '<='
					| '>'
					| '>='
					;			

// OPERATIONS------------------------------------------------------------------
operation			: cast operation								#operation_Cast	
					| '(' operation ')' 							#operation_Parenthesis
					| operation op=('*' | '/' | '%') operation		#operation_Mult_Div_Mod
					| '-' operation									#operation_Simetric
					| operation  op=('+' | '-') operation			#operation_Add_Sub
					|<assoc=right> operation '^' operation			#operation_Power
					| var											#operation_Var
					| functionCall									#operation_FunctionCall
					| NUMBER										#operation_NUMBER
					;
			
// PRINTS----------------------------------------------------------------------

print				: PRINT  '(' (printVar ('+'printVar)* ) ')' EOL		# print_Print
					| PRINTLN '(' (printVar ('+'printVar)* ) ')' EOL	# print_Println
					;

printVar			: value	#printVar_Value
					| var	#printVar_Var
					;
	
// VARS------------------------------------------------------------------------ 
var					: ID
					;

varDeclaration		: type ID
					;			

type				: NUMBER_TYPE		# type_Number_Type
					| BOOLEAN_TYPE		# type_Boolean_Type
					| STRING_TYPE		# type_String_Type
					| VOID_TYPE			# type_Void_Type
					| ID				# type_ID_Type
					;
	
value				: cast NUMBER		# value_Cast_Number
					| NUMBER			# value_Number
					| BOOLEAN			# value_Boolean
					| STRING			# value_String
					;
		

// CASTS-----------------------------------------------------------------------
cast				: '(' ID ')'
					;				

//--------------------------------------------------------------------------
//LEXER---------------------------------------------------------------------
//--------------------------------------------------------------------------

//
USING			  : 'using';

// END OF LINE
EOL               : ';';

// FUNCTIONS---------------------------------------------------------------
MAIN			  : 'main' ;
FUN 			  : 'fun';
RETURN            : 'return';

// CONTROL FLOW------------------------------------------------------------
IF                : 'if';
ELSE			  : 'else';
FOR               : 'for';
WHILE             : 'while';
WHEN              : 'when';

// TYPE NAME---------------------------------------------------------------
NUMBER_TYPE       : 'number';
BOOLEAN_TYPE      : 'boolean';
STRING_TYPE       : 'string';
VOID_TYPE         : 'void';

// BOOLEAN VALUES----------------------------------------------------------
BOOLEAN           : 'false' | 'true';

// PRINTS------------------------------------------------------------------
PRINT			  : 'print';
PRINTLN			  : 'println';

// VARS-------------------------------------------------------------------- 
ID                : [a-z] [a-zA-Z0-9_]*;

// TYPE AGROUPMENT---------------------------------------------------------
NUMBER            : '0'
				  | [0-9] ('.'[0-9]+)?
				  | [1-9][0-9]* ('.'[0-9]+)?
				  ;

STRING            : '"' (ESC | . )*? '"';
fragment ESC      : '//"' | '\\\\';

// COMMENTS----------------------------------------------------------------
LINE_COMMENT      : '//' .*? '\n' -> skip;
COMMENT           : '/*' .*? '*/' -> skip;

// WHITESPACE--------------------------------------------------------------
WS                : [ \t\n\r]+ -> skip;
		
		
		
		