package compiler;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import potatoesGrammar.grammar.PotatoesBaseVisitor;
import potatoesGrammar.grammar.PotatoesFunctionNames;
import potatoesGrammar.grammar.PotatoesParser.*;
import potatoesGrammar.utils.Variable;
import typesGrammar.grammar.TypesFileInfo;
import typesGrammar.utils.Code;
import typesGrammar.utils.Type;
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
	private static final boolean debug = true;

	// --------------------------------------------------------------------------
	// Static Fields
	private static String TypesFilePath;
	
	private	static TypesFileInfo		typesFileInfo; // initialized in visitUsing();
	private	static List<String>		reservedWords;
	private static Map<String, Type> 	typesTable;
	private static PotatoesFunctionNames functions;
	private static Map<String, ParserRuleContext> functionNames;

	protected static ParseTreeProperty<Object> mapCtxObj = new ParseTreeProperty<>();
	protected static Map<String, Object> symbolTable = new HashMap<>();
	
	
	public PotatoesSemanticCheck(String PotatoesFilePath){
		functions = new PotatoesFunctionNames(PotatoesFilePath);
		functionNames = functions.getFunctions();
		if (debug) ErrorHandling.printInfo("The PotatoesFilePath is: " + PotatoesFilePath);
	}
	
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
		TypesFilePath = getStringText(ctx.STRING().getText());
		if (debug) ErrorHandling.printInfo(ctx, TypesFilePath);
		typesFileInfo = new TypesFileInfo(TypesFilePath);
		reservedWords = typesFileInfo.getReservedWords();
		typesTable = typesFileInfo.getTypesTable();

		// Debug
		if (debug) {
			ErrorHandling.printInfo(ctx, "Types File path is: " + TypesFilePath);
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
		if(debug) {ErrorHandling.printInfo("Visited " + ctx.assignment().getText() + " : " + result);}
		return result;
	}

	@Override
	public Boolean visitCode_Function(Code_FunctionContext ctx) {
		return visitChildren(ctx);
	}
	
	// FIXME still need to create function that saves scope and then restores it (first check IF case)
	@Override
	public Boolean visitScope(ScopeContext ctx) {
		Boolean valid = true;
		List<StatementContext> statements = ctx.statement();

		// Visit all code rules
		for (StatementContext stat : statements) {
			Boolean res = visit(stat);
			valid = valid && res;
		}
		return valid;
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
		if(debug) {ErrorHandling.printInfo(ctx, "Visited " + ctx.assignment().getText() + " : " + valid);}
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
	// Assignments

	@Override 
	public Boolean visitAssignment_Var_Declaration_Not_Boolean(Assignment_Var_Declaration_Not_BooleanContext ctx) {
		if (!visit(ctx.varDeclaration()) || !visit(ctx.var())) {
			return false;
		};

		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		String varName = ctx.varDeclaration().ID().getText();

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_NotBoolean] Visited visitAssignment_Var_Declaration_NotBoolean");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + varName + " with type " + typeName);
		}

		// verify that variable to be created has valid name
		if (symbolTable.containsKey(varName)) {
			ErrorHandling.printError(ctx, varName + " is already declared");
			return false;
		}

		// verify that Variable to be assigned is of Type boolean
		if (!typeName.equals("boolean")) {
			ErrorHandling.printError(ctx, "Type \"" + typeName + "\" and boolean are not compatible");
			return false;
		}

		Object obj = mapCtxObj.get(ctx.var());

		if (obj instanceof Boolean) {
			symbolTable.put(ctx.varDeclaration().ID().getText(), true);
			mapCtxObj.put(ctx, true);

			if(debug) {ErrorHandling.printInfo(ctx, "boolean variable was assigned");}

			return true;
		}

		ErrorHandling.printError(ctx, "Cannot assign logical operation to non boolean Type");
		return false;
	}

	@Override 
	public Boolean visitAssignment_Var_Declaration_Value(Assignment_Var_Declaration_ValueContext ctx) {
		if (!visit(ctx.varDeclaration()) || !visit(ctx.value())) {
			return false;
		}
		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		String varName = ctx.varDeclaration().ID().getText();
		Object value = mapCtxObj.get(ctx.value());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_VALUE] Visited visitAssignment_Var_Declaration_Value");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + varName + " with type " + typeName);
			ErrorHandling.printInfo(ctx, "--- Value to assign is " + value);
		}

		// verify that variable to be created has valid name
		if(!isValidNewVariableName(varName, ctx)) return false;

		// assign boolean to boolean
		if (value instanceof Boolean && typeName.equals("boolean")) {
			symbolTable.put(ctx.varDeclaration().ID().getText(), true);
			mapCtxObj.put(ctx, true);

			if(debug) {
				ErrorHandling.printInfo(ctx, "assigned boolean to " + ctx.varDeclaration().ID().getText());
			}

			return true;
		}

		// assign string to string
		if (value instanceof String && typeName.equals("string")) {
			symbolTable.put(ctx.varDeclaration().ID().getText(), "str");
			mapCtxObj.put(ctx, "str");

			if(debug) {ErrorHandling.printInfo(ctx, "assigned string to " + ctx.varDeclaration().ID().getText());}

			return true;
		}

		// assign compatible types
		if (typesTable.containsKey(typeName)) {
			Variable aux = (Variable) value;
			Variable a = new Variable(aux); // deep copy
			Type typeToConvertTo = ((Variable) mapCtxObj.get(ctx.varDeclaration())).getType();
			if (a.convertTypeTo(typeToConvertTo) == true) {
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
		if (!visit(ctx.varDeclaration()) || !visit(ctx.comparison())) {
			return false;
		};
		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		String varName = ctx.varDeclaration().ID().getText();

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_COMP] Visited visitAssignment_Var_Declaration_Comparison");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + varName + " with type " + typeName);
		}
		
		// verify that variable to be created has valid name
		if(!isValidNewVariableName(varName, ctx)) return false;

		// assign boolean to boolean
		if (typeName.equals("boolean")) {

			symbolTable.put(ctx.varDeclaration().ID().getText(), true);
			mapCtxObj.put(ctx, true);

			if(debug) {ErrorHandling.printInfo(ctx, "assigned boolean to " + ctx.varDeclaration().ID().getText());}
			return true;
		}

		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with boolean");
		return false;
	}

	@Override 
	public Boolean visitAssignment_Var_Declaration_Operation(Assignment_Var_Declaration_OperationContext ctx) {
		if (!visit(ctx.varDeclaration()) || !visit(ctx.operation())) {
			return false;
		};

		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		String varName = ctx.varDeclaration().ID().getText();
		Object obj = mapCtxObj.get(ctx.operation());

		// verify that variable to be created has valid name
		if(!isValidNewVariableName(varName, ctx)) return false;

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_OP] Visited visitAssignment_Var_Declaration_Operation");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + varName + " with type " + typeName);
			if(!typeName.equals("boolean") || !typeName.equals("string")) {
				ErrorHandling.printInfo(ctx, "--- destinationType is " + typeName);
			}
		}

		// assign Variable to string is not possible
		if(typeName.equals("string")) {
			if (obj instanceof String) {
				symbolTable.put(ctx.varDeclaration().ID().getText(), "str");
				mapCtxObj.put(ctx, "str");
				return true;
			}
			ErrorHandling.printError(ctx, "righthand side of assignment is not compatible with string Type");
			return false;
		}

		// assign Variable to boolean is not possible
		if (typeName.equals("boolean")) {
			if (obj instanceof Boolean) {
				symbolTable.put(ctx.varDeclaration().ID().getText(), true);
				mapCtxObj.put(ctx, true);
				return true;
			}
			ErrorHandling.printError(ctx, "righthand side of assignment is not compatible with boolean Type");
			return false;
		}

		// assign Variable to Variable
		Variable temp = (Variable) mapCtxObj.get(ctx.operation());
		Variable a = new Variable(temp); // deep copy

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
		
		Object obj = symbolTable.get(ctx.var(0).ID().getText());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_NOT_BOOLEAN] Visited visitAssignment_Var_Declaration_Operation");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + ctx.var(0).ID().getText() + " with type " + symbolTable.get(ctx.var(0).ID().getText()));
		}
		
		// verify that assigned Variable is of Type boolean
		if (obj instanceof Boolean) {
			if (!visit(ctx.var(1))) {return false;}
			Object obj2 = mapCtxObj.get(ctx.var(1));
			if (obj2 instanceof Boolean) {
				symbolTable.put(ctx.var(0).ID().getText(), true);
				mapCtxObj.put(ctx, true);
				if(debug) {ErrorHandling.printInfo(ctx, "assigned boolean to " + ctx.var(0).ID().getText());}
				return true;
			}
			ErrorHandling.printError(ctx,"Cannot apply Not Logical Operand to a non boolean Type");
			return false;
		}
		
		// variable to be assigned is not boolean (is Variable)
		if (obj instanceof Variable) {
			// creates Variable only to extract name for Error print
			Variable temp = (Variable) obj;
			Variable a = new Variable(temp);
			ErrorHandling.printError(ctx, "Type \"" + a.getType().getTypeName() + "\" and boolean are not compatible");
		}
		
		// variable to be assigned is not bollean (is string)
		if (obj instanceof String) {
			ErrorHandling.printError(ctx, "Type \"string\" and \"boolean\" are not compatible");
		}
		
		return false;
	}

	@Override 
	public Boolean visitAssignment_Var_Value(Assignment_Var_ValueContext ctx) {
		if (!visit(ctx.var()) || !visit(ctx.value())) {
			return false;
		}

		Object obj = symbolTable.get(ctx.var().ID().getText());
		Object value = mapCtxObj.get(ctx.value());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_VALUE] Visited visitAssignment_Var_Declaration_Operation");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + ctx.var().ID().getText() + " with type " + symbolTable.get(ctx.var().ID().getText()));
		}

		// assign boolean to boolean
		if (value instanceof Boolean && obj instanceof Boolean) {
			symbolTable.put(ctx.var().ID().getText(), true);
			mapCtxObj.put(ctx, true);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned boolean to " + ctx.var().ID().getText());}
			return true;
		}

		// assign string to string
		if (value instanceof String && obj instanceof String) {
			symbolTable.put(ctx.var().ID().getText(), "str");
			mapCtxObj.put(ctx, "str");
			if(debug) {ErrorHandling.printInfo(ctx, "assigned string to " + ctx.var().ID().getText());}
			return true;
		}

		// assign compatible types
		Variable temp = (Variable) obj;
		Variable a = new Variable(temp); // deep copy

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
			}
			symbolTable.put(ctx.var().ID().getText(), true);
			mapCtxObj.put(ctx, true);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned boolean to " + ctx.var().ID().getText());}
			return true;
		}

		// Types are not compatible
		ErrorHandling.printError(ctx, "Type is not compatible with boolean!");
		return false;
	}

	@Override 
	public Boolean visitAssignment_Var_Operation(Assignment_Var_OperationContext ctx) {
		if (!visit(ctx.var()) || !visit(ctx.operation())) {
			return false;
		};
		Object obj = mapCtxObj.get(ctx.var());
		Object opRes = mapCtxObj.get(ctx.operation());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_OP] Visited visitAssignment_Var_Declaration_Operation");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + ctx.var().ID().getText() + " with type " + symbolTable.get(ctx.var().ID().getText()));
		}

		if(obj instanceof String) {
			if (opRes instanceof String) {
				symbolTable.put(ctx.var().ID().getText(), "str");
				mapCtxObj.put(ctx, "str");
				return true;
			}
			// TODO mudar estes prints de erro
			ErrorHandling.printError(ctx, "string Type is not compatible with \"" +ctx.operation().getText() + "\"");
			return false;
		}

		if(obj instanceof Boolean) {
			if (opRes instanceof Boolean) {
				symbolTable.put(ctx.var().ID().getText(), true);
				mapCtxObj.put(ctx, true);
				return true;
			}
			ErrorHandling.printError(ctx, "boolean Type is not compatible with the operation \"" +ctx.operation().getText() + "\"");
			return false;
		}

		Variable aux = (Variable) obj;
		Variable var = new Variable(aux);
		String typeName = var.getType().getTypeName();

		aux = (Variable) opRes;
		Variable a = new Variable(aux);

		if (debug) {ErrorHandling.printInfo(ctx, "--- Variable to assign is " + a);}

		// If type of variable a can be converted to the destination type (ie are compatible)
		if (a.convertTypeTo(typesTable.get(typeName))) {
			symbolTable.put(ctx.var().ID().getText(), a);
			mapCtxObj.put(ctx, a);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned Variable var=" + a.getType().getTypeName() + ", " +
					"val=" + a.getValue() + " to " + ctx.var().ID().getText());}
			return true;
		}

		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with \"" + a.getType().getTypeName() + "\"");
		return false;
	}

	@Override
	public Boolean visitAssingment_Var_FunctionCall(Assingment_Var_FunctionCallContext ctx) {
		if (!visit(ctx.var()) || !visit(ctx.functionCall())) {
			return false;
		}
		return true;
	}

	// --------------------------------------------------------------------------
	// Functions

	@Override
	public Boolean visitFunction_Main(Function_MainContext ctx) {
		return visit(ctx.scope());
	}

	@Override
	public Boolean visitFunction_ID(Function_IDContext ctx) {
		return super.visitFunction_ID(ctx);
	}

	@Override
	public Boolean visitFunctionReturn(FunctionReturnContext ctx) {
		return super.visitFunctionReturn(ctx);
	}
	
	// FIXME must be greatly improved... this is only the idea to get the function. still needs to run it and get return value
	@Override
	public Boolean visitFunctionCall(FunctionCallContext ctx) {
		ParserRuleContext function = functionNames.get(ctx.ID().getText());
		visit(function);
		
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

		// visit all assignments
		for (AssignmentContext a : ctx.assignment()) {		
			res = visit(a);
			valid = valid && res;
		}

		res = visit(ctx.logicalOperation());
		valid = valid && res;

		// visit scope
		valid = valid && visit(ctx.scope());

		return valid;
	}

	@Override 
	public Boolean visitWhileLoop(WhileLoopContext ctx) {
		Boolean valid = true;
		Boolean res = visit(ctx.logicalOperation());
		valid = valid && res;

		// visit all scope
		valid = valid && visit(ctx.scope());

		return valid;
	}

	@Override
	public Boolean visitCondition_withoutElse(Condition_withoutElseContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitCondition_withElse(Condition_withElseContext ctx) {
		return visitChildren(ctx);
	}
	
	//FIXME verify that scopes work ly for IF statements and loops and functions
	@Override 
	public Boolean visitIfCondition(IfConditionContext ctx) {
		Boolean valid = true;
		Boolean res = visit(ctx.logicalOperation());
		valid = valid && res;

		// visit all scope
		valid = valid && visit(ctx.scope());
		
		return valid;
	}

	@Override 
	public Boolean visitElseIfCondition(ElseIfConditionContext ctx) {
		Boolean valid = true;
		Boolean res = visit(ctx.logicalOperation());
		valid = valid && res;

		// visit all scope
		valid = valid && visit(ctx.scope());
		
		return valid;
	}

	@Override 
	public Boolean visitElseCondition(ElseConditionContext ctx) {
		Boolean valid = true;

		// visit all scope
		valid = valid && visit(ctx.scope());
				
		return valid;
	}

	// --------------------------------------------------------------------------
	// Logical Operations

	@Override 
	public Boolean visitLogicalOperation_Parenthesis(LogicalOperation_ParenthesisContext ctx) {
		if(!visit(ctx.logicalOperation())) {
			// TODO faz sentido esta mensagem de erro aqui?
			ErrorHandling.printError(ctx, "Logical operands must have value boolean");
			return false;
		}
		if (debug) {ErrorHandling.printInfo(ctx, "[OP_LOGIC_OP_PAR]");}
		return true;
	}

	@Override 
	public Boolean visitLogicalOperation_Operation(LogicalOperation_OperationContext ctx) {
		Boolean validOp0 = visit(ctx.logicalOperation(0));
		Boolean validOp1 = visit(ctx.logicalOperation(1));

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_LOGIC_OP_OP]");
		}

		if (validOp0 && validOp1) {
			return true;
		}

		ErrorHandling.printError(ctx, "Logical operands must have value boolean");
		return false;

	}

	@Override 
	public Boolean visitLogicalOperation_logicalOperand(LogicalOperation_logicalOperandContext ctx) {
		Boolean valid = visit(ctx.logicalOperand());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_LOGIC_OP_OPERAND]");
		}

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

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_LOGIC_OPERAND_VAR]");
		}

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

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_LOGIC_OPERAND_NOT_VAR]");
		}

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

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_LOGIC_OPERAND_VALUE]");
		}

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

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_LOGIC_OPERAND_NOT_VALUE]");
		}

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
		mapCtxObj.put(ctx, true);
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
		
		// booleans and string cannot be casted
		if(mapCtxObj.get(ctx.operation()) instanceof Boolean || mapCtxObj.get(ctx.operation()) instanceof String) {
			ErrorHandling.printError(ctx, "Type cannot be casted");
			return false;
		}

		Variable temp = (Variable) mapCtxObj.get(ctx.operation());
		Variable opRes = new Variable(temp); // deep copy
		String destinationType = ctx.cast().ID().getText();

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_CAST] Visited Operation Cast");
			ErrorHandling.printInfo(ctx, "[OP_CAST] Casting Variable " + opRes);
			ErrorHandling.printInfo(ctx, "[OP_CAST] Casting to " + destinationType + ". Cast can happen? "
			+ (opRes.getType().getCode() == typesTable.get("number").getCode()) + "\n");
		}

		// cast is to a compatible type.
		if (opRes.convertTypeTo(typesTable.get(destinationType))) {
			return true;
		}
		
		// type is number cast is possible
		if (opRes.getType().equals(typesTable.get("number"))) {
			Type newType = typesTable.get(ctx.cast().ID().getText());
			if (newType == null) {
				ErrorHandling.printError(ctx,"Cast Type is not declared");
				return false;
			}
			Variable res = new Variable(newType, opRes.getValue());
			if (debug) {ErrorHandling.printInfo(ctx, "[OP_CAST] variable res " + res);}
			mapCtxObj.put(ctx, res);
			return true;
		}
	
		return false;
	}

	@Override 
	public Boolean visitOperation_Parenthesis(Operation_ParenthesisContext ctx) {
		if(!visit(ctx.operation())) {
			return false;
		}

		if(mapCtxObj.get(ctx.operation()) instanceof Boolean || mapCtxObj.get(ctx.operation()) instanceof String) {
			ErrorHandling.printError(ctx, "Incompatible types in operation");
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

		if(mapCtxObj.get(ctx.operation(0)) instanceof Boolean || mapCtxObj.get(ctx.operation(0)) instanceof String) {
			ErrorHandling.printError(ctx, "Incompatible types in operation");
			return false;
		}

		if(mapCtxObj.get(ctx.operation(1)) instanceof Boolean || mapCtxObj.get(ctx.operation(1)) instanceof String) {
			ErrorHandling.printError(ctx, "Incompatible types in operation");
			return false;
		}

		Variable aux = (Variable) mapCtxObj.get(ctx.operation(0));
		Variable a = new Variable(aux); // deep copy
		
		aux = (Variable) mapCtxObj.get(ctx.operation(1));
		Variable b = new Variable(aux); // deep copy
		
		String op = ctx.op.getText();

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] Visiting Operation Mult_Div_Mod");
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] op0: " + ctx.operation(0).getText());
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] op1: " + ctx.operation(1).getText());
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] variable a " + a);
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] variable b " + b);
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] op		 " + op + "\n");
			ErrorHandling.printInfo(ctx, "[OP_MULTDIVMOD] temp: " + aux);
		}
		
		// TODO update to using Variable.mod();
		// Modulus operation
		if (op.equals("%")) {
			try {
				Variable res = Variable.mod(a, b);
				mapCtxObj.put(ctx, res);
				return true;
			}
			catch (IllegalArgumentException e) {
				ErrorHandling.printError(ctx, "Right side of mod operation has to be of Type Number!");
				return false;
			}
		}

		// Multiplication operation
		// TODO melhorar o acesso a typesTable e a codigos. Talvez devesse haver uma classe apenas para resolver cenas com a tabela.
		if (op.equals("*")) {
			Variable res = Variable.multiply(a, b); 
			Code resCode = res.getType().getCode(); 
			Collection<Type> types = typesTable.values(); 
			for (Type t : types) { 
				if (t.getCode().equals(resCode)) {
					res = new Variable(typesTable.get(t.getTypeName()), res.getValue());
					break; 
				} 

			} 
			if (debug) { ErrorHandling.printInfo(ctx, "result of multiplication is Variable " + res);}
			mapCtxObj.put(ctx, res); 
		}
		
		// Division operation
		if (op.equals("/")) {
			
			if (b.getValue() == 0) {ErrorHandling.printError(ctx, "Can't divide by zero!");}
			
			Variable res = Variable.divide(a, b); 
			Code resCode = res.getType().getCode(); 
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

		if(mapCtxObj.get(ctx.operation()) instanceof Boolean || mapCtxObj.get(ctx.operation()) instanceof String) {
			ErrorHandling.printError(ctx, "Incompatible types in operation");
			return false;
		}

		Variable aux = (Variable) mapCtxObj.get(ctx.operation());
		Variable a = new Variable(aux); // deep copy
		mapCtxObj.put(ctx, Variable.simetric(a));

		if(debug) {ErrorHandling.printInfo(ctx, "[OP_OP_SIMETRIC]");}

		return true;
	}

	@Override 
	public Boolean visitOperation_Add_Sub(Operation_Add_SubContext ctx) {
		if(!visit(ctx.operation(0)) || !visit(ctx.operation(1))) {
			return false;
		}
		
		Object obj0 = mapCtxObj.get(ctx.operation(0));
		Object obj1 = mapCtxObj.get(ctx.operation(1));
		String op = ctx.op.getText();
		
		// one of the elements in operation is boolean
		if(obj0 instanceof Boolean || obj1 instanceof Boolean) {
			ErrorHandling.printError(ctx, "Incompatible types in operation");
			return false;
		}
		
		// both elemts are string and operation is + (concatenation)
		if(obj0 instanceof String && obj1 instanceof String) {
			if (op.equals("+")) {
				String str0 = (String) obj0;
				String str1 = (String) obj1;
				mapCtxObj.put(ctx, str0 + str1);
				return true;
			}
			ErrorHandling.printError(ctx, "Incompatible types in operation");
			return false;
		}
		
		// only one element is string other is Variable - incompatible
		if (obj0 instanceof String || obj1 instanceof String) {
			ErrorHandling.printError(ctx, "Incompatible types in operation");
			return false;
		}
		
		// both elements are Variables
		Variable aux = (Variable) obj0;
		Variable a = new Variable(aux); // deep copy
		
		aux = (Variable) obj1;
		Variable b = new Variable(aux); // deep copy

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ADDSUB] Visiting Operation Add_Sub");
			ErrorHandling.printInfo(ctx, "[OP_ADDSUB] variable a " + a);
			ErrorHandling.printInfo(ctx, "[OP_ADDSUB] variable b " + b + "\n");
		}

		// Addition operation
		// TODO mudar o grafo talvez. Tenho de verificar se tipos sao iguais, deveria bastar tentar converter
		// para tal é necessário que se introduza aresta do tipo para ele proprio. Verificar se isso nao tras outros problemas.
		if (ctx.op.getText().equals("+")) {
			try {
				Variable res = Variable.add(a, b);
				if (debug) { ErrorHandling.printInfo(ctx, "result of sum is Variable " + res);}
				mapCtxObj.put(ctx, res);
			}
			catch (IllegalArgumentException e) {
				ErrorHandling.printError(ctx, "Incompatible types in operation");
				return false;
			}
			
			
		}
		
		// Subtraction operation
		if (ctx.op.getText().equals("-")) {
			try {
				Variable res = Variable.subtract(a, b);
				if (debug) { ErrorHandling.printInfo(ctx, "result of sum is Variable " + res);}
				mapCtxObj.put(ctx, res);
			}
			catch (IllegalArgumentException e) {
				ErrorHandling.printError(ctx, "Incompatible types in operation");
				return false;
			}
		}
		
		return true;
	}

	@Override 
	public Boolean visitOperation_Power(Operation_PowerContext ctx) {
		if(!visit(ctx.operation(0)) || !visit(ctx.operation(1))) {
			return false;
		}
		Object obj0 = mapCtxObj.get(ctx.operation(0));
		Object obj1 = mapCtxObj.get(ctx.operation(1));

		if (obj0 instanceof String || obj1 instanceof String) {
			ErrorHandling.printError(ctx, "Type string is not compatible with power operation");
			return false;
		}

		if (obj0 instanceof Boolean || obj1 instanceof Boolean) {
			ErrorHandling.printError(ctx, "Type boolean is not compatible with power operation");
			return false;
		}

		Variable aux = (Variable) obj1;
		Variable pow = new Variable(aux);

		if (!pow.getType().getTypeName().equals("number")) {
			ErrorHandling.printError(ctx, "power operation requires type number in exponent");
			return false;
		}

		aux = (Variable) obj0;
		Variable base = new Variable(aux);

		Variable res = Variable.power(base, pow);
		Code resCode = res.getType().getCode(); 
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
		
		// TODO na realidade acho que, uma vez que esta funcao nao deixa passar booleans, nao preciso verificar tudo em cima
		Object obj = symbolTable.get(ctx.var().ID().getText());
		if (obj instanceof Boolean) {
			ErrorHandling.printError(ctx, "Cannot operate with boolean!");
		}

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_VAR] Visited Operation Variable");
			ErrorHandling.printInfo(ctx, "[OP_VAR] varName is " + ctx.var().ID().getText());
			ErrorHandling.printInfo(ctx, "[OP_VAR] obj " + obj);
		}

		// verify that var is not of type string
		// TODO na reliade esta funcao impede que String suba nas opercoes, como tal, nao � possivel concatenar strings.
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
		return true;
	}

	@Override 
	public Boolean visitOperation_NUMBER(Operation_NUMBERContext ctx) {
		try {
			Double value = Double.parseDouble(ctx.NUMBER().getText());
			Variable a = new Variable(typesTable.get("number"), value);
			mapCtxObj.put(ctx, a);
		}
		catch (NumberFormatException e) {
			ErrorHandling.printError(ctx, "Not a valid value");
			return false;
		}

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_NUMBER] Visited Operation Number");
			ErrorHandling.printInfo(ctx, "--- Value is " + Double.parseDouble(ctx.NUMBER().getText()));
		}

		return true;
	}

	// --------------------------------------------------------------------------
	// Prints

	@Override
	public Boolean visitPrint_Print(Print_PrintContext ctx) {
		boolean valid = true;
		boolean res = true;

		// visit all PrintVars
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

		// visit all PrintVars
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
			ErrorHandling.printError(ctx, "Variable \"" + key + "\" is not declared!");
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
	
	private boolean isValidNewVariableName(String varName, ParserRuleContext ctx) {

		if (symbolTable.containsKey(varName)) {
			ErrorHandling.printError(ctx, "Variable \"" + varName +"\" already declared");
			return false;
		}
		
		if (reservedWords.contains(varName)) {
			ErrorHandling.printError(ctx, varName +"\" is a reserved word");
			return false;
		}
		
		return true;
	}

	private static String getStringText(String str) {
		str = str.substring(1, str.length() -1);
		return str;
	}

}