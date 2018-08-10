/***************************************************************************************
*	Title: PotatoesProject - PotatoesFunctionCheck Source globalStatement
*	globalStatement version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Date: August-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package potatoesGrammar.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

import potatoesGrammar.grammar.PotatoesParser.*;


public class PotatoesFunctionsCheck extends PotatoesBaseVisitor<Boolean>  {
	
	Map<String, Function_IDContext> functionsCtx = new HashMap<>();
	Map<String, List<String>> functionsArgs = new HashMap<>();
	
	/**
	 * @return the Map functions
	 */
	public Map<String, Function_IDContext> getFunctionsCtx() {
		return functionsCtx;
	}
	
	public Map<String, List<String>> getFunctionsArgs() {
		return functionsArgs;
	}

	@Override
	public Boolean visitProgram(ProgramContext ctx) {
		Boolean valid = true;
		List<GlobalStatementContext> statInstructions = ctx.globalStatement();

		// Visit all globalStatement rules
		for (GlobalStatementContext c : statInstructions) {
			Boolean res = visit(c);
			valid = valid && res;
		}
		return valid;
	}

	@Override
	public Boolean visitGlobalStatement_Declaration(GlobalStatement_DeclarationContext ctx) {
		return true;
	}

	@Override
	public Boolean visitGlobalStatement_Assignment(GlobalStatement_AssignmentContext ctx) {
		return true;
	}

	@Override
	public Boolean visitGlobalStatement_Function(GlobalStatement_FunctionContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitFunction_Main(Function_MainContext ctx) {
		return true;
	}

	@Override
	public Boolean visitFunction_ID(Function_IDContext ctx) {
		String functionName = ctx.ID(0).getText();
		List<String> typesNames = new ArrayList<>();
		for (TypeContext type : ctx.type()) {
			typesNames.add(type.getText());
		}
		functionsCtx.put(functionName, ctx);
		functionsArgs.put(functionName, typesNames);
		return true;
	}
}
