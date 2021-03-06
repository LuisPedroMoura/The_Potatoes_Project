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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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
 */
public class PotatoesSemanticCheck extends PotatoesBaseVisitor<Boolean>  {

	// Static Constant (Debug Only)
	private static final boolean debug = false;

	// --------------------------------------------------------------------------
	// Static Fields
	private static String UnitsFilePath;
	private static String PotatoesFilePath;
	
	private	static Units							unitsFile;		// initialized in visitUsing();
	private static PotatoesFunctionNames			functions;		// initialized in CTOR;
	private static Map<String, FunctionIDContext>	functionNames;	// initialized in CTOR;
	private static Map<String, List<String>>		functionArgs;	// initialized in CTOR;

	protected static ParseTreeProperty<Variable> 		mapCtxVar		= new ParseTreeProperty<>();
	protected static ParseTreeProperty<Variable> 		mapCtxListDict	= new ParseTreeProperty<>();
	protected static List<HashMap<String, Variable>>	symbolTable 	= new ArrayList<>();
	
	protected static boolean visitedMain = false;
	protected static String currentReturn = null;
	
 	public PotatoesSemanticCheck(String PotatoesFilePath){
 		PotatoesSemanticCheck.PotatoesFilePath = PotatoesFilePath;
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
	
	public static ParseTreeProperty<Variable> getmapCtxListDict(){
		return mapCtxListDict;
	}

	public static Units getUnitsFileInfo() {
		return unitsFile;
	}
	
	public static Map<String, FunctionIDContext> getFunctionNames() {
		return functionNames;
	}
	
	// --------------------------------------------------------------------------
	// Main Rules 	

	@Override 
	public Boolean visitProgram(ProgramContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->PROGRAM\n");
		
		Boolean valid = visit(ctx.using());
		List<GlobalStatementContext> globalStatementsInstructions = ctx.globalStatement();

		// Visit all globalStatement Declarations
		for (GlobalStatementContext c : globalStatementsInstructions) {
			if (c instanceof GlobalStatement_DeclarationContext) {
				Boolean res = visit(c);
				valid = valid && res;
			}
		}
		
		// Visit all globalStatement Assignments
		for (GlobalStatementContext c : globalStatementsInstructions) {
			if (c instanceof GlobalStatement_AssignmentContext) {
				Boolean res = visit(c);
				valid = valid && res;
			}
		}
		
		boolean mainExists = false;
		// Visit all globalStatement Function Main if exists
		for (GlobalStatementContext c : globalStatementsInstructions) {
			if (c instanceof GlobalStatement_FunctionMainContext) {
				mainExists = true;
				Boolean res = visit(c);
				valid = valid && res;
			}
		}
		
		// Visit all globalStatement Functions if Main does NOT exist
		if (!mainExists) {
			for (GlobalStatementContext c : globalStatementsInstructions) {
				if (c instanceof GlobalStatement_FunctionIDContext) {
					Boolean res = visit(c);
					valid = valid && res;
				}
			}
		}
		
		if(debug) ci();
		
		return valid;
	}

	@Override 
	public Boolean visitUsing(UsingContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->USING");

		// Get information from the units file
		UnitsFilePath = getStringText(ctx.STRING().getText());
		
		UnitsFilePath = PotatoesFilePath.substring(0, PotatoesFilePath.lastIndexOf("/")+1) + UnitsFilePath;
		
		if (debug) { ErrorHandling.printInfo(ctx, "UnitsFilePath is : " + UnitsFilePath);}
		unitsFile = new Units(UnitsFilePath);

		if (debug) {
			ErrorHandling.printInfo(ctx, "Units File path is: " + UnitsFilePath);
			//ErrorHandling.printInfo(ctx, unitsFile.toString());
			ci();
		}
		
		return true;
	}

	@Override
	public Boolean visitGlobalStatement_Declaration(GlobalStatement_DeclarationContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->GLOBAL STATEMENT - DECLARATION");
		
		boolean valid = visit(ctx.varDeclaration()); 
		
		if (debug) ci();
		
		return valid;
	}

	@Override 
	public Boolean visitGlobalStatement_Assignment(GlobalStatement_AssignmentContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->GLOBAL STATEMENT - ASSIGNMENT");
		
		
		boolean valid = false;
		if (ctx.assignment() instanceof Assignment_Var_Declaration_ExpressionContext) {
			valid =  visit(ctx.assignment());
		}
		else {
			ErrorHandling.printError(ctx, "No re-assignments allowed in global Scope");
		}
		
		if(debug) ci();
		
		return valid;
	}
	
	@Override
	public Boolean visitGlobalStatement_FunctionMain(GlobalStatement_FunctionMainContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->GLOBAL STATEMENT - FUNCTION MAIN");
		
		boolean valid = visitChildren(ctx);
		
		if (debug) ci();
		
		return valid;
	}
	
	@Override
	public Boolean visitGlobalStatement_FunctionID(GlobalStatement_FunctionIDContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->GLOBAL STATEMENT - FUNCTION ID");
		
		boolean valid = visitChildren(ctx);
		
		if (debug) ci();
		
		return valid;
	}

	// --------------------------------------------------------------------------
	// Statements 

	@Override 
	public Boolean visitStatement_Declaration(Statement_DeclarationContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->STATEMENT - DECLARATION");
		
		boolean valid = visit(ctx.varDeclaration());
		
		if (debug) ci();
		
		return valid;
	}

	@Override 
	public Boolean visitStatement_Assignment(Statement_AssignmentContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->STATEMENT - ASSIGNMENT");
		
		boolean valid =  visit(ctx.assignment());
		
		if(debug) ci();
		
		return valid;
	}

	@Override 
	public Boolean visitStatement_Control_Flow_Statement(Statement_Control_Flow_StatementContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->STATEMENT - CONTROL FLOW STATEMENT");
		
		boolean valid = visitChildren(ctx);
		
		if (debug) ci();
		
		return valid;
	}

	@Override 
	public Boolean visitStatement_FunctionCall(Statement_FunctionCallContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->STATEMENT - FUNCTION CALL");
		
		boolean valid =  visit(ctx.functionCall());
		
		if (debug) ci();
		
		return valid;
	}

	@Override 
	public Boolean visitStatement_InputOutput(Statement_InputOutputContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->STATEMENT - INPUT OUTPUT");
		
		boolean valid = visit(ctx.inputOutput());
		
		if (debug) ci();
		
		return valid;
	}
	
	@Override
	public Boolean visitStatement_Expression(Statement_ExpressionContext ctx) {
	
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->STATEMENT - EXPRESSION");
		
		boolean valid = false;
		
		if (ctx.expression() instanceof Expression_SORTContext ||
			ctx.expression() instanceof Expression_ADDContext ||
			ctx.expression() instanceof Expression_REMContext) {
				valid = visit(ctx.expression());
			}
		
		if (debug) ci();
		
		return valid;
		
	}
	

	// --------------------------------------------------------------------------
	// Assignments
	

	@Override
	public Boolean visitAssignment_Var_Declaration_Expression(Assignment_Var_Declaration_ExpressionContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->ASSIGNMENT - VAR DECLARATION - EXPRESSION");
		
		if (!visit(ctx.varDeclaration()) || !visit(ctx.expression())) {
			return false;
		};
		
		Variable var = new Variable(mapCtxVar.get(ctx.varDeclaration()));
		Variable expr = new Variable(mapCtxVar.get(ctx.expression()));
		String varName = ctx.varDeclaration().ID().getText();
		
		if (expr.getVarType() == varType.VOID) {
			ErrorHandling.printError(ctx, "expression value is null, cannot be assigned");
			return false;
		}
		
		// Types are not compatible -> error
		else if (var.getVarType() != expr.getVarType()) {
			ErrorHandling.printError(ctx, "Units in assignment are not compatible");
			return false;
		}
		
		// types are list, may or may not be compatible -> verify
		if (var.isList()) {
			
			if(!((ListVar) var.getValue()).getType().equals(((ListVar) expr.getValue()).getType())) {
				ErrorHandling.printError(ctx, "Lists values in assignment are not compatible");
				return false;
			}
		}
		
		// types are dict, may or may not be compatible -> verify
		else if (var.isDict()) {
			
			if(!((DictVar) var.getValue()).getKeyType().equals(((DictVar) expr.getValue()).getKeyType())) {
				ErrorHandling.printError(ctx, "Dict keys in assignment are not compatible");
				return false;
			}
			if(!((DictVar) var.getValue()).getValueType().equals(((DictVar) expr.getValue()).getValueType())) {
				ErrorHandling.printError(ctx, "Dict values in assignment are not compatible");
				return false;
			}
		}
		
		// types are numeric, may or may not be compatible -> verify
		else if (var.isNumeric()) {
			
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
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> varDeclaration : varName = " + varName + ", var = " + var);
			ErrorHandling.printInfo(ctx,indent + "-> expression = " + expr);
			ci();
		}
		
		return true;
	}

	@Override
	public Boolean visitAssignment_Var_Expression(Assignment_Var_ExpressionContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->ASSIGNMENT - VAR - EXPRESSION");
		
		if (!visit(ctx.var()) || !visit(ctx.expression())) {
			return false;
		};
		
		Variable var = new Variable(mapCtxVar.get(ctx.var()));
		Variable expr = new Variable(mapCtxVar.get(ctx.expression()));
		
		// Units are not compatible -> error
		if (var.getVarType() != expr.getVarType()) {
			ErrorHandling.printError(ctx, "Units in assignment are not compatible");
			return false;
		}
		
		if (expr.getVarType() == varType.VOID) {
			ErrorHandling.printError(ctx, "expression value is null, cannot be assigned");
			return false;
		}
		
		// units are numeric, may or may not be compatible -> verify
		if (var.isNumeric() && expr.isNumeric()) {
			
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
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> varDeclaration : varName = " + ctx.var().getText() + ", var = " + var);
			ErrorHandling.printInfo(ctx,indent + "-> expression = " + expr);
			ci();
		}
		
		return true;
	}
	
	// --------------------------------------------------------------------------
	// Functions
	
	@Override
	public Boolean visitFunctionMain(FunctionMainContext ctx) {

		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->FUNCTION MAIN");
		
		if (visitedMain == true) {
			ErrorHandling.printError(ctx, "Only one main function is allowed");
			return false;
		}
		
		visitedMain = true;
		openFunctionScope();
		boolean valid = visit(ctx.scope());
		
		if (debug) ci();
		
		return valid;
	}
	
	@Override
	public Boolean visitFunctionID(FunctionIDContext ctx) {

		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->ASSIGNMENT - FUNCTION ID");
		
		if (!visit(ctx.scope())) {
			return false;
		}

		mapCtxVar.put(ctx, new Variable(mapCtxVar.get(ctx.scope())));
		
		if (debug) ci();
		
		return true;
	}

	@Override
	public Boolean visitFunctionReturn(FunctionReturnContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->FUNCTION RETURN");
		
		if (ctx.expression() == null) {
			if (currentReturn == null) {
				return true;
			}
			ErrorHandling.printError(ctx, "return is not compatible with function signature");
			return false;
		}
		
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Variable var = new Variable(mapCtxVar.get(ctx.expression()));
		
		
		if (var.isNumeric()) {
			
			if (Units.exists(currentReturn)) {
				
				try {
					var.convertUnitTo(Units.instanceOf(currentReturn));
					// jumps to the end
				}
				catch (IllegalArgumentException e) {
					ErrorHandling.printError(ctx, "Retturn unit is not compatible with fucntion signature");
					return false;
				}
			}
		}
		
		else if(!(var.isString() && currentReturn.equals("string")) &&
			!(var.isBoolean() && currentReturn.equals("boolean")) &&
			!(var.isList() && currentReturn.equals("list")) &&
			!(var.isDict() && currentReturn.equals("dict"))) {
			
			ErrorHandling.printError(ctx, "return is not compatible with function signature");
			return false;
		}
		
		mapCtxVar.put(ctx, var);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> expressionn : return var = " + var);
			ErrorHandling.printInfo(ctx,indent +  "currentReturn is: " + currentReturn);
			ci();
		}
		
		return true;
		
	}
	
	@Override
	public Boolean visitFunctionCall(FunctionCallContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->FUNCTION CALL");
		
		Boolean valid = true;
		for (ExpressionContext expr : ctx.expression()) {
			valid = valid && visit(expr);
		}
		if(!valid) {
			return false;
		}
		
		// get function context to be visited and args needed from list of functions	
		FunctionIDContext functionToVisit = functionNames.get(ctx.ID().getText());
		List<String> argsToUse	= functionArgs.get(ctx.ID().getText());
		
		// update currentReturn
		currentReturn = functionToVisit.type(0).getText();
		String cr = currentReturn;
		if (!Units.exists(cr) && !cr.equals("string") && !cr.equals("boolean") && !cr.equals("list") && !cr.equals("dict") && !cr.equals("void")) {
			ErrorHandling.printError(ctx, "Function return unit is not a valid unit");
			return false;
		}
		
		// get list of arguments given in function call
		List<Variable> functionCallArgs = new ArrayList<>();
		for (ExpressionContext expr : ctx.expression()) {
			visit(expr);
			functionCallArgs.add(new Variable(mapCtxVar.get(expr)));
		}
				
		// if number of arguments do not match -> error
		if(argsToUse.size() != functionCallArgs.size()) {
			ErrorHandling.printError(ctx, "Number of arguments in function call do not match required arguments");
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
		if (mapCtxVar.get(functionToVisit) != null) {
			mapCtxVar.put(ctx, new Variable(mapCtxVar.get(functionToVisit)));
		}
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent +  "currentReturn is: " + currentReturn);
			ci();
		}
		
		return true;
	}


	// --------------------------------------------------------------------------
	// Control Flow Statements

	@Override 
	public Boolean visitControlFlowStatement(ControlFlowStatementContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->CONTROL FLOW STATEMENT");
		
		boolean valid = visitChildren(ctx);
		
		if (debug) ci();
		
		return valid;
	}

	@Override 
	public Boolean visitForLoop(ForLoopContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->FOR LOOP");
		
		extendScope();
		
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
		
		if (debug) ci();

		return valid;
	}

	@Override 
	public Boolean visitWhileLoop(WhileLoopContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->WHILE LOOP");
		
		extendScope();
		
		Boolean valid = true;
		Boolean res = visit(ctx.expression());
		valid = valid && res;
		
		// condition is boolean -> ok
		Variable var = new Variable(mapCtxVar.get(ctx.expression()));
		if (!var.isBoolean()) {
			ErrorHandling.printError(ctx, "If condition must be boolean");
			return false;
		}

		// visit all scope
		valid = valid && visit(ctx.scope());

		if (debug) ci();
		
		return valid;
	}

	@Override
	public Boolean visitCondition(ConditionContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->CONDITION");
		
		boolean valid = visitChildren(ctx);
		
		if (debug) ci();
		
		return valid;
	}
	
	@Override 
	public Boolean visitIfCondition(IfConditionContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->IF CONDITION");
		
		extendScope();
		
		Boolean valid = true;
		Boolean res = visit(ctx.expression());
		valid = valid && res;
		
		// condition is boolean -> ok
		Variable var = new Variable(mapCtxVar.get(ctx.expression()));
		
		if (!var.isBoolean()) {
			ErrorHandling.printError(ctx, "If condition must be boolean");
			return false;
		}

		// visit all scope
		valid = valid && visit(ctx.scope());
		
		if (debug) ci();
		
		return valid;
	}

	@Override 
	public Boolean visitElseIfCondition(ElseIfConditionContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->ELSE IF CONDITION");
		
		extendScope();
		
		Boolean valid = true;
		Boolean res = visit(ctx.expression());
		valid = valid && res;
		
		// condition is boolean -> ok
		Variable var = new Variable(mapCtxVar.get(ctx.expression()));
		if (!var.isBoolean()) {
			ErrorHandling.printError(ctx, "If condition must be boolean");
			return false;
		}

		// visit all scope
		valid = valid && visit(ctx.scope());
		
		if (debug) ci();
		
		return valid;
	}

	@Override 
	public Boolean visitElseCondition(ElseConditionContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->ELSE CONDITION");
		
		extendScope();
		
		Boolean valid = true;

		// visit all scope
		valid = valid && visit(ctx.scope());
		
		if (debug) ci();
				
		return valid;
	}

	@Override
	public Boolean visitScope(ScopeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->SCOPE");
		
		Boolean valid = true;
		List<StatementContext> statements = ctx.statement();

		// Visit all statement rules
		for (StatementContext stat : statements) {
			valid = valid && visit(stat);
		}
		
		if (valid && ctx.functionReturn() != null) {
			
			valid = valid && visit(ctx.functionReturn());
			if (!valid) {
				return false;
			}
			
			mapCtxVar.put(ctx, new Variable(mapCtxVar.get(ctx.functionReturn())));
			
			closeScope();
			
			return true;
		}
		
		mapCtxVar.put(ctx, new Variable(null, varType.VOID, null));
		
		currentReturn = null;
		
		closeScope();
		
		if (debug) ci();

		return valid;
	}

	// --------------------------------------------------------------------------
	// Expressions
	
	@Override 
	public Boolean visitExpression_Parenthesis(Expression_ParenthesisContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION PARENTHESIS");
		
		if(!visit(ctx.expression())) {
			return false;
		}
		mapCtxVar.put(ctx, new Variable(mapCtxVar.get(ctx.expression())));
		
		if (debug) {
			ErrorHandling.printInfo(ctx, "Expression in Parenthesis is: " + mapCtxVar.get(ctx.expression()));
			ci();
		}
		
		return true;
	}
	
	@Override
	public Boolean visitExpression_LISTINDEX(Expression_LISTINDEXContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION LISTINDEX");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable var1 = new Variable(mapCtxVar.get(ctx.expression(1)));
		
		// expression units are list and numeric ('number') -> ok
		if (var0.isList() && var1.isNumeric()) {
			
			if (var1.getUnit().equals(Units.instanceOf("number"))){
				
				ListVar listVar = (ListVar) var0.getValue();
				int index = ((Double) var1.getValue()).intValue();
				
				try {
					Variable get = new Variable(listVar.getList().get(index));
					mapCtxVar.put(ctx, get);
					
					if (debug) {
						ErrorHandling.printInfo(ctx, indent+" -> expression 0 : " + var0);
						ErrorHandling.printInfo(ctx, indent+" -> expression 1 : " + var1);
						ErrorHandling.printInfo(ctx, indent+" -> get result : " + get);
						ci();
					}
					
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
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION ISEMPTY");
		
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Variable exprVar = new Variable(mapCtxVar.get(ctx.expression()));
		boolean isEmpty;
		
		if (exprVar.isList()) {
			
			ListVar listVar = (ListVar) exprVar.getValue();
			isEmpty = listVar.getList().isEmpty();
		}
		
		else if (exprVar.isDict()) {
			
			DictVar dictVar = (DictVar) exprVar.getValue();
			isEmpty = dictVar.getDict().isEmpty();
		}
		
		else if (exprVar.isString()) {
			
			String str = (String) exprVar.getValue();
			isEmpty = str.isEmpty();
		}
		
		else {
			ErrorHandling.printError(ctx, "Bad operand units for operation 'isEmpty'");
			return false;
		}
		
		Variable var = new Variable(null , varType.BOOLEAN, isEmpty);
		mapCtxVar.put(ctx, var);
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> expression : " + exprVar);
			ErrorHandling.printInfo(ctx, indent+" -> isEMpty? : " + var);
			ci();
		}
		
		return true;
	}
	
	@Override
	public Boolean visitExpression_SIZE(Expression_SIZEContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION SIZE");
		
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Variable exprVar = new Variable(mapCtxVar.get(ctx.expression()));
		Integer size = 0;
		
		if (exprVar.isList()) {
			
			ListVar listVar = (ListVar) exprVar.getValue();
			size = listVar.getList().size();
		}
		
		else if (exprVar.isDict()) {
			
			DictVar dictVar = (DictVar) exprVar.getValue();
			size = dictVar.getDict().size();
		}
		
		else if (exprVar.isString()) {
			
			String str = (String) exprVar.getValue();
			size = str.length();
		}
		
		else {
			ErrorHandling.printError(ctx, "Bad operand units for operation 'size'");
			return false;
		}
		
		Variable var = new Variable(Units.instanceOf("number") , varType.NUMERIC, size.doubleValue());
		mapCtxVar.put(ctx, var);
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> expression : " + exprVar);
			ErrorHandling.printInfo(ctx, indent+" -> isEMpty? : " + var);
			ci();
		}
		
		return true;
	}
	
	@Override
	public Boolean visitExpression_SORT(Expression_SORTContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION SORT");
		
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Variable var = new Variable(mapCtxVar.get(ctx.expression()));
		
		if (var.isList()) {
			
			// get list info
			ListVar listVar = (ListVar) var.getValue();
			List<Variable> list = listVar.getList();
			
			mapCtxListDict.put(ctx, new Variable(null, varType.LIST, new ListVar(listVar))); // pre order list
			
			Collections.sort(list);
			
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
		
		if (debug) ci();
		
		ErrorHandling.printError(ctx, "Bad operand units for operator 'sort'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_KEYS(Expression_KEYSContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION KEYS");
		
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Variable var = new Variable(mapCtxVar.get(ctx.expression()));
		
		if (var.isDict()) {
			
			DictVar dictVar = (DictVar) var.getValue();
			Object[] arr= dictVar.getDict().keySet().toArray();
			
			ListVar listVar = new ListVar(dictVar.getKeyType(), dictVar.isBlockedKey());
			
			List<Variable> list = listVar.getList();
			for (Object obj : arr) {
				list.add((Variable) obj);
			}
			
			mapCtxVar.put(ctx, new Variable(null, varType.LIST, listVar));
			
			if (debug) ci();
			
			return true;
		}
		
		ErrorHandling.printError(ctx, "Bad operand units for operator 'value'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_VALUES(Expression_VALUESContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION VALUES");
		
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Variable var = new Variable(mapCtxVar.get(ctx.expression()));
		
		if (var.isDict()) {
			
			DictVar dictVar = (DictVar) var.getValue();
			Object[] arr = dictVar.getDict().values().toArray();
			
			ListVar listVar = new ListVar(dictVar.getValueType(), dictVar.isBlockedValue());
			
			List<Variable> list = listVar.getList();
			for (Object obj : arr) {
				list.add((Variable) obj);
			}
			
			mapCtxVar.put(ctx, new Variable(null, varType.LIST, listVar));
			
			if (debug) ci();
			
			return true;
		}
		
		ErrorHandling.printError(ctx, "Bad operand units for operator value");
		return false;
	}
	
	@Override
	public Boolean visitExpression_Cast(Expression_CastContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION CAST");
		
		if(!visitChildren(ctx)) {
			return false;
		}
		
		Variable exprVar= new Variable(mapCtxVar.get(ctx.expression()));
		Variable castVar = new Variable(mapCtxVar.get(ctx.cast()));
		
		if (!exprVar.isNumeric()) {
			ErrorHandling.printError(ctx, "Invalid operands for operartor cast");
			return false;
		}
			
		try {
			exprVar.convertUnitTo(Units.instanceOf(castVar.getUnit().getName()));
		}
		catch (IllegalArgumentException e) {
			ErrorHandling.printError(ctx, "Units are not compatible, cast is not possible");
			return false;
		}
		
		mapCtxVar.put(ctx, exprVar);
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> expression : " + exprVar);
			ErrorHandling.printInfo(ctx, indent+" -> cast Name : " + castVar.getUnit().getName());
			ci();
		}
		
		return true;
	}
	
	@Override
	public Boolean visitExpression_UnaryOperators(Expression_UnaryOperatorsContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION UNARY OPERATORS");
		
		if(!visit(ctx.expression())) {
			return false;
		}
		
		Variable var = new Variable(mapCtxVar.get(ctx.expression()));
		String op = ctx.op.getText();
		Variable res = null;
		
		if ((op.equals("-") && var.isNumeric())) {
			
			res = Variable.simetric(var);
		}
		
		else if (op.equals("!") && var.isBoolean()) {
			
			res = new Variable(var.getUnit(), var.getVarType(), !((Boolean) var.getValue()));
		}
		// other variable combinations
		else {
			
			ErrorHandling.printError(ctx, "Bad operand units for operator + '" + op + "'");
			return false;
		}
		
		mapCtxVar.put(ctx, res);
		
		if (debug) ci();
		
		return true;
	}
	
	@Override 
	public Boolean visitExpression_Power(Expression_PowerContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION POWER");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable base = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable pow = new Variable(mapCtxVar.get(ctx.expression(1)));
		
		if (base.isNumeric() && pow.isNumeric()) {
			
			if (pow.getUnit().equals(Units.instanceOf("number"))) {
				
				Variable res = Variable.power(base, pow);
				
				mapCtxVar.put(ctx, res);
				
				if (debug) {
					ErrorHandling.printInfo(ctx, indent+ " -> base: " + base);
					ErrorHandling.printInfo(ctx, indent+ " -> power: " + pow);
					ErrorHandling.printInfo(ctx, indent+ " -> result: " + res);
					ci();
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
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION MULT DIV MOD");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable var1 = new Variable(mapCtxVar.get(ctx.expression(1)));
		String op = ctx.op.getText();
	
		if (var0.isNumeric() && var1.isNumeric()) {
			
			Variable res = null;
			// Modulus
			if (op.equals("%")) {
				try {
					res = Variable.mod(var0, var1);
				}
				catch (IllegalArgumentException e) {
					ErrorHandling.printError(ctx, "Right side of mod expression has to be of Unit Number!");
					return false;
				}
			}
			
			// Multiplication
			else if (op.equals("*")) {
				res = Variable.multiply(var0, var1);
			}
			
			// Division expression
			else if (op.equals("/")) {
				try {
					res = Variable.divide(var0, var1);
				}
				catch (ArithmeticException e) {
					ErrorHandling.printError(ctx, "Cannot divide by zero");
				}
			}
			
			// update tables
			mapCtxVar.put(ctx, res);
			
			if (debug) {
				ErrorHandling.printInfo(ctx, indent+" -> Numerical Operation!");
				ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
				ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
				ErrorHandling.printInfo(ctx, indent+" -> result of op " + op + ": " + res);
				ci();
			}
			
			return true;
		}
		
		// one operand is string and the other is numeric (unit number) -> ok (string expanded concatenation)
		else if (((var0.isString() && var1.isNumeric()) || (var0.isNumeric() && var1.isString())) && op.equals("*")) {
			
			String str = "";
			int mult = 0;

			if (var0.isString()) {
				str = (String) var0.getValue();
			}
			else {
				if (var0.getUnit().equals(Units.instanceOf("number"))) {
					mult = ((Double) var0.getValue()).intValue();
				}
			}
			
			if (var1.isString()) {
				str = (String) var1.getValue();
			}
			else {
				if (var1.getUnit().equals(Units.instanceOf("number"))) {
					mult = ((Double) var1.getValue()).intValue();
				}
			}
			
			String finalStr = "";
			for (int i = 0; i < mult; i++) {
				finalStr += str;
			}
			
			// update tables
			mapCtxVar.put(ctx, new Variable (null, varType.STRING, finalStr));
			
			if (debug) {
				ErrorHandling.printInfo(ctx, indent+" -> String Operation!");
				ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
				ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
				ErrorHandling.printInfo(ctx, indent+" -> result of op " + op + ": " + finalStr);
				ci();
			}
			
			return true;
		}
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> Bad operands Error");
			ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
			ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
			ErrorHandling.printInfo(ctx, indent+" -> op: " + op);
			ci();
		}
		
		// other variable combinations
		ErrorHandling.printError(ctx, "Bad operand units for operator '" + op + "'");
		return false;
	}
	
	@Override 
	public Boolean visitExpression_Add_Sub(Expression_Add_SubContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION ADD SUB");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable var1 = new Variable(mapCtxVar.get(ctx.expression(1)));
		String op = ctx.op.getText();
		
		// both elements are numeric -> ok
		if (var0.isNumeric() && var1.isNumeric()) {
	
			try {
				Variable res = null;
				if (op.equals("+")) {
					res = Variable.add(var0, var1);
				}
				else {
					res = Variable.subtract(var0,  var1);
				}
				mapCtxVar.put(ctx, res);
				
				if (debug) {
					ErrorHandling.printInfo(ctx, indent+" -> Numeric Operation!");
					ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
					ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
					ErrorHandling.printInfo(ctx, indent+" -> result of op " + op + ": " + res);
					ci();
				}
				
				return true;
			}
			catch (IllegalArgumentException e) {
				ErrorHandling.printError(ctx, "Incompatible units");
				return false;
			}
		}
		
		// one operand is string and the other is string || boolean || numeric -> ok (string concatenation)
		else if ((var0.isString() || var1.isString()) && op.equals("+") && !var0.isTuple() && !var1.isTuple()) {
			
			String str0 = "";
			String str1 = "";
			
			// get var0 string value
			if (var0.isString()) {
				str0 = (String) var0.getValue();
			}
			else if (var0.isBoolean()) {
				str0 = ((Boolean) var0.getValue())+""; 
			}
			else if (var0.isNumeric()) {
				str0 = ((Double) var0.getValue()) + var0.getUnit().getSymbol();
			}
			else if (var0.isList()) {
				str0 = ((ListVar) var0.getValue()).toString();
			}
			else if (var0.isDict()) {
				str0 = ((DictVar) var0.getValue()).toString();
			}
			
			// get var1 string value
			if (var1.isString()) {
				str1 = (String) var1.getValue();
			}
			else if (var1.isBoolean()) {
				str1 = ((Boolean) var1.getValue())+""; 
			}
			else if (var1.isNumeric()) {
				str1 = ((Double) var1.getValue()) + var1.getUnit().getSymbol();
			}
			else if (var1.isList()) {
				str1 = ((ListVar) var1.getValue()).toString();
			}
			else if (var0.isDict()) {
				str1 = ((DictVar) var1.getValue()).toString();
			}
			
			String finalStr = str0 + str1;
			mapCtxVar.put(ctx, new Variable (null, varType.STRING, finalStr));
			
			if (debug) {
				ErrorHandling.printInfo(ctx, indent+" -> String Operation!");
				ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
				ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
				ErrorHandling.printInfo(ctx, indent+" -> result of op " + op + ": " + finalStr);
				ci();
			}
			
			return true;
		}
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> Bas Operands Error!");
			ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
			ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
			ci();
		}
		
		// other variable combinations
		ErrorHandling.printError(ctx, "Bad operand units for operator '" + op + "'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_RelationalQuantityOperators(Expression_RelationalQuantityOperatorsContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION RELATIONAL QUANTITY OPERATORS");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable var1 = new Variable(mapCtxVar.get(ctx.expression(1)));
		String op = ctx.op.getText();
		Double var0Val = (Double) var0.getValue();
		Double var1Val = (Double) var1.getValue();
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
			ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
			ci();
		}
		
		if (!(var0.isNumeric() && var1.isNumeric()) && !(var0.isString() && var1.isString())) {
			ErrorHandling.printError(ctx, "Units to be compared are not compatible");
			return false;
		}
		
		if (var0.isNumeric() && var1.isNumeric()) {
			
			try {
				var0.convertUnitTo(var1.getUnit());
				var0Val = (Double) var0.getValue();
			}
			catch (IllegalArgumentException e) {
				// do nothing (jumps to error)
			}
		}
		
		else if (var0.isString() && var1.isString()) {
			var0Val = (double) ((String) var0.getValue()).length();
			var1Val = (double) ((String) var1.getValue()).length();
		}
		
		Boolean res = false;
		if (op.equals("<")) {
			res = var0Val < var1Val;
		}
		else if (op.equals("<=")) {
			res = var0Val <= var1Val;
		}
		else if (op.equals(">")) {
			res = var0Val > var1Val;
		}
		else {
			res = var0Val >= var1Val;
		}
		
		mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, res));	
		return true;
		
	}
	
	@Override
	public Boolean visitExpression_INSTANCEOF(Expression_INSTANCEOFContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION INSTANCEOF");
		
		if(!visit(ctx.expression()) || !visit(ctx.type())) {
			return false;
		}
		
		Variable var = new Variable(mapCtxVar.get(ctx.expression()));
		Variable type = new Variable(mapCtxVar.get(ctx.type()));
		
		Boolean res = false;
		if (var.getVarType() == type.getVarType()) {
			
			if (!var.isNumeric()) {
				res = true;
			}
			else {
				if (var.getUnit().equals(type.getUnit())) {
					res = true;
				}
			}
		}
		
		mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, res));
		
		if (debug) ci();
		
		return true;
	}

	@Override
	public Boolean visitExpression_RelationalEquality(Expression_RelationalEqualityContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION RELATIONAL EQUALITY");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable var1 = new Variable(mapCtxVar.get(ctx.expression(1)));
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
			ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
			ci();
		}
		
		if (!(var0.getVarType() == var1.getVarType())) {
			ErrorHandling.printError(ctx, "Units to be compared are not compatible");
			return false;
		}
		
		if (var0.isNumeric() && var1.isNumeric()) {
			
			try {
				var0.convertUnitTo(var1.getUnit());
			}
			catch (IllegalArgumentException e) {
				// do nothing
			}
		}
		
		Boolean res = false;
		if (var0.equals(var1)) {
			res = true;
		}
		
		mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, res));
		return true;
	}
	
	@Override
	public Boolean visitExpression_logicalOperation(Expression_logicalOperationContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION LOGICAL OPERATION");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable var1 = new Variable(mapCtxVar.get(ctx.expression(1)));
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
			ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
			ci();
		}
		
		if (var0.isBoolean() && var1.isBoolean()) {
			
			Boolean res = null;
			if (ctx.op.getText().equals("&&")) {
				res = ((Boolean)var0.getValue()) && ((Boolean)var1.getValue());
			}
			else {
				res = ((Boolean)var0.getValue()) || ((Boolean)var1.getValue());
			}
			
			mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, res));
			return true;
		}
		
		ErrorHandling.printError(ctx, "bad operand units for logical operator '" + ctx.op.getText() + "'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_tuple(Expression_tupleContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION TUPLE");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable var1 = new Variable(mapCtxVar.get(ctx.expression(1)));
		
		DictTuple tuple = new DictTuple(var0, var1);
		
		mapCtxVar.put(ctx, new Variable(null, varType.TUPLE, tuple));
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
			ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
			ErrorHandling.printInfo(ctx, indent+" -> tuple: " + tuple);
			ci();
		}
		
		return true;
	}
	
	@Override
	public Boolean visitExpression_ADD(Expression_ADDContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION ADD");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable var1 = new Variable(mapCtxVar.get(ctx.expression(1)));
		
		// left expression is unit list -> verify
		if (var0.isList()) {
			
			var0 = new Variable(mapCtxListDict.get(ctx.expression(0)));
			ListVar listVar = (ListVar) var0.getValue();
			String listValueType = listVar.getType();
			boolean added = false;
			
			// list is parameterized with string or boolean
			if ((listValueType.equals("string") && var1.isString()) || (listValueType.equals("boolean") && var1.isBoolean())) {
				
				added = listVar.getList().add(var1);
			}
			
			// list is parameterized with numeric unit
			else if (Units.exists(listValueType) && var1.isNumeric()) {
				
				// as number is compatible with everything, it has to be blocked manually
				if ((!listValueType.equals("number") && var1.getUnit().equals(Units.instanceOf("number")))
						|| (listValueType.equals("number") && !var1.getUnit().equals(Units.instanceOf("number")))) {
					ErrorHandling.printError(ctx, "Bad operand. Unit '" + listValueType + "' is not compatible with '" + var1.getUnit().getName() + "'");
					return false;
				}
				
				// list accepts compatible value units -> verify
				if (!listVar.isBlocked()) {
					
					// list value unit and expression unit are not compatible -> error
					if (var1.getUnit().isCompatible(Units.instanceOf(listValueType))) {
						added = listVar.getList().add(var1);
					}
					else {
						ErrorHandling.printError(ctx, "Bad operand. Unit '" + listValueType + "' is not compatible with '" + var1.getUnit().getName() + "'");
						return false;
					}
				}
				// list is blocked to specific unit
				else {

					if (var1.getUnit().equals(Units.instanceOf("valueType"))) {
						added = listVar.getList().add(var1);
					}
					else {
						ErrorHandling.printError(ctx, "Bad operand. Unit '" + var1.getUnit().getName() + "' is not equal to blocked list values type '" + listValueType + "'");
						return false;
					}
				}
			}
			
			// list is parameterized with list or map
			else if (listValueType.contains("list") || listValueType.contains("dict")) {
				
				boolean valid = true;
				
				String exprType = "";
				String bl = ((ListVar) var1.getValue()).isBlocked() ? "" : "?";
				if (var1.isList()) {
					exprType = "list[" + bl + ((ListVar) var1.getValue()).getType() + "]";
				}
				else if (var1.isDict()) {
					exprType = "dict[" + bl + ((ListVar) var1.getValue()).getType() + "]";
				}

				while (listValueType.length() > 1) {

					int open = listValueType.indexOf('['); if (open == -1) open = Integer.MAX_VALUE;
					int close = listValueType.indexOf(']'); if (close == -1) close = Integer.MAX_VALUE;
					int comma = listValueType.indexOf(','); if (comma == -1) comma = Integer.MAX_VALUE;
					
					String next = "";
					
					if (open < close && open < comma) {
						next = "open";
					}
					else if (close < open && close < comma) {
						next = "close";
					}
					else {
						next = "comma";
					}
					
					switch(next) {
					case "open" :
						
						valid = listValueType.substring(0, open).equals(exprType.substring(0, exprType.indexOf("[")));
						if (valid) {
							listValueType = listValueType.substring(open +1, listValueType.length());
							exprType = exprType.substring(exprType.indexOf("[") +1, exprType.length());
						}
						break;
						
					case "close" :

						String eType = exprType.substring(0, exprType.indexOf("]"));
						String lType = listValueType.substring(0, close);
						boolean blocked;
						
						if((!eType.equals("") && lType.equals("")) || (eType.equals("") && !lType.equals(""))) {
							valid = false;
						}
						
						if (eType.contains("?") && !lType.contains("?")) {
							valid = false;
							break;
						}

						if (!eType.equals("") && !lType.equals("")) {
							
							if (eType.charAt(0) =='?') eType = eType.substring(1, eType.length());
							
							if (lType.charAt(0) =='?') {
								blocked = false;
								lType = lType.substring(1, lType.length());
							}
							else {
								blocked = true;
							}
							
							if (!blocked) {
								if (!Units.instanceOf(eType).isCompatible(Units.instanceOf(lType))) {
									valid = false;
								}
							}
							else {
								if (!Units.instanceOf(eType).equals(Units.instanceOf(lType))) {
									valid = false;
								}
							}
						}
						
						if (valid) {
							listValueType = listValueType.substring(close +1, listValueType.length());
							exprType = exprType.substring(exprType.indexOf("]") +1, exprType.length());
						}		
						break;
						
					default:
						
						eType = exprType.substring(0, exprType.indexOf(","));
						lType = listValueType.substring(0, comma);
												
						if (eType.contains("?") && !lType.contains("?")) {
							break;
						}
						
						if (!eType.equals("") && !lType.equals("")) {
							if (eType.charAt(0) =='?') eType = eType.substring(1, eType.length());
							if (lType.charAt(0) =='?') {
								blocked = false;
								lType = lType.substring(1, lType.length());
							}
							else {
								blocked = true;
							}
							
							if (!blocked) {

								if (!Units.instanceOf(eType).isCompatible(Units.instanceOf(lType))) {
									valid = false;
								}
							}
							else {
								if (!Units.instanceOf(eType).equals(Units.instanceOf(lType))) {
									valid = false;
								}
							}
						}

						if (valid) {
							listValueType = listValueType.substring(comma +1, listValueType.length());
							exprType = exprType.substring(exprType.indexOf(",") +1, exprType.length());
						}
					}
					
					if (!valid) {
						ErrorHandling.printError(ctx, "Bad operand. Unit expression type is not compatible to blocked list values accepted types");
						return false;
					}
				}
				added = listVar.getList().add(var1);
			}
			
			mapCtxVar.put(ctx, new Variable(null, varType.BOOLEAN, added));
			Variable list = new Variable(null, varType.LIST, listVar);
			mapCtxListDict.put(ctx, list);
			updateSymbolTable(ctx.expression(0).getText(), list);
			
			if (debug) {
				ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0.toString());
				ErrorHandling.printInfo(ctx, indent+" -> expression 0 (list) type: " + ((ListVar) var0.getValue()).getType());
				ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
				ErrorHandling.printInfo(ctx, indent+" -> added?: " + added);
				ci();
			}
			
			return true;
		}
		
		// expression to search key on is dict -> verify
		else if (var0.isDict()) {
			
			DictVar dictVar = (DictVar) var0.getValue();
			String keyUnit = dictVar.getKeyType();
			String valueUnit = dictVar.getValueType();
			Variable previous = null;
			// variable to be added is tuple -> verify
			if (var1.isTuple()) {
				
				Variable tupleKey = new Variable(((DictTuple) var1.getValue()).getKey());
				Variable tupleValue = new Variable(((DictTuple) var1.getValue()).getValue());
				
				boolean checkKey = false;
				boolean checkVal = false;
				
				// check if key is compatible
				if ((keyUnit.equals("string") && tupleKey.isString()) || (keyUnit.equals("boolean") && tupleKey.isBoolean())) {
					
					checkKey = true;
				}
				else if (Units.exists(keyUnit) && tupleKey.isNumeric()) {
					
					// as number is compatible with everything, it has to be blocked manually
					if ((!keyUnit.equals("number") && tupleKey.getUnit().equals(Units.instanceOf("number")))
							|| (keyUnit.equals("number") && !tupleKey.getUnit().equals(Units.instanceOf("number")))) {
						ErrorHandling.printError(ctx, "Bad operand. Unit '" + keyUnit + "' is not compatible with 'number'");
						return false;
					}
					
					// dict accepts compatible key units
					if (!dictVar.isBlockedKey()) {
						
						if (tupleKey.getUnit().isCompatible(Units.instanceOf(keyUnit))) {
							checkKey = true;
						}
					}
					// dict key is blocked to specific unit
					else {
						
						if (tupleKey.getUnit().equals(Units.instanceOf(keyUnit))) {
							checkKey = true;
						}
					}
					
					// key is not compatible
					if (checkKey == false) {
						ErrorHandling.printError(ctx, "Bad operand. Key unit is not compatible with dictionary parameterized key unit");
						return false;
					}
				}
				else {
					ErrorHandling.printError(ctx, "Bad operand. Key unit is not compatible with dictionary parameterized key unit");
					return false;
				}
				
				// check if value is compatible
				if ((valueUnit.equals("string") && tupleValue.isString()) || (valueUnit.equals("boolean") && tupleValue.isBoolean())) {
					
					checkVal = true;
				}
				else if (Units.exists(valueUnit) && tupleValue.isNumeric()) {
					
					// as number is compatible with everything, it has to be blocked manually
					if ((!valueUnit.equals("number") && tupleValue.getUnit().equals(Units.instanceOf("number")))
							|| (valueUnit.equals("number") && !tupleValue.getUnit().equals(Units.instanceOf("number")))) {
						ErrorHandling.printError(ctx, "Bad operand. Unit '" + valueUnit + "' is not compatible with 'number'");
						return false;
					}
					
					// dict accepts compatible value units
					if (!dictVar.isBlockedValue()) {
						
						if (tupleValue.getUnit().isCompatible(Units.instanceOf(valueUnit))) {
							checkVal = true;
						}
					}
					// dict value is blocked to specific unit
					else {
						
						if (tupleValue.getUnit().equals(Units.instanceOf(valueUnit))) {
							checkVal = true;
						}
					}
					
					// value is not compatible
					if (checkVal == false) {
						ErrorHandling.printError(ctx, "Bad operand. Value unit is not compatible with dictionary parameterized value unit");
						return false;
					}
				}
				else {
					ErrorHandling.printError(ctx, "Bad operand. Value unit is not compatible with dictionary parameterized key unit");
					return false;
				}
				
				// add/put the tuple into the dict
				if (checkKey && checkVal) {
					dictVar.getDict().put(tupleKey, tupleValue);
					previous = new Variable(var0);
					if (previous == null) {
						previous = new Variable(null, varType.VOID, null);
					}
				}
 
				mapCtxVar.put(ctx, previous);
				Variable dict = new Variable(null, varType.DICT, dictVar);
				mapCtxListDict.put(ctx, dict);
				updateSymbolTable(ctx.expression(0).getText(), dict);
				
				if (debug) {
					ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
					ErrorHandling.printInfo(ctx, indent+" -> expression 0 (dict) types: " + ((DictVar) var0.getValue()).getKeyType() + ", " + ((DictVar) var0.getValue()).getValueType());
					ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
					ErrorHandling.printInfo(ctx, indent+" -> previous: " + previous);
					ci();
				}
				
				return true;
			}
		}
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> Bad operands!");
			ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
			ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
			ci();
		}
		
		// Bad operands: var0 is not list || dict
		ErrorHandling.printError(ctx, "Bad operand units for operator 'add'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_REM(Expression_REMContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION REM");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable var1 = new Variable(mapCtxVar.get(ctx.expression(1)));
		
		// expression to search index on is unit list -> verify
		if (var0.isList()) {
			
			var0 = new Variable(mapCtxListDict.get(ctx.expression(0)));
			ListVar listVar = (ListVar) var0.getValue();
			
			// expression unit is 'number' -> verify
			if (var1.isNumeric()) {
					
				if (var1.getUnit().equals(Units.instanceOf("number"))) {
					try {
						int index = ((Double)var1.getValue()).intValue();
						Variable rem = new Variable(listVar.getList().remove(index));
						mapCtxVar.put(ctx, rem);
						Variable list = new Variable(null, varType.LIST, listVar);
						mapCtxListDict.put(ctx, list);
						updateSymbolTable(ctx.expression(0).getText(), list);
						
						if (debug) {
							ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
							ErrorHandling.printInfo(ctx, indent+" -> expression 0 (list) type: " + ((ListVar) var0.getValue()).getType());
							ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
							ErrorHandling.printInfo(ctx, indent+" -> removed: " + rem);
							ci();
						}
						
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
			
			DictVar dictVar = (DictVar) var0.getValue() ;
			String keyUnit = dictVar.getKeyType();
			
			boolean checkKey = false;
			
			// check if key is compatible
			if ((keyUnit.equals("string") && var1.isString()) || (keyUnit.equals("boolean") && var1.isBoolean())) {
				
				checkKey = true;
			}
			else if (Units.exists(keyUnit) && var1.isNumeric()) {
				
				// as number is compatible with everything, it has to be blocked manually
				if ((!keyUnit.equals("number") && var1.getUnit().equals(Units.instanceOf("number")))
						|| (keyUnit.equals("number") && !var1.getUnit().equals(Units.instanceOf("number")))) {
					ErrorHandling.printError(ctx, "Bad operand. Unit '" + keyUnit + "' is not compatible with 'number'");
					return false;
				}
				
				// dict accepts compatible key units
				if (!dictVar.isBlockedKey()) {
					
					if (var1.getUnit().isCompatible(Units.instanceOf(keyUnit))) {
						checkKey = true;
					}
				}
				// dict key is blocked to specific unit
				else {
					
					if (var1.getUnit().equals(Units.instanceOf(keyUnit))) {
						checkKey = true;
					}
				}
				
				// key is not compatible
				if (checkKey == false) {
					ErrorHandling.printError(ctx, "Bad operand. Key unit is not compatible with dictionary parameterized key unit");
					return false;
				}
			}
			else {
				ErrorHandling.printError(ctx, "Bad operand. Key unit is not compatible with dictionary parameterized key unit");
				return false;
			}
			
			Variable rem = null;
			if (checkKey) {
				
				// get index of map entry for compiler purposes (before actually removing
				int index = 0;
				Set<Variable> set = dictVar.getDict().keySet();
				Iterator<Variable> it = set.iterator();
				while (it.hasNext()) {
					Variable var = it.next();
					if (var.equals(var1)) {
						break;
					}
					index++;
				}
				mapCtxListDict.put(ctx, new Variable(Units.instanceOf("number"), varType.NUMERIC, index));
				
				// remove the entry
				rem = new Variable(dictVar.getDict().remove(var1));
				if (rem == null) {
					rem = new Variable(null, varType.VOID, null);
				}
				
				
			}
			
			// update tables
			mapCtxVar.put(ctx, rem);
			Variable dict = new Variable(null, varType.DICT, dictVar);
			//mapCtxListDict.put(ctx, dict);
			updateSymbolTable(ctx.expression(0).getText(), dict);
			
			if (debug) {
				ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
				ErrorHandling.printInfo(ctx, indent+" -> expression 0 (dict) types: " + ((DictVar) var0.getValue()).getKeyType() + ", " + ((DictVar) var0.getValue()).getValueType());
				ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
				ErrorHandling.printInfo(ctx, indent+" -> removed: " + rem);
				ci();
			}
			
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
				
				if (debug) {
					ErrorHandling.printInfo(ctx, indent+" -> String operation");
					ci();
				}
				
				return true;
			}
		}
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> Bad operands!");
			ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
			ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
			ci();
		}
		
		// left expression is not list || dict || string nor right expression is boolean || string || numeric -> error
		ErrorHandling.printError(ctx, "Bad operand for operator 'rem'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_GET(Expression_GETContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION GET");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable var1 = new Variable(mapCtxVar.get(ctx.expression(1)));
		
		// expression to search index on is list -> verify
		if(var0.isList()) {
			
			ListVar listVar = (ListVar) var0.getValue();
			
			// expression for index search is 'number' -> ok
			if (var1.isNumeric()) {
				
				if (var1.getUnit().equals(Units.instanceOf("number"))) {
					try {
						int index = ((Double) var1.getValue()).intValue();
						Variable get = new Variable((Variable) listVar.getList().get(index));
						mapCtxVar.put(ctx, get);
						
						if (debug) {
							ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
							ErrorHandling.printInfo(ctx, indent+" -> expression 0 (list) type: " + ((ListVar) var0.getValue()).getType());
							ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
							ErrorHandling.printInfo(ctx, indent+" -> get: " + get);
							ci();
						}
						
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
				
				// dict key unit and expression unit are not compatible -> error
				try {
					var1.convertUnitTo(Units.instanceOf(keyUnit));
				}
				catch (IllegalArgumentException e) {
					ErrorHandling.printError(ctx, "Bad operand. Unit '" + keyUnit + "' is not compatible with '" + var1.getUnit().getName() + "'");
					return false;
				}
				// dict key unit and expression unit are compatible -> ok (jumps to next code)
			}
			
			// dict does not accept compatible key units
			Variable get = new Variable((Variable) dictVar.getDict().get(var1));
			
			// get index of map entry for compiler purposes
			int index = 0;
			Set<Variable> set = dictVar.getDict().keySet();
			Iterator<Variable> it = set.iterator();
			while (it.hasNext()) {
				Variable var = it.next();
				if (var.equals(var1)) {
					break;
				}
				index++;
			}
			mapCtxListDict.put(ctx, new Variable(Units.instanceOf("number"), varType.NUMERIC, index));
			
			// if dictionary does not contain key
			if (get == null) {
				ErrorHandling.printError(ctx, "Dictionary does not contain key");
				return false;
			}
			
			// update tables 
			mapCtxVar.put(ctx, get);
			
			if (debug) {
				ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
				ErrorHandling.printInfo(ctx, indent+" -> expression 0 (dict) types: " + ((DictVar) var0.getValue()).getKeyType() + ", " + ((DictVar) var0.getValue()).getValueType());
				ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
				ErrorHandling.printInfo(ctx, indent+" -> get: " + get);
				ci();
			}
			
			return true;
		}
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> Bad operands!");
			ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
			ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
			ci();
		}
		
		// left expression is not list || dict nor right expression is boolean || string || numeric -> error
		ErrorHandling.printError(ctx, "Bad operand units for operator 'get'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_CONTAINS(Expression_CONTAINSContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION CONTAINS");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable var1 = new Variable(mapCtxVar.get(ctx.expression(1)));
		
		// expression to search value on has unit list -> ok
		if (var0.isList()) {
			
			ListVar listVar = (ListVar) var0.getValue();
			String valueUnit = listVar.getType();
			
			// list accepts compatible value units -> verify
			if (!listVar.isBlocked()) {

				
				// list value unit and expression unit are not compatible -> error
				try {
					var1.convertUnitTo(Units.instanceOf(valueUnit));
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
			
			if (debug) {
				ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
				ErrorHandling.printInfo(ctx, indent+" -> expression 0 (list) type: " + ((ListVar) var0.getValue()).getType());
				ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
				ErrorHandling.printInfo(ctx, indent+" -> contains?: " + contains);
				ci();
			}
			
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
				
				if (debug) {
					ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
					ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
					ErrorHandling.printInfo(ctx, indent+" -> contains?: " + contains);
					ci();
				}
				
				return true;
			}
			
			// expression to e searched is not string -> error
			ErrorHandling.printError(ctx, "Operands are not compatible");
			return false;
		}
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> Bad Operands!");
			ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
			ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
			ci();
		}
		
		// left expression is not list || string nor right expression is not boolean || string || numeric -> error
		ErrorHandling.printError(ctx, "Bad operand units for operator 'contains'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_CONTAINSKEY(Expression_CONTAINSKEYContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION CONTAINSKEY");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable var1 = new Variable(mapCtxVar.get(ctx.expression(1)));
		
		// expression to search value on has unit dict -> ok
		
		if (var0.isDict()) {
			
			DictVar dictVar = (DictVar) var0.getValue();
			String keyUnit = dictVar.getValueType();
			
			// dict accepts compatible key units -> verify
			if (!dictVar.isBlockedValue()) {
				
				// dict key unit and expression unit are not compatible -> error
				try {
					var1.convertUnitTo(Units.instanceOf(keyUnit));
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
			
			if (debug) {
				ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
				ErrorHandling.printInfo(ctx, indent+" -> expression 0 (dict) types: " + ((DictVar) var0.getValue()).getKeyType() + ", " + ((DictVar) var0.getValue()).getValueType());
				ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
				ErrorHandling.printInfo(ctx, indent+" -> contains?: " + contains);
				ci();
			}
			
			return true;
		}
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> Bad Operands!");
			ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
			ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
			ci();
		}
		
		// either left expression is not dict or right expression is not boolean || string || numeric -> error
		ErrorHandling.printError(ctx, "Bad operand units for operator 'containsKey'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_CONTAINSVALUE(Expression_CONTAINSVALUEContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION CONTAINSVALUE");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable var1 = new Variable(mapCtxVar.get(ctx.expression(1)));
		
		// expression to search value on is dict -> ok
		if (var0.isDict()) {
			
			DictVar dict = (DictVar) var0.getValue();
			String valueUnit = dict.getValueType();
			
			// dict accepts compatible value units -> verify
			if (!dict.isBlockedValue()) {
				
				// dict value unit and expression unit are not compatible -> error
				try {
					var1.convertUnitTo(Units.instanceOf(valueUnit));
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
			
			if (debug) {
				ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
				ErrorHandling.printInfo(ctx, indent+" -> expression 0 (dict) types: " + ((DictVar) var0.getValue()).getKeyType() + ", " + ((DictVar) var0.getValue()).getValueType());
				ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
				ErrorHandling.printInfo(ctx, indent+" -> contains?: " + contains);
				ci();
			}
			
			return true;
		}
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> Bad Operands!");
			ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
			ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
			ci();
		}
		
		// either left expression is not dict or right expression is not boolean || string || numeric -> error
		ErrorHandling.printError(ctx, "Bad operand units for operator 'containsValue'");
		return false;
	}
	
	@Override
	public Boolean visitExpression_INDEXOF(Expression_INDEXOFContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION INDEXOF");
		
		if(!visit(ctx.expression(0)) || !visit(ctx.expression(1))) {
			return false;
		}
		
		Variable var0 = new Variable(mapCtxVar.get(ctx.expression(0)));
		Variable var1 = new Variable(mapCtxVar.get(ctx.expression(1)));
		
		// expression to search index on is a list -> verify
		if (var0.isList()) {
			
			ListVar listVar = (ListVar) var0.getValue();
			String listUnit = listVar.getType();
			int index;
			
			// dict accepts compatible value units -> verify
			if (!listVar.isBlocked()) {
			
				// dict value unit and expression unit are not compatible -> error
				try {
					var1.convertUnitTo(Units.instanceOf(listUnit));
				}
				catch (IllegalArgumentException e) {
					ErrorHandling.printError(ctx, "Bad operand. List has parameterized unit '" + listUnit +
							"' (accepts compatible? -> " + listVar.isBlocked() + ")");
					return false;
				}
				// list unit and expression unit are different -> error
			}
			
			index = listVar.getList().indexOf(var1);
			mapCtxVar.put(ctx, new Variable(Units.instanceOf("number"), varType.NUMERIC, (double) index));
			
			if (debug) {
				ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
				ErrorHandling.printInfo(ctx, indent+" -> expression 0 (list) type: " + ((ListVar) var0.getValue()).getType());
				ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
				ErrorHandling.printInfo(ctx, indent+" -> index: " + index);
				ci();
			}
			
			return true;
		}
		
		// expression to search index on has unit string -> verify
		else if (var0.isString()) {
			
			// expression to be searched is string -> ok
			if (var1.isString()) {
				String str = (String) var0.getValue();
				String subStr = (String) var1.getValue();
				int index = str.indexOf(subStr);
				mapCtxVar.put(ctx, new Variable(Units.instanceOf("number"), varType.NUMERIC, (double) index));
				
				if (debug) {
					ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
					ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
					ErrorHandling.printInfo(ctx, indent+" -> index: " + index);
					ci();
				}
				
				return true;
			}	
		}
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> Bad operands!");
			ErrorHandling.printInfo(ctx, indent+" -> expression 0: " + var0);
			ErrorHandling.printInfo(ctx, indent+" -> expression 1: " + var1);
			ci();
		}
		
		// expression to search index on is not list or string -> error
		ErrorHandling.printError(ctx, "Bad operand units for operator 'indexof'");
		return false;	
	}
	
	@Override 
	public Boolean visitExpression_Var(Expression_VarContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION VAR");
		
		if(!visit(ctx.var())) {
			return false;
		}
		
		Variable var = new Variable(mapCtxVar.get(ctx.var()));
		mapCtxVar.put(ctx, var);
		if (var.isList() || var.isDict()) {
			mapCtxListDict.put(ctx, var);
		}
		
		
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> expression: " + mapCtxVar.get(ctx.var()));
			ci();
		}
		
		return true;
	}
	
	@Override
	public Boolean visitExpression_Value(Expression_ValueContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION VALUE");
		
		if(!visit(ctx.value())) {
			return false;
		}
		mapCtxVar.put(ctx, new Variable(mapCtxVar.get(ctx.value())));
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> expression: " + mapCtxVar.get(ctx.value()));
			ci();
		}
		
		return true;
	}

	@Override
	public Boolean visitExpression_FunctionCall(Expression_FunctionCallContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->EXPRESSION FUNCTION CALL");
		
		if(!visit(ctx.functionCall())) {
			return false;
		}
		mapCtxVar.put(ctx, new Variable(mapCtxVar.get(ctx.functionCall())));
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> expression: " + mapCtxVar.get(ctx.functionCall()));
			ci();
		}
		
		return true;
	}
	
	// --------------------------------------------------------------------------
	// Prints
	
	@Override
	public Boolean visitInputOutput(InputOutputContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->INPUT OUTPUT");
		
		boolean valid = visitChildren(ctx);
		
		if (debug) ci();
		
		return valid;
	}
	
	@Override
	public Boolean visitPrint(PrintContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->PRINT");
		
		boolean valid = visit(ctx.expression());
		
		mapCtxVar.put(ctx, new Variable(mapCtxVar.get(ctx.expression())));
		
		if (debug) ci();
		
		return valid;
	}
	
	@Override
	public Boolean visitSave(SaveContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->SAVE");
		
		boolean valid = visit(ctx.expression());
		
		if (debug) ci();
		
		return valid;
	}
	
	// TODO its not useful if cannot be parsed from string to whatever
	@Override
	public Boolean visitInput(InputContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->INPUT");
		
		if (debug) ci();
		
		return true;
	}

	// --------------------------------------------------------------------------
	// Variables
	
	@Override 
	public Boolean visitVar(VarContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->VAR");
		
		String varName = ctx.ID().getText();
		// variable is declared -> ok
		if (symbolTableContains(varName)) {
			mapCtxVar.put(ctx, new Variable(symbolTableGet(varName)));
			mapCtxListDict.put(ctx, new Variable(symbolTableGet(varName)));
			
			if (debug) {
				ErrorHandling.printInfo(ctx, indent+" -> var Name: " + varName);
				ErrorHandling.printInfo(ctx, indent+" -> var symbolTable Value: " + symbolTableGet(varName));
				ci();
			}
			
			return true;
		}
		// variable is not declared -> error
		ErrorHandling.printError(ctx, "Variable \"" + varName + "\" is not declared!");
		return false;
		
	}

	@Override
	public Boolean visitVarDeclaration(VarDeclarationContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->VARDECLARATION VARIABLE");
		
		if(!visit(ctx.type())) {
			return false;
		}
		
		Variable type = new Variable(mapCtxVar.get(ctx.type()));
		String newVarName = ctx.ID().getText();
		
		// variable to be created is already declared or is reserved word -> error
		if(!isValidNewVariableName(newVarName, ctx)) {return false;}
				
		// update tables -> unit already contains information necessary to create variable
		mapCtxVar.put(ctx, type);
		updateSymbolTable(newVarName, type);
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+" -> var Name: " + newVarName);
			ErrorHandling.printInfo(ctx, indent+" -> var type: " + type);
			ci();
		}
		
		return true;
	}
	
	// --------------------------------------------------------------------------
	// Units

	@Override 
	public Boolean visitType_Number_Type(Type_Number_TypeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->TYPE - NUMBER TYPE");
		
		Variable var = new Variable (Units.instanceOf("number"), varType.NUMERIC, 0.0);
		mapCtxVar.put(ctx, var);
		
		if (debug) ci();
		
		return true;
	}

	@Override 
	public Boolean visitType_Boolean_Type(Type_Boolean_TypeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->TYPE - BOOLEAN TYPE");
		
		Variable var = new Variable (null, varType.BOOLEAN, false);
		mapCtxVar.put(ctx, var);
		
		if (debug) ci();
		
		return true;
	}

	@Override 
	public Boolean visitType_String_Type(Type_String_TypeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->TYPE - STRING TYPE");
		
		Variable var = new Variable (null, varType.STRING, "");
		mapCtxVar.put(ctx, var);
		
		if (debug) ci();
		
		return true;
	}
	
	@Override
	public Boolean visitType_Void_Type(Type_Void_TypeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->TYPE - VOID TYPE");
		
		Variable var = new Variable (null, varType.VOID, null);
		mapCtxVar.put(ctx, var);
		
		if (debug) ci();
		
		return true;
	}

	@Override 
	public Boolean visitType_ID_Type(Type_ID_TypeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->TYPE - ID TYPE");
		
		String unitName = ctx.ID().getText();
		// unit exists -> ok
		if (Units.exists(unitName)) {
			Variable var = new Variable (Units.instanceOf(unitName), varType.NUMERIC, 0.0);
			mapCtxVar.put(ctx, var);
			
			if (debug) {
				ErrorHandling.printInfo(ctx, indent+ " -> var: " + var);
				ci();
			}
			
			return true;
		}
		// unit is not declared in units file -> error
		ErrorHandling.printError(ctx, "Invalid unit. Unit '" + unitName + "' is not declared");
		return false;
	}
	
	@Override
	public Boolean visitType_List_Type(Type_List_TypeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->TYPE - LIST TYPE");
		
		if(!visit(ctx.type())) {
			return false;
		}
		
		if (ctx.type() instanceof Type_Void_TypeContext) {
			ErrorHandling.printError(ctx, "List value type cannot be void");
			return false;
		}
		
		String type = ctx.type().getText();
		Boolean blocked = ctx.block == null ? true : false;
		
		if (ctx.type() instanceof Type_List_TypeContext) {
			
			type = ((ListVar) mapCtxVar.get(ctx.type()).getValue()).getType_();
			String isStructure = type.substring(0, 4);
			if (blocked && (isStructure.equals("list") || isStructure.equals("dict"))) {
				ErrorHandling.printError(ctx, "Invalid permision modifier to use with " + isStructure);
				return false;
			}
			type = "list[" + type + "]";
		}
		else if (ctx.type() instanceof Type_Dict_TypeContext) {
			
			DictVar dict = (DictVar) mapCtxVar.get(ctx.type()).getValue();
			type = "dict[" + dict.getKeyType() + ", " + dict.getValueType() + "]";
		}
		else {
			
			if (!Units.exists(type)) {
				ErrorHandling.printError(ctx, "Invalid unit type for list value");
				return false;
			}
			
			if (!blocked) type = "?" + type;

		}
		
		Variable var = new Variable (null, varType.LIST, new ListVar(type, blocked));
		mapCtxVar.put(ctx, var);
		mapCtxListDict.put(ctx, var);
		
		if (debug) ci();
		
		return true;
	}

	@Override
	public Boolean visitType_Dict_Type(Type_Dict_TypeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->TYPE - DICT TYPE");

		if(!visit(ctx.type(0)) || !visit(ctx.type(1))) {
			return false;
		}
		
		if (ctx.type(0) instanceof Type_Void_TypeContext || ctx.type(1) instanceof Type_Void_TypeContext) {
			ErrorHandling.printError(ctx, "Dict value and key type cannot be void");
			return false;
		}
		
		String keyType = ctx.type(0).getText();
		if (ctx.type() instanceof Type_List_TypeContext) {
			keyType = "list[" + ((ListVar) mapCtxVar.get(ctx.type(0)).getValue()).getType() + "]";
		}
		else if (ctx.type() instanceof Type_Dict_TypeContext) {
			DictVar dict = (DictVar) mapCtxVar.get(ctx.type(0)).getValue();
			keyType = "dict[" + dict.getKeyType() + ", " + dict.getValueType() + "]";
		}
		else {
			if (!Units.exists(keyType)) {
				ErrorHandling.printError(ctx, "Invalid unit type for dict key");
				return false;
			}
		}
		Boolean blockedKey = ctx.block0 == null ? true : false;
		
		String valType = ctx.type(1).getText();
		if (ctx.type() instanceof Type_List_TypeContext) {
			valType = "list[" + ((ListVar) mapCtxVar.get(ctx.type(1)).getValue()).getType() + "]";
		}
		else if (ctx.type() instanceof Type_Dict_TypeContext) {
			DictVar dict = (DictVar) mapCtxVar.get(ctx.type(1)).getValue();
			valType = "dict[" + dict.getKeyType() + ", " + dict.getValueType() + "]";
		}
		else {
			if (!Units.exists(valType)) {
				ErrorHandling.printError(ctx, "Invalid unit type for dict value");
				return false;
			}
		}
		Boolean blockedVal = ctx.block1 == null ? true : false;
		
		Variable var = new Variable (null, varType.DICT, new DictVar(keyType, blockedKey, valType, blockedVal));
		mapCtxVar.put(ctx, var);
		mapCtxListDict.put(ctx, var);
		
		if (debug) ci();
		
		return true;
	}

	@Override
	public Boolean visitValue_Number(Value_NumberContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->VALUE - NUMBER");
		
		try {
			Variable var = new Variable(Units.instanceOf("number"), varType.NUMERIC, Double.parseDouble(ctx.NUMBER().getText()));
			mapCtxVar.put(ctx, var);
			
			if (debug) {
				ErrorHandling.printInfo(ctx, indent+ " -> var: " + var);
				ci();
			}
			
			return true;
		}
		catch (NumberFormatException e) {
			ErrorHandling.printError(ctx, "Invalid number value");
			return false;
		}
	}

	@Override
	public Boolean visitValue_Boolean(Value_BooleanContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->VALUE - BOOLEAN");
		
		Variable var = new Variable(null, varType.BOOLEAN, Boolean.parseBoolean(ctx.BOOLEAN().getText()));
		mapCtxVar.put(ctx, var);
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+ " -> var: " + var);
			ci();
		}
		
		return true;
	}

	@Override
	public Boolean visitValue_String(Value_StringContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->VALUE - STRING");
		
		Variable var = new Variable(null, varType.STRING, getStringText(ctx.STRING().getText()));
		mapCtxVar.put(ctx, var);
		
		if (debug) {
			ErrorHandling.printInfo(ctx, indent+ " -> var: " + var);
			ci();
		}
		
		return true;
	}

	@Override
	public Boolean visitCast(CastContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "PSC->CAST");
		
		String castName = ctx.id.getText();
		// cast unit exists -> ok
		if (Units.exists(castName)){
			Variable var = new Variable(Units.instanceOf(castName), varType.NUMERIC, null);
			mapCtxVar.put(ctx, var);
			
			if (debug) {
				ErrorHandling.printInfo(ctx, indent+ " -> var: " + var);
				ci();
			}
			
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
		HashMap<String, Variable> currentScope = symbolTable.get(symbolTable.size()-1);
		
		for (String key : oldScope.keySet()) {
			newScope.put(key, oldScope.get(key));
		}
		
		for (String key : currentScope.keySet()) {
			newScope.put(key, currentScope.get(key));
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
			ErrorHandling.printError(ctx, "Variable \"" + varName +"\" is already declared");
			return false;
		}
		
		if (Units.isReservedWord(varName)) {
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
		if (debug) ErrorHandling.printInfo("removed quotes from string - " + str);
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
	
	private static String indent = "";
	
	private static String oi() {
		indent = indent + "\t";
		return indent;
	}
	
	private static void ci() {
		indent = indent.substring(0, indent.length()-1);
	}
}