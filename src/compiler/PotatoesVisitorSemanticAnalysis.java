package compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

import static java.lang.System.*;

import potatoesGrammar.PotatoesBaseVisitor;
import potatoesGrammar.PotatoesParser;
import potatoesGrammar.PotatoesParser.ArrayAccessContext;
import potatoesGrammar.PotatoesParser.ArrayDeclarationContext;
import potatoesGrammar.PotatoesParser.ArrayLengthContext;
import potatoesGrammar.PotatoesParser.ArrayTypeContext;
import potatoesGrammar.PotatoesParser.Assignment_ArrayContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_ComparisonContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_ComparisonContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_FunctionCallContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_OperationContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_ValueContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_Declaration_VarContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_OperationContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_ValueContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_ValueListContext;
import potatoesGrammar.PotatoesParser.Assignment_Var_VarContext;
import potatoesGrammar.PotatoesParser.Assingment_Var_FunctionCallContext;
import potatoesGrammar.PotatoesParser.CastContext;
import potatoesGrammar.PotatoesParser.Class_Content_AssignmentContext;
import potatoesGrammar.PotatoesParser.Class_Content_DeclarationContext;
import potatoesGrammar.PotatoesParser.Class_Content_FunctionContext;
import potatoesGrammar.PotatoesParser.Code_AssignmentContext;
import potatoesGrammar.PotatoesParser.Code_DeclarationContext;
import potatoesGrammar.PotatoesParser.Code_FunctionContext;
import potatoesGrammar.PotatoesParser.CompareOperatorContext;
import potatoesGrammar.PotatoesParser.ComparisonContext;
import potatoesGrammar.PotatoesParser.ConditionContext;
import potatoesGrammar.PotatoesParser.ControlFlowStatementContext;
import potatoesGrammar.PotatoesParser.Declaratio_AarrayContext;
import potatoesGrammar.PotatoesParser.Declaration_VarContext;
import potatoesGrammar.PotatoesParser.ForLoopContext;
import potatoesGrammar.PotatoesParser.FunctionCallContext;
import potatoesGrammar.PotatoesParser.FunctionContext;
import potatoesGrammar.PotatoesParser.FunctionReturnContext;
import potatoesGrammar.PotatoesParser.LogicalOperandContext;
import potatoesGrammar.PotatoesParser.LogicalOperationContext;
import potatoesGrammar.PotatoesParser.LogicalOperatorContext;
import potatoesGrammar.PotatoesParser.Operation_Add_SubContext;
import potatoesGrammar.PotatoesParser.Operation_ArrayAccessContext;
import potatoesGrammar.PotatoesParser.Operation_ArrayLengthContext;
import potatoesGrammar.PotatoesParser.Operation_ExprContext;
import potatoesGrammar.PotatoesParser.Operation_FunctionCallContext;
import potatoesGrammar.PotatoesParser.Operation_ModulusContext;
import potatoesGrammar.PotatoesParser.Operation_Mult_DivContext;
import potatoesGrammar.PotatoesParser.Operation_NumberContext;
import potatoesGrammar.PotatoesParser.Operation_ParenthesisContext;
import potatoesGrammar.PotatoesParser.Operation_PowerContext;
import potatoesGrammar.PotatoesParser.Operation_SimetricContext;
import potatoesGrammar.PotatoesParser.PrintContext;
import potatoesGrammar.PotatoesParser.ProgramContext;
import potatoesGrammar.PotatoesParser.Statement_AssignmentContext;
import potatoesGrammar.PotatoesParser.Statement_Control_Flow_StatementContext;
import potatoesGrammar.PotatoesParser.Statement_DeclarationContext;
import potatoesGrammar.PotatoesParser.Statement_FunctionCallContext;
import potatoesGrammar.PotatoesParser.Statement_Function_ReturnContext;
import potatoesGrammar.PotatoesParser.Statement_PrintContext;
import potatoesGrammar.PotatoesParser.TypeContext;
import potatoesGrammar.PotatoesParser.UsingContext;
import potatoesGrammar.PotatoesParser.ValueContext;
import potatoesGrammar.PotatoesParser.ValuesListContext;
import potatoesGrammar.PotatoesParser.VarContext;
import potatoesGrammar.PotatoesParser.VarDeclarationContext;
import potatoesGrammar.PotatoesParser.WhenCaseContext;
import potatoesGrammar.PotatoesParser.WhenContext;
import potatoesGrammar.PotatoesParser.WhileLoopContext;
import typesGrammar.TypesFileInfo;
import utils.*;
import utils.errorHandling.ErrorHandling;


public class PotatoesVisitorSemanticAnalysis extends PotatoesBaseVisitor<Boolean>  {
	
	// Static Field (Debug Only)
	private static final boolean debug = true;
	
	static String path;
	TypesFileInfo typesFileInfo; // initialized in visitUsing();
	Map<String, Type> typesTable = typesFileInfo.getTypesTable();
	
	protected ParseTreeProperty<Variable> mapVar = new ParseTreeProperty<>();
	protected static Map<String, Object> symbolTable = new HashMap<>();



@Override
	public Boolean visitCode_Declaration(Code_DeclarationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCode_Declaration(ctx);
	}

	@Override
	public Boolean visitCode_Assignment(Code_AssignmentContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCode_Assignment(ctx);
	}

	@Override
	public Boolean visitCode_Function(Code_FunctionContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCode_Function(ctx);
	}

	// --------------------------------------------------------------------------------------------------------------------
// MAIN RULES----------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------
	@Override // [LM] Done
	public Boolean visitProgram(ProgramContext ctx) {
		return visitChildren(ctx);
	}
	
	@Override // [LM] Done
	public Boolean visitUsing(UsingContext ctx) {
		path = getStringText(ctx.STRING().getText());
		typesFileInfo = new TypesFileInfo(path);
		return true;
	}

	@Override
	public Boolean visitCode_Declaration(Code_DeclarationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCode_Declaration(ctx);
	}

	@Override
	public Boolean visitCode_Assignment(Code_AssignmentContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCode_Assignment(ctx);
	}

	@Override
	public Boolean visitCode_Function(Code_FunctionContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCode_Function(ctx);
	}

// --------------------------------------------------------------------------------------------------------------------	
// CLASS - STATEMENTS-----------------------------------------------------------------------	
// --------------------------------------------------------------------------------------------------------------------	
	@Override
	public Boolean visitStatement_Declaration(Statement_DeclarationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStatement_Declaration(ctx);
	}

	@Override
	public Boolean visitStatement_Assignment(Statement_AssignmentContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStatement_Assignment(ctx);
	}

	@Override
	public Boolean visitStatement_Control_Flow_Statement(Statement_Control_Flow_StatementContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStatement_Control_Flow_Statement(ctx);
	}

	@Override
	public Boolean visitStatement_FunctionCall(Statement_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStatement_FunctionCall(ctx);
	}

	@Override
	public Boolean visitStatement_Function_Return(Statement_Function_ReturnContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStatement_Function_Return(ctx);
	}

	@Override
	public Boolean visitStatement_Print(Statement_PrintContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStatement_Print(ctx);
	}


// --------------------------------------------------------------------------------------------------------------------	
// CLASS - DECLARATIONS-----------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------	
	@Override
	public Boolean visitDeclaratio_Aarray(Declaratio_AarrayContext ctx) {
		// TODO Auto-generated method stub
		return super.visitDeclaratio_Aarray(ctx);
	}

	@Override
	public Boolean visitDeclaration_Var(Declaration_VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitDeclaration_Var(ctx);
	}
	
// --------------------------------------------------------------------------------------------------------------------
// CLASS - ASSIGNMENTS-----------------------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------	
	@Override
	public Boolean visitAssignment_Var_Declaration_Var(Assignment_Var_Declaration_VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Declaration_Var(ctx);
	}

	@Override
	public Boolean visitAssignment_Var_Declaration_Value(Assignment_Var_Declaration_ValueContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Declaration_Value(ctx);
	}

	@Override
	public Boolean visitAssignment_Var_Declaration_Comparison(Assignment_Var_Declaration_ComparisonContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Declaration_Comparison(ctx);
	}

	@Override
	public Boolean visitAssignment_Var_Declaration_Operation(Assignment_Var_Declaration_OperationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Declaration_Operation(ctx);
	}

	@Override
	public Boolean visitAssignment_Array(Assignment_ArrayContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Array(ctx);
	}

	@Override
	public Boolean visitAssignment_Var_Declaration_FunctionCall(Assignment_Var_Declaration_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Declaration_FunctionCall(ctx);
	}

	@Override
	public Boolean visitAssignment_Var_Var(Assignment_Var_VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Var(ctx);
	}

	@Override
	public Boolean visitAssignment_Var_Value(Assignment_Var_ValueContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Value(ctx);
	}

	@Override
	public Boolean visitAssignment_Var_Comparison(Assignment_Var_ComparisonContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Comparison(ctx);
	}

	@Override
	public Boolean visitAssignment_Var_Operation(Assignment_Var_OperationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Operation(ctx);
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
	@Override
	public Boolean visitControlFlowStatement(ControlFlowStatementContext ctx) {
		// TODO Auto-generated method stub
		return super.visitControlFlowStatement(ctx);
	}

	@Override
	public Boolean visitForLoop(ForLoopContext ctx) {
		// TODO Auto-generated method stub
		return super.visitForLoop(ctx);
	}

	@Override
	public Boolean visitWhileLoop(WhileLoopContext ctx) {
		// TODO Auto-generated method stub
		return super.visitWhileLoop(ctx);
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
	@Override
	public Boolean visitLogicalOperation(LogicalOperationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperation(ctx);
	}

	@Override
	public Boolean visitLogicalOperand(LogicalOperandContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperand(ctx);
	}

	@Override
	public Boolean visitLogicalOperator(LogicalOperatorContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperator(ctx);
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
	@Override
	public Boolean visitOperation_Modulus(Operation_ModulusContext ctx) {
		Variable a = mapVar.get(ctx.operation(0));
		Variable b = mapVar.get(ctx.operation(1));
		
		// verify that right side of mod operation is of Type Number
		if (b.getType().getCode() != 1) {
			ErrorHandling.printError(ctx, "Right side of mod operation has to be of Type Number");
			return false;
		}
		
		Double moddedValue = a.getValue() % b.getValue();
		a = new Variable (a.getType(), moddedValue);
		mapVar.put(ctx,  a);
		return true;
	}

	@Override
	public Boolean visitOperation_FunctionCall(Operation_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_FunctionCall(ctx);
	}

	@Override
	public Boolean visitOperation_Simetric(Operation_SimetricContext ctx) {
		Variable a = mapVar.get(ctx.operation());
		a = new Variable(a.getType(), a.getValue() * -1);
		mapVar.put(ctx, a);
		return true;
	}

	@Override
	public Boolean visitOperation_ArrayAccess(Operation_ArrayAccessContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_ArrayAccess(ctx);
	}

	@Override
	public Boolean visitOperation_NUMBER(Operation_NumberContext ctx) {
		Type numberType = new Type("Number", "", 1.0);
		Double code = Double.parseDouble(ctx.NUMBER().getText());
		Variable a = new Variable(numberType, code);
		mapVar.put(ctx, a);
		return true;
	}

	@Override
	public Boolean visitOperation_Parenthesis(Operation_ParenthesisContext ctx) {
		mapVar.put(ctx, mapVar.get(ctx.operation()));
		return true;
	}

	@Override
	public Boolean visitOperation_ArrayLength(Operation_ArrayLengthContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_ArrayLength(ctx);
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
		
		mapVar.put(ctx, a);
		return true;
	}

	@Override
	public Boolean visitOperation_Power(Operation_PowerContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_Power(ctx);
	}

	@Override
	public Boolean visitOperation_Mult_Div(Operation_Mult_DivContext ctx) {
		Variable a = mapVar.get(ctx.operation(0));
		Variable b = mapVar.get(ctx.operation(1));
		
		
		return super.visitOperation_Mult_Div(ctx);
	}

	@Override
	public Boolean visitOperation_Add_Sub(Operation_Add_SubContext ctx) {
		Variable a = mapVar.get(ctx.operation(0));
		Variable b = mapVar.get(ctx.operation(1));
		
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
			mapVar.put(ctx, Variable.add(a, b));
		}
		else if (ctx.op.equals("-")) {
			mapVar.put(ctx,  Variable.subtract(a, b));
		}
		return true;
	}
	
	@Override
	public Boolean visitCast(CastContext ctx) {
		Variable a = mapVar.get(ctx.operation());
		
		// cast is only possible if Variable is of Type Number (with code 1)
		if (a.getType().getCode() != 1) {
			ErrorHandling.printError(ctx, "Type \"" + a.getType() + "\" cannot be casted. Only Number type can be casted");
			return false;
		}
		
		// type is Number cast is possible
		mapVar.put(ctx, new Variable(typesTable.get(ctx.ID().getText())));
		return true;
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

	@Override
	public Boolean visitType(TypeContext ctx) {
		// TODO Auto-generated method stub
		return super.visitType(ctx);
	}

	@Override
	public Boolean visitValue(ValueContext ctx) {
		// TODO Auto-generated method stub
		return super.visitValue(ctx);
	}

	@Override
	public Boolean visitValuesList(ValuesListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitValuesList(ctx);
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