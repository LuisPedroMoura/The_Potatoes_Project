package compiler;

import org.stringtemplate.v4.*;

import potatoesGrammar.PotatoesBaseVisitor;
import potatoesGrammar.PotatoesParser.ArrayAccessContext;
import potatoesGrammar.PotatoesParser.ArrayDeclarationContext;
import potatoesGrammar.PotatoesParser.ArrayLengthContext;
import potatoesGrammar.PotatoesParser.ArrayTypeContext;
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
import potatoesGrammar.PotatoesParser.UsingContext;
import potatoesGrammar.PotatoesParser.ValuesListContext;
import potatoesGrammar.PotatoesParser.VarContext;
import potatoesGrammar.PotatoesParser.VarDeclarationContext;
import potatoesGrammar.PotatoesParser.WhenCaseContext;
import potatoesGrammar.PotatoesParser.WhenContext;
import potatoesGrammar.PotatoesParser.WhileLoopContext;
/**
 * <b>PotatoesCompiler</b><p>
 * 
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class PotatoesCompiler extends PotatoesBaseVisitor<ST> {
	
	protected STGroup stg = null;
	
	
	// --------------------------------------------------------------------------------------------------------------------
	// MAIN RULES----------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------
	
	//[MJ] DONE - don't delete
	@Override
	public ST visitProgram(ProgramContext ctx) {
		stg = new STGroupFile("java.stg");
	    ST classContent = stg.getInstanceOf("class");
	    classContent.add("name", "MyClass");
	    classContent.add("stat", visitChildren(ctx));
	      
	    return classContent;
	}
	
	
	@Override
	public ST visitUsing(UsingContext ctx) {
		// TODO Auto-generated method stub
		return super.visitUsing(ctx);
	}

	
	@Override
	public ST visitCode_Declaration(Code_DeclarationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCode_Declaration(ctx);
	}

	
	@Override
	public ST visitCode_Assignment(Code_AssignmentContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCode_Assignment(ctx);
	}


	@Override
	public ST visitCode_Function(Code_FunctionContext ctx) {
	
		return super.visitCode_Function(ctx);
	}

	
	// --------------------------------------------------------------------------------------------------------------------	
	// CLASS - STATEMENTS--------------------------------------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------	
	
	@Override
	public ST visitStatement_Declaration(Statement_DeclarationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStatement_Declaration(ctx);
	}

	
	@Override
	public ST visitStatement_Assignment(Statement_AssignmentContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStatement_Assignment(ctx);
	}

	
	@Override
	public ST visitStatement_Control_Flow_Statement(Statement_Control_Flow_StatementContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStatement_Control_Flow_Statement(ctx);
	}

	
	@Override
	public ST visitStatement_FunctionCall(Statement_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStatement_FunctionCall(ctx);
	}

	
	@Override
	public ST visitStatement_Function_Return(Statement_Function_ReturnContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStatement_Function_Return(ctx);
	}


	@Override
	public ST visitStatement_Print(Statement_PrintContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStatement_Print(ctx);
	}

	
	// --------------------------------------------------------------------------------------------------------------------	
	// CLASS - DECLARATIONS------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	

	@Override
	public ST visitDeclaration_array(Declaration_arrayContext ctx) {
		// TODO Auto-generated method stub
		return super.visitDeclaration_array(ctx);
	}


	@Override
	public ST visitDeclaration_Var(Declaration_VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitDeclaration_Var(ctx);
	}

	
	// --------------------------------------------------------------------------------------------------------------------
	// CLASS - ASSIGNMENTS-----------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	
	@Override
	public ST visitAssignment_Var_Declaration_Not_Boolean(Assignment_Var_Declaration_Not_BooleanContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Declaration_Not_Boolean(ctx);
	}


	@Override
	public ST visitAssignment_Var_Declaration_Value(Assignment_Var_Declaration_ValueContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Declaration_Value(ctx);
	}

	
	@Override
	public ST visitAssignment_Var_Declaration_Comparison(Assignment_Var_Declaration_ComparisonContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Declaration_Comparison(ctx);
	}

	
	@Override
	public ST visitAssignment_Var_Declaration_Operation(Assignment_Var_Declaration_OperationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Declaration_Operation(ctx);
	}


	@Override
	public ST visitAssignment_Var_Declaration_FunctionCall(Assignment_Var_Declaration_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Declaration_FunctionCall(ctx);
	}


	@Override
	public ST visitAssignment_Array_ValuesList(Assignment_Array_ValuesListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Array_ValuesList(ctx);
	}

	
	@Override
	public ST visitAssignment_Array_FunctionCall(Assignment_Array_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Array_FunctionCall(ctx);
	}

	
	@Override
	public ST visitAssignment_Var_Not_Boolean(Assignment_Var_Not_BooleanContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Not_Boolean(ctx);
	}

	
	@Override
	public ST visitAssignment_Var_Value(Assignment_Var_ValueContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Value(ctx);
	}

	
	@Override
	public ST visitAssignment_Var_Comparison(Assignment_Var_ComparisonContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Comparison(ctx);
	}

	
	@Override
	public ST visitAssignment_Var_Operation(Assignment_Var_OperationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_Operation(ctx);
	}

	
	@Override
	public ST visitAssignment_Var_ValueList(Assignment_Var_ValueListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_ValueList(ctx);
	}

	
	@Override
	public ST visitAssingment_Var_FunctionCall(Assingment_Var_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssingment_Var_FunctionCall(ctx);
	}
	
	
	// --------------------------------------------------------------------------------------------------------------------	
	// FUNCTIONS-----------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	
	@Override
	public ST visitCast(CastContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCast(ctx);
	}

	
	@Override
	public ST visitFunction(FunctionContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFunction(ctx);
	}

	
	@Override
	public ST visitFunctionReturn(FunctionReturnContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFunctionReturn(ctx);
	}

	
	@Override
	public ST visitFunctionCall(FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFunctionCall(ctx);
	}

	
	
	// --------------------------------------------------------------------------------------------------------------------
	// CONTROL FLOW STATMENTS----------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	
	@Override
	public ST visitControlFlowStatement(ControlFlowStatementContext ctx) {
		// TODO Auto-generated method stub
		return super.visitControlFlowStatement(ctx);
	}

	
	@Override
	public ST visitForLoop(ForLoopContext ctx) {
		// TODO Auto-generated method stub
		return super.visitForLoop(ctx);
	}

	
	@Override
	public ST visitWhileLoop(WhileLoopContext ctx) {
		// TODO Auto-generated method stub
		return super.visitWhileLoop(ctx);
	}

	
	@Override
	public ST visitWhen(WhenContext ctx) {
		// TODO Auto-generated method stub
		return super.visitWhen(ctx);
	}

	
	@Override
	public ST visitWhenCase(WhenCaseContext ctx) {
		// TODO Auto-generated method stub
		return super.visitWhenCase(ctx);
	}

	
	@Override
	public ST visitCondition(ConditionContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCondition(ctx);
	}

	
	// --------------------------------------------------------------------------------------------------------------------
	// LOGICAL OPERATIONS--------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------

	
	@Override
	public ST visitComparison(ComparisonContext ctx) {
		// TODO Auto-generated method stub
		return super.visitComparison(ctx);
	}

	
	@Override
	public ST visitCompareOperator(CompareOperatorContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCompareOperator(ctx);
	}

	
	
	// --------------------------------------------------------------------------------------------------------------------
	// OPERATIONS----------------------------------------------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------
	
	
	@Override
	public ST visitOperation_FunctionCall(Operation_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_FunctionCall(ctx);
	}

	
	@Override
	public ST visitOperation_NUMBER(Operation_NUMBERContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_NUMBER(ctx);
	}

	
	@Override
	public ST visitOperation_Simetric(Operation_SimetricContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_Simetric(ctx);
	}

	
	@Override
	public ST visitOperation_ArrayAccess(Operation_ArrayAccessContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_ArrayAccess(ctx);
	}

	
	@Override
	public ST visitOperation_Parenthesis(Operation_ParenthesisContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_Parenthesis(ctx);
	}

	
	@Override
	public ST visitOperation_Cast(Operation_CastContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_Cast(ctx);
	}

	
	@Override
	public ST visitOperation_Mult_Div_Mod(Operation_Mult_Div_ModContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_Mult_Div_Mod(ctx);
	}

	
	@Override
	public ST visitOperation_ArrayLength(Operation_ArrayLengthContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_ArrayLength(ctx);
	}

	
	@Override
	public ST visitOperation_Power(Operation_PowerContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_Power(ctx);
	}

	
	@Override
	public ST visitOperation_Var(Operation_VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_Var(ctx);
	}

	
	@Override
	public ST visitOperation_Add_Sub(Operation_Add_SubContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_Add_Sub(ctx);
	}

	
	
	// --------------------------------------------------------------------------------------------------------------------
	// STRUCTURES - ARRAYS-------------------------------------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------
	
	
	@Override
	public ST visitArrayDeclaration(ArrayDeclarationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitArrayDeclaration(ctx);
	}

	
	@Override
	public ST visitArrayType(ArrayTypeContext ctx) {
		// TODO Auto-generated method stub
		return super.visitArrayType(ctx);
	}

	
	@Override
	public ST visitArrayAccess(ArrayAccessContext ctx) {
		// TODO Auto-generated method stub
		return super.visitArrayAccess(ctx);
	}

	
	@Override
	public ST visitArrayLength(ArrayLengthContext ctx) {
		// TODO Auto-generated method stub
		return super.visitArrayLength(ctx);
	}

	
	// --------------------------------------------------------------------------------------------------------------------
	// VARS AND TYPES------------------------------------------------------------------------------------------------------ 
	// --------------------------------------------------------------------------------------------------------------------
	
	@Override
	public ST visitPrint(PrintContext ctx) {
		// TODO Auto-generated method stub
		return super.visitPrint(ctx);
	}

	
	@Override
	public ST visitVar(VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitVar(ctx);
	}

	
	@Override
	public ST visitVarDeclaration(VarDeclarationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitVarDeclaration(ctx);
	}

	
	@Override
	public ST visitValuesList(ValuesListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitValuesList(ctx);
	}
	
	
}
