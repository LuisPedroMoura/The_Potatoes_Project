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
					
code				: declaration EOL								#code_Declaration 
					| assignment EOL								#code_Assignment
					| function										#code_Function
					;	
// CLASS-----------------------------------------------------------------------	
		
statement			: declaration EOL								#statement_Declaration
					| assignment EOL								#statement_Assignment
					| controlFlowStatement							#statement_Control_Flow_Statement
					| functionCall EOL								#statement_FunctionCall
					// [IJ] - o return tem de estar dentro da statement
					//      - controlar posteriormente os erros
					| functionReturn								#statement_Function_Return
					| print											#statement_Print
					;
					
declaration			: arrayDeclaration								#declaration_Aarray
					| varDeclaration								#declaration_Var
					;


assignment			: varDeclaration '=' '!'? var					#assignment_Var_Declaration_Var //boolean vars
					| varDeclaration '=' value						#assignment_Var_Declaration_Value
					| varDeclaration '=' comparison					#assignment_Var_Declaration_Comparison
					| varDeclaration '=' operation					#assignment_Var_Declaration_Operation
					| varDeclaration '=' functionCall				#assignment_Var_Declaration_FunctionCall
					| arrayDeclaration '=' valuesList				#assignment_Array_ValuesList
					| arrayDeclaration '=' functionCall				#assignment_Array_FunctionCall
					
					| var '=' '!'? var								#assignment_Var_Var //boolean vars
					| var '=' value									#assignment_Var_Value
					| var '=' comparison							#assignment_Var_Comparison
					| var '=' operation								#assignment_Var_Operation
					| var '=' valuesList							#assignment_Var_ValueList
					| var '=' functionCall							#assingment_Var_FunctionCall
					
					| arrayAccess '=' '!'? var						#assignment_Var_Var //boolean vars
					| arrayAccess '=' value							#assignment_Var_Value
					| arrayAccess '=' comparison					#assignment_Var_Comparison
					| arrayAccess '=' operation						#assignment_Var_Operation
					| arrayAccess '=' valuesList					#assignment_Var_ValueList
					| arrayAccess '=' functionCall					#assingment_Var_FunctionCall
					;

// CASTS-----------------------------------------------------------------------

cast				: '(' ID ')'
					;

// FUNCTIONS-------------------------------------------------------------------
function			: FUN MAIN '{' statement* '}'
					| FUN ID '(' (type var (',' type var)* )* ')' ':' type '{' statement* '}'
					;

functionReturn		: RETURN (var|value|operation) EOL
					;
					
functionCall		: ID '(' ((var|value|operation|arrayAccess) (',' (var|value|operation|arrayAccess))* )* ')'
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
condition			: IF '(' logicalOperation ')' '{' statement* '}'
					  (ELSE IF '(' logicalOperation ')' '{' statement* '}')*
					  (ELSE '{' statement* '}')?
					;

// LOGICAL OPERATIONS----------------------------------------------------------
logicalOperation	: logicalOperand (logicalOperator logicalOperand)?
					;
					
logicalOperand 		: '!'? comparison
					| '!'? var	// boolean var
					| '!'? value // true or false
					;
					
logicalOperator		: '&&'
					| '||'
					;
						
comparison			: operation compareOperator operation
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
					| arrayAccess									#operation_ArrayAccess
					| arrayLength									#operation_ArrayLength
					| NUMBER										#operation_NUMBER
					;
			

// STRUCTURES------------------------------------------------------------------
arrayDeclaration	: ARRAY '<' type ',' NUMBER'>' var
					;
					
arrayType			: ARRAY '<' type '>'
					;			
					
arrayAccess			: ID '['  (var|NUMBER) ']'
					;
					
arrayLength			: LENGTH '(' var ')' 
					;

// PRINTS----------------------------------------------------------------------

print				: (PRINT | PRINTLN) '(' ((value | var) ('+' (value | var))* ) ')' EOL
					;

	
// VARS------------------------------------------------------------------------ 
var					: ID
					;

// [LM] - to the tester: please verify what happens if declaration is: x z;
//						 where x and z are both variables (because user created
//						 types can only be solved into ID's)
varDeclaration		: type var
					| ID var	// to declare personalized type variables
					;			

type				: NUMBER_TYPE
					| BOOLEAN_TYPE
					| STRING_TYPE
					| VOID_TYPE
					| ID	// to declare personalized type variables
					| arrayType
					;
	
value				: cast NUMBER
					| BOOLEAN
					| STRING
					;
		
valuesList			: '{' (value|var) (',' (value|var))* '}'
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

// STRUCTURES--------------------------------------------------------------
ARRAY			  : 'Array';
LENGTH			  : 'length';

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
		
		
		
		