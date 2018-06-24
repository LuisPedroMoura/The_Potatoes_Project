package compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

import static java.lang.System.*;

import typesGrammar.TypesFileInfo;
import utils.*;
import utils.errorHandling.ErrorHandling;

import potatoesGrammar.PotatoesBaseVisitor;
import potatoesGrammar.PotatoesParser;
import potatoesGrammar.PotatoesParser.ArrayAccessContext;
import potatoesGrammar.PotatoesParser.ArrayDeclarationContext;
import potatoesGrammar.PotatoesParser.ArrayLengthContext;
import potatoesGrammar.PotatoesParser.ArrayTypeContext;
import potatoesGrammar.PotatoesParser.Assigmennt_Array_VarContext;
import potatoesGrammar.PotatoesParser.Assignment_ArrayAccess_ComparisonContext;
import potatoesGrammar.PotatoesParser.Assignment_ArrayAccess_Not_BooleanContext;
import potatoesGrammar.PotatoesParser.Assignment_ArrayAccess_OperationContext;
import potatoesGrammar.PotatoesParser.Assignment_ArrayAccess_ValueContext;
import potatoesGrammar.PotatoesParser.Assignment_ArrayAccess_ValueListContext;
import potatoesGrammar.PotatoesParser.Assignment_Array_FunctionCallContext;
import potatoesGrammar.PotatoesParser.Assignment_Array_ValuesListContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_ComparisonContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_ComparisonContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_FunctionCallContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_Not_BooleanContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_OperationContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_ValueContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Not_BooleanContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_OperationContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_ValueContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_ValueListContext;
import potatoesGrammar.PotatoesParser.Assingment_ArrayAccess_FunctionCallContext;
import potatoesGrammar.PotatoesParser.Assingment_Var_FunctionCallContext;
import potatoesGrammar.PotatoesParser.CastContext;
import potatoesGrammar.PotatoesParser.Code_AssignmentContext;
import potatoesGrammar.PotatoesParser.Code_DeclarationContext;
import potatoesGrammar.PotatoesParser.Code_FunctionContext;
import potatoesGrammar.PotatoesParser.CompareOperatorContext;
import potatoesGrammar.PotatoesParser.ComparisonContext;
import potatoesGrammar.PotatoesParser.ConditionContext;
import potatoesGrammar.PotatoesParser.ControlFlowStatementContext;
import potatoesGrammar.PotatoesParser.Declaration_VarContext;
import potatoesGrammar.PotatoesParser.Declaration_arrayContext;
import potatoesGrammar.PotatoesParser.ForLoopContext;
import potatoesGrammar.PotatoesParser.FunctionCallContext;
import potatoesGrammar.PotatoesParser.FunctionContext;
import potatoesGrammar.PotatoesParser.FunctionReturnContext;
import potatoesGrammar.PotatoesParser.LogicalOperandContext;
import potatoesGrammar.PotatoesParser.LogicalOperand_ComparisonContext;
import potatoesGrammar.PotatoesParser.LogicalOperand_Not_ComparisonContext;
import potatoesGrammar.PotatoesParser.LogicalOperand_Not_ValueContext;
import potatoesGrammar.PotatoesParser.LogicalOperand_Not_VarContext;
import potatoesGrammar.PotatoesParser.LogicalOperand_ValueContext;
import potatoesGrammar.PotatoesParser.LogicalOperand_VarContext;
import potatoesGrammar.PotatoesParser.LogicalOperationContext;
import potatoesGrammar.PotatoesParser.LogicalOperation_OperationContext;
import potatoesGrammar.PotatoesParser.LogicalOperation_ParenthesisContext;
import potatoesGrammar.PotatoesParser.LogicalOperation_logicalOperandContext;
import potatoesGrammar.PotatoesParser.Operation_Add_SubContext;
import potatoesGrammar.PotatoesParser.Operation_ArrayAccessContext;
import potatoesGrammar.PotatoesParser.Operation_ArrayLengthContext;
import potatoesGrammar.PotatoesParser.Operation_CastContext;
import potatoesGrammar.PotatoesParser.Operation_FunctionCallContext;
import potatoesGrammar.PotatoesParser.Operation_Mult_Div_ModContext;
import potatoesGrammar.PotatoesParser.Operation_NUMBERContext;
import potatoesGrammar.PotatoesParser.Operation_ParenthesisContext;
import potatoesGrammar.PotatoesParser.Operation_PowerContext;
import potatoesGrammar.PotatoesParser.Operation_SimetricContext;
import potatoesGrammar.PotatoesParser.Operation_VarContext;
import potatoesGrammar.PotatoesParser.PrintContext;
import potatoesGrammar.PotatoesParser.ProgramContext;
import potatoesGrammar.PotatoesParser.Statement_AssignmentContext;
import potatoesGrammar.PotatoesParser.Statement_Control_Flow_StatementContext;
import potatoesGrammar.PotatoesParser.Statement_DeclarationContext;
import potatoesGrammar.PotatoesParser.Statement_FunctionCallContext;
import potatoesGrammar.PotatoesParser.Statement_Function_ReturnContext;
import potatoesGrammar.PotatoesParser.Statement_PrintContext;
import potatoesGrammar.PotatoesParser.TypeContext;
import potatoesGrammar.PotatoesParser.Type_ArrayTypeContext;
import potatoesGrammar.PotatoesParser.Type_Boolean_TypeContext;
import potatoesGrammar.PotatoesParser.Type_ID_TypeContext;
import potatoesGrammar.PotatoesParser.Type_Number_TypeContext;
import potatoesGrammar.PotatoesParser.Type_String_TypeContext;
import potatoesGrammar.PotatoesParser.Type_Void_TypeContext;
import potatoesGrammar.PotatoesParser.UsingContext;
import potatoesGrammar.PotatoesParser.ValueContext;
import potatoesGrammar.PotatoesParser.Value_BooleanContext;
import potatoesGrammar.PotatoesParser.Value_Cast_NumberContext;
import potatoesGrammar.PotatoesParser.Value_NumberContext;
import potatoesGrammar.PotatoesParser.Value_StringContext;
import potatoesGrammar.PotatoesParser.ValuesListContext;
import potatoesGrammar.PotatoesParser.VarContext;
import potatoesGrammar.PotatoesParser.VarDeclarationContext;
import potatoesGrammar.PotatoesParser.WhenCaseContext;
import potatoesGrammar.PotatoesParser.WhenContext;
import potatoesGrammar.PotatoesParser.WhileLoopContext;


public class PotatoesVisitorSemanticAnalysis extends PotatoesBaseVisitor<Boolean>  {
	
	// Static Field (Debug Only)
	private static final boolean debug = true;
	
	static String path;
	private static TypesFileInfo typesFileInfo; // initialized in visitUsing();
	private static Map<String, Type> typesTable = typesFileInfo.getTypesTable();
	
	protected static ParseTreeProperty<Object> mapCtxObj = new ParseTreeProperty<>();
	protected static Map<String, Object> symbolTable = new HashMap<>();
	
	private static Type destinationType;
	
	public static ParseTreeProperty<Object> getMapCtxObj(){
		return mapCtxObj;
	}
	
	public static TypesFileInfo getTypesFileInfo() {
		return typesFileInfo;
	}

// -----------------------
// -----------------------
// OUT OF PLACE VISITORS
// -----------------------


	
	
	
// --------------------------------------------------------------------------------------------------------------------
// MAIN RULES----------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE - DON'T DELETE FROM THIS FILE
	public Boolean visitProgram(ProgramContext ctx) {
		return visitChildren(ctx);
	}
	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitUsing(UsingContext ctx) {
		path = getStringText(ctx.STRING().getText());
		typesFileInfo = new TypesFileInfo(path);
		mapCtxObj.put(ctx, path);
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitCode_Declaration(Code_DeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitCode_Assignment(Code_AssignmentContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitCode_Function(Code_FunctionContext ctx) {
		return visitChildren(ctx);
	}

// --------------------------------------------------------------------------------------------------------------------	
// CLASS - STATEMENTS-----------------------------------------------------------------------	
// --------------------------------------------------------------------------------------------------------------------	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitStatement_Declaration(Statement_DeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitStatement_Assignment(Statement_AssignmentContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitStatement_Control_Flow_Statement(Statement_Control_Flow_StatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitStatement_FunctionCall(Statement_FunctionCallContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitStatement_Function_Return(Statement_Function_ReturnContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitStatement_Print(Statement_PrintContext ctx) {
		return visitChildren(ctx);
	}


// --------------------------------------------------------------------------------------------------------------------	
// CLASS - DECLARATIONS-----------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitDeclaration_array(Declaration_arrayContext ctx) {
		// TODO Auto-generated method stub
		return super.visitDeclaration_array(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitDeclaration_Var(Declaration_VarContext ctx) {
		return visitChildren(ctx);
	}
	
// --------------------------------------------------------------------------------------------------------------------
// CLASS - ASSIGNMENTS-----------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Declaration_Not_Boolean(Assignment_Var_Declaration_Not_BooleanContext ctx) {
		String type = (String) mapCtxObj.get(ctx.varDeclaration().type());
		String varName = ctx.varDeclaration().var().ID().getText();
		
		// verify that variable to be created has valid name
		if (typesTable.containsKey(varName)) {
			ErrorHandling.printError(ctx, varName + " is a reserved word");
			return false;
		}
		
		// verify that assigned Variable is of Type boolean
		if (type.equals("boolean")) {
			Boolean b = Boolean.parseBoolean(ctx.BOOLEAN().getText());
			symbolTable.put(ctx.varDeclaration().var().ID().getText(), !b);
			mapCtxObj.put(ctx, b);
			return true;
		}
		
		ErrorHandling.printError(ctx, "Type \"" + type + "\" and boolean are not compatible");
		return false;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Declaration_Value(Assignment_Var_Declaration_ValueContext ctx) {
		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		Object value = mapCtxObj.get(ctx.value());
		String varName = ctx.varDeclaration().var().ID().getText();
		
		// verify that variable to be created has valid name
		if (typesTable.containsKey(varName)) {
			ErrorHandling.printError(ctx, varName + " is a reserved word");
			return false;
		}
		
		// assign boolean to boolean
		if (value instanceof Boolean && typeName.equals("boolean")) {
			Boolean b = (Boolean) value;
			symbolTable.put(ctx.varDeclaration().var().ID().getText(), b);
			mapCtxObj.put(ctx, b);
			return true;
		}
		
		// assign string to string
		if (value instanceof String && typeName.equals("string")) {
			String str = (String) value;
			symbolTable.put(ctx.varDeclaration().var().ID().getText(), str);
			mapCtxObj.put(ctx, str);
			return true;
		}
		
		// assign compatible types
		if (typesTable.containsKey(typeName)) {
			Variable a = (Variable) value;
			Type type = typesTable.get(typeName);
			if (a.convertTypeTo(type) == true) {
				symbolTable.put(ctx.varDeclaration().var().ID().getText(), a);
				mapCtxObj.put(ctx, a);
				return true;
			}
		}
		
		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with given Type");
		return false;
	}

	@Override  // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Declaration_Comparison(Assignment_Var_Declaration_ComparisonContext ctx) {
		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		Boolean b = (Boolean) mapCtxObj.get(ctx.comparison());
		String varName = ctx.varDeclaration().var().ID().getText();
		
		// verify that variable to be created has valid name
		if (typesTable.containsKey(varName)) {
			ErrorHandling.printError(ctx, varName + " is a reserved word");
			return false;
		}
		
		// verify taht assigned var has type boolean
		if (typeName.equals("boolean")) {
			symbolTable.put(ctx.varDeclaration().var().ID().getText(), b);
			mapCtxObj.put(ctx, b);
			return true;
		}
		
		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with boolean");
		return false;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Declaration_Operation(Assignment_Var_Declaration_OperationContext ctx) {
		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		destinationType = typesTable.get(typeName); // static field to aid in operation predictive convertions
		Variable a = (Variable) mapCtxObj.get(ctx.operation());
		String varName = ctx.varDeclaration().var().ID().getText();
		
		// verify that variable to be created has valid name
		if (typesTable.containsKey(varName)) {
			ErrorHandling.printError(ctx, varName + " is a reserved word");
			return false;
		}
		
		if (a.convertTypeTo(typesTable.get(typeName))) {
			symbolTable.put(ctx.varDeclaration().var().ID().getText(), a);
			mapCtxObj.put(ctx, a);
			return true;
		}
		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with \"" + a.getType().getTypeName() + "\"");
		return false;
	}

	@Override
	public Boolean visitAssignment_Var_Declaration_FunctionCall(Assignment_Var_Declaration_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Declaration_FunctionCall(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Not_Boolean(Assignment_Var_Not_BooleanContext ctx) {
		String type = (String) mapCtxObj.get(ctx.var().ID());
		
		// verify that assigned Variable is of Type boolean
		if (type.equals("boolean")) {
			Boolean b = Boolean.parseBoolean(ctx.BOOLEAN().getText());
			symbolTable.put(ctx.var().ID().getText(), !b);
			mapCtxObj.put(ctx, b);
			return true;
		}
		
		ErrorHandling.printError(ctx, "Type \"" + type + "\" and boolean are not compatible");
		return false;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Value(Assignment_Var_ValueContext ctx) {
		String typeName = (String) mapCtxObj.get(ctx.var().ID());
		Object value = mapCtxObj.get(ctx.value());
		
		// assign boolean to boolean
		if (value instanceof Boolean && typeName.equals("boolean")) {
			Boolean b = (Boolean) value;
			symbolTable.put(ctx.var().ID().getText(), b);
			mapCtxObj.put(ctx, b);
			return true;
		}
		
		// assign string to string
		if (value instanceof String && typeName.equals("string")) {
			String str = (String) value;
			symbolTable.put(ctx.var().ID().getText(), str);
			mapCtxObj.put(ctx, str);
			return true;
		}
		
		// assign compatible types
		if (typesTable.containsKey(typeName)) {
			Variable a = (Variable) value;
			Type type = typesTable.get(typeName);
			if (a.convertTypeTo(type) == true) {
				symbolTable.put(ctx.var().ID().getText(), a);
				mapCtxObj.put(ctx, a);
				return true;
			}
		}
		
		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with given Type");
		return false;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Comparison(Assignment_Var_ComparisonContext ctx) {
		String typeName = (String) mapCtxObj.get(ctx.var().ID());
		Boolean b = (Boolean) mapCtxObj.get(ctx.comparison());
		
		// verify taht assigned var has type boolean
		if (typeName.equals("boolean")) {
			symbolTable.put(ctx.var().ID().getText(), b);
			mapCtxObj.put(ctx, b);
			return true;
		}
		
		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with boolean");
		return false;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Operation(Assignment_Var_OperationContext ctx) {
		String typeName = (String) mapCtxObj.get(ctx.var().ID());
		destinationType = typesTable.get(typeName); // static field to aid in operation predictive convertions
		Variable a = (Variable) mapCtxObj.get(ctx.operation());
		
		if (a.convertTypeTo(typesTable.get(typeName))) {
			symbolTable.put(ctx.var().ID().getText(), a);
			mapCtxObj.put(ctx, a);
			return true;
		}
		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with \"" + a.getType().getTypeName() + "\"");
		return false;
	}

	@Override
	public Boolean visitAssignment_Var_ValueList(Assignment_Var_ValueListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_ValueList(ctx);
	}

	@Override
	public Boolean visitAssingment_Var_FunctionCall(Assingment_Var_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssingment_Var_FunctionCall(ctx);
	}

	@Override
	public Boolean visitAssigmennt_Array_Var(Assigmennt_Array_VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssigmennt_Array_Var(ctx);
	}
	
	@Override
	public Boolean visitAssignment_Array_ValuesList(Assignment_Array_ValuesListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Array_ValuesList(ctx);
	}

	@Override
	public Boolean visitAssignment_Array_FunctionCall(Assignment_Array_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Array_FunctionCall(ctx);
	}

	@Override
	public Boolean visitAssignment_ArrayAccess_Not_Boolean(Assignment_ArrayAccess_Not_BooleanContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_Not_Boolean(ctx);
	}

	@Override
	public Boolean visitAssignment_ArrayAccess_Value(Assignment_ArrayAccess_ValueContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_Value(ctx);
	}

	@Override
	public Boolean visitAssignment_ArrayAccess_Comparison(Assignment_ArrayAccess_ComparisonContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_Comparison(ctx);
	}

	@Override
	public Boolean visitAssignment_ArrayAccess_Operation(Assignment_ArrayAccess_OperationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_Operation(ctx);
	}

	@Override
	public Boolean visitAssignment_ArrayAccess_ValueList(Assignment_ArrayAccess_ValueListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_ValueList(ctx);
	}

	@Override
	public Boolean visitAssingment_ArrayAccess_FunctionCall(Assingment_ArrayAccess_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssingment_ArrayAccess_FunctionCall(ctx);
	}

// --------------------------------------------------------------------------------------------------------------------	
// FUNCTIONS-------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------	
	@Override
	public Boolean visitFunction(FunctionContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFunction(ctx);
	}

	@Override
	public Boolean visitFunctionReturn(FunctionReturnContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFunctionReturn(ctx);
	}

	@Override
	public Boolean visitFunctionCall(FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFunctionCall(ctx);
	}
	
	@Override
	public Boolean visitPrint(PrintContext ctx) {
		// TODO Auto-generated method stub
		return super.visitPrint(ctx);
	}
	
// --------------------------------------------------------------------------------------------------------------------
// CONTROL FLOW STATMENTS------------------------------------------------------	
// --------------------------------------------------------------------------------------------------------------------	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitControlFlowStatement(ControlFlowStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitForLoop(ForLoopContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitWhileLoop(WhileLoopContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitWhen(WhenContext ctx) {
		// TODO Auto-generated method stub
		return super.visitWhen(ctx);
	}

	@Override
	public Boolean visitWhenCase(WhenCaseContext ctx) {
		// TODO Auto-generated method stub
		return super.visitWhenCase(ctx);
	}

	@Override
	public Boolean visitCondition(ConditionContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCondition(ctx);
	}

	
// --------------------------------------------------------------------------------------------------------------------
// LOGICAL OPERATIONS----------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitLogicalOperation_Parenthesis(LogicalOperation_ParenthesisContext ctx) {
		mapCtxObj.put(ctx, mapCtxObj.get(ctx.logicalOperation()));
		return visitChildren(ctx);
	}
	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitLogicalOperation_Operation(LogicalOperation_OperationContext ctx) {
		Boolean b1 = (Boolean) mapCtxObj.get(ctx.logicalOperation(0));
		Boolean b2 = (Boolean) mapCtxObj.get(ctx.logicalOperation(1));
		Boolean res = true;
		
		// logical AND
		if (ctx.op.getText().equals("&&")) {
			res = b1 && b2;
			mapCtxObj.put(ctx, res);
		}
		
		// logical OR
		if(ctx.op.getText().equals("||")) {
			res = b1 || b2;
			mapCtxObj.put(ctx, res);
		}
		
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitLogicalOperation_logicalOperand(LogicalOperation_logicalOperandContext ctx) {
		mapCtxObj.put(ctx, mapCtxObj.get(ctx.getChild(0)));
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitLogicalOperand_Comparison(LogicalOperand_ComparisonContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitLogicalOperand_Not_Comparison(LogicalOperand_Not_ComparisonContext ctx) {
		return !visitChildren(ctx);
	}

	@Override
	public Boolean visitLogicalOperand_Var(LogicalOperand_VarContext ctx) {
		String varName = ctx.var().ID().getText();
		Boolean res;
		
		// verify that variable exists
		if (symbolTable.containsKey(varName)) {
			Object object = symbolTable.get(varName);
			if (object instanceof Boolean) {
				res = (Boolean) object;
			}
			else {
				ErrorHandling.printError(ctx, "Variable \"" + varName + "\" is not boolean");
				return false;
			}
		}
		else {
			ErrorHandling.printError(ctx, "Variable \"" + varName + "\" is not declared");
			return false;
		}
		
		return res;
	}

	@Override
	public Boolean visitLogicalOperand_Not_Var(LogicalOperand_Not_VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperand_Not_Var(ctx);
	}

	@Override
	public Boolean visitLogicalOperand_Value(LogicalOperand_ValueContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperand_Value(ctx);
	}

	@Override
	public Boolean visitLogicalOperand_Not_Value(LogicalOperand_Not_ValueContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperand_Not_Value(ctx);
	}

	@Override
	public Boolean visitComparison(ComparisonContext ctx) {
		// TODO Auto-generated method stub
		return super.visitComparison(ctx);
	}

	@Override
	public Boolean visitCompareOperator(CompareOperatorContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCompareOperator(ctx);
	}
	
// --------------------------------------------------------------------------------------------------------------------
// OPERATIONS------------------------------------------------------------------	
// --------------------------------------------------------------------------------------------------------------------
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitOperation_Cast(Operation_CastContext ctx) {
		Variable a = (Variable) mapCtxObj.get(ctx.operation());
		
		// cast is only possible if Variable is of Type Number (with code 1)
		if (a.getType().getCode() != 1) {
			ErrorHandling.printError(ctx, "Type \"" + a.getType() + "\" cannot be casted. Only number type can be casted");
			return false;
		}
		
		// type is number cast is possible
		Variable res = new Variable(typesTable.get(ctx.cast().ID().getText()), a.getValue());
		mapCtxObj.put(ctx, res);
		return true;
	}
	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitOperation_Parenthesis(Operation_ParenthesisContext ctx) {
		mapCtxObj.put(ctx, mapCtxObj.get(ctx.operation()));
		return true;
	}

	@Override
	public Boolean visitOperation_Mult_Div_Mod(Operation_Mult_Div_ModContext ctx) {
		Variable a = (Variable) mapCtxObj.get(ctx.operation(0));
		Variable b = (Variable) mapCtxObj.get(ctx.operation(1));
		String op = ctx.op.getText();
		
		// Modulos operation
		if (op.equals("%")) {
			// verify that right side of mod operation is of Type Number
			if (b.getType().getCode() == 1) {
				Double moddedValue = a.getValue() % b.getValue();
				a = new Variable (a.getType(), moddedValue);
				mapCtxObj.put(ctx,  a);
			}
			else {
				ErrorHandling.printError(ctx, "Right side of mod operation has to be of Type Number");
				return false;
			}
		}
		
		if (op.equals("*")) {
			boolean checkType = destinationType.checkType(a.getType());
			if (checkType == false) {
				
			}
			
			
			
			
			
		}
		return true;
	}
	
	@Override
	public Boolean visitOperation_Simetric(Operation_SimetricContext ctx) {
		Variable a = mapCtxObj.get(ctx.operation());
		a = new Variable(a.getType(), a.getValue() * -1);
		mapCtxObj.put(ctx, a);
		return true;
	}
	
	@Override
	public Boolean visitOperation_Add_Sub(Operation_Add_SubContext ctx) {
		Variable a = mapCtxObj.get(ctx.operation(0));
		Variable b = mapCtxObj.get(ctx.operation(1));
		
		// verify that types are equals before adding or subtracting 
		if (!a.getType().equals(b.getType())) {
			// if types are not equal, try to convert Variable 'b' Type into 'a' Type
			if (!b.convertTypeTo(a.getType())) {
				ErrorHandling.printError(ctx, "Type \"" + a.getType() + "\" is not compatible with \"" + b.getType() + "\"");
				return false;
			}
		}
		// types are equal adding and subtracting is possible
		if (ctx.op.equals("+")) {
			mapCtxObj.put(ctx, Variable.add(a, b));
		}
		else if (ctx.op.equals("-")) {
			mapCtxObj.put(ctx,  Variable.subtract(a, b));
		}
		return true;
	}
	
	@Override
	public Boolean visitOperation_Power(Operation_PowerContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_Power(ctx);
	}
	
	@Override
	public Boolean visitOperation_Var(Operation_VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_Var(ctx);
	}
	
	@Override
	public Boolean visitOperation_FunctionCall(Operation_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_FunctionCall(ctx);
	}
	
	@Override
	public Boolean visitOperation_ArrayAccess(Operation_ArrayAccessContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_ArrayAccess(ctx);
	}
	
	@Override
	public Boolean visitOperation_ArrayLength(Operation_ArrayLengthContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_ArrayLength(ctx);
	}
	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitOperation_NUMBER(Operation_NUMBERContext ctx) {
		Type numberType = new Type("number", "", 1.0);
		Double value = Double.parseDouble(ctx.NUMBER().getText());
		Variable a = new Variable(numberType, value);
		mapCtxObj.put(ctx, a);
		return true;
	}
	
	@Override
	public Boolean visitOperation_Modulus(Operation_ModulusContext ctx) {
		
	}

	@Override
	public Boolean visitOperation_Expr(Operation_ExprContext ctx) {
		String varName = ctx.var().ID().getText();
		
		// verify is Variable is declared and contained in symbolTable
		if (!symbolTable.containsKey(varName)) {
			ErrorHandling.printError(ctx, "Variable \"" + varName + "is not declared");
			return false;
		}
		
		// verify that Variable is initialized
		if (symbolTable.get(varName) == null) {
			ErrorHandling.printError(ctx, "Variable \"" + varName + "is not initialized");
			return false;
		}
		
		// Variable is declared
		Variable a = (Variable) symbolTable.get(ctx.var().ID().getText());
		
		mapCtxObj.put(ctx, a);
		return true;
	}

	

	@Override
	public Boolean visitOperation_Mult_Div(Operation_Mult_DivContext ctx) {
		Variable a = mapCtxObj.get(ctx.operation(0));
		Variable b = mapCtxObj.get(ctx.operation(1));
		
		
		return super.visitOperation_Mult_Div(ctx);
	}

	
	


// --------------------------------------------------------------------------------------------------------------------
// STRUCTURES - ARRAYS------------------------------------------------------------------	
// --------------------------------------------------------------------------------------------------------------------
	@Override
	public Boolean visitArrayDeclaration(ArrayDeclarationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitArrayDeclaration(ctx);
	}

	@Override
	public Boolean visitArrayType(ArrayTypeContext ctx) {
		// TODO Auto-generated method stub
		return super.visitArrayType(ctx);
	}

	@Override
	public Boolean visitArrayAccess(ArrayAccessContext ctx) {
		// TODO Auto-generated method stub
		return super.visitArrayAccess(ctx);
	}

	@Override
	public Boolean visitArrayLength(ArrayLengthContext ctx) {
		// TODO Auto-generated method stub
		return super.visitArrayLength(ctx);
	}

	
// --------------------------------------------------------------------------------------------------------------------
// VARS AND TYPES------------------------------------------------------------------------ 
// --------------------------------------------------------------------------------------------------------------------
	@Override
	public Boolean visitVar(VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitVar(ctx);
	}

	@Override
	public Boolean visitVarDeclaration(VarDeclarationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitVarDeclaration(ctx);
	}
	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitType_Number_Type(Type_Number_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.NUMBER_TYPE().getText());
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitType_Boolean_Type(Type_Boolean_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.BOOLEAN_TYPE().getText());
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitType_String_Type(Type_String_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.STRING_TYPE().getText());
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitType_Void_Type(Type_Void_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.VOID_TYPE().getText());
		return true;
	}

	@Override
	public Boolean visitType_ID_Type(Type_ID_TypeContext ctx) {
		// TODO Auto-generated method stub
		return super.visitType_ID_Type(ctx);
	}

	@Override
	public Boolean visitType_ArrayType(Type_ArrayTypeContext ctx) {
		// TODO Auto-generated method stub
		return super.visitType_ArrayType(ctx);
	}
	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitValue_Cast_Number(Value_Cast_NumberContext ctx) {
		String castType = ctx.cast().ID().getText();
		
		// verify that cast type exists
		if (!typesTable.containsKey(castType)) {
			ErrorHandling.printError("");
			return false;
		}
		
		// create new Variable and store it in mapCtxObj
		Type type = typesTable.get(castType);
		Double value = Double.parseDouble(ctx.NUMBER().getText());
		Variable a = new Variable(type, value);
		mapCtxObj.put(ctx, a);
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitValue_Number(Value_NumberContext ctx) {
		Double number = Double.parseDouble(ctx.NUMBER().getText());
		mapCtxObj.put(ctx, number);
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitValue_Boolean(Value_BooleanContext ctx) {
		Boolean b = Boolean.parseBoolean(ctx.BOOLEAN().getText());
		mapCtxObj.put(ctx, b);
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitValue_String(Value_StringContext ctx) {
		String str = getStringText(ctx.STRING().getText());
		mapCtxObj.put(ctx, str);
		return true;
	}
	
	@Override
	public Boolean visitValuesList(ValuesListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitValuesList(ctx);
	}
	
	@Override
	public Boolean visitCast(CastContext ctx) {
		
	}
// --------------------------------------------------------------------------------------------------------------------
// AUXILIAR FUNCTIONS ---------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------	

	public String getStringText(String str) {
		str = str.substring(1, str.length() -1);
		// FIXME escapes still need to be removed from antlr STRING token to have correct text.
		return str;
	}

}