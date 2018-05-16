package compiler;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import potatoesGrammar.PotatoesParser;
import potatoesGrammar.PotatoesParserBaseListener;
import utils.Quantity;


public class PotatoesListenerCompiler extends PotatoesParserBaseListener {
	
	protected ParseTreeProperty<Quantity> mapQuantity = new ParseTreeProperty<>();
	protected Map<String, Object> symbolTable = new HashMap<>();

	//@Override public void enterProgram(PotatoesParser.ProgramContext ctx) { }

	//@Override public void exitProgram(PotatoesParser.ProgramContext ctx) { }
	
	//@Override public void enterCode(PotatoesParser.CodeContext ctx) { }

	//@Override public void exitCode(PotatoesParser.CodeContext ctx) { }

	//@Override public void enterHeaderDeclaration(PotatoesParser.HeaderDeclarationContext ctx) { }

	@Override public void exitHeaderDeclaration(PotatoesParser.HeaderDeclarationContext ctx) {
		// [LM] javaCode rule text is to be copied to compiled Java file
		
	}

	@Override public void enterJavaCode(PotatoesParser.JavaCodeContext ctx) { }

	@Override public void exitJavaCode(PotatoesParser.JavaCodeContext ctx) { }

	@Override public void enterClassDeclaration(PotatoesParser.ClassDeclarationContext ctx) { }

	@Override public void exitClassDeclaration(PotatoesParser.ClassDeclarationContext ctx) { }

	@Override public void enterClassContent(PotatoesParser.ClassContentContext ctx) { }

	@Override public void exitClassContent(PotatoesParser.ClassContentContext ctx) { }

	@Override public void enterStatement_declaration(PotatoesParser.Statement_declarationContext ctx) { }

	@Override public void exitStatement_declaration(PotatoesParser.Statement_declarationContext ctx) { }

	@Override public void enterStatement_assignment(PotatoesParser.Statement_assignmentContext ctx) { }

	@Override public void exitStatement_assignment(PotatoesParser.Statement_assignmentContext ctx) { }

	@Override public void enterStatement_controlFlowStatement(PotatoesParser.Statement_controlFlowStatementContext ctx) { }

	@Override public void exitStatement_controlFlowStatement(PotatoesParser.Statement_controlFlowStatementContext ctx) { }

	@Override public void enterStatement_function_call(PotatoesParser.Statement_function_callContext ctx) { }

	@Override public void exitStatement_function_call(PotatoesParser.Statement_function_callContext ctx) { }

	@Override public void enterDeclaration_array(PotatoesParser.Declaration_arrayContext ctx) { }

	@Override public void exitDeclaration_array(PotatoesParser.Declaration_arrayContext ctx) { }

	@Override public void enterDeclaration_var(PotatoesParser.Declaration_varContext ctx) { }

	@Override public void exitDeclaration_var(PotatoesParser.Declaration_varContext ctx) { }

	@Override public void enterAssignment_array(PotatoesParser.Assignment_arrayContext ctx) { }

	@Override public void exitAssignment_array(PotatoesParser.Assignment_arrayContext ctx) { }

	@Override public void enterAssignement_varDeclaration(PotatoesParser.Assignement_varDeclarationContext ctx) { }

	@Override public void exitAssignement_varDeclaration(PotatoesParser.Assignement_varDeclarationContext ctx) { }

	@Override public void enterAssigment_var(PotatoesParser.Assigment_varContext ctx) { }

	@Override public void exitAssigment_var(PotatoesParser.Assigment_varContext ctx) { }

	@Override public void enterAssignment_operator(PotatoesParser.Assignment_operatorContext ctx) { }

	@Override public void exitAssignment_operator(PotatoesParser.Assignment_operatorContext ctx) { }

	@Override public void enterFunction(PotatoesParser.FunctionContext ctx) { }

	@Override public void exitFunction(PotatoesParser.FunctionContext ctx) { }

	@Override public void enterFunction_return(PotatoesParser.Function_returnContext ctx) { }

	@Override public void exitFunction_return(PotatoesParser.Function_returnContext ctx) { }

	@Override public void enterFunction_call(PotatoesParser.Function_callContext ctx) { }

	@Override public void exitFunction_call(PotatoesParser.Function_callContext ctx) { }

	@Override public void enterControl_flow_statement(PotatoesParser.Control_flow_statementContext ctx) { }

	@Override public void exitControl_flow_statement(PotatoesParser.Control_flow_statementContext ctx) { }

	@Override public void enterFor_loop(PotatoesParser.For_loopContext ctx) { }

	@Override public void exitFor_loop(PotatoesParser.For_loopContext ctx) { }

	@Override public void enterWhile_loop(PotatoesParser.While_loopContext ctx) { }

	@Override public void exitWhile_loop(PotatoesParser.While_loopContext ctx) { }

	@Override public void enterWhen(PotatoesParser.WhenContext ctx) { }

	@Override public void exitWhen(PotatoesParser.WhenContext ctx) { }

	@Override public void enterWhen_case(PotatoesParser.When_caseContext ctx) { }

	@Override public void exitWhen_case(PotatoesParser.When_caseContext ctx) { }

	@Override public void enterCondition(PotatoesParser.ConditionContext ctx) { }

	@Override public void exitCondition(PotatoesParser.ConditionContext ctx) { }

	@Override public void enterLogical_operation(PotatoesParser.Logical_operationContext ctx) { }

	@Override public void exitLogical_operation(PotatoesParser.Logical_operationContext ctx) { }

	@Override public void enterLogical_operand(PotatoesParser.Logical_operandContext ctx) { }

	@Override public void exitLogical_operand(PotatoesParser.Logical_operandContext ctx) { }

	@Override public void enterLogical_operator(PotatoesParser.Logical_operatorContext ctx) { }

	@Override public void exitLogical_operator(PotatoesParser.Logical_operatorContext ctx) { }

	@Override public void enterComparison(PotatoesParser.ComparisonContext ctx) { }

	@Override public void exitComparison(PotatoesParser.ComparisonContext ctx) { }

	@Override public void enterCompare_operator(PotatoesParser.Compare_operatorContext ctx) { }

	@Override public void exitCompare_operator(PotatoesParser.Compare_operatorContext ctx) { }

	@Override public void enterOperation_mult_div(PotatoesParser.Operation_mult_divContext ctx) { }

	@Override public void exitOperation_mult_div(PotatoesParser.Operation_mult_divContext ctx) { }
	
	@Override public void enterOperation_NUMBER(PotatoesParser.Operation_NUMBERContext ctx) { }
	
	@Override public void exitOperation_NUMBER(PotatoesParser.Operation_NUMBERContext ctx) { }
	
	@Override public void enterOperation_expr(PotatoesParser.Operation_exprContext ctx) { }
	
	@Override public void exitOperation_expr(PotatoesParser.Operation_exprContext ctx) { }
	
	@Override public void enterOperation_decrement(PotatoesParser.Operation_decrementContext ctx) { }
	
	@Override public void exitOperation_decrement(PotatoesParser.Operation_decrementContext ctx) { }
	
	@Override public void enterOperation_add_sub(PotatoesParser.Operation_add_subContext ctx) { }
	
	@Override public void exitOperation_add_sub(PotatoesParser.Operation_add_subContext ctx) { }
	
	@Override public void enterOperation_increment(PotatoesParser.Operation_incrementContext ctx) { }
	
	@Override public void exitOperation_increment(PotatoesParser.Operation_incrementContext ctx) { }
	
	@Override public void enterOperation_modulus(PotatoesParser.Operation_modulusContext ctx) { }
	
	@Override public void exitOperation_modulus(PotatoesParser.Operation_modulusContext ctx) { }
	
	@Override public void enterOperation_parenthesis(PotatoesParser.Operation_parenthesisContext ctx) { }
	
	@Override public void exitOperation_parenthesis(PotatoesParser.Operation_parenthesisContext ctx) { }
	
	@Override public void enterOperation_power(PotatoesParser.Operation_powerContext ctx) { }
	
	@Override public void exitOperation_power(PotatoesParser.Operation_powerContext ctx) { }
	
	@Override public void enterArray_declaration(PotatoesParser.Array_declarationContext ctx) { }
	
	@Override public void exitArray_declaration(PotatoesParser.Array_declarationContext ctx) { }
	
	@Override public void enterVar(PotatoesParser.VarContext ctx) { }
	
	@Override public void exitVar(PotatoesParser.VarContext ctx) { }
	
	@Override public void enterVar_declaration(PotatoesParser.Var_declarationContext ctx) { }
	
	@Override public void exitVar_declaration(PotatoesParser.Var_declarationContext ctx) { }
	
	@Override public void enterType(PotatoesParser.TypeContext ctx) { }
	
	@Override public void exitType(PotatoesParser.TypeContext ctx) { }
	
	@Override public void enterValue(PotatoesParser.ValueContext ctx) { }
	
	@Override public void exitValue(PotatoesParser.ValueContext ctx) { }
	
	@Override public void enterValues_list(PotatoesParser.Values_listContext ctx) { }
	
	@Override public void exitValues_list(PotatoesParser.Values_listContext ctx) { }

	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	
	@Override public void visitTerminal(TerminalNode node) { }

	@Override public void visitErrorNode(ErrorNode node) { }
}