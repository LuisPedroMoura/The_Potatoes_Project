// Generated from Potatoes.g4 by ANTLR 4.7.1

	package potatoesGrammar;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PotatoesParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PotatoesVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(PotatoesParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#using}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsing(PotatoesParser.UsingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code code_Declaration}
	 * labeled alternative in {@link PotatoesParser#code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCode_Declaration(PotatoesParser.Code_DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code code_Assignment}
	 * labeled alternative in {@link PotatoesParser#code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCode_Assignment(PotatoesParser.Code_AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code code_Function}
	 * labeled alternative in {@link PotatoesParser#code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCode_Function(PotatoesParser.Code_FunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statement_Declaration}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_Declaration(PotatoesParser.Statement_DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statement_Assignment}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_Assignment(PotatoesParser.Statement_AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statement_Control_Flow_Statement}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_Control_Flow_Statement(PotatoesParser.Statement_Control_Flow_StatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statement_FunctionCall}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_FunctionCall(PotatoesParser.Statement_FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statement_Function_Return}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_Function_Return(PotatoesParser.Statement_Function_ReturnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statement_Print}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_Print(PotatoesParser.Statement_PrintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code declaration_array}
	 * labeled alternative in {@link PotatoesParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration_array(PotatoesParser.Declaration_arrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code declaration_Var}
	 * labeled alternative in {@link PotatoesParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration_Var(PotatoesParser.Declaration_VarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_Var_Declaration_Not_Boolean}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_Var_Declaration_Not_Boolean(PotatoesParser.Assignment_Var_Declaration_Not_BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_Var_Declaration_Value}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_Var_Declaration_Value(PotatoesParser.Assignment_Var_Declaration_ValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_Var_Declaration_Comparison}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_Var_Declaration_Comparison(PotatoesParser.Assignment_Var_Declaration_ComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_Var_Declaration_Operation}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_Var_Declaration_Operation(PotatoesParser.Assignment_Var_Declaration_OperationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_Var_Declaration_FunctionCall}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_Var_Declaration_FunctionCall(PotatoesParser.Assignment_Var_Declaration_FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_Var_Not_Boolean}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_Var_Not_Boolean(PotatoesParser.Assignment_Var_Not_BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_Var_Value}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_Var_Value(PotatoesParser.Assignment_Var_ValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_Var_Comparison}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_Var_Comparison(PotatoesParser.Assignment_Var_ComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_Var_Operation}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_Var_Operation(PotatoesParser.Assignment_Var_OperationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_Var_ValueList}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_Var_ValueList(PotatoesParser.Assignment_Var_ValueListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assingment_Var_FunctionCall}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssingment_Var_FunctionCall(PotatoesParser.Assingment_Var_FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_Array_ValuesList}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_Array_ValuesList(PotatoesParser.Assignment_Array_ValuesListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_Array_FunctionCall}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_Array_FunctionCall(PotatoesParser.Assignment_Array_FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(PotatoesParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#functionReturn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionReturn(PotatoesParser.FunctionReturnContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(PotatoesParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#controlFlowStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlFlowStatement(PotatoesParser.ControlFlowStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#forLoop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForLoop(PotatoesParser.ForLoopContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#whileLoop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileLoop(PotatoesParser.WhileLoopContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#when}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhen(PotatoesParser.WhenContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#whenCase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenCase(PotatoesParser.WhenCaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(PotatoesParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#logicalOperation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOperation(PotatoesParser.LogicalOperationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#logicalOperand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOperand(PotatoesParser.LogicalOperandContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#logicalOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOperator(PotatoesParser.LogicalOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparison(PotatoesParser.ComparisonContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#compareOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompareOperator(PotatoesParser.CompareOperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_FunctionCall}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_FunctionCall(PotatoesParser.Operation_FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_NUMBER}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_NUMBER(PotatoesParser.Operation_NUMBERContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_Simetric}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_Simetric(PotatoesParser.Operation_SimetricContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_ArrayAccess}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_ArrayAccess(PotatoesParser.Operation_ArrayAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_Parenthesis}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_Parenthesis(PotatoesParser.Operation_ParenthesisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_Cast}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_Cast(PotatoesParser.Operation_CastContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_Mult_Div_Mod}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_Mult_Div_Mod(PotatoesParser.Operation_Mult_Div_ModContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_ArrayLength}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_ArrayLength(PotatoesParser.Operation_ArrayLengthContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_Power}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_Power(PotatoesParser.Operation_PowerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_Var}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_Var(PotatoesParser.Operation_VarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_Add_Sub}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_Add_Sub(PotatoesParser.Operation_Add_SubContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#arrayDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayDeclaration(PotatoesParser.ArrayDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#arrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayType(PotatoesParser.ArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#arrayAccess}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayAccess(PotatoesParser.ArrayAccessContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#arrayLength}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLength(PotatoesParser.ArrayLengthContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#print}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrint(PotatoesParser.PrintContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#var}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar(PotatoesParser.VarContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclaration(PotatoesParser.VarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code type_Number_Type}
	 * labeled alternative in {@link PotatoesParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_Number_Type(PotatoesParser.Type_Number_TypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code type_Boolean_Type}
	 * labeled alternative in {@link PotatoesParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_Boolean_Type(PotatoesParser.Type_Boolean_TypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code type_String_Type}
	 * labeled alternative in {@link PotatoesParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_String_Type(PotatoesParser.Type_String_TypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code type_Void_Type}
	 * labeled alternative in {@link PotatoesParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_Void_Type(PotatoesParser.Type_Void_TypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code type_ID_Type}
	 * labeled alternative in {@link PotatoesParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_ID_Type(PotatoesParser.Type_ID_TypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code type_ArrayType}
	 * labeled alternative in {@link PotatoesParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_ArrayType(PotatoesParser.Type_ArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code value_Cast_Number}
	 * labeled alternative in {@link PotatoesParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_Cast_Number(PotatoesParser.Value_Cast_NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code value_Number}
	 * labeled alternative in {@link PotatoesParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_Number(PotatoesParser.Value_NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code value_Boolean}
	 * labeled alternative in {@link PotatoesParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_Boolean(PotatoesParser.Value_BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code value_String}
	 * labeled alternative in {@link PotatoesParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_String(PotatoesParser.Value_StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#valuesList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValuesList(PotatoesParser.ValuesListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#cast}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCast(PotatoesParser.CastContext ctx);
}