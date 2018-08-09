/* Potatoes Grammar
 * Ines Justo (84804), Luis Pedro Moura (83808)
 * Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 */
 
grammar Potatoes;

@header{
	package potatoesGrammar.grammar;
}

// -----------------------------------------------------------------------------
// Parser

// ----------------------------------------------
// Main Rules
program				: using code+ EOF	
					;
	
using				: USING STRING EOL
					;	
					
code				: varDeclaration EOL							#code_Declaration 
					| assignment EOL								#code_Assignment
					| function										#code_Function
					;
					
// ----------------------------------------------
// Rules
		
statement			: varDeclaration EOL							#statement_Declaration
					| assignment EOL								#statement_Assignment
					| controlFlowStatement							#statement_Control_Flow_Statement
					| functionCall EOL								#statement_FunctionCall
					| functionReturn EOL								#statement_Function_Return
					| print	EOL										#statement_Print
					;

assignment			: varDeclaration '=' expression					#assignment_Var_Declaration_Expression
					| var '=' expression							#assignment_Var_Expression
					;

// ----------------------------------------------
// Functions

function			: FUN MAIN scope										#function_Main
					| FUN ID '(' (type var (',' type var)* )* ')' scope		#function_ID
					;

functionReturn		: RETURN (var|value|expression)
					;
					
functionCall		: ID '(' ((var|value|expression) (',' (var|value|expression))* )* ')'
					;

// ----------------------------------------------
// Control Flow Statements

controlFlowStatement: condition
 					| forLoop
 					| whileLoop
 					;	

forLoop				: FOR '(' assignment? EOL expression EOL assignment ')' scope 
 					;
 			
whileLoop			: WHILE '(' expression ')' scope
					;
 			
condition			: ifCondition elseIfCondition*					#condition_withoutElse
					| ifCondition elseIfCondition* elseCondition	#condition_withElse
					;

ifCondition			: IF '(' expression ')' scope
					;
					
elseIfCondition		: ELSE IF '(' expression ')' scope
					;
					
elseCondition		: ELSE scope
					;			

scope				: '{' statement* '}'
					;
					
// ----------------------------------------------
// Operations

expression			: '(' expression ')' 							#expression_Parenthesis
					| cast expression								#expression_Cast
					| op=('-'|'!') expression						#expression_UnaryOperators
					|<assoc=right> expression '^' expression		#expression_Power
					| expression op=('*' | '/' | '%') expression	#expression_Mult_Div_Mod
					| expression  op=('+' | '-') expression			#expression_Add_Sub				
					| expression op=('<'|'<='|'>'|'>=') expression	#expression_RelationalQuantityOperators
					| expression op=('=='|'!=') expression			#expression_RelationalEquality
					| expression op=('&&'|'||') expression			#expression_logicalOperation
					| var											#expression_Var
					| value											#expression_Value
					| functionCall									#expression_FunctionCall
					;
		
// ----------------------------------------------	
// Prints

print				: printType=(PRINT | PRINTLN)  '(' expression ')'
					;

// ----------------------------------------------
// Variables

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
	
value				: NUMBER			# value_Number
					| BOOLEAN			# value_Boolean
					| STRING			# value_String
					;
		
// ----------------------------------------------
// Casts

cast				: '(' ID ')'
					;				

// -----------------------------------------------------------------------------
// Lexer

USING			  : 'using';

// Separator between instructions
EOL               : ';';

// Functions
MAIN			  : 'main' ;
FUN 			  : 'fun';
RETURN            : 'return';

// Control Flow
IF                : 'if';
ELSE			  : 'else';
FOR               : 'for';
WHILE             : 'while';

// Reserved Types
NUMBER_TYPE       : 'number';
BOOLEAN_TYPE      : 'boolean';
STRING_TYPE       : 'string';
VOID_TYPE         : 'void';

// Boolean Values
BOOLEAN           : 'false' | 'true';

// Prints
PRINT			  : 'print';
PRINTLN			  : 'println';

// Variables 
ID                : [a-z] [a-zA-Z0-9_]*;

// Type Agroupment
NUMBER            : '0'
				  | [0-9] ('.'[0-9]+)?
				  | [1-9][0-9]* ('.'[0-9]+)?
				  ;

STRING            : '"' (ESC | . )*? '"';
fragment ESC      : '//"' | '\\\\';

// Comments & White Space
LINE_COMMENT      : '//' .*? '\n' -> skip;
COMMENT           : '/*' .*? '*/' -> skip;
WS                : [ \t\n\r]+ -> skip;
		
		
		
		