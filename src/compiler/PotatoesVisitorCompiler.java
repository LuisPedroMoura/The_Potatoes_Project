package compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.lang.System.*;
import potatoesGrammar.PotatoesParser;
import potatoesGrammar.PotatoesParser.Assigment_var_valueListContext;
import potatoesGrammar.PotatoesParser.Assignment_varDeclaration_ValueContext;
import potatoesGrammar.PotatoesParser.Assignment_varDeclaration_VarContext;
import potatoesGrammar.PotatoesParser.Assignment_var_valueContext;
import potatoesGrammar.PotatoesParser.Assignment_var_varContext;
import potatoesGrammar.PotatoesParser.Class_contentContext;
import potatoesGrammar.PotatoesParser.CodeContext;
import potatoesGrammar.PotatoesParser.Header_declarationContext;
import potatoesGrammar.PotatoesParser.ProgramContext;
import potatoesGrammar.PotatoesParser.Statement_assignmentContext;
import potatoesGrammar.PotatoesParser.Statement_declarationContext;
import potatoesGrammar.PotatoesParserBaseVisitor;
import utils.*;




public class PotatoesVisitorCompiler extends PotatoesParserBaseVisitor<Boolean>  {
	
	protected static Map<String, Object> symbolTable = new HashMap<>();
	
//	@Override public Boolean visitProgram(PotatoesParser.ProgramContext ctx) {
//		return visitChildren(ctx);
//	}

	
//	@Override public Boolean visitCode(PotatoesParser.CodeContext ctx) {
//		return visitChildren(ctx);
//	}

	
//	@Override public Boolean visitHeaderDeclaration(PotatoesParser.HeaderDeclarationContext ctx) {
//		return visitChildren(ctx);
//	}

	
	@Override public Boolean visitJavaCode(PotatoesParser.JavaCodeContext ctx) {
		// [LM] javaCode rule text is to be copied to compiled Java file
		visitChildren(ctx);
		return true;
	}



	@Override public Boolean visitClass_content(Class_contentContext ctx) {
		return visitChildren(ctx);
	}




	@Override
	public Boolean visitAssignment_varDeclaration_Var(Assignment_varDeclaration_VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_varDeclaration_Var(ctx);
	}


	@Override
	public Boolean visitAssignment_varDeclaration_Value(Assignment_varDeclaration_ValueContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_varDeclaration_Value(ctx);
	}


	@Override
	public Boolean visitAssignment_var_var(Assignment_var_varContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_var_var(ctx);
	}


	@Override public Boolean visitAssignment_var_value(Assignment_var_valueContext ctx) {
		// [LM] this functions escalates in complexity.
		// [LM] Sugestion: create auxiliary functions to validate minor things
		// [LM] it is needed to address problem of distinguishing var from value in right side of assignment
		
		String varName = ctx.var().ID().getText();
		String operator = ctx.assignment_operator().getText();
		String varValue = ctx.value().getText();
		if (!symbolTable.containsKey(varName)) {
			if (varType....)
			symbolTable.put(varName,);
		}
		else {
			
			if (symbolTable.get(varName) instanceof String && ctx.value().) {
				switch(operator) {
					case "=":
						//symbolTable.put(varName, ctx. .getText())
						break;
					case "+=":
						//symbolTable.put(varName, symbolTable.get(varName) + ctx.getText();
						break;
					default:
						return false;
				}

			}
			else {
				err.println("incompatible types");
				return false;
			}
			
			if (symbolTable.get(varName) instanceof Number && ctx.varisNumber()) {
				if (operator.equals("=")) {
					//symbolTable.put(varName, new Quantity())
				}
				else if (operator.equals("+=")){
					//symbolTable.put(varName, symbolTable.get(varName) + ctx.getText();
				}
			}
			else {
				err.println("incompatible types");
				return false;
			}
			
			//varValue = symbolTable.get(varName);
			
			if (symbolTable.containsKey(varName) && operator.equals("=")) {
				err.println("variable is already inicialized");
				return false;
			}
			else if (operator.equals("+=")) {
				//symbolTable.put(varName, );
			}
		}
		return true;
	}


	@Override
	public Boolean visitAssigment_var_valueList(Assigment_var_valueListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssigment_var_valueList(ctx);
	}


	@Override public Boolean visitClass_declaration(PotatoesParser.Class_declarationContext ctx) {
		// [LM] Simple Class declaration to be copied to compiled Java file
		visitChildren(ctx);
		return true;
	}


	
//	@Override public Boolean visitStatement_declaration(PotatoesParser.Statement_declarationContext ctx) {
//		return visitChildren(ctx);
//	}

	
//	@Override public Boolean visitStatement_assignment(PotatoesParser.Statement_assignmentContext ctx) {
//		return visitChildren(ctx);
//	}


	@Override public Boolean visitStatement_controlFlowStatement(PotatoesParser.Statement_controlFlowStatementContext ctx) { return visitChildren(ctx); }

	
	@Override public Boolean visitStatement_function_call(PotatoesParser.Statement_function_callContext ctx) { return visitChildren(ctx); }

	
	@Override public Boolean visitDeclaration_array(PotatoesParser.Declaration_arrayContext ctx) {
		visitChildren(ctx);
		return true;
	}

	@Override public Boolean visitDeclaration_var(PotatoesParser.Declaration_varContext ctx) {
		visitChildren(ctx);
		return true;
	}

	
	@Override public Boolean visitAssignment_array(PotatoesParser.Assignment_arrayContext ctx) {
		// [LM] to be completed, complex parsing of values to array... Intended error to mark place
		
		
		// get variables
		String arrayName = ctx.array_declaration().var().getText();
		String arrayType = ctx.array_declaration().type().getText();
		String[] arrayValues = ctx.values_list().get;
		
		// validate if assigned variable exists
		if (symbolTable.containsKey(arrayName)) {
			err.println("variable is already initialized");
			return false;
		}
		
		// create lists for all possible cases
		// [LM] is declaration without type possible, to avoid creating multiple lists?
		List<String> arrayString;
		List<Quantity> arrayQuantity;
		
		
		if (arrayType.equals("String")){
			arrayString = new ArrayList<>();
			if ()
			Collections.addAll(arrayString, arrayValues);
			symbolTable.put(arrayName, arrayString);
		}
		else if (arrayType.equals("Number")) {
			arrayQuantity = new ArrayList<>();
			for (int i = 0; i < arrayValues.length; i++) {
				// [LM] Quantity object creation from Parser yet to be solved
				arrayQuantity.add(new Quantity());
			}
			symbolTable.put(arrayName, arrayQuantity);
		}
		else {
			err.println("Array accepts only NUmber or String type");
			return false;
		}
		
		return true;
	}

	
	@Override public Boolean visitAssignement_varDeclaration(PotatoesParser.Assignement_varDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override public Boolean visitAssigment_var(PotatoesParser.Assigment_varContext ctx) {
		
		
	}

	@Override public Boolean visitAssignment_operator(PotatoesParser.Assignment_operatorContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitFunction(PotatoesParser.FunctionContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitFunction_return(PotatoesParser.Function_returnContext ctx) { return visitChildren(ctx); }
	
	@Override public Boolean visitFunction_call(PotatoesParser.Function_callContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitControl_flow_statement(PotatoesParser.Control_flow_statementContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitFor_loop(PotatoesParser.For_loopContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitWhile_loop(PotatoesParser.While_loopContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitWhen(PotatoesParser.WhenContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitWhen_case(PotatoesParser.When_caseContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitCondition(PotatoesParser.ConditionContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitLogical_operation(PotatoesParser.Logical_operationContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitLogical_operand(PotatoesParser.Logical_operandContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitLogical_operator(PotatoesParser.Logical_operatorContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitComparison(PotatoesParser.ComparisonContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitCompare_operator(PotatoesParser.Compare_operatorContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitOperation_mult_div(PotatoesParser.Operation_mult_divContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitOperation_NUMBER(PotatoesParser.Operation_NUMBERContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitOperation_expr(PotatoesParser.Operation_exprContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitOperation_decrement(PotatoesParser.Operation_decrementContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitOperation_add_sub(PotatoesParser.Operation_add_subContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitOperation_increment(PotatoesParser.Operation_incrementContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitOperation_modulus(PotatoesParser.Operation_modulusContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitOperation_parenthesis(PotatoesParser.Operation_parenthesisContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitOperation_power(PotatoesParser.Operation_powerContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitArray_declaration(PotatoesParser.Array_declarationContext ctx) {
		// [LM] what return should be used?
		if (!symbolTable.containsKey(ctx.var().ID().getText())) {
			symbolTable.put(ctx.var().ID().getText(), null);
		}
		else {
			err.println("variable is already initialized");
		}
		return visitChildren(ctx);
	}

	@Override public Boolean visitVar(PotatoesParser.VarContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitVar_declaration(PotatoesParser.Var_declarationContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitType(PotatoesParser.TypeContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitValue(PotatoesParser.ValueContext ctx) { return visitChildren(ctx); }

	@Override public Boolean visitValues_list(PotatoesParser.Values_listContext ctx) { return visitChildren(ctx); }


	// AUXILIAR FUNCTIONS ---------------------------------------------------------------------------------------------
	


}