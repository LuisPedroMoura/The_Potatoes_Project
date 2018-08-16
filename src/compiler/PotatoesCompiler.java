package compiler;

import utils.errorHandling.ErrorHandling;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.stringtemplate.v4.*;

import potatoesGrammar.grammar.PotatoesBaseVisitor;
import potatoesGrammar.grammar.PotatoesParser.*;
import potatoesGrammar.utils.DictTuple;
import potatoesGrammar.utils.DictVar;
import potatoesGrammar.utils.ListVar;
import potatoesGrammar.utils.Variable;
import potatoesGrammar.utils.varType;
import typesGrammar.grammar.TypesFileInfo;
import typesGrammar.utils.Type;


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

	protected static STGroup stg = null;
	//protected static ParseTreeProperty<Object> mapCtxObj = PotatoesSemanticCheck.getMapCtxObj();

	protected static Map<String, String>			symbolTableNames	= new HashMap<>();  // stores the updated name of variables
	protected static Map<String, Variable>			symbolTableValue	= new HashMap<>(); // stores the updated value of variables
	protected static ParseTreeProperty<Variable> 	mapCtxVar			= new ParseTreeProperty<>();
	
	private static TypesFileInfo typesFileInfo;
	private static Map<String, Type> typesTable;
	
	private static int varCounter = 0;
	
	private static final boolean debug = true;
	
	// --------------------------------------------------------------------------------------------------------------------
	// MAIN RULES----------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------
	
	
	@Override
	public ST visitProgram(ProgramContext ctx) {
		stg = new STGroupFile("java.stg");
	    ST classContent = stg.getInstanceOf("class");
	    for(GlobalStatementContext statement : ctx.globalStatement()) {
	    	classContent.add("stat", visit(statement));
	    }
	    
	    if(debug) {
			ErrorHandling.printInfo("->PROGRAM\n");
		}
	    
	    return classContent;
	}
	
	@Override
	public ST visitUsing(UsingContext ctx) {
		String TypesFilePath = getStringText(ctx.STRING().getText());
		typesFileInfo = new TypesFileInfo(TypesFilePath);
		typesTable = typesFileInfo.getTypesTable();
		
		if(debug) {
			ErrorHandling.printInfo("->USING");
			ErrorHandling.printInfo("\t-> path = "+TypesFilePath+"\n");
		}
		
		// return is empty
		return stg.getInstanceOf("values");
	}
	
	@Override
	public ST visitGlobalStatement_Declaration(GlobalStatement_DeclarationContext ctx) {
		ST statement = visit(ctx.varDeclaration());
		
		if(debug) {
			ErrorHandling.printInfo("->GLOBAL STATEMENT - DECLARATION");
		}
		
		return createEOL(statement);
	}

	@Override
	public ST visitGlobalStatement_Assignment(GlobalStatement_AssignmentContext ctx) {
		ST statement = visit(ctx.assignment());
		
		if(debug) {
			ErrorHandling.printInfo("->GLOBAL STATEMENT - ASSIGNMENT\n");
		}
		
		return createEOL(statement);
	}

	@Override
	public ST visitGlobalStatement_Function(GlobalStatement_FunctionContext ctx) {
		
		if(debug) {
			ErrorHandling.printInfo("->GLOBAL STATEMENT - FUNCTION\n");
		}
		
		return visit(ctx.function());
	}
	
	// --------------------------------------------------------------------------------------------------------------------	
	// CLASS - STATEMENTS--------------------------------------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------	

	@Override
	public ST visitStatement_Declaration(Statement_DeclarationContext ctx) {
		ST statement = visit(ctx.varDeclaration());
		
		if(debug) {
			ErrorHandling.printInfo("->STATEMENT - DECLARATION\n");
		}
		
		return createEOL(statement);
	}
	
	@Override
	public ST visitStatement_Assignment(Statement_AssignmentContext ctx) {
		ST statement = visit(ctx.assignment());
		
		if(debug) {
			ErrorHandling.printInfo("->STATEMENT - ASSIGNMENT\n");
		}
		
		return createEOL(statement);
	}
	
	@Override
	public ST visitStatement_Control_Flow_Statement(Statement_Control_Flow_StatementContext ctx) {
		
		if(debug) {
			ErrorHandling.printInfo("->STATEMENT - CONTROL FLOW STATEMENTS\n");
		}
		
		return visit(ctx.controlFlowStatement());
	}
	
	@Override
	public ST visitStatement_FunctionCall(Statement_FunctionCallContext ctx) {
		ST statement = visit(ctx.functionCall());
		
		if(debug) {
			ErrorHandling.printInfo("->STATEMENT - FUNCTION CALL");
		}
		
		return createEOL(statement);
	}
	
	@Override
	public ST visitStatement_Function_Return(Statement_Function_ReturnContext ctx) {
		ST statement = visit(ctx.functionReturn());
		
		if(debug) {
			ErrorHandling.printInfo("->STATEMENT - FUNCTION RETURN\n");
		}
		
		return createEOL(statement);
	}
	
	@Override
	public ST visitStatement_InputOutput(Statement_InputOutputContext ctx) {
		ST statement = visit(ctx.inputOutput());
		
		if(debug) {
			ErrorHandling.printInfo("->STATEMENT - INPUT OUTPUT\n");
		}
		
		return createEOL(statement);
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	// CLASS - ASSIGNMENTS-----------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	@Override
	public ST visitAssignment_Var_Declaration_Expression(Assignment_Var_Declaration_ExpressionContext ctx) {
		
		// get var and expression info
		ST var = visit(ctx.varDeclaration());
		ST expr = visit(ctx.expression());
		String exprName = (String) var.getAttribute("var");
		String id = "";
		if (ctx.varDeclaration() instanceof VarDeclaration_VariableContext) {
			VarDeclaration_VariableContext decl = (VarDeclaration_VariableContext) ctx.varDeclaration();
			id = decl.ID().getText();
		}
		if (ctx.varDeclaration() instanceof VarDeclaration_listContext) {
			VarDeclaration_listContext decl = (VarDeclaration_listContext) ctx.varDeclaration();
			id = decl.ID(1).getText();
		}
		if (ctx.varDeclaration() instanceof VarDeclaration_dictContext) {
			VarDeclaration_dictContext decl = (VarDeclaration_dictContext) ctx.varDeclaration();
			id = decl.ID(2).getText();
		}
		
		// create template
		String newName = getNewVarName();
		ST newVariable = stg.getInstanceOf("varAssignment");
		newVariable.add("previousStatements", var);
		newVariable.add("previousStatements", expr);
		newVariable.add("var", newName);
		newVariable.add("operation", exprName);
		
		// update tables
		symbolTableNames.put(id,  newName);
		symbolTableValue.put(newName, mapCtxVar.get(ctx.expression()));
		mapCtxVar.put(ctx,  mapCtxVar.get(ctx.expression()));
		
		if(debug) {
			ErrorHandling.printInfo("->ASSIGNMENT - VAR DECLARATION - EXPRESSION\n");
			ErrorHandling.printInfo("\t-> varDeclaration = " + ctx.varDeclaration().getText());
			ErrorHandling.printInfo("\t-> expression = " + ctx.expression().getText() + "\n");
		}
		
		return newVariable;
	}

	@Override
	public ST visitAssignment_Var_Expression(Assignment_Var_ExpressionContext ctx) {
		
		// get var and expression info
		String id = ctx.var().ID().getText();
		ST var = visit(ctx.var());
		ST expr = visit(ctx.expression());
		String exprName = (String) var.getAttribute("var");
		
		// create template
		String newName = getNewVarName();
		ST newVariable = stg.getInstanceOf("var");
		newVariable.add("previousStatements", var);
		newVariable.add("previousStatements", expr);
		newVariable.add("var", newName);
		newVariable.add("operation", exprName);
	
		// update tables
		symbolTableNames.put(id, newName);
		symbolTableValue.put(newName, mapCtxVar.get(ctx.expression()));
		mapCtxVar.put(ctx, mapCtxVar.get(ctx.expression()));
		
		if(debug) {
			ErrorHandling.printInfo("");
			ErrorHandling.printInfo("->ASSIGNMENT - VAR - EXPRESSION");
			ErrorHandling.printInfo("\t-> var = " + ctx.var().getText());
			ErrorHandling.printInfo("\t-> expression = " + ctx.expression().getText() + "\n");
		}
		
		return newVariable;
	}
	
	// --------------------------------------------------------------------------------------------------------------------	
	// FUNCTIONS-----------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	@Override
	public ST visitFunction_Main(Function_MainContext ctx) {
		ST main = stg.getInstanceOf("main");
		main.add("scope", visit(ctx.scope()));
		
		if(debug) {
			ErrorHandling.printInfo("->FUNCTION MAIN\n");
		}
		
		return main;
	}
	
	@Override
	public ST visitFunction_ID(Function_IDContext ctx) {
		
		// create template
		ST function = stg.getInstanceOf("function");
		function.add("returnType", getCorrespondingTypeDeclaration(ctx.ID(0).getText(), "", ""));
		function.add("functionName", ctx.ID(1).getText());
		
		for (int i = 2; i < ctx.ID().size(); i++) {
			String type = visit(ctx.type(i-2)).render();
			String var = getNewVarName();
			function.add("args", type + " " + var);
			symbolTableNames.put(ctx.ID(i).getText(), var);
		}
		
		function.add("scope",  visit(ctx.scope()));
		
		if(debug) {
			ErrorHandling.printInfo("->FUNCTION ID");
			ErrorHandling.printInfo("\t-> function name = " + ctx.ID(1).getText() + "\n");
		}
		
		return function;
		
	}

	@Override
	public ST visitFunctionReturn(FunctionReturnContext ctx) {
		
		// get expression info
		ST expr = visit(ctx.expression());
		
		// create template
		ST functionReturn = stg.getInstanceOf("stats");
		functionReturn.add("stat", "return " + expr.render());
		functionReturn.add("stat", "return " + (String) expr.getAttribute("var"));
		
		// create Variable and save ctx
		mapCtxVar.put(ctx,  mapCtxVar.get(ctx.expression()));
		
		if(debug) {
			ErrorHandling.printInfo("->FUNCTION RETURN");
			ErrorHandling.printInfo("\t-> retur  type = " + mapCtxVar.get(ctx.expression()).getVarType().toString() + "\n");
		}
		
		return functionReturn;
	}

	@Override
	public ST visitFunctionCall(FunctionCallContext ctx) {
		
		// create template
		ST functionCall = stg.getInstanceOf("values");
		
		// add previousStatements and arguments
		for (int i = 0; i < ctx.expression().size(); i++) {
			ST expr = visit(ctx.expression(i));
			functionCall.add("previousStatements", expr.render());
			if (i == ctx.expression().size()-1) {
				functionCall.add("args", (String) expr.getAttribute("var"));
			}
			functionCall.add("args", (String) expr.getAttribute("var") + ";");
		}
		
		// add function Name
		functionCall.add("functionName", ctx.ID().getText());
		
		if(debug) {
			ErrorHandling.printInfo("->FUNCTION CALL\n");
		}
		
		return functionCall;
	}
			
	// --------------------------------------------------------------------------------------------------------------------
	// CONTROL FLOW STATMENTS----------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	@Override
	public ST visitControlFlowStatement(ControlFlowStatementContext ctx) {

		if(debug) {
			ErrorHandling.printInfo("->CONTROL FLOW STATEMENT\n");
		}
		
		return visitChildren(ctx);
	}

	@Override
	public ST visitForLoop(ForLoopContext ctx) {
		/* parser rule -> forLoop : FOR '(' assignment? EOL logicalOperation EOL assignment ')' scope */
		
		// create template
		ST forLoop = stg.getInstanceOf("forLoop");
		
		// get first assignments  and add to forLoop
		for (int i = 1; i < ctx.assignment().size(); i++) {
			forLoop.add("outsideStatements", visit(ctx.assignment(i-1)));
		}
		
		// get logical operation and add to forLoop outsideStatements
		ST expr = visit(ctx.expression());
		forLoop.add("outsideStatements", expr.render());
		
		// add logical Operation result to internal if
		String exprRes = (String) expr.getAttribute("var");
		forLoop.add("logicalOperation", "!" + exprRes);
		
		// get scope and add to stats
		forLoop.add("content", visit(ctx.scope()));
		
		// add last assignment to for loop content
		forLoop.add("content", visit(ctx.assignment(ctx.assignment().size()-1)));
		
		// add logical operation to for loop content
		expr = visit(ctx.expression());
		String lastAssignVarName = (String) expr.getAttribute("var");
		forLoop.add("content", expr.render());
		
		// force logical espression result into last varName
		forLoop.add("content", exprRes + " = " + lastAssignVarName);
		
		if(debug) {
			ErrorHandling.printInfo("->FOR LOOP\n");
		}
		
		return forLoop;
	}

	@Override
	public ST visitWhileLoop(WhileLoopContext ctx) {
		/* parser rule -> whileLoop : WHILE '(' logicalOperation ')' scope */

		// get expression info
		ST expr = visit(ctx.expression());
		String logicalOperation = (String) expr.getAttribute("var");
		
		// create template
		ST whileLoop = stg.getInstanceOf("whileLoop");
		whileLoop.add("previousStatements", expr.render());
		whileLoop.add("logicalOperation", logicalOperation);
		whileLoop.add("scope", visit(ctx.scope()));
		
		if(debug) {
			ErrorHandling.printInfo("->WHILE LOOP\n");
		}
		
		return whileLoop;
	}
	
	@Override
	public ST visitCondition(ConditionContext ctx) {
		
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
		
		if(debug) {
			ErrorHandling.printInfo("->CONDITIONS\n");
		}
		
		return stats;

	}
	
	@Override 
	public ST visitIfCondition(IfConditionContext ctx) { 
		/* parser rule -> ifCondition : IF '(' logicalOperation ')' scope */

		// get expression info
		ST expr = visit(ctx.expression());
		String logicalOperation = (String) expr.getAttribute("var");
		
		// create template
		ST ifCondition = stg.getInstanceOf("ifCondition");
		ifCondition.add("previousStatements", expr.render());
		ifCondition.add("logicalOperation", logicalOperation);
		ifCondition.add("scope", visit(ctx.scope()));
		
		if(debug) {
			ErrorHandling.printInfo("->IF CONDITION");
		}
		
		return ifCondition;
	}

	@Override 
	public ST visitElseIfCondition(ElseIfConditionContext ctx) {
		/* parser rule -> elseIfCondition : ELSE IF '(' logicalOperation ')' scope */
		
		// get expression info
		ST expr = visit(ctx.expression());
		String logicalOperation = (String) expr.getAttribute("var");
		
		// create template
		ST elseIfCondition = stg.getInstanceOf("ifCondition");
		elseIfCondition.add("previousStatements", expr.render());
		elseIfCondition.add("logicalOperation", logicalOperation);
		elseIfCondition.add("scope", visit(ctx.scope()));
		
		if(debug) {
			ErrorHandling.printInfo("->ELSE IF CONDITION\n");
		}
		
		return elseIfCondition;
	}

	@Override 
	public ST visitElseCondition(ElseConditionContext ctx) {
		/* parser rule -> elseCondition : ELSE scope */
		
		// create template
		ST elseCondition = stg.getInstanceOf("elseCondition");
		elseCondition.add("scope", visit(ctx.scope()));
		
		if(debug) {
			ErrorHandling.printInfo("->ELSE CONDITION\n");
		}
		
		return elseCondition;
	}
	
	@Override
	public ST visitScope(ScopeContext ctx) {
		
		// Visit all statement rules
		ST scopeContent = stg.getInstanceOf("scope");
		for (StatementContext stat : ctx.statement()) {
			scopeContent.add("stat", visit(stat));
		}
		
		if(debug) {
			ErrorHandling.printInfo("->SCOPE\n");
		}
		
		return scopeContent;
	}

	// --------------------------------------------------------------------------------------------------------------------
	// EXPRESSIONS----------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	@Override
	public ST visitExpression_Parenthesis(Expression_ParenthesisContext ctx) {
		
		// get expression info
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String type = (String) expr.getAttribute("type");
		String operation = exprName;
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr.render());
		newVariable.add("operation", operation);
		
		// create variable and save ctx
		mapCtxVar.put(ctx,  mapCtxVar.get(ctx.expression()));
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - PARENTHESIS\n");
			ErrorHandling.printInfo("\t-> expr type = " + mapCtxVar.get(ctx.expression()).getVarType() + "\n");
		}
		
		return newVariable;
	}

	@Override
	public ST visitExpression_LISTINDEX(Expression_LISTINDEXContext ctx) {
		
		// get expression info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String type = getListValueType(expr0);
		String operation = expr0Name + ".get(" + expr1Name + ")";
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		newVariable.add("operation", operation);
		
		// save ctx
		Variable exprVar = mapCtxVar.get(ctx.expression(0));
		varType vType = newVarType(((ListVar) exprVar.getValue()).getType());
		// variable is string || boolean
		Variable var = new Variable(null, vType, null);
		// variable is numeric
		if (vType.isNumeric()) {
			var = new Variable(exprVar.getType(), vType, null);
		}
		
		// update table
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - LIST INDEX\n");
		}
		
		return newVariable;
	}

	@Override
	public ST visitExpression_ISEMPTY(Expression_ISEMPTYContext ctx) {
		
		// get expression info
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String type = "Boolean";
		String operation = exprName + ".isEmpty()";
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr.render());
		newVariable.add("operation", operation);
		
		// save ctx
		Variable var = new Variable(null, varType.BOOLEAN, null);
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - ES EMPTY");
			ErrorHandling.printInfo("\t-> expr type = " + var.getVarType().toString() + "\n");
		}
		
		return newVariable;
	}

	@Override
	public ST visitExpression_SIZE(Expression_SIZEContext ctx) {
		
		// get expression info
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String type = "";
		String operation = "";
		
		Variable var = null;
		
		// expression is string
		if (typeIsString(expr)) {
			
			type = "String";
			operation = exprName + ".length()";
			
			// create Variable
			var = new Variable(null, varType.STRING, null);
		}
		
		// expression is list
		else if (typeIsList(expr)) {
			
			String listType = getListValueType(expr);
			type = getListDeclaration(listType);
			operation = exprName + ".size()";
			
			// create Variable
			ListVar listVar = new ListVar(listType, true);
			var = new Variable(null, varType.LIST, listVar);
		}
		
		// expression is dict
		else if (typeIsMap(expr)) {
			
			String dictKeyType = getDictKeyType(expr);
			String dictValueType = getDictValueType(expr);
			type = getDictDeclaration(dictKeyType, dictValueType);
			operation = exprName + ".size()";
			
			// create Variable
			DictVar dictVar = new DictVar(dictKeyType, true, dictValueType, true);
			var = new Variable(null, varType.DICT, dictVar);
		}
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr.render());
		newVariable.add("operation", operation);
		
		// save ctx
		mapCtxVar.put(ctx, var);

		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - SIZE");
			ErrorHandling.printInfo("\t-> expr type = " + var.getVarType().toString() + "\n");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_SORT(Expression_SORTContext ctx) {
		
		// get expression info
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String type = "";
		String operation = "";
		String previousStatements = "";
		
		Variable var = null;
		
		// expression is list
		if (typeIsList(expr)) {
			
			type = getListDeclaration(getListValueType(expr));
			operation = exprName + ".sort()";
			
			// create Variable
			ListVar listVar = new ListVar(getListValueType(expr), true);
			var = new Variable(null, varType.LIST, listVar);
		}
		
		// expression is string
		else if (typeIsString(expr)) {
			
			type = "String";
			previousStatements = "char[] chars = " + exprName + "toCharArray();\nArrays.sort(chars)";
			operation = "new String(chars)";
			
			// create Variable
			var = new Variable(null, varType.STRING, null);
		}
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr.render());
		newVariable.add("previousStatements", previousStatements);
		newVariable.add("operation", operation);
		
		// save ctx
		mapCtxVar.put(ctx, var);

		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - SORT");
			ErrorHandling.printInfo("\t-> EXPR TYPE = " + var.getVarType().toString() + "\n");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_KEYS(Expression_KEYSContext ctx) {
		
		// get expression info
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String dictKeyType = getDictKeyType(expr);
		String type = getListDeclaration(dictKeyType);
		String operation = exprName + ".values()";
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr.render());
		newVariable.add("operation", operation);
		
		// create Variable and save ctx
		ListVar listVar = new ListVar(dictKeyType, true);
		Variable var = new Variable(null, varType.LIST, listVar);
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - KEYS\n");
		}
		
		return newVariable;
	}

	@Override
	public ST visitExpression_VALUES(Expression_VALUESContext ctx) {
		
		// get expression info
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String dictValueType = getDictValueType(expr);
		String type = getListDeclaration(dictValueType);
		String operation = exprName + ".values()";
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr.render());
		newVariable.add("operation", operation);
		
		// create Variable and save ctx
		ListVar listVar = new ListVar(dictValueType, true);
		Variable var = new Variable(null, varType.LIST, listVar);
		mapCtxVar.put(ctx, var);

		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION VALUES\n");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_Cast(Expression_CastContext ctx) {
		
		// get cast info
		String castType = ctx.cast().ID().getText();
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String type = "Double";
		String operation = "";
		
		// calcultion to create operation
		Variable exprVar = mapCtxVar.get(ctx.expression());
		exprVar = new Variable(exprVar); // deep copy
		
		double factor = exprVar.convertTypeTo(typesTable.get(castType));
		
		operation = exprName + " * " + factor;
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr.render());
		newVariable.add("operation", operation);
		
		// create Variable and save ctx
		Variable var = new Variable(exprVar.getType(), varType.NUMERIC, null);
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"->EXPRESSION CAST");
			ErrorHandling.printInfo(ctx,"\t-> cast type = " + castType + "\n");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_UnaryOperators(Expression_UnaryOperatorsContext ctx) {
		
		// get expressions info
		ST expr = visit(ctx.expression());
		String exprName = (String) expr.getAttribute("var");
		String type = (String) expr.getAttribute("type");
		String op = ctx.op.getText();
		String operation = op + exprName;
		
		// create ST
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr.render());
		newVariable.add("operation", operation);
		
		// create Variable and save ctx
		Variable var = mapCtxVar.get(ctx.expression());
		var = new Variable(var); // deep copy
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"->EXPRESSION UNARY OPERATORS");
			ErrorHandling.printInfo(ctx,"\t-> expr type = " + var.getVarType().toString());
			ErrorHandling.printInfo(ctx,"\t-> op = " + op + "\n");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_Power(Expression_PowerContext ctx) {
		
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
		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		newVariable.add("operation", operation);

		// create Variables
		Variable expr0Var = mapCtxVar.get(ctx.expression(0));
		expr0Var = new Variable(expr0Var);
		Variable expr1Var = mapCtxVar.get(ctx.expression(1));
		expr1Var = new Variable(expr1Var);
		
		Variable var = Variable.multiply(expr0Var, expr1Var);
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"->EXPRESSION - POWER");
			ErrorHandling.printInfo(ctx,"\t-> expr type = " + var.getVarType().toString() + "\n");
		}
		
		return newVariable;	
	}
	
	@Override
	public ST visitExpression_Mult_Div_Mod(Expression_Mult_Div_ModContext ctx) {
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String op = ctx.op.getText();
		String operation = "";
		String type = "";
		
		Variable expr0Var = mapCtxVar.get(ctx.expression(0));
		expr0Var = new Variable(expr0Var);
		Variable expr1Var = mapCtxVar.get(ctx.expression(1));
		expr1Var = new Variable(expr1Var);
		Variable var = null;
		
		// if both operand are numeric
		if (typeIsDouble(expr0) && typeIsDouble(expr1)) {
			
			type = "Double";
			
			if (op.equals("*")) {
				Double simpleMult = (double) expr0Var.getValue() * (double) expr0Var.getValue();
				Variable res = Variable.multiply(expr0Var, expr1Var);
				
				double codeSimplificationFactor = (double) res.getValue() / simpleMult;
				operation = expr0Name + " " + op + " " + expr1Name + " " + op + " " + codeSimplificationFactor;
				
				var = new Variable(res.getType(), varType.NUMERIC, null);
			}
			
			else if (op.equals("/")) {
				Double simpleDiv = (double) expr0Var.getValue() * (double) expr0Var.getValue();
				Variable res = Variable.multiply(expr0Var, expr1Var);
				
				double codeSimplificationFactor = (double) res.getValue() / simpleDiv;
				operation = expr0Name + " " + op + " " + expr1Name + " " + " * " + " " + codeSimplificationFactor;
				
				var = new Variable(res.getType(), varType.NUMERIC, null);
			}
			
			else if (op.equals("%")) {
				
				operation = expr0Name + " " + op + " " + expr1Name;
				var = new Variable(expr0Var.getType(), varType.NUMERIC, null);
			}
		}
		
		// one of the expressions is string -> expanded concatenation
		else {
			
			type = "String";
			
			if (typeIsString(expr0)) {
				
				operation = expr0Name;
				double mult = (double) mapCtxVar.get(ctx.expression(1)).getValue();
				for (int i = 1; i < mult ; i++) {
					operation += "+ " + expr0Name;
				}				
			}
			
			if (typeIsString(expr1)) {
				
				operation = expr1Name;
				double mult = (double) mapCtxVar.get(ctx.expression(0)).getValue();
				for (int i = 1; i < mult ; i++) {
					operation += "+ " + expr1Name;
				}				
			}
			
			var = new Variable(null, varType.STRING, null);
		}
		
		// create ST
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		newVariable.add("operation", operation);
		
		// update table
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"->EXPRESSION - MULT DIV MOD");
			ErrorHandling.printInfo(ctx,"\t-> op = " + op);
			ErrorHandling.printInfo(ctx,"\t-> expr0 type = " + expr0Var.getVarType().toString());
			ErrorHandling.printInfo(ctx,"\t-> expr1 type = " + expr1Var.getVarType().toString() + "\n");
		}
		
		return newVariable;
	}

	@Override
	public ST visitExpression_Add_Sub(Expression_Add_SubContext ctx) {
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String op = ctx.op.getText();
		String operation = "";
		String type = "";
		
		Variable expr0Var = mapCtxVar.get(ctx.expression(0));
		expr0Var = new Variable(expr0Var);
		Variable expr1Var = mapCtxVar.get(ctx.expression(1));
		expr1Var = new Variable(expr1Var);
		Variable var = null;
		
		// both expressions are numeric
		if (typeIsDouble(expr0) && typeIsDouble(expr1)) {
			
			type = "Double";
			String factor = " * " + expr0Var.convertTypeTo(expr1Var.getType());
			operation = expr0Name + factor + " " + op + " " + expr1Name;
			
			// create Variable
			var = new Variable(expr1Var.getType(), varType.NUMERIC, null);
		}
		
		// one of the expressions is string -> concatenation
		if (typeIsString(expr0)) {

			type = "String";
			String expr0Symbol = "";
			String expr1Symbol = "";
			
			// expr0 is numeric -> get symbol for printing
			if (typeIsDouble(expr0)) {
				expr0Symbol = " " + mapCtxVar.get(ctx.expression(0)).getType().getPrintName();
			}
			
			// expr1 is numeric -> get symbol for printing
			if (typeIsDouble(expr1)) {
				expr1Symbol = " " + mapCtxVar.get(ctx.expression(1)).getType().getPrintName();
			}
			
			operation = expr0Name + expr0Symbol + " + " + expr1Name + expr1Symbol;
			
			// create Variable
			var = new Variable(null, varType.STRING, null);
		}
			
		// create ST 
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName); 

		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		newVariable.add("operation", operation);
		
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"->EXPRESSION - ADD SUB");
			ErrorHandling.printInfo(ctx,"\t-> op = " + op);
			ErrorHandling.printInfo(ctx,"\t-> expr0 type = " + expr0Var.getVarType().toString());
			ErrorHandling.printInfo(ctx,"\t-> expr1 type = " + expr1Var.getVarType().toString() + "\n");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_RelationalQuantityOperators(Expression_RelationalQuantityOperatorsContext ctx) {
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String op = ctx.op.getText();
		String operation = "";
		
		// operands are numeric
		if (typeIsDouble(expr0)) {
			Variable expr1Var = mapCtxVar.get(ctx.expression(1));
			expr1Var = new Variable(expr1Var); // deep copy
			String factor = " * " + expr1Var.convertTypeTo(mapCtxVar.get(ctx.expression(0)).getType()).toString();
			operation = expr0Name + " " + op + " " + "(" + expr1Name + factor + ")";
		}
		
		// operands are boolean
		else if (typeIsString(expr0)) {
			operation = expr0Name + ".length() " + op + " " + expr1Name + ".length()";
		}
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Boolean", newName);
		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		newVariable.add("operation", operation);
		
		// create Variable and save ctx
		Variable var = new Variable(null, varType.BOOLEAN, null);
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - RELATIONAL QUANTITY OPERATORS");
			ErrorHandling.printInfo(ctx,"\t-> op = " + op);
			ErrorHandling.printInfo(ctx,"\t-> expr0 type = " + var.getVarType().toString());
			ErrorHandling.printInfo(ctx,"\t-> expr1 type = " + var.getVarType().toString() + "\n");
		}
		
		return newVariable;
	}

	@Override
	public ST visitExpression_RelationalEquality(Expression_RelationalEqualityContext ctx) {
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String op = ctx.op.getText();
		String operation = "";
		
		// operands are numeric
		if (typeIsDouble(expr0)) {
			Variable expr1Var = mapCtxVar.get(ctx.expression(1));
			expr1Var = new Variable(expr1Var); // deep copy
			String factor = " * " + expr1Var.convertTypeTo(mapCtxVar.get(ctx.expression(0)).getType()).toString();
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
		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		newVariable.add("operation", operation);
		
		// create Variable and save ctx
		Variable var = new Variable(null, varType.BOOLEAN, null);
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - RELATION EQUALITY");
			ErrorHandling.printInfo(ctx,"\t-> op = " + op);
			ErrorHandling.printInfo(ctx,"\t-> expr0 type = " + var.getVarType().toString());
			ErrorHandling.printInfo(ctx,"\t-> expr1 type = " + var.getVarType().toString() + "\n");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_logicalOperation(Expression_logicalOperationContext ctx) {
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String op = ctx.op.getText();
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Boolean", newName);
		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		newVariable.add("operation", expr0Name + op + expr1Name);
		
		// create Variable and save ctx
		Variable var = new Variable(null, varType.BOOLEAN, null);
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - LOGICAL OPERATION");
			ErrorHandling.printInfo(ctx,"\t-> op = " + op + "\n");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_tuple(Expression_tupleContext ctx) {
		
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
		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		newVariable.add("operation", operation);
		
		// create Variable and save ctx
		DictTuple tuple = new DictTuple(mapCtxVar.get(ctx.expression(0)), mapCtxVar.get(ctx.expression(1)));
		Variable var = new Variable(null, varType.TUPLE, tuple);
		mapCtxVar.put(ctx,  var);
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - TUPLE");
			ErrorHandling.printInfo("\t-> key type = " + expr0Type);
			ErrorHandling.printInfo("\t-> value type = " + expr1Type + "\n");
		}
		
		return super.visitExpression_tuple(ctx);
	}
	
	@Override
	public ST visitExpression_ADD(Expression_ADDContext ctx) {
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String valueType = "";
		String operation = "";
		String type = "";
		
		// create Variable to save in mapCtxVar
		Variable var = null;
		
		// expr0 is a list
		if (typeIsList(expr0)) {
			
			valueType = getListValueType(expr0);
			type = getListDeclaration(valueType);
			operation = expr0Name + ".get(" + expr1Name + ")";
			
			// get expr1 Variable
			Variable expr0Var = mapCtxVar.get(ctx.expression(1));
			expr0Var = new Variable(expr0Var); // deep copy
			
			// if expr1 is Numeric conversion may be needed
			if (expr0Var.isNumeric()) {
				double factor = expr0Var.convertTypeTo(typesTable.get(valueType));
				operation = expr0Name + ".add(" + expr1Name + " * " + factor;
			}
		}
		
		// expr0 is a dict
		if (typeIsMap(expr0)) {
			
			valueType = getDictValueType(expr0);
			String keyType = getDictKeyType(expr0);
			type = getDictDeclaration(keyType, valueType);
			String keyFactor = "";
			String valueFactor = "";
			
			// get variable type for dictionary key conversion factor
			DictTuple dictTuple = (DictTuple) mapCtxVar.get(ctx.expression(1)).getValue();
			Variable key = dictTuple.getKey();
			key = new Variable(key); // deep copy
			Variable value = dictTuple.getValue();
			value = new Variable(value); // deep copy
			
			// key is numeric -> get conversion factor
			if (key.isNumeric()) {
				keyFactor = " * " + key.convertTypeTo(typesTable.get(keyType)).toString();
			}
			
			// value is numeric -> get convertion factor
			if (value.isNumeric()) {
				valueFactor = " * " + value.convertTypeTo(typesTable.get(keyType)).toString();
			}
			
			operation = expr0Name + ".put(" + expr1Name + ".getKey()" + keyFactor + ", " + expr1Name + ".getValue()" + valueFactor + ")";
		}
		
		// expr0 is string -> concatenation
		if (typeIsString(expr0)) {
			
			type = "String";
			String expr0Symbol = "";
			String expr1Symbol = "";
			
			// expr0 is numeric -> get symbol for printing
			if (typeIsDouble(expr0)) {
				expr0Symbol = " " + mapCtxVar.get(ctx.expression(0)).getType().getPrintName();
			}
			
			// expr1 is numeric -> get symbol for printing
			if (typeIsDouble(expr1)) {
				expr1Symbol = " " + mapCtxVar.get(ctx.expression(1)).getType().getPrintName();
			}
			
			operation = expr0Name + expr0Symbol + " + " + expr1Name + expr1Symbol;
		}
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(type, newName);
		
		// add previous statements
		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		newVariable.add("operation", operation);
		
		// create Variable and save ctx
		varType vType = varType.STRING;
		if (typeIsList(expr0)) {
			vType = varType.LIST;
			if(typeIsMap(expr0)) {
				vType = varType.DICT;
			}
		}
		var = new Variable(null, vType, null);	// the boolean, string and numeric values are not relevant for compilation
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - ADD");
			ErrorHandling.printInfo("\t-> expr0 type = " + type);
			ErrorHandling.printInfo("\t-> expr1 type = " + type + "\n");
		}
		
		return newVariable;
	}

	@Override
	public ST visitExpression_REM(Expression_REMContext ctx) {
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		String valueType = "";
		String operation = "";
		String paramType = "";
		
		// expr0 is a list
		if (typeIsList(expr0)) {
			
			valueType = getListValueType(expr0);
			operation = expr0Name + ".get(" + expr1Name + ".intValue())";
			
			// get info to create Variable
			ListVar listVar = (ListVar) mapCtxVar.get(ctx.expression(0)).getValue();
			paramType = listVar.getType();
		}
		
		// expr0 is a dict
		if (typeIsMap(expr0)) {
			
			valueType = getDictValueType(expr0);
			String keyType = getDictKeyType(expr0);
			String factor = "";
			
			// get variable type for dictionary key conversion factor
			Variable key = mapCtxVar.get(ctx.expression(1));
			key = new Variable(key); // deep copy
			
			// key is numeric -> get conversion factor
			if (key.isNumeric()) {
				factor = " * " + key.convertTypeTo(typesTable.get(keyType)).toString();
			}
			
			operation = expr0Name + ".get(" + expr1Name + factor + ")";
			
			// create Variable
			DictVar dictVar = (DictVar) mapCtxVar.get(ctx.expression(0)).getValue();
			paramType = dictVar.getValueType();
		}
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(valueType, newName);
		
		// add previous statements
		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		newVariable.add("operation", operation);
		
		// create Variable and save ctx
		Type type = null;
		varType vType = varType.STRING;
		if (!paramType.equals("string")) {
			vType = varType.BOOLEAN;
			if(!paramType.equals("boolean")) {
				type = typesTable.get(paramType);
				vType = varType.NUMERIC;
			}
		}
		Variable var = new Variable(type, vType, null);	// the boolean, string and numeric values are not relevant for compilation
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION REMOVE");
			ErrorHandling.printInfo("\t-> expr0 type = " + type);
			ErrorHandling.printInfo("\t-> expr1 type = " + valueType + "\n");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_GET(Expression_GETContext ctx) {
		
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
			String keyType = getDictKeyType(expr0);
			String factor = "";
			
			// get variable type for dictionary key conversion factor
			Variable key = mapCtxVar.get(ctx.expression(1));
			key = new Variable(key); // deep copy
			
			// key is numeric -> get conversion factor
			if (key.isNumeric()) {
				factor = " * " + key.convertTypeTo(typesTable.get(keyType)).toString();
			}
			
			operation = expr0Name + ".get(" + expr1Name + factor + ")";
		}
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(valueType, newName);
		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		newVariable.add("operation", operation);
		
		// create Variable and save ctx
		Variable var = new Variable(typesTable.get("number"), varType.NUMERIC, 1.0);
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - GET");
			ErrorHandling.printInfo("\t-> expr0 type = " + expr0.getAttribute("type"));
			ErrorHandling.printInfo("\t-> expr1 type = " + valueType + "\n");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_CONTAINS(Expression_CONTAINSContext ctx) {
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("boolean", newName);
		
		// add previous statements
		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		
		// get var names to complete operation and add it
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		newVariable.add("operation", expr0Name + ".contains(" + expr1Name + ")");
		
		// create Variable and save ctx
		Variable var = new Variable(null, varType.BOOLEAN, true); // the boolean value is not relevant for compilation
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - CONTAINS\n");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_CONTAINSKEY(Expression_CONTAINSKEYContext ctx) {
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("boolean", newName);

		// add previous statements
		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		
		// get var names to complete operation and add it
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		newVariable.add("operation", expr0Name + ".containsKey(" + expr1Name + ")");
		
		// create Variable and save ctx
		Variable var = new Variable(null, varType.BOOLEAN, true); // the boolean value is not relevant for compilation
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - CONTAINS KEY\n");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_CONTAINSVALUE(Expression_CONTAINSVALUEContext ctx) {

		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("boolean", newName);

		// add previous statements
		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		
		// get var names to complete operation and add it
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		newVariable.add("operation", expr0Name + ".containsValue(" + expr1Name + ")");
		
		// create Variable and save ctx
		Variable var = new Variable(null, varType.BOOLEAN, true); // the boolean value is not relevant for compilation
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - CONTAINS VALUE\n");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_INDEXOF(Expression_INDEXOFContext ctx) {
		
		// get expressions info
		ST expr0 = visit(ctx.expression(0));
		ST expr1 = visit(ctx.expression(1));
		
		// create new template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Double", newName);
		
		// add previous statements
		newVariable.add("previousStatements", expr0.render());
		newVariable.add("previousStatements", expr1.render());
		
		// get var names to complete operation and add it
		String expr0Name = (String) expr0.getAttribute("var");
		String expr1Name = (String) expr1.getAttribute("var");
		newVariable.add("operation", expr0Name + ".indeOf(" + expr1Name + ")");
		
		// create Variable and save ctx
		Variable var = new Variable (typesTable.get("number"), varType.NUMERIC, 1.0); // the value '1.0' is not relevant for compilation
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - INDEXOF\n");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitExpression_Var(Expression_VarContext ctx) {
		ST var = visit(ctx.var());
		mapCtxVar.put(ctx, mapCtxVar.get(ctx.var()));
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - VAR\n");
		}
		
		return var;
	}
	
	@Override
	public ST visitExpression_Value(Expression_ValueContext ctx) {
		ST value = visit(ctx.value());
		mapCtxVar.put(ctx, mapCtxVar.get(ctx.value()));
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - VALUE");
			ErrorHandling.printInfo("\t-> value = " + value.render() + "\n");
		}
		
		return value;
	}
	
	@Override
	public ST visitExpression_FunctionCall(Expression_FunctionCallContext ctx) {
		ST functionCall = visit(ctx.functionCall());
		mapCtxVar.put(ctx, mapCtxVar.get(ctx.functionCall()));
		
		if(debug) {
			ErrorHandling.printInfo("->EXPRESSION - FUNCTION CALL");
			ErrorHandling.printInfo("\t-> function name = " + (String) functionCall.getAttribute("functionName") + "\n");
		}
		
		return functionCall;
	}
	
	@Override
	public ST visitInputOutput(InputOutputContext ctx) {
		
		if(debug) {
			ErrorHandling.printInfo("->INPUT OUTPUT\n");
		}
		
		return super.visitChildren(ctx);
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	// INPUT OUTPUT----------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	

	@Override
	public ST visitPrint(PrintContext ctx) {
		
		ST expr = visit(ctx.expression());
		
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
		String varName = (String) expr.getAttribute("var");
		print.add("expression", varName);
			
		if(debug) {
			ErrorHandling.printInfo(ctx,"-> PRINT\n");
		}
		
		return print;
	}

	// TODO implement...
	@Override
	public ST visitSave(SaveContext ctx) {
		
		if(debug) {
			ErrorHandling.printInfo("->SAVE\n");
		}
		
		return super.visitSave(ctx);
	}
	
	// TODO cry.... implement... cry again...
	@Override
	public ST visitInput(InputContext ctx) {
		
		if(debug) {
			ErrorHandling.printInfo("->INPUT\n");
		}
		
		// TODO Auto-generated method stub
		return super.visitInput(ctx);
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	// VARS AND TYPES------------------------------------------------------------------------------------------------------ 
	// --------------------------------------------------------------------------------------------------------------------

	@Override
	public ST visitVar(VarContext ctx) {
		
		// get var info
		String id = ctx.ID().getText();
		String lastName = symbolTableNames.get(id);
		Variable var = symbolTableValue.get(lastName);
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST(getVarTypeDeclaration(var) , newName , lastName);
		
		// create Variable and save ctx
		var = new Variable(var); // deep copy
		symbolTableValue.put(newName, var);
		symbolTableNames.put(id, newName);
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->VAR\n");
		}
		
		return newVariable;
	}

	@Override
	public ST visitVarDeclaration_Variable(VarDeclaration_VariableContext ctx) {
		
		// get varDeclaration info
		ST type = visit(ctx.type());
		String originalName = ctx.ID().getText();
		String newName = getNewVarName();
		
		// create varDeclaration ST
		ST varDeclaration = stg.getInstanceOf("varDeclaration");
		varDeclaration.add("type", type);
		varDeclaration.add("var", newName);
		
		// update variable names table
		symbolTableNames.put(originalName, newName);

		// create Variable and save ctx
		Variable var = mapCtxVar.get(ctx.type()); // type already contains information necessary to create variable
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->VARDECLARATION - VARIABLE");
			ErrorHandling.printInfo("\t-> decl type = " + type.toString() + "\n");
		}
		
		return varDeclaration;
	}

	@Override
	public ST visitVarDeclaration_list(VarDeclaration_listContext ctx) {
		
		// get varDeclaration info
		String listType = ctx.ID(0).getText();
		String originalName = ctx.ID(1).getText();
		String newName = getNewVarName();
		
		// create varDeclaration ST
		ST newVariable = varAssignmentST(getListDeclaration(listType), newName, "new ArrayList<>()");
		
		// update variable names table
		symbolTableNames.put(originalName, newName);
		
		// create Variable and save ctx
		ListVar listVar = new ListVar(ctx.ID(0).getText(), true);
		Variable var = new Variable(null, varType.LIST, listVar);
		mapCtxVar.put(ctx,  var);
		
		if(debug) {
			ErrorHandling.printInfo("->VARDECLARATION - LIST\n");
		}
		
		return newVariable;
	}

	@Override
	public ST visitVarDeclaration_dict(VarDeclaration_dictContext ctx) {
		
		// get varDeclaration info
		String keyType = ctx.ID(0).getText();
		String valueType = ctx.ID(1).getText();
		String originalName = ctx.ID(2).getText();
		String newName = getNewVarName();
		
		// create varDeclaration ST
		ST newVariable = varAssignmentST(getDictDeclaration(keyType, valueType), newName, "new HashMap<>()");
		
		// update variable names table
		symbolTableNames.put(originalName, newName);
		
		// create Variable and save ctx
		DictVar dictVar = new DictVar(ctx.ID(0).getText(), true, ctx.ID(1).getText(), true); // boolean value is not relevante for compilation
		Variable var = new Variable(null, varType.DICT, dictVar);
		mapCtxVar.put(ctx,  var);
		
		if(debug) {
			ErrorHandling.printInfo("->VARDECLARATION - LIST\n");
		}
		
		return newVariable;
	}

	@Override
	public ST visitType_Number_Type(Type_Number_TypeContext ctx) {
		
		// create template
		ST type = stg.getInstanceOf("type");
		type.add("type", "number");
		
		//create Variable and save ctx
		Variable var = new Variable (typesTable.get("number"), varType.NUMERIC, 0.0);
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->TYPE - NUMBER_TYPE\n");
		}
		
		return type;
	}

	@Override
	public ST visitType_Boolean_Type(Type_Boolean_TypeContext ctx) {
		
		// create template
		ST type = stg.getInstanceOf("type");
		type.add("type", "Boolean");
		
		//create Variable and save ctx
		Variable var = new Variable (null, varType.BOOLEAN, false);
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->TYPE BOOLEAN_TYPE\n");
		}
		
		return type;
	}

	@Override
	public ST visitType_String_Type(Type_String_TypeContext ctx) {
		
		// create template
		ST type = stg.getInstanceOf("type");
		type.add("type", "String");
		
		// create Variable and save ctx
		Variable var = new Variable (null, varType.STRING, "");
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->TYPE STRING_TYPE\n");
		}
		
		return type;
	}
	
	@Override
	public ST visitType_Void_Type(Type_Void_TypeContext ctx) {
		
		// create template
		ST type = stg.getInstanceOf("type");
		type.add("type", "void");
		
		// create Variable and save ctx
		Variable var = new Variable (null, varType.VOID, null);
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->TYPE VOID_TYPE\n");
		}
		
		return type;
	}

	@Override
	public ST visitType_ID_Type(Type_ID_TypeContext ctx) {
		
		// create template
		ST type = stg.getInstanceOf("type");
		type.add("type", "id");
		
		// create Variable and save ctx
		Variable var = new Variable (typesTable.get(ctx.ID().getText()), varType.NUMERIC, 0.0);
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo("->TYPE ID_TYPE\n");
		}
		
		return type;
	}
	
	@Override
	public ST visitValue_Number(Value_NumberContext ctx) {
		
		// get number info
		String number = ctx.NUMBER().getText();
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("double", newName, number); 
		
		// create Variable and save ctx
		Variable var = new Variable(typesTable.get("number"), varType.NUMERIC, Double.parseDouble(number));
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"-> VALUE NUMBER");
			ErrorHandling.printInfo(ctx,"\t-> value = " + ctx.NUMBER().getText() + "\n");
		}
		
		return newVariable;
	}

	@Override
	public ST visitValue_Boolean(Value_BooleanContext ctx) {
		
		// get boolean info
		Boolean b = Boolean.parseBoolean(ctx.BOOLEAN().getText());
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("boolean", newName, b+""); 
		
		// create Variable and save ctx
		Variable var = new Variable(null, varType.BOOLEAN, b);
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"-> VALUE BOOLEAN");
			ErrorHandling.printInfo(ctx,"\t-> boolean = " + ctx.BOOLEAN().getText() + "\n");
		}
		
		return newVariable;
	}

	@Override
	public ST visitValue_String(Value_StringContext ctx) {
		
		// string is returned with quotation marks included
		String str = ctx.STRING().getText();
		
		// create template
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("string", newName, str);
		
		// create Variable and save ctx
		Variable var = new Variable(null, varType.STRING, getStringText(str));
		mapCtxVar.put(ctx, var);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"-> VALUE STRING");
			ErrorHandling.printInfo(ctx,"\t-> string = " + ctx.STRING().getText() + "\n");
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
	
	/**
	 * trims de quotes of a lexer string
	 * @param str
	 * @return
	 */
	private static String getStringText(String str) {
		str = str.substring(1, str.length() -1);
		return str;
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
			if (!key.equals("boolean") && !key.equals("string")) val = "Double";
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
		String type = decl.split(",")[0].substring(5, decl.length());
		if (type.equals("string")) return "String";
		else if (type.equals("boolean")) return "Boolean";
		else return "Double";
	}
	
	private static String getDictValueType(ST exprST) {
		String decl = ((String) exprST.getAttribute("type"));
		String type = decl.split(",")[1].substring(1, decl.length()-1);
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
		if (((String)exprST.getAttribute("type")).contains("String"))
				return true;
		return false;
	}
	
	private static boolean typeIsBoolean(ST exprST) {
		if (((String)exprST.getAttribute("type")).contains("Boolean"))
				return true;
		return false;
	}
	
	private static boolean typeIsEntry(ST exprST) {
		if (((String)exprST.getAttribute("type")).contains("Entry"))
				return true;
		return false;
	}
	
	private static boolean typeIsDouble(ST exprST) {
		if (((String)exprST.getAttribute("type")).contains("Double"))
				return true;
		return false;
	}
	
	/**
	 * Creates new varType Enum using the equivalent types names from Potatoes Language
	 * @param str
	 * @return
	 */
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

