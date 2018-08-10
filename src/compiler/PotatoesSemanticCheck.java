/***************************************************************************************
*	Title: PotatoesProject - PotatoesSemanticCheck Class Source GlobalStatement
*	GlobalStatement version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Acknowledgments for version 1.0: Maria Joao Lavoura
*	(https://github.com/mariajoaolavoura), for the help in brainstorming the concepts
*	needed to create the first working version of this Class.
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import potatoesGrammar.grammar.PotatoesBaseVisitor;
import potatoesGrammar.grammar.PotatoesFunctionNames;
import potatoesGrammar.grammar.PotatoesParser.*;
import potatoesGrammar.utils.DictTuple;
import potatoesGrammar.utils.DictVar;
import potatoesGrammar.utils.ListVar;
import potatoesGrammar.utils.Variable;
import typesGrammar.grammar.TypesFileInfo;
import typesGrammar.utils.Type;
import utils.errorHandling.ErrorHandling;

/**
 * 
 * <b>PotatoesSemanticCheck</b><p>
 * This class performs a semantic analysis for a Parse Tree generated from a Potatoes Source File<p>
 * 
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class PotatoesSemanticCheck extends PotatoesBaseVisitor<Boolean>  {

	// Static Constant (Debug Only)
	private static final boolean debug = false;

	// --------------------------------------------------------------------------
	// Static Fields
	private static String TypesFilePath;
	
	private	static TypesFileInfo					typesFileInfo;	// initialized in visitUsing();
	private	static List<String>						reservedWords;	// initialized in visitUsing();
	private static Map<String, Type> 				typesTable;		// initialized in visitUsing();
	private static PotatoesFunctionNames			functions;		// initialized in CTOR;
	private static Map<String, Function_IDContext>	functionNames;	// initialized in CTOR;
	private static Map<String, List<String>>		functionArgs;	// initialized in CTOR;

	protected static ParseTreeProperty<Object> 		mapCtxObj		= new ParseTreeProperty<>();
	protected static List<HashMap<String, Object>>	symbolTable 	= new ArrayList<>();
	
	protected static boolean visitedMain = false;
	protected static Object currentReturn = null;
	
 	public PotatoesSemanticCheck(String PotatoesFilePath){
		functions = new PotatoesFunctionNames(PotatoesFilePath);
		functionNames = functions.getFunctions();
		functionArgs = functions.getFunctionsArgs();
		symbolTable.add(new HashMap<String, Object>());
		if (debug) ErrorHandling.printInfo("The PotatoesFilePath is: " + PotatoesFilePath);
	}
	
	// --------------------------------------------------------------------------
	// Getters
	public static ParseTreeProperty<Object> getMapCtxObj(){
		return mapCtxObj;
	}

	public static TypesFileInfo getTypesFileInfo() {
		return typesFileInfo;
	}

	// --------------------------------------------------------------------------
	// Main Rules 
	@Override 
	public Boolean visitProgram(ProgramContext ctx) {
		Boolean valid = visit(ctx.using());
		List<GlobalStatementContext> globalStatementsInstructions = ctx.globalStatement();

		// Visit all globalStatement rules
		for (GlobalStatementContext c : globalStatementsInstructions) {
			Boolean res = visit(c);
			valid = valid && res;
		}
		return valid;
	}

	@Override 
	public Boolean visitUsing(UsingContext ctx) {
		// Get information from the types file
		TypesFilePath = getStringText(ctx.STRING().getText());
		if (debug) ErrorHandling.printInfo(ctx, TypesFilePath);
		typesFileInfo = new TypesFileInfo(TypesFilePath);
		reservedWords = typesFileInfo.getReservedWords();
		typesTable = typesFileInfo.getTypesTable();

		// Debug
		if (debug) {
			ErrorHandling.printInfo(ctx, "Types File path is: " + TypesFilePath);
			ErrorHandling.printInfo(ctx, typesFileInfo.toString());
		}

		return true;
	}

	@Override
	public Boolean visitGlobalStatement_Declaration(GlobalStatement_DeclarationContext ctx) {
		return visit(ctx.varDeclaration());
	}

	@Override 
	public Boolean visitGlobalStatement_Assignment(GlobalStatement_AssignmentContext ctx) {
		Boolean result =  visit(ctx.assignment());
		if(debug) {ErrorHandling.printInfo("Visited " + ctx.assignment().getText() + " : " + result);}
		return result;
	}

	@Override
	public Boolean visitGlobalStatement_Function(GlobalStatement_FunctionContext ctx) {
		return visitChildren(ctx);
	}

	// --------------------------------------------------------------------------
	// Statements 

	@Override 
	public Boolean visitStatement_Declaration(Statement_DeclarationContext ctx) {
		return visit(ctx.varDeclaration());
	}

	@Override 
	public Boolean visitStatement_Assignment(Statement_AssignmentContext ctx) {
		boolean valid =  visit(ctx.assignment());
		if(debug) {ErrorHandling.printInfo(ctx, "Visited " + ctx.assignment().getText() + " : " + valid);}
		return valid;
	}

	@Override 
	public Boolean visitStatement_Control_Flow_Statement(Statement_Control_Flow_StatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override 
	public Boolean visitStatement_FunctionCall(Statement_FunctionCallContext ctx) {
		return visit(ctx.functionCall());
	}

	@Override 
	public Boolean visitStatement_Function_Return(Statement_Function_ReturnContext ctx) {
		return visitChildren(ctx);
	}

	@Override 
	public Boolean visitStatement_Print(Statement_PrintContext ctx) {
		return visit(ctx.print());
	}

	// --------------------------------------------------------------------------
	// Assignments
	
	@Override
	public Boolean visitAssignment_Var_Declaration_Expression(Assignment_Var_Declaration_ExpressionContext ctx) {
		if (!visit(ctx.varDeclaration()) || !visit(ctx.expression())) {
			return false;
		};

		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		String varName = ctx.varDeclaration().ID().getText();
		Object obj = mapCtxObj.get(ctx.expression());

		// verify that variable to be created has valid name
		if(!isValidNewVariableName(varName, ctx)) return false;

		if (debug) {
			ErrorHandling.printInfo(ctx, "[ASSIGN_VARDEC_EXPR] Visited visitAssignment_Var_Declaration_Expression");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + varName + " with type " + typeName);
		}

		// assign Variable to string is not possible
		if(typeName.equals("string")) {
			if (obj instanceof String) {
				updateSymbolTable(ctx.varDeclaration().ID().getText(), "str");
				mapCtxObj.put(ctx, "str");
				return true;
			}
			ErrorHandling.printError(ctx, "expression result is not compatible with string Type");
			return false;
		}

		// assign Variable to boolean is not possible
		if (typeName.equals("boolean")) {
			if (obj instanceof Boolean) {
				updateSymbolTable(ctx.varDeclaration().ID().getText(), true);
				mapCtxObj.put(ctx, true);
				return true;
			}
			ErrorHandling.printError(ctx, "expression result is not compatible with boolean Type");
			return false;
		}

		// assign Variable to Variable
		Variable temp = (Variable) mapCtxObj.get(ctx.expression());
		Variable a = new Variable(temp); // deep copy

		if (debug) {
			ErrorHandling.printInfo(ctx, "--- Variable to assign is " + a);
			ErrorHandling.printInfo(ctx, "type to assign to is: " + typesTable.get(typeName));
		}

		if (a.convertTypeTo(typesTable.get(typeName))) {
			updateSymbolTable(ctx.varDeclaration().ID().getText(), a);
			mapCtxObj.put(ctx, a);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned Variable var=" + a.getType().getTypeName() + ", " +
					"val=" + a.getValue() + " to " + ctx.varDeclaration().ID().getText());}
			return true;
		}

		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with \"" + a.getType().getTypeName() + "\"!");
		return false;
	}

	@Override
	public Boolean visitAssignment_Var_Expression(Assignment_Var_ExpressionContext ctx) {
		if (!visit(ctx.var()) || !visit(ctx.expression())) {
			return false;
		};
		
		Object varObj = mapCtxObj.get(ctx.var());
		Object exprResObj = mapCtxObj.get(ctx.expression());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_OP] Visited visitAssignment_Var_Declaration_Expression");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + ctx.var().ID().getText() + " with type " + checkSymbolTable().get(ctx.var().ID().getText()));
		}

		if(varObj instanceof String) {
			if (exprResObj instanceof String) {
				updateSymbolTable(ctx.var().ID().getText(), "str");
				mapCtxObj.put(ctx, "str");
				return true;
			}
			ErrorHandling.printError(ctx, "expression result is not compatible with string Type");
			return false;
		}

		if(varObj instanceof Boolean) {
			if (exprResObj instanceof Boolean) {
				updateSymbolTable(ctx.var().ID().getText(), true);
				mapCtxObj.put(ctx, true);
				return true;
			}
			ErrorHandling.printError(ctx, "expression result is not compatible with boolean Type");
			return false;
		}

		Variable aux = (Variable) varObj;
		Variable var = new Variable(aux);
		String typeName = var.getType().getTypeName();

		aux = (Variable) exprResObj;
		Variable a = new Variable(aux);

		if (debug) {ErrorHandling.printInfo(ctx, "--- Variable to assign is " + a);}

		// If type of variable a can be converted to the destination type (ie are compatible)
		if (a.convertTypeTo(typesTable.get(typeName))) {
			updateSymbolTable(ctx.var().ID().getText(), a);
			mapCtxObj.put(ctx, a);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned Variable var=" + a.getType().getTypeName() + ", " +
					"val=" + a.getValue() + " to " + ctx.var().ID().getText());}
			return true;
		}

		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with \"" + a.getType().getTypeName() + "\"");
		return false;
	}
	
	// --------------------------------------------------------------------------
	// Functions
	
	@Override
	public Boolean visitFunction_Main(Function_MainContext ctx) {
		if (visitedMain == true) {
			ErrorHandling.printError(ctx, "Only one main function is allowed");
			return false;
		}
		
		visitedMain = true;
		
		openFunctionScope();
		visit(ctx.scope());
		
		return true;
	}

	@Override
	public Boolean visitFunction_ID(Function_IDContext ctx) {
		boolean valid = true;
		for (TypeContext type : ctx.type()) {
			valid = valid && visit(type);
		}
		if (!valid) {
			return false;
		}
		
		// get args list from function call
		@SuppressWarnings("unchecked")
		List<Object> args = (List<Object>) mapCtxObj.get(ctx.getParent());
		
		// open new scope
		openFunctionScope();
		
		// store new variables with function call value and function signature name
		for (int i = 0; i < args.size(); i++) {
			updateSymbolTable((String) mapCtxObj.get(ctx.type(i)), args.get(i));
		}
		
		return valid && visit(ctx.scope());
	}

	@Override
	public Boolean visitFunctionReturn(FunctionReturnContext ctx) {
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Object obj = mapCtxObj.get(ctx.expression());
		
		if ((currentReturn instanceof String && obj instanceof String) || (currentReturn instanceof Boolean && obj instanceof Boolean)) {
			mapCtxObj.put(ctx, mapCtxObj.get(ctx.expression()));
			return true;
		}
		
		if (currentReturn instanceof Variable && obj instanceof Variable) {
			Variable a = (Variable) obj;
			Variable b = (Variable) currentReturn;
			if(a.typeIsCompatible(b)) {
				mapCtxObj.put(ctx, mapCtxObj.get(ctx.expression()));
				return true;
			}
		}
		
		ErrorHandling.printError(ctx, "return is not compatible with function signature");
		return false;
	}
	
	@Override
	public Boolean visitFunctionCall(FunctionCallContext ctx) {
		Boolean valid = true;
		for (ExpressionContext expr : ctx.expression()) {
			valid = valid && visit(expr);
		}
		if(!valid) {
			return false;
		}
		
		// get function context to be visited and args needed from list of functions	
		Function_IDContext functionToVisit = functionNames.get(ctx.ID().getText());
		List<String> argsToUse	= functionArgs.get(ctx.ID().getText());
		
		// get list of arguments given in function call
		List<Object> functionCallArgs = new ArrayList<>();
		for (ExpressionContext expr : ctx.expression()) {
			visit(expr);
			functionCallArgs.add(mapCtxObj.get(expr));
		}
				
		// if number of arguments do not match -> error
		if(argsToUse.size() != functionCallArgs.size()) {
			ErrorHandling.printError(ctx, "NUmber of arguments in function call do not match required arguments");
			return false;
		}
		
		// verify that all arguments types match function arguments
		for (int i = 0; i < argsToUse.size(); i++) {
			if (argsToUse.get(i).equals("string") && functionCallArgs.get(i) instanceof String) {
				continue;
			}
			else if (argsToUse.get(i).equals("boolean") && functionCallArgs.get(i) instanceof Boolean) {
				continue;
			}
			else if (argsToUse.get(i).equals("string") || functionCallArgs.get(i) instanceof String) {
				ErrorHandling.printError(ctx, "function call arguments are no compatible with function signature");
				return false;
			}
			else if (argsToUse.get(i).equals("boolean") || functionCallArgs.get(i) instanceof Boolean) {
				ErrorHandling.printError(ctx, "function call arguments are no compatible with function signature");
				return false;
			}
			else {
				Variable arg = (Variable) functionCallArgs.get(i);
				String argTypeName = arg.getType().getTypeName();
				if (argsToUse.get(i).equals(argTypeName)) {
					continue;
				}
				ErrorHandling.printError(ctx, "function call arguments are no compatible with function signature");
				return false;
			}
		}
		
		mapCtxObj.put(ctx, functionCallArgs);
		visit(functionToVisit);
		return true;
	}


	// --------------------------------------------------------------------------
	// Control Flow Statements

	@Override 
	public Boolean visitControlFlowStatement(ControlFlowStatementContext ctx) {
		extendScope();
		return visitChildren(ctx);
	}

	@Override 
	public Boolean visitForLoop(ForLoopContext ctx) {
		Boolean valid = true;
		Boolean res   = true;

		// visit all assignments
		for (AssignmentContext a : ctx.assignment()) {		
			res = visit(a);
			valid = valid && res;
		}

		res = visit(ctx.expression());
		valid = valid && res;

		// visit scope
		valid = valid && visit(ctx.scope());

		return valid;
	}

	@Override 
	public Boolean visitWhileLoop(WhileLoopContext ctx) {
		Boolean valid = true;
		Boolean res = visit(ctx.expression());
		valid = valid && res;

		// visit all scope
		valid = valid && visit(ctx.scope());

		return valid;
	}

	@Override
	public Boolean visitCondition(ConditionContext ctx) {
		return visitChildren(ctx);
	}
	
	@Override 
	public Boolean visitIfCondition(IfConditionContext ctx) {
		Boolean valid = true;
		Boolean res = visit(ctx.expression());
		valid = valid && res;

		// visit all scope
		valid = valid && visit(ctx.scope());
		
		return valid;
	}

	@Override 
	public Boolean visitElseIfCondition(ElseIfConditionContext ctx) {
		Boolean valid = true;
		Boolean res = visit(ctx.expression());
		valid = valid && res;

		// visit all scope
		valid = valid && visit(ctx.scope());
		
		return valid;
	}

	@Override 
	public Boolean visitElseCondition(ElseConditionContext ctx) {
		Boolean valid = true;

		// visit all scope
		valid = valid && visit(ctx.scope());
				
		return valid;
	}

	@Override
	public Boolean visitScope(ScopeContext ctx) {
		Boolean valid = true;
		List<StatementContext> statements = ctx.statement();

		// Visit all statement rules
		for (StatementContext stat : statements) {
			Boolean res = visit(stat);
			valid = valid && res;
		}
		
		closeScope();
		
		return valid;
	}

	// --------------------------------------------------------------------------
	// Expressions
	
	@Override 
	public Boolean visitExpression_Parenthesis(Expression_ParenthesisContext ctx) {
		if(!visit(ctx.expression())) {
			return false;
		}
		mapCtxObj.put(ctx, mapCtxObj.get(ctx.expression()));
		return true;
	}
	
	@Override
	public Boolean visitExpression_LISTINDEX(Expression_LISTINDEXContext ctx) {
		// TODO Auto-generated method stub
		return super.visitExpression_LISTINDEX(ctx);
	}
	
	@Override
	public Boolean visitExpression_ISEMPTY(Expression_ISEMPTYContext ctx) {
		// TODO Auto-generated method stub
		return super.visitExpression_ISEMPTY(ctx);
	}
	
	@Override
	public Boolean visitExpression_SIZE(Expression_SIZEContext ctx) {
		// TODO Auto-generated method stub
		return super.visitExpression_SIZE(ctx);
	}
	
	@Override
	public Boolean visitExpression_SORT(Expression_SORTContext ctx) {
		// TODO Auto-generated method stub
		return super.visitExpression_SORT(ctx);
	}
	
	@Override
	public Boolean visitExpression_KEYS(Expression_KEYSContext ctx) {
		// TODO Auto-generated method stub
		return super.visitExpression_KEYS(ctx);
	}
	
	@Override
	public Boolean visitExpression_VALUES(Expression_VALUESContext ctx) {
		// TODO Auto-generated method stub
		return super.visitExpression_VALUES(ctx);
	}
	
	@Override
	public Boolean visitExpression_Cast(Expression_CastContext ctx) {
		if(!visitChildren(ctx)) {
			return false;
		}
		
		String castName = ctx.cast().ID().getText();
		Object obj = mapCtxObj.get(ctx.expression());
		
		// erify if cast is valid ID
		if (!typesTable.keySet().contains(castName)) {
			ErrorHandling.printError(ctx, "'" + castName + "' is not a valid Type");
			return false;
		}
		
		// booleans cannot be casted
		if(obj instanceof Boolean) {
			ErrorHandling.printError(ctx, "boolean Type cannot be casted");
			return false;
		}
		
		if (obj instanceof String) {
			try {
				String str = (String) obj;
				Double value = Double.parseDouble(str);
				Type newType = typesTable.get(castName);
				mapCtxObj.put(ctx, new Variable(newType, value));
				return true;
			}
			catch (NumberFormatException e) {
				ErrorHandling.printError("String does not contain a parsable value");
				return false;
			}
			catch (NullPointerException e) {
				ErrorHandling.printError("String is null");
				return false;
			}
		}
		
		if (obj instanceof Variable) {
			Variable temp = (Variable) obj;
			Variable opRes = new Variable(temp); // deep copy
			Type newType = typesTable.get(ctx.cast().ID().getText());

			boolean wasConverted = opRes.convertTypeTo(newType);
			//Types are not compatible
			if (!wasConverted) {
				if (castName.equals("number")){
					opRes = new Variable(newType, opRes.getValue());
				}
				else {
					ErrorHandling.printError(ctx,"cast and expression Types are not compatible");
					return false;
				}	
			}

			mapCtxObj.put(ctx, opRes);
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean visitExpression_UnaryOperators(Expression_UnaryOperatorsContext ctx) {
		if(!visit(ctx.expression())) {
			return false;
		}
		
		String op = ctx.op.getText();
		Object obj = mapCtxObj.get(ctx.expression());
		
		if (op.equals("-")) {
			
			if (obj instanceof String) {
				ErrorHandling.printError(ctx, "Unary Simetric operator cannot be applied to string Type");
				return false;
			}
			if(obj instanceof Boolean) {
				ErrorHandling.printError(ctx, "Unary Simetric operator cannot be applied to boolean Type");
				return false;
			}
			
			mapCtxObj.put(ctx, obj); // don't need to calculate symmetric value to guarantee semantic correctness in future calculations

			if(debug) ErrorHandling.printInfo(ctx, "[OP_OP_SIMETRIC]");
		}
		
		if (op.equals("!")) {
			
			if (obj instanceof String) {
				ErrorHandling.printError(ctx, "Unary Not operator cannot be applied to string Type");
				return false;
			}
			if(obj instanceof Variable) {
				ErrorHandling.printError(ctx, "Unary Not operator cannot be applied to numeric Types");
				return false;
			}
			
			mapCtxObj.put(ctx, true);

			if (debug) ErrorHandling.printInfo(ctx, "[OP_LOGIC_OPERAND_NOT_VAR]");
		}
		
		return true;
	}
	
	@Override 
	public Boolean visitExpression_Power(Expression_PowerContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Object base = mapCtxObj.get(ctx.expression(0));
		Object pow = mapCtxObj.get(ctx.expression(1));
		
		// pow has to have type number
		if (pow instanceof String || pow instanceof Boolean) {
			ErrorHandling.printError(ctx, "exponent has invalid Type for power operation");
			return false;
		}
		if (pow instanceof Variable) {
			Variable var = (Variable) pow;
			if (!var.getType().equals(typesTable.get("number"))) {
				
			}
		}
		
		if (base instanceof Boolean) {
			ErrorHandling.printError(ctx, "boolean Type cannot be powered");
			return false;
		}
		if (base instanceof String) { 
			mapCtxObj.put(ctx, "str"); // strings can be powered, "str"^3 == "strstrstr"
			return true;
		}
		if (base instanceof Variable) {
			Variable aux = (Variable) pow;
			Variable powVar = new Variable(aux);
			aux = (Variable) base;
			Variable baseVar = new Variable(aux);
			
			Variable res = Variable.power(baseVar, powVar);
			mapCtxObj.put(ctx, res);
			
			if (debug) {
				ErrorHandling.printInfo(ctx, "[OP_POWER] Visited Expression Power");
				ErrorHandling.printInfo(ctx, "--- Powering Variable " + base + "with power " + pow + "and result is " + res);
			}
			return true;
		}

		
		return false;
	}
	
	@Override 
	public Boolean visitExpression_Mult_Div_Mod(Expression_Mult_Div_ModContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		String op = ctx.op.getText();
		Object obj0 = mapCtxObj.get(ctx.expression(0));
		Object obj1 = mapCtxObj.get(ctx.expression(1));
		
		if(obj0 instanceof Boolean || obj1 instanceof Boolean) {
			ErrorHandling.printError(ctx, "bad operand Types for operator '" + op + "'");
			return false;
		}

		if(obj0 instanceof String || obj1 instanceof String) {
			ErrorHandling.printError(ctx, "bad operand Types for operator '" + op + "'");
			return false;
		}
		
		// string multiplication (expanded concatenation)
		if(obj0 instanceof String && obj1 instanceof Variable) {
			Variable var = (Variable) obj1;
			if (var.getType().equals(typesTable.get("number")) && op.equals("*")) {
				mapCtxObj.put(ctx, "str");
				return true;
			}
		}
		if(obj0 instanceof Variable && obj1 instanceof String) {
			Variable var = (Variable) obj0;
			if (var.getType().equals(typesTable.get("number")) && op.equals("*")) {
				mapCtxObj.put(ctx, "str");
				return true;
			}
		}
		
		if (obj0 instanceof Variable && obj1 instanceof Variable) {
			Variable aux = (Variable) obj0;
			Variable a = new Variable(aux); // deep copy
			aux = (Variable) obj1;
			Variable b = new Variable(aux); // deep copy
			
			// Modulus
			if (op.equals("%")) {
				try {
					Variable res = Variable.mod(a, b);
					mapCtxObj.put(ctx, res);
					return true;
				}
				catch (IllegalArgumentException e) {
					ErrorHandling.printError(ctx, "Right side of mod expression has to be of Type Number!");
					return false;
				}
			}
			
			// Multiplication
			if (op.equals("*")) {
				Variable res = Variable.multiply(a, b); 
				mapCtxObj.put(ctx, res); 
				if (debug) { ErrorHandling.printInfo(ctx, "result of multiplication is Variable " + res);}
				return true;
			}
			
			// Division expression
			if (op.equals("/")) {
				try {
					Variable res = Variable.divide(a, b); 
					mapCtxObj.put(ctx, res);
					if (debug) { ErrorHandling.printInfo(ctx, "result of division is Variable " + res);}
					return true;
				}
				catch (ArithmeticException e) {
					ErrorHandling.printError(ctx, "Cannot divide by zero");
				}
			}
			
			if (debug) {
				ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] Visiting Expression Mult_Div_Mod");
				ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] op0: " + ctx.expression(0).getText());
				ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] op1: " + ctx.expression(1).getText());
				ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] variable a " + a);
				ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] variable b " + b);
				ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] op		 " + op + "\n");
				ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] temp: " + aux);
			}
		}
		return false;
	}
	
	@Override 
	public Boolean visitExpression_Add_Sub(Expression_Add_SubContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Object obj0 = mapCtxObj.get(ctx.expression(0));
		Object obj1 = mapCtxObj.get(ctx.expression(1));
		String op = ctx.op.getText();
		
		// one of the elements in expression is boolean
		if(obj0 instanceof Boolean || obj1 instanceof Boolean) {
			ErrorHandling.printError(ctx, "bad operand Types for operator '" + op + "'");
			return false;
		}
		
		// both elements are string (concatenation or removeAll)
		if(obj0 instanceof String && obj1 instanceof String) {
			mapCtxObj.put(ctx, "str");
			return true;
		}
		
		// only one element is string (not possible)
		if (obj0 instanceof String || obj1 instanceof String) {
			ErrorHandling.printError(ctx, "bad operand Types for operator '" + op + "'");
			return false;
		}
		
		// both elements are Variables
		if (obj0 instanceof Variable && obj1 instanceof Variable) {
			Variable aux = (Variable) obj0;
			Variable a = new Variable(aux); // deep copy
			aux = (Variable) obj1;
			Variable b = new Variable(aux); // deep copy
	
			if (debug) {
				ErrorHandling.printInfo(ctx, "[OP_ADDSUB] Visiting Expression Add_Sub");
				ErrorHandling.printInfo(ctx, "[OP_ADDSUB] variable a " + a);
				ErrorHandling.printInfo(ctx, "[OP_ADDSUB] variable b " + b + "\n");
			}
	
			// Addition or Subtraction (if one is compatible the other is also compatible
			try {
				Variable res = Variable.add(a, b);
				if (debug) { ErrorHandling.printInfo(ctx, "result of sum is Variable " + res);}
				mapCtxObj.put(ctx, res);
				return true;
			}
			catch (IllegalArgumentException e) {
				ErrorHandling.printError(ctx, "Incompatible types in expression");
				return false;
			}
		}
		
		return false;
	}
	
	@Override
	public Boolean visitExpression_RelationalQuantityOperators(Expression_RelationalQuantityOperatorsContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_COMPARISON]");
		}
		
		Object obj0 = mapCtxObj.get(ctx.expression(0));
		Object obj1 = mapCtxObj.get(ctx.expression(1));
		String op = ctx.op.getText();
		
		if(obj0 instanceof Boolean || obj1 instanceof Boolean) {
			ErrorHandling.printError(ctx, "bad operand types for relational operator '" + op + "'");
			return false;
		}
		
		if(obj0 instanceof String && obj1 instanceof String) {
			mapCtxObj.put(ctx, true);
			return true;
		}
		
		if(obj0 instanceof String || obj1 instanceof String) {
			ErrorHandling.printError(ctx, "bad operand types for relational operator '" + op + "'");
			return false;
		}
		
		if(obj0 instanceof Variable && obj1 instanceof Variable) {
			Variable a = (Variable) obj0;
			Variable b = (Variable) obj1;
			
			if (debug) {
				ErrorHandling.printInfo(ctx, "THIS IS A : " + a);
				ErrorHandling.printInfo(ctx, "THIS IS B : " + b);
			}
			
			boolean comparisonIsPossible = a.typeIsCompatible(b);
			
			if(!comparisonIsPossible) {
				ErrorHandling.printError(ctx, "Types are not compatible");
				return false;
			}
			
			mapCtxObj.put(ctx, comparisonIsPossible);
			return true;
		}
		return false;
	}

	@Override
	public Boolean visitExpression_RelationalEquality(Expression_RelationalEqualityContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_COMPARISON]");
		}
		
		Object obj0 = mapCtxObj.get(ctx.expression(0));
		Object obj1 = mapCtxObj.get(ctx.expression(1));
		
		if (obj0 instanceof Boolean && obj1 instanceof Boolean) {
			mapCtxObj.put(ctx, true);
			return true;
		}
		
		if (obj0 instanceof String && obj1 instanceof String) {
			mapCtxObj.put(ctx, "str");
			return true;
		}
		
		if (obj0 instanceof Variable && obj1 instanceof Variable) {
			Variable a = (Variable) obj0;
			Variable b = (Variable) obj1;
			
			if (debug) {
				ErrorHandling.printInfo(ctx, "THIS IS A : " + a);
				ErrorHandling.printInfo(ctx, "THIS IS B : " + b);
			}
			
			boolean comparisonIsPossible = a.typeIsCompatible(b);
			
			if(!comparisonIsPossible) {
				ErrorHandling.printError(ctx, "Types to be compared are not compatible");
				return false;
			}
			
			mapCtxObj.put(ctx, comparisonIsPossible);
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean visitExpression_logicalOperation(Expression_logicalOperationContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_COMPARISON]");
		}
		
		Object obj0 = mapCtxObj.get(ctx.expression(0));
		Object obj1 = mapCtxObj.get(ctx.expression(1));
		String op = ctx.op.getText();
		
		if(obj0 instanceof Variable || obj1 instanceof Variable || obj0 instanceof String || obj1 instanceof String) {
			ErrorHandling.printError(ctx, "bad operand types for logical operator '" + op + "'");
			return false;
		}
		
		// both obj have Type boolean
		mapCtxObj.put(ctx, true);
		return true;
	}
	
	@Override
	public Boolean visitExpression_tuple(Expression_tupleContext ctx) {
		// TODO Auto-generated method stub
		return super.visitExpression_tuple(ctx);
	}
	
	@Override
	public Boolean visitExpression_ADD(Expression_ADDContext ctx) {
		// TODO Auto-generated method stub
		return super.visitExpression_ADD(ctx);
	}
	
	@Override
	public Boolean visitExpression_REM(Expression_REMContext ctx) {
		// TODO Auto-generated method stub
		return super.visitExpression_REM(ctx);
	}
	
	@Override
	public Boolean visitExpression_GET(Expression_GETContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Object obj0 = mapCtxObj.get(ctx.expression(0));
		Object obj1 = mapCtxObj.get(ctx.expression(1));
		
		// expression to search on is not type list | dict -> error
		if (!(obj0 instanceof ListVar) || !(obj0 instanceof DictVar)) {
			ErrorHandling.printError(ctx, "Bad operand types for operator 'get'");
			return false;
		}
		
		// expression to search index on is list -> verify
		if(obj0 instanceof ListVar) {
			ListVar list = (ListVar) obj0;
			
			list.getList().remo
			
			// expression for index search is 'number' -> ok
			if (obj1 instanceof Variable) {
				Variable aux = (Variable) obj1;
				Variable a = new Variable(aux);
				
				if (a.getType().equals(typesTable.get("number"))) {
					try {
						int index = (int) a.getValue();
						mapCtxObj.put(ctx, list.getList().get(index));
						return true;
					}
					catch (IndexOutOfBoundsException e) {
						ErrorHandling.printError(ctx, "Index out of bounds");
						return false;
					}
				}
			}
			
			// expression for index search is not 'number -> error
			ErrorHandling.printError(ctx, "Not a valid index");
			return false;
		}
		
		// expression to search key on is dict -> verify
		if (obj0 instanceof DictVar) {
			DictVar dict = (DictVar) obj1;
			String keyType = dict.getKeyType();
			
			// key to be searched is NOT type boolean || string || numeric -> error
			if (!(obj1 instanceof Boolean) && !(obj1 instanceof String) && !(obj1 instanceof Variable)) {
				ErrorHandling.printError(ctx, "Bad operand types for operator 'get'");
				return false;
			}
			
			// key to be searched is of type boolean -> verify
			if (obj1 instanceof Boolean) {
				
				// dict as key type boolean -> ok
				if (keyType.equals("boolean")) {
					try {
						
						mapCtxObj.put(ctx, dict.getDict().get(obj1));
						return true;
					}
					catch (NullPointerException e) {
						ErrorHandling.printError(ctx, "Bad operand. Operand is null");
						return false;
					}
				}
				
				// dict has different key type
				ErrorHandling.printError(ctx, "Operands types are not compatible");
				return false;
			}
			
			// key to be searched is of type string -> verify
			else if (obj1 instanceof String) {
				
				// dict as key type string -> ok
				if (keyType.equals("string")) {
					try {
						mapCtxObj.put(ctx, dict.getDict().get(obj1));
						return true;
					}
					catch (NullPointerException e) {
						ErrorHandling.printError(ctx, "Bad operand. Operand is null");
						return false;
					}
				}
				
				// dict has different key type
				ErrorHandling.printError(ctx, "Operands types are not compatible");
				return false;
			}
			
			// key to be searched is of numeric type - > verify
			else if (obj1 instanceof Variable) {
				Variable aux = (Variable) obj1;
				Variable a = new Variable(aux); // deep copy
				
				// dict key is string || boolean -> error
				if (keyType.equals("string") || keyType.equals("boolean")) {
					ErrorHandling.printError(ctx, "Operands types are not compatible");
					return false;
				}
				
				// dict accepts compatible key types -> verify
				if (!dict.isBlockedValue()) {
					
					// dict key type and expression type are compatible -> ok
					if (a.convertTypeTo(typesTable.get(keyType))) {	
						try {
							mapCtxObj.put(ctx, dict.getDict().get(a));
							return true;
						}
						catch (NullPointerException e) {
							ErrorHandling.printError(ctx, "Bad operand. Operand is null");
							return false;
						}
					}
					// dict key type and expression type are not compatible -> error
					ErrorHandling.printError(ctx, "Bad operand. Type '" + keyType + "' is not compatible with '" + aux.getType().getTypeName() + "'");
					return false;
				}
				
				// dict key type is blocked to specific type -> verify
				// dict key type and expression type are equals -> ok
				if (a.getType().getTypeName().equals(keyType)) {
					try {
						mapCtxObj.put(ctx, dict.getDict().get(a));
						return true;
					}
					catch (NullPointerException e) {
						ErrorHandling.printError(ctx, "Bad operand. Operand is null");
						return false;
					}
				}
				// dict key type and expression type are different -> error
				ErrorHandling.printError(ctx, "Bad operand. List only accepts type '" + keyType + "'");
				return false;
			}
		}
		
		return false;
	}
	
	@Override
	public Boolean visitExpression_CONTAINS(Expression_CONTAINSContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Object obj0 = mapCtxObj.get(ctx.expression(0));
		Object obj1 = mapCtxObj.get(ctx.expression(1));
		
		// expression to search value on is not type list -> error
		if (!(obj0 instanceof ListVar)) {
			ErrorHandling.printError(ctx, "Bad operand types for operator 'contains'");
			return false;
		}
		
		// expression to search value on has type list. Create ListVar variable
		ListVar dict = (ListVar) obj0;
		String valueType = dict.getType();
		
		// expression to be searched is NOT type boolean || string || numeric -> error
		if (!(obj1 instanceof Boolean) && !(obj1 instanceof String) && !(obj1 instanceof Variable)) {
			ErrorHandling.printError(ctx, "Bad operand types for operator 'contains'");
			return false;
		}
		
		// expression to be searched is of type boolean -> verify
		if (obj1 instanceof Boolean) {
			
			// list as value type boolean -> ok
			if (valueType.equals("boolean")) {
				try {
					boolean contains = dict.getList().contains(obj1);
					mapCtxObj.put(ctx, contains);
					return true;
				}
				catch (NullPointerException e) {
					ErrorHandling.printError(ctx, "Bad operand. Operand is null");
					return false;
				}
			}
			
			// list has different value type
			ErrorHandling.printError(ctx, "Operands types are not compatible");
			return false;
		}
		
		// expression to be searched is of type boolean -> verify
		else if (obj1 instanceof String) {
			
			// list has value type string -> ok
			if (valueType.equals("string")) {
				try {
					boolean contains = dict.getList().contains(obj1);
					mapCtxObj.put(ctx, contains);
					return true;
				}
				catch (NullPointerException e) {
					ErrorHandling.printError(ctx, "Bad operand. Operand is null");
					return false;
				}
			}
			
			// list has different value type
			ErrorHandling.printError(ctx, "Operands types are not compatible");
			return false;
		}
		
		// expression to be searched is of numeric type - > verify
		else if (obj1 instanceof Variable) {
			Variable aux = (Variable) obj1;
			Variable a = new Variable(aux); // deep copy
			
			// list value is string || boolean -> error
			if (valueType.equals("string") || valueType.equals("boolean")) {
				ErrorHandling.printError(ctx, "Operands types are not compatible");
				return false;
			}
			
			// list accepts compatible value types -> verify
			if (!dict.isBlocked()) {
				
				// list value type and expression type are compatible -> ok
				if (a.convertTypeTo(typesTable.get(valueType))) {	
					try {
						boolean contains = dict.getList().contains(a);
						mapCtxObj.put(ctx, contains);
						return true;
					}
					catch (NullPointerException e) {
						ErrorHandling.printError(ctx, "Bad operand. Operand is null");
						return false;
					}
				}
				// list value type and expression type are not compatible -> error
				ErrorHandling.printError(ctx, "Bad operand. Type '" + valueType + "' is not compatible with '" + aux.getType().getTypeName() + "'");
				return false;
			}
			
			// list value type is blocked to specific type -> verify
			// list value type and expression type are equals -> ok
			if (a.getType().getTypeName().equals(valueType)) {
				try {
					boolean contains = dict.getList().contains(a);
					mapCtxObj.put(ctx, contains);
					return true;
				}
				catch (NullPointerException e) {
					ErrorHandling.printError(ctx, "Bad operand. Operand is null");
					return false;
				}
			}
			// list value type and expression type are different -> error
			ErrorHandling.printError(ctx, "Bad operand. List only accepts type '" + valueType + "'");
			return false;
		}
			
		return false;
	}
	
	@Override
	public Boolean visitExpression_CONTAINSKEY(Expression_CONTAINSKEYContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Object obj0 = mapCtxObj.get(ctx.expression(0));
		Object obj1 = mapCtxObj.get(ctx.expression(1));
		
		// expression to search value on is not type dict -> error
		if (!(obj0 instanceof DictVar)) {
			ErrorHandling.printError(ctx, "Bad operand types for operator 'containsKey'");
			return false;
		}
		
		// expression to search value on has type dict. Create DicVar variable
		DictVar dict = (DictVar) obj0;
		String keyType = dict.getValueType();
		
		// expression to be searched is NOT type boolean || string || numeric -> error
		if (!(obj1 instanceof Boolean) && !(obj1 instanceof String) && !(obj1 instanceof Variable)) {
			ErrorHandling.printError(ctx, "Bad operand types for operator 'containsKey'");
			return false;
		}
		
		// expression to be searched is of type boolean -> verify
		if (obj1 instanceof Boolean) {
			
			// dict as key type boolean -> ok
			if (keyType.equals("boolean")) {
				try {
					boolean contains = dict.getDict().containsKey(obj1);
					mapCtxObj.put(ctx, contains);
					return true;
				}
				catch (NullPointerException e) {
					ErrorHandling.printError(ctx, "Bad operand. Operand is null");
					return false;
				}
			}
			
			// dict has different key type
			ErrorHandling.printError(ctx, "Operands types are not compatible");
			return false;
		}
		
		// expression to be searched is of type boolean -> verify
		else if (obj1 instanceof String) {
			
			// dict as key type string -> ok
			if (keyType.equals("string")) {
				try {
					boolean contains = dict.getDict().containsKey(obj1);
					mapCtxObj.put(ctx, contains);
					return true;
				}
				catch (NullPointerException e) {
					ErrorHandling.printError(ctx, "Bad operand. Operand is null");
					return false;
				}
			}
			
			// dict has different key type
			ErrorHandling.printError(ctx, "Operands types are not compatible");
			return false;
		}
		
		// expression to be searched is of numeric type - > verify
		else if (obj1 instanceof Variable) {
			Variable aux = (Variable) obj1;
			Variable a = new Variable(aux); // deep copy
			
			// dict key is string || boolean -> error
			if (keyType.equals("string") || keyType.equals("boolean")) {
				ErrorHandling.printError(ctx, "Operands types are not compatible");
				return false;
			}
			
			// dict accepts compatible key types -> verify
			if (!dict.isBlockedValue()) {
				
				// dict key type and expression type are compatible -> ok
				if (a.convertTypeTo(typesTable.get(keyType))) {	
					try {
						boolean contains = dict.getDict().containsKey(a);
						mapCtxObj.put(ctx, contains);
						return true;
					}
					catch (NullPointerException e) {
						ErrorHandling.printError(ctx, "Bad operand. Operand is null");
						return false;
					}
				}
				// dict key type and expression type are not compatible -> error
				ErrorHandling.printError(ctx, "Bad operand. Type '" + keyType + "' is not compatible with '" + aux.getType().getTypeName() + "'");
				return false;
			}
			
			// dict key type is blocked to specific type -> verify
			// dict key type and expression type are equals -> ok
			if (a.getType().getTypeName().equals(keyType)) {
				try {
					boolean contains = dict.getDict().containsKey(a);
					mapCtxObj.put(ctx, contains);
					return true;
				}
				catch (NullPointerException e) {
					ErrorHandling.printError(ctx, "Bad operand. Operand is null");
					return false;
				}
			}
			// dict key type and expression type are different -> error
			ErrorHandling.printError(ctx, "Bad operand. List only accepts type '" + keyType + "'");
			return false;
		}
			
		return false;
	}
	
	@Override
	public Boolean visitExpression_CONTAINSVALUE(Expression_CONTAINSVALUEContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Object obj0 = mapCtxObj.get(ctx.expression(0));
		Object obj1 = mapCtxObj.get(ctx.expression(1));
		
		// expression to search value on is not type dict -> error
		if (!(obj0 instanceof DictVar)) {
			ErrorHandling.printError(ctx, "Bad operand types for operator 'containsValue'");
			return false;
		}
		
		// expression to search value on has type dict. Create DicVar variable
		DictVar dict = (DictVar) obj0;
		String valueType = dict.getValueType();
		
		// expression to be searched is NOT type boolean || string || numeric -> error
		if (!(obj1 instanceof Boolean) && !(obj1 instanceof String) && !(obj1 instanceof Variable)) {
			ErrorHandling.printError(ctx, "Bad operand types for operator 'containsValue'");
			return false;
		}
		
		// expression to be searched is of type boolean -> verify
		if (obj1 instanceof Boolean) {
			
			// dict as value type boolean -> ok
			if (valueType.equals("boolean")) {
				try {
					boolean contains = dict.getDict().containsValue(obj1);
					mapCtxObj.put(ctx, contains);
					return true;
				}
				catch (NullPointerException e) {
					ErrorHandling.printError(ctx, "Bad operand. Operand is null");
					return false;
				}
			}
			
			// dict has different value type
			ErrorHandling.printError(ctx, "Operands types are not compatible");
			return false;
		}
		
		// expression to be searched is of type boolean -> verify
		else if (obj1 instanceof String) {
			
			// dict as value type string -> ok
			if (valueType.equals("string")) {
				try {
					boolean contains = dict.getDict().containsValue(obj1);
					mapCtxObj.put(ctx, contains);
					return true;
				}
				catch (NullPointerException e) {
					ErrorHandling.printError(ctx, "Bad operand. Operand is null");
					return false;
				}
			}
			
			// dict has different value type
			ErrorHandling.printError(ctx, "Operands types are not compatible");
			return false;
		}
		
		// expression to be searched is of numeric type - > verify
		else if (obj1 instanceof Variable) {
			Variable aux = (Variable) obj1;
			Variable a = new Variable(aux); // deep copy
			
			// dict value is string || boolean -> error
			if (valueType.equals("string") || valueType.equals("boolean")) {
				ErrorHandling.printError(ctx, "Operands types are not compatible");
				return false;
			}
			
			// dict accepts compatible value types -> verify
			if (!dict.isBlockedValue()) {
				
				// dict value type and expression type are compatible -> ok
				if (a.convertTypeTo(typesTable.get(valueType))) {	
					try {
						boolean contains = dict.getDict().containsValue(a);
						mapCtxObj.put(ctx, contains);
						return true;
					}
					catch (NullPointerException e) {
						ErrorHandling.printError(ctx, "Bad operand. Operand is null");
						return false;
					}
				}
				// dict value type and expression type are not compatible -> error
				ErrorHandling.printError(ctx, "Bad operand. Type '" + valueType + "' is not compatible with '" + aux.getType().getTypeName() + "'");
				return false;
			}
			
			// dict value type is blocked to specific type -> verify
			// dict value type and expression type are equals -> ok
			if (a.getType().getTypeName().equals(valueType)) {
				try {
					boolean contains = dict.getDict().containsValue(a);
					mapCtxObj.put(ctx, contains);
					return true;
				}
				catch (NullPointerException e) {
					ErrorHandling.printError(ctx, "Bad operand. Operand is null");
					return false;
				}
			}
			// dict value type and expression type are different -> error
			ErrorHandling.printError(ctx, "Bad operand. List only accepts type '" + valueType + "'");
			return false;
		}
			
		return false;
	}
	
	@Override
	public Boolean visitExpression_INDEXOF(Expression_INDEXOFContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Object obj0 = mapCtxObj.get(ctx.expression(0));
		Object obj1 = mapCtxObj.get(ctx.expression(1));
		
		// expression to search index on is type boolean || numeric || dict || tuple -> error
		if (obj0 instanceof Boolean || obj0 instanceof Variable || obj0 instanceof DictVar || obj0 instanceof DictTuple) {
			ErrorHandling.printError(ctx, "Bad operand types for operator 'indexof'");
			return false;
		}
		
		// expression to be searched is NOT type boolean || string || numeric -> error
		if (!(obj1 instanceof Boolean) && !(obj1 instanceof String) && !(obj1 instanceof Variable)) {
			ErrorHandling.printError(ctx, "Bad operand types for operator 'indexof'");
			return false;
		}
		
		// expression to search index on is type list -> verify
		if (obj0 instanceof ListVar) {
			
			ListVar list = (ListVar) obj0;
			String listType = list.getType();
			int index;
			
			// list type is string and expression is string -> ok
			if (listType.equals("string") && obj1 instanceof String) {
				try {
					index = list.getList().indexOf((String) obj1);
					mapCtxObj.put(ctx, new Variable(typesTable.get("number"), (double) index));
					return true;
				}
				catch (NullPointerException e) {
					ErrorHandling.printError(ctx, "Bad operand. Operand is null");
					return false;
				}
			}
			
			// list type is boolean and expression is boolean -> ok
			else if (listType.equals("boolean") && obj1 instanceof Boolean) {
				try {
					index = list.getList().indexOf((Boolean) obj1);
					mapCtxObj.put(ctx, new Variable(typesTable.get("number"), (double) index));
					return true;
				}
				catch (NullPointerException e) {
					ErrorHandling.printError(ctx, "Bad operand. Operand is null");
					return false;
				}
			}
			
			// list type is string || boolean, but expression is not compatible -> error
			else if (listType.equals("string") || listType.equals("boolean")) {
				ErrorHandling.printError(ctx, "Operands types are not compatible");
				return false;
			}
			
			// list type is numeric and expression type is numeric -> verify
			else if (obj1 instanceof Variable) {
				Variable aux = (Variable) obj1;
				Variable a = new Variable(aux); // deep copy
				
				// list accepts compatible types
				if (!list.isBlocked()) {
					// list type and expression type are compatible -> ok
					if (a.convertTypeTo(typesTable.get(listType))) {	
						try {
							index = list.getList().indexOf(a);
							mapCtxObj.put(ctx, new Variable(typesTable.get("number"), (double) index));
							return true;
						}
						catch (NullPointerException e) {
							ErrorHandling.printError(ctx, "Bad operand. Operand is null");
							return false;
						}
					}
					// list type and expression type are not compatible -> error
					ErrorHandling.printError(ctx, "Bad operand. Type '" + listType + "' is not compatible with '" + aux.getType().getTypeName() + "'");
					return false;
				}
				
				// list is blocked to specific type -> verify
				// list type and expression type are equals -> ok
				if (a.getType().getTypeName().equals(listType)) {
					try {
						index = list.getList().indexOf(a);
						mapCtxObj.put(ctx, new Variable(typesTable.get("number"), (double) index));
						return true;
					}
					catch (NullPointerException e) {
						ErrorHandling.printError(ctx, "Bad operand. Operand is null");
						return false;
					}
				}
				// list type and expression type are different -> error
				ErrorHandling.printError(ctx, "Bad operand. List only accepts type '" + listType + "'");
				return false;
			}
		}
		
		// expression to search index on has type string -> verify
		else if (obj0 instanceof String) {
			
			// expression to be searched is boolean || numeric -> error
			if (obj1 instanceof Boolean || obj1 instanceof Variable) {
				ErrorHandling.printError(ctx, "Bad operand types for operator 'indexof'");
				return false;
			}
			
			// expression to be searched is string -> ok
			String str = (String) obj0;
			String subStr = (String) obj1;
			int index = str.indexOf(subStr);
			// string does not contain sub string
			if (index == -1) {
				ErrorHandling.printError(ctx, "String does not contain sub string");
				return false;
			}
			mapCtxObj.put(ctx, new Variable(typesTable.get("number"), (double) index));
			return true;
		}
		return false;
	}
	
	@Override 
	public Boolean visitExpression_Var(Expression_VarContext ctx) {
		if(!visit(ctx.var())) {
			return false;
		}
		mapCtxObj.put(ctx, mapCtxObj.get(ctx.var()));
		return true;
	}
	
	@Override
	public Boolean visitExpression_Value(Expression_ValueContext ctx) {
		if(!visit(ctx.value())) {
			return false;
		}
		mapCtxObj.put(ctx, mapCtxObj.get(ctx.value()));
		return true;
	}

	@Override
	public Boolean visitExpression_FunctionCall(Expression_FunctionCallContext ctx) {
		if(!visit(ctx.functionCall())) {
			return false;
		}
		mapCtxObj.put(ctx, mapCtxObj.get(ctx.functionCall()));
		return true;
	}

	
	// --------------------------------------------------------------------------
	// Prints
	
	@Override
	public Boolean visitPrint(PrintContext ctx) {
		return visit(ctx.expression());
	}
	
	@Override
	public Boolean visitSave(SaveContext ctx) {
		return visit(ctx.expression());
	}
	
	// TODO its not useful if cannot be parsed from string to whatever
	@Override
	public Boolean visitInput(InputContext ctx) {
		return true;
	}

	// --------------------------------------------------------------------------
	// Variables
	
	@Override 
	public Boolean visitVar(VarContext ctx) {
		String varName = ctx.ID().getText();
		if (!symbolTableContains(varName)) {
			ErrorHandling.printError(ctx, "Variable \"" + varName + "\" is not declared!");
			return false;
		}
		mapCtxObj.put(ctx, symbolTableGet(varName));
		return true;
	}

	@Override
	public Boolean visitVarDeclaration_Variable(VarDeclaration_VariableContext ctx) {
		if(!visit(ctx.type())) {
			return false;
		}
		
		String type = (String) mapCtxObj.get(ctx.type());
		String newVarName = ctx.ID().getText();
		
		// new variable is already declared -> error
		if(symbolTableContains(newVarName)) {
			ErrorHandling.printError(ctx, "Variable '" + newVarName + "' is already declared");
			return false;
		}
		
		// type is string -> ok
		if (type.equals("string")) {
			mapCtxObj.put(ctx, "str");
		}
		
		// type is boolean -> ok
		else if (type.equals("boolean")) {
			mapCtxObj.put(ctx,  true);
		}
		
		// type is numeric -> ok
		else if (typesTable.containsKey(type)) {
			mapCtxObj.put(ctx, new Variable(typesTable.get(type), null));
		}
		
		// type is data structure -> error
		else if (type.equals("list") || type.equals("dict")) {
			ErrorHandling.printError(ctx, "Incorrect number of arguments for type '" + type + "'");
			return false;
		}
		
		// type is not defined -> error
		else {
			ErrorHandling.printError(ctx, "Type '" + type + "' is not defined in " + TypesFilePath);
			return false;
		}
		
		// update symbol table
		updateSymbolTable(newVarName, null);
		
		return true;
	}

	@Override
	public Boolean visitVarDeclaration_list(VarDeclaration_listContext ctx) {
		if(!visit(ctx.type())) {
			return false;
		}
		
		String type = (String) mapCtxObj.get(ctx.type());
		String newVarName = ctx.ID(1).getText();
		
		// new variable is already declared -> error
		if(symbolTableContains(newVarName)) {
			ErrorHandling.printError(ctx, "Variable '" + newVarName + "' is already declared");
			return false;
		}
		
		// type is string || type is boolean || type is numeric -> error
		if (type.equals("string") || type.equals("boolean") || typesTable.containsKey(type)) {
			ErrorHandling.printError(ctx, "Incorrect declaration for type '" + type + "'");
			return false;
		}
		
		// type is dict -> error
		else if (type.equals("dict")) {
			ErrorHandling.printError(ctx, "Incorrect number of arguments for type '" + type + "'");
			return false;
		}
		
		// type is list -> ok
		else if (type.equals("list")){
			String listValuesType = ctx.ID(0).getText();
			ListVar list;
			
			// list type is string || boolean || numeric -> ok
			if (listValuesType.equals("string") || listValuesType.equals("boolean") || symbolTableContains(listValuesType)) {
				boolean blockedType = false;
				if (ctx.block == null) {
					blockedType = true;
				}
				list = new ListVar(listValuesType, blockedType);
				mapCtxObj.put(ctx, list);
			}
			
			// other list types or undefined types -> error
			else {
				ErrorHandling.printError(ctx, "Incorrect argument for list type");
				return false;
			}
		}
		
		// type is not defined -> error
		else {
			ErrorHandling.printError(ctx, "Type '" + type + "' is not defined in " + TypesFilePath);
			return false;
		}
		
		// update symbol table
		updateSymbolTable(newVarName, null);
		
		return true;
	}

	@Override
	public Boolean visitVarDeclaration_dict(VarDeclaration_dictContext ctx) {
		if(!visit(ctx.type())) {
			return false;
		}
		
		String type = (String) mapCtxObj.get(ctx.type());
		String newVarName = ctx.ID(2).getText();
		
		// new variable is already declared -> error
		if(symbolTableContains(newVarName)) {
			ErrorHandling.printError(ctx, "Variable '" + newVarName + "' is already declared");
			return false;
		}
		
		// type is string || type is boolean || type is numeric -> error
		if (type.equals("string") || type.equals("boolean") || typesTable.containsKey(type)) {
			ErrorHandling.printError(ctx, "Incorrect declaration for type '" + type + "'");
			return false;
		}
		
		// type is list -> error
		else if (type.equals("list")) {
			ErrorHandling.printError(ctx, "Incorrect number of arguments for type '" + type + "'");
			return false;
		}
		
		// type is dict -> ok
		else if (type.equals("dict")){
			String dictKeyType = ctx.ID(0).getText();
			String dictValueType = ctx.ID(1).getText();
			DictVar dict;
			
			// dict key and value types are string || boolean || numeric -> ok
			if (dictKeyType.equals("string") || dictKeyType.equals("boolean") || symbolTableContains(dictKeyType)) {
				if (dictValueType.equals("string") || dictValueType.equals("boolean") || symbolTableContains(dictValueType)) {
					boolean blockKeyType = false;
					boolean blockValType = false;
					if (ctx.block0 == null) {
						blockKeyType = true;
					}
					if (ctx.block1 == null) {
						blockValType = true;
					}
					dict = new DictVar(dictKeyType, blockKeyType, dictValueType, blockValType);
					mapCtxObj.put(ctx, dict);
				}
			}
			
			// other dict key and value types or undefined types -> error
			else {
				ErrorHandling.printError(ctx, "Incorrect argument for list type");
				return false;
			}	
		}
		
		// type is not defined -> error
		else {
			ErrorHandling.printError(ctx, "Type '" + type + "' is not defined in " + TypesFilePath);
			return false;
		}
		
		// update symbol table
		updateSymbolTable(newVarName, null);
		
		return true;
	}
	
	// --------------------------------------------------------------------------
	// Types

	@Override 
	public Boolean visitType_Number_Type(Type_Number_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.NUMBER_TYPE().getText());
		return true;
	}

	@Override 
	public Boolean visitType_Boolean_Type(Type_Boolean_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.BOOLEAN_TYPE().getText());
		return true;
	}

	@Override 
	public Boolean visitType_String_Type(Type_String_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.STRING_TYPE().getText());
		return true;
	}

	@Override 
	public Boolean visitType_Void_Type(Type_Void_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.VOID_TYPE().getText());
		return true;
	}
	
	@Override
	public Boolean visitType_List_Type(Type_List_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.LIST_TYPE().getText());
		return super.visitType_List_Type(ctx);
	}

	@Override
	public Boolean visitType_Dict_Type(Type_Dict_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.DICT_TYPE().getText());
		return super.visitType_Dict_Type(ctx);
	}
	
	@Override 
	public Boolean visitType_ID_Type(Type_ID_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.ID().getText());
		return true;
	}
	
	@Override
	public Boolean visitValue_Number(Value_NumberContext ctx) {
		mapCtxObj.put(ctx, new Variable(typesTable.get("number"), Double.parseDouble(ctx.NUMBER().getText())));
		return true;
	}

	@Override
	public Boolean visitValue_Boolean(Value_BooleanContext ctx) {
		mapCtxObj.put(ctx, true);
		return true;
	}

	@Override
	public Boolean visitValue_String(Value_StringContext ctx) {
		mapCtxObj.put(ctx, getStringText(ctx.STRING().getText()));
		return true;
	}

	@Override
	public Boolean visitCast(CastContext ctx) {
		mapCtxObj.put(ctx, ctx.ID().getText());
		return true;
	}

	// -------------------------------------------------------------------------
	// Auxiliar Fucntion
	
	

	

	/**
	 * Extends the previous scope into a new scope for use inside control flow statements
	 */
	private static void extendScope() {
		// create copy of scope context
		HashMap<String, Object> newScope = new HashMap<>();
		HashMap<String, Object> oldScope = symbolTable.get(0);
		for (String key : oldScope.keySet()) {
			newScope.put(key, oldScope.get(key));
		}
		symbolTable.add(newScope);
	}
	
	/**
	 * Creates a new clean scope for the function and adds global variables (that are always in scope[0]
	 */
	private static void openFunctionScope() {
		HashMap<String, Object> newScope = new HashMap<>();
		HashMap<String, Object> globalScope = symbolTable.get(0);
		for (String key : globalScope.keySet()) {
			newScope.put(key, globalScope.get(key));
		}
		symbolTable.add(newScope);
	}
	
	
	private static void closeScope() {
		int lastIndex = symbolTable.size();
		symbolTable.remove(lastIndex);
	}
	
	private static void updateSymbolTable(String key, Object value) {
		int lastIndex = symbolTable.size();
		symbolTable.get(lastIndex).put(key, value);
	}
	
	private static Object symbolTableGet(String key) {
		int lastIndex = symbolTable.size();
		return symbolTable.get(lastIndex).get(key);
	}
	
	private static boolean symbolTableContains(String key) {
		int lastIndex = symbolTable.size();
		return symbolTable.get(lastIndex).containsKey(key);
	}

	private static boolean isValidNewVariableName(String varName, ParserRuleContext ctx) {

		if (symbolTableContains(varName)) {
			ErrorHandling.printError(ctx, "Variable \"" + varName +"\" already declared");
			return false;
		}
		
		if (reservedWords.contains(varName)) {
			ErrorHandling.printError(ctx, varName +"\" is a reserved word");
			return false;
		}
		
		return true;
	}
	

	private static String getStringText(String str) {
		str = str.substring(1, str.length() -1);
		return str;
	}

}