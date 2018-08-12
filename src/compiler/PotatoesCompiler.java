package compiler;

import utils.*;
import utils.errorHandling.ErrorHandling;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.stringtemplate.v4.*;

import potatoesGrammar.grammar.PotatoesBaseVisitor;
import potatoesGrammar.grammar.PotatoesParser.*;
import potatoesGrammar.utils.Variable;
import potatoesGrammar.utils.varType;
import typesGrammar.grammar.TypesFileInfo;
import typesGrammar.utils.Code;
import typesGrammar.utils.Type;

/**
 * * <b>PotatoesCompiler</b><p>
 * 
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class PotatoesCompiler extends PotatoesBaseVisitor<ST> {
	
	protected static STGroup stg = null;
	//protected static ParseTreeProperty<Object> mapCtxObj = PotatoesSemanticCheck.getMapCtxObj();

	protected static Map<String, String> symbolTableName = new HashMap<>();  // stores the updated name of variables
	protected static Map<String, Object> symbolTableValue = new HashMap<>(); // stores the updated value of variables
	
	private static TypesFileInfo typesFileInfo;
	private static Map<String, Type> typesTable;
	
	private static int varCounter = 0;
	
	private static final boolean debug = false;
	
	// --------------------------------------------------------------------------------------------------------------------
	// MAIN RULES----------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------
	
	
	@Override
	public ST visitProgram(ProgramContext ctx) {
		stg = new STGroupFile("java.stg");
	    ST classContent = stg.getInstanceOf("class");
	    visit(ctx.using());
	    for(CodeContext context : ctx.code()) {
	    	classContent.add("stat", visit(context));
	    }
	    return classContent;
	}
	
	@Override
	public ST visitUsing(UsingContext ctx) {
		String str = ctx.STRING().getText();
		String path = str.substring(1, str.length() -1);
		typesFileInfo = new TypesFileInfo(path);
		typesTable = typesFileInfo.getTypesTable();
		return visitChildren(ctx);
	}
	
	@Override
	public ST visitCode_Declaration(Code_DeclarationContext ctx) {
		ST varDeclaration = visit(ctx.varDeclaration());
		return createEOL(varDeclaration);
	}
	
	@Override
	public ST visitCode_Assignment(Code_AssignmentContext ctx) {
		return visit(ctx.assignment());
	}

	@Override
	public ST visitCode_Function(Code_FunctionContext ctx) {
		return visit(ctx.function());
	}
	
	@Override
	public ST visitScope(ScopeContext ctx) {
		// Visit all statement rules
		ST scopeContent = stg.getInstanceOf("scope");
		for (StatementContext stat : ctx.statement()) {
			scopeContent.add("stat", visit(stat));
		}
		return scopeContent;
	}
	
	// --------------------------------------------------------------------------------------------------------------------	
	// CLASS - STATEMENTS--------------------------------------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------	
	@Override
	public ST visitStatement_Declaration(Statement_DeclarationContext ctx) {
		ST varDeclaration = visit(ctx.varDeclaration());
		return createEOL(varDeclaration);
	}
	
	@Override
	public ST visitStatement_Assignment(Statement_AssignmentContext ctx) {
		return visit(ctx.assignment());
	}
	
	@Override
	public ST visitStatement_Control_Flow_Statement(Statement_Control_Flow_StatementContext ctx) {
		return visit(ctx.controlFlowStatement());
	}
	
	@Override
	public ST visitStatement_FunctionCall(Statement_FunctionCallContext ctx) {
		return visit(ctx.functionCall());
	}
	
	@Override
	public ST visitStatement_Function_Return(Statement_Function_ReturnContext ctx) {
		return visit(ctx.functionReturn());
	}

	@Override
	public ST visitStatement_Print(Statement_PrintContext ctx) {
		return visit(ctx.print());
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	// CLASS - ASSIGNMENTS-----------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	@Override
	public ST visitAssignment_Var_Declaration_Not_Boolean(Assignment_Var_Declaration_Not_BooleanContext ctx) {
		
		// get varDeclaration info
		ST varDeclaration =  visit(ctx.varDeclaration());
		String type = (String)varDeclaration.getAttribute("type");
		String varOriginalName = ctx.varDeclaration().ID().getText();
		String varNewName = (String) varDeclaration.getAttribute("var");
		
		//get var info
		String varOpOriginalName = ctx.var().getText();
		String varOpName = symbolTableName.get(varOpOriginalName);
		
		// create assignment ST
		ST assignment = varAssignmentST(type, varNewName, "! "+varOpName); 	
		
		// update
		updateSymbolTables(varOriginalName, varNewName, !(Boolean)getValueFromSymbolsTable(varOpName));
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> Assignment_Var_Declaration_Not_Boolean");
			ErrorHandling.printInfo(ctx,"\t-> assignment = "+assignment.render());
			ErrorHandling.printInfo(ctx,"");
		}
		
		return assignment;
	}
	
	@Override
	public ST visitAssignment_Var_Declaration_Value(Assignment_Var_Declaration_ValueContext ctx) {
		
		// get varDeclaration info
		ST varDeclaration =  visit(ctx.varDeclaration());
		String type = (String) varDeclaration.getAttribute("type");
		String varOriginalName = ctx.varDeclaration().ID().getText();	
		String varNewName = (String) varDeclaration.getAttribute("var");
		
		// get value info
		String value = ctx.value().getText();
		
		// create assignment ST
		ST assignment = varAssignmentST(type, varNewName); 
		
		// do calculations and update ST accordingly
		Object obj = null;
		if(type.equals("Double")) {
			ST valueST = visit(ctx.value());
			assignment.add("operation",valueST.getAttribute("operation"));
			
			Variable temp = (Variable) getValueFromSymbolsTable((String)valueST.getAttribute("var"));
			obj = new Variable(temp);
		}
		else if(type.equals("String")) {
			obj = value;
			assignment.add("operation", (String)obj);
		}
		else { //typeValue.equals("Boolean")
			obj = Boolean.parseBoolean((ctx.value().getText()));
			assignment.add("operation", (Boolean)obj);
		}
		
		// update tables
		updateSymbolTables(varOriginalName, varNewName, obj);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitAssignment_Var_Declaration_Value");
			ErrorHandling.printInfo(ctx,"\t-> type = "+type);
			ErrorHandling.printInfo(ctx,"\t-> assignment = "+assignment.render());
			ErrorHandling.printInfo(ctx,"");
		}
	
		return assignment;
	}
	
	@Override
	public ST visitAssignment_Var_Declaration_Comparison(Assignment_Var_Declaration_ComparisonContext ctx) {
			
		// get varDeclaration info
		ST varDeclaration =  visit(ctx.varDeclaration());
		String type = (String) varDeclaration.getAttribute("type");
		String originalName = ctx.varDeclaration().ID().getText();
		String newVarName = (String) varDeclaration.getAttribute("var");
		
		// get comparison info
		ST comparison = visit(ctx.comparison());
		String previousDec =  comparison.render();
		String resultVarName = (String) comparison.getAttribute("var");
		
		// create ST
		ST assignment = varAssignmentST(previousDec, type, newVarName, resultVarName);
		
		// update tables
		updateSymbolTables(originalName, newVarName, getValueFromSymbolsTable(resultVarName));
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitAssignment_Var_Declaration_Comparison");
			ErrorHandling.printInfo(ctx,"\t-> assignment = "+assignment.render());
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"");
		}
		
		return assignment;
	}
	
	@Override
	public ST visitAssignment_Var_Declaration_Operation(Assignment_Var_Declaration_OperationContext ctx) {
		/* parser rule -> assignment : varDeclaration '=' operation */
		
		// get varDeclaration info
		ST varDeclaration =  visit(ctx.varDeclaration());
		String type = (String) varDeclaration.getAttribute("type");
		String typeName = ctx.varDeclaration().type().getText();
		
		String originalName = ctx.varDeclaration().ID().getText();
		String newVarName = (String) varDeclaration.getAttribute("var");
		
		//get operation info
		ST operation =  visit(ctx.operation());
		String resultVarName = (String) operation.getAttribute("var");
		
		//create ST
		ST assignment = varAssignmentST(type, newVarName);
		assignment.add("stat", operation);
		
		// do calculations and update ST accordingly
		if(!typeName.equals("number") && !typeName.equals("boolean") && !typeName.equals("string")) {
			Variable temp = (Variable)getValueFromSymbolsTable(resultVarName);
			Variable a = new Variable(temp);
			Variable b = (Variable) getValueFromSymbolsTable(newVarName);
			a.convertTypeTo(b.getType());
			Double factor = Variable.pathCost(a, b);
			assignment.add("operation", resultVarName+"*"+factor);
		}
		else {
			assignment.add("operation", resultVarName);
		}
		
		// update tables
		updateSymbolTables(originalName, newVarName, getValueFromSymbolsTable(resultVarName));
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitAssignment_Var_Declaration_Operation");
			ErrorHandling.printInfo(ctx,"\t-> assignment:\n"+assignment.render());
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"");
		}
		
		return assignment;	
	}

	@Override
	public ST visitAssignment_Var_Declaration_FunctionCall(Assignment_Var_Declaration_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}
	
	@Override
	public ST visitAssignment_Var_Not_Boolean(Assignment_Var_Not_BooleanContext ctx) {
		
		//get var info
		String originalName = ctx.var(0).getText();	
		String varNewName = symbolTableName.get(originalName); //getNewVarName();
		
		// get varOp info
		String varOpOriginalName = ctx.var(1).getText();
		String varOpName = symbolTableName.get(varOpOriginalName); //getNewVarName();
		
		//create ST
		ST assignment = stg.getInstanceOf("varAssignment");
		assignment.add("var", varNewName);
		assignment.add("operation",  "!"+varOpName);
		
		symbolTableValue.put(varNewName, !((Boolean)getValueFromSymbolsTable(varOpOriginalName)));
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitAssignment_Var_Not_Boolean");
			ErrorHandling.printInfo(ctx,"\t-> assignment = "+assignment.render());
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"");
		}
		
		return assignment;
	}
	
	@Override
	public ST visitAssignment_Var_Value(Assignment_Var_ValueContext ctx) {
		
		// get the var info
		String originalName = ctx.var().getText();
		String varNewName = symbolTableName.get(originalName);  //getNewVarName();
		
		// create ST
		ST assignment = stg.getInstanceOf("varAssignment");
		assignment.add("var", varNewName);
		
		// do calculations and update ST accordingly
		Object typeValue = getValueFromSymbolsTable(originalName);
		
		if(typeValue instanceof Boolean) {
			Boolean b = Boolean.parseBoolean((ctx.value().getText()));
			assignment.add("operation", b);
			symbolTableValue.put(varNewName, b);
		}
		else if(typeValue instanceof String) {
			String str = ctx.value().getText();
			assignment.add("operation", str);
			symbolTableValue.put(varNewName, str);
		}
		else { // typeValue instanceof Variable (number)
			ST valueST = visit(ctx.value());
			
			String operation = (String)valueST.getAttribute("operation");
			assignment.add("operation", operation);	
			
			Variable temp = (Variable) getValueFromSymbolsTable((String)valueST.getAttribute("var"));
			Variable a = new Variable(temp);
			Variable b = (Variable) getValueFromSymbolsTable(originalName);
			
			a.convertTypeTo(b.getType());
			
			double factor = Variable.pathCost(a, b);
			
			symbolTableValue.put(varNewName, a+"*"+factor);
		}

		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitAssignment_Var_Value");
			ErrorHandling.printInfo(ctx,"\t-> assignment = "+assignment.render());
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"");
		}
	
		return assignment;
	}
	
	@Override
	public ST visitAssignment_Var_Comparison(Assignment_Var_ComparisonContext ctx) {
		
		//get var info
		String originalName = ctx.var().getText();
		String varNewName = symbolTableName.get(originalName);// getNewVarName();
				
		//get comparison info
		ST comparison = visit(ctx.comparison());
		String previousDec =  comparison.render();
		String comparisonVarName = (String) comparison.getAttribute("var");
		
		//create ST
		ST assignment = varAssignmentST(previousDec, "", varNewName, comparisonVarName);
		
		// update tables
		symbolTableValue.put(varNewName, getValueFromSymbolsTable(comparisonVarName));
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitAssignment_Var_Comparison");
			ErrorHandling.printInfo(ctx,"\t-> comparison = "+comparison.render());
			ErrorHandling.printInfo(ctx,"\t-> assignment = "+assignment.render());
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"");
		}
		
		return assignment;
	}
	
	@Override
	public ST visitAssignment_Var_Operation(Assignment_Var_OperationContext ctx) {
		/* parser rule -> assignment : var '=' operation */	
		
		//get var info
		String originalName = ctx.var().getText();
		String newVarName = symbolTableName.get(originalName);//getNewVarName();
		
		//get operation info
		ST operation =  visit(ctx.operation());
		String operationName = (String) operation.getAttribute("var");
		
		//create a ST for this assignment
		ST assignment = stg.getInstanceOf("varAssignment");
		assignment.add("var", newVarName);
		assignment.add("stat", operation.render());

		// do the calculations and final updates to ST
		Object obj = getValueFromSymbolsTable(operationName);
		
		if(obj instanceof Boolean) {
			assignment.add("operation", operationName);
			symbolTableValue.put(newVarName, obj);
		}
		else if(obj instanceof String) {
			assignment.add("operation", operationName);
			symbolTableValue.put(newVarName, obj);
		}
		else { // obj instanceof Variable
			
			Variable temp = (Variable) obj;
			Variable a = new Variable(temp);
			Variable b = (Variable) getValueFromSymbolsTable(originalName);
				
			a.convertTypeTo(b.getType());	
			double factor = Variable.pathCost(a, b);
			
			assignment.add("operation", operationName+"*"+factor);
			
			// update tables
			symbolTableValue.put(newVarName, a);
			
		}
	
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitAssignment_Var_Operation");
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"");
		}
		
		return assignment;
	}
	
	@Override
	public ST visitAssingment_Var_FunctionCall(Assingment_Var_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}
	
	// --------------------------------------------------------------------------------------------------------------------	
	// FUNCTIONS-----------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	@Override
	public ST visitFunction_Main(Function_MainContext ctx) {
		ST main = stg.getInstanceOf("main");
		main.add("scope", visit(ctx.scope()));
		return main;
	}
	
	@Override
	public ST visitFunctionReturn(FunctionReturnContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	@Override
	public ST visitFunctionCall(FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}
			
	// --------------------------------------------------------------------------------------------------------------------
	// CONTROL FLOW STATMENTS----------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	@Override
	public ST visitControlFlowStatement(ControlFlowStatementContext ctx) {
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitControlFlowStatement");
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"");
		}
		
		return visitChildren(ctx);
	}

	@Override
	public ST visitForLoop(ForLoopContext ctx) {
		/* parser rule -> forLoop : FOR '(' assignment? EOL logicalOperation EOL assignment ')' scope */
		
		// get first assignment info
		ST firstAssignment = null;
		int size = ctx.assignment().size();
		if(size!=1){// FOR '('assignment EOL logicalOperation EOL assignment ')'
			firstAssignment = visit(ctx.assignment(0));
		}
		
		// get logical operation info
		ST logicalOperation = visit(ctx.logicalOperation());
		//forLoop.add("outsideStatements", "//"+ctx.logicalOperation().getText());
		String booleanVarName = (String) logicalOperation.getAttribute("var");
		
		// get scope info
		ST scope = visit(ctx.scope());
		
		// update scope with assignment updates and finalAssignments
		scope.add("stat", "\n//finalAssignment actualization");
		ST lastAssignment = null;
		if(size==1) {// FOR '(' EOL logicalOperation EOL assignment ')'
			lastAssignment = visit(ctx.assignment(0));
			scope.add("stat",lastAssignment.render() );
		}
		else {// FOR '('assignment EOL logicalOperation EOL assignment ')'
			lastAssignment = visit(ctx.assignment(1));
			scope.add("stat", lastAssignment.render());
		}
		
		ST logicalOperation1 = visit(ctx.logicalOperation()); // have to visit again
		//scope.add("stat", "//"+ctx.logicalOperation().getText());
		scope.add("stat", logicalOperation1.render());
		String booleanVarName1 = (String) logicalOperation1.getAttribute("var");
		String reAssignment = booleanVarName + "=" + booleanVarName1 + ";";
		scope.add("stat", reAssignment);
		
		//create ST
		ST forLoop = stg.getInstanceOf("forLoop");
		if(size!=1) forLoop.add("outsideStatements", firstAssignment.render());
		forLoop.add("outsideStatements", logicalOperation.render());
		forLoop.add("logicalOperation", "!"+booleanVarName);
		forLoop.add("scope", scope);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitForLoop");
			ErrorHandling.printInfo(ctx,"\t-> forLoop.render()\n"+forLoop.render());
			ErrorHandling.printInfo(ctx,"------------------------------------------------");
			ErrorHandling.printInfo(ctx,"");
		}
		
		return forLoop;
	}

	@Override
	public ST visitWhileLoop(WhileLoopContext ctx) {
		/* parser rule -> whileLoop : WHILE '(' logicalOperation ')' scope */
		
		// get logical operation info
		ST logicalOperation = visit(ctx.logicalOperation());
		String booleanVarName = (String) logicalOperation.getAttribute("var");
		
		// get scope info and create scope ST
		ST scope = visit(ctx.scope());
		
		// add update of logical operation to scope
		ST logicalOperation1 = visit(ctx.logicalOperation()); // have to visit again
		//scope.add("content", "//"+ctx.logicalOperation().getText());
		scope.add("content", logicalOperation1);
		String booleanVarName1 = (String) logicalOperation1.getAttribute("var");
		String reAssignment = booleanVarName + "=" + booleanVarName1 + ";";
		scope.add("content", reAssignment);
		
		// create ST
		ST whileLoop = stg.getInstanceOf("whileLoop");
		whileLoop.add("previousStatements", logicalOperation);
		whileLoop.add("logicalOperation", booleanVarName);
		whileLoop.add("scope", scope);

		return whileLoop;
	}
	
	@Override
	public ST visitCondition_withoutElse(Condition_withoutElseContext ctx) {
		/* parser rule -> ifCondition elseIfCondition* */
		
		// get condition info
		ST ifCondition = visit(ctx.ifCondition());
		ST elseIfCondition = stg.getInstanceOf("stats");
		
		for(ElseIfConditionContext context : ctx.elseIfCondition()) {
			ST temp = visit(context);
			String previousStatements = (String) temp.getAttribute("previousStatements");
			ifCondition.add("previousStatements", previousStatements);
			elseIfCondition.add("stat", temp.render().substring(previousStatements.length()));
		}
		
		// create ST
		ST condition = stg.getInstanceOf("stats");
		condition.add("stat", ifCondition.render());
		condition.add("stat", elseIfCondition.render());
		
		return condition;
	}

	@Override
	public ST visitCondition_withElse(Condition_withElseContext ctx) {
		/* parser rule -> ifCondition elseIfCondition* elseCondition */
		
		// get conditions info
		ST ifCondition = visit(ctx.ifCondition());
		ST elseIfCondition = stg.getInstanceOf("stats"); // have to move previous statements to ifCondition
		ST elseCondition = visit(ctx.elseCondition());
		
		for(ElseIfConditionContext context : ctx.elseIfCondition()) {
			ST temp = visit(context);
			String previousStatements = (String) temp.getAttribute("previousStatements");
			ifCondition.add("previousStatements", previousStatements);
			elseIfCondition.add("stat", temp.render().substring(previousStatements.length()));
		}
			
		// create ST
		ST condition = stg.getInstanceOf("stats");
		condition.add("stat", ifCondition.render());
		condition.add("stat", elseIfCondition.render());
		condition.add("stat", elseCondition.render());
		
		return condition;
	}

	@Override 
	public ST visitIfCondition(IfConditionContext ctx) { 
		/* parser rule -> ifCondition : IF '(' logicalOperation ')' scope */

		// get logicalOperation info
		ST logicalOperation = visit(ctx.logicalOperation());
		
		// create ST
		ST ifCondition = stg.getInstanceOf("ifCondition");
		ifCondition.add("previousStatements", logicalOperation.render());
		ifCondition.add("logicalOperation", logicalOperation.getAttribute("operation"));
		ifCondition.add("scope", visit(ctx.scope()));
		// FIXME visit(ctx.scope()).render() ??????
				
		return ifCondition;
	}

	@Override 
	public ST visitElseIfCondition(ElseIfConditionContext ctx) {
		/* parser rule -> elseIfCondition : ELSE IF '(' logicalOperation ')' scope */
		
		// get logicalOperation info
		ST logicalOperation = visit(ctx.logicalOperation());
		
		// create ST
		ST elseIfCondition = stg.getInstanceOf("elseIfCondition");
		elseIfCondition.add("previousStatements", logicalOperation.render());
		elseIfCondition.add("logicalOperation", logicalOperation.getAttribute("operation"));
		elseIfCondition.add("scope", visit(ctx.scope()));
		
		return elseIfCondition;
	}

	@Override 
	public ST visitElseCondition(ElseConditionContext ctx) {
		/* parser rule -> elseCondition : ELSE scope */
		
		// create ST
		ST elseCondition = stg.getInstanceOf("elseCondition");
		elseCondition.add("scope", visit(ctx.scope()));
		
		return elseCondition;
	}

	// --------------------------------------------------------------------------------------------------------------------
	// LOGICAL OPERATIONS--------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------
	
	@Override
	public ST visitLogicalOperation_Parenthesis(LogicalOperation_ParenthesisContext ctx) {
		/* parser rule -> logicalOperation : '(' logicalOperation ')' */
		return visit(ctx.logicalOperation());
	}
	
	@Override
	public ST visitLogicalOperation_Operation(LogicalOperation_OperationContext ctx) {
		/* parser rule -> logicalOperation : logicalOperation op=('&&' | '||') logicalOperation */
		
		// get operands info
		ST op0 = visit(ctx.logicalOperation(0));
		ST op1 = visit(ctx.logicalOperation(1));
		String nameVar0 = (String) op0.getAttribute("var");
		String nameVar1 = (String) op1.getAttribute("var");
		
		// get operator info
		String op = ctx.op.getText();
		
		// create ST
		String newName = getNewVarName();
		String logicalOperation = nameVar0 + op + nameVar1;
		ST assign = varAssignmentST("boolean", newName, logicalOperation);
		assign.add("previousStatements", op0.render());
		assign.add("previousStatements", op1.render());
		
		// create variables and do calculations
		Boolean b0 = (Boolean) getValueFromSymbolsTable(nameVar0);
		Boolean b1 = (Boolean) getValueFromSymbolsTable(nameVar1);
		
		// update tables
		symbolTableValue.put( newName, getLogicOperationResult(b0,b1,op));
		
		return assign;
	}
	
	@Override
	public ST visitLogicalOperation_logicalOperand(LogicalOperation_logicalOperandContext ctx) {
		/* parser rule -> logicalOperation : logicalOperand */
		return visit(ctx.logicalOperand());
	}
	
	@Override
	public ST visitLogicalOperand_Comparison(LogicalOperand_ComparisonContext ctx) {
		/* parser rule -> logicalOperand : comparison */
		return visit(ctx.comparison());
	}
	
	@Override
	public ST visitLogicalOperand_Not_Comparison(LogicalOperand_Not_ComparisonContext ctx) {
		/* parser rule -> logicalOperand : '!' comparison */
		
		// get comparison info
		ST comparison = visit(ctx.comparison());
		String previousDec =  comparison.render();
		String comparisonVarName = (String) comparison.getAttribute("var");
		
		// create ST
		String newName = getNewVarName();
		ST assignment = varAssignmentST(previousDec, "boolean", newName, "!"+comparisonVarName);
		
		// update tables
		symbolTableValue.put(newName, !((Boolean)getValueFromSymbolsTable(comparisonVarName)));
		
		return assignment;
	}
	
	@Override
	public ST visitLogicalOperand_Var(LogicalOperand_VarContext ctx) {
		/* parser rule -> logicalOperand : var */
		
		// get var info
		String originalName = ctx.var().getText();
		
		// create ST
		String resultVarName = symbolTableName.get(originalName);
		String newName = getNewVarName();
		ST assignment = varAssignmentST("boolean", newName, resultVarName);
		
		// update tables
		symbolTableValue.put(newName, getValueFromSymbolsTable(resultVarName));
		
		return assignment;
	}
	
	@Override
	public ST visitLogicalOperand_Not_Var(LogicalOperand_Not_VarContext ctx) {
		/* parser rule -> logicalOperand : '!' var */

		// get var info
		String originalName = ctx.var().getText();
		
		// create ST
		String resultVarName = symbolTableName.get(originalName);
		String newName = getNewVarName();
		ST assignment = varAssignmentST("boolean", newName, "!"+resultVarName);
		
		// update tables
		symbolTableValue.put(newName, !((Boolean) getValueFromSymbolsTable(resultVarName)));
		
		return assignment;
	}
	
	@Override
	public ST visitLogicalOperand_Value(LogicalOperand_ValueContext ctx) {
		/* parser rule -> logicalOperand : value */
		
		// get value info
		boolean value = Boolean.parseBoolean(ctx.value().getText());
		
		// create ST
		String newName = getNewVarName();
		ST assignment = varAssignmentST("boolean", newName, value+"");
		
		// update tables
		symbolTableValue.put( newName, value);
		
		return assignment;
	}
	
	@Override
	public ST visitLogicalOperand_Not_Value(LogicalOperand_Not_ValueContext ctx) {
		/* parser rule -> logicalOperand : '!' value */
		
		// get value info
		boolean value = Boolean.parseBoolean(ctx.value().getText());
		
		// create ST
		String newName = getNewVarName();
		ST assignment = varAssignmentST("boolean", newName, "!"+value);
		
		// update tables
		symbolTableValue.put(newName, !value);
		
		return assignment;
	}
	
	@Override
	public ST visitComparison(ComparisonContext ctx) {
		/* parser rule -> comparison : compareOperation compareOperator compareOperation */
		
		// get operand info
		ST op0 = visit(ctx.compareOperation(0));
		ST op1 = visit(ctx.compareOperation(1));
		String varNameOp0 =  (String) op0.getAttribute("var");
		String varNameOp1 =  (String) op1.getAttribute("var");
		
		// get operator info
		String compareOp = ctx.compareOperator().getText();
		String comparison = varNameOp0 + compareOp + varNameOp1;
		if(compareOp.equals("==")) {
			comparison = varNameOp0 + ".equals(" + varNameOp1 + ")";
		}else if(compareOp.equals("!=")) {
			comparison = "!" + varNameOp0 + ".equals(" + varNameOp1 + ")";
		}else {
			comparison = varNameOp0 + compareOp + varNameOp1;
		}
		
		// create ST
		String varNewName = getNewVarName();
		ST assignment = varAssignmentST("boolean", varNewName, comparison);
		assignment.add("previousStatements",(String) op0.render());
		assignment.add("previousStatements",(String) op1.render());
		
		
		String typeOp0 = (String)op0.getAttribute("type"); // typeOp1 is boolean guaranteed by semantic check
		if(typeOp0.equals("Boolean")) {
			// create variables
			Boolean b0 = (Boolean) getValueFromSymbolsTable(varNameOp0);
			Boolean b1 = (Boolean) getValueFromSymbolsTable(varNameOp1);
			// do comparison
			boolean result = getBooleanResult(b0,b1,compareOp);
			// update tables
			symbolTableValue.put( varNewName, result);
		}
		else { //comparison between variables
			// create variables
			Variable temp = (Variable) getValueFromSymbolsTable(varNameOp0);
			Variable v0 = new Variable(temp);
			temp = (Variable) getValueFromSymbolsTable(varNameOp1);
			Variable v1 = new Variable(temp);
			// get conversion factor form v0 to v1
			double factor = Variable.pathCost(v0, v1);
			// do comparison
			boolean result = getBooleanResult(v0.getValue()*factor,v1.getValue(),compareOp);
			// update tables
			symbolTableValue.put(varNewName, result);
		}
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");;
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitComparison");
			ErrorHandling.printInfo(ctx,"\t-> assignment = "+assignment.render());
			ErrorHandling.printInfo(ctx,"");
		}
			
		return assignment;
	}	
	
	@Override
	public ST visitCompareOperation_Operation(CompareOperation_OperationContext ctx) {
		/* parser rule -> compareOperation : operation */
		return visit(ctx.operation());
	}

	@Override
	public ST visitCompareOperation_BOOLEAN(CompareOperation_BOOLEANContext ctx) {
		/* parser rule -> compareOperation : BOOLEAN */
		
		// get boolean info
		Boolean b = Boolean.parseBoolean(ctx.BOOLEAN().getText());
		
		// create ST
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Boolean", newName, b+""); 
		
		// update tables
		symbolTableValue.put( newName, b);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitCompareOperation_BOOLEAN");
			ErrorHandling.printInfo(ctx,"");
		}
		
		return newVariable;
	}

	@Override
	public ST visitCompareOperator(CompareOperatorContext ctx) {
		return visitChildren(ctx);
	}

	// --------------------------------------------------------------------------------------------------------------------
	// OPERATIONS----------------------------------------------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------

	@Override
	public ST visitOperation_Cast(Operation_CastContext ctx) {
		/* parser rule -> operation : cast operation */
		
		// get operation info
		ST op = visit(ctx.operation());
		String oldVarName = (String)op.getAttribute("var");
		 
		// create variable and get info
		Variable temp = (Variable) getValueFromSymbolsTable(oldVarName);
		Variable a = new Variable(temp);
		// get cast type
		String castType = (String) visit(ctx.cast()).getAttribute("value");
		// create dummy variable to get conversion factor
		Variable b = new Variable(typesTable.get(castType), 1);
		double factor = Variable.pathCost(a, b);
		
		// create ST
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Double", newName, oldVarName+"*"+factor); 
		newVariable.add("previousStatements", op.render());
		
		// do the calculations
		a.convertTypeTo(typesTable.get(castType));
		
		// update tables
		symbolTableValue.put( newName, a);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitOperation_Cast");
			ErrorHandling.printInfo(ctx,"\t-> oldVar = "+op.render());
			ErrorHandling.printInfo(ctx,"\t-> newVar = "+newVariable.render());
			ErrorHandling.printInfo(ctx,"");
		}
		
		return newVariable;
	}		
	
	@Override
	public ST visitOperation_Parenthesis(Operation_ParenthesisContext ctx) {
		return visit(ctx.operation());
	}                                                               
	
	@Override
	public ST visitOperation_Mult_Div_Mod(Operation_Mult_Div_ModContext ctx) {
		/* parser rule -> operation : operation op=('*' | '/' | '%') operation */
		
		// get operands info
		ST op0 = visit(ctx.operation(0));
		ST op1 = visit(ctx.operation(1));
		String op0Name = (String)op0.getAttribute("var");
		String op1Name = (String)op1.getAttribute("var");
		
		// create ST
		String newName = getNewVarName();
		ST newVariable = varAssignmentST( "Double", newName);
		newVariable.add("previousStatements", op0.render());
		newVariable.add("previousStatements", op1.render());
		
		// create variables and do calculations
		Variable temp = (Variable) getValueFromSymbolsTable(op0Name);
		Variable varOp0 = new Variable(temp);
		temp = (Variable) getValueFromSymbolsTable(op1Name);
		Variable varOp1 = new Variable(temp);

		String op = ctx.op.getText();
		Variable result = null;
		if (op.equals("%")) {
			// update ST
			newVariable.add("operation", op0Name + " % " + op1Name);
			//create variable
			result = Variable.mod(varOp0, varOp1);
		}
		else if (op.equals("*")) {
			// update ST
			newVariable.add("operation", op0Name + "*" + op1Name);
			// create variables and do calculations
			// TODO when this process is moved to the VAriable class, make necessary corrections
			result = Variable.multiply(varOp0, varOp1); 
			Code resCode = result.getType().getCode(); 
			Collection<Type> types = typesTable.values(); 
			for (Type t : types) { 
				if (t.getCode().equals(resCode)) {
					result = new Variable(typesTable.get(t.getTypeName()), result.getValue());
					break; 
				} 
			} 
		
			
		}
		else if (op.equals("/")) {
			//update ST
			newVariable.add("operation", op0Name + "/" + op1Name);
			//create variables and do calculations
			result = Variable.divide(varOp0, varOp1); 
			Code resCode = result.getType().getCode(); 
			Collection<Type> types = typesTable.values(); 
			for (Type t : types) { 
				if (t.getCode().equals(resCode)) { 
					result = new Variable(typesTable.get(t.getTypeName()), result.getValue());
					break; 
				} 
			} 
		}
		
		// update tables
		symbolTableValue.put(newName, result);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitOperation_Mult_Div_Mod");
			ErrorHandling.printInfo(ctx,"\t-> op0 = "+op0.render());
			ErrorHandling.printInfo(ctx,"\t-> op1 = "+op1.render());
			ErrorHandling.printInfo(ctx,"\t-> newVar = "+newVariable.render());
			ErrorHandling.printInfo(ctx,"");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitOperation_Simetric(Operation_SimetricContext ctx) {
		/* parser rule -> operation : '-' operation */
		
		// get operation info
		ST op = visit(ctx.operation());
		String previousVarName = (String) op.getAttribute("var");
		
		// create ST
		ST newVariable = varAssignmentST("Double", getNewVarName(), "- "+previousVarName); 
		String newName = (String) newVariable.getAttribute("var");
		newVariable.add("previousStatements", op.render());
		
		// create variable and do calculations
		Variable temp = (Variable) getValueFromSymbolsTable(previousVarName);
		Variable a = new Variable(temp);
		Variable.simetric(a);
		
		// update tables
		symbolTableValue.put( newName, a);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitOperation_Simetric");
			ErrorHandling.printInfo(ctx,"\t-> op0 = "+op.render());
			ErrorHandling.printInfo(ctx,"\t-> newVar = "+newVariable.render());
			ErrorHandling.printInfo(ctx,"");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitOperation_Add_Sub(Operation_Add_SubContext ctx) {
		
		// get operands info
		ST op0 = visit(ctx.operation(0));
		ST op1 = visit(ctx.operation(1));
		String op0Name = (String)op0.getAttribute("var");
		String op1Name = (String)op1.getAttribute("var");
		
		// create ST 
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Double", newName); 

		newVariable.add("previousStatements", op0.render());
		newVariable.add("previousStatements", op1.render());
		
		// create variables and do the calculations
		Variable temp = (Variable) getValueFromSymbolsTable(op0Name);
		Variable varOp0 = new Variable(temp);
		temp = (Variable) getValueFromSymbolsTable(op1Name);
		Variable varOp1 = new Variable(temp); 
		
		varOp1.convertTypeTo(varOp0.getType());
		Double factor = Variable.pathCost(varOp0, varOp1);
		
		Variable result = null;
		if (ctx.op.getText().equals("+")) {
			result = Variable.add(varOp0, varOp1);
			newVariable.add("operation", op0Name+"*"+factor + "+" + op1Name);
		}
		else {//if (ctx.op.getText().equals("-")) {
			result = Variable.subtract(varOp0, varOp1);
			newVariable.add("operation", op0Name+"*"+factor + "-" + op1Name);
		}
		
		symbolTableValue.put(newName, result);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitOperation_Add_Sub");
			ErrorHandling.printInfo(ctx,"\t-> newVar = "+newVariable.render());
			ErrorHandling.printInfo(ctx,"");
		}
		
		return newVariable;
		
	}

	@Override
	public ST visitOperation_Power(Operation_PowerContext ctx) {
		
		// get power operands ST
		ST op0 = visit(ctx.operation(0));
		ST op1 = visit(ctx.operation(1));
		String nameVar0 = (String) op0.getAttribute("var");
		String nameVar1 = (String) op1.getAttribute("var");
		
		// create ST
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Double", newName, "Math.pow(" + nameVar0 + "," + nameVar1 + ");"); 

		newVariable.add("previousStatements", op0.render());
		newVariable.add("previousStatements", op1.render());

		// create Variables
		Variable temp = (Variable) getValueFromSymbolsTable(nameVar0);
		Variable base = new Variable(temp);
		temp = (Variable) getValueFromSymbolsTable(nameVar1);
		Variable pow = new Variable(temp);
		
		symbolTableValue.put(newName, Variable.power(base, pow));
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitOperation_Power");
			ErrorHandling.printInfo(ctx,"\t-> op0 = "+op0.render());
			ErrorHandling.printInfo(ctx,"\t-> op1 = "+op1.render());
			ErrorHandling.printInfo(ctx,"\t-> newVar = "+newVariable.render());
			ErrorHandling.printInfo(ctx,"");
		}
		
		return newVariable;		
	}
	
	@Override
	public ST visitOperation_Var(Operation_VarContext ctx) {
		// get var info
		String varOpOriginalName = ctx.var().getText(); 
		String varOpNewName = symbolTableName.get(varOpOriginalName);	
		
		//create a ST for this assignment
		ST newVariable = stg.getInstanceOf("varAssignment");
		String newName = getNewVarName();
		newVariable.add("var", newName);
		
		// get Object
		Object value = getValueFromSymbolsTable(varOpOriginalName);
		
		// complete ST accordingly
		if(value instanceof Boolean) {
			newVariable.add("type", "Boolean");
			
		}
		else if(value instanceof String) {
			newVariable.add("type", "String");
		}
		else { // value instanceof Variable (number or unit)
			newVariable.add("type", "Double");
		}

		newVariable.add("operation", varOpNewName);
		
		// update tables
		symbolTableValue.put( newName, value);

		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitOperation_Var");
			ErrorHandling.printInfo(ctx,"\t-> varOpOriginalName = "+varOpOriginalName);
			ErrorHandling.printInfo(ctx,"\t-> varOpOriginalName = "+varOpNewName);
			ErrorHandling.printInfo(ctx,"\t-> newVar = "+newVariable.render());
			ErrorHandling.printInfo(ctx,"\t-> symbolTableName.get(varOpOriginalName) = "+symbolTableName.get(varOpOriginalName));
			ErrorHandling.printInfo(ctx,"\t-> getValueFromSymbolsTable(varOpOriginalName) = "+getValueFromSymbolsTable(varOpOriginalName));
			ErrorHandling.printInfo(ctx,"");
		}		
		
		return newVariable;
	}

	@Override
	public ST visitOperation_FunctionCall(Operation_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}
	
	@Override
	public ST visitOperation_NUMBER(Operation_NUMBERContext ctx) {
		// get NUMBER info
		String number = ctx.NUMBER().getText();
		
		// create the variable
		Variable numberVar = createNumberVariable(number);
		
		// create ST
		ST newVariable = varAssignmentST( "Double", getNewVarName(), numberVar.getValue()+"");
		
		// get new variable name and update tables
		String newName = (String) newVariable.getAttribute("var");
		symbolTableValue.put( newName, numberVar);
		
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitOperation_NUMBER");;
			ErrorHandling.printInfo(ctx,"\t-> newVar = "+newVariable.render());
			ErrorHandling.printInfo(ctx,"\t-> numberVar = " + numberVar);
			ErrorHandling.printInfo(ctx,"\t-> getValueFromSymbolsTable(newName) = "+(Variable) getValueFromSymbolsTable(newName));
			
			ErrorHandling.printInfo(ctx,"");
		}	
		
		return newVariable;
	}
	
	// FIXME this is repeated code, must be joined in one function
	@Override
	public ST visitPrint_Print(Print_PrintContext ctx) {
		//create printST
		ST print = stg.getInstanceOf("print");
		print.add("type", "print");
		
		// add all printVAr Sts to print ST
		int size = ctx.printVar().size();
		int i = 1;
		for(PrintVarContext toPrint : ctx.printVar()) {
			print.add("valueOrVarList", visit(toPrint).render());
			if(i<size) print.add("valueOrVarList", "+");
			i++;
		}
			
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitPrint_Print");
			ErrorHandling.printInfo(ctx,"\t-> print = "+ print.render());
			ErrorHandling.printInfo(ctx,"");
		}
		
		return print;
	}
	
	@Override
	public ST visitPrint_Println(Print_PrintlnContext ctx) {
		// create print ST
		ST print = stg.getInstanceOf("print");
		print.add("type", "println");
		
		// add all printVar STs to print ST
		int size = ctx.printVar().size();
		int i = 1;
		for(PrintVarContext toPrint : ctx.printVar()) {
			print.add("valueOrVarList", visit(toPrint).render());
			if(i<size) print.add("valueOrVarList", "+");
			i++;
		}
		
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitPrint_Println");
			ErrorHandling.printInfo(ctx,"\t-> print = "+ print.render());
			ErrorHandling.printInfo(ctx,"");
		}
		
		return print;
	}
	
	@Override
	public ST visitPrintVar_Var(PrintVar_VarContext ctx) {
		// get var info
		String varOriginalName = ctx.var().getText();
		String newVarName = symbolTableName.get(varOriginalName);
		
		// create var ST
		ST varST = stg.getInstanceOf("values");
		
		// get Object and add to ST accordingly
		Object obj = symbolTableValue.get(newVarName);
		
		if(obj instanceof Boolean) {
			varST.add("value", newVarName);
		}
		else if(obj instanceof String) {
			varST.add("value", newVarName);
		}
		else { // obj instanceof Variable
			Variable a = (Variable) obj;
			varST.add("value", newVarName + " " + a.getType().getPrintName());
		}
		return varST;
	}
	
	@Override
	public ST visitPrintVar_Value(PrintVar_ValueContext ctx) {
		String value = ctx.value().getText();
		ST valueST = stg.getInstanceOf("values");
		valueST.add("value", "\"\"+"+value+"+\"\"");
		return valueST;
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	// VARS AND TYPES------------------------------------------------------------------------------------------------------ 
	// --------------------------------------------------------------------------------------------------------------------
	
	// TODO verify if this is enough
	@Override
	public ST visitVar(VarContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public ST visitVarDeclaration(VarDeclarationContext ctx) {
		// get varDeclaration info
		ST type = visit(ctx.type());
		String newVarName = getNewVarName();
		
		// create varDeclaration ST
		ST varDeclaration = stg.getInstanceOf("varDeclaration");
		varDeclaration.add("type", type.render());
		varDeclaration.add("var",newVarName );

		symbolTableValue.put(newVarName, null);
		
		return varDeclaration;
	}
	
	@Override
	public ST visitType_Number_Type(Type_Number_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "number");
		return type;
	}

	@Override
	public ST visitType_Boolean_Type(Type_Boolean_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "boolean");
		return type;
	}

	@Override
	public ST visitType_String_Type(Type_String_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "string");
		return type;
	}

	@Override
	public ST visitType_Void_Type(Type_Void_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "void");
		return type;
	}

	@Override
	public ST visitType_ID_Type(Type_ID_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "id");
		return type;
	}

	@Override
	public ST visitValue_Cast_Number(Value_Cast_NumberContext ctx) {
		// get cast info
		String castType = (String) visit(ctx.cast()).getAttribute("operation");
		
		// get number info
		String number = ctx.NUMBER().getText();
		
		// create variable from number
		Variable a = createNumberVariable(number);
		
		// create the casted variable
		Variable b = new Variable(a);
		b.convertTypeTo(typesTable.get(castType));
		
		// get the cost of conversion
		double factor = Variable.pathCost(b, a);
		
		// create ST to the cast (explained in visitCast())
		String newName0 = getNewVarName();
		ST cast = varAssignmentST("Double", newName0, factor+";"); // include semicolon
		
		// create this ST
		String newName1 = getNewVarName();
		ST newVariable = varAssignmentST(cast.render(), "Double", newName1, number+"*"+factor); 
		
		symbolTableValue.put(newName1, b);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitValue_Cast_Number");
			ErrorHandling.printInfo(ctx,"");
		}
		
		return newVariable;
	}
	
	@Override
	public ST visitValue_Number(Value_NumberContext ctx) {
		
		Variable number = createVariable("number", ctx.NUMBER().getText());
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Double", newName, number.getValue()+""); 
		
		symbolTableValue.put(newName, number);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitValue_Cast_Number");
			ErrorHandling.printInfo(ctx,"");
		}
		
		return newVariable;
	}

	@Override
	public ST visitValue_Boolean(Value_BooleanContext ctx) {
		Boolean b = Boolean.parseBoolean(ctx.BOOLEAN().getText());
		String newName = getNewVarName();
		ST newVariable = varAssignmentST("Boolean", newName, b+""); 
		
		symbolTableValue.put( newName, b);
		
		if(debug) {
			ErrorHandling.printInfo(ctx,"");
			ErrorHandling.printInfo(ctx,"->"+ctx.getText());
			ErrorHandling.printInfo(ctx,"\t-> visitCompareOperation_BOOLEAN");
			ErrorHandling.printInfo(ctx,"");
		}
		
		return newVariable;
	}

	@Override
	public ST visitValue_String(Value_StringContext ctx) {
		// string is returned with quotation marks included
		ST str = stg.getInstanceOf("values");
		str.add("value", ctx.STRING().getText());
		return str;
	}

	@Override
	public ST visitCast(CastContext ctx) {
		// In java the cast is converted to a conversion factor between the units, as all are converted to Double
		// but this conversion has to be made up in the hierarchy because in here there is no access to the other variable
		ST castValue = stg.getInstanceOf("values");
		castValue.add("value", ctx.ID().getText());
		return castValue;
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
	
	protected static Variable createNumberVariable(String d) {
		Double number = Double.parseDouble(d);
		Variable a = new Variable(typesTable.get("number"), varType.NUMERIC, number);
		
		if(debug) {
			ErrorHandling.printInfo("");
			ErrorHandling.printInfo("->createNumberVariable");
			ErrorHandling.printInfo("\t-> number = "+d);
			ErrorHandling.printInfo("\t-> var : "+a.getType().getTypeName()+" "+a.getValue());
			ErrorHandling.printInfo("");
		}
		
		return a;
	}
	
	protected static Variable createVariable(String destType, String doubleValue) {
		Type type = typesTable.get(destType);
		Double value = Double.parseDouble(doubleValue);
		return new Variable(new Type(typesTable.get(type.getTypeName())), varType.NUMERIC, value);
	}
	
	protected static Variable createVariable(Type type, String doubleValue) {
		Double value = Double.parseDouble(doubleValue);
		return new Variable(type,varType.NUMERIC, value);
	}	
	
	protected static void updateSymbolTables(String originalName,String newName, Object value) {
		symbolTableName.put(originalName, newName);
		symbolTableValue.put(newName, value);
	}
	
	protected static Object getValueFromSymbolsTable(String name) {
		Object obj = null;
		if(symbolTableName.containsKey(name)) { //name is original name
			String newName = symbolTableName.get(name);
			obj = symbolTableValue.get(newName);
		}
		else {
			obj = symbolTableValue.get(name);
		}
		return	obj;		
	}
	
	public static Boolean getBooleanResult(Object objOp0, Object objOp1, String op) {
		
		if (objOp0 instanceof Boolean) {
			Boolean b0 = (Boolean)objOp0;
			Boolean b1 = (Boolean)objOp1;
			switch(op) {
				case "==" : return b0 == b1; 
				case "!=" : return b0 != b1; 
			}
			return false;
		}
	
		Double d0 = (Double)objOp0;
		Double d1 = (Double)objOp1;
		switch(op) {
			case "==" : return d0 == d1; 
			case "!=" : return d0 != d1; 
			case "<"  : return d0 < d1;
			case "<=" : return d0 <= d1; 
			case ">"  : return d0 > d1; 
			case ">=" : return d0 >= d1;
		}
		return false;
	}
	
	public static Boolean getLogicOperationResult(Boolean booleanOp0, Boolean booleanOp1, String op) {
		switch(op) {
			case "&&" : return booleanOp0 && booleanOp1; 
			case "||" : return booleanOp0 || booleanOp1; 
		}
		return false;
	}
	
	public static String getNewVarName() {
		String newName = "var"+varCounter;
		varCounter++;
		return newName;
		
	}

	public static String getcCorrespondingType(String type) {
		switch(type) {
		case "number" : return "Double";
		case "string" : return "String";
		case "boolean" : return "Boolean";
		default : return "Double";
		}
	}
		
}

