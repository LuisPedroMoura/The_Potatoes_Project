package compiler;

import utils.errorHandling.ErrorHandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.stringtemplate.v4.*;

import potatoesGrammar.grammar.PotatoesBaseVisitor;
import potatoesGrammar.grammar.PotatoesParser.*;
import potatoesGrammar.utils.*;
import unitsGrammar.grammar.*;


/**
 * 
 * <b>PotatoesCompiler</b><p>
 * 
 * This Visitor Class runs the Potatoes Code and compiles it to Java.
 * It works under the assumption that everything is verified by the Potatoes Semantic Check.
 * Should anything fail, Potatoes Semantic Check should be the one to be corrected.
 * @
 */
public class PotatoesCompiler extends PotatoesBaseVisitor<ST> {
	
	// for debug purposes only
	private static final boolean debug = false;

	protected static STGroup stg = new STGroupFile("java.stg");
	//protected static ParseTreeProperty<Object> mapCtxObj = PotatoesSemanticCheck.getMapCtxObj();

	protected static List<HashMap<String, String>>	symbolTableNames	= new ArrayList<>();  // stores the updated name of variables
	protected static Map<String, Variable>			symbolTableValue	= new HashMap<>(); // stores the updated value of variables
	protected static ParseTreeProperty<Variable> 	mapCtxVar			= PotatoesSemanticCheck.getmapCtxVar();
	protected static ParseTreeProperty<Variable> 	mapCtxListDict		= PotatoesSemanticCheck.getmapCtxListDict();
	protected static Map<String, FunctionIDContext> functionNames		= PotatoesSemanticCheck.getFunctionNames();
	
	private static int varCounter = 0;
	private static boolean globalScope = true;
	
	ST classContent = stg.getInstanceOf("class");
	
	// --------------------------------------------------------------------------------------------------------------------
	// MAIN RULES----------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------
	
	@Override
	public ST visitProgram(ProgramContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->PROGRAM\n");
		
		// initialize symbolTableNames
		symbolTableNames.add(new HashMap<>());
	    
		// visit global Declarations
	    for(GlobalStatementContext statement : ctx.globalStatement()) {
	    	if (statement instanceof GlobalStatement_DeclarationContext) {
	    		classContent.add("stat", visit(statement));
	    	}
	    }
	    
	    // visit global Assignments
	    for(GlobalStatementContext statement : ctx.globalStatement()) {
	    	if (statement instanceof GlobalStatement_AssignmentContext) {
	    		classContent.add("stat", visit(statement));
	    	}
	    }
	    
	    globalScope = false;
	    // visit Main function
	    for(GlobalStatementContext statement : ctx.globalStatement()) {
	    	if (statement instanceof GlobalStatement_FunctionMainContext) {
	    		classContent.add("stat", visit(statement));
	    	}
	    }
	    
	    // visit Normal Methods that are not called by the Main function directly or indirectly
	    for (String key : functionNames.keySet()) {
	    	classContent.add("stat", visit(functionNames.get(key)));
	    }
	    
	    if(debug) ci();
	    
	    return classContent;
	}
	
	@Override
	public ST visitUsing(UsingContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->USING");

		
		if(debug) {
			ci();
		}
		
		// return is empty
		return stg.getInstanceOf("values");
	}
	
	@Override
	public ST visitGlobalStatement_Declaration(GlobalStatement_DeclarationContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->GLOBAL STATEMENT - DECLARATION");
		
		ST statement = visit(ctx.varDeclaration());
		
		if(debug) ci();
		
		return createEOL(statement);
	}

	@Override
	public ST visitGlobalStatement_Assignment(GlobalStatement_AssignmentContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->GLOBAL STATEMENT - ASSIGNMENT\n");
		
		ST statement = visit(ctx.assignment());
		
		if(debug) ci();
		
		return createEOL(statement);
	}
	
	@Override
	public ST visitGlobalStatement_FunctionMain(GlobalStatement_FunctionMainContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->GLOBAL STATEMENT - FUNCTION MAIN\n");
		
		if(debug) ci();
		
		return visit(ctx.functionMain());
	}

	@Override
	public ST visitGlobalStatement_FunctionID(GlobalStatement_FunctionIDContext ctx) {

		if(debug) ErrorHandling.printInfo(ctx,oi() + "->GLOBAL STATEMENT - FUNCTION ID\n");
		
		if(debug) ci();
		
		return visit(ctx.functionID());
	}
	
	// --------------------------------------------------------------------------------------------------------------------	
	// CLASS - STATEMENTS--------------------------------------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------	

	@Override
	public ST visitStatement_Declaration(Statement_DeclarationContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->STATEMENT - DECLARATION\n");
		
		ST statement = visit(ctx.varDeclaration());
		
		if(debug) ci();
		
		return createEOL(statement);
	}
	
	@Override
	public ST visitStatement_Assignment(Statement_AssignmentContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->STATEMENT - ASSIGNMENT\n");
		
		ST statement = visit(ctx.assignment());

		if(debug) ci();
		
		return statement;
	}
	
	@Override
	public ST visitStatement_Control_Flow_Statement(Statement_Control_Flow_StatementContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->STATEMENT - CONTROL FLOW STATEMENTS\n");
		if(debug) ci();
		return visit(ctx.controlFlowStatement());
	}
	
	@Override
	public ST visitStatement_FunctionCall(Statement_FunctionCallContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->STATEMENT - FUNCTION CALL");
		
		ST statement = visit(ctx.functionCall());
		
		if(debug) ci();
		
		return createEOL(statement);
	}
	
	@Override
	public ST visitStatement_InputOutput(Statement_InputOutputContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->STATEMENT - INPUT OUTPUT\n");
		
		ST statement = visit(ctx.inputOutput());
		
		if(debug) ci();
		
		return createEOL(statement);
	}
	
	@Override
	public ST visitStatement_Expression(Statement_ExpressionContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->STATEMENT - EXPRESSION\n");
		
		ST statement = visit(ctx.expression());
		
		if(debug) ci();
		
		return createEOL(statement);
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	// CLASS - ASSIGNMENTS-----------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	

	@Override
	public ST visitAssignment_Var_Declaration_Expression(Assignment_Var_Declaration_ExpressionContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->ASSIGNMENT - VAR DECLARATION - EXPRESSION\n");
		
		// get var and expression info
		ST var = visit(ctx.varDeclaration());
		ST expr = visit(ctx.expression());
		String type = (String) expr.getAttribute("type");
		String varName = (String) var.getAttribute("var");
		String exprName = (String) expr.getAttribute("var");
		String id = "";
		String factor = "";
		Variable exprVar = new Variable(mapCtxVar.get(ctx.expression())); // deep copy
		
		if (ctx.varDeclaration() instanceof VarDeclaration_VariableContext) {
			VarDeclaration_VariableContext decl = (VarDeclaration_VariableContext) ctx.varDeclaration();
			id = decl.ID().getText();
			if (exprVar.isNumeric()) {
				double conversionFactor = exprVar.convertUnitTo(Units.instanceOf(decl.type().getText()));
				factor = " * " + conversionFactor;
			}
		}
		if (ctx.varDeclaration() instanceof VarDeclaration_listContext) {
			VarDeclaration_listContext decl = (VarDeclaration_listContext) ctx.varDeclaration();
			id = decl.ID().getText();
		}
		if (ctx.varDeclaration() instanceof VarDeclaration_dictContext) {
			VarDeclaration_dictContext decl = (VarDeclaration_dictContext) ctx.varDeclaration();
			id = decl.ID().getText();
		}
		
		ST newVariable = stg.getInstanceOf("varAssignment");
		
		// create template for every variable except global variables
		if (globalScope == false) {
			
			newVariable.add("previousStatements", var);
			newVariable.add("previousStatements", expr);
			newVariable.add("var", varName);
			newVariable.add("operation", exprName + factor);
			
			// update tables
			symbolTableValue.put(varName, exprVar);
		}
		
		// global variables have to be declared with type because java does not allow assignments in global scope
		else {
			
			// global declarations must be static in order to be compatible with main function
			String[] varLines = var.render().split("\n");
			for (int i = 1; i < varLines.length; i++) {
				varLines[i] = "static " + varLines[i];
				newVariable.add("previousStatements",  varLines[i]);
			}
			
			String[] exprLines = expr.render().split("\n");
			for (String str : exprLines) {
				str = "static " + str;
				newVariable.add("previousStatements", str);
			}
		
			String newName = getNewVarName();
			newVariable.add("type", "static " + type);
			newVariable.add("var", newName);
			newVariable.add("operation", exprName + factor);
			
			// update tables
			symbolTableNamesPut(id,  newName);
			symbolTableValue.put(newName, exprVar);
		}
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> varDeclaration = " + ctx.varDeclaration().getText());
			ErrorHandling.printInfo(ctx,indent + "-> expression = " + ctx.expression().getText());
			ErrorHandling.printInfo(ctx,indent + "-> assigned = " + exprVar + "\n");
			ci();
		}
		
		return newVariable;
	}

	@Override
	public ST visitAssignment_Var_Expression(Assignment_Var_ExpressionContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->ASSIGNMENT - VAR - EXPRESSION");
		
		// get var and expression info
		String id = ctx.var().ID().getText();
		String varName = symbolTableNamesGet(id);
		//ST var = visit(ctx.var());
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String factor = "";
		
		//String lastName = symbolTableNames.get(ctx.var().ID().getText());
		Variable varVar = new Variable(symbolTableValue.get(varName));
		Variable exprVar = new Variable(mapCtxVar.get(ctx.expression())); // deep copy
		
		if (exprVar.isNumeric()) {
			double conversionFactor = exprVar.convertUnitTo(Units.instanceOf(varVar.getUnit().getName()));
			factor = " * " + conversionFactor;
		}
		
		// create template
		ST newVariable = stg.getInstanceOf("varAssignment");
		newVariable.add("previousStatements", expr);
		newVariable.add("var", varName);
		newVariable.add("operation", exprName + factor);
	
		// update tables
		symbolTableValue.put(varName, exprVar);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> var = " + ctx.var().getText());
			ErrorHandling.printInfo(ctx,indent + "-> expression = " + ctx.expression().getText() + "\n");
			ErrorHandling.printInfo(ctx,indent + "-> assigned = " + exprVar + "\n");
			ci();
		}
		
		return newVariable;
	}
	
	// --------------------------------------------------------------------------------------------------------------------	
	// FUNCTIONS-----------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	@Override
	public ST visitFunctionMain(FunctionMainContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->FUNCTION MAIN\n");
		
		openScope();
		
		ST main = stg.getInstanceOf("main");
		main.add("scope", visit(ctx.scope()));
		
		if(debug) ci();
		
		return main;
	}
	
	@Override
	public ST visitFunctionID(FunctionIDContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->FUNCTION ID");
		
		openScope();
		
		// create template
		ST function = stg.getInstanceOf("function");
		function.add("returnType", getCorrespondingTypeDeclaration(ctx.type(0).getText(), "", ""));
		function.add("functionName", ctx.ID(0).getText());
		
		for (int i = 2; i < ctx.ID().size(); i++) {
			String type = visit(ctx.type(i-2)).render();
			String var = getNewVarName();
			function.add("args", type + " " + var);
			symbolTableNamesPut(ctx.ID(i).getText(), var);
		}
		
		function.add("scope",  visit(ctx.scope()));
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> function name = " + ctx.ID(0).getText() + "\n");
			ci();
		}
		
		return function;
		
	}

	@Override
	public ST visitFunctionReturn(FunctionReturnContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->FUNCTION RETURN");
		
		// get expression info
		ST expr = visit(ctx.expression());
		
		// create template
		ST functionReturn = stg.getInstanceOf("stats");
		functionReturn.add("stat", expr);
		functionReturn.add("stat", "return " + (String) expr.getAttribute("var") + ";");
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> return type = " + mapCtxVar.get(ctx.expression()).getVarType().toString() + "\n");
			ci();
		}
		
		return functionReturn;
	}

	@Override
	public ST visitFunctionCall(FunctionCallContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->FUNCTION CALL\n");
		
		// create template
		ST functionCall = stg.getInstanceOf("functionCall");
		
		// add previousStatements and arguments
		for (int i = 0; i < ctx.expression().size(); i++) {
			ST expr = visit(ctx.expression(i));
			functionCall.add("previousStatements", expr);
			if (i == ctx.expression().size()-1) {
				functionCall.add("args", (String) expr.getAttribute("var"));
			}
			functionCall.add("args", (String) expr.getAttribute("var") + ";");
		}
		
		// add function Name
		functionCall.add("functionName", ctx.ID().getText());
		
		String functionName = ctx.ID().getText();
		FunctionIDContext functionToVisit = functionNames.get(functionName);
		functionNames.remove(functionName);
		classContent.add("stat", visit(functionToVisit));
		
		if(debug) ci();
				
		return functionCall;
	}
			
	// --------------------------------------------------------------------------------------------------------------------
	// CONTROL FLOW STATMENTS----------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	@Override
	public ST visitControlFlowStatement(ControlFlowStatementContext ctx) {

		if(debug) ErrorHandling.printInfo(ctx,oi() + "->CONTROL FLOW STATEMENT\n");
		if(debug) ci();
		return visitChildren(ctx);
	}

	@Override
	public ST visitForLoop(ForLoopContext ctx) {
	
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->FOR LOOP\n");
		
		openScope();
		
		// create template
		ST forLoop = stg.getInstanceOf("forLoop");
		
		// get first assignments and add to forLoop outsideStatements
		List<String> assignVarNames = new ArrayList<>();
		for (int i = 0; i < ctx.assignment().size()-1; i++) {
			ST assign = visit(ctx.assignment(i));
			forLoop.add("outsideStatements", assign);
			assignVarNames.add((String) assign.getAttribute("var"));
		}
		
		// get logical operation and add to forLoop outsideStatements
		ST expr = visit(ctx.expression());
		forLoop.add("outsideStatements", expr);
		
		// add logical Operation result to internal if
		String exprRes = (String) expr.getAttribute("var");
		forLoop.add("logicalOperation", "!" + exprRes);
		
		// get scope and add to stats
		forLoop.add("content", visit(ctx.scope()));
		
		// add last assignment to for loop content
		ST finalAssign = visit(ctx.assignment(ctx.assignment().size()-1));
		forLoop.add("content", finalAssign);
		
		// add logical operation to for loop content
		expr = visit(ctx.expression());
		String lastLogicalName = (String) expr.getAttribute("var");
		forLoop.add("content", expr);
		
		// force logical expression result into last varName
		forLoop.add("content", exprRes + " = " + lastLogicalName + ";");
		
		if(debug) ci();
		
		return forLoop;
	}

	@Override
	public ST visitWhileLoop(WhileLoopContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->WHILE LOOP\n");
		
		openScope();

		// get expression info
		ST expr = visit(ctx.expression());
		String logicalOperation = (String) expr.getAttribute("var");
		
		// create template
		ST whileLoop = stg.getInstanceOf("whileLoop");
		
		whileLoop.add("previousStatements", "\n//starting while template\n");
		
		whileLoop.add("previousStatements", expr);
		whileLoop.add("logicalOperation", logicalOperation);
		whileLoop.add("content", visit(ctx.scope()));
		ST exprNewVisit = visit(ctx.expression());
		String logicalOperationInside = (String) exprNewVisit.getAttribute("var");
		whileLoop.add("content", exprNewVisit);
		whileLoop.add("content", logicalOperation + " = " + logicalOperationInside + ";");

		
		if(debug) ci();
				
		return whileLoop;
	}
	
	@Override
	public ST visitCondition(ConditionContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->CONDITIONS\n");
		
		// create template
		ST stats = stg.getInstanceOf("stats");
		
		// add if condition
		ST ifCond = visit(ctx.ifCondition());
		stats.add("stat", ifCond);
		
		// add else if conditions
		for (ElseIfConditionContext elseif : ctx.elseIfCondition()) {
			stats.add("stat", visit(elseif));
		}
		
		// add else condition
		if (ctx.elseCondition() != null) {
			stats.add("stat", visit(ctx.elseCondition()));
		}
		
		if(debug) ci();
				
		return stats;
	}
	
	@Override 
	public ST visitIfCondition(IfConditionContext ctx) { 
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->IF CONDITION");
		
		openScope();

		// get expression info
		ST expr = visit(ctx.expression());
		String logicalOperation = (String) expr.getAttribute("var");
		
		// create template
		ST ifCondition = stg.getInstanceOf("ifCondition");
		ifCondition.add("previousStatements", expr);
		ifCondition.add("logicalOperation", logicalOperation);
		ifCondition.add("scope", visit(ctx.scope()));
		
		if(debug) ci();
			
		return ifCondition;
	}

	@Override 
	public ST visitElseIfCondition(ElseIfConditionContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->ELSE IF CONDITION\n");
		
		openScope();
		
		// get expression info
		ST expr = visit(ctx.expression());
		String logicalOperation = (String) expr.getAttribute("var");
		
		// create template
		ST elseIfCondition = stg.getInstanceOf("ifCondition");
		elseIfCondition.add("previousStatements", expr);
		elseIfCondition.add("logicalOperation", logicalOperation);
		elseIfCondition.add("scope", visit(ctx.scope()));
		
		if(debug) ci();
			
		return elseIfCondition;
	}

	@Override 
	public ST visitElseCondition(ElseConditionContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->ELSE CONDITION\n");
		
		openScope();
		
		// create template
		ST elseCondition = stg.getInstanceOf("elseCondition");
		elseCondition.add("scope", visit(ctx.scope()));
		
		if(debug) ci();
		
		return elseCondition;
	}
	
	@Override
	public ST visitScope(ScopeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->SCOPE\n");
		
		// Visit all statement rules
		ST scopeContent = stg.getInstanceOf("stats");
		for (StatementContext stat : ctx.statement()) {
			scopeContent.add("stat", visit(stat));
		}
		
		if (ctx.functionReturn() != null) {
			scopeContent.add("stat", visit(ctx.functionReturn()));
		}
		
		closeScope();
		
		if(debug) ci();
		
		return scopeContent;
	}

	// --------------------------------------------------------------------------------------------------------------------
	// EXPRESSIONS----------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	@Override
	public ST visitExpression_Parenthesis(Expression_ParenthesisContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - PARENTHESIS\n");
		
		// get expression info
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String type = (String) expr.getAttribute("type");
		String operation = exprName;
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr);
		newVariable.add("operation", operation);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> expr type = " + mapCtxVar.get(ctx.expression()).getVarType() + "\n");
			ci();
		}
		
		return newVariable;
	}

	@Override
	public ST visitExpression_LISTINDEX(Expression_LISTINDEXContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - LIST INDEX\n");
		
		// get expression info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String type = getListValueType(expr0);
		String operation = expr0Name + ".indexOf(" + expr1Name + ")";
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		newVariable.add("operation", operation);
		
		if(debug) ci();
		
		return newVariable;
	}

	@Override
	public ST visitExpression_ISEMPTY(Expression_ISEMPTYContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - ES EMPTY");
		
		// get expression info
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String type = "Boolean";
		String operation = exprName + ".isEmpty()";
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr);
		newVariable.add("operation", operation);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> expr type = " + mapCtxVar.get(ctx).getVarType().toString() + "\n");
			ci();
		}
		
		return newVariable;
	}

	@Override
	public ST visitExpression_SIZE(Expression_SIZEContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - SIZE");
		
		// get expression info
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String type = "";
		String operation = "";
		
		// expression is string
		if (typeIsString(expr)) {
			
			type = "String";
			operation = exprName + ".length()";
		}
		
		// expression is list
		else if (typeIsList(expr)) {
			
			String listType = getListValueType(expr);
			type = getListDeclaration(listType);
			operation = exprName + ".size()";
		}
		
		// expression is dict
		else if (typeIsMap(expr)) {
			
			String dictKeyType = getDictKeyType(expr);
			String dictValueType = getDictValueType(expr);
			type = getDictDeclaration(dictKeyType, dictValueType);
			operation = exprName + ".size()";
		}
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr);
		newVariable.add("operation", operation);

		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> expr type = " + mapCtxVar.get(ctx).getVarType().toString() + "\n");
			ci();
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_SORT(Expression_SORTContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - SORT");
		
		// get expression info
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String type = "";
		String operation = "";
		String previousStatements = "";
		
		// expression is list
		if (typeIsList(expr)) {
			
			type = getListDeclaration(getListValueType(expr));
			operation = exprName + ".sort()";
		}
		
		// expression is string
		else if (typeIsString(expr)) {
			
			type = "String";
			previousStatements = "char[] chars = " + exprName + ".toCharArray();\nArrays.sort(chars)";
			operation = "new String(chars)";
		}
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr);
		newVariable.add("previousStatements", previousStatements);
		newVariable.add("operation", operation);

		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> expr type = " + mapCtxVar.get(ctx).getVarType().toString() + "\n");
			ci();
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_KEYS(Expression_KEYSContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - KEYS\n");
		
		// get expression info
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String dictKeyType = getDictKeyType(expr);
		String type = getListDeclaration(dictKeyType);
		String operation = exprName + ".keys()";
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr);
		newVariable.add("operation", operation);
		
		if(debug) ci();
				
		return newVariable;
	}

	@Override
	public ST visitExpression_VALUES(Expression_VALUESContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION VALUES\n");
		
		// get expression info
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String dictValueType = getDictValueType(expr);
		String type = getListDeclaration(dictValueType);
		String operation = exprName + ".values()";
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr);
		newVariable.add("operation", operation);
		
		if(debug) ci();
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_Cast(Expression_CastContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION CAST");
		
		// get cast info
		String castType = ctx.cast().id.getText();
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String type = "Double";
		String operation = "";
		
		// calculation to create operation
		Variable exprVar = new Variable(mapCtxVar.get(ctx.expression())); // deep copy
		Unit castUnit = Units.instanceOf(castType); // deep copy
		double factor = exprVar.convertUnitTo(castUnit);
		
		operation = exprName + " * " + factor;
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr);
		newVariable.add("operation", operation);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> cast type = " + castType + "\n");
			ci();
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_UnaryOperators(Expression_UnaryOperatorsContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION UNARY OPERATORS");
		
		// get expressions info
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String type = (String) expr.getAttribute("type");
		String op = ctx.op.getText();
		String operation = op + exprName;
		
		// create ST
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr);
		newVariable.add("operation", operation);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> expr type = " + mapCtxVar.get(ctx).getVarType().toString());
			ErrorHandling.printInfo(ctx,indent + "-> op = " + op + "\n");
			ci();
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_Power(Expression_PowerContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - POWER");
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String operation = "Math.pow(" + expr0Name + ", " + expr1Name + ")";
		String type = "Double";
		
		// create ST
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		newVariable.add("operation", operation);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> expr type = " + mapCtxVar.get(ctx).getVarType().toString() + "\n");
			ci();
		}
		
		return newVariable;	
	}
	
	@Override
	public ST visitExpression_Mult_Div_Mod(Expression_Mult_Div_ModContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - MULT DIV MOD");
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String op = ctx.op.getText();
		String operation = "";
		String type = "";
		
		Variable expr0Var = new Variable(mapCtxVar.get(ctx.expression(0)));
		expr0Var = new Variable(expr0Var);
		Variable expr1Var = new Variable(mapCtxVar.get(ctx.expression(1)));
		expr1Var = new Variable(expr1Var);
		
		// if both operand are numeric
		if (typeIsDouble(expr0) && typeIsDouble(expr1)) {
			
			type = "Double";
			
			if (op.equals("*")) {
				Double simpleMult = (double) expr0Var.getValue() * (double) expr1Var.getValue();
				Variable res = Variable.multiply(expr0Var, expr1Var);
				
				double codeSimplificationFactor = (double) res.getValue() / simpleMult;
				operation = expr0Name + " " + op + " " + expr1Name + " " + op + " " + codeSimplificationFactor;
			}
			
			else if (op.equals("/")) {
				
				Double simpleDiv = (double) expr0Var.getValue() / (double) expr1Var.getValue();
				Variable res = Variable.divide(expr0Var, expr1Var);
				
				double codeSimplificationFactor = (double) res.getValue() / simpleDiv;
				operation = expr0Name + " " + op + " " + expr1Name + " " + " * " + " " + codeSimplificationFactor;
			}
			
			else if (op.equals("%")) {
				
				operation = expr0Name + " " + op + " " + expr1Name;
			}
		}
		
		// one of the expressions is string -> expanded concatenation
		else {
			
			type = "String";
			
			if (typeIsString(expr0)) {
				
				operation = expr0Name;
				double mult = (double) new Variable(mapCtxVar.get(ctx.expression(1))).getValue();
				for (int i = 1; i < mult ; i++) {
					operation += " + " + expr0Name;
				}				
			}
			
			if (typeIsString(expr1)) {
				
				operation = expr1Name;
				double mult = (double) new Variable(mapCtxVar.get(ctx.expression(0))).getValue();
				for (int i = 1; i < mult ; i++) {
					operation += " + " + expr1Name;
				}				
			}
		}
		
		// create ST
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		newVariable.add("operation", operation);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> op = " + op);
			ErrorHandling.printInfo(ctx,indent + "-> expr0 type = " + expr0Var.getVarType().toString());
			ErrorHandling.printInfo(ctx,indent + "-> expr1 type = " + expr1Var.getVarType().toString() + "\n");
			ci();
		}
		
		return newVariable;
	}

	@Override
	public ST visitExpression_Add_Sub(Expression_Add_SubContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - ADD SUB");
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String op = ctx.op.getText();
		String operation = "";
		String type = "";
		ST getToString = null;
		
		Variable expr0Var = new Variable(mapCtxVar.get(ctx.expression(0))); // deep copy
		Variable expr1Var = new Variable(mapCtxVar.get(ctx.expression(1))); // deep copy
		
		// create ST 
		String newName = getNewVarName();
		ST newVariable = stg.getInstanceOf("varAssignment"); 

		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		
		// both expressions are numeric
		if (typeIsDouble(expr0) && typeIsDouble(expr1)) {
			
			type = "Double";
			String factor = " * " + expr1Var.convertUnitTo(expr0Var.getUnit());
			operation = expr0Name + " " + op + " " + expr1Name + factor;
		}
		
		// one of the expressions is string -> concatenation
		else if (typeIsString(expr0) || typeIsString(expr1)) {
				
			type = "String";
			String expr0Symbol = "";
			String expr1Symbol = "";
			
			// expr0 is numeric -> get symbol for printing
			if (typeIsDouble(expr0)) {				
				expr0Symbol = " + \" " + mapCtxVar.get(ctx.expression(0)).getUnit().getSymbol() + "\"";
				operation = expr0Name + expr0Symbol + " + " + expr1Name + expr1Symbol;
			}
			else if (typeIsList(expr0) || typeIsMap(expr0)) {
				
				operation = "\"" + (String) mapCtxVar.get(ctx).getValue() + "\"";
			}
			else {
				
				operation = expr0Name + " + " + expr1Name;
			}
			
			// expr1 is numeric -> get symbol for printing
			if (typeIsDouble(expr1)) {
				expr1Symbol = " + \" " + mapCtxVar.get(ctx.expression(1)).getUnit().getSymbol() + "\"";
			}
			else if (typeIsList(expr0) || typeIsMap(expr0)) {
				
				operation = "\"" + (String) mapCtxVar.get(ctx).getValue() + "\"";
			}
			else {
				
				operation = expr0Name + " + " + expr1Name;
			}
		} 
			
		//complete template
		newVariable.add("type", type);
		newVariable.add("var", newName);
		newVariable.add("operation", operation);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> op = " + op);
			ErrorHandling.printInfo(ctx,indent + "-> expr0 = " + mapCtxVar.get(ctx.expression(0)));
			ErrorHandling.printInfo(ctx,indent + "-> expr0 type = " + expr0Var.getVarType());
			ErrorHandling.printInfo(ctx,indent + "-> expr1 = " + mapCtxVar.get(ctx.expression(1)));
			ErrorHandling.printInfo(ctx,indent + "-> expr1 type = " + expr1Var.getVarType() + "\n");
			ci();
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_RelationalQuantityOperators(Expression_RelationalQuantityOperatorsContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - RELATIONAL QUANTITY OPERATORS");
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String op = ctx.op.getText();
		String operation = "";
		
		// operands are numeric
		if (typeIsDouble(expr0)) {
			Variable expr0Var = new Variable(mapCtxVar.get(ctx.expression(0)));
			Variable expr1Var = new Variable(mapCtxVar.get(ctx.expression(1)));
			expr1Var = new Variable(expr1Var); // deep copy
			String factor = " * " + expr1Var.convertUnitTo(expr0Var.getUnit()).toString();
			operation = expr0Name + " " + op + " " + "(" + expr1Name + factor + ")";
		}
		
		// operands are boolean
		else if (typeIsString(expr0)) {
			operation = expr0Name + ".length() " + op + " " + expr1Name + ".length()";
		}
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Boolean", newName);
		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		newVariable.add("operation", operation);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> op = " + op);
			ErrorHandling.printInfo(ctx,indent + "-> expr0 type = " + mapCtxVar.get(ctx).getVarType().toString());
			ErrorHandling.printInfo(ctx,indent + "-> expr1 type = " + mapCtxVar.get(ctx).getVarType().toString() + "\n");
			ci();
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_INSTANCEOF(Expression_INSTANCEOFContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - INSTANCEOF");
		
		// get expressions info
		ST expr = visit(ctx.expression());
		ST type = visit(ctx.type());
		String operation = "";
		
		// get operation boolean value
		Variable exprVar = mapCtxVar.get(ctx.expression());
		Variable typeVar = mapCtxVar.get(ctx.type());
		
		if (exprVar.getVarType() == typeVar.getVarType()) {
			
			if (!exprVar.isNumeric()) {
				operation = "true";
			}
			
			if (exprVar.getUnit().equals(typeVar.getUnit())) {
				operation = "true";
			}
		}
		
		else {
			operation = "false";
		}
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Boolean", newName);
		newVariable.add("previousStatements", expr);
		newVariable.add("previousStatements", type);
		newVariable.add("operation", operation);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> expr0 type = " + mapCtxVar.get(ctx).getVarType().toString());
			ErrorHandling.printInfo(ctx,indent + "-> expr1 type = " + mapCtxVar.get(ctx).getVarType().toString() + "\n");
			ci();
		}
		
		return newVariable;
	}

	@Override
	public ST visitExpression_RelationalEquality(Expression_RelationalEqualityContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - RELATION EQUALITY");
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String op = ctx.op.getText();
		String operation = "";
		
		// operands are numeric
		if (typeIsDouble(expr0)) {
			Variable expr0Var = new Variable(mapCtxVar.get(ctx.expression(0)));
			Variable expr1Var = new Variable(mapCtxVar.get(ctx.expression(1)));
			expr1Var = new Variable(expr1Var); // deep copy
			String factor = " * " + expr1Var.convertUnitTo(expr0Var.getUnit()).toString();
			operation = expr0Name + op + "(" + expr1Name + factor + ")";
		}
		
		// operands are boolean
		else if (typeIsBoolean(expr0)) {
			operation = expr0Name + op + expr1Name;
		}
		
		// operands are string || lists || dict
		else {
			if (op.equals("==")) {
				operation = expr0Name + ".equals(" + expr1Name + ")";
			}
			else if (op.equals("!=")) {
				operation = "!" + expr0Name + ".equals(" + expr1Name + ")";
			}
		}
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Boolean", newName);
		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		newVariable.add("operation", operation);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> op = " + op);
			ErrorHandling.printInfo(ctx,indent + "-> expr0 type = " + mapCtxVar.get(ctx).getVarType().toString());
			ErrorHandling.printInfo(ctx,indent + "-> expr1 type = " + mapCtxVar.get(ctx).getVarType().toString() + "\n");
			ci();
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_logicalOperation(Expression_logicalOperationContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - LOGICAL OPERATION");
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String op = ctx.op.getText();
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Boolean", newName);
		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		newVariable.add("operation", expr0Name + " " + op + " " + expr1Name);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> op = " + op + "\n");
			ci();
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_tuple(Expression_tupleContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - TUPLE");
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String expr0Type = (String) expr0.getAttribute("type");
		String expr1Type = (String) expr1.getAttribute("type");
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(getDictDeclaration(expr0Type, expr1Type), newName);
		
		// create operation string
		String operation = "new AbstractMap.SimpleEntry<>(" + expr0Name + ", " + expr1Name + ")";
		
		// add previous statements
		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		newVariable.add("operation", operation);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> key type = " + expr0Type);
			ErrorHandling.printInfo(ctx,indent + "-> value type = " + expr1Type + "\n");
			ci();
		}
		
		return super.visitExpression_tuple(ctx);
	}
	
	@Override
	public ST visitExpression_ADD(Expression_ADDContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - ADD");
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String valueType = "";
		String operation = "";
		String type = "";
		
		// expr0 is a list
		if (typeIsList(expr0)) {
			
			valueType = getListValueType(expr0);
			type = "Boolean"; // in Java, list add returns boolean
			operation = expr0Name + ".get(" + expr1Name + ")";
			
			// get expr1 Variable
			Variable expr1Var = new Variable(mapCtxVar.get(ctx.expression(1)));
			expr1Var = new Variable(expr1Var); // deep copy
			
			// if expr1 is Numeric conversion may be needed
			if (expr1Var.isNumeric()) {
				double factor = expr1Var.convertUnitTo(Units.instanceOf(((ListVar)(mapCtxVar.get(ctx.expression(0)).getValue())).getType()));
				operation = expr0Name + ".add(" + expr1Name + " * " + factor + ")";
			}
		}
		
		// expr0 is a dict
		else if (typeIsMap(expr0)) {
			
			valueType = getDictValueType(expr0);
			type = valueType;
			
			operation = expr0Name + ".put(" + expr1Name + ", " + expr1Name + ")";
		}
		
		// expr0 is string -> concatenation
		else if (typeIsString(expr0)) {
			
			type = "String";
			String expr0Symbol = "";
			String expr1Symbol = "";
			
			// expr0 is numeric -> get symbol for printing
			if (typeIsDouble(expr0)) {
				expr0Symbol = " " + mapCtxVar.get(ctx.expression(0)).getUnit().getSymbol();
			}
			
			// expr1 is numeric -> get symbol for printing
			if (typeIsDouble(expr1)) {
				expr1Symbol = " " + mapCtxVar.get(ctx.expression(1)).getUnit().getSymbol();
			}
			
			operation = expr0Name + expr0Symbol + " + " + expr1Name + expr1Symbol;
		}
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		
		// add previous statements
		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		newVariable.add("operation", operation);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> expr0 type = " + type);
			ErrorHandling.printInfo(ctx,indent + "-> expr1 type = " + type + "\n");
			ci();
		}
		
		return newVariable;
	}

	@Override
	public ST visitExpression_REM(Expression_REMContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION REMOVE");
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String valueType = "";
		String operation = "";
		
		// expr0 is a list
		if (typeIsList(expr0)) {
			
			valueType = getListValueType(expr0);
			operation = expr0Name + ".remove(" + expr1Name + ".intValue())";
		}
		
		// expr0 is a dict
		if (typeIsMap(expr0)) {
			
			valueType = getDictValueType(expr0);
			
			operation = "(" + valueType + ") " + expr0Name + ".keySet().toArray()[" + (int) mapCtxListDict.get(ctx).getValue() + "]";
		}
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(valueType, newName);
		
		// add previous statements
		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		newVariable.add("operation", operation);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> expr1 type = " + valueType + "\n");
			ci();
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_GET(Expression_GETContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - GET");
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		
		// get var names to complete operation
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String valueType = "";
		String operation = "";
		
		// expr0 is a list
		if (typeIsList(expr0)) {
			
			valueType = getListValueType(expr0);
			operation = expr0Name + ".get(" + expr1Name + ".intValue())";
		}
		
		// expr0 is a dict
		if (typeIsMap(expr0)) {
			
			valueType = getDictValueType(expr0);
			
			operation = expr0Name + ".keySet().toArray()[" + (int) mapCtxListDict.get(ctx).getValue() + "]";
		}
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(valueType, newName);
		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		newVariable.add("operation", operation);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> expr0 type = " + expr0.getAttribute("type"));
			ErrorHandling.printInfo(ctx,indent + "-> expr1 type = " + valueType + "\n");
			ci();
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_CONTAINS(Expression_CONTAINSContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - CONTAINS\n");
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("boolean", newName);
		
		// add previous statements
		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		
		// get var names to complete operation and add it
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		newVariable.add("operation", expr0Name + ".contains(" + expr1Name + ")");
		
		if(debug) ci();
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_CONTAINSKEY(Expression_CONTAINSKEYContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - CONTAINS KEY\n");
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("boolean", newName);

		// add previous statements
		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		
		// get var names to complete operation and add it
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		newVariable.add("operation", expr0Name + ".containsKey(" + expr1Name + ")");
		
		if(debug) ci();
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_CONTAINSVALUE(Expression_CONTAINSVALUEContext ctx) {

		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - CONTAINS VALUE\n");
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("boolean", newName);

		// add previous statements
		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		
		// get var names to complete operation and add it
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		newVariable.add("operation", expr0Name + ".containsValue(" + expr1Name + ")");
		
		if(debug) ci();
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_INDEXOF(Expression_INDEXOFContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - INDEXOF\n");
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Double", newName);
		
		// add previous statements
		newVariable.add("previousStatements", expr0);
		newVariable.add("previousStatements", expr1);
		
		// get var names to complete operation and add it
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		newVariable.add("operation", expr0Name + ".indexOf(" + expr1Name + ")");
		
		if(debug) ci();
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_Var(Expression_VarContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - VAR\n");
		
		ST var = visit(ctx.var());
		
		if(debug) ci();
				
		return var;
	}
	
	@Override
	public ST visitExpression_Value(Expression_ValueContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - VALUE");
		
		ST value = visit(ctx.value());
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> value = " + mapCtxVar.get(ctx).getValue().toString() + "\n");
			ci();
		}
		
		return value;
	}
	
	@Override
	public ST visitExpression_FunctionCall(Expression_FunctionCallContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->EXPRESSION - FUNCTION CALL");
		
		// get expression info
		ST functionCall = visit(ctx.functionCall());
		String functionCallRender = functionCall.render();
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = stg.getInstanceOf("varAssignment");
		
		// add preiousStatments
		if (functionCallRender.contains(";")) {
			functionCallRender = functionCallRender.substring(0, functionCallRender.lastIndexOf(";"));
			newVariable.add("previousStatements", functionCallRender);
		}
		newVariable.add("type", getVarTypeDeclaration(mapCtxVar.get(ctx.functionCall())));
		newVariable.add("var", newName);
		
		String[] aux = functionCall.render().split(";");
		String opFunctCall = aux[aux.length-1];
		newVariable.add("operation", opFunctCall);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> function name = " + (String) functionCall.getAttribute("functionName") + "\n");
			ci();
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitInputOutput(InputOutputContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->INPUT OUTPUT\n");
		if(debug) ci();
		
		return super.visitChildren(ctx);
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	// INPUT OUTPUT----------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	

	@Override
	public ST visitPrint(PrintContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "-> PRINT\n");
		
		ST expr = visit(ctx.expression());
		Variable exprVar = new Variable(mapCtxVar.get(ctx.expression()));
		String exprName = (String) expr.getAttribute("var");
		
		//create printST
		ST print = stg.getInstanceOf("print");
		
		// add previous statements
		print.add("previousStatements", expr);
		
		// add print type
		if (ctx.printType.getText().equals("PRINT")){
			print.add("type", "print");
		}
		else {
			print.add("type", "println");
		}
		
		// add expression to be printed
		String expression = "";
		
		if (exprVar.isNumeric()) {
			
			expression = exprName + " + \" " + exprVar.getUnit().getSymbol() + "\"";
		}
		else if (exprVar.isList()) {
			
			expression = "\"" + ((ListVar) exprVar.getValue()).toString() + "\"";
		}
		else if (exprVar.isDict()) {
			
			expression = "\"" + ((DictVar) exprVar.getValue()).toString() + "\"";
		}
		else {
			
			expression = exprName;
		}
		print.add("expression", expression);
		
		if(debug) ci();
			
		return print;
	}

	// TODO implement...
	@Override
	public ST visitSave(SaveContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->SAVE\n");
		if(debug) ci();
		
		return super.visitSave(ctx);
	}
	
	// TODO cry.... implement... cry again...
	@Override
	public ST visitInput(InputContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->INPUT\n");
		if(debug) ci();
		
		// TODO Auto-generated method stub
		return super.visitInput(ctx);
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	// VARS AND TYPES------------------------------------------------------------------------------------------------------ 
	// --------------------------------------------------------------------------------------------------------------------

	@Override
	public ST visitVar(VarContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->VAR\n");
		
		// get var info
		String id = ctx.ID().getText();
		String lastName = symbolTableNamesGet(id);
		Variable var = new Variable(symbolTableValue.get(lastName));
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(getVarTypeDeclaration(var) , newName , lastName);
		
		// create Variable and save ctx
		symbolTableValue.put(newName, mapCtxVar.get(ctx));
		
		if(debug) {
			ErrorHandling.printInfo(ctx,indent + "-> original/last/new name = " + id + ", " + lastName + ", " + newName);
			ErrorHandling.printInfo(ctx, indent + "-> var value = " + var.getValue());
			ci();
		}
		
		return newVariable;
	}

	@Override
	public ST visitVarDeclaration_Variable(VarDeclaration_VariableContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->VARDECLARATION - VARIABLE");
		
		// get varDeclaration info
		ST typeST = visit(ctx.type());
		String originalName = ctx.ID().getText();
		String newName = getNewVarName();
		String operation = "";
		String type = "";
		
		if(typeST.render().equals("Boolean")) {
			operation = "false";
			type = "Boolean";
		}
		else if(typeST.render().equals("String")) {
			operation = "\"\"";
			type = "String";
		}
		else {
			operation = "0.0";
			type = "Double";
		}
		
		if (globalScope == true) {
			type = "static " + type;
		}
		
		// create varDeclaration ST
		ST varDeclaration = stg.getInstanceOf("varAssignment");
		varDeclaration.add("type", type);
		varDeclaration.add("var", newName);
		varDeclaration.add("operation", operation);
		
		// create Variable and save ctx and update tables
		symbolTableNamesPut(originalName, newName);
		symbolTableValue.put(newName, mapCtxVar.get(ctx));
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> original/new name = " + originalName + ", " + newName);
			ErrorHandling.printInfo(ctx,indent + "-> decl type = " + typeST.render() + "\n");
			ci();
		}
		
		return varDeclaration;
	}

	@Override
	public ST visitVarDeclaration_list(VarDeclaration_listContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->VARDECLARATION - LIST\n");
		
		// get varDeclaration info
		String originalName = ctx.ID().getText();
		String newName = getNewVarName();
		
		// create varDeclaration ST - in Java all Lists are parameterized with String to guarantee correct search and removal
		ST newVariable = varAssignmentST("List<" + visit(ctx.type()).render() + ">", newName, "new ArrayList<>()");
		
		// update variable names table
		symbolTableNamesPut(originalName, newName);
		symbolTableValue.put(newName, mapCtxVar.get(ctx));
		
		if(debug) ci();
				
		return newVariable;
	}

	@Override
	public ST visitVarDeclaration_dict(VarDeclaration_dictContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->VARDECLARATION - LIST\n");
		
		// get varDeclaration info
		String originalName = ctx.ID().getText();
		String newName = getNewVarName();
		
		// create varDeclaration ST - in Java all Maps are parameterized with String to guarantee correct search and removal
		ST newVariable = varAssignmentST("Map<" + visit(ctx.type(0)).render() + ", " + visit(ctx.type(1)).render() + ">", newName, "new LinkedHashMap<>()");
		
		// update variable names table
		symbolTableNamesPut(originalName, newName);
		symbolTableValue.put(newName, mapCtxVar.get(ctx));
		
		if(debug) ci();
		
		return newVariable;
	}

	@Override
	public ST visitType_Number_Type(Type_Number_TypeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->TYPE - NUMBER_TYPE\n");
		
		// create template
		ST type = stg.getInstanceOf("type");
		type.add("type", "Double");
		
		if(debug) ci();
		
		return type;
	}

	@Override
	public ST visitType_Boolean_Type(Type_Boolean_TypeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->TYPE BOOLEAN_TYPE\n");
		
		// create template
		ST type = stg.getInstanceOf("type");
		type.add("type", "Boolean");
		
		if(debug) ci();
		
		return type;
	}

	@Override
	public ST visitType_String_Type(Type_String_TypeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->TYPE STRING_TYPE\n");
		
		// create template
		ST type = stg.getInstanceOf("type");
		type.add("type", "String");
		
		if(debug) ci();
				
		return type;
	}
	
	@Override
	public ST visitType_Void_Type(Type_Void_TypeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->TYPE VOID_TYPE\n");
		
		// create template
		ST type = stg.getInstanceOf("type");
		type.add("type", "void");
		
		if(debug) ci();
		
		return type;
	}

	@Override
	public ST visitType_ID_Type(Type_ID_TypeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->TYPE ID_TYPE\n");
		
		// create template
		ST type = stg.getInstanceOf("type");
		type.add("type", "Double");
		
		if(debug) ci();
		
		return type;
	}
	
	@Override
	public ST visitType_List_Type(Type_List_TypeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->TYPE LIST_TYPE\n");
		
		// create template
		ST type = stg.getInstanceOf("type");
		type.add("type", "list");
		
		if(debug) ci();
		
		return type;
	}

	@Override
	public ST visitType_Dict_Type(Type_Dict_TypeContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "->TYPE DICT_TYPE\n");
		
		// create template
		ST type = stg.getInstanceOf("type");
		type.add("type", "dict");
		
		if(debug) ci();
		
		return type;
	}

	@Override
	public ST visitValue_Number(Value_NumberContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "-> VALUE NUMBER");
		
		// get number info
		String number = "" + Double.parseDouble(ctx.NUMBER().getText());
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Double", newName, number); 
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> value = " + ctx.NUMBER().getText() + "\n");
			ci();
		}
		
		return newVariable;
	}

	@Override
	public ST visitValue_Boolean(Value_BooleanContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "-> VALUE BOOLEAN");
		
		// get boolean info
		Boolean b = Boolean.parseBoolean(ctx.BOOLEAN().getText());
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Boolean", newName, b+""); 
		
		if (debug ) {
			ErrorHandling.printInfo(ctx,indent + "-> boolean = " + ctx.BOOLEAN().getText() + "\n");
			ci();
		}
		
		return newVariable;
	}

	@Override
	public ST visitValue_String(Value_StringContext ctx) {
		
		if(debug) ErrorHandling.printInfo(ctx,oi() + "-> VALUE STRING");
		
		// string is returned with quotation marks included
		String str = ctx.STRING().getText();
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("String", newName, str);
		
		if (debug) {
			ErrorHandling.printInfo(ctx,indent + "-> string = " + ctx.STRING().getText() + "\n");
			ci();
		}
		
		return newVariable;
	}
	
	/**
	 * This visitor should never b called during compilation
	 */
	@Override
	public ST visitCast(CastContext ctx) {
		return null;
	}
	
	//-------------------------------------------------------------------------------------------------------------------------------------
	//OTHER ONES---------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------

	protected static ST createEOL (ST temp) {
		String stat = temp.render()+";";
		ST statements = stg.getInstanceOf("stats");
		statements.add("stat", stat);
		return statements;		
	}
	
	protected static ST varAssignmentST(String previousStatements, String type, String var, String operation) {
		ST newVariable = stg.getInstanceOf("varAssignment");
		
		newVariable.add("previousStatements", previousStatements);
		newVariable.add("type", type);
		newVariable.add("var", var);
		newVariable.add("operation", operation);
		
		return newVariable;
	}

	protected static ST varAssignmentST(String type, String var, String operation) {
				
		ST newVariable = stg.getInstanceOf("varAssignment");
		newVariable.add("type", type);
		newVariable.add("var", var);
		newVariable.add("operation", operation);
		
		return newVariable;
	} 
	
	protected static ST varAssignmentST(String type, String var) {
		
		ST newVariable = stg.getInstanceOf("varAssignment");
		newVariable.add("type", type);
		newVariable.add("var", var);
		
		return newVariable;
	} 
	
	public static String getNewVarName() {
		String newName = "var"+varCounter;
		varCounter++;
		return newName;
		
	}

	public static String getCorrespondingTypeDeclaration(String type, String key, String value) {
		switch(type) {
		case "number"	: return "Double";
		case "string"	: return "String";
		case "boolean"	: return "Boolean";
		case "void"		: return "void";
		case "list"		: return "ArrayList<"+value+">";
		case "dict"		: return "HashMap<"+key+","+value+">";
		default : return "Double";
		}
	}
	
	private static String getVarTypeDeclaration(Variable var) {
		
		if (var.isNumeric()) {
			return "Double";
		}
		else if (var.isBoolean()) {
			return "Boolean";
		}
		else if (var.isTuple()) {
			Variable keyVar = ((DictTuple) var.getValue()).getKey();
			Variable valVar = ((DictTuple) var.getValue()).getValue();
			String key = "";
			String val = "";
			
			if (keyVar.isBoolean()) key = "Boolean";
			else if (keyVar.isString()) key = "String";
			else key = "Double";
			
			if (valVar.isBoolean()) val = "Boolean";
			else if (valVar.isString()) val = "String";
			else val = "Double";
			
			return "Entry<" + key + "' " + val + ">";
		}
		else if (var.isList()) {
			String val = ((ListVar) var.getValue()).getType();
			if (!val.equals("boolean") && !val.equals("string")) val = "Double";
			
			return "List<" + val + ">";
		}
		else if (var.isDict()) {
			String key = ((DictVar) var.getValue()).getKeyType();
			String val = ((DictVar) var.getValue()).getValueType();
			if (!key.equals("boolean") && !key.equals("string")) key = "Double";
			if (!val.equals("boolean") && !val.equals("string")) val = "Double";
			
			return "Map<" + key + ", " + val + ">";
		}
		else {
			return "String";
		}
	}
	
	private static String getListDeclaration(String param) {
		if (param.equals("string")) param = "String";
		else if (param.equals("boolean")) param = "Boolean";
		else param = "Double";
		
		return "List<" + param + ">";
	}
	
	private static String getDictDeclaration(String keyType, String valueType) {
		if (keyType.equals("string")) keyType = "String";
		else if (keyType.equals("boolean")) keyType = "Boolean";
		else keyType = "Double";
		
		if (keyType.equals("string")) keyType = "String";
		else if (keyType.equals("boolean")) keyType = "Boolean";
		else keyType = "Double";
		
		return "Map<" + keyType + ", " + valueType + ">";
	}
	
	private static String getListValueType(ST exprST) {
		String decl = ((String) exprST.getAttribute("type"));
		String type = decl.substring(5, decl.length()-1);
		if (type.equals("string")) return "String";
		else if (type.equals("boolean")) return "Boolean";
		else return "Double";
	}
	
	private static String getDictKeyType(ST exprST) {
		String decl = ((String) exprST.getAttribute("type"));
		String type = decl.split(",")[0];
		if (type.charAt(0) == '?') {
			type = type.substring(5, type.length());
		}
		else {
			type = type.substring(4, type.length());
		}
		if (type.equals("string")) return "String";
		else if (type.equals("boolean")) return "Boolean";
		else return "Double";
	}
	
	private static String getDictValueType(ST exprST) {
		String decl = ((String) exprST.getAttribute("type"));
		String type = decl.split(",")[1];
		if (type.charAt(0) == '?') {
			type = type.substring(2, type.length()-1);
		}
		else {
			type = type.substring(1, type.length()-1);
		}
		if (type.equals("string")) return "String";
		else if (type.equals("boolean")) return "Boolean";
		else return "Double";
	}
	
	private static boolean typeIsList(ST exprST) {
		if (((String)exprST.getAttribute("type")).contains("List"))
				return true;
		return false;
	}
	
	private static boolean typeIsMap(ST exprST) {
		if (((String)exprST.getAttribute("type")).contains("Map"))
				return true;
		return false;
	}
	
	private static boolean typeIsString(ST exprST) {
		if (((String)exprST.getAttribute("type")).equals("String"))
				return true;
		return false;
	}
	
	private static boolean typeIsBoolean(ST exprST) {
		if (((String)exprST.getAttribute("type")).equals("Boolean"))
				return true;
		return false;
	}
	
	private static boolean typeIsEntry(ST exprST) {
		if (((String)exprST.getAttribute("type")).contains("Entry"))
				return true;
		return false;
	}
	
	private static boolean typeIsDouble(ST exprST) {
		if (((String)exprST.getAttribute("type")).equals("Double"))
				return true;
		return false;
	}
		
	private static void openScope() {
		
		HashMap<String, String> newSymbolTable = new HashMap<>();
		int lastIndex = symbolTableNames.size()-1;
		
		Map<String, String> oldSymbolTable = symbolTableNames.get(lastIndex);
		for (String key : oldSymbolTable.keySet()) {
			newSymbolTable.put(key, oldSymbolTable.get(key));
		}
		
		symbolTableNames.add(newSymbolTable);
		
	}
	
	private static void closeScope() {
		
		int lastIndex = symbolTableNames.size()-1;
		symbolTableNames.remove(lastIndex);
	}
	
	private static void symbolTableNamesPut(String key, String value) {
		
		int lastIndex = symbolTableNames.size()-1;
		symbolTableNames.get(lastIndex).put(key, value);
	}
	
	private static String symbolTableNamesGet(String key) {
		
		int lastIndex = symbolTableNames.size()-1;
		return symbolTableNames.get(lastIndex).get(key);
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

