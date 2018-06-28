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
import potatoesGrammar.PotatoesParser.PrintVarContext;
import potatoesGrammar.PotatoesParser.Print_PrintContext;
import potatoesGrammar.PotatoesParser.Print_PrintlnContext;
import potatoesGrammar.PotatoesParser.ProgramContext;
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
		return visit(ctx.varDeclaration());
	}

	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitCode_Assignment(Code_AssignmentContext ctx) {
		return visit(ctx.assignment());
	}

	@Override
	public ST visitCode_Function(Code_FunctionContext ctx) {
		return visitChildren(ctx);
	}
	
	// --------------------------------------------------------------------------------------------------------------------	
	// CLASS - STATEMENTS--------------------------------------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------	
	
	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitStatement_Declaration(Statement_DeclarationContext ctx) {
		return visitChildren(ctx);
	}

	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitStatement_Assignment(Statement_AssignmentContext ctx) {
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
		
		ST assignment = varAssignmentST(type, varNewName, "! "+varOpName); 	
	
		updateSymbolsTable(varOriginalName, varNewName, !(Boolean)symbolTableValue.get(varOpOriginalName));
		
		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> Assignment_Var_Declaration_Not_Boolean");
			System.out.println("\t-> assignment = "+assignment.render());
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
			Variable d = createVariable(destinationType.getTypeName(), value);
			assignment.add("operation", d.getValue());			
			updateSymbolsTable(originalName, varNewName, d);
		}
		else if(type.equals("String")) {
			String s = value;
			assignment.add("operation", s);
			updateSymbolsTable(originalName, varNewName, s);
		}
		else { //typeValue.equals("Boolean")
			Boolean b = Boolean.parseBoolean((ctx.value().getText()));
			assignment.add("operation", b);
			updateSymbolsTable(originalName, varNewName, b);
		}
		
		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitAssignment_Var_Declaration_Value");
			System.out.println("\t-> assignment = "+assignment.render());
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
		ST assignment = varAssignmentST(previousDec, type, varNewName, resultVarName);
					
		//get the var name in potatoes code 
		String originalName = ctx.varDeclaration().ID().getText();
		
		updateSymbolsTable(originalName, varNewName, symbolTableValue.get(resultVarName));
		
		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitAssignment_Var_Declaration_Comparison");
			System.out.println("\t-> assignment = "+assignment.render());
		}
		
		return assignment;
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitAssignment_Var_Declaration_Operation(Assignment_Var_Declaration_OperationContext ctx) {

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
		ST assignment = varAssignmentST(previousOp, type, varNewName, resultVarName);
		
		String originalName = ctx.varDeclaration().ID().getText();
		
		updateSymbolsTable(originalName, varNewName, symbolTableValue.get(resultVarName));
		
		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitAssignment_Var_Declaration_Operation");
			System.out.println("\t-> assignment = "+assignment.render());
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
		ST assignment = varAssignmentST(type, varNewName, "! "+varOpName);

		
		updateSymbolsTable(originalName, varNewName, !(Boolean)symbolTableValue.get(varOpOriginalName));
		
		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitAssignment_Var_Not_Boolean");
			System.out.println("\t-> assignment = "+assignment.render());
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
		
		String typeValue = ((Variable)symbolTableValue.get(originalName)).getType().getTypeName();
		
		
		if(typeValue.equals("boolean")) {
			assignment.add("type", "Boolean");
			Boolean b = Boolean.parseBoolean((ctx.value().getText()));
			assignment.add("operation", b);
			updateSymbolsTable(originalName, varNewName, b);
		}
		else if(typeValue.equals("string")) {
			assignment.add("type", "String");
			String s = value;
			assignment.add("operation", s);
			updateSymbolsTable(originalName, varNewName, s);
		}
		else { //typeValue.equals("number")||typeValue.equals("ID")
			assignment.add("type", "Double");
			destinationType = typesTable.get(typeValue);
			destinationType.clearCheckList();
			Variable d = createVariable(destinationType.getTypeName(), value);
			assignment.add("operation", d.getValue());						
			updateSymbolsTable(originalName, varNewName, d);
		}

		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitAssignment_Var_Value");
			System.out.println("\t-> assignment = "+assignment.render());
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
		ST assignment = varAssignmentST(previousDec, type, varNewName, comparisonVarName);
		
		updateSymbolsTable(varNewName, varNewName, symbolTableValue.get(comparisonVarName));
		
		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitAssignment_Var_Comparison");
			System.out.println("\t-> comparison = "+comparison.render());
			System.out.println("\t-> assignment = "+assignment.render());
		}
		
		return assignment;
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitAssignment_Var_Operation(Assignment_Var_OperationContext ctx) {
		if(debug) {
			System.out.println("-> visitAssignment_Var_Operation");
			System.out.println("\t"+ctx.getText());
		}
		
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
		assignment.add("operation", resultVarName);
		
		updateSymbolsTable(originalName, newVarName, symbolTableValue.get(resultVarName));
		
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
		
		for(int i = 0; i<ctx.printVar().size()-1; i++)
			print.add("valueOrVarList", visit(ctx.printVar(i)).render()+"+");

		print.add("valueOrVarList", visit(ctx.printVar(ctx.printVar().size()-1)).render());

		
		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitPrint_Print");
			System.out.println("\t-> print = "+ print.render());
		}
		
		return print;
	}
	
	// [IJ] 
	@Override
	public ST visitPrint_Println(Print_PrintlnContext ctx) {
		ST print = stg.getInstanceOf("print");
		
		print.add("type",ctx.PRINTLN().getText());
		
		for(int i = 0; i<ctx.printVar().size()-1; i++)
			print.add("valueOrVarList", visit(ctx.printVar(i)).render()+"+");

		print.add("valueOrVarList", visit(ctx.printVar(ctx.printVar().size()-1)).render());

		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitPrint_Print");
			System.out.println("\t-> print = "+ print.render());
		}
		
		return print;
	}

	// [IJ] - DONE
	@Override
	public ST visitPrintVar(PrintVarContext ctx) {
		if(debug) {
			System.out.println("-> visitPrintVar");
			System.out.println("\t"+ctx.getText());
		}
		return visitChildren(ctx);
	}
		
	// --------------------------------------------------------------------------------------------------------------------
	// CONTROL FLOW STATMENTS----------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	// [IJ] - DONE
	@Override
	public ST visitControlFlowStatement(ControlFlowStatementContext ctx) {
		return visitChildren(ctx);
	}

	// [IJ] - DONE
	@Override
	public ST visitForLoop(ForLoopContext ctx) {
		
		ST forLoop = stg.getInstanceOf("forLoop");
		forLoop.add("firstAssignment", visit(ctx.assignment(0)).render());					
		forLoop.add("logicalOperation", visit(ctx.logicalOperation()).render());				
		forLoop.add("finalAssignment", visit(ctx.assignment(1)).render());						
		for(int i = 0; i<ctx.statement().size(); i++)
			forLoop.add("stat", visit(ctx.statement(i)).render());
		
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
		
		Boolean b0 = (Boolean) symbolTableValue.get(nameVar0);
		Boolean b1 = (Boolean) symbolTableValue.get(nameVar1);
		
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
		
		updateSymbolsTable(varNewName, varNewName, !(Boolean)symbolTableValue.get(comparisonVarName));
		
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
		updateSymbolsTable(varNewName, varNewName, symbolTableValue.get(resultVarName));
		
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
		updateSymbolsTable(varNewName, varNewName, !(Boolean)symbolTableValue.get(resultVarName));
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
		String comparison = varNameOp0 + compareOp + varNameOp1;
		ST assignment = varAssignmentST(type, varNewName, comparison);
		
		assignment.add("stat",(String) op0.render());
		assignment.add("stat",(String) op1.render());
		
		
		String typeOp0 = (String)op0.getAttribute("type");
		
		if(typeOp0.equals("Boolean")) {
			Boolean b0 = (Boolean) symbolTableValue.get(varNameOp0);
			Boolean b1 = (Boolean) symbolTableValue.get(varNameOp1);
			
			updateSymbolsTable(varNewName, varNewName, getBooleanResult(b0,b1,compareOp));
		}
		else {
			
			Variable v0 = (Variable) symbolTableValue.get(varNameOp0);
			Variable v1 = (Variable) symbolTableValue.get(varNameOp1);
			
			updateSymbolsTable(varNewName, varNewName, getBooleanResult(v0.getValue(),v1.getValue(),compareOp));
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
		
		Variable a = (Variable) symbolTableValue.get(oldVariableName);
		Variable result = new Variable(typesTable.get(ctx.cast().ID().getText()), a.getValue());
		
		

		ST newVariable = varAssignmentST("Double", getNewVarName(), result.getValue()+""); 
		String newName = (String) newVariable.getAttribute("var");
		
		newVariable.add("stat", oldVariable.render());//all the declarations until now
		
		updateSymbolsTable(newName, newName, result);
		
		
		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitOperation_Cast");
			System.out.println("\t-> oldVar = "+oldVariable.render());
			System.out.println("\t-> newVar = "+newVariable.render());
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
		
		Variable varOp0 = (Variable) symbolTableValue.get(op0Name) ;
		Variable varOp1 = (Variable) symbolTableValue.get(op1Name) ;
		Variable result = null;
		
		ST newVariable = varAssignmentST( "Double", getNewVarName());
		String newName = (String) newVariable.getAttribute("var");
			
		//all the declarations done until now
		newVariable.add("stat", op0.render());
		newVariable.add("stat", op1.render());
		
		if (ctx.op.getText().equals("%")) {
			Double moddedValue = varOp0.getValue() % varOp0.getValue();
			result = new Variable (typesTable.get(varOp0.getType().getTypeName()), moddedValue);
			newVariable.add("operation", op0Name + " % " + op1Name);
		}
		else {
			
			try {
				varOp0.MultDivCheckConvertType(destinationType);
				varOp1.MultDivCheckConvertType(destinationType);
			} catch (Exception e) {
				System.err.println("semantic analyses faild!");
				e.printStackTrace();
			}
			
			
			if (ctx.op.getText().equals("*")) {
				result = Variable.multiply(varOp0, varOp1); 
				Double resCode = result.getType().getCode(); 
				Collection<Type> types = typesTable.values(); 
				for (Type t : types) { 
					if (t.getCode() == resCode) {
						result = new Variable(typesTable.get(t.getTypeName()), result.getValue());
						break; 
					} 
				} 
			
				newVariable.add("operation", op0Name + " * " + op1Name);
			}
			else if (ctx.op.getText().equals("/")) {
				result = Variable.divide(varOp0, varOp1); 
				Double resCode = result.getType().getCode(); 
				Collection<Type> types = typesTable.values(); 
				for (Type t : types) { 
					if (t.getCode() == resCode) { 
						result = new Variable(typesTable.get(t.getTypeName()), result.getValue());
						break; 
					} 
				} 
				
				newVariable.add("operation", op0Name + " / " + op1Name);
			}
			else
				assert false: "missing semantic check";
		}		

		updateSymbolsTable(newName, newName, result);
		
		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitOperation_Mult_Div_Mod");
			System.out.println("\t-> op0 = "+op0.render());
			System.out.println("\t-> op1 = "+op1.render());
			System.out.println("\t-> newVar = "+newVariable.render());
		}
		
		return newVariable;
	}

	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitOperation_Simetric(Operation_SimetricContext ctx) {
		
		ST previousVariable = visit(ctx.operation());
		
		String previousVariableName = (String)previousVariable.getAttribute("var");
		
		Variable a = (Variable) symbolTableValue.get(previousVariableName);
		Variable.simetric(a);
		
		ST newVariable = varAssignmentST("Double", getNewVarName(), "- "+previousVariableName); 
		String newName = (String) newVariable.getAttribute("var");
		
		//add all the declarations until now
		newVariable.add("stat", previousVariable.render());
		
		updateSymbolsTable(newName, newName, a);
		
		
		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitOperation_Simetric");
			System.out.println("\t-> op0 = "+previousVariable.render());
			System.out.println("\t-> newVar = "+newVariable.render());
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
		
		Variable varOp0 = (Variable) symbolTableValue.get(op0Name) ;
		Variable varOp1 = (Variable) symbolTableValue.get(op1Name) ;
		Variable result = null;
		
		ST newVariable = varAssignmentST("Double", getNewVarName()); 
		String newName = (String) newVariable.getAttribute("var");
		
		//all the declarations done until now
		newVariable.add("stat", op0.render());
		newVariable.add("stat", op1.render());
	
		
		destinationType.clearCheckList();
	
		varOp1.convertTypeTo(varOp0.getType());
		
		if (ctx.op.getText().equals("+")) {
			result = Variable.add(varOp0, varOp1);
			newVariable.add("operation", op0Name + " + " + op1Name);
		}
		else {//if (ctx.op.getText().equals("-")) {
			result = Variable.subtract(varOp0, varOp1);
			newVariable.add("operation", op0Name + " - " + op1Name);
		}
		
		updateSymbolsTable(newName, newName, result);
		
		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitOperation_Add_Sub");
			System.out.println("\t-> op0 = "+op0.render());
			System.out.println("\t-> op1 = "+op1.render());
			System.out.println("\t-> newVar = "+newVariable.render());
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
		
		ST newVariable = varAssignmentST("Double", getNewVarName(), "Math.pow(" + nameVar0 + "," + nameVar1 + ")"); 
		String newName = (String) newVariable.getAttribute("var");
		
		newVariable.add("stat", op0.render());
		newVariable.add("stat", op1.render());

		updateSymbolsTable(newName, newName, createPowerVariable(nameVar0, nameVar1));
		
		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitOperation_Power");
			System.out.println("\t-> op0 = "+op0.render());
			System.out.println("\t-> op1 = "+op1.render());
			System.out.println("\t-> newVar = "+newVariable.render());
		}
		
		return newVariable;		
	}
	
	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitOperation_Var(Operation_VarContext ctx) {
		
		String varOpOriginalName = ctx.var().getText(); 
		String varOpNewName = symbolTableName.get(varOpOriginalName);
		
		Variable a = (Variable) symbolTableValue.get(varOpOriginalName);
		
		ST newVariable = varAssignmentST("Double", getNewVarName(), varOpNewName); 
		String newName = (String) newVariable.getAttribute("var");
		
		updateSymbolsTable(varOpOriginalName, newName, a);
		
		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitOperation_Var");;
			System.out.println("\t-> newVar = "+newVariable.render());
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
		ST newVariable = varAssignmentST( "Double", getNewVarName(), number );
		String newName = (String) newVariable.getAttribute("var");
		updateSymbolsTable(newName, newName, numberVar);
		
		if(debug) {
			System.out.println("->"+ctx.getText());
			System.out.println("\t-> visitOperation_NUMBER");;
			System.out.println("\t-> newVar = "+newVariable.render());
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
		ST varDeclaration = stg.getInstanceOf("varDeclaration");
		varDeclaration.add("type", ctx.type().getText());
		String newVarName = getNewVarName();
		varDeclaration.add("var",newVarName );
		
		String originalVarName = ctx.ID().getText();

		updateSymbolsTable(originalVarName, newVarName, null );
		
		return varDeclaration;
	}
	
	
	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitType_Number_Type(Type_Number_TypeContext ctx) {
		return visitChildren(ctx);
	}


	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitType_Boolean_Type(Type_Boolean_TypeContext ctx) {
		return visitChildren(ctx);
	}


	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitType_String_Type(Type_String_TypeContext ctx) {
		return visitChildren(ctx);
	}


	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitType_Void_Type(Type_Void_TypeContext ctx) {
		return visitChildren(ctx);
	}


	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitType_ID_Type(Type_ID_TypeContext ctx) {
		return visitChildren(ctx);
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
		return a;
	}
	
	protected static Variable createVariable(String destType, String doubleValue) {
		Type type = typesTable.get(destType);
		Double value = Double.parseDouble(doubleValue);
		return new Variable(new Type(typesTable.get(type.getTypeName())), value);
	}
	//[MJ] REVIEW
	protected static Variable createPowerVariable(String op0Name, String op1Name) {
		
		Variable varOp0 = (Variable)symbolTableValue.get(op0Name);
		Variable varOp1 = (Variable)symbolTableValue.get(op1Name);
		
		Variable a =  Variable.power(varOp0, varOp1);
		
		return a;
	}
	
	
	protected static void updateSymbolsTable(String originalName,String newName, Object value) {
		symbolTableValue.put(originalName, value);
		symbolTableName.put(originalName, newName);
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
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitFunction_Main(potatoesGrammar.PotatoesParser.Function_MainContext)
	 */
	@Override
	public ST visitFunction_Main(Function_MainContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFunction_Main(ctx);
	}

	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitFunction_ID(potatoesGrammar.PotatoesParser.Function_IDContext)
	 */
	@Override
	public ST visitFunction_ID(Function_IDContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFunction_ID(ctx);
	}

	
	
}
