package compiler;

import potatoesGrammar.PotatoesParser;
import potatoesGrammar.PotatoesParserBaseVisitor;


public class PotatoesVisitorCompiler<T> extends PotatoesParserBaseVisitor<T>  {

	@Override public T visitProgram(PotatoesParser.ProgramContext ctx) { return visitChildren(ctx); }

	@Override public T visitCode(PotatoesParser.CodeContext ctx) { return visitChildren(ctx); }

	@Override public T visitHeaderDeclaration(PotatoesParser.HeaderDeclarationContext ctx) { return visitChildren(ctx); }

	@Override public T visitJavaCode(PotatoesParser.JavaCodeContext ctx) { return visitChildren(ctx); }
	
	@Override public T visitClassDeclaration(PotatoesParser.ClassDeclarationContext ctx) { return visitChildren(ctx); }

	@Override public T visitClassContent(PotatoesParser.ClassContentContext ctx) { return visitChildren(ctx); }

	@Override public T visitStatement_declaration(PotatoesParser.Statement_declarationContext ctx) { return visitChildren(ctx); }

	@Override public T visitStatement_assignment(PotatoesParser.Statement_assignmentContext ctx) { return visitChildren(ctx); }

	@Override public T visitStatement_controlFlowStatement(PotatoesParser.Statement_controlFlowStatementContext ctx) { return visitChildren(ctx); }

	@Override public T visitStatement_function_call(PotatoesParser.Statement_function_callContext ctx) { return visitChildren(ctx); }

	@Override public T visitDeclaration_array(PotatoesParser.Declaration_arrayContext ctx) { return visitChildren(ctx); }

	@Override public T visitDeclaration_var(PotatoesParser.Declaration_varContext ctx) { return visitChildren(ctx); }

	@Override public T visitAssignment_array(PotatoesParser.Assignment_arrayContext ctx) { return visitChildren(ctx); }

	@Override public T visitAssignement_varDeclaration(PotatoesParser.Assignement_varDeclarationContext ctx) { return visitChildren(ctx); }

	@Override public T visitAssigment_var(PotatoesParser.Assigment_varContext ctx) { return visitChildren(ctx); }

	@Override public T visitAssignment_operator(PotatoesParser.Assignment_operatorContext ctx) { return visitChildren(ctx); }

	@Override public T visitFunction(PotatoesParser.FunctionContext ctx) { return visitChildren(ctx); }

	@Override public T visitFunction_return(PotatoesParser.Function_returnContext ctx) { return visitChildren(ctx); }
	
	@Override public T visitFunction_call(PotatoesParser.Function_callContext ctx) { return visitChildren(ctx); }

	@Override public T visitControl_flow_statement(PotatoesParser.Control_flow_statementContext ctx) { return visitChildren(ctx); }

	@Override public T visitFor_loop(PotatoesParser.For_loopContext ctx) { return visitChildren(ctx); }

	@Override public T visitWhile_loop(PotatoesParser.While_loopContext ctx) { return visitChildren(ctx); }

	@Override public T visitWhen(PotatoesParser.WhenContext ctx) { return visitChildren(ctx); }

	@Override public T visitWhen_case(PotatoesParser.When_caseContext ctx) { return visitChildren(ctx); }

	@Override public T visitCondition(PotatoesParser.ConditionContext ctx) { return visitChildren(ctx); }

	@Override public T visitLogical_operation(PotatoesParser.Logical_operationContext ctx) { return visitChildren(ctx); }

	@Override public T visitLogical_operand(PotatoesParser.Logical_operandContext ctx) { return visitChildren(ctx); }

	@Override public T visitLogical_operator(PotatoesParser.Logical_operatorContext ctx) { return visitChildren(ctx); }

	@Override public T visitComparison(PotatoesParser.ComparisonContext ctx) { return visitChildren(ctx); }

	@Override public T visitCompare_operator(PotatoesParser.Compare_operatorContext ctx) { return visitChildren(ctx); }

	@Override public T visitOperation_mult_div(PotatoesParser.Operation_mult_divContext ctx) { return visitChildren(ctx); }

	@Override public T visitOperation_NUMBER(PotatoesParser.Operation_NUMBERContext ctx) { return visitChildren(ctx); }

	@Override public T visitOperation_expr(PotatoesParser.Operation_exprContext ctx) { return visitChildren(ctx); }

	@Override public T visitOperation_decrement(PotatoesParser.Operation_decrementContext ctx) { return visitChildren(ctx); }

	@Override public T visitOperation_add_sub(PotatoesParser.Operation_add_subContext ctx) { return visitChildren(ctx); }

	@Override public T visitOperation_increment(PotatoesParser.Operation_incrementContext ctx) { return visitChildren(ctx); }

	@Override public T visitOperation_modulus(PotatoesParser.Operation_modulusContext ctx) { return visitChildren(ctx); }

	@Override public T visitOperation_parenthesis(PotatoesParser.Operation_parenthesisContext ctx) { return visitChildren(ctx); }

	@Override public T visitOperation_power(PotatoesParser.Operation_powerContext ctx) { return visitChildren(ctx); }

	@Override public T visitArray_declaration(PotatoesParser.Array_declarationContext ctx) { return visitChildren(ctx); }

	@Override public T visitVar(PotatoesParser.VarContext ctx) { return visitChildren(ctx); }

	@Override public T visitVar_declaration(PotatoesParser.Var_declarationContext ctx) { return visitChildren(ctx); }

	@Override public T visitType(PotatoesParser.TypeContext ctx) { return visitChildren(ctx); }

	@Override public T visitValue(PotatoesParser.ValueContext ctx) { return visitChildren(ctx); }

	@Override public T visitValues_list(PotatoesParser.Values_listContext ctx) { return visitChildren(ctx); }
}