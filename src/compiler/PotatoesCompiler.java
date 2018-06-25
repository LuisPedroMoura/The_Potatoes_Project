package compiler;

import utils.*;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.stringtemplate.v4.*;

import potatoesGrammar.PotatoesBaseVisitor;
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
import potatoesGrammar.PotatoesParser.Type_ArrayTypeContext;
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
import potatoesGrammar.PotatoesParser.ValuesListContext;
import potatoesGrammar.PotatoesParser.VarContext;
import potatoesGrammar.PotatoesParser.VarDeclarationContext;
import potatoesGrammar.PotatoesParser.WhenCaseContext;
import potatoesGrammar.PotatoesParser.WhenContext;
import potatoesGrammar.PotatoesParser.WhileLoopContext;
/**
 * <b>PotatoesCompiler</b><p>
 * 
 * @author In�s Justo (84804), Luis Pedro Moura (83808), Maria Jo�o Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class PotatoesCompiler extends PotatoesBaseVisitor<ST> {
	
	protected STGroup stg = null;
	protected ParseTreeProperty<Object> mapCtxObj = PotatoesVisitorSemanticAnalysis.getMapCtxObj();
	
	private int varCounter = 0;
	
	// --------------------------------------------------------------------------------------------------------------------
	// MAIN RULES----------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------
	
	//[MJ] REVIEW -> visitChildren(ctx)?
	@Override
	public ST visitProgram(ProgramContext ctx) {
		stg = new STGroupFile("java.stg");
	    ST classContent = stg.getInstanceOf("class");
	    classContent.add("name", "MyClass");
	    classContent.add("stat", visitChildren(ctx));
	      
	    return classContent;
	}
	
	
	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitUsing(UsingContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	//[MJ] nothing to do, but don't delete
	@Override
	public ST visitCode_Declaration(Code_DeclarationContext ctx) {
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitCode_Assignment(Code_AssignmentContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
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
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitStatement_Assignment(Statement_AssignmentContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitStatement_Control_Flow_Statement(Statement_Control_Flow_StatementContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
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


	@Override
	public ST visitStatement_Print(Statement_PrintContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	// --------------------------------------------------------------------------------------------------------------------	
	// CLASS - DECLARATIONS------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	

	@Override
	public ST visitDeclaration_array(Declaration_arrayContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}


	
	@Override
	public ST visitDeclaration_Var(Declaration_VarContext ctx) {
		return visitChildren(ctx);
	}

	
	// --------------------------------------------------------------------------------------------------------------------
	// CLASS - ASSIGNMENTS-----------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	
	@Override
	public ST visitAssignment_Var_Declaration_Not_Boolean(Assignment_Var_Declaration_Not_BooleanContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}


	@Override
	public ST visitAssignment_Var_Declaration_Value(Assignment_Var_Declaration_ValueContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitAssignment_Var_Declaration_Comparison(Assignment_Var_Declaration_ComparisonContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitAssignment_Var_Declaration_Operation(Assignment_Var_Declaration_OperationContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}


	@Override
	public ST visitAssignment_Var_Declaration_FunctionCall(Assignment_Var_Declaration_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}


	@Override
	public ST visitAssignment_Array_ValuesList(Assignment_Array_ValuesListContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitAssignment_Array_FunctionCall(Assignment_Array_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitAssignment_Var_Not_Boolean(Assignment_Var_Not_BooleanContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitAssignment_Var_Value(Assignment_Var_ValueContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitAssignment_Var_Comparison(Assignment_Var_ComparisonContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitAssignment_Var_Operation(Assignment_Var_OperationContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitAssignment_Var_ValueList(Assignment_Var_ValueListContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
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
	public ST visitFunction(FunctionContext ctx) {
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

	@Override
	public ST visitPrint(PrintContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}
	
	// --------------------------------------------------------------------------------------------------------------------
	// CONTROL FLOW STATMENTS----------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	
	
	@Override
	public ST visitControlFlowStatement(ControlFlowStatementContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitForLoop(ForLoopContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitWhileLoop(WhileLoopContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
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

	
	@Override
	public ST visitCondition(ConditionContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	// --------------------------------------------------------------------------------------------------------------------
	// LOGICAL OPERATIONS--------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------

	
	@Override
	public ST visitComparison(ComparisonContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitCompareOperator(CompareOperatorContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	
	// --------------------------------------------------------------------------------------------------------------------
	// OPERATIONS----------------------------------------------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------
	
	
	@Override
	public ST visitOperation_FunctionCall(Operation_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitOperation_NUMBER(Operation_NUMBERContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitOperation_Simetric(Operation_SimetricContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitOperation_ArrayAccess(Operation_ArrayAccessContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitOperation_Parenthesis(Operation_ParenthesisContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitOperation_Cast(Operation_CastContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitOperation_Mult_Div_Mod(Operation_Mult_Div_ModContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitOperation_ArrayLength(Operation_ArrayLengthContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitOperation_Power(Operation_PowerContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitOperation_Var(Operation_VarContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitOperation_Add_Sub(Operation_Add_SubContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	
	// --------------------------------------------------------------------------------------------------------------------
	// STRUCTURES - ARRAYS-------------------------------------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------
	
	
	@Override
	public ST visitArrayDeclaration(ArrayDeclarationContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitArrayType(ArrayTypeContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitArrayAccess(ArrayAccessContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}

	
	@Override
	public ST visitArrayLength(ArrayLengthContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
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
		varDeclaration.add("var", "var"+varCounter);
		varCounter++;
		
		return varDeclaration;
	}
	
	
	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitType_Number_Type(Type_Number_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "number");
		return type;
	}


	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitType_Boolean_Type(Type_Boolean_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "boolean");
		return type;
	}


	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitType_String_Type(Type_String_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "string");
		return type;
	}


	//[MJ] REVIEW -> NOT SURE IF IT'S RIGHT
	@Override
	public ST visitType_Void_Type(Type_Void_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "void");
		return type;
	}


	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitType_ID_Type(Type_ID_TypeContext ctx) {
		ST type = stg.getInstanceOf("type");
		type.add("type", "id");
		return type;
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitType_ArrayType(potatoesGrammar.PotatoesParser.Type_ArrayTypeContext)
	 */
	@Override
	public ST visitType_ArrayType(Type_ArrayTypeContext ctx) {
		// TODO Auto-generated method stub
		return super.visitType_ArrayType(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitValue_Cast_Number(potatoesGrammar.PotatoesParser.Value_Cast_NumberContext)
	 */
	@Override
	public ST visitValue_Cast_Number(Value_Cast_NumberContext ctx) {
		// TODO Auto-generated method stub
		return super.visitValue_Cast_Number(ctx);
	}


	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitValue_Number(Value_NumberContext ctx) {
		Double d = (Double) mapCtxObj.get(ctx);
		ST value = stg.getInstanceOf("value");
		value.add("value", d);
		return value;
	}


	//[MJ] DONE -> review just to be sure everything is right right
	@Override
	public ST visitValue_Boolean(Value_BooleanContext ctx) {
		Boolean b = (Boolean) mapCtxObj.get(ctx);
		ST value = stg.getInstanceOf("value");
		value.add("value", b);
		return value;
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitValue_String(potatoesGrammar.PotatoesParser.Value_StringContext)
	 */
	@Override
	public ST visitValue_String(Value_StringContext ctx) {
		String s = (String) mapCtxObj.get(ctx);
		ST value = stg.getInstanceOf("value");
		value.add("value", s);
		return value;
	}

	
	@Override
	public ST visitValuesList(ValuesListContext ctx) {
		// TODO Auto-generated method stub
		return visitChildren(ctx);
	}


	//-------------------------------------------------------------------------------------------------------------------------------------
	//OUT OF PLACE-------------------------------------------------------------------------------------------------------------------------
	//OUT OF PLACE-------------------------------------------------------------------------------------------------------------------------
	//OUT OF PLACE-------------------------------------------------------------------------------------------------------------------------
	//OUT OF PLACE-------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------
		
	
	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitAssigmennt_Array_Var(potatoesGrammar.PotatoesParser.Assigmennt_Array_VarContext)
	 */
	@Override
	public ST visitAssigmennt_Array_Var(Assigmennt_Array_VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssigmennt_Array_Var(ctx);
	}

	
	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitAssignment_ArrayAccess_Not_Boolean(potatoesGrammar.PotatoesParser.Assignment_ArrayAccess_Not_BooleanContext)
	 */
	@Override
	public ST visitAssignment_ArrayAccess_Not_Boolean(Assignment_ArrayAccess_Not_BooleanContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_Not_Boolean(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitAssignment_ArrayAccess_Value(potatoesGrammar.PotatoesParser.Assignment_ArrayAccess_ValueContext)
	 */
	@Override
	public ST visitAssignment_ArrayAccess_Value(Assignment_ArrayAccess_ValueContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_Value(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitAssignment_ArrayAccess_Comparison(potatoesGrammar.PotatoesParser.Assignment_ArrayAccess_ComparisonContext)
	 */
	@Override
	public ST visitAssignment_ArrayAccess_Comparison(Assignment_ArrayAccess_ComparisonContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_Comparison(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitAssignment_ArrayAccess_Operation(potatoesGrammar.PotatoesParser.Assignment_ArrayAccess_OperationContext)
	 */
	@Override
	public ST visitAssignment_ArrayAccess_Operation(Assignment_ArrayAccess_OperationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_Operation(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitAssignment_ArrayAccess_ValueList(potatoesGrammar.PotatoesParser.Assignment_ArrayAccess_ValueListContext)
	 */
	@Override
	public ST visitAssignment_ArrayAccess_ValueList(Assignment_ArrayAccess_ValueListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_ValueList(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitAssingment_ArrayAccess_FunctionCall(potatoesGrammar.PotatoesParser.Assingment_ArrayAccess_FunctionCallContext)
	 */
	@Override
	public ST visitAssingment_ArrayAccess_FunctionCall(Assingment_ArrayAccess_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssingment_ArrayAccess_FunctionCall(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitLogicalOperation_Operation(potatoesGrammar.PotatoesParser.LogicalOperation_OperationContext)
	 */
	@Override
	public ST visitLogicalOperation_Operation(LogicalOperation_OperationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperation_Operation(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitLogicalOperation_Parenthesis(potatoesGrammar.PotatoesParser.LogicalOperation_ParenthesisContext)
	 */
	@Override
	public ST visitLogicalOperation_Parenthesis(LogicalOperation_ParenthesisContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperation_Parenthesis(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitLogicalOperation_logicalOperand(potatoesGrammar.PotatoesParser.LogicalOperation_logicalOperandContext)
	 */
	@Override
	public ST visitLogicalOperation_logicalOperand(LogicalOperation_logicalOperandContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperation_logicalOperand(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitLogicalOperand_Comparison(potatoesGrammar.PotatoesParser.LogicalOperand_ComparisonContext)
	 */
	@Override
	public ST visitLogicalOperand_Comparison(LogicalOperand_ComparisonContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperand_Comparison(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitLogicalOperand_Not_Comparison(potatoesGrammar.PotatoesParser.LogicalOperand_Not_ComparisonContext)
	 */
	@Override
	public ST visitLogicalOperand_Not_Comparison(LogicalOperand_Not_ComparisonContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperand_Not_Comparison(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitLogicalOperand_Var(potatoesGrammar.PotatoesParser.LogicalOperand_VarContext)
	 */
	@Override
	public ST visitLogicalOperand_Var(LogicalOperand_VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperand_Var(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitLogicalOperand_Not_Var(potatoesGrammar.PotatoesParser.LogicalOperand_Not_VarContext)
	 */
	@Override
	public ST visitLogicalOperand_Not_Var(LogicalOperand_Not_VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperand_Not_Var(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitLogicalOperand_Value(potatoesGrammar.PotatoesParser.LogicalOperand_ValueContext)
	 */
	@Override
	public ST visitLogicalOperand_Value(LogicalOperand_ValueContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperand_Value(ctx);
	}


	/* (non-Javadoc)
	 * @see potatoesGrammar.PotatoesBaseVisitor#visitLogicalOperand_Not_Value(potatoesGrammar.PotatoesParser.LogicalOperand_Not_ValueContext)
	 */
	@Override
	public ST visitLogicalOperand_Not_Value(LogicalOperand_Not_ValueContext ctx) {
		// TODO Auto-generated method stub
		return super.visitLogicalOperand_Not_Value(ctx);
	}


	
	
	
}
