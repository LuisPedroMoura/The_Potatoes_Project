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
program				: using globalStatement* EOF	
					;
	
using				: USING STRING EOL
					;	
					
globalStatement		: varDeclaration EOL							#globalStatement_Declaration 
					| assignment EOL								#globalStatement_Assignment
					| functionMain									#globalStatement_FunctionMain
					| functionID									#globalStatement_FunctionID
					;
					
// ----------------------------------------------
// Rules
		
statement			: varDeclaration EOL							#statement_Declaration
					| assignment EOL								#statement_Assignment
					| controlFlowStatement							#statement_Control_Flow_Statement
					| functionCall EOL								#statement_FunctionCall
					| inputOutput EOL								#statement_InputOutput
					| expression EOL								#statement_Expression
					;

assignment			: varDeclaration '=' expression					#assignment_Var_Declaration_Expression
					| var '=' expression							#assignment_Var_Expression
					;

// ----------------------------------------------
// Functions

functionMain		: FUN MAIN scope
					;
					
functionID			: FUN type ID '(' (type ID)? (',' type ID)* ')' scope
					;

functionReturn		: RETURN expression? ';'
					;
					
functionCall		: ID '(' expression? (',' (expression))* ')'
					;

// ----------------------------------------------
// Control Flow Statements

controlFlowStatement: condition
 					| forLoop
 					| whileLoop
 					;	

forLoop				: FOR '(' (assignment EOL)* expression EOL assignment ')' scope 
 					;
 			
whileLoop			: WHILE '(' expression ')' scope
					;
 			
condition			: ifCondition elseIfCondition* elseCondition?
					;

ifCondition			: IF '(' expression ')' scope
					;
					
elseIfCondition		: ELSE IF '(' expression ')' scope
					;
					
elseCondition		: ELSE scope
					;			

scope				: '{' statement* functionReturn? '}'
					;
					
// ----------------------------------------------
// Operations

expression			: '(' expression ')' 							#expression_Parenthesis
					| expression '[' expression ']'					#expression_LISTINDEX
					| expression 'isEmpty'							#expression_ISEMPTY
					| expression 'size'								#expression_SIZE
					| expression 'sort'								#expression_SORT
					| expression 'keys'								#expression_KEYS
					| expression 'values'							#expression_VALUES
					| cast expression								#expression_Cast
					| op=('-'|'!') expression						#expression_UnaryOperators
					| <assoc=right> expression '^' expression		#expression_Power
					| expression op=('*' | '/' | '%') expression	#expression_Mult_Div_Mod
					| expression  op=('+' | '-') expression			#expression_Add_Sub				
					| expression op=('<'|'<='|'>'|'>=') expression	#expression_RelationalQuantityOperators
					| expression 'instanceof' type					#expression_INSTANCEOF
					| expression op=('=='|'!=') expression			#expression_RelationalEquality
					| expression op=('&&'|'||') expression			#expression_logicalOperation
					| expression '->' expression					#expression_tuple
					| expression 'add' expression					#expression_ADD
					| expression 'rem' expression					#expression_REM
					| expression 'get' expression					#expression_GET
					| expression 'contains' expression				#expression_CONTAINS
					| expression 'containsKey' expression			#expression_CONTAINSKEY
					| expression 'containsValue' expression			#expression_CONTAINSVALUE
					| expression 'indexOf' expression				#expression_INDEXOF
					| var											#expression_Var
					| value											#expression_Value
					| functionCall									#expression_FunctionCall
					;
		
// ----------------------------------------------	
// Prints

inputOutput			: print
					| save
					| input
					;

print				: printType=(PRINT | PRINTLN)  '(' expression ')'
					;
					
save				: SAVE '(' expression ',' STRING (',' APPEND)? ')'
					;
					
input				: INPUT '(' STRING ')' 
					;
// ----------------------------------------------
// Variables

var					: ID
					;

varDeclaration		: type ID
					;			

type				: NUMBER_TYPE												# type_Number_Type
					| BOOLEAN_TYPE												# type_Boolean_Type
					| STRING_TYPE												# type_String_Type
					| VOID_TYPE													# type_Void_Type
					| LIST_TYPE	'[' block='?'? type ']'							# type_List_Type
					| DICT_TYPE	'[' block0='?'? type ',' block1='?'? type ']'	# type_Dict_Type
					| ID														# type_ID_Type
					;
	
value				: NUMBER			# value_Number
					| BOOLEAN			# value_Boolean
					| STRING			# value_String
					;
		
// ----------------------------------------------
// Casts

cast				: '(' id=(ID | NUMBER_TYPE) ')'
					;				

// -----------------------------------------------------------------------------
// Lexer

ADD : 'add';

USING				: 'using';

// Separator between instructions
EOL					: ';';

// Functions
MAIN				: 'main' ;
FUN					: 'fun';
RETURN				: 'return';

// Control Flow
IF					: 'if';
ELSE				: 'else';
FOR					: 'for';
WHILE				: 'while';

// Reserved Types
NUMBER_TYPE			: 'number';
BOOLEAN_TYPE		: 'boolean';
STRING_TYPE			: 'string';
VOID_TYPE			: 'void';
LIST_TYPE			: 'list';
DICT_TYPE			: 'dict';


// Boolean Values
BOOLEAN				: 'false' | 'true';

// Prints
PRINT				: 'print';
PRINTLN				: 'println';
INPUT				: 'input';
SAVE				: 'save';
APPEND				: 'append';

// Variables 
ID					: [a-z] [a-zA-Z0-9_]*;

// Type Agroupment
NUMBER				: '0'
					| [0-9] ('.'[0-9]+)?
					| [1-9][0-9]* ('.'[0-9]+)?
					;

STRING				: '"' (ESC | . )*? '"';
fragment ESC		: '//"' | '\\\\';

// Comments & White Space
LINE_COMMENT		: '//' .*? '\n' -> skip;
COMMENT				: '/*' .*? '*/' -> skip;
WS					: [ \t\n\r]+ -> skip;
		
		
		
		