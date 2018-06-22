parser grammar PotatoesParser;
options{
	tokenVocab = PotatoesLexer;
}

@header{
	package potatoesGrammar;
}


// MAIN RULES------------------------------------------------------------------
program				:	code+ EOF	
					;
		
code				: declaration EOL								#classContentDeclaration 
					| assignment EOL								#classContentAssignment
					| function										#classContentFunction
					;	
// CLASS-----------------------------------------------------------------------	
		
statement			: declaration EOL								#statementDeclaration
					| assignment EOL								#statementAssignment
					| controlFlowStatement							#statementControlFlowStatement
					| functionCall EOL								#statementFunctionCall
					// [IJ] - o return tem de estar dentro da statement
					//      - controlar posteriormente os erros
					| functionReturn								#statementFunctionReturn
					| print											#statementPrint
					;
					
declaration			: arrayDeclaration								#declaratioAarray
					| varDeclaration								#declarationVar
					;


assignment			: varDeclaration EQUAL cast? NOT? var			#assignmentVarDeclarationVar //boolean vars
					| varDeclaration EQUAL cast? value				#assignmentVarDeclarationValue
					| varDeclaration EQUAL comparison				#assignmentVarDeclarationComparison
					| varDeclaration EQUAL cast? operation			#assignmentVarDeclarationOperation
					| arrayDeclaration EQUAL valuesList				#assignmentArray
					| varDeclaration EQUAL functionCall				#assignmentVarDeclarationFunctionCall
					
					| var EQUAL cast? NOT? var						#assignmentVarVar //boolean vars
					| var EQUAL cast? value							#assignmentVarValue
					| var EQUAL comparison							#assignmentVarComparison
					| var EQUAL cast? operation						#assignmentVarOperation
					| var EQUAL valuesList							#assignmentVarValueList
					| var EQUAL functionCall						#assingmentVarFunctionCall
					
					| arrayAccess EQUAL cast? NOT? var				#assignmentVarVar //boolean vars
					| arrayAccess EQUAL cast? value					#assignmentVarValue
					| arrayAccess EQUAL comparison					#assignmentVarComparison
					| arrayAccess EQUAL cast? operation				#assignmentVarOperation
					| arrayAccess EQUAL valuesList					#assignmentVarValueList
					| arrayAccess EQUAL functionCall				#assingmentVarFunctionCall
					;

// CASTS-----------------------------------------------------------------------

cast				: '(' var ')'
					;

// FUNCTIONS-------------------------------------------------------------------
function			: 'fun' 'main' '{' statement* '}'
					| 'fun' ID '(' (type var (',' type var)* )* ')' ':' type '{' statement* '}'
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
 
forLoop				: FOR '(' assignment? EOL logicalOperation EOL operation ')'
					  '{' statement* '}' //[MJ] must have scopes for the sake of simplicity 
 					;
 			
whileLoop			: WHILE '(' logicalOperation ')' ('{' statement* '}' | EOL)
					;
 			
when				: WHEN '(' var ')' '{' whenCase* '}'
					;
 			
whenCase			: value '->' '{'? statement* '}'?
 					;
		
//[MJ] must have scopes for the sake of simplicity 
condition			: IF '(' logicalOperation ')' '{' statement* '}'
					  (ELSE IF '(' logicalOperation ')' '{' statement* '}')*
					  (ELSE '{' statement* '}')?
					;

// LOGICAL OPERATIONS----------------------------------------------------------
logicalOperation	: logicalOperand (logicalOperator logicalOperand)?
					;
					
logicalOperand 		: NOT? comparison
					| NOT? var	// boolean var
					| NOT? value // true or false
					;
					
logicalOperator		: AND
					| OR
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
operation			: '(' operation ')' 							#operationParenthesis
					| operation '^' NUMBER							#operationPower
					| operation op=('*' | '/') operation		 	#operationMult
					| '-' operation									
					| '+' operation
					| operation  op=('+' | '-') operation			#operationAddSub
					| operation '%' NUMBER 							#operationModulus
					| var											#operationExpr
					| functionCall									#operationFunctionCall
					| arrayAccess									#operationArrayAccess
					| arrayLength									#operationArrayLength
					| cast? NUMBER									#operationNumber
					;
			

// STRUCTURES------------------------------------------------------------------
arrayDeclaration	: arrayType var
					;
					
arrayType			: 'Array' '<' type '>'
					;					
					
arrayAccess			: ID '['  (var|NUMBER) ']'
					;
					
arrayLength			: 'length' '(' var ')' 
					;

// PRINTS----------------------------------------------------------------------

print				: ('print'|'println') '(' ((value | var) ('+' (value | var))* ) ')' EOL
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

type				: 'number'
					| 'boolean'
					| 'string'
					| 'void'
					| ID	// to declare personalized type variables
					| arrayType
					;
	
value				: NUMBER
					| BOOLEAN
					| STRING
					;
		
valuesList			: (value|var) (',' (value|var))*
					;
				

		
		
		
		