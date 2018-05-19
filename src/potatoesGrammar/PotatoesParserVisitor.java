// Generated from PotatoesParser.g4 by ANTLR 4.7.1

	package potatoesGrammar;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PotatoesParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PotatoesParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(PotatoesParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCode(PotatoesParser.CodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#header_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeader_declaration(PotatoesParser.Header_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#javaCode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJavaCode(PotatoesParser.JavaCodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#class_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_declaration(PotatoesParser.Class_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#class_content}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_content(PotatoesParser.Class_contentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statement_declaration}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_declaration(PotatoesParser.Statement_declarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statement_assignment}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_assignment(PotatoesParser.Statement_assignmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statement_controlFlowStatement}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_controlFlowStatement(PotatoesParser.Statement_controlFlowStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code statement_function_call}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement_function_call(PotatoesParser.Statement_function_callContext ctx);
	/**
	 * Visit a parse tree produced by the {@code declaration_array}
	 * labeled alternative in {@link PotatoesParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration_array(PotatoesParser.Declaration_arrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code declaration_var}
	 * labeled alternative in {@link PotatoesParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration_var(PotatoesParser.Declaration_varContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_array}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_array(PotatoesParser.Assignment_arrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_varDeclaration_Var}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_varDeclaration_Var(PotatoesParser.Assignment_varDeclaration_VarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_varDeclaration_Value}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_varDeclaration_Value(PotatoesParser.Assignment_varDeclaration_ValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_var_var}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_var_var(PotatoesParser.Assignment_var_varContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignment_var_value}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_var_value(PotatoesParser.Assignment_var_valueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assigment_var_valueList}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssigment_var_valueList(PotatoesParser.Assigment_var_valueListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#assignment_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_operator(PotatoesParser.Assignment_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(PotatoesParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#function_return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_return(PotatoesParser.Function_returnContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#function_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_call(PotatoesParser.Function_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#control_flow_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControl_flow_statement(PotatoesParser.Control_flow_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#for_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_loop(PotatoesParser.For_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#while_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_loop(PotatoesParser.While_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#when}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhen(PotatoesParser.WhenContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#when_case}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhen_case(PotatoesParser.When_caseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(PotatoesParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#logical_operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogical_operation(PotatoesParser.Logical_operationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#logical_operand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogical_operand(PotatoesParser.Logical_operandContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#logical_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogical_operator(PotatoesParser.Logical_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparison(PotatoesParser.ComparisonContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#compare_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompare_operator(PotatoesParser.Compare_operatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_mult_div}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_mult_div(PotatoesParser.Operation_mult_divContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_NUMBER}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_NUMBER(PotatoesParser.Operation_NUMBERContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_expr}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_expr(PotatoesParser.Operation_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_decrement}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_decrement(PotatoesParser.Operation_decrementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_add_sub}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_add_sub(PotatoesParser.Operation_add_subContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_increment}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_increment(PotatoesParser.Operation_incrementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_modulus}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_modulus(PotatoesParser.Operation_modulusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_parenthesis}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_parenthesis(PotatoesParser.Operation_parenthesisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operation_power}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation_power(PotatoesParser.Operation_powerContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#array_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_declaration(PotatoesParser.Array_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#diamond_begin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiamond_begin(PotatoesParser.Diamond_beginContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#diamond_end}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiamond_end(PotatoesParser.Diamond_endContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#var}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar(PotatoesParser.VarContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#var_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar_declaration(PotatoesParser.Var_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(PotatoesParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(PotatoesParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link PotatoesParser#values_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValues_list(PotatoesParser.Values_listContext ctx);
}