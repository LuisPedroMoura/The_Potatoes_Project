package compiler;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

import potatoesGrammar.PotatoesBaseVisitor;
import potatoesGrammar.PotatoesParser.*;
import typesGrammar.TypesFileInfo;
import utils.Type;
import utils.Variable;
import utils.errorHandling.ErrorHandling;

/**
 * 
 * <b>PotatoesSemanticCheck</b><p>
 * This class performs a semantic analysis for a Parse Tree generated from a Potatoes Source File<p>
 * 
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class PotatoesSemanticCheck extends PotatoesBaseVisitor<Boolean>  {

	// Static Constant (Debug Only)
	private static final boolean debug = false;

	// --------------------------------------------------------------------------
	// Static Fields
	static String path;
	private static 	 TypesFileInfo typesFileInfo; // initialized in visitUsing();
	private static 	 Map<String, Type> typesTable;

	protected static ParseTreeProperty<Object> mapCtxObj = new ParseTreeProperty<>();
	protected static Map<String, Object> symbolTable = new HashMap<>();

	private static Type destinationType;

	// --------------------------------------------------------------------------
	// Getters
	public static ParseTreeProperty<Object> getMapCtxObj(){
		return mapCtxObj;
	}

	public static TypesFileInfo getTypesFileInfo() {
		return typesFileInfo;
	}

	// --------------------------------------------------------------------------
	// Main Rules 
	@Override 
	public Boolean visitProgram(ProgramContext ctx) {
		Boolean valid = visit(ctx.using());
		List<CodeContext> codesInstructions = ctx.code();

		// Visit all code rules
		for (CodeContext c : codesInstructions) {
			Boolean res = visit(c);
			valid = valid && res;
		}
		return valid;
	}

	@Override 
	public Boolean visitUsing(UsingContext ctx) {
		// Get information from the types file
		path = getStringText(ctx.STRING().getText());
		typesFileInfo = new TypesFileInfo(path);
		typesTable = typesFileInfo.getTypesTable();
		mapCtxObj.put(ctx, path);

		// Debug
		if (debug) {
			ErrorHandling.printInfo(ctx, "Types File path is: " + path);
			ErrorHandling.printInfo(ctx, typesFileInfo.toString());
		}

		return true;
	}

	@Override
	public Boolean visitCode_Declaration(Code_DeclarationContext ctx) {
		return visit(ctx.varDeclaration());
	}

	@Override 
	public Boolean visitCode_Assignment(Code_AssignmentContext ctx) {
		Boolean result =  visit(ctx.assignment());

		// Debug
		if(debug) {
			ErrorHandling.printInfo("Visited " + ctx.assignment().getText() + " : " + result);
		}

		return result;
	}

	@Override
	public Boolean visitCode_Function(Code_FunctionContext ctx) {
		return visitChildren(ctx);
	}

	// --------------------------------------------------------------------------
	// Statements 

	@Override 
	public Boolean visitStatement_Declaration(Statement_DeclarationContext ctx) {
		return visit(ctx.varDeclaration());
	}

	@Override 
	public Boolean visitStatement_Assignment(Statement_AssignmentContext ctx) {
		boolean valid =  visit(ctx.assignment());

		if(debug) {
			ErrorHandling.printInfo(ctx, "Visited " + ctx.assignment().getText() + " : " + valid);
		}

		return valid;
	}

	@Override 
	public Boolean visitStatement_Control_Flow_Statement(Statement_Control_Flow_StatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override 
	public Boolean visitStatement_FunctionCall(Statement_FunctionCallContext ctx) {
		return visit(ctx.functionCall());
	}

	@Override 
	public Boolean visitStatement_Function_Return(Statement_Function_ReturnContext ctx) {
		return visitChildren(ctx);
	}

	@Override 
	public Boolean visitStatement_Print(Statement_PrintContext ctx) {
		return visit(ctx.print());
	}

	// --------------------------------------------------------------------------
	// Assignements

	@Override 
	public Boolean visitAssignment_Var_Declaration_Not_Boolean(Assignment_Var_Declaration_Not_BooleanContext ctx) {
		if (!visit(ctx.varDeclaration())) {
			return false;
		};

		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		String varName = ctx.varDeclaration().ID().getText();

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_NotBoolean] Visited visitAssignment_Var_Declaration_NotBoolean");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + varName + " with type " + typeName);
		}

		// verify that variable to be created has valid name
		if (typesTable.containsKey(varName)) {
			ErrorHandling.printError(ctx, varName + " is a reserved word");
			return false;
		}

		// verify that assigned Variable is of Type boolean
		if (typeName.equals("boolean")) {
			if (!visit(ctx.var())) {
				return false;
			};

			Object obj = mapCtxObj.get(ctx.var());

			if (obj instanceof Boolean) {
				Boolean b = (Boolean) (symbolTable.get(ctx.var().ID().getText()));
				symbolTable.put(ctx.varDeclaration().ID().getText(), !b);
				mapCtxObj.put(ctx, !b);

				if(debug) {
					ErrorHandling.printInfo(ctx, "boolean variable with value: " + !b + " was assigned");
				}

				return true;
			}

			ErrorHandling.printError(ctx, "Cannot assign logical operation to non boolean Type");
			return false;
		}

		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" and boolean are not compatible");
		return false;
	}

	@Override 
	public Boolean visitAssignment_Var_Declaration_Value(Assignment_Var_Declaration_ValueContext ctx) {
		if (!visit(ctx.varDeclaration())) {
			return false;
		}

		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		String varName = ctx.varDeclaration().ID().getText();

		if (!visit(ctx.value())) {
			return false;
		}
		Object value = mapCtxObj.get(ctx.value());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_VALUE] Visited visitAssignment_Var_Declaration_Value");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + varName + " with type " + typeName);
			ErrorHandling.printInfo(ctx, "--- Value to assign is " + value);
		}

		// verify that variable to be created has valid name
		if (typesTable.containsKey(varName)) {
			ErrorHandling.printError(ctx, varName + " is a reserved word");
			return false;
		}

		// assign boolean to boolean
		if (value instanceof Boolean && typeName.equals("boolean")) {
			Boolean b = (Boolean) value;
			symbolTable.put(ctx.varDeclaration().ID().getText(), b);
			mapCtxObj.put(ctx, b);

			if(debug) {
				ErrorHandling.printInfo(ctx, "assigned boolean b=" + b + " to " + ctx.varDeclaration().ID().getText());
			}

			return true;
		}

		// assign string to string
		if (value instanceof String && typeName.equals("string")) {
			String str = (String) value;
			symbolTable.put(ctx.varDeclaration().ID().getText(), str);
			mapCtxObj.put(ctx, str);

			if(debug) {
				ErrorHandling.printInfo(ctx, "assigned string str=" + str + " to " + ctx.varDeclaration().ID().getText());
			}

			return true;
		}

		// assign compatible types
		if (typesTable.containsKey(typeName)) {
			destinationType = typesTable.get(typeName);
			destinationType.clearCheckList();
			Variable temp = (Variable) value;
			Variable a = new Variable(temp);
			Type type = destinationType;
			if (a.convertTypeTo(type) == true) {
				symbolTable.put(ctx.varDeclaration().ID().getText(), a);
				mapCtxObj.put(ctx, a);

				if(debug) {
					ErrorHandling.printInfo(ctx, "assigned Variable var=" + a.getType().getTypeName() + ", " +
							"val=" + a.getValue() + " to " + ctx.varDeclaration().ID().getText());
				}

				return true;
			}
		}

		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with given Type");
		return false;
	}

	@Override  
	public Boolean visitAssignment_Var_Declaration_Comparison(Assignment_Var_Declaration_ComparisonContext ctx) {
		if (!visit(ctx.varDeclaration())) {
			return false;
		};
		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		String varName = ctx.varDeclaration().ID().getText();

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_COMP] Visited visitAssignment_Var_Declaration_Comparison");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + varName + " with type " + typeName);
		}

		// assign boolean to boolean
		if (typeName.equals("boolean")) {
			if (!visit(ctx.comparison())) {
				return false;
			};

			Boolean b = (Boolean) mapCtxObj.get(ctx.comparison());
			symbolTable.put(ctx.varDeclaration().ID().getText(), b);

			mapCtxObj.put(ctx, b);
			if(debug) {
				ErrorHandling.printInfo(ctx, "assigned boolean b=" + b + " to " + ctx.varDeclaration().ID().getText());
			}
			return true;
		}

		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with boolean");
		return false;
	}

	@Override 
	public Boolean visitAssignment_Var_Declaration_Operation(Assignment_Var_Declaration_OperationContext ctx) {
		if (!visit(ctx.varDeclaration())) {
			return false;
		};

		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		String varName = ctx.varDeclaration().ID().getText();

		if(!typeName.equals("boolean") && !typeName.equals("string")) {
			destinationType = typesTable.get(typeName);
			destinationType.clearCheckList();
		}

		if (!visit(ctx.operation())) {
			return false;
		};
		Object obj = mapCtxObj.get(ctx.operation());

		// verify that variable to be created has valid name
		if (symbolTable.containsKey(varName)) {
			ErrorHandling.printError(ctx, varName + " is a reserved word");
			return false;
		}

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_OP] Visited visitAssignment_Var_Declaration_Operation");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + varName + " with type " + typeName);
			if(!typeName.equals("boolean") || !typeName.equals("string")) {
				ErrorHandling.printInfo(ctx, "--- destinationType is " + destinationType);
			}
		}

		// assign Variable to string is not possible
		if(obj instanceof String) {
			if (obj instanceof String) {
				String str = (String) obj;
				symbolTable.put(ctx.varDeclaration().ID().getText(), str);
				mapCtxObj.put(ctx, str);
				return true;
			}
			ErrorHandling.printError(ctx, "string Type is not compatible with the operation \"" +ctx.operation().getText() + "\"");
			return false;
		}

		// assign Variable to boolean is not possible
		if (typeName.equals("boolean")) {
			if (obj instanceof Boolean) {
				Boolean b = (Boolean) obj;
				symbolTable.put(ctx.varDeclaration().ID().getText(), b);
				mapCtxObj.put(ctx, b);
				return true;
			}
			ErrorHandling.printError(ctx, "Cannot assign Type \"" + typeName + "\" to boolean");
			return false;
		}

		// assign Variable to Variable
		Variable temp = (Variable) mapCtxObj.get(ctx.operation());
		Variable a = new Variable(temp);

		if (debug) {
			ErrorHandling.printInfo(ctx, "--- Variable to assign is " + a);
			ErrorHandling.printInfo(ctx, "type to assign to is: " + typesTable.get(typeName));
		}

		if (a.convertTypeTo(typesTable.get(typeName))) {
			symbolTable.put(ctx.varDeclaration().ID().getText(), a);
			mapCtxObj.put(ctx, a);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned Variable var=" + a.getType().getTypeName() + ", " +
					"val=" + a.getValue() + " to " + ctx.varDeclaration().ID().getText());}
			return true;
		}

		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with \"" + a.getType().getTypeName() + "\"!");
		return false;
	}

	@Override
	public Boolean visitAssignment_Var_Declaration_FunctionCall(Assignment_Var_Declaration_FunctionCallContext ctx) {
		return visitChildren(ctx);
	}

	@Override 
	public Boolean visitAssignment_Var_Not_Boolean(Assignment_Var_Not_BooleanContext ctx) {
		if (!visit(ctx.var(0))) {
			return false;
		};

		if(!symbolTable.containsKey(ctx.var(0).ID().getText())) {
			ErrorHandling.printError(ctx, "Variable \"" + ctx.var(1).ID().getText() + "\"is not defined");
			return false;
		}
		Object obj = symbolTable.get(ctx.var(0).ID().getText());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_NOT_BOOLEAN] Visited visitAssignment_Var_Declaration_Operation");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + ctx.var(0).ID().getText() + " with type " + symbolTable.get(ctx.var(0).ID().getText()));
		}

		// verify that assigned Variable is of Type boolean
		if (obj instanceof Boolean) {
			if (!visit(ctx.var(1))) {
				return false;
			};
			Boolean b = (Boolean) (symbolTable.get(ctx.var(1).ID().getText()));
			symbolTable.put(ctx.var(0).ID().getText(), !b);
			mapCtxObj.put(ctx, !b);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned boolean b=" + b + " to " + ctx.var(0).ID().getText());}
			return true;
		}

		// if assigned variable is not boolean
		if (obj instanceof Variable) {
			Variable temp = (Variable) obj;
			Variable a = new Variable(temp);
			ErrorHandling.printError(ctx, "Type \"" + a.getType().getTypeName() + "\" and boolean are not compatible");
		}
		if (obj instanceof String) {
			ErrorHandling.printError(ctx, "Type \"string\" and \"boolean\" are not compatible");
		}

		return false;
	}

	@Override 
	public Boolean visitAssignment_Var_Value(Assignment_Var_ValueContext ctx) {
		if (!visit(ctx.var())) {
			return false;
		}

		Object obj = symbolTable.get(ctx.var().ID().getText());

		if (!visit(ctx.value())) {
			return false;
		}

		Object value = mapCtxObj.get(ctx.value());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_VALUE] Visited visitAssignment_Var_Declaration_Operation");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + ctx.var().ID().getText() + " with type " + symbolTable.get(ctx.var().ID().getText()));
		}

		// assign boolean to boolean
		if (value instanceof Boolean && obj instanceof Boolean) {
			Boolean b = (Boolean) value;
			symbolTable.put(ctx.var().ID().getText(), b);
			mapCtxObj.put(ctx, b);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned boolean b=" + b + " to " + ctx.var().ID().getText());}
			return true;
		}

		// assign string to string
		if (value instanceof String && obj instanceof String) {
			String str = (String) value;
			symbolTable.put(ctx.var().ID().getText(), str);
			mapCtxObj.put(ctx, str);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned string str=" + str + " to " + ctx.var().ID().getText());}
			return true;
		}

		// assign compatible types
		Variable temp = (Variable) obj;
		Variable a = new Variable(temp);

		String typeName = a.getType().getTypeName();
		if (typesTable.containsKey(typeName)) {
			Type type = typesTable.get(typeName);
			if (a.convertTypeTo(type) == true) {
				symbolTable.put(ctx.var().ID().getText(), a);
				mapCtxObj.put(ctx, a);
				if(debug) {ErrorHandling.printInfo(ctx, "assigned Variable var=" + a.getType().getTypeName() + ", " +
						"val=" + a.getValue() + " to " + ctx.var().ID().getText());}
				return true;
			}
		}

		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with given Type!");
		return false;
	}

	@Override 
	public Boolean visitAssignment_Var_Comparison(Assignment_Var_ComparisonContext ctx) {
		if (!visit(ctx.var())) {
			return false;
		};
		Object obj = mapCtxObj.get(ctx.var());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_COMP] Visited visitAssignment_Var_Declaration_Operation");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + ctx.var().ID().getText() + " with type " + symbolTable.get(ctx.var().ID().getText()));
		}

		// verify that assigned var has type boolean

		if (obj instanceof Boolean) {
			if (!visit(ctx.comparison())) {
				return false;
			};
			Boolean b = (Boolean) mapCtxObj.get(ctx.comparison());
			symbolTable.put(ctx.var().ID().getText(), b);
			mapCtxObj.put(ctx, b);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned boolean b=" + b + " to " + ctx.var().ID().getText());}
			return true;
		}

		// Types are not compatible
		ErrorHandling.printError(ctx, "Type is not compatible with boolean!");
		return false;
	}

	@Override 
	public Boolean visitAssignment_Var_Operation(Assignment_Var_OperationContext ctx) {
		if (!visit(ctx.var())) {
			return false;
		};
		Object obj = mapCtxObj.get(ctx.var());
		if (!visit(ctx.operation())) {
			return false;
		};
		Object opValue = mapCtxObj.get(ctx.operation());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_OP] Visited visitAssignment_Var_Declaration_Operation");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + ctx.var().ID().getText() + " with type " + symbolTable.get(ctx.var().ID().getText()));
		}

		if(obj instanceof String) {
			if (opValue instanceof String) {
				String str = (String) opValue;
				symbolTable.put(ctx.var().ID().getText(), str);
				mapCtxObj.put(ctx, str);
				return true;
			}
			ErrorHandling.printError(ctx, "string Type is not compatible with the operation \"" +ctx.operation().getText() + "\"");
			return false;
		}

		if(obj instanceof Boolean) {
			if (opValue instanceof Boolean) {
				Boolean b = (Boolean) opValue;
				symbolTable.put(ctx.var().ID().getText(), b);
				mapCtxObj.put(ctx, b);
				return true;
			}
			ErrorHandling.printError(ctx, "boolean Type is not compatible with the operation \"" +ctx.operation().getText() + "\"");
			return false;
		}

		Variable var = (Variable) obj;
		String typeName = var.getType().getTypeName();
		destinationType = typesTable.get(typeName); // static field to aid in operation predictive convertions
		destinationType.clearCheckList();


		Variable a = (Variable) opValue;

		if (debug) {ErrorHandling.printInfo(ctx, "--- Variable to assign is " + a);}

		if (a.convertTypeTo(destinationType)) {
			symbolTable.put(ctx.var().ID().getText(), a);
			mapCtxObj.put(ctx, a);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned Variable var=" + a.getType().getTypeName() + ", " +
					"val=" + a.getValue() + " to " + ctx.var().ID().getText());}
			destinationType.clearCheckList();
			return true;
		}

		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with \"" + a.getType().getTypeName() + "\"");
		destinationType.clearCheckList();
		return false;
	}

	@Override
	public Boolean visitAssingment_Var_FunctionCall(Assingment_Var_FunctionCallContext ctx) {
		if (!visit(ctx.var())) {
			return false;
		}
		if (!visit(ctx.functionCall())) {
			return false;
		}
		return true;
	}

	// --------------------------------------------------------------------------
	// Functions

	@Override
	public Boolean visitFunction_Main(Function_MainContext ctx) {
		Boolean valid = true;
		for (StatementContext stat : ctx.statement()) {
			Boolean res = visit(stat);
			valid = valid && res;
		}
		return valid;
	}


	@Override
	public Boolean visitFunction_ID(Function_IDContext ctx) {
		return super.visitFunction_ID(ctx);
	}


	@Override
	public Boolean visitFunctionReturn(FunctionReturnContext ctx) {
		return super.visitFunctionReturn(ctx);
	}


	@Override
	public Boolean visitFunctionCall(FunctionCallContext ctx) {
		return super.visitFunctionCall(ctx);
	}


	// --------------------------------------------------------------------------
	// Control Flow Statements

	@Override 
	public Boolean visitControlFlowStatement(ControlFlowStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override 
	public Boolean visitForLoop(ForLoopContext ctx) {
		Boolean valid = true;
		Boolean res   = true;
		for (AssignmentContext a : ctx.assignment()) {
			res = visit(a);
			valid = valid && res;
		}
		res = visit(ctx.logicalOperation());
		valid = valid && res;
		for (StatementContext b : ctx.statement()) {
			res = visit(b);
			valid = valid && res;
		}
		return valid;
	}

	@Override 
	public Boolean visitWhileLoop(WhileLoopContext ctx) {
		Boolean valid = true;
		Boolean res = visit(ctx.logicalOperation());
		valid = valid && res;
		for (StatementContext b : ctx.statement()) {
			res = visit(b);
			valid = valid && res;
		}
		return valid;
	}

	@Override 
	public Boolean visitCondition(ConditionContext ctx) {
		return visitChildren(ctx);
	}

	@Override 
	public Boolean visitIfCondition(IfConditionContext ctx) {
		Boolean valid = true;
		Boolean res = visit(ctx.logicalOperation());
		valid = valid && res;
		for (StatementContext b : ctx.statement()) {
			res = visit(b);
			valid = valid && res;
		}
		return valid;
	}

	@Override 
	public Boolean visitElseIfCondition(ElseIfConditionContext ctx) {
		Boolean valid = true;
		Boolean res = visit(ctx.logicalOperation());
		valid = valid && res;
		for (StatementContext b : ctx.statement()) {
			res = visit(b);
			valid = valid && res;
		}
		return valid;
	}

	@Override 
	public Boolean visitElseCondition(ElseConditionContext ctx) {
		Boolean valid = true;
		for (StatementContext b : ctx.statement()) {
			Boolean res = visit(b);
			valid = valid && res;
		}
		return valid;
	}

	// --------------------------------------------------------------------------
	// Logical Operations

	@Override 
	public Boolean visitLogicalOperation_Parenthesis(LogicalOperation_ParenthesisContext ctx) {
		Boolean valid = visit(ctx.logicalOperation());
		if (debug) {ErrorHandling.printInfo(ctx, "[OP_LOGIC_OP_PAR]");}
		if(valid) {
			return true;
		}
		ErrorHandling.printError(ctx, "Logical operands must have value boolean");
		if(debug) {ErrorHandling.printInfo(ctx, "visited logicalOperation_Parenthesis");}
		return false;
	}

	@Override 
	public Boolean visitLogicalOperation_Operation(LogicalOperation_OperationContext ctx) {
		Boolean validOp0 = visit(ctx.logicalOperation(0));
		Boolean validOp1 = visit(ctx.logicalOperation(1));
		if (debug) {ErrorHandling.printInfo(ctx, "[OP_LOGIC_OP_OP]");}
		if (validOp0 && validOp1) {
			return true;
		}
		ErrorHandling.printError(ctx, "Logical operands must have value boolean");
		return false;

	}

	@Override 
	public Boolean visitLogicalOperation_logicalOperand(LogicalOperation_logicalOperandContext ctx) {
		Boolean valid = visit(ctx.logicalOperand());
		if (debug) {ErrorHandling.printInfo(ctx, "[OP_LOGIC_OP_OPERAND]");}
		mapCtxObj.put(ctx, mapCtxObj.get(ctx.logicalOperand()));
		return valid;
	}

	@Override 
	public Boolean visitLogicalOperand_Comparison(LogicalOperand_ComparisonContext ctx) {
		return visit(ctx.comparison());
	}

	@Override 
	public Boolean visitLogicalOperand_Not_Comparison(LogicalOperand_Not_ComparisonContext ctx) {
		return visit(ctx.comparison());
	}

	@Override
	public Boolean visitLogicalOperand_Var(LogicalOperand_VarContext ctx) {
		String varName = ctx.var().ID().getText();
		if (debug) {ErrorHandling.printInfo(ctx, "[OP_LOGIC_OPERAND_VAR]");}
		// verify that variable exists
		if (symbolTable.containsKey(varName)) {
			Object obj = symbolTable.get(varName);
			if (obj instanceof Boolean) {
				mapCtxObj.put(ctx, obj);
				return true;
			}
			ErrorHandling.printError(ctx, "Variable \"" + varName + "\" is not boolean!");
			return false;
		}
		ErrorHandling.printError(ctx, "Variable \"" + varName + "\" is not declared!");
		return false;
	}

	@Override
	public Boolean visitLogicalOperand_Not_Var(LogicalOperand_Not_VarContext ctx) {
		String varName = ctx.var().ID().getText();
		if (debug) {ErrorHandling.printInfo(ctx, "[OP_LOGIC_OPERAND_NOT_VAR]");}
		// verify that variable exists
		if (symbolTable.containsKey(varName)) {
			Object obj = symbolTable.get(varName);
			if (obj instanceof Boolean) {
				mapCtxObj.put(ctx, obj);
				return true;
			}
			ErrorHandling.printError(ctx, "Variable \"" + varName + "\" is not boolean!");
			return false;
		}
		ErrorHandling.printError(ctx, "Variable \"" + varName + "\" is not declared!");
		return false;
	}

	@Override
	public Boolean visitLogicalOperand_Value(LogicalOperand_ValueContext ctx) {
		Boolean valid = visit(ctx.value());
		Object obj = mapCtxObj.get(ctx.value());
		if (debug) {ErrorHandling.printInfo(ctx, "[OP_LOGIC_OPERAND_VALUE]");}
		if (valid) {
			if (obj instanceof Boolean) {
				mapCtxObj.put(ctx, obj);
				return true;
			}
		}

		return false;
	}

	@Override
	public Boolean visitLogicalOperand_Not_Value(LogicalOperand_Not_ValueContext ctx) {
		Boolean valid = visit(ctx.value());
		Object obj = mapCtxObj.get(ctx.value());
		if (debug) {ErrorHandling.printInfo(ctx, "[OP_LOGIC_OPERAND_NOT_VALUE]");}
		if (valid) {
			if (obj instanceof Boolean) {
				mapCtxObj.put(ctx, obj);
				return true;
			}
		}

		return false;
	}

	@Override
	public Boolean visitComparison(ComparisonContext ctx) {
		Boolean validOp0 = visit(ctx.compareOperation(0));
		Boolean validOp1 = visit(ctx.compareOperation(1));

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_COMPARISON]");
		}

		if (validOp0 && validOp1) {
			Object obj0 = mapCtxObj.get(ctx.compareOperation(0));
			Object obj1 = mapCtxObj.get(ctx.compareOperation(1));

			if(obj0 instanceof Boolean && obj1 instanceof Boolean) {
				mapCtxObj.put(ctx, true);
				return true;
			}

			if(obj0 instanceof Variable && obj1 instanceof Variable) {
				Variable a = (Variable) obj0;
				Variable b = (Variable) obj1;
				if (debug) {
					ErrorHandling.printInfo(ctx, "THIS IS A : " + a);
					ErrorHandling.printInfo(ctx, "THIS IS B : " + b);
				}
				boolean comparisonIsPossible = a.typeIsCompatible(b.getType());
				mapCtxObj.put(ctx, comparisonIsPossible);
				return comparisonIsPossible;
			}
		}
		ErrorHandling.printError(ctx, "Types to be compared are not compatible");
		return false;
	}

	@Override
	public Boolean visitCompareOperation_Operation(CompareOperation_OperationContext ctx) {
		Boolean valid = visitChildren(ctx);
		mapCtxObj.put(ctx, mapCtxObj.get(ctx.operation()));
		return valid;
	}

	@Override
	public Boolean visitCompareOperation_BOOLEAN(CompareOperation_BOOLEANContext ctx) {
		Boolean b = Boolean.parseBoolean(ctx.BOOLEAN().getText());
		mapCtxObj.put(ctx, b);
		return true;
	}

	@Override
	public Boolean visitCompareOperator(CompareOperatorContext ctx) {
		return true;
	}

	// --------------------------------------------------------------------------
	// Operations

	@Override
	public Boolean visitOperation_Cast(Operation_CastContext ctx) {
		if(!visitChildren(ctx)) {
			return false;
		}
		Variable temp = (Variable) mapCtxObj.get(ctx.operation());
		Variable a = new Variable(temp);

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_CAST] Visited Operation Cast");
			ErrorHandling.printInfo(ctx, "[OP_CAST] Casting Variable " + a);
			ErrorHandling.printInfo(ctx, "[OP_CAST] Casting to " + destinationType.getTypeName() + ". Cast can happen? " + (a.getType().getCode() == 1.0) + "\n");
		}

		// cast is to a compatible type. Cast is not needed, direct atribution is possible
		if (a.typeIsCompatible(typesTable.get(ctx.cast().ID().getText()))) {
			ErrorHandling.printWarning(ctx, "Variable was not casted. Cast from \"" + a.getType().getTypeName() + "\" to \"" + ctx.cast().ID().getText() + 
					"\" is not necessary. Direct assignment is possible.");
			return true;
		}

		// cast is only possible if Variable is of Type Number (with code 1)
		if (a.getType().getCode() != 1) {
			ErrorHandling.printError(ctx, "Type \"" + a.getType() + "\" cannot be casted. Only number type can be casted!");
			return false;
		}

		// type is number cast is possible
		Variable res = new Variable(typesTable.get(ctx.cast().ID().getText()), a.getValue());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_CAST] variable res " + res);
		}

		mapCtxObj.put(ctx, res);
		return true;
	}

	@Override 
	public Boolean visitOperation_Parenthesis(Operation_ParenthesisContext ctx) {
		if(!visit(ctx.operation())) {
			return false;
		}
		mapCtxObj.put(ctx, mapCtxObj.get(ctx.operation()));
		return true;
	}

	@Override 
	public Boolean visitOperation_Mult_Div_Mod(Operation_Mult_Div_ModContext ctx) {
		if(!visit(ctx.operation(0)) || !visit(ctx.operation(1))) {
			return false;
		}
		Variable temp = (Variable) mapCtxObj.get(ctx.operation(0));
		Variable a = new Variable(temp);
		temp = (Variable) mapCtxObj.get(ctx.operation(1));

		Variable b = new Variable(temp);
		String op = ctx.op.getText();

		if (debug) {
			//ErrorHandling.printInfo(ctx, "-------------------- " + temp);
			//ErrorHandling.printInfo(ctx, "---------------op0 - " + ctx.operation(0).getText());
			//ErrorHandling.printInfo(ctx, "---------------op1 - " + ctx.operation(1).getText());

			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] Visiting Operation Mult_Div_Mod");
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] variable a " + a);
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] variable b " + b);
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] op		 " + op + "\n");
		}

		// MODULOS OPERATION
		if (op.equals("%")) {
			// verify that right side of mod operation is of Type Number
			if (b.getType().getCode() == 1) {
				Double moddedValue = a.getValue() % b.getValue();
				a = new Variable (typesTable.get(a.getType().getTypeName()), moddedValue);
				mapCtxObj.put(ctx, a);
				return true;
			}
			ErrorHandling.printError(ctx, "Right side of mod operation has to be of Type Number!");
			return false;

		}

		// MULTIPLICATION OR DIVISION OPERATION
		// conversion always returns a valid Variable Object, but it may or may not be compatible with assignment
		// assignment will ultimately check teh result, but possible problems will be flagged with an Exception
		try {
			if(!a.getType().equals(typesTable.get("number"))) {
				a.MultDivCheckConvertType(destinationType);
			}
			if(debug) {ErrorHandling.printInfo(ctx, "Variable a was converted to " + a);}
		}
		catch (Exception e) {
			ErrorHandling.printWarning(ctx, "Variable has multiple derivation. Operation may not be resolved!");
		}

		try {
			if(!b.getType().equals(typesTable.get("number"))) {
				b.MultDivCheckConvertType(destinationType);
			}
			if(debug) {ErrorHandling.printInfo(ctx, "Variable b was converted to " + b);}
		}
		catch (Exception e) {
			ErrorHandling.printWarning(ctx, "Variable has multiple derivation. Operation may not be resolved!");
		}

		// variables are converted, do the operation
		if (op.equals("*")) {
			Variable res = Variable.multiply(a, b); 
			Double resCode = res.getType().getCode(); 
			Collection<Type> types = typesTable.values(); 
			for (Type t : types) { 
				if (t.getCode() == resCode) {
					res = new Variable(typesTable.get(t.getTypeName()), res.getValue());
					break; 
				} 

			} 
			if (debug) { ErrorHandling.printInfo(ctx, "result of multiplication is Variable " + res);}
			mapCtxObj.put(ctx, res); 
		}

		if (op.equals("/")) {
			//mapCtxObj.put(ctx, Variable.divide(a, b));
			Variable res = Variable.divide(a, b); 
			Double resCode = res.getType().getCode(); 
			Collection<Type> types = typesTable.values(); 
			for (Type t : types) { 
				if (t.getCode() == resCode) { 
					res = new Variable(typesTable.get(t.getTypeName()), res.getValue());
					break; 
				} 

			} 
			if (debug) { ErrorHandling.printInfo(ctx, "result of division is Variable " + res);}
			mapCtxObj.put(ctx, res);
		}	
		return true;
	}

	@Override 
	public Boolean visitOperation_Simetric(Operation_SimetricContext ctx) {
		if(!visit(ctx.operation())) {
			return false;
		}
		Variable temp = (Variable) mapCtxObj.get(ctx.operation());
		Variable a = new Variable(temp);
		mapCtxObj.put(ctx, Variable.simetric(a));
		if(debug) {ErrorHandling.printInfo(ctx, "[OP_OP_SIMETRIC]");}
		return true;
	}

	@Override 
	public Boolean visitOperation_Add_Sub(Operation_Add_SubContext ctx) {
		if(!visit(ctx.operation(0))) {
			return false;
		}
		destinationType.clearCheckList();
		Variable temp = (Variable) mapCtxObj.get(ctx.operation(0));
		Variable a = new Variable(temp);
		destinationType.clearCheckList();
		if(!visit(ctx.operation(1))) {
			return false;
		}
		temp = (Variable) mapCtxObj.get(ctx.operation(1));
		Variable b = new Variable(temp);
		destinationType.clearCheckList();

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ADDSUB] Visiting Operation Add_Sub");
			ErrorHandling.printInfo(ctx, "[OP_ADDSUB] variable a " + a);
			ErrorHandling.printInfo(ctx, "[OP_ADDSUB] variable b " + b + "\n");
		}

		// verify that types are equals before adding or subtracting 
		if (!a.getType().equals(b.getType())) {
			// if types are not equal, try to convert Variable 'b' type into 'a' type
			if (!b.convertTypeTo(a.getType())) {
				ErrorHandling.printError(ctx, "Type \"" + a.getType() + "\" is not compatible with \"" + b.getType() + "\"!");
				return false;
			}
			if (debug) { ErrorHandling.printInfo(ctx, "Variable b was converted to : " + b);}
		}

		// types are equal adding and subtracting is possible
		if (ctx.op.getText().equals("+")) {
			Variable res = Variable.add(a, b);
			if (debug) { ErrorHandling.printInfo(ctx, "result of sum is Variable " + res);}
			mapCtxObj.put(ctx, res);
		}
		else if (ctx.op.getText().equals("-")) {
			Variable res = Variable.subtract(a, b);
			if (debug) { ErrorHandling.printInfo(ctx, "result of sum is Variable " + res);}
			mapCtxObj.put(ctx, res);
		}
		return true;
	}

	@Override 
	public Boolean visitOperation_Power(Operation_PowerContext ctx) {
		if(!visit(ctx.operation(0)) || !visit(ctx.operation(1))) {
			return false;
		}
		Object obj0= mapCtxObj.get(ctx.operation(0));
		Object obj1 = mapCtxObj.get(ctx.operation(1));

		if (obj0 instanceof String || obj1 instanceof String) {
			ErrorHandling.printError(ctx, "Type string is not compatible with power operation");
			return false;
		}

		if (obj0 instanceof Boolean || obj1 instanceof Boolean) {
			ErrorHandling.printError(ctx, "Type boolean is not compatible with power operation");
			return false;
		}

		Variable temp = (Variable) obj1;

		if (!temp.getType().getTypeName().equals("number")) {
			ErrorHandling.printError(ctx, "power operation requires type number in exponent");
			return false;
		}

		Variable pow = new Variable(temp);
		temp = (Variable) mapCtxObj.get(ctx.operation(0));
		Variable base = new Variable(temp);

		Variable res = Variable.power(base, pow);
		Double resCode = res.getType().getCode(); 
		Collection<Type> types = typesTable.values(); 
		for (Type t : types) { 
			if (t.getCode() == resCode) {
				res = new Variable(typesTable.get(t.getTypeName()), res.getValue());
				break; 
			} 

		} 
		mapCtxObj.put(ctx, res);

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_POWER] Visited Operation Power");
			ErrorHandling.printInfo(ctx, "--- Powering Variable " + base + "with power " + pow + "and result is " + res);
		}

		return true;
	}

	@Override 
	public Boolean visitOperation_Var(Operation_VarContext ctx) {
		if(!visit(ctx.var())) {
			return false;
		}
		Object obj = symbolTable.get(ctx.var().ID().getText());
		if (obj == null) {
			ErrorHandling.printError(ctx, "Cannot operate with boolean!");
		}

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_VAR] Visited Operation Variable");
			ErrorHandling.printInfo(ctx, "[OP_VAR] varName is " + ctx.var().ID().getText());
			ErrorHandling.printInfo(ctx, "[OP_VAR] obj " + obj);
		}

		// verify that var is not of type string
		if (obj instanceof String) {
			ErrorHandling.printError(ctx, "Cannot operate with string!");
			return false;
		}

		// var is of type Variable
		mapCtxObj.put(ctx, mapCtxObj.get(ctx.var()));
		return true;
	}

	@Override
	public Boolean visitOperation_FunctionCall(Operation_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override 
	public Boolean visitOperation_NUMBER(Operation_NUMBERContext ctx) {
		Double value = Double.parseDouble(ctx.NUMBER().getText());
		Variable a = new Variable(typesTable.get("number"), value);
		mapCtxObj.put(ctx, a);

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_NUMBER] Visited Operation Number");
			ErrorHandling.printInfo(ctx, "--- Value is " + value);
		}

		return true;
	}

	// --------------------------------------------------------------------------
	// Prints

	@Override
	public Boolean visitPrint_Print(Print_PrintContext ctx) {
		boolean valid = true;
		boolean res = true;
		for (PrintVarContext pvc : ctx.printVar()) {
			res = visit(pvc);
			valid = valid && res;
		}
		return valid;
	}

	@Override
	public Boolean visitPrint_Println(Print_PrintlnContext ctx) {
		boolean valid = true;
		boolean res = true;
		for (PrintVarContext pvc : ctx.printVar()) {
			res = visit(pvc);
			valid = valid && res;
		}
		return valid;
	}

	@Override
	public Boolean visitPrintVar_Value(PrintVar_ValueContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitPrintVar_Var(PrintVar_VarContext ctx) {
		return visitChildren(ctx);
	}

	// --------------------------------------------------------------------------
	// Variables

	@Override 
	public Boolean visitVar(VarContext ctx) {
		String key = ctx.ID().getText();
		if (!symbolTable.containsKey(key)) {
			ErrorHandling.printError(ctx, "Variable \"" + key + "\" is not defined!");
			return false;
		};
		mapCtxObj.put(ctx, symbolTable.get(key));
		return true;
	}

	@Override  
	public Boolean visitVarDeclaration(VarDeclarationContext ctx) {
		return visit(ctx.type());
	}

	// --------------------------------------------------------------------------
	// Types

	@Override 
	public Boolean visitType_Number_Type(Type_Number_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.NUMBER_TYPE().getText());
		return true;
	}

	@Override 
	public Boolean visitType_Boolean_Type(Type_Boolean_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.BOOLEAN_TYPE().getText());
		return true;
	}

	@Override 
	public Boolean visitType_String_Type(Type_String_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.STRING_TYPE().getText());
		return true;
	}

	@Override 
	public Boolean visitType_Void_Type(Type_Void_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.VOID_TYPE().getText());
		return true;
	}

	@Override 
	public Boolean visitType_ID_Type(Type_ID_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.ID().getText());
		return true;
	}

	// --------------------------------------------------------------------------
	// Values

	@Override 
	public Boolean visitValue_Cast_Number(Value_Cast_NumberContext ctx) {
		if (!visit(ctx.cast())) {
			return false;
		};
		String castType = ctx.cast().ID().getText();

		// verify that cast type exists
		if (!typesTable.containsKey(castType)) {
			ErrorHandling.printError(ctx, "Cast Type \"" + castType + "\" + is not defined!");
			return false;
		}

		// create new Variable and store it in mapCtxObj
		Type type = typesTable.get(castType);
		Double value = Double.parseDouble(ctx.NUMBER().getText());
		Variable a = new Variable(new Type(typesTable.get(type.getTypeName())), value);
		mapCtxObj.put(ctx, a);

		if (debug) {
			ErrorHandling.printInfo(ctx, "[CAST] Visited Cast");
			ErrorHandling.printInfo(ctx, "[CAST] Casting the value " + value + " to " + type.getTypeName());
		}
		return true;
	}

	@Override 
	public Boolean visitValue_Number(Value_NumberContext ctx) {
		Double number = Double.parseDouble(ctx.NUMBER().getText());
		Variable a = new Variable(typesTable.get("number"), number);
		mapCtxObj.put(ctx, a);
		return true;
	}

	@Override 
	public Boolean visitValue_Boolean(Value_BooleanContext ctx) {
		Boolean b = Boolean.parseBoolean(ctx.BOOLEAN().getText());
		mapCtxObj.put(ctx, b);
		return true;
	}

	@Override 
	public Boolean visitValue_String(Value_StringContext ctx) {
		String str = getStringText(ctx.STRING().getText());
		mapCtxObj.put(ctx, str);
		return true;
	}

	@Override 
	public Boolean visitCast(CastContext ctx) {
		mapCtxObj.put(ctx, ctx.ID().getText());
		return true;
	}

	// --------------------------------------------------------------------------
	// Auxiliar Functions 

	private static String getStringText(String str) {
		str = str.substring(1, str.length() -1);
		// FIXME escapes still need to be removed from antlr STRING token to have correct text.
		return str;
	}

}