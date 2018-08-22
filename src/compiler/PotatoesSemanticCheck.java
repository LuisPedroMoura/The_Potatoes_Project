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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import unitsGrammar.grammar.*;
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
	private static String UnitsFilePath;
	
	private	static Units							unitsFile;	// initialized in visitUsing();
	private	static List<String>						reservedWords;	// initialized in visitUsing();
	private static Map<String, Unit> 				unitsTable;		// initialized in visitUsing();
	private static PotatoesFunctionNames			functions;		// initialized in CTOR;
	private static Map<String, Function_IDContext>	functionNames;	// initialized in CTOR;
	private static Map<String, List<String>>		functionArgs;	// initialized in CTOR;

	protected static ParseTreeProperty<Variable> 		mapCtxVar		= new ParseTreeProperty<>();
	protected static List<HashMap<String, Variable>>	symbolTable 	= new ArrayList<>();
	
	protected static boolean visitedMain = false;
	protected static String currentReturn = null;
	
 	public PotatoesSemanticCheck(String PotatoesFilePath){
		functions = new PotatoesFunctionNames(PotatoesFilePath);
		functionNames = functions.getFunctions();
		functionArgs = functions.getFunctionsArgs();
		symbolTable.add(new HashMap<String, Variable>());
		if (debug) ErrorHandling.printInfo("The PotatoesFilePath is: " + PotatoesFilePath);
	}
	
	// --------------------------------------------------------------------------
	// Getters
	public static ParseTreeProperty<Variable> getmapCtxVar(){
		return mapCtxVar;
	}

	public static Units getUnitsFileInfo() {
		return unitsFile;
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
		// Get information from the units file
		UnitsFilePath = getStringText(ctx.STRING().getText());
		if (debug) ErrorHandling.printInfo(ctx, UnitsFilePath);
		unitsFile = new Units(UnitsFilePath);
		reservedWords = Units.getReservedWords();
		unitsTable = Units.getUnitsTable();

		// Debug
		if (debug) {
			ErrorHandling.printInfo(ctx, "Units File path is: " + UnitsFilePath);
			ErrorHandling.printInfo(ctx, unitsFile.toString());
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
	public Boolean visitStatement_InputOutput(Statement_InputOutputContext ctx) {
		return visit(ctx.inputOutput());
	}

	// --------------------------------------------------------------------------
	// Assignments
	
	@Override
	public Boolean visitAssignment_Var_Declaration_Expression(Assignment_Var_Declaration_ExpressionContext ctx) {
		if (!visit(ctx.varDeclaration()) || !visit(ctx.expression())) {
			return false;
		};
		
		Variable var = mapCtxVar.get(ctx.varDeclaration());
		Variable expr = mapCtxVar.get(ctx.expression());
		String varName = "";
		if (ctx.varDeclaration() instanceof VarDeclaration_VariableContext) {
			VarDeclaration_VariableContext decl = (VarDeclaration_VariableContext) ctx.varDeclaration();
			varName = decl.ID().getText();
		}
		if (ctx.varDeclaration() instanceof VarDeclaration_listContext) {
			VarDeclaration_listContext decl = (VarDeclaration_listContext) ctx.varDeclaration();
			varName = decl.ID(1).getText();
		}
		if (ctx.varDeclaration() instanceof VarDeclaration_dictContext) {
			VarDeclaration_dictContext decl = (VarDeclaration_dictContext) ctx.varDeclaration();
			varName = decl.ID(2).getText();
		}

		if (debug) {
			ErrorHandling.printInfo(ctx, "[ASSIGN_VARDEC_EXPR] Visited visitAssignment_Var_Declaration_Expression");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + varName + " with unit " + var.getUnit());
		}
		
		// Units are not compatible -> error
		else if (var.getVarType() != expr.getVarType()) {
			ErrorHandling.printError(ctx, "Units in assignment are not compatible");
			return false;
		}
		
		// units are numeric, may or may not be compatible -> verify
		if (var.isNumeric() && expr.isNumeric()) {
			
			expr = new Variable (expr); // deep copy
			
			// units are not compatible -> error
			try {
				expr.convertUnitTo(var.getUnit());
			}
			catch (IllegalArgumentException e) {
				ErrorHandling.printError(ctx, "Units in assignment are not compatible");
				return false;
			}
		}
		
		// units are compatible -> ok
		mapCtxVar.put(ctx, expr);
		updateSymbolTable(varName, expr);
		return true;
	}

	@Override
	public Boolean visitAssignment_Var_Expression(Assignment_Var_ExpressionContext ctx) {
		if (!visit(ctx.var()) || !visit(ctx.expression())) {
			return false;
		};
		
		Variable var = mapCtxVar.get(ctx.var());
		Variable expr = mapCtxVar.get(ctx.expression());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_OP] Visited visitAssignment_Var_Declaration_Expression");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + ctx.var().ID().getText() + " with unit " + var.getUnit());
		}
		
		// Units are not compatible -> error
		else if (var.getVarType() != expr.getVarType()) {
			ErrorHandling.printError(ctx, "Units in assignment are not compatible");
			return false;
		}
		
		// units are numeric, may or may not be compatible -> verify
		if (var.isNumeric() && expr.isNumeric()) {
			
			expr = new Variable (expr); // deep copy
			
			// units are not compatible -> error
			try {
				expr.convertUnitTo(var.getUnit());
			}
			catch (IllegalArgumentException e) {
				ErrorHandling.printError(ctx, "Units in assignment are not compatible");
				return false;
			}
		}
		
		// units are compatible -> ok
		mapCtxVar.put(ctx, expr);
		updateSymbolTable(ctx.var().ID().getText(), expr);
		return true;
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
		if (!visit(ctx.scope())) {
			return false;
		}

		mapCtxVar.put(ctx, mapCtxVar.get(ctx.scope()));
		
		return true;
	}

	@Override
	public Boolean visitFunctionReturn(FunctionReturnContext ctx) {
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Variable var = mapCtxVar.get(ctx.expression());
		
		if (var.isNumeric()) {
			
			if (unitsTable.containsKey(currentReturn)) {
				
				var = new Variable(var); // deep copy
				try {
					var.convertUnitTo(unitsTable.get(currentReturn));
					mapCtxVar.put(ctx, var);
					return true;
				}
				catch (IllegalArgumentException e) {
					ErrorHandling.printError(ctx, "Retturn unit is not compatible with fucntion signature");
					return false;
				}
			}
		}
		
		if(	var.isString() && currentReturn.equals("string") ||
			var.isBoolean() && currentReturn.equals("boolean") ||
			var.isList() && currentReturn.equals("list") ||
			var.isDict() && currentReturn.equals("dict")) {
			
			mapCtxVar.put(ctx, var);
			return true;
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
		
		// update currentReturn
		currentReturn = argsToUse.get(0);
		String cr = currentReturn;
		if (!unitsTable.containsKey(cr) && !cr.equals("string") && !cr.equals("boolean") && !cr.equals("list") && !cr.equals("dict")) {
			ErrorHandling.printError(ctx, "Function return unit is not a valid unit");
			return false;
		}
		
		// get list of arguments given in function call
		List<Variable> functionCallArgs = new ArrayList<>();
		for (ExpressionContext expr : ctx.expression()) {
			visit(expr);
			functionCallArgs.add(mapCtxVar.get(expr));
		}
				
		// if number of arguments do not match -> error
		if(argsToUse.size() != functionCallArgs.size()) {
			ErrorHandling.printError(ctx, "NUmber of arguments in function call do not match required arguments");
			return false;
		}
		
		// verify that all arguments units match function arguments
		for (int i = 0; i < functionCallArgs.size(); i++) {
			
			String toUseArg = argsToUse.get(i+1);
			Variable callArg = functionCallArgs.get(i);
			
			if (toUseArg.equals("string") && callArg.isString()) {
				continue;
			}
			else if (toUseArg.equals("boolean") && callArg.isBoolean()) {
				continue;
			}
			else if (toUseArg.equals("list") && callArg.isList()) {
				continue;
			}
			else if (toUseArg.equals("dict") && callArg.isDict()) {
				continue;
			}
			else if (callArg.isNumeric()) {
				String callArgUnitName = callArg.getUnit().getName();
				if (toUseArg.equals(callArgUnitName)) {
					continue;
				}
			}
			else {
				ErrorHandling.printError(ctx, "function call arguments are no compatible with function signature");
				return false;
			}
		}
		
		// open new scope
		openFunctionScope();
		
		// store new variables with function call value and function signature name
		for (int i = 0; i < functionCallArgs.size(); i++) {
			updateSymbolTable(argsToUse.get(i), functionCallArgs.get(i));
		}
		
		// visit the function with correct scope and arguments
		visit(functionToVisit);
		
		// update tables and visit function
		mapCtxVar.put(ctx, new Variable(null, varType.LIST, mapCtxVar.get(functionToVisit)));
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
		
		// condition is boolean -> ok
		Variable var = mapCtxVar.get(ctx.expression());
		if (!var.isBoolean()) {
			ErrorHandling.printError(ctx, "If condition must be boolean");
			return false;
		}

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
		
		// condition is boolean -> ok
		Variable var = mapCtxVar.get(ctx.expression());
		if (!var.isBoolean()) {
			ErrorHandling.printError(ctx, "If condition must be boolean");
			return false;
		}

		// visit all scope
		valid = valid && visit(ctx.scope());
		
		return valid;
	}

	@Override 
	public Boolean visitElseIfCondition(ElseIfConditionContext ctx) {
		Boolean valid = true;
		Boolean res = visit(ctx.expression());
		valid = valid && res;
		
		// condition is boolean -> ok
		Variable var = mapCtxVar.get(ctx.expression());
		if (!var.isBoolean()) {
			ErrorHandling.printError(ctx, "If condition must be boolean");
			return false;
		}

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
		
		if (ctx.functionReturn() != null) {
			valid = valid && visit(ctx.functionReturn());
			if (!valid) {
				return false;
			}
			mapCtxVar.put(ctx, mapCtxVar.get(ctx.functionReturn()));
			closeScope();
			return true;
		}
		
		mapCtxVar.put(ctx, null);
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
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		// expression units are list and numeric ('number') -> ok
		if (var0.isList() && var1.isNumeric()) {
			
			if (var1.getUnit().equals(unitsTable.get("number"))){
				
				ListVar listVar = (ListVar) var0.getValue();
				int index = ((Double) var1.getValue()).intValue();
				
				try {
					Variable get = listVar.getList().get(index);
					mapCtxVar.put(ctx, get);
					return true;
				}
				catch (IndexOutOfBoundsException e) {
					ErrorHandling.printError(ctx, "Index out of bounds");
					return false;
				}
			}
		}
		
		// other expression combinations -> error
		ErrorHandling.printError(ctx, "Bad operands for operator '[ ]'");
		return false;
			
	}
	
	@Override
	public Boolean visitExpression_ISEMPTY(Expression_ISEMPTYContext ctx) {
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Variable var = mapCtxVar.get(ctx.expression());
		boolean isEmpty;
		
		if (var.isList()) {
			
			ListVar listVar = (ListVar) var.getValue();
			isEmpty = listVar.getList().isEmpty();
		}
		
		else if (var.isDict()) {
			
			DictVar dictVar = (DictVar) var.getValue();
			isEmpty = dictVar.getDict().isEmpty();
		}
		
		else if (var.isString()) {
			
			String str = (String) var.getValue();
			isEmpty = str.isEmpty();
		}
		
		else {
			ErrorHandling.printError(ctx, "Bad operand units for operation 'isEmpty'");
			return false;
		}
		
		mapCtxVar.put(ctx, new Variable(null , varType.BOOLEAN, isEmpty));
		return true;
	}
	
	@Override
	public Boolean visitExpression_SIZE(Expression_SIZEContext ctx) {
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Variable var = mapCtxVar.get(ctx.expression());
		int size = 0;
		
		if (var.isList()) {
			
			ListVar listVar = (ListVar) var.getValue();
			size = listVar.getList().size();
		}
		
		else if (var.isDict()) {
			
			DictVar dictVar = (DictVar) var.getValue();
			size = dictVar.getDict().size();
		}
		
		else if (var.isString()) {
			
			String str = (String) var.getValue();
			size = str.length();
		}
		
		else {
			ErrorHandling.printError(ctx, "Bad operand units for operation 'size'");
			return false;
		}
		
		mapCtxVar.put(ctx, new Variable(unitsTable.get("number") , varType.NUMERIC, size));
		return true;
	}
	
	@Override
	public Boolean visitExpression_SORT(Expression_SORTContext ctx) {
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Variable var = mapCtxVar.get(ctx.expression());
		
		if (var.isList()) {
			
			ListVar listVar = (ListVar) var.getValue();
			mapCtxVar.put(ctx, new Variable(null, varType.LIST, listVar));
			return true;
		}
		
		if (var.isString()) {
			
			String str = (String) var.getValue();
			
			char[] chars = str.toCharArray();
			Arrays.sort(chars);
			str = new String(chars);
			
			mapCtxVar.put(ctx, new Variable(null, varType.STRING, str));
			return true;
		}
		
		ErrorHandling.printError(ctx, "Bad operand units for operator 'sort'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_KEYS(Expression_KEYSContext ctx) {
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Variable var = mapCtxVar.get(ctx.expression());
		
		if (var.isDict()) {
			
			DictVar dictVar = (DictVar) var.getValue();
			Object[] arr= dictVar.getDict().keySet().toArray();
			
			ListVar listVar = new ListVar(dictVar.getKeyType(), dictVar.isBlockedKey());
			
			List<Variable> list = listVar.getList();
			for (Object obj : arr) {
				list.add((Variable) obj);
			}
			
			mapCtxVar.put(ctx, new Variable(null, varType.LIST, listVar));
			return true;
		}
		
		ErrorHandling.printError(ctx, "Bad operand units for operator 'value'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_VALUES(Expression_VALUESContext ctx) {
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Variable var = mapCtxVar.get(ctx.expression());
		
		if (var.isDict()) {
			
			DictVar dictVar = (DictVar) var.getValue();
			Object[] arr= dictVar.getDict().values().toArray();
			
			ListVar listVar = new ListVar(dictVar.getValueType(), dictVar.isBlockedValue());
			
			List<Variable> list = listVar.getList();
			for (Object obj : arr) {
				list.add((Variable) obj);
			}
			
			mapCtxVar.put(ctx, new Variable(null, varType.LIST, listVar));
			return true;
		}
		
		ErrorHandling.printError(ctx, "Bad operand units for operator value");
		return false;
	}
	
	@Override
	public Boolean visitExpression_Cast(Expression_CastContext ctx) {
		if(!visitChildren(ctx)) {
			return false;
		}
		
		Variable var= mapCtxVar.get(ctx.expression());
		String castName = ctx.cast().ID().getText();
		
		// verify if cast is valid numeric unit
		if (!unitsTable.keySet().contains(castName)) {
			ErrorHandling.printError(ctx, "'" + castName + "' is not a valid Unit");
			return false;
		}
		
		// verify that variable to be casted is numeric
		if (var.isNumeric()) {
			
			var = new Variable(var); // deep copy
			try {
				var.convertUnitTo(unitsTable.get(castName));
			}
			catch (IllegalArgumentException e) {
				ErrorHandling.printError(ctx, "Units are not compatible, cast is not possible");
				return false;
			}
			
			mapCtxVar.put(ctx, var);
			return true;
		}
		
		ErrorHandling.printError(ctx, "Invalid operands for operartor cast");
		return false;
	}
	
	@Override
	public Boolean visitExpression_UnaryOperators(Expression_UnaryOperatorsContext ctx) {
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Variable var = mapCtxVar.get(ctx.expression());
		String op = ctx.op.getText();
		
		if (op.equals("-")) {
			
			if (var.isNumeric()) {
				if(debug) ErrorHandling.printInfo(ctx, "[OP_OP_SIMETRIC]");
				mapCtxVar.put(ctx, var); // don't need to calculate symmetric value to guarantee semantic correctness in future calculations
				return true;
			}
		}
		
		else if (op.equals("!")) {
			
			if (var.isBoolean()) {
				if (debug) ErrorHandling.printInfo(ctx, "[OP_LOGIC_OPERAND_NOT_VAR]");
				mapCtxVar.put(ctx, var); // don't need to calculate negated value to guarantee semantic correctness in future calculations
				return true;
			}
		}
		
		// other variable combinations
		ErrorHandling.printError(ctx, "Bad operand units for operator + '" + op + "'");
		return false;
	}
	
	@Override 
	public Boolean visitExpression_Power(Expression_PowerContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable base = mapCtxVar.get(ctx.expression(0));
		Variable pow = mapCtxVar.get(ctx.expression(1));
		
		if (base.isNumeric() && pow.isNumeric()) {
			
			if (pow.getUnit().equals(unitsTable.get("number"))) {
				
				base = new Variable(base); // deep copy
				
				Variable res = Variable.power(base, pow);
				
				mapCtxVar.put(ctx, res);
				
				if (debug) {
					ErrorHandling.printInfo(ctx, "[OP_POWER] Visited Expression Power");
					ErrorHandling.printInfo(ctx, "--- Powering Variable " + base + "with power " + pow + "and result is " + res);
				}
				return true;
			}
		}
			
		// other variable combinations
		ErrorHandling.printError(ctx, "Bad operand units for operator '^'");
		return false;
	}
	
	@Override 
	public Boolean visitExpression_Mult_Div_Mod(Expression_Mult_Div_ModContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		String op = ctx.op.getText();
	
		if (var0.isNumeric() && var1.isNumeric()) {
			
			var0 = new Variable(var0); // deep copy
			var1 = new Variable(var1); // deep copy
			
			Variable res = null;
			// Modulus
			if (op.equals("%")) {
				try {
					res = Variable.mod(var0, var1);
					return true;
				}
				catch (IllegalArgumentException e) {
					ErrorHandling.printError(ctx, "Right side of mod expression has to be of Unit Number!");
					return false;
				}
			}
			
			// Multiplication
			if (op.equals("*")) {
				res = Variable.multiply(var0, var1); 
				if (debug) { ErrorHandling.printInfo(ctx, "result of multiplication is Variable " + res);}
				return true;
			}
			
			// Division expression
			if (op.equals("/")) {
				try {
					res = Variable.divide(var0, var1); 
					if (debug) { ErrorHandling.printInfo(ctx, "result of division is Variable " + res);}
					return true;
				}
				catch (ArithmeticException e) {
					ErrorHandling.printError(ctx, "Cannot divide by zero");
				}
			}
			
			// update tables
			mapCtxVar.put(ctx, res);
		}
		
		// one operand is string and the other is numeric (unit number) -> ok (string expanded concatenation)
		else if (var0.isString() || var1.isString()) {
			
			// operator is '*' -> concatenation is possible
			if (op.equals("*")) {
			
				String str = "";
				int mult = 0;
				
				// variables are string || boolean || numeric, concatenation is possible -> ok
				if (var0.isNumeric() || var1.isNumeric()) {
	
					if (var0.isString()) {
						str = (String) var0.getValue();
					}
					
					if (var1.isString()) {
						str = (String) var1.getValue();
					}
					
					if (var0.isNumeric()) {
						if (var0.getUnit().equals(unitsTable.get("number"))) {
							mult = ((Double) var0.getValue()).intValue();
						}
					}
					
					if (var1.isNumeric()) {
						if (var1.getUnit().equals(unitsTable.get("number"))) {
							mult = ((Double) var1.getValue()).intValue();
						}
					}
					
					String finalStr = "";
					for (int i = 0; i < mult; i++) {
						finalStr += str;
					}
					
					// update tables
					mapCtxVar.put(ctx, new Variable (null, varType.STRING, finalStr));
					return true;
				}
			}
		}
			
		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] Visiting Expression Mult_Div_Mod");
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] op0: " + ctx.expression(0).getText());
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] op1: " + ctx.expression(1).getText());
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] variable a " + var0);
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] variable b " + var1);
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] op		 " + op + "\n");
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] temp: " + var0);
		}
		
		// other variable combinations
		ErrorHandling.printError(ctx, "Bad operand units for operator '" + op + "'");
		return false;
	}
	
	@Override 
	public Boolean visitExpression_Add_Sub(Expression_Add_SubContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		String op = ctx.op.getText();
		
		// both elements are numeric -> ok
		if (var0.isNumeric() && var1.isNumeric()) {
			
			var0 = new Variable(var0); // deep copy
			var1 = new Variable(var1); // deep copy
	
			if (debug) {
				ErrorHandling.printInfo(ctx, "[OP_ADDSUB] Visiting Expression Add_Sub");
				ErrorHandling.printInfo(ctx, "[OP_ADDSUB] variable a " + var1);
				ErrorHandling.printInfo(ctx, "[OP_ADDSUB] variable b " + var1 + "\n");
			}
	
			try {
				Variable res = Variable.add(var0, var1);
				if (debug) { ErrorHandling.printInfo(ctx, "result of sum is Variable " + res);}
				mapCtxVar.put(ctx, res);
				return true;
			}
			catch (IllegalArgumentException e) {
				ErrorHandling.printError(ctx, "Incompatible units");
				return false;
			}
		}
		
		// one operand is string and the other is string || boolean || numeric -> ok (string concatenation)
		else if (var0.isString() || var1.isString()) {
			
			// operator is '+' -> concatenation is possible
			if (op.equals("+")) {
			
				String str0 = "";
				String str1 = "";
				
				// variables are string || boolean || numeric, concatenation is possible -> ok
				if ((var0.isString() || var0.isBoolean() || var0.isNumeric()) && (var1.isString() || var1.isBoolean() || var1.isNumeric())) {
	
					if (var0.isString()) {
						str0 = (String) var0.getValue();
					}
					if (var0.isBoolean()) {
						str0 = ((Boolean) var0.getValue())+""; 
					}
					if (var0.isNumeric()) {
						str0 = ((Double) var0.getValue()) + var0.getUnit().getSymbol();
					}
					
					if (var1.isString()) {
						str1 = (String) var1.getValue();
					}
					if (var1.isBoolean()) {
						str1 = ((Boolean) var1.getValue())+""; 
					}
					if (var1.isNumeric()) {
						str1 = ((Double) var1.getValue()) + var1.getUnit().getSymbol();
					}
					
					String finalStr = str0 + str1;
					mapCtxVar.put(ctx, new Variable (null, varType.STRING, finalStr));
					return true;
				}
			}
		}
		
		// other variable combinations
		ErrorHandling.printError(ctx, "Bad operand units for operator '" + op + "'");
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
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		if (var0.isNumeric() && var1.isNumeric()) {
			
			var0 = new Variable(var0); // deep copy
			
			try {
				var0.convertUnitTo(var1.getUnit());
				mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, true));
				return true;
			}
			catch (IllegalArgumentException e) {
				// do nothing
			}
		}
		
		if (var0.isString() && var1.isString()) {
			mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, true));
			return true;
		}

		if (debug) {
			ErrorHandling.printInfo(ctx, "THIS IS A : " + var0);
			ErrorHandling.printInfo(ctx, "THIS IS B : " + var1);
		}
			
		ErrorHandling.printError(ctx, "Units to be compared are not compatible");
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
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		if (var0.isNumeric() && var1.isNumeric()) {
			
			var0 = new Variable(var0); // deep copy
			
			try {
				var0.convertUnitTo(var1.getUnit());
				mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, true));
				return true;
			}
			catch (IllegalArgumentException e) {
				// do nothing
			}
		}
		
		else if (var0.getVarType() == var1.getVarType()) {
			mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, true));
			return true;
		}

		if (debug) {
			ErrorHandling.printInfo(ctx, "THIS IS A : " + var0);
			ErrorHandling.printInfo(ctx, "THIS IS B : " + var1);
		}
			
		ErrorHandling.printError(ctx, "Units to be compared are not compatible");
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
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		if (var0.isBoolean() && var1.isBoolean()) {
			mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, true));
			return true;
		}
		
		ErrorHandling.printError(ctx, "bad operand units for logical operator '" + ctx.op.getText() + "'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_tuple(Expression_tupleContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		DictTuple tuple = new DictTuple(var0, var1);
		
		mapCtxVar.put(ctx, new Variable(null, varType.TUPLE, tuple));
		return true;
	}
	
	@Override
	public Boolean visitExpression_ADD(Expression_ADDContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		// left expression is unit list -> verify
		if (var0.isList()) {
			
			ListVar listVar = (ListVar) var0.getValue();
			String valueUnit = listVar.getType();
			
			// list accepts compatible value units -> verify
			if (!listVar.isBlocked()) {
				
				var1 = new Variable(var1); // deep copy
				
				// list value unit and expression unit are not compatible -> error
				try {
					var1.convertUnitTo(unitsTable.get(valueUnit));
				}
				catch (IllegalArgumentException e) {
					ErrorHandling.printError(ctx, "Bad operand. Unit '" + valueUnit + "' is not compatible with '" + var1.getUnit().getName() + "'");
					return false;
				}
				// list value unit and expression unit are compatible -> ok (jumps to next code)
			}
			
			// list value unit is blocked to specific unit -> verify
			boolean added = listVar.getList().add(var1);
			mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, added));
			return true;
		}
		
		// expression to search key on is dict -> verify
		else if (var0.isDict()) {
			
			DictVar dictVar = (DictVar) var0.getValue();
			String keyUnit = dictVar.getKeyType();
			String valueUnit = dictVar.getValueType();
			
			Variable tupleKey = ((DictTuple) var1.getValue()).getKey();
			Variable tupleValue = ((DictTuple) var1.getValue()).getValue();
			
			// variable to be added is tuple -> verify
			if (var1.isTuple()) {
				
				if (!dictVar.isBlockedKey()) {
					
					tupleKey = new Variable(tupleKey); // deep copy
					
					// dict key unit and expression unit are not compatible -> error
					try {
						tupleKey.convertUnitTo(unitsTable.get(keyUnit));
					}
					catch (IllegalArgumentException e){
						ErrorHandling.printError(ctx, "Bad operand. Key unit is not compatible with dictionary parameterized key unit");
						return false;
					}
				}
				
				if (!dictVar.isBlockedValue()) {
					
					tupleValue = new Variable(tupleValue); // deep copy
					
					// dict key unit and expression unit are not compatible -> error
					try {
						tupleValue.convertUnitTo(unitsTable.get(valueUnit));
					}
					catch (IllegalArgumentException e){
						ErrorHandling.printError(ctx, "Bad operand. Value unit is not compatible with dictionary parameterized value unit");
						return false;
					}
				}
				
				Variable previous = dictVar.getDict().put(tupleKey, tupleValue); // previous can be null, but I think the code will not allow it anywhere
				mapCtxVar.put(ctx, previous);
				return true;
			}
		}
		
		// expression to add have string printing capacity -> verify
		else if (var0.isString() || var1.isString()) {
			
			String str0 = "";
			String str1 = "";
			
			// variables are string || boolean || numeric, concatenation is possible -> ok
			if ((var0.isString() || var0.isBoolean() || var0.isNumeric()) && (var1.isString() || var1.isBoolean() || var1.isNumeric())) {

				if (var0.isString() || var0.isBoolean()) {
					str0 = (String) var0.getValue();
				}
				
				if (var1.isString() || var1.isBoolean()) {
					str1 = (String) var1.getValue();
				}
				
				if (var0.isNumeric()) {
					str0 = ((String) var0.getValue()) + var0.getUnit().getSymbol();
				}
				
				if (var1.isNumeric()) {
					str1 = ((String) var1.getValue()) + var1.getUnit().getSymbol();
				}
				
				String finalStr = str0 + str1;
				mapCtxVar.put(ctx, new Variable (null, varType.STRING, finalStr));
				return true;
			}
		}
		
		// variable to be added is not tuple -> error
		ErrorHandling.printError(ctx, "Bad operand units for operator 'add'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_REM(Expression_REMContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		// expression to search index on is unit list -> verify
		if (var0.isList()) {
			
			ListVar listVar = (ListVar) var0.getValue();
			
			// expression unit is 'number' -> verify
			if (var1.isNumeric()) {
					
				if (var1.getUnit().equals(unitsTable.get("number"))) {
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
			String keyUnit = dictVar.getKeyType();
			
			// dict accepts compatible key units -> verify
			if (!dictVar.isBlockedKey()) {
				
				var1 = new Variable(var1); // deep copy
				
				// dict key unit and expression unit are not compatible -> error
				try {
					var1.convertUnitTo(unitsTable.get(keyUnit));
				}
				catch (IllegalArgumentException e) {
					ErrorHandling.printError(ctx, "Bad operand. Unit '" + keyUnit + "' is not compatible with '" + var1.getUnit().getName() + "'");
					return false;
				}
				// dict key unit and expression unit are compatible -> ok (jumps to next code
				
			}
			
			// dict key unit is blocked to specific unit -> verify
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
		
		// expression to search on has unit string (removeAll)-> verify
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
				
				if (var1.getUnit().equals(unitsTable.get("number"))) {
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
			String keyUnit = dictVar.getKeyType();
			
			// dict accepts compatible key units -> verify
			if (!dictVar.isBlockedKey()) {
				
				var1 = new Variable(var1); // deep copy
				
				// dict key unit and expression unit are not compatible -> error
				try {
					var1.convertUnitTo(unitsTable.get(keyUnit));
				}
				catch (IllegalArgumentException e) {
					ErrorHandling.printError(ctx, "Bad operand. Unit '" + keyUnit + "' is not compatible with '" + var1.getUnit().getName() + "'");
					return false;
				}
				// dict key unit and expression unit are compatible -> ok (jumps to next code)
			}
			
			// dict does not accept compatible key units -> verify
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
		ErrorHandling.printError(ctx, "Bad operand units for operator 'get'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_CONTAINS(Expression_CONTAINSContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		// expression to search value on has unit list -> ok
		if (var0.isList()) {
			
			ListVar listVar = (ListVar) var0.getValue();
			String valueUnit = listVar.getType();
			
			// list accepts compatible value units -> verify
			if (!listVar.isBlocked()) {
				
				var1 = new Variable(var1); // deep copy
				
				// list value unit and expression unit are not compatible -> error
				try {
					var1.convertUnitTo(unitsTable.get(valueUnit));
				}
				catch (IllegalArgumentException e) {
					ErrorHandling.printError(ctx, "Bad operand. Unit '" + valueUnit + "' is not compatible with '" + var1.getUnit().getName() + "'");
					return false;
				}
				// list value unit and expression unit are compatible -> ok (jumps to next code
			}
			
			// list value unit is blocked to specific unit -> ok
			boolean contains = listVar.getList().contains(var1);
			mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, contains));
			return true;
		}
		
		// expression to search value on has unit string -> verify
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
		ErrorHandling.printError(ctx, "Bad operand units for operator 'contains'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_CONTAINSKEY(Expression_CONTAINSKEYContext ctx) {
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = mapCtxVar.get(ctx.expression(0));
		Variable var1 = mapCtxVar.get(ctx.expression(1));
		
		// expression to search value on has unit dict -> ok
		
		if (var0.isDict()) {
			
			DictVar dictVar = (DictVar) var0.getValue();
			String keyUnit = dictVar.getValueType();
			
			// dict accepts compatible key units -> verify
			if (!dictVar.isBlockedValue()) {
				
				var1 = new Variable(var1); // deep copy
				
				// dict key unit and expression unit are not compatible -> error
				try {
					var1.convertUnitTo(unitsTable.get(keyUnit));
				}
				catch (IllegalArgumentException e) {
					ErrorHandling.printError(ctx, "Bad operand. Unit '" + keyUnit + "' is not compatible with '" + var1.getUnit().getName() + "'");
					return false;
				}
				// dict key unit and expression unit are compatible -> ok (jumps to next code)
			}
			
			// dict key unit is blocked to specific unit -> ok
			boolean contains = dictVar.getDict().containsKey(var1);
			mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, contains));
			return true;
		}
		
		// either left expression is not dict or right expression is not boolean || string || numeric -> error
		ErrorHandling.printError(ctx, "Bad operand units for operator 'containsKey'");
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
			String valueUnit = dict.getValueType();
			
			// dict accepts compatible value units -> verify
			if (!dict.isBlockedValue()) {
				
				var1 = new Variable(var1); // deep copy
				
				// dict value unit and expression unit are not compatible -> error
				try {
					var1.convertUnitTo(unitsTable.get(valueUnit));
				}
				catch (IllegalArgumentException e) {
					ErrorHandling.printError(ctx, "Bad operand. Unit '" + valueUnit + "' is not compatible with '" + var1.getUnit().getName() + "'");
					return false;
				}
				// dict value unit and expression unit are compatible -> ok (jumps to next code
			}
			
			// dict value unit is blocked to specific unit -> ok
			boolean contains = dict.getDict().containsValue(var1);
			mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, contains));
			return true;
		}
		
		// either left expression is not dict or right expression is not boolean || string || numeric -> error
		ErrorHandling.printError(ctx, "Bad operand units for operator 'containsValue'");
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
			String listUnit = listVar.getType();
			int index;
			
			// dict accepts compatible value units -> verify
			if (!listVar.isBlocked()) {
				
				var1 = new Variable(var1); // deep copy
			
				// dict value unit and expression unit are not compatible -> error
				try {
					var1.convertUnitTo(unitsTable.get(listUnit));
				}
				catch (IllegalArgumentException e) {
					ErrorHandling.printError(ctx, "Bad operand. List has parameterized unit '" + listUnit +
							"' (accepts compatible? -> " + listVar.isBlocked() + ")");
					return false;
				}
				// list unit and expression unit are different -> error
			}
			
			index = listVar.getList().indexOf(var1);
			mapCtxVar.put(ctx, new Variable(unitsTable.get("number"), varType.NUMERIC, (double) index));
			return true;
		}
		
		// expression to search index on has unit string -> verify
		else if (var0.isString()) {
			
			// expression to be searched is string -> ok
			if (var1.isString()) {
				String str = (String) var0.getValue();
				String subStr = (String) var1.getValue();
				int index = str.indexOf(subStr);
				mapCtxVar.put(ctx, new Variable(unitsTable.get("number"), varType.NUMERIC, (double) index));
				return true;
			}	
		}
		
		// expression to search index on is not list or string -> error
		ErrorHandling.printError(ctx, "Bad operand units for operator 'indexof'");
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
	public Boolean visitInputOutput(InputOutputContext ctx) {
		return visitChildren(ctx);
	}
	
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
		
		Variable unit = mapCtxVar.get(ctx.type());
		String newVarName = ctx.ID().getText();
		
		// variable to be created is already declared or is reserved word -> error
		if(!isValidNewVariableName(newVarName, ctx)) {return false;}
				
		// update tables -> unit already contains information necessary to create variable
		mapCtxVar.put(ctx, unit);
		updateSymbolTable(newVarName, unit);
		
		return true;
	}

	@Override
	public Boolean visitVarDeclaration_list(VarDeclaration_listContext ctx) {
		
		String listParamName = ctx.ID(0).getText();
		varType listParam = newVarUnit(listParamName);
		String newVarName = ctx.ID(1).getText();
		
		// new variable is already declared -> error
		if(symbolTableContains(newVarName)) {
			ErrorHandling.printError(ctx, "Variable '" + newVarName + "' is already declared");
			return false;
		}
		

		// list parameterized unit is string || boolean || numeric -> ok
		if (listParam.isString() || listParam.isBoolean() || (listParam.isNumeric() && unitsTable.containsKey(listParamName))) {
			// verify if list unit is blocked or accepts compatible units
			boolean blockedUnit = false;
			if (ctx.block == null) {
				blockedUnit = true;
			}
			ListVar listVar = new ListVar(listParamName, blockedUnit);
			Variable list = new Variable(null, varType.LIST, listVar);
			
			// update tables
			mapCtxVar.put(ctx, list);
			updateSymbolTable(newVarName, list);
			return true;
		}
		
		// other list units -> error
		ErrorHandling.printError(ctx, "Incorrect argument for list parameter");
		return false;
	}

	@Override
	public Boolean visitVarDeclaration_dict(VarDeclaration_dictContext ctx) {

		String keyUnitName = ctx.ID(0).getText();
		String valueUnitName = ctx.ID(1).getText();
		varType keyUnit = newVarUnit(keyUnitName);
		varType valueUnit = newVarUnit(valueUnitName);
		String newVarName = ctx.ID(1).getText();
		
		// new variable is already declared -> error
		if(symbolTableContains(newVarName)) {
			ErrorHandling.printError(ctx, "Variable '" + newVarName + "' is already declared");
			return false;
		}
		
		// dict key unit and value unit are string || boolean || numeric -> ok
		if (keyUnit.isString() || keyUnit.isBoolean() || (keyUnit.isNumeric() && unitsTable.containsKey(keyUnitName))) {
			if (valueUnit.isString() || valueUnit.isBoolean() || (valueUnit.isNumeric() && unitsTable.containsKey(valueUnitName))) {
				
				// verify if dict key unit and value unit are blocked or accept compatible units
				boolean blockKeyUnit = false;
				if (ctx.block0 == null) {
					blockKeyUnit = true;
				}
				boolean blockValUnit = false;
				if (ctx.block0 == null) {
					blockValUnit = true;
				}
				
				DictVar dictVar = new DictVar(keyUnitName, blockKeyUnit, valueUnitName, blockValUnit);
				Variable dict = new Variable(null, varType.DICT, dictVar);
				
				// update tables
				mapCtxVar.put(ctx, dict);
				updateSymbolTable(newVarName, dict);
				return true;
			}
		}
		
		// other dict units -> error
		ErrorHandling.printError(ctx, "Incorrect arguments for dict unit");
		return false;

	}
	
	// --------------------------------------------------------------------------
	// Units

	@Override 
	public Boolean visitType_Number_Type(Type_Number_TypeContext ctx) {
		Variable var = new Variable (unitsTable.get("number"), varType.NUMERIC, 0.0);
		mapCtxVar.put(ctx, var);
		return true;
	}

	@Override 
	public Boolean visitType_Boolean_Type(Type_Boolean_TypeContext ctx) {
		Variable var = new Variable (null, varType.BOOLEAN, false);
		mapCtxVar.put(ctx, var);
		return true;
	}

	@Override 
	public Boolean visitType_String_Type(Type_String_TypeContext ctx) {
		Variable var = new Variable (null, varType.STRING, "");
		mapCtxVar.put(ctx, var);
		return true;
	}
	
	@Override
	public Boolean visitType_Void_Type(Type_Void_TypeContext ctx) {
		Variable var = new Variable (null, varType.VOID, "");
		mapCtxVar.put(ctx, var);
		return true;
	}

	@Override 
	public Boolean visitType_ID_Type(Type_ID_TypeContext ctx) {
		String unitName = ctx.ID().getText();
		// unit exists -> ok
		if (unitsTable.containsKey(unitName)) {
			Variable var = new Variable (unitsTable.get(unitName), varType.NUMERIC, 0.0);
			mapCtxVar.put(ctx, var);
			return true;
		}
		// unit is not declared in units file -> error
		ErrorHandling.printError(ctx, "Invalid unit. Unit '" + unitName + "' is not declared");
		return false;
	}
	
	@Override
	public Boolean visitValue_Number(Value_NumberContext ctx) {
		try {
			Variable var = new Variable(unitsTable.get("number"), varType.NUMERIC, Double.parseDouble(ctx.NUMBER().getText()));
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
		Variable var = new Variable(null, varType.BOOLEAN, Boolean.parseBoolean(ctx.BOOLEAN().getText()));
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
		// cast unit exists -> ok
		if (unitsTable.containsKey(castName)){
			Variable var = new Variable(Units.instanceOf(castName), null, null);
			mapCtxVar.put(ctx, var);
			return true;
		}
		// cast unit does not exist -> error
		ErrorHandling.printError(ctx, "Invalid cast Unit. Unit '" + castName + "' does not exist");
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
	
	/**
	 * closes current scope exposing previous scope
	 */
	private static void closeScope() {
		int lastIndex = symbolTable.size()-1;
		symbolTable.remove(lastIndex);
	}
	
	/**
	 * Interface to update a new pair of key value to symbolTable in the correct scope
	 * @param key
	 * @param value
	 */
	private static void updateSymbolTable(String key, Variable value) {
		int lastIndex = symbolTable.size()-1;
		symbolTable.get(lastIndex).put(key, value);
	}
	
	/**
	 * Interface to get value from symbolTable in the correct scope
	 * @param key
	 * @return
	 */
	private static Variable symbolTableGet(String key) {
		int lastIndex = symbolTable.size()-1;
		return symbolTable.get(lastIndex).get(key);
	}
	
	/**
	 * Interface to get key using value from symbolTable in the correct scope
	 * @param var
	 * @return
	 */
	private static String symbolTableGetKeyByValue(Variable var) {
		int lastIndex = symbolTable.size()-1;
		Set<Entry<String, Variable>> entries = symbolTable.get(lastIndex).entrySet();
		for (Entry<String, Variable> en : entries) {
			if (en.getValue().equals(var)) {
				return en.getKey();
			}
		}
		return "";
	}
	
	/**
	 * Interface to verify if symbolTable contains value in the correct scope
	 * @param key
	 * @return
	 */
	private static boolean symbolTableContains(String key) {
		int lastIndex = symbolTable.size()-1;
		return symbolTable.get(lastIndex).containsKey(key);
	}
	
	/**
	 * Used in variable declarations to verify that the new name is a Valid new name
	 * @param varName
	 * @param ctx
	 * @return
	 */
	private static boolean isValidNewVariableName(String varName, ParserRuleContext ctx) {

		if (symbolTableContains(varName)) {
			ErrorHandling.printError(ctx, "Variable \"" + varName +"\" is already declared VALID");
			return false;
		}
		
		if (reservedWords.contains(varName)) {
			ErrorHandling.printError(ctx, varName +"\" is a reserved word");
			return false;
		}
		
		return true;
	}
	
	/**
	 * trims the quotes of a lexer string
	 * @param str
	 * @return
	 */
	private static String getStringText(String str) {
		str = str.substring(1, str.length() -1);
		if (debug) ErrorHandling.printError("removed quotes from string - " + str);
		return str;
	}
	
	/**
	 * Creates new varType enum using the equivalent units names from Potatoes Language
	 * @param str
	 * @return
	 */
	private static varType newVarUnit(String str) {
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