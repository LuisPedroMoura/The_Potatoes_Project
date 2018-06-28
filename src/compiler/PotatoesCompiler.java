package compiler;

import utils.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.stringtemplate.v4.*;
import potatoesGrammar.PotatoesBaseVisitor;
import potatoesGrammar.PotatoesParser.Assignment_Var_ComparisonContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_ComparisonContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_FunctionCallContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_Not_BooleanContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_OperationContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_ValueContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Not_BooleanContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_OperationContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_ValueContext;
import potatoesGrammar.PotatoesParser.Assingment_Var_FunctionCallContext;
import potatoesGrammar.PotatoesParser.CastContext;
import potatoesGrammar.PotatoesParser.CodeContext;
import potatoesGrammar.PotatoesParser.Code_AssignmentContext;
import potatoesGrammar.PotatoesParser.Code_DeclarationContext;
import potatoesGrammar.PotatoesParser.Code_FunctionContext;
import potatoesGrammar.PotatoesParser.CompareOperation_BOOLEANContext;
import potatoesGrammar.PotatoesParser.CompareOperation_OperationContext;
import potatoesGrammar.PotatoesParser.CompareOperatorContext;
import potatoesGrammar.PotatoesParser.ComparisonContext;
import potatoesGrammar.PotatoesParser.ConditionContext;
import potatoesGrammar.PotatoesParser.ControlFlowStatementContext;
import potatoesGrammar.PotatoesParser.ElseConditionContext;
import potatoesGrammar.PotatoesParser.ElseIfConditionContext;
import potatoesGrammar.PotatoesParser.ForLoopContext;
import potatoesGrammar.PotatoesParser.FunctionCallContext;
import potatoesGrammar.PotatoesParser.FunctionReturnContext;
import potatoesGrammar.PotatoesParser.Function_IDContext;
import potatoesGrammar.PotatoesParser.Function_MainContext;
import potatoesGrammar.PotatoesParser.IfConditionContext;
import potatoesGrammar.PotatoesParser.LogicalOperand_ComparisonContext;
import potatoesGrammar.PotatoesParser.LogicalOperand_Not_ComparisonContext;
import potatoesGrammar.PotatoesParser.LogicalOperand_Not_ValueContext;
import potatoesGrammar.PotatoesParser.LogicalOperand_Not_VarContext;
import potatoesGrammar.PotatoesParser.LogicalOperand_ValueContext;
import potatoesGrammar.PotatoesParser.LogicalOperand_VarContext;
import potatoesGrammar.PotatoesParser.LogicalOperation_OperationContext;
import potatoesGrammar.PotatoesParser.LogicalOperation_ParenthesisContext;
import potatoesGrammar.PotatoesParser.LogicalOperation_logicalOperandContext;
import potatoesGrammar.PotatoesParser.Operation_Add_SubContext;
import potatoesGrammar.PotatoesParser.Operation_CastContext;
import potatoesGrammar.PotatoesParser.Operation_FunctionCallContext;
import potatoesGrammar.PotatoesParser.Operation_Mult_Div_ModContext;
import potatoesGrammar.PotatoesParser.Operation_NUMBERContext;
import potatoesGrammar.PotatoesParser.Operation_ParenthesisContext;
import potatoesGrammar.PotatoesParser.Operation_PowerContext;
import potatoesGrammar.PotatoesParser.Operation_SimetricContext;
import potatoesGrammar.PotatoesParser.Operation_VarContext;
import potatoesGrammar.PotatoesParser.PrintVar_ValueContext;
import potatoesGrammar.PotatoesParser.PrintVar_VarContext;
import potatoesGrammar.PotatoesParser.Print_PrintContext;
import potatoesGrammar.PotatoesParser.Print_PrintlnContext;
import potatoesGrammar.PotatoesParser.ProgramContext;
import potatoesGrammar.PotatoesParser.StatementContext;
import potatoesGrammar.PotatoesParser.Statement_AssignmentContext;
import potatoesGrammar.PotatoesParser.Statement_Control_Flow_StatementContext;
import potatoesGrammar.PotatoesParser.Statement_DeclarationContext;
import potatoesGrammar.PotatoesParser.Statement_FunctionCallContext;
import potatoesGrammar.PotatoesParser.Statement_Function_ReturnContext;
import potatoesGrammar.PotatoesParser.Statement_PrintContext;
import potatoesGrammar.PotatoesParser.Type_Boolean_TypeContext;
import potatoesGrammar.PotatoesParser.Type_ID_TypeContext;
import potatoesGrammar.PotatoesParser.Type_Number_TypeContext;
import potatoesGrammar.PotatoesParser.Type_String_TypeContext;
import potatoesGrammar.PotatoesParser.Type_Void_TypeContext;
import potatoesGrammar.PotatoesParser.UsingContext;
import potatoesGrammar.PotatoesParser.Value_BooleanContext;
import potatoesGrammar.PotatoesParser.Value_Cast_NumberContext;
import potatoesGrammar.PotatoesParser.Value_NumberContext;
import potatoesGrammar.PotatoesParser.Value_StringContext;
import potatoesGrammar.PotatoesParser.VarContext;
import potatoesGrammar.PotatoesParser.VarDeclarationContext;
import potatoesGrammar.PotatoesParser.WhenCaseContext;
import potatoesGrammar.PotatoesParser.WhenContext;
import potatoesGrammar.PotatoesParser.WhileLoopContext;
import typesGrammar.TypesFileInfo;

/**
 * * <b>PotatoesCompiler</b><p>
 * 
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class PotatoesCompiler extends PotatoesBaseVisitor<ST> {
	
	protected static STGroup stg = null;
	//protected static ParseTreeProperty<Object> mapCtxObj = PotatoesSemanticCheck.getMapCtxObj();

	protected static Map<String, String> symbolTableName = new HashMap<>();
	protected static Map<String, Object> symbolTableValue = new HashMap<>();
	
	private static TypesFileInfo typesFileInfo;
	private static Map<String, Type> typesTable;
	private static Type destinationType;
	
	private static int varCounter = 0;
	
	private static final boolean debug = true;
	
	// --------------------------------------------------------------------------------------------------------------------
	// MAIN RULES----------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------
	
	//[MJ] REVIEW -> visitChildren(ctx)?
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
	
	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitUsing(UsingContext ctx) {
		String str = ctx.STRING().getText();
		String path = str.substring(1, str.length() -1);
		typesFileInfo = new TypesFileInfo(path);
		typesTable = typesFileInfo.getTypesTable();
		return visitChildren(ctx);
	}

	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitCode_Declaration(Code_DeclarationContext ctx) {
		ST varDeclaration = visit(ctx.varDeclaration());
		return createEOL(varDeclaration);
	}
	
	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitCode_Assignment(Code_AssignmentContext ctx) {
		ST assignment = visit(ctx.assignment());
		return createEOL(assignment);
	}

	@Override
	public ST visitCode_Function(Code_FunctionContext ctx) {
		return visit(ctx.function());
	}
	
	// --------------------------------------------------------------------------------------------------------------------	
	// CLASS - STATEMENTS--------------------------------------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------	
	
	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitStatement_Declaration(Statement_DeclarationContext ctx) {
		ST varDeclaration = visit(ctx.varDeclaration());
		return createEOL(varDeclaration);
	}

	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitStatement_Assignment(Statement_AssignmentContext ctx) {
		//ST assignment = visit(ctx.assignment());
		//return createEOL(assignment);
		return visit(ctx.assignment());
	}

	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitStatement_Control_Flow_Statement(Statement_Control_Flow_StatementContext ctx) {
		return visit(ctx.controlFlowStatement());
	}
	
	@Override
	public ST visitStatement_FunctionCall(Statement_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}
	
	@Override
	public ST visitStatement_Function_Return(Statement_Function_ReturnContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitStatement_Print(Statement_PrintContext ctx) {
		return visit(ctx.print());
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	// CLASS - ASSIGNMENTS-----------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitAssignment_Var_Declaration_Not_Boolean(Assignment_Var_Declaration_Not_BooleanContext ctx) {
		
		ST varDeclaration =  visit(ctx.varDeclaration());
		
		String type = (String)varDeclaration.getAttribute("type");
		
		String varOriginalName = ctx.varDeclaration().ID().getText();
		String varNewName = (String) varDeclaration.getAttribute("var");
		
		String varOpOriginalName = ctx.var().getText();
		String varOpName = symbolTableName.get(varOpOriginalName);
		
		ST assignment = varAssignmentST(type, varNewName, "! "+varOpName+";"); 	
	
		updateSymbolsTable(varOriginalName, varNewName, !(Boolean)getValueFromSymbolsTable(varOpName));
		
		if(debug) {
			System.out.println();
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> Assignment_Var_Declaration_Not_Boolean");
			System.out.println("\t-> assignment = "+assignment.render());
			System.out.println();
		}
		
		return assignment;
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitAssignment_Var_Declaration_Value(Assignment_Var_Declaration_ValueContext ctx) {
			
		ST varDeclaration =  visit(ctx.varDeclaration());
		
		String originalName = ctx.varDeclaration().ID().getText();	
		
		String type = (String) varDeclaration.getAttribute("type");
		String varNewName = (String) varDeclaration.getAttribute("var");
		ST assignment = varAssignmentST(type, varNewName); 
	
		String value = ctx.value().getText();
		
		if(type.equals("Double")) {
			destinationType = typesTable.get(ctx.varDeclaration().type().getText());
			destinationType.clearCheckList();
			Variable d = createVariable(destinationType, value);
			assignment.add("operation", d.getValue()+";");		
			//System.out.println("\t-> d.getValue() = "+ d.getValue());
			updateSymbolsTable(originalName, varNewName, d);
		}
		else if(type.equals("String")) {
			String s = value;
			assignment.add("operation", s+";");
			updateSymbolsTable(originalName, varNewName, s);
		}
		else { //typeValue.equals("Boolean")
			Boolean b = Boolean.parseBoolean((ctx.value().getText()));
			assignment.add("operation", b+";");
			updateSymbolsTable(originalName, varNewName, b);
		}
		
		if(debug) {
			System.out.println();
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitAssignment_Var_Declaration_Value");
			System.out.println("\t-> type = "+type);
			System.out.println("\t-> assignment = "+assignment.render());
			System.out.println();
		}
	
		return assignment;
	}
	
	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitAssignment_Var_Declaration_Comparison(Assignment_Var_Declaration_ComparisonContext ctx) {
			
		//get ST of var declaration
		ST varDeclaration =  visit(ctx.varDeclaration());
		
		//get ST of comparison
		ST comparison = visit(ctx.comparison());
		
		//get typeValue from var declaration ST 
		String type = (String) varDeclaration.getAttribute("type");
		//get the new var name of this assignment from var declaration ST 
		String varNewName = (String) varDeclaration.getAttribute("var");
		//all the declarations until now
		String previousDec =  comparison.render();
		//assign the result var of comparison
		String resultVarName = (String) comparison.getAttribute("var");
		//create a ST for this assignment
		ST assignment = varAssignmentST(previousDec, type, varNewName, resultVarName+";");
					
		//get the var name in potatoes code 
		String originalName = ctx.varDeclaration().ID().getText();
		
		updateSymbolsTable(originalName, varNewName, getValueFromSymbolsTable(resultVarName));
		
		if(debug) {
			System.out.println();
			System.out.println("------------------------------------------------");
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitAssignment_Var_Declaration_Comparison");
			System.out.println("\t-> assignment = "+assignment.render());
			System.out.println("------------------------------------------------");
			System.out.println();
		}
		
		return assignment;
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitAssignment_Var_Declaration_Operation(Assignment_Var_Declaration_OperationContext ctx) {
		String typeName = ctx.varDeclaration().type().getText();
		destinationType = typesTable.get(typeName);
		destinationType.clearCheckList();
		
		//get ST of var declaration
		ST varDeclaration =  visit(ctx.varDeclaration());
		
		//get ST of operation
		ST operation =  visit(ctx.operation());
				
		//get typeValue from var declaration ST 
		String type = (String) varDeclaration.getAttribute("type");
		//get the new var name of this assignment from var declaration ST 
		String varNewName = (String) varDeclaration.getAttribute("var");
		// all assignments until now
		String previousOp =  operation.render();
		//assign the operation
		String resultVarName = (String) operation.getAttribute("var");
		
		//create a ST for this assignment
		ST assignment = varAssignmentST(previousOp, type, varNewName, resultVarName+";");
		
		String originalName = ctx.varDeclaration().ID().getText();
		
		Variable a = (Variable)getValueFromSymbolsTable(resultVarName);
		
		if(!typeName.equals("number")) {
			a.convertTypeTo(destinationType);
			destinationType.clearCheckList();
		}
		
		updateSymbolsTable(originalName, varNewName, a);
		
		if(debug) {
			System.out.println();
			System.out.println("------------------------------------------------");
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitAssignment_Var_Declaration_Operation");
			System.out.println("\t-> assignment:\n"+assignment.render());
			System.out.println("------------------------------------------------");
			System.out.println();
		}
		
		return assignment;	
	}

	@Override
	public ST visitAssignment_Var_Declaration_FunctionCall(Assignment_Var_Declaration_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitAssignment_Var_Not_Boolean(Assignment_Var_Not_BooleanContext ctx) {
		
		//get typeValue from var declaration ST 
		String type = "Boolean";
		//get the new var name of this assignment from var declaration ST 
		String originalName = ctx.var(0).getText();	
		String varNewName = symbolTableName.get(originalName);

		String varOpOriginalName = ctx.var(1).getText();
		String varOpName = symbolTableName.get(varOpOriginalName);
		
		//create a ST for this assignment
		ST assignment = varAssignmentST(type, varNewName, "! "+varOpName+";");

		
		updateSymbolsTable(originalName, varNewName, !(Boolean)getValueFromSymbolsTable(varOpOriginalName));
		
		if(debug) {
			System.out.println();
			System.out.println("------------------------------------------------");
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitAssignment_Var_Not_Boolean");
			System.out.println("\t-> assignment = "+assignment.render());
			System.out.println("------------------------------------------------");
			System.out.println();
		}
		
		return assignment;
	}
	
	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitAssignment_Var_Value(Assignment_Var_ValueContext ctx) {
		
		//get the var name in potatoes code
		String originalName = ctx.var().getText();
		//get the var name in java code
		String varNewName = symbolTableName.get(originalName);
	
		//create a ST for this assignment
		ST assignment = stg.getInstanceOf("varAssignment");
		assignment.add("var", varNewName);
		
		String value = ctx.value().getText();
		
		String typeValue = ((Variable)getValueFromSymbolsTable(originalName)).getType().getTypeName();
		
		
		if(typeValue.equals("boolean")) {
			assignment.add("type", "Boolean");
			Boolean b = Boolean.parseBoolean((ctx.value().getText()));
			assignment.add("operation", b+";");
			updateSymbolsTable(originalName, varNewName, b);
		}
		else if(typeValue.equals("string")) {
			assignment.add("type", "String");
			String s = value;
			assignment.add("operation", s+";");
			updateSymbolsTable(originalName, varNewName, s);
		}
		else { //typeValue.equals("number")||typeValue.equals("ID")
			assignment.add("type", "Double");
			destinationType = typesTable.get(typeValue);
			destinationType.clearCheckList();
			Variable d = createVariable(destinationType.getTypeName(), value);
			assignment.add("operation", d.getValue()+";");						
			updateSymbolsTable(originalName, varNewName, d);
		}

		if(debug) {
			System.out.println();
			System.out.println("------------------------------------------------");
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitAssignment_Var_Value");
			System.out.println("\t-> assignment = "+assignment.render());
			System.out.println("------------------------------------------------");
			System.out.println();
		}
	
		return assignment;
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitAssignment_Var_Comparison(Assignment_Var_ComparisonContext ctx) {
		
		//get the var name in potatoes code
		String originalName = ctx.var().getText();
		//get the var name in java code
		String varNewName = symbolTableName.get(originalName);
				
		//get ST of comparison
		ST comparison = visit(ctx.comparison());
		//all the declarations until now
		String previousDec =  comparison.render();
		//assign the result var of comparison
		String comparisonVarName = (String) comparison.getAttribute("var");
		
		//get typeValue from var declaration ST 
		String type = "Boolean";
		
		//create a ST for this assignment
		ST assignment = varAssignmentST(previousDec, type, varNewName, comparisonVarName+";");
		
		updateSymbolsTable(varNewName, varNewName, getValueFromSymbolsTable(comparisonVarName));
		
		if(debug) {
			System.out.println();
			System.out.println("------------------------------------------------");
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitAssignment_Var_Comparison");
			System.out.println("\t-> comparison = "+comparison.render());
			System.out.println("\t-> assignment = "+assignment.render());
			System.out.println("------------------------------------------------");
			System.out.println();
		}
		
		return assignment;
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitAssignment_Var_Operation(Assignment_Var_OperationContext ctx) {
			
		//get the var name in potatoes code
		String originalName = ctx.var().getText();
		//get the var name in java code
		String newVarName = symbolTableName.get(originalName);
		
		//create a ST for this assignment
		ST assignment = stg.getInstanceOf("varAssignment");
		
		//get the new var name
		assignment.add("var", newVarName);
		
		//get ST of operation
		ST operation =  visit(ctx.operation());
		
		//add all the other assignments until now
		assignment.add("stat", operation.render());
		
		//assign the operation
		String resultVarName = (String) operation.getAttribute("var");
		assignment.add("operation", resultVarName+";");
		
		Variable a = (Variable)getValueFromSymbolsTable(originalName);
		String typeName = a.getType().getTypeName();
		destinationType = typesTable.get(typeName); // static field to aid in operation predictive convertions
		destinationType.clearCheckList();
		Variable b = (Variable)getValueFromSymbolsTable(resultVarName);
		b.convertTypeTo(destinationType);
		destinationType.clearCheckList();
		
		updateSymbolsTable(originalName, newVarName, b);
		
		if(debug) {
			System.out.println();
			System.out.println("------------------------------------------------");
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitAssignment_Var_Operation");
			System.out.println("------------------------------------------------");
			System.out.println();
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
		ST statements = stg.getInstanceOf("stats");
		 for(StatementContext context : ctx.statement()) {
			 statements.add("stat", visit(context));
		    }
		return statements;
	}
	
	
	@Override
	public ST visitCast(CastContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
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
		
	// [IJ] 
	@Override
	public ST visitPrint_Print(Print_PrintContext ctx) {
		ST print = stg.getInstanceOf("print");
		
		print.add("type",ctx.PRINT().getText());
		
		String varOrValueList = "";
		int numberOfvalueOrVar = ctx.printVar().size();
		
		for(int i = 0; i<numberOfvalueOrVar-1; i++) {
			ST assign = visit(ctx.printVar(i));
			String varOrValue = (String) assign.getAttribute("operation");
			varOrValueList += "+" + varOrValue;
		}
		
		print.add("valueOrVarList", varOrValueList);
		
		if(debug) {
			System.out.println();
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitPrint_Print");
			System.out.println("\t-> print = "+ print.render());
			System.out.println();
		}
		
		return print;
	}
	
	// [IJ] 
	@Override
	public ST visitPrint_Println(Print_PrintlnContext ctx) {
		ST print = stg.getInstanceOf("print");

		print.add("type",ctx.PRINTLN().getText());
		
		String varOrValueList = "";
		int numberOfvalueOrVar = ctx.printVar().size();

		for(int i = 0; i<numberOfvalueOrVar-1; i++) {
			ST assign = visit(ctx.printVar(i));
			String varOrValue = (String) assign.getAttribute("operation");
			varOrValueList += "+" + varOrValue;
		}
		
		print.add("valueOrVarList", varOrValueList);
		
		
		if(debug) {
			System.out.println();
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitPrint_Print");
			System.out.println("\t-> print = "+ print.render());
			System.out.println();
		}
		
		return print;
	}
	
	@Override
	public ST visitPrintVar_Var(PrintVar_VarContext ctx) {
		String oldVariableName = ctx.var().getText();
		Type type = typesTable.get(ctx.var().getText());
		String resultVarName = symbolTableName.get(oldVariableName);
		
		
		//Variable oldVarValue = (Variable) getValueFromSymbolsTable(oldVariableName);
		String oldVarValue = (String) getValueFromSymbolsTable(oldVariableName);

		//oldVarValue tem de ser string (valor da variavel)
		ST assignment = varAssignmentST(type.getTypeName(), resultVarName, oldVarValue);
		
		return assignment;
	}
	
	@Override
	public ST visitPrintVar_Value(PrintVar_ValueContext ctx) {
		String value = ctx.value().getText();

		//nao temos o tipo de value!!
		String type = "String";
		
		String varNewName = getNewVarName();
		ST assignment = varAssignmentST(type, varNewName, value);
		updateSymbolsTable(varNewName, varNewName, value);
		
		return assignment;
	}
	
		
	// --------------------------------------------------------------------------------------------------------------------
	// CONTROL FLOW STATMENTS----------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	

	// [IJ] - DONE
	@Override
	public ST visitControlFlowStatement(ControlFlowStatementContext ctx) {
		if(debug) {
			System.out.println();
			System.out.println("------------------------------------------------");
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitControlFlowStatement");
			System.out.println("------------------------------------------------");
			System.out.println();
		}
		
		return visitChildren(ctx);
	}

	// [IJ] - DONE
	@Override
	public ST visitForLoop(ForLoopContext ctx) {
		
		ST forLoop = stg.getInstanceOf("forLoop");
		
		//assignments
		int size = ctx.assignment().size();
		if(size==1){// FOR '(' EOL logicalOperation EOL assignment ')'
			ST assignment = visit(ctx.assignment(0));
			forLoop.add("outsideStatements", assignment.render());
			
			String stat [] = assignment.render().split("Double");
			for(String s: stat)
				forLoop.add("finalAssignment", s);
		}
		else {// FOR '(' assignment EOL logicalOperation EOL assignment ')'
			ST assignment0 = visit(ctx.assignment(0));
			ST assignment1 = visit(ctx.assignment(1));
			
			forLoop.add("outsideStatements", assignment0.render());
			forLoop.add("outsideStatements", assignment1.render());
			
			//var actualization
			String stat [] = assignment1.render().split("Double");
			for(String s: stat)
				forLoop.add("finalAssignment", s);
		}
		
		
		//logical operation
		ST logicalOperation = visit(ctx.logicalOperation());
		forLoop.add("outsideStatements", logicalOperation.render());
		
		String operation = (String)logicalOperation.getAttribute("operation");
		String comparison = operation.substring(0, operation.length()-1);
		forLoop.add("logicalOperation", comparison);
		
		//var actualization
		String stat [] = logicalOperation.render().split("Double");
		for(String s: stat) {
			if(s.contains("Boolean")) {
				String [] stat2 = s.split("Boolean");
				for(String s2: stat2) {
					forLoop.add("finalAssignment", s2);
				}
				break;
			}
			forLoop.add("finalAssignment", s);
		}
		
		
		//statements
		for(StatementContext context : ctx.statement()) {
			ST statements = visit(context);
			forLoop.add("content", statements.render());
		}
		
		forLoop.add("content", "\n//finalAssignment actualization");
		
		
		if(debug) {
			System.out.println();
			System.out.println("------------------------------------------------");
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitForLoop");
			System.out.println("\t-> forLoop.render()\n"+forLoop.render());
			System.out.println("------------------------------------------------");
			System.out.println();
		}
		
		return forLoop;
	}

	// [IJ] - DONE
	@Override
	public ST visitWhileLoop(WhileLoopContext ctx) {

		ST whileLoop = stg.getInstanceOf("whileLoop");
		whileLoop.add("logicalOperation", visit(ctx.logicalOperation()).render());
		for(int i = 0; i<ctx.statement().size(); i++)
			whileLoop.add("stat", visit(ctx.statement(i)).render());

		return whileLoop;
	}
	
	@Override
	public ST visitWhen(WhenContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}
	
	@Override
	public ST visitWhenCase(WhenCaseContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}
	
	// [IJ] - DONE
	@Override
	public ST visitCondition(ConditionContext ctx) {
		return visitChildren(ctx);
	}
	
	// [IJ] - DONE
	@Override 
	public ST visitIfCondition(IfConditionContext ctx) { 
		ST ifCondition = stg.getInstanceOf("ifCondition");
		ifCondition.add("logicalOperation", visit(ctx.logicalOperation()).render());
		for(int i = 0; i<ctx.statement().size(); i++)
			ifCondition.add("stat", visit(ctx.statement(i)).render());

		return ifCondition;
	}

	// [IJ] - DONE
	@Override 
	public ST visitElseIfCondition(ElseIfConditionContext ctx) { 
		ST elseIfCondition = stg.getInstanceOf("elseIfCondition");
		elseIfCondition.add("logicalOperation", visit(ctx.logicalOperation()).render());
		for(int i = 0; i<ctx.statement().size(); i++)
			elseIfCondition.add("stat", visit(ctx.statement(i)).render());
		
		return elseIfCondition;
	}

	// [IJ] -  DONE
	@Override 
	public ST visitElseCondition(ElseConditionContext ctx) { 
		ST elseCondition = stg.getInstanceOf("elseCondition");
		for(int i = 0; i<ctx.statement().size(); i++)
			elseCondition.add("stat", visit(ctx.statement(i)).render());
		
		return elseCondition;
	}

	
	// --------------------------------------------------------------------------------------------------------------------
	// LOGICAL OPERATIONS--------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------
	
	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitLogicalOperation_Parenthesis(LogicalOperation_ParenthesisContext ctx) {
		return visit(ctx.logicalOperation());
	}

	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitLogicalOperation_Operation(LogicalOperation_OperationContext ctx) {
		ST op0 = visit(ctx.logicalOperation(0));
		ST op1 = visit(ctx.logicalOperation(1));

		String nameVar0 = (String) op0.getAttribute("var");
		String nameVar1 = (String) op1.getAttribute("var");
		
		String op = ctx.op.getText();

		String type = "Boolean";
		String varNewName = getNewVarName();
		String logicalOperation = nameVar0 + op + nameVar1;
		ST assign = varAssignmentST(type, varNewName, logicalOperation);
		
		//all the declarations done until now
		assign.add("stat", op0.render());
		assign.add("stat", op1.render());
		
		Boolean b0 = (Boolean) getValueFromSymbolsTable(nameVar0);
		Boolean b1 = (Boolean) getValueFromSymbolsTable(nameVar1);
		
		updateSymbolsTable(varNewName, varNewName, getLogicOperationResult(b0,b1,op));
		
		return assign;
	}

	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitLogicalOperation_logicalOperand(LogicalOperation_logicalOperandContext ctx) {
		return visit(ctx.logicalOperand());
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitLogicalOperand_Comparison(LogicalOperand_ComparisonContext ctx) {
		return visit(ctx.comparison());
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitLogicalOperand_Not_Comparison(LogicalOperand_Not_ComparisonContext ctx) {
		ST comparison = visit(ctx.comparison());
		String previousDec =  comparison.render();
		String comparisonVarName = (String) comparison.getAttribute("var");
	
		String type = "Boolean";
		String varNewName = getNewVarName();
		ST assignment = varAssignmentST(previousDec, type, varNewName, comparisonVarName);
		
		updateSymbolsTable(varNewName, varNewName, !(Boolean)getValueFromSymbolsTable(comparisonVarName));
		
		return assignment;
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitLogicalOperand_Var(LogicalOperand_VarContext ctx) {
		String originalName = ctx.var().getText();
		String type = "Boolean";
		String varNewName = getNewVarName();
		String resultVarName = symbolTableName.get(originalName);
		ST assignment = varAssignmentST(type, varNewName, resultVarName);
		updateSymbolsTable(varNewName, varNewName, getValueFromSymbolsTable(resultVarName));
		
		return assignment;
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitLogicalOperand_Not_Var(LogicalOperand_Not_VarContext ctx) {
		String originalName = ctx.var().getText();
		String type = "Boolean";
		String varNewName = getNewVarName();
		String resultVarName = symbolTableName.get(originalName);
		ST assignment = varAssignmentST(type, varNewName, resultVarName);
		updateSymbolsTable(varNewName, varNewName, !(Boolean)getValueFromSymbolsTable(resultVarName));
		return assignment;
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitLogicalOperand_Value(LogicalOperand_ValueContext ctx) {
		String value = ctx.value().getText();
		String type = "Boolean";
		String varNewName = getNewVarName();
		ST assignment = varAssignmentST(type, varNewName, value);
		updateSymbolsTable(varNewName, varNewName, value);
		
		return assignment;
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitLogicalOperand_Not_Value(LogicalOperand_Not_ValueContext ctx) {
		String value = ctx.value().getText();
		String type = "Boolean";
		String varNewName = getNewVarName();
		ST assignment = varAssignmentST(type, varNewName, value);
		updateSymbolsTable(varNewName, varNewName, !Boolean.parseBoolean(value));
		
		return assignment;
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitComparison(ComparisonContext ctx) {
		ST op0 = visit(ctx.compareOperation(0));
		ST op1 = visit(ctx.compareOperation(1));
		
		String varNameOp0 =  (String) op0.getAttribute("var");
		String varNameOp1 =  (String) op1.getAttribute("var");

		String compareOp = ctx.compareOperator().getText();
		
		String type = "Boolean";
		String varNewName = getNewVarName();
		String comparison = varNameOp0 + compareOp + varNameOp1+";";
		ST assignment = varAssignmentST(type, varNewName, comparison);
		
		assignment.add("stat",(String) op0.render());
		assignment.add("stat",(String) op1.render());
		
		
		String typeOp0 = (String)op0.getAttribute("type");
		if(typeOp0.equals("Boolean")) {
			Boolean b0 = (Boolean) getValueFromSymbolsTable(varNameOp0);
			Boolean b1 = (Boolean) getValueFromSymbolsTable(varNameOp1);
			
			updateSymbolsTable(varNewName, varNewName, getBooleanResult(b0,b1,compareOp));
		}
		else {
			
			Variable v0 = (Variable) getValueFromSymbolsTable(varNameOp0);
			Variable v1 = (Variable) getValueFromSymbolsTable(varNameOp1);
			
			updateSymbolsTable(varNewName, varNewName, getBooleanResult(v0.getValue(),v1.getValue(),compareOp));
		}
		

		if(debug) {
			System.out.println();;
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitComparison");
			System.out.println("\t-> assignment = "+assignment.render());
			System.out.println();
		}
			
		return assignment;
	}	
	
	@Override
	public ST visitCompareOperation_Operation(CompareOperation_OperationContext ctx) {
		return super.visitCompareOperation_Operation(ctx);
	}

	@Override
	public ST visitCompareOperation_BOOLEAN(CompareOperation_BOOLEANContext ctx) {
		return super.visitCompareOperation_BOOLEAN(ctx);
	}

	@Override
	public ST visitCompareOperator(CompareOperatorContext ctx) {
		return visitChildren(ctx);
	}

	// --------------------------------------------------------------------------------------------------------------------
	// OPERATIONS----------------------------------------------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------
	
	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitOperation_Cast(Operation_CastContext ctx) {
			
		ST oldVariable = visit(ctx.operation());
		
		String oldVariableName = (String)oldVariable.getAttribute("var");
		
		Variable a = (Variable) getValueFromSymbolsTable(oldVariableName);
		Variable result = new Variable(typesTable.get(ctx.cast().ID().getText()), a.getValue());
		
		

		ST newVariable = varAssignmentST("Double", getNewVarName(), result.getValue()+";"); 
		String newName = (String) newVariable.getAttribute("var");
		
		newVariable.add("stat", oldVariable.render());//all the declarations until now
		
		updateSymbolsTable(newName, newName, result);
		
		
		if(debug) {
			System.out.println();
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitOperation_Cast");
			System.out.println("\t-> oldVar = "+oldVariable.render());
			System.out.println("\t-> newVar = "+newVariable.render());
			System.out.println();
		}
		
		return newVariable;
	}
		
	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitOperation_Parenthesis(Operation_ParenthesisContext ctx) {
		return visit(ctx.operation());
	}
                                                                    
	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitOperation_Mult_Div_Mod(Operation_Mult_Div_ModContext ctx) {
		
		ST op0 = visit(ctx.operation(0));
		ST op1 = visit(ctx.operation(1));
	
		String op0Name = (String)op0.getAttribute("var");
		String op1Name = (String)op1.getAttribute("var");
		
		Variable varOp0  = null;
		Variable varOp1  = null;
		Variable result = null;
		
		varOp0 = (Variable) getValueFromSymbolsTable(op0Name);
		varOp1 = (Variable) getValueFromSymbolsTable(op1Name);
			
		ST newVariable = varAssignmentST( "Double", getNewVarName());
		String newName = (String) newVariable.getAttribute("var");
			
		//all the declarations done until now
		newVariable.add("stat", op0.render());
		newVariable.add("stat", op1.render());
		
		String op = ctx.op.getText();
		
		if (op.equals("%")) {
			Double moddedValue = varOp0.getValue() % varOp0.getValue();
			result = new Variable (typesTable.get(varOp0.getType().getTypeName()), moddedValue);
			newVariable.add("operation", op0Name + " % " + op1Name+ ";");
		}
		else {
			
			try {
				varOp0.MultDivCheckConvertType(destinationType);
				varOp1.MultDivCheckConvertType(destinationType);
			} catch (Exception e) {
				System.err.println("semantic analyses faild!");
				e.printStackTrace();
			}
			
			
			if (op.equals("*")) {
				result = Variable.multiply(varOp0, varOp1); 
				Double resCode = result.getType().getCode(); 
				Collection<Type> types = typesTable.values(); 
				for (Type t : types) { 
					if (t.getCode() == resCode) {
						result = new Variable(typesTable.get(t.getTypeName()), result.getValue());
						break; 
					} 
				} 
			
				newVariable.add("operation", op0Name + " * " + op1Name+ ";");
			}
			else if (op.equals("/")) {
				result = Variable.divide(varOp0, varOp1); 
				Double resCode = result.getType().getCode(); 
				Collection<Type> types = typesTable.values(); 
				for (Type t : types) { 
					if (t.getCode() == resCode) { 
						result = new Variable(typesTable.get(t.getTypeName()), result.getValue());
						break; 
					} 
				} 
				
				newVariable.add("operation", op0Name + " / " + op1Name+ ";");
			}
			else
				assert false: "missing semantic check";
		}		

		updateSymbolsTable(newName, newName, result);
		
		if(debug) {
			System.out.println();
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitOperation_Mult_Div_Mod");
			System.out.println("\t-> op0 = "+op0.render());
			System.out.println("\t-> op1 = "+op1.render());
			System.out.println("\t-> newVar = "+newVariable.render());
			System.out.println();
		}
		
		return newVariable;
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitOperation_Simetric(Operation_SimetricContext ctx) {
		
		ST previousVariable = visit(ctx.operation());
		
		String previousVariableName = (String)previousVariable.getAttribute("var");
		
		Variable a = (Variable) getValueFromSymbolsTable(previousVariableName);
		Variable.simetric(a);
		
		ST newVariable = varAssignmentST("Double", getNewVarName(), "- "+previousVariableName+ ";"); 
		String newName = (String) newVariable.getAttribute("var");
		
		//add all the declarations until now
		newVariable.add("stat", previousVariable.render());
		
		updateSymbolsTable(newName, newName, a);
		
		
		if(debug) {
			System.out.println();
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitOperation_Simetric");
			System.out.println("\t-> op0 = "+previousVariable.render());
			System.out.println("\t-> newVar = "+newVariable.render());
			System.out.println();
		}
		
		return newVariable;
	}
	
	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitOperation_Add_Sub(Operation_Add_SubContext ctx) {
		ST op0 = visit(ctx.operation(0));
		ST op1 = visit(ctx.operation(1));
		
		String op0Name = (String)op0.getAttribute("var");
		String op1Name = (String)op1.getAttribute("var");
		
		Variable varOp0  = null;
		Variable varOp1  = null;
		Variable result = null;
	
		varOp0 = (Variable) getValueFromSymbolsTable(op0Name);
		varOp1 = (Variable) getValueFromSymbolsTable(op1Name);
			
		ST newVariable = varAssignmentST("Double", getNewVarName()); 
		String newName = (String) newVariable.getAttribute("var");
		
		//all the declarations done until now
		newVariable.add("stat", op0.render());
		newVariable.add("stat", op1.render());
	
		
		destinationType.clearCheckList();
		
		varOp1.convertTypeTo(varOp0.getType());
			
		if (ctx.op.getText().equals("+")) {
			result = Variable.add(varOp0, varOp1);
			newVariable.add("operation", op0Name + " + " + op1Name+ ";");
		}
		else {//if (ctx.op.getText().equals("-")) {
			result = Variable.subtract(varOp0, varOp1);
			newVariable.add("operation", op0Name + " - " + op1Name+ ";");
		}
		
		updateSymbolsTable(newName, newName, result);
		
		if(debug) {
			System.out.println();
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitOperation_Add_Sub");
			System.out.println("\t-> newVar = "+newVariable.render());
			System.out.println();
		}
		
		return newVariable;
		
	}

	// [MJ] - DONE
	@Override
	public ST visitOperation_Power(Operation_PowerContext ctx) {
			
		ST op0 = visit(ctx.operation(0));
		ST op1 = visit(ctx.operation(1));
		
		String nameVar0 = (String) op0.getAttribute("var");
		String nameVar1 = (String) op1.getAttribute("var");
		
		ST newVariable = varAssignmentST("Double", getNewVarName(), "Math.pow(" + nameVar0 + "," + nameVar1 + ");"); 
		String newName = (String) newVariable.getAttribute("var");
		
		newVariable.add("stat", op0.render());
		newVariable.add("stat", op1.render());

		updateSymbolsTable(newName, newName, createPowerVariable(nameVar0, nameVar1));
		
		if(debug) {
			System.out.println();
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitOperation_Power");
			System.out.println("\t-> op0 = "+op0.render());
			System.out.println("\t-> op1 = "+op1.render());
			System.out.println("\t-> newVar = "+newVariable.render());
			System.out.println();
		}
		
		return newVariable;		
	}
	
	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitOperation_Var(Operation_VarContext ctx) {
		
		String varOpOriginalName = ctx.var().getText(); 
		String varOpNewName = symbolTableName.get(varOpOriginalName);		
		Variable a = (Variable) getValueFromSymbolsTable(varOpOriginalName);
		
		ST newVariable = varAssignmentST("Double", getNewVarName(), varOpNewName+ ";"); 
		String newName = (String) newVariable.getAttribute("var");
		
		updateSymbolsTable(varOpOriginalName, newName, a);
		
		if(debug) {
			System.out.println();
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitOperation_Var");
			System.out.println("\t-> varOpOriginalName = "+varOpOriginalName);
			System.out.println("\t-> varOpOriginalName = "+varOpNewName);
			System.out.println("\t-> a = "+a);
			System.out.println("\t-> newVar = "+newVariable.render());
			System.out.println("\t-> symbolTableName.get(varOpOriginalName) = "+symbolTableName.get(varOpOriginalName));
			System.out.println("\t-> getValueFromSymbolsTable(varOpOriginalName) = "+getValueFromSymbolsTable(varOpOriginalName));
			System.out.println();
		}		
		
		return newVariable;
	}

	@Override
	public ST visitOperation_FunctionCall(Operation_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}
	
	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitOperation_NUMBER(Operation_NUMBERContext ctx) {
		
		String number = ctx.NUMBER().getText();
		Variable numberVar = createNumberVariable(number);
		ST newVariable = varAssignmentST( "Double", getNewVarName(), Double.parseDouble(number)+";");
		String newName = (String) newVariable.getAttribute("var");
		updateSymbolsTable(newName, newName, numberVar);
		
		//System.out.println("newVariable.getAttribute(var) = " + newVariable.getAttribute("var"));
		
		if(debug) {
			System.out.println();
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitOperation_NUMBER");;
			System.out.println("\t-> newVar = "+newVariable.render());
			System.out.println("\t-> numberVar = " + numberVar);
			System.out.println("\t-> getValueFromSymbolsTable(newName) = "+(Variable) getValueFromSymbolsTable(newName));
			
			System.out.println();
		}	
		
		return newVariable;
	}

	// --------------------------------------------------------------------------------------------------------------------
	// VARS AND TYPES------------------------------------------------------------------------------------------------------ 
	// --------------------------------------------------------------------------------------------------------------------
	
	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitVar(VarContext ctx) {
		return visitChildren(ctx);
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitVarDeclaration(VarDeclarationContext ctx) {
		ST type = visit(ctx.type());
		ST varDeclaration = stg.getInstanceOf("varDeclaration");
		varDeclaration.add("type", type.render());
		String newVarName = getNewVarName();
		varDeclaration.add("var",newVarName );
		
		String originalVarName = ctx.ID().getText();

		updateSymbolsTable(originalVarName, newVarName, null );
		
		return varDeclaration;
	}
	
	
	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitType_Number_Type(Type_Number_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "number");
		return type;
	}


	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitType_Boolean_Type(Type_Boolean_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "boolean");
		return type;
	}


	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitType_String_Type(Type_String_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "string");
		return type;
	}


	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitType_Void_Type(Type_Void_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "void");
		return type;
	}


	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitType_ID_Type(Type_ID_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "id");
		return type;
	}


	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitValue_Cast_Number(Value_Cast_NumberContext ctx) {
		return visitChildren(ctx);
	}


	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitValue_Number(Value_NumberContext ctx) {
		return visitChildren(ctx);
	}


	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitValue_Boolean(Value_BooleanContext ctx) {
		return visitChildren(ctx);
	}


	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitValue_String(Value_StringContext ctx) {
		return visitChildren(ctx);
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
	
	protected static ST varAssignmentST(String stat, String type, String var, String operation) {
		ST newVariable = stg.getInstanceOf("varAssignment");
		
		newVariable.add("stat", stat);
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
	
	//[MJ] DONE
	protected static Variable createNumberVariable(String d) {
		Double number = Double.parseDouble(d);
		Variable a = new Variable(typesTable.get("number"), number);
		
		if(debug) {
			System.out.println();
			System.out.println("->createNumberVariable");
			System.out.println("\t-> number = "+d);
			System.out.println("\t-> var : "+a.getType().getTypeName()+" "+a.getValue());
			System.out.println();
		}
		
		return a;
	}
	
	protected static Variable createVariable(String destType, String doubleValue) {
		Type type = typesTable.get(destType);
		Double value = Double.parseDouble(doubleValue);
		return new Variable(new Type(typesTable.get(type.getTypeName())), value);
	}
	
	protected static Variable createVariable(Type type, String doubleValue) {
		Double value = Double.parseDouble(doubleValue);
		return new Variable(type, value);
	}
	//[MJ] REVIEW
	protected static Variable createPowerVariable(String op0Name, String op1Name) {
		
		Variable varOp0 = null;
		Variable varOp1 = null;
		
		if(symbolTableName.containsKey(op0Name)) { //op0Name is original name
			varOp0 = (Variable) getValueFromSymbolsTable(op0Name);
		}
		else {
			varOp0 = (Variable) symbolTableValue.get(op0Name);
		}
		
		if(symbolTableName.containsKey(op1Name)) { //op1Name is original name
			varOp1 = (Variable) getValueFromSymbolsTable(op1Name);
		}
		else {
			varOp1 = (Variable) symbolTableValue.get(op1Name);
		}
		
		Variable a =  Variable.power(varOp0, varOp1);
		
		return a;
	}
	
	
	protected static void updateSymbolsTable(String originalName,String newName, Object value) {
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
	
	//[MJ] DONE
	public static Boolean getBooleanResult(Object objOp0, Object objOp1, String op) {
		
		if (objOp0 instanceof Boolean) {
			Boolean b0 = (Boolean)objOp0;
			Boolean b1 = (Boolean)objOp1;
			switch(op) {
				case "==" : return b0 == b1; 
				case "!=" : return b0 != b1; 
			}
			assert false : "Semantic analysis faild";
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
		assert false : "Semantic analysis faild";
		return false;
	}
	
	// [IJ] DONE
	public static Boolean getLogicOperationResult(Boolean booleanOp0, Boolean booleanOp1, String op) {
		switch(op) {
			case "&&" : return booleanOp0 && booleanOp1; 
			case "||" : return booleanOp0 || booleanOp1; 
		}
		return false;
	}
	
	//[MJ] DONE
	public static String getNewVarName() {
		String newName = "var"+varCounter;
		varCounter++;
		return newName;
		
	}
	

	//-------------------------------------------------------------------------------------------------------------------------------------
	//OUT OF PLACE-------------------------------------------------------------------------------------------------------------------------
	//OUT OF PLACE-------------------------------------------------------------------------------------------------------------------------
	//OUT OF PLACE-------------------------------------------------------------------------------------------------------------------------
	//OUT OF PLACE-------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitFunction_ID(potatoesGrammar.PotatoesParser.Function_IDContext)
	 */
	@Override
	public ST visitFunction_ID(Function_IDContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFunction_ID(ctx);
	}

	
	
}
