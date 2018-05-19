// Generated from PotatoesParser.g4 by ANTLR 4.7.1

	package potatoesGrammar;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PotatoesParser}.
 */
public interface PotatoesParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(PotatoesParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(PotatoesParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#code}.
	 * @param ctx the parse tree
	 */
	void enterCode(PotatoesParser.CodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#code}.
	 * @param ctx the parse tree
	 */
	void exitCode(PotatoesParser.CodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#header_declaration}.
	 * @param ctx the parse tree
	 */
	void enterHeader_declaration(PotatoesParser.Header_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#header_declaration}.
	 * @param ctx the parse tree
	 */
	void exitHeader_declaration(PotatoesParser.Header_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#javaCode}.
	 * @param ctx the parse tree
	 */
	void enterJavaCode(PotatoesParser.JavaCodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#javaCode}.
	 * @param ctx the parse tree
	 */
	void exitJavaCode(PotatoesParser.JavaCodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#class_declaration}.
	 * @param ctx the parse tree
	 */
	void enterClass_declaration(PotatoesParser.Class_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#class_declaration}.
	 * @param ctx the parse tree
	 */
	void exitClass_declaration(PotatoesParser.Class_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#class_content}.
	 * @param ctx the parse tree
	 */
	void enterClass_content(PotatoesParser.Class_contentContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#class_content}.
	 * @param ctx the parse tree
	 */
	void exitClass_content(PotatoesParser.Class_contentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statement_declaration}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement_declaration(PotatoesParser.Statement_declarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statement_declaration}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement_declaration(PotatoesParser.Statement_declarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statement_assignment}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement_assignment(PotatoesParser.Statement_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statement_assignment}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement_assignment(PotatoesParser.Statement_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statement_controlFlowStatement}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement_controlFlowStatement(PotatoesParser.Statement_controlFlowStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statement_controlFlowStatement}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement_controlFlowStatement(PotatoesParser.Statement_controlFlowStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code statement_function_call}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement_function_call(PotatoesParser.Statement_function_callContext ctx);
	/**
	 * Exit a parse tree produced by the {@code statement_function_call}
	 * labeled alternative in {@link PotatoesParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement_function_call(PotatoesParser.Statement_function_callContext ctx);
	/**
	 * Enter a parse tree produced by the {@code declaration_array}
	 * labeled alternative in {@link PotatoesParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration_array(PotatoesParser.Declaration_arrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code declaration_array}
	 * labeled alternative in {@link PotatoesParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration_array(PotatoesParser.Declaration_arrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code declaration_var}
	 * labeled alternative in {@link PotatoesParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration_var(PotatoesParser.Declaration_varContext ctx);
	/**
	 * Exit a parse tree produced by the {@code declaration_var}
	 * labeled alternative in {@link PotatoesParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration_var(PotatoesParser.Declaration_varContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignment_array}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_array(PotatoesParser.Assignment_arrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignment_array}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_array(PotatoesParser.Assignment_arrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignment_varDeclaration_Var}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_varDeclaration_Var(PotatoesParser.Assignment_varDeclaration_VarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignment_varDeclaration_Var}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_varDeclaration_Var(PotatoesParser.Assignment_varDeclaration_VarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignment_varDeclaration_Value}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_varDeclaration_Value(PotatoesParser.Assignment_varDeclaration_ValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignment_varDeclaration_Value}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_varDeclaration_Value(PotatoesParser.Assignment_varDeclaration_ValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignment_var_var}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_var_var(PotatoesParser.Assignment_var_varContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignment_var_var}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_var_var(PotatoesParser.Assignment_var_varContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignment_var_value}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_var_value(PotatoesParser.Assignment_var_valueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignment_var_value}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_var_value(PotatoesParser.Assignment_var_valueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assigment_var_valueList}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssigment_var_valueList(PotatoesParser.Assigment_var_valueListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assigment_var_valueList}
	 * labeled alternative in {@link PotatoesParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssigment_var_valueList(PotatoesParser.Assigment_var_valueListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#assignment_operator}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_operator(PotatoesParser.Assignment_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#assignment_operator}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_operator(PotatoesParser.Assignment_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(PotatoesParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(PotatoesParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#function_return}.
	 * @param ctx the parse tree
	 */
	void enterFunction_return(PotatoesParser.Function_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#function_return}.
	 * @param ctx the parse tree
	 */
	void exitFunction_return(PotatoesParser.Function_returnContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#function_call}.
	 * @param ctx the parse tree
	 */
	void enterFunction_call(PotatoesParser.Function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#function_call}.
	 * @param ctx the parse tree
	 */
	void exitFunction_call(PotatoesParser.Function_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#control_flow_statement}.
	 * @param ctx the parse tree
	 */
	void enterControl_flow_statement(PotatoesParser.Control_flow_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#control_flow_statement}.
	 * @param ctx the parse tree
	 */
	void exitControl_flow_statement(PotatoesParser.Control_flow_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#for_loop}.
	 * @param ctx the parse tree
	 */
	void enterFor_loop(PotatoesParser.For_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#for_loop}.
	 * @param ctx the parse tree
	 */
	void exitFor_loop(PotatoesParser.For_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#while_loop}.
	 * @param ctx the parse tree
	 */
	void enterWhile_loop(PotatoesParser.While_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#while_loop}.
	 * @param ctx the parse tree
	 */
	void exitWhile_loop(PotatoesParser.While_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#when}.
	 * @param ctx the parse tree
	 */
	void enterWhen(PotatoesParser.WhenContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#when}.
	 * @param ctx the parse tree
	 */
	void exitWhen(PotatoesParser.WhenContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#when_case}.
	 * @param ctx the parse tree
	 */
	void enterWhen_case(PotatoesParser.When_caseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#when_case}.
	 * @param ctx the parse tree
	 */
	void exitWhen_case(PotatoesParser.When_caseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(PotatoesParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(PotatoesParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#logical_operation}.
	 * @param ctx the parse tree
	 */
	void enterLogical_operation(PotatoesParser.Logical_operationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#logical_operation}.
	 * @param ctx the parse tree
	 */
	void exitLogical_operation(PotatoesParser.Logical_operationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#logical_operand}.
	 * @param ctx the parse tree
	 */
	void enterLogical_operand(PotatoesParser.Logical_operandContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#logical_operand}.
	 * @param ctx the parse tree
	 */
	void exitLogical_operand(PotatoesParser.Logical_operandContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#logical_operator}.
	 * @param ctx the parse tree
	 */
	void enterLogical_operator(PotatoesParser.Logical_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#logical_operator}.
	 * @param ctx the parse tree
	 */
	void exitLogical_operator(PotatoesParser.Logical_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterComparison(PotatoesParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitComparison(PotatoesParser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#compare_operator}.
	 * @param ctx the parse tree
	 */
	void enterCompare_operator(PotatoesParser.Compare_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#compare_operator}.
	 * @param ctx the parse tree
	 */
	void exitCompare_operator(PotatoesParser.Compare_operatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code operation_mult_div}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void enterOperation_mult_div(PotatoesParser.Operation_mult_divContext ctx);
	/**
	 * Exit a parse tree produced by the {@code operation_mult_div}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void exitOperation_mult_div(PotatoesParser.Operation_mult_divContext ctx);
	/**
	 * Enter a parse tree produced by the {@code operation_NUMBER}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void enterOperation_NUMBER(PotatoesParser.Operation_NUMBERContext ctx);
	/**
	 * Exit a parse tree produced by the {@code operation_NUMBER}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void exitOperation_NUMBER(PotatoesParser.Operation_NUMBERContext ctx);
	/**
	 * Enter a parse tree produced by the {@code operation_expr}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void enterOperation_expr(PotatoesParser.Operation_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code operation_expr}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void exitOperation_expr(PotatoesParser.Operation_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code operation_decrement}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void enterOperation_decrement(PotatoesParser.Operation_decrementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code operation_decrement}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void exitOperation_decrement(PotatoesParser.Operation_decrementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code operation_add_sub}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void enterOperation_add_sub(PotatoesParser.Operation_add_subContext ctx);
	/**
	 * Exit a parse tree produced by the {@code operation_add_sub}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void exitOperation_add_sub(PotatoesParser.Operation_add_subContext ctx);
	/**
	 * Enter a parse tree produced by the {@code operation_increment}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void enterOperation_increment(PotatoesParser.Operation_incrementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code operation_increment}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void exitOperation_increment(PotatoesParser.Operation_incrementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code operation_modulus}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void enterOperation_modulus(PotatoesParser.Operation_modulusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code operation_modulus}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void exitOperation_modulus(PotatoesParser.Operation_modulusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code operation_parenthesis}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void enterOperation_parenthesis(PotatoesParser.Operation_parenthesisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code operation_parenthesis}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void exitOperation_parenthesis(PotatoesParser.Operation_parenthesisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code operation_power}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void enterOperation_power(PotatoesParser.Operation_powerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code operation_power}
	 * labeled alternative in {@link PotatoesParser#operation}.
	 * @param ctx the parse tree
	 */
	void exitOperation_power(PotatoesParser.Operation_powerContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#array_declaration}.
	 * @param ctx the parse tree
	 */
	void enterArray_declaration(PotatoesParser.Array_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#array_declaration}.
	 * @param ctx the parse tree
	 */
	void exitArray_declaration(PotatoesParser.Array_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#diamond_begin}.
	 * @param ctx the parse tree
	 */
	void enterDiamond_begin(PotatoesParser.Diamond_beginContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#diamond_begin}.
	 * @param ctx the parse tree
	 */
	void exitDiamond_begin(PotatoesParser.Diamond_beginContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#diamond_end}.
	 * @param ctx the parse tree
	 */
	void enterDiamond_end(PotatoesParser.Diamond_endContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#diamond_end}.
	 * @param ctx the parse tree
	 */
	void exitDiamond_end(PotatoesParser.Diamond_endContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#var}.
	 * @param ctx the parse tree
	 */
	void enterVar(PotatoesParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#var}.
	 * @param ctx the parse tree
	 */
	void exitVar(PotatoesParser.VarContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#var_declaration}.
	 * @param ctx the parse tree
	 */
	void enterVar_declaration(PotatoesParser.Var_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#var_declaration}.
	 * @param ctx the parse tree
	 */
	void exitVar_declaration(PotatoesParser.Var_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(PotatoesParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(PotatoesParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(PotatoesParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(PotatoesParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link PotatoesParser#values_list}.
	 * @param ctx the parse tree
	 */
	void enterValues_list(PotatoesParser.Values_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link PotatoesParser#values_list}.
	 * @param ctx the parse tree
	 */
	void exitValues_list(PotatoesParser.Values_listContext ctx);
}