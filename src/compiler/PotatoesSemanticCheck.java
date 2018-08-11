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
import java.util.Locale;
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
import potatoesGrammar.utils.varType;
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

	protected static ParseTreeProperty<Variable> 		mapCtxVar		= new ParseTreeProperty<>();
	protected static List<HashMap<String, Variable>>	symbolTable 	= new ArrayList<>();
	
	protected static boolean visitedMain = false;
	protected static Object currentReturn = null;
	
 	public PotatoesSemanticCheck(String PotatoesFilePath){
		functions = new PotatoesFunctionNames(PotatoesFilePath);
		functionNames = functions.getFunctions();
		functionArgs = functions.getFunctionsArgs();
		symbolTable.add(new HashMap<String, Variable>());
		if (debug) ErrorHandling.printInfo("The PotatoesFilePath is: " + PotatoesFilePath);
	}
	
	// --------------------------------------------------------------------------
	// Getters
	public static ParseTreeProperty<Object> getmapCtxVar(){
		return mapCtxVar;
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

		String typeName = (String) mapCtxVar.get(ctx.varDeclaration().type());
		String varName = ctx.varDeclaration().ID().getText();
		Object obj = mapCtxVar.get(ctx.expression());

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
				mapCtxVar.put(ctx, "str");
				return true;
			}
			ErrorHandling.printError(ctx, "expression result is not compatible with string Type");
			return false;
		}

		// assign Variable to boolean is not possible
		if (typeName.equals("boolean")) {
			if (obj instanceof Boolean) {
				updateSymbolTable(ctx.varDeclaration().ID().getText(), true);
				mapCtxVar.put(ctx, true);
				return true;
			}
			ErrorHandling.printError(ctx, "expression result is not compatible with boolean Type");
			return false;
		}

		// assign Variable to Variable
		Variable temp = (Variable) mapCtxVar.get(ctx.expression());
		Variable a = new Variable(temp); // deep copy

		if (debug) {
			ErrorHandling.printInfo(ctx, "--- Variable to assign is " + a);
			ErrorHandling.printInfo(ctx, "type to assign to is: " + typesTable.get(typeName));
		}

		if (a.convertTypeTo(typesTable.get(typeName))) {
			updateSymbolTable(ctx.varDeclaration().ID().getText(), a);
			mapCtxVar.put(ctx, a);
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
		
		Object varObj = mapCtxVar.get(ctx.var());
		Object exprResObj = mapCtxVar.get(ctx.expression());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_OP] Visited visitAssignment_Var_Declaration_Expression");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + ctx.var().ID().getText() + " with type " + checkSymbolTable().get(ctx.var().ID().getText()));
		}

		if(varObj instanceof String) {
			if (exprResObj instanceof String) {
				updateSymbolTable(ctx.var().ID().getText(), "str");
				mapCtxVar.put(ctx, "str");
				return true;
			}
			ErrorHandling.printError(ctx, "expression result is not compatible with string Type");
			return false;
		}

		if(varObj instanceof Boolean) {
			if (exprResObj instanceof Boolean) {
				updateSymbolTable(ctx.var().ID().getText(), true);
				mapCtxVar.put(ctx, true);
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
			mapCtxVar.put(ctx, a);
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
		List<Object> args = (List<Object>) mapCtxVar.get(ctx.getParent());
		
		// open new scope
		openFunctionScope();
		
		// store new variables with function call value and function signature name
		for (int i = 0; i < args.size(); i++) {
			updateSymbolTable((String) mapCtxVar.get(ctx.type(i)), args.get(i));
		}
		
		return valid && visit(ctx.scope());
	}

	@Override
	public Boolean visitFunctionReturn(FunctionReturnContext ctx) {
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Object obj = mapCtxVar.get(ctx.expression());
		
		if ((currentReturn instanceof String && obj instanceof String) || (currentReturn instanceof Boolean && obj instanceof Boolean)) {
			mapCtxVar.put(ctx, mapCtxVar.get(ctx.expression()));
			return true;
		}
		
		if (currentReturn instanceof Variable && obj instanceof Variable) {
			Variable a = (Variable) obj;
			Variable b = (Variable) currentReturn;
			if(a.typeIsCompatible(b)) {
				mapCtxVar.put(ctx, mapCtxVar.get(ctx.expression()));
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
			functionCallArgs.add(mapCtxVar.get(expr));
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
		
		mapCtxVar.put(ctx, functionCallArgs);
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
		mapCtxVar.put(ctx, mapCtxVar.get(ctx.expression()));
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
		Object obj = mapCtxVar.get(ctx.expression());
		
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
				mapCtxVar.put(ctx, new Variable(newType, value));
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

			mapCtxVar.put(ctx, opRes);
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
		Object obj = mapCtxVar.get(ctx.expression());
		
		if (op.equals("-")) {
			
			if (obj instanceof String) {
				ErrorHandling.printError(ctx, "Unary Simetric operator cannot be applied to string Type");
				return false;
			}
			if(obj instanceof Boolean) {
				ErrorHandling.printError(ctx, "Unary Simetric operator cannot be applied to boolean Type");
				return false;
			}
			
			mapCtxVar.put(ctx, obj); // don't need to calculate symmetric value to guarantee semantic correctness in future calculations

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
			
			mapCtxVar.put(ctx, true);

			if (debug) ErrorHandling.printInfo(ctx, "[OP_LOGIC_OPERAND_NOT_VAR]");
		}
		
		return true;
	}
	
	@Override 
	public Boolean visitExpression_Power(Expression_PowerContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Object base = mapCtxVar.get(ctx.expression(0));
		Object pow = mapCtxVar.get(ctx.expression(1));
		
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
			mapCtxVar.put(ctx, "str"); // strings can be powered, "str"^3 == "strstrstr"
			return true;
		}
		if (base instanceof Variable) {
			Variable aux = (Variable) pow;
			Variable powVar = new Variable(aux);
			aux = (Variable) base;
			Variable baseVar = new Variable(aux);
			
			Variable res = Variable.power(baseVar, powVar);
			mapCtxVar.put(ctx, res);
			
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
		Object obj0 = mapCtxVar.get(ctx.expression(0));
		Object obj1 = mapCtxVar.get(ctx.expression(1));
		
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
				mapCtxVar.put(ctx, "str");
				return true;
			}
		}
		if(obj0 instanceof Variable && obj1 instanceof String) {
			Variable var = (Variable) obj0;
			if (var.getType().equals(typesTable.get("number")) && op.equals("*")) {
				mapCtxVar.put(ctx, "str");
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
					mapCtxVar.put(ctx, res);
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
				mapCtxVar.put(ctx, res); 
				if (debug) { ErrorHandling.printInfo(ctx, "result of multiplication is Variable " + res);}
				return true;
			}
			
			// Division expression
			if (op.equals("/")) {
				try {
					Variable res = Variable.divide(a, b); 
					mapCtxVar.put(ctx, res);
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
		
		Object obj0 = mapCtxVar.get(ctx.expression(0));
		Object obj1 = mapCtxVar.get(ctx.expression(1));
		String op = ctx.op.getText();
		
		// one of the elements in expression is boolean
		if(obj0 instanceof Boolean || obj1 instanceof Boolean) {
			ErrorHandling.printError(ctx, "bad operand Types for operator '" + op + "'");
			return false;
		}
		
		// both elements are string (concatenation or remove)
		if(obj0 instanceof String && obj1 instanceof String) {
			mapCtxVar.put(ctx, "str");
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
				mapCtxVar.put(ctx, res);
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
		
		Object obj0 = mapCtxVar.get(ctx.expression(0));
		Object obj1 = mapCtxVar.get(ctx.expression(1));
		String op = ctx.op.getText();
		
		if(obj0 instanceof Boolean || obj1 instanceof Boolean) {
			ErrorHandling.printError(ctx, "bad operand types for relational operator '" + op + "'");
			return false;
		}
		
		if(obj0 instanceof String && obj1 instanceof String) {
			mapCtxVar.put(ctx, true);
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
			
			mapCtxVar.put(ctx, comparisonIsPossible);
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
		
		Object obj0 = mapCtxVar.get(ctx.expression(0));
		Object obj1 = mapCtxVar.get(ctx.expression(1));
		
		if (obj0 instanceof Boolean && obj1 instanceof Boolean) {
			mapCtxVar.put(ctx, true);
			return true;
		}
		
		if (obj0 instanceof String && obj1 instanceof String) {
			mapCtxVar.put(ctx, "str");
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
			
			mapCtxVar.put(ctx, comparisonIsPossible);
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
		
		Object obj0 = mapCtxVar.get(ctx.expression(0));
		Object obj1 = mapCtxVar.get(ctx.expression(1));
		String op = ctx.op.getText();
		
		if(obj0 instanceof Variable || obj1 instanceof Variable || obj0 instanceof String || obj1 instanceof String) {
			ErrorHandling.printError(ctx, "bad operand types for logical operator '" + op + "'");
			return false;
		}
		
		// both obj have Type boolean
		mapCtxVar.put(ctx, true);
		return true;
	}
	
	@Override
	public Boolean visitExpression_tuple(Expression_tupleContext ctx) {
		// TODO Auto-generated method stub
		return super.visitExpression_tuple(ctx);
	}
	
	@Override
	public Boolean visitExpression_ADD(Expression_ADDContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Object obj0 = mapCtxVar.get(ctx.expression(0));
		Object obj1 = mapCtxVar.get(ctx.expression(1));
		
		// left expression is type boolean || tuple -> error
		if (obj0 instanceof Boolean || obj0 instanceof DictTuple) {
			ErrorHandling.printError(ctx, "Bad operand types for operator 'add'");
			return false;
		}
		
		// right expression to be searched is type list || dict -> error
		if (obj1 instanceof ListVar || obj1 instanceof DictVar) {
			ErrorHandling.printError(ctx, "Bad operand types for operator 'add'");
			return false;
		}
		
		// left expression is type list -> verify
		if (obj0 instanceof ListVar) {
			
			ListVar dict = (ListVar) obj0;
			String valueType = dict.getType();
			
			// right expression is type tuple -> error
			if (obj1 instanceof DictTuple) {
				ErrorHandling.printError(ctx, "Operand types are not compatible");
				return false;
			}
			
			// expression to be added is type boolean -> verify
			if (obj1 instanceof Boolean) {
				
				// list as value type boolean -> ok
				if (valueType.equals("boolean")) {
					try {
						boolean contains = dict.getList().contains(obj1);
						mapCtxVar.put(ctx, contains);
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
						mapCtxVar.put(ctx, contains);
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
							mapCtxVar.put(ctx, contains);
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
						mapCtxVar.put(ctx, contains);
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
		}
		
		// expression to search key on is dict -> verify
		else if (obj0 instanceof DictVar) {
			DictVar dict = (DictVar) obj1;
			String keyType = dict.getKeyType();
			
			// key to be searched is of type boolean -> verify
			if (obj1 instanceof Boolean) {
				
				// dict as key type boolean -> ok
				if (keyType.equals("boolean")) {
					try {
						dict.getDict().remove(obj1);
						mapCtxVar.put(ctx, dict);
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
						dict.getDict().remove(obj1);
						mapCtxVar.put(ctx, dict);
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
							dict.getDict().remove(a);
							mapCtxVar.put(ctx, dict);
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
						dict.getDict().remove(a);
						mapCtxVar.put(ctx, dict);
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
		
		// expression to search on has type string -> verify
		else if (obj0 instanceof String) {
			
			// expression to be searched is boolean || numeric -> error
			if (obj1 instanceof Boolean || obj1 instanceof Variable) {
				ErrorHandling.printError(ctx, "Bad operand types for operator 'indexof'");
				return false;
			}
			
			// expression to be searched is string -> ok
			String str = (String) obj0;
			String subStr = (String) obj1;
			str = str.replace(subStr, "");
			mapCtxVar.put(ctx, str);
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean visitExpression_REM(Expression_REMContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		// expression to search index on is type list -> verify
		if (var0.isList()) {
			
			ListVar listVar = (ListVar) var0.getValue();
			
			// expression type is 'number' -> verify
			if (var1.isNumeric()) {
					
				if (var1.getType().equals(typesTable.get("number"))) {
					try {
						int index = (int) var1.getValue();
						Variable rem = listVar.getList().remove(index);
						mapCtxVar.put(ctx, rem);
						return true;
					}
					catch (IndexOutOfBoundsException e) {
						ErrorHandling.printError(ctx, "Index out of bounds");
						return false;
					}
				}
			}
			
			// expression for index is not 'number -> error
			ErrorHandling.printError(ctx, "Not a valid index");
			return false;	
		}
		
		// expression to search key on is dict -> verify
		else if (var0.isDict()) {
			
			DictVar dictVar = (DictVar) var1.getValue() ;
			String keyType = dictVar.getKeyType();
			
			// dict accepts compatible key types -> verify
			if (!dictVar.isBlockedKey()) {
				
				// dict key type and expression type are not compatible -> error
				if (!var1.convertTypeTo(typesTable.get(keyType))) {	
					ErrorHandling.printError(ctx, "Bad operand. Type '" + keyType + "' is not compatible with '" + var1.getType().getTypeName() + "'");
					return false;
				}
				// dict key type and expression type are compatible -> ok (jumps to next code
				
			}
			
			// dict key type is blocked to specific type -> verify
			Variable rem = dictVar.getDict().remove(var1);
			// if dictionary does not contain key
			if (rem == null) {
				ErrorHandling.printError(ctx, "Dictionary does not contain key or value");
				return false;
			}
			// update tables
			mapCtxVar.put(ctx, rem);
			return true;
		}
		
		// expression to search on has type string (removeAll)-> verify
		else if (var0.isString()) {
			
			// expression to be searched is also string -> ok
			if (var1.isString()) {
				String str = (String) var0.getValue();
				String subStr = (String) var1.getValue();
				str = str.replace(subStr, "");
				mapCtxVar.put(ctx, new Variable(null, varType.STRING, str));
				return true;
			}
		}
		
		// left espression is not list || dict || string nor right expression is boolean || string || numeric -> error
		ErrorHandling.printError(ctx, "Bad operand for operator 'rem'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_GET(Expression_GETContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		// expression to search index on is list -> verify
		if(var0.isList()) {
			
			ListVar listVar = (ListVar) var0.getValue();
			
			// expression for index search is 'number' -> ok
			if (var1.isNumeric()) {

				var1 = new Variable(var1); // deep copy
				
				if (var1.getType().equals(typesTable.get("number"))) {
					try {
						int index = (int) var1.getValue();
						Variable get = (Variable) listVar.getList().get(index);
						mapCtxVar.put(ctx, get);
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
		else if (var0.isDict()) {
			
			DictVar dictVar = (DictVar) var1.getValue();
			String keyType = dictVar.getKeyType();
			
			// dict accepts compatible key types -> verify
			if (!dictVar.isBlockedKey()) {
				
				// dict key type and expression type are not compatible -> error
				if (!var1.convertTypeTo(typesTable.get(keyType))) {	
					ErrorHandling.printError(ctx, "Bad operand. Type '" + keyType + "' is not compatible with '" + var1.getType().getTypeName() + "'");
					return false;
				}
				// dict key type and expression type are compatible -> ok (jumps to next code)
			}
			
			// dict does not accept compatible key types -> verify
			Variable get = (Variable) dictVar.getDict().get(var1);
			// if dictionary does not contain key
			if (get == null) {
				ErrorHandling.printError(ctx, "Dictionary does not contain key");
				return false;
			}
			// update tables 
			mapCtxVar.put(ctx, get);
			return true;
		}
		
		// left expression is not list || dict nor right expression is boolean || string || numeric -> error
		ErrorHandling.printError(ctx, "Bad operand types for operator 'get'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_CONTAINS(Expression_CONTAINSContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		// expression to search value on has type list -> ok
		if (var0.isList()) {
			
			ListVar listVar = (ListVar) var0.getValue();
			String valueType = listVar.getType();
			
			// list accepts compatible value types -> verify
			if (!listVar.isBlocked()) {
				
				// list value type and expression type are compatible -> ok
				if (var1.convertTypeTo(typesTable.get(valueType))) {	
					boolean contains = listVar.getList().contains(var1);
					mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, contains));
					return true;
				}
				// list value type and expression type are not compatible -> error
				ErrorHandling.printError(ctx, "Bad operand. Type '" + valueType + "' is not compatible with '" + var1.getType().getTypeName() + "'");
				return false;
			}
			
			// list value type is blocked to specific type -> ok
			boolean contains = listVar.getList().contains(var1);
			mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, contains));
			return true;
		}
		
		// expression to search value on has type string -> verify
		else if (var0.isString()) {
			
			// expression to be searched is string -> ok
			if (var1.isString()) {
				String str = (String) var0.getValue();
				String subStr = (String) var1.getValue();
				boolean contains = str.contains(subStr);
				mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, contains));
				return true;
			}
			
			// expression to e searched is not string -> error
			ErrorHandling.printError(ctx, "Operands are not compatible");
			return false;
		}
		
		// left expression is not list || string nor right expression is not boolean || string || numeric -> error
		ErrorHandling.printError(ctx, "Bad operand types for operator 'contains'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_CONTAINSKEY(Expression_CONTAINSKEYContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		// expression to search value on has type dict -> ok
		
		if (var0.isDict()) {
			
			DictVar dictVar = (DictVar) var0.getValue();
			String keyType = dictVar.getValueType();
			
			// dict accepts compatible key types -> verify
			if (!dictVar.isBlockedValue()) {
				
				// dict key type and expression type are compatible -> ok
				if (var1.convertTypeTo(typesTable.get(keyType))) {	
					boolean contains = dictVar.getDict().containsKey(var1);
					mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, contains));
					return true;
				}
				// dict key type and expression type are not compatible -> error
				ErrorHandling.printError(ctx, "Bad operand. Type '" + keyType + "' is not compatible with '" + var1.getType().getTypeName() + "'");
				return false;
			}
			
			// dict key type is blocked to specific type -> ok
			boolean contains = dictVar.getDict().containsKey(var1);
			mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, contains));
			return true;
		}
		
		// either left expression is not dict or right expression is not boolean || string || numeric -> error
		ErrorHandling.printError(ctx, "Bad operand types for operator 'containsKey'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_CONTAINSVALUE(Expression_CONTAINSVALUEContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		// expression to search value on is dict -> ok
		if (var0.isDict()) {
			
			DictVar dict = (DictVar) var0.getValue();
			String valueType = dict.getValueType();
			
			// dict accepts compatible value types -> verify
			if (!dict.isBlockedValue()) {
				
				// dict value type and expression type are compatible -> ok
				if (var1.convertTypeTo(typesTable.get(valueType))) {	
					boolean contains = dict.getDict().containsValue(var1);
					mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, contains));
					return true;
				}
				// dict value type and expression type are not compatible -> error
				ErrorHandling.printError(ctx, "Bad operand. Type '" + valueType + "' is not compatible with '" + var1.getType().getTypeName() + "'");
				return false;
			}
			
			// dict value type is blocked to specific type -> ok
			boolean contains = dict.getDict().containsValue(var1);
			mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, contains));
			return true;
		}
		
		// either left expression is not dict or right expression is not boolean || string || numeric -> error
		ErrorHandling.printError(ctx, "Bad operand types for operator 'containsValue'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_INDEXOF(Expression_INDEXOFContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		// expression to search index on is a list -> verify
		if (var0.isList()) {
			
			ListVar listVar = (ListVar) var0.getValue();
			String listType = listVar.getType();
			int index;
			
			// in case list accepts compatible types, tries to convert, then searches
			// if conversion is unsuccessful, will search original variable
			if (var1.convertTypeTo(typesTable.get(listType))) {	
				index = listVar.getList().indexOf(var1);
				mapCtxVar.put(ctx, new Variable(typesTable.get("number"), varType.NUMERIC, (double) index));
				return true;
			}
			// list type and expression type are different -> error
			ErrorHandling.printError(ctx, "Bad operand. List has parameterized type '" + listType +
					"' (accepts compatible? -> " + listVar.isBlocked() + ")");
			return false;
		}
		
		// expression to search index on has type string -> verify
		else if (var0.isString()) {
			
			// expression to be searched is string -> ok
			if (var1.isString()) {
				String str = (String) var0.getValue();
				String subStr = (String) var1.getValue();
				int index = str.indexOf(subStr);
				mapCtxVar.put(ctx, new Variable(typesTable.get("number"), varType.NUMERIC, (double) index));
				return true;
			}	
		}
		
		// expression to search index on is not list or string -> error
		ErrorHandling.printError(ctx, "Bad operand types for operator 'indexof'");
		return false;	
	}
	
	@Override 
	public Boolean visitExpression_Var(Expression_VarContext ctx) {
		if(!visit(ctx.var())) {
			return false;
		}
		mapCtxVar.put(ctx, mapCtxVar.get(ctx.var()));
		return true;
	}
	
	@Override
	public Boolean visitExpression_Value(Expression_ValueContext ctx) {
		if(!visit(ctx.value())) {
			return false;
		}
		mapCtxVar.put(ctx, mapCtxVar.get(ctx.value()));
		return true;
	}

	@Override
	public Boolean visitExpression_FunctionCall(Expression_FunctionCallContext ctx) {
		if(!visit(ctx.functionCall())) {
			return false;
		}
		mapCtxVar.put(ctx, mapCtxVar.get(ctx.functionCall()));
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
		// variable is declared -> ok
		if (symbolTableContains(varName)) {
			mapCtxVar.put(ctx, symbolTableGet(varName));
			return true;
		}
		// variable is not declared -> error
		ErrorHandling.printError(ctx, "Variable \"" + varName + "\" is not declared!");
		return false;
		
	}

	@Override
	public Boolean visitVarDeclaration_Variable(VarDeclaration_VariableContext ctx) {
		if(!visit(ctx.type())) {
			return false;
		}
		
		Variable type = mapCtxVar.get(ctx.type());
		String newVarName = ctx.ID().getText();
		Variable newVar = new Variable(type); // deep copy .. type already contains information necessary to create variable
		
		// new variable is already declared -> error
		if(symbolTableContains(newVarName)) {
			ErrorHandling.printError(ctx, "Variable '" + newVarName + "' is already declared");
			return false;
		}
		
		// type is data structure -> error
		if (type.isList() || type.isDict()) {
			ErrorHandling.printError(ctx, "Incorrect number of arguments for type '" + type + "'");
			return false;
		}
		
		// update tables
		mapCtxVar.put(ctx, newVar);
		updateSymbolTable(newVarName, newVar);
		
		return true;
	}

	@Override
	public Boolean visitVarDeclaration_list(VarDeclaration_listContext ctx) {
		if(!visit(ctx.type())) {
			return false;
		}
		
		Variable type = mapCtxVar.get(ctx.type());
		String listParamName = ctx.ID(0).getText();
		varType listParam = varType.valueOf(listParamName.toUpperCase(Locale.ENGLISH));
		String newVarName = ctx.ID(1).getText();
		
		// new variable is already declared -> error
		if(symbolTableContains(newVarName)) {
			ErrorHandling.printError(ctx, "Variable '" + newVarName + "' is already declared");
			return false;
		}
		
		// type is list -> ok
		else if (type.isList()){
			
			// list parameterized type is string || boolean || numeric -> ok
			if (listParam.isString() || listParam.isBoolean() || listParam.isNumeric()) {
				// verify if list type is blocked or accepts compatible types
				boolean blockedType = false;
				if (ctx.block == null) {
					blockedType = true;
				}
				ListVar listVar = new ListVar(listParamName, blockedType);
				Variable list = new Variable(null, varType.LIST, listVar);
				
				// update tables
				mapCtxVar.put(ctx, list);
				updateSymbolTable(newVarName, list);
				return true;
			}
			
			// other list types -> error
			ErrorHandling.printError(ctx, "Incorrect argument for list type");
			return false;
		}
		
		// type is not list -> error
		ErrorHandling.printError(ctx, "Incorrect number of arguments for type '" + type + "'");
		return false;
	}

	@Override
	public Boolean visitVarDeclaration_dict(VarDeclaration_dictContext ctx) {
		if(!visit(ctx.type())) {
			return false;
		}
		
		Variable type = mapCtxVar.get(ctx.type());
		String keyTypeName = ctx.ID(0).getText();
		String valueTypeName = ctx.ID(1).getText();
		varType keyType = varType.valueOf(keyTypeName.toUpperCase(Locale.ENGLISH));
		varType valueType = varType.valueOf(valueTypeName.toUpperCase(Locale.ENGLISH));
		String newVarName = ctx.ID(1).getText();
		
		// new variable is already declared -> error
		if(symbolTableContains(newVarName)) {
			ErrorHandling.printError(ctx, "Variable '" + newVarName + "' is already declared");
			return false;
		}
		
		// type is dict -> ok
		else if (type.isDict()){
			
			// dict key type and value type are string || boolean || numeric -> ok
			if (keyType.isString() || keyType.isBoolean() || keyType.isNumeric()) {
				if (valueType.isString() || valueType.isBoolean() || valueType.isNumeric()) {
					
					// verify if dict key type and value type are blocked or accept compatible types
					boolean blockKeyType = false;
					if (ctx.block0 == null) {
						blockKeyType = true;
					}
					boolean blockValType = false;
					if (ctx.block0 == null) {
						blockValType = true;
					}
					
					DictVar dictVar = new DictVar(keyTypeName, blockKeyType, valueTypeName, blockValType);
					Variable dict = new Variable(null, varType.DICT, dictVar);
					
					// update tables
					mapCtxVar.put(ctx, dict);
					updateSymbolTable(newVarName, dict);
					return true;
				}
			}
			
			// other dict types -> error
			ErrorHandling.printError(ctx, "Incorrect arguments for dict type");
			return false;
		}
		
		// type is not dict -> error
		ErrorHandling.printError(ctx, "Incorrect number of arguments for type '" + type + "'");
		return false;
	}
	
	// --------------------------------------------------------------------------
	// Types

	@Override 
	public Boolean visitType_Number_Type(Type_Number_TypeContext ctx) {
		Variable var = new Variable (typesTable.get("number"), varType.NUMERIC, null);
		mapCtxVar.put(ctx, var);
		return true;
	}

	@Override 
	public Boolean visitType_Boolean_Type(Type_Boolean_TypeContext ctx) {
		Variable var = new Variable (null, varType.BOOLEAN, null);
		mapCtxVar.put(ctx, var);
		return true;
	}

	@Override 
	public Boolean visitType_String_Type(Type_String_TypeContext ctx) {
		Variable var = new Variable (null, varType.STRING, null);
		mapCtxVar.put(ctx, var);
		return true;
	}

//	@Override 
//	public Boolean visitType_Void_Type(Type_Void_TypeContext ctx) {
//		Variable var = new Variable (typesTable.get("number"), varType.NUMERIC, null);
//		mapCtxVar.put(ctx, var);
//		return true;
//	}
	
	@Override
	public Boolean visitType_List_Type(Type_List_TypeContext ctx) {
		Variable var = new Variable (null, varType.LIST, null);
		mapCtxVar.put(ctx, var);
		return true;
	}

	@Override
	public Boolean visitType_Dict_Type(Type_Dict_TypeContext ctx) {
		Variable var = new Variable (null, varType.DICT, null);
		mapCtxVar.put(ctx, var);
		return true;
	}
	
	@Override 
	public Boolean visitType_ID_Type(Type_ID_TypeContext ctx) {
		String typeName = ctx.ID().getText();
		// type exists -> ok
		if (typesTable.containsKey(typeName)) {
			Variable var = new Variable (typesTable.get(typeName), varType.NUMERIC, null);
			mapCtxVar.put(ctx, var);
			return true;
		}
		// type is not declared in types file -> error
		ErrorHandling.printError(ctx, "Invalid type. Type '" + typeName + "' is not declared");
		return false;
	}
	
	@Override
	public Boolean visitValue_Number(Value_NumberContext ctx) {
		try {
			Variable var = new Variable(typesTable.get("number"), varType.NUMERIC, Double.parseDouble(getStringText(ctx.NUMBER().getText())));
			mapCtxVar.put(ctx, var);
			return true;
		}
		catch (NumberFormatException e) {
			ErrorHandling.printError(ctx, "Invalid number value");
			return false;
		}
	}

	@Override
	public Boolean visitValue_Boolean(Value_BooleanContext ctx) {
		Variable var = new Variable(null, varType.BOOLEAN, Boolean.parseBoolean(getStringText(ctx.BOOLEAN().getText())));
		mapCtxVar.put(ctx, var);
		return true;
	}

	@Override
	public Boolean visitValue_String(Value_StringContext ctx) {
		Variable var = new Variable(null, varType.STRING, getStringText(ctx.STRING().getText()));
		mapCtxVar.put(ctx, var);
		return true;
	}

	@Override
	public Boolean visitCast(CastContext ctx) {
		String castName = ctx.ID().getText();
		// cast type exists -> ok
		if (typesTable.containsKey(castName)){
			Variable var = new Variable(typesTable.get(castName), null, null);
			mapCtxVar.put(ctx, var);
			return true;
		}
		// cast type does not exist -> error
		ErrorHandling.printError(ctx, "Invalid cast Type. Type '" + castName + "' does not exist");
		return false;
	}

	// -------------------------------------------------------------------------
	// Auxiliar Fucntion
	
	/**
	 * Extends the previous scope into a new scope for use inside control flow statements
	 */
	private static void extendScope() {
		// create copy of scope context
		HashMap<String, Variable> newScope = new HashMap<>();
		HashMap<String, Variable> oldScope = symbolTable.get(0);
		for (String key : oldScope.keySet()) {
			newScope.put(key, oldScope.get(key));
		}
		symbolTable.add(newScope);
	}
	
	/**
	 * Creates a new clean scope for the function and adds global variables (that are always in scope[0]
	 */
	private static void openFunctionScope() {
		HashMap<String, Variable> newScope = new HashMap<>();
		HashMap<String, Variable> globalScope = symbolTable.get(0);
		for (String key : globalScope.keySet()) {
			newScope.put(key, globalScope.get(key));
		}
		symbolTable.add(newScope);
	}
	
	private static void closeScope() {
		int lastIndex = symbolTable.size();
		symbolTable.remove(lastIndex);
	}
	
	private static void updateSymbolTable(String key, Variable value) {
		int lastIndex = symbolTable.size();
		symbolTable.get(lastIndex).put(key, value);
	}
	
	private static Variable symbolTableGet(String key) {
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
	
	private static varType newVarType(String str) {
		switch (str) {
			case "boolean"	:	return varType.valueOf(varType.class, "BOOLEAN");
			case "string"	:	return varType.valueOf(varType.class, "STRING");
			case "list"		:	return varType.valueOf(varType.class, "LIST");
			case "tuple"	:	return varType.valueOf(varType.class, "TUPLE");
			case "dict"		:	return varType.valueOf(varType.class, "DICT");
			default			:	return varType.valueOf(varType.class, "NUMERIC");
		}
	}
}