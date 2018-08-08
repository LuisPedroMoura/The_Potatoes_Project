/***************************************************************************************
*	Title: PotatoesProject - PotatoesFunctionCheck Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Date: August-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package potatoesGrammar.grammar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

import potatoesGrammar.grammar.PotatoesParser.*;


public class PotatoesFunctionsCheck extends PotatoesBaseVisitor<Boolean>  {
	
	Map<String, ParserRuleContext> functions = new HashMap<>();
	
	/**
	 * @return the Map functions
	 */
	public Map<String, ParserRuleContext> getFunctions() {
		return functions;
	}

	@Override
	public Boolean visitProgram(ProgramContext ctx) {
		Boolean valid = true;
		List<CodeContext> codesInstructions = ctx.code();

		// Visit all code rules
		for (CodeContext c : codesInstructions) {
			Boolean res = visit(c);
			valid = valid && res;
		}
		return valid;
	}

	@Override
	public Boolean visitCode_Declaration(Code_DeclarationContext ctx) {
		return true;
	}

	@Override
	public Boolean visitCode_Assignment(Code_AssignmentContext ctx) {
		return true;
	}

	@Override
	public Boolean visitCode_Function(Code_FunctionContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitFunction_Main(Function_MainContext ctx) {
		return true;
	}

	@Override
	public Boolean visitFunction_ID(Function_IDContext ctx) {
		String functionName = ctx.ID().getText();
		functions.put(functionName, ctx);
		return true;
	}
}
