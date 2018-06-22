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
		
code				: declaration EOL	#classContentDeclaration 
					| assignment EOL	#classContentAssignment
					| function			#classContentFunction
					;	
// CLASS-----------------------------------------------------------------------	
		
statement			: declaration EOL			#statementDeclaration
					| assignment EOL			#statementAssignment
					| controlFlowStatement		#statementControlFlowStatement
					| functionCall EOL			#statementFunctionCall
					// [IJ] - o return tem de estar dentro da statement
					//      - controlar posteriormente os erros
					| functionReturn			#statementFunctionReturn
					| print						#statementPrint
					;
					
declaration			: arrayDeclaration			#declaratioAarray
					| varDeclaration			#declarationVar
					;


assignment			: varDeclaration EQUAL cast? NOT? var		#assignmentVarDeclarationVar //boolean vars
					| varDeclaration EQUAL cast? value			#assignmentVarDeclarationValue
					| varDeclaration EQUAL comparison			#assignmentVarDeclarationComparison
					| varDeclaration EQUAL cast? operation		#assignmentVarDeclarationOperation
					| arrayDeclaration EQUAL valuesList		#assignmentArray
					| varDeclaration EQUAL functionCall		#assignmentVarDeclarationFunctionCall
					
					| var EQUAL cast? NOT? var					#assignmentVarVar //boolean vars
					| var EQUAL cast? value					#assignmentVarValue
					| var EQUAL comparison						#assignmentVarComparison
					| var EQUAL cast? operation				#assignmentVarOperation
					| var EQUAL valuesList						#assignmentVarValueList
					| var EQUAL functionCall					#assingmentVarFunctionCall
					
					| arrayAccess EQUAL cast? NOT? var			#assignmentVarVar //boolean vars
					| arrayAccess EQUAL cast? value			#assignmentVarValue
					| arrayAccess EQUAL comparison				#assignmentVarComparison
					| arrayAccess EQUAL cast? operation		#assignmentVarOperation
					| arrayAccess EQUAL valuesList				#assignmentVarValueList
					| arrayAccess EQUAL functionCall			#assingmentVarFunctionCall
					;

// CASTS-----------------------------------------------------------------------

cast				:	PARENTHESIS_BEGIN var PARENTHESIS_END
					;

// FUNCTIONS-------------------------------------------------------------------
function			: FUN 'main' SCOPE_BEGIN statement* SCOPE_END
					| FUN ID PARENTHESIS_BEGIN (type var (COMMA type var)* )*
					  PARENTHESIS_END ':' type 
					  SCOPE_BEGIN statement* SCOPE_END
					;

functionReturn		: RETURN (var|value|operation) EOL
					;
					
functionCall		: ID PARENTHESIS_BEGIN ((var|value|operation|arrayAccess) 
					  (COMMA (var|value|operation|arrayAccess))* )* PARENTHESIS_END
					;

// CONTROL FLOW STATMENTS------------------------------------------------------
controlFlowStatement: condition
 					| forLoop
 					| whileLoop
 					| when
 					;	
 
forLoop				: FOR PARENTHESIS_BEGIN
					  assignment? EOL logicalOperation EOL operation PARENTHESIS_END
					  SCOPE_BEGIN statement* SCOPE_END //[MJ] must have scopes for the sake of simplicity 
 					;
 			
whileLoop			: WHILE PARENTHESIS_BEGIN logicalOperation PARENTHESIS_END
					  (SCOPE_BEGIN statement* SCOPE_END | EOL)
					;
 			
when				: WHEN PARENTHESIS_BEGIN var PARENTHESIS_END
					  SCOPE_BEGIN whenCase* SCOPE_END
					;
 			
whenCase			: value '->' SCOPE_BEGIN? statement* SCOPE_END?
 					;
		
//[MJ] must have scopes for the sake of simplicity 
condition			: IF PARENTHESIS_BEGIN logicalOperation PARENTHESIS_END
					  SCOPE_BEGIN statement* SCOPE_END
					  (ELSE IF PARENTHESIS_BEGIN logicalOperation PARENTHESIS_END
					  SCOPE_BEGIN statement* SCOPE_END)*
					  (ELSE SCOPE_BEGIN statement* SCOPE_END)?
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

operation			: PARENTHESIS_BEGIN operation PARENTHESIS_END 	#operationParenthesis
					| operation POWER NUMBER						#operationPower
					| operation op=(MULTIPLY | DIVIDE) operation 	#operationMult
					| SUBTRACT operation 
					| ADD operation
					| operation  op=(ADD | SUBTRACT) operation		#operationAddSub
					| operation MODULUS NUMBER 						#operationModulus
					| var											#operationExpr
					| functionCall									#operationFunctionCall
					| arrayAccess									#operationArrayAccess
					| arrayLength									#operationArrayLength
					| cast? NUMBER									#operationNumber
					;
			

// STRUCTURES------------------------------------------------------------------
arrayDeclaration	: arrayType var
					;
					
arrayType			:  ARRAY DIAMOND_BEGIN type DIAMOND_END
					;					
					
arrayAccess			: ID '['  (var|NUMBER) ']'
					;
					
arrayLength			: LENGTH PARENTHESIS_BEGIN var PARENTHESIS_END 
					;

// PRINTS----------------------------------------------------------------------

print				: ('print'|'println') PARENTHESIS_BEGIN ((value | var) (ADD (value | var))* ) 
					  PARENTHESIS_END EOL
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
	
value				: NUMBER
					| BOOLEAN
					| STRING
					;
		
valuesList			: (value|var) (COMMA (value|var))*
					;
				

		
		
		
		