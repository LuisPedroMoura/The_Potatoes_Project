package compiler;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

import potatoesGrammar.PotatoesBaseVisitor;
import potatoesGrammar.PotatoesParser.*;
import tests.typesGrammar.TestGraph;
import typesGrammar.TypesFileInfo;
import utils.Type;
import utils.Variable;
import utils.errorHandling.ErrorHandling;

/**
 * 
 * <b>PotatoesSemanticCheck</b><p>
 * 
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class PotatoesSemanticCheck extends PotatoesBaseVisitor<Boolean>  {

	// Static Field (Debug Only)
	private static final boolean debug = false;

	static String path;
	private static 	 TypesFileInfo typesFileInfo; // initialized in visitUsing();
	private static 	 Map<String, Type> typesTable;

	protected static ParseTreeProperty<Object> mapCtxObj = new ParseTreeProperty<>();
	protected static Map<String, Object> symbolTable = new HashMap<>();

	private static Type destinationType;

	public static ParseTreeProperty<Object> getMapCtxObj(){
		return mapCtxObj;
	}

	public static TypesFileInfo getTypesFileInfo() {
		return typesFileInfo;
	}

	// --------------------------------------------------------------------------------------------------------------------
	// MAIN RULES----------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE - DON'T DELETE FROM THIS FILE
	public Boolean visitProgram(ProgramContext ctx) {
		System.out.println("HELOOOOO!!!!!");
		Boolean valid = visit(ctx.using());
		List<CodeContext> codesInstructions = ctx.code();
		int i = -1;
		for (CodeContext c : codesInstructions) {
			i = i + 1;
			System.out.println(i);
			Boolean res = visit(c);
			
		//ErrorHandling.printInfo("Visiting " + c.getText() + " : " + res);
			valid = valid && res;
		}

		return valid;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitUsing(UsingContext ctx) {
		path = getStringText(ctx.STRING().getText());
		typesFileInfo = new TypesFileInfo(path);
		typesTable = typesFileInfo.getTypesTable();
		mapCtxObj.put(ctx, path);

		if (debug) {
			ErrorHandling.printInfo(ctx, "Types File path is: " + path);
			ErrorHandling.printInfo(ctx, typesFileInfo.toString());
			new TestGraph(typesFileInfo.getTypesGraph());

			Map<String, Type> map = typesFileInfo.getTypesTable();

			JFrame f = new JFrame("View Table");
			f.setSize(557, 597);
			f.setResizable(true);
			f.setVisible(true);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setLocationRelativeTo(null);

			JPanel contents = new JPanel();
			contents.setBorder(new EmptyBorder(5, 5, 5, 5));
			contents.setLayout(new BorderLayout(0, 0));
			f.setContentPane(contents);

			JTable table = new JTable(map.size() * 100,2);
			//JTable.createScrollPaneForTable(table);
			//table.setAutoResizeMode();
			int row=0;
			for(Map.Entry<String,Type> entry: map.entrySet()){
				table.setValueAt(entry.getKey(),row,0);
				table.setValueAt(entry.getValue(),row,1);
				row++;
				for (Type t : entry.getValue().getOpTypes()) {
					table.setValueAt("opType entry from " + entry.getKey() + " is " ,row,0);
					table.setValueAt(t,row,1);
					row++;
				}
			}

			table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

			for (int column = 0; column < table.getColumnCount(); column++)
			{
				TableColumn tableColumn = table.getColumnModel().getColumn(column);
				int preferredWidth = tableColumn.getMinWidth();
				int maxWidth = tableColumn.getMaxWidth();

				for (int row1 = 0; row1 < table.getRowCount(); row1++)
				{
					TableCellRenderer cellRenderer = table.getCellRenderer(row1, column);
					Component c = table.prepareRenderer(cellRenderer, row1, column);
					int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
					preferredWidth = Math.max(preferredWidth, width);

					//  We've exceeded the maximum width, no need to check other rows

					if (preferredWidth >= maxWidth)
					{
						preferredWidth = maxWidth;
						break;
					}
				}

				tableColumn.setPreferredWidth( preferredWidth );
			}

			contents.add(table);
		}
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitCode_Declaration(Code_DeclarationContext ctx) {
		return visit(ctx.declaration());
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitCode_Assignment(Code_AssignmentContext ctx) {
		Boolean result =  visit(ctx.assignment());
		//if(debug) {ErrorHandling.printInfo("Visited " + ctx.assignment().getText() + " : " + result);}
		return result;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitCode_Function(Code_FunctionContext ctx) {
		return visitChildren(ctx);
	}

	// --------------------------------------------------------------------------------------------------------------------	
	// CLASS - STATEMENTS-----------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitStatement_Declaration(Statement_DeclarationContext ctx) {
		return visit(ctx.declaration());
	}
	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitStatement_Assignment(Statement_AssignmentContext ctx) {
		boolean valid =  visit(ctx.assignment());
		if(debug) {ErrorHandling.printInfo(ctx, "Visited " + ctx.assignment().getText() + " : " + valid);}
		return valid;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitStatement_Control_Flow_Statement(Statement_Control_Flow_StatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitStatement_FunctionCall(Statement_FunctionCallContext ctx) {
		return visit(ctx.functionCall());
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitStatement_Function_Return(Statement_Function_ReturnContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitStatement_Print(Statement_PrintContext ctx) {
		return visit(ctx.print());
	}

	// --------------------------------------------------------------------------------------------------------------------	
	// CLASS - DECLARATIONS-----------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitDeclaration_array(Declaration_arrayContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitDeclaration_Var(Declaration_VarContext ctx) {
		return visitChildren(ctx);
	}

	// --------------------------------------------------------------------------------------------------------------------
	// CLASS - ASSIGNMENTS-----------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Declaration_Not_Boolean(Assignment_Var_Declaration_Not_BooleanContext ctx) {
		visit(ctx.varDeclaration());
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
			visit(ctx.var());
			Object obj = mapCtxObj.get(ctx.var());
			if (obj instanceof Boolean) {
				Boolean b = (Boolean) (symbolTable.get(ctx.var().ID().getText()));
				symbolTable.put(ctx.varDeclaration().ID().getText(), !b);
				mapCtxObj.put(ctx, !b);
				if(debug) {ErrorHandling.printInfo(ctx, "boolean variable with value: " + !b + "was assigned");}
				return true;
			}
			ErrorHandling.printError(ctx, "Cannot assign logical operation to non boolean Type");
			return false;
		}

		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" and boolean are not compatible");
		return false;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Declaration_Value(Assignment_Var_Declaration_ValueContext ctx) {
		visit(ctx.varDeclaration());
		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		String varName = ctx.varDeclaration().ID().getText();
		visit(ctx.value());
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
			if(debug) {ErrorHandling.printInfo(ctx, "assigned boolean b=" + b + " to " + ctx.varDeclaration().ID().getText());}
			return true;
		}

		// assign string to string
		if (value instanceof String && typeName.equals("string")) {
			String str = (String) value;
			symbolTable.put(ctx.varDeclaration().ID().getText(), str);
			mapCtxObj.put(ctx, str);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned string str=" + str + " to " + ctx.varDeclaration().ID().getText());}
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
				if(debug) {ErrorHandling.printInfo(ctx, "assigned Variable var=" + a.getType().getTypeName() + ", " +
						"val=" + a.getValue() + " to " + ctx.varDeclaration().ID().getText());}
				return true;
			}
		}

		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with given Type");
		return false;
	}

	@Override  // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Declaration_Comparison(Assignment_Var_Declaration_ComparisonContext ctx) {
		visit(ctx.varDeclaration());
		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		String varName = ctx.varDeclaration().ID().getText();
		
		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_COMP] Visited visitAssignment_Var_Declaration_Comparison");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + varName + " with type " + typeName);
		}

		// assign boolean to boolean
		if (typeName.equals("boolean")) {
			visit(ctx.comparison());
			Boolean b = (Boolean) mapCtxObj.get(ctx.comparison());
			symbolTable.put(ctx.varDeclaration().ID().getText(), b);
			mapCtxObj.put(ctx, b);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned boolean b=" + b + " to " + ctx.varDeclaration().ID().getText());}
			return true;
		}

		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with boolean");
		return false;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Declaration_Operation(Assignment_Var_Declaration_OperationContext ctx) {
		visit(ctx.varDeclaration());
		String typeName = (String) mapCtxObj.get(ctx.varDeclaration().type());
		String varName = ctx.varDeclaration().ID().getText();

		// verify that variable to be created has valid name
		if (symbolTable.containsKey(varName)) {
			ErrorHandling.printError(ctx, varName + " is a reserved word");
			return false;
		}

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_OP] Visited visitAssignment_Var_Declaration_Operation");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + varName + " with type " + typeName);
			ErrorHandling.printInfo(ctx, "--- destinationType is " + destinationType);
		}
		
		// assign Variable to string is not possible
		if (typeName.equals("string")) {
			ErrorHandling.printError(ctx, "Cannot assign Type \"boolean\" to \"string\"");
			return false;
		}
		
		Object obj = mapCtxObj.get(ctx.operation());
		
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
		visit(ctx.operation());
		Variable temp = (Variable) mapCtxObj.get(ctx.operation());
		Variable a = new Variable(temp);
		destinationType = typesTable.get(typeName);
		destinationType.clearCheckList();
		
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

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Not_Boolean(Assignment_Var_Not_BooleanContext ctx) {
		visit(ctx.var(0));
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
			visit(ctx.var(1));
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

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Value(Assignment_Var_ValueContext ctx) {
		visit(ctx.var());
		String typeName = (String) mapCtxObj.get(ctx.var().ID());
		visit(ctx.value());
		Object value = mapCtxObj.get(ctx.value());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_VALUE] Visited visitAssignment_Var_Declaration_Operation");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + ctx.var().ID().getText() + " with type " + symbolTable.get(ctx.var().ID().getText()));
		}

		// assign boolean to boolean
		if (value instanceof Boolean && typeName.equals("boolean")) {
			Boolean b = (Boolean) value;
			symbolTable.put(ctx.var().ID().getText(), b);
			mapCtxObj.put(ctx, b);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned boolean b=" + b + " to " + ctx.var().ID().getText());}
			return true;
		}

		// assign string to string
		if (value instanceof String && typeName.equals("string")) {
			String str = (String) value;
			symbolTable.put(ctx.var().ID().getText(), str);
			mapCtxObj.put(ctx, str);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned string str=" + str + " to " + ctx.var().ID().getText());}
			return true;
		}

		// assign compatible types
		if (typesTable.containsKey(typeName)) {
			destinationType = typesTable.get(typeName);
			destinationType.clearCheckList();
			Variable temp = (Variable) value;
			Variable a = new Variable(temp);
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

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Comparison(Assignment_Var_ComparisonContext ctx) {
		visit(ctx.var());
		String typeName = (String) mapCtxObj.get(ctx.var().ID());

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_COMP] Visited visitAssignment_Var_Declaration_Operation");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + ctx.var().ID().getText() + " with type " + symbolTable.get(ctx.var().ID().getText()));
		}

		// verify that assigned var has type boolean
		if (typeName.equals("boolean")) {
			visit(ctx.comparison());
			Boolean b = (Boolean) mapCtxObj.get(ctx.comparison());
			symbolTable.put(ctx.var().ID().getText(), b);
			mapCtxObj.put(ctx, b);
			if(debug) {ErrorHandling.printInfo(ctx, "assigned boolean b=" + b + " to " + ctx.var().ID().getText());}
			return true;
		}

		// Types are not compatible
		ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is not compatible with boolean!");
		return false;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitAssignment_Var_Operation(Assignment_Var_OperationContext ctx) {
		visit(ctx.var());
		Object obj = mapCtxObj.get(ctx.var());
		
		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_ASSIGN_VAR_OP] Visited visitAssignment_Var_Declaration_Operation");
			ErrorHandling.printInfo(ctx, "--- Assigning to " + ctx.var().ID().getText() + " with type " + symbolTable.get(ctx.var().ID().getText()));
		}
		
		if(obj instanceof String) {
			ErrorHandling.printError(ctx, "string Type is not compatible with the operation \"" +ctx.operation().getText() + "\"");
			return false;
		}
		if(obj instanceof Boolean) {
			ErrorHandling.printError(ctx, "boolean Type is not compatible with the operation \"" +ctx.operation().getText() + "\"");
			return false;
		}
		
		Variable var = (Variable) obj;
		String typeName = var.getType().getTypeName();
		destinationType = typesTable.get(typeName); // static field to aid in operation predictive convertions
		destinationType.clearCheckList();
		
		visit(ctx.operation());
		Object opValue = mapCtxObj.get(ctx.operation());
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
	public Boolean visitAssignment_Var_ValueList(Assignment_Var_ValueListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Var_ValueList(ctx);
	}

	@Override
	public Boolean visitAssingment_Var_FunctionCall(Assingment_Var_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssingment_Var_FunctionCall(ctx);
	}

	@Override
	public Boolean visitAssignment_Array_Var(Assignment_Array_VarContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Array_Var(ctx);
	}

	@Override
	public Boolean visitAssignment_Array_ValuesList(Assignment_Array_ValuesListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Array_ValuesList(ctx);
	}

	@Override
	public Boolean visitAssignment_Array_FunctionCall(Assignment_Array_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_Array_FunctionCall(ctx);
	}

	@Override
	public Boolean visitAssignment_ArrayAccess_Not_Boolean(Assignment_ArrayAccess_Not_BooleanContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_Not_Boolean(ctx);
	}

	@Override
	public Boolean visitAssignment_ArrayAccess_Value(Assignment_ArrayAccess_ValueContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_Value(ctx);
	}

	@Override
	public Boolean visitAssignment_ArrayAccess_Comparison(Assignment_ArrayAccess_ComparisonContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_Comparison(ctx);
	}

	@Override
	public Boolean visitAssignment_ArrayAccess_Operation(Assignment_ArrayAccess_OperationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_Operation(ctx);
	}

	@Override
	public Boolean visitAssignment_ArrayAccess_ValueList(Assignment_ArrayAccess_ValueListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_ValueList(ctx);
	}

	@Override
	public Boolean visitAssignment_ArrayAccess_FunctionCall(Assignment_ArrayAccess_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitAssignment_ArrayAccess_FunctionCall(ctx);
	}

	// --------------------------------------------------------------------------------------------------------------------	
	// FUNCTIONS-------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	
	@Override
	public Boolean visitFunction(FunctionContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFunction(ctx);
	}

	@Override
	public Boolean visitFunctionReturn(FunctionReturnContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFunctionReturn(ctx);
	}

	@Override
	public Boolean visitFunctionCall(FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFunctionCall(ctx);
	}

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
	public Boolean visitPrintVar(PrintVarContext ctx) {
		return visitChildren(ctx);
	}

	// --------------------------------------------------------------------------------------------------------------------
	// CONTROL FLOW STATMENTS------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitControlFlowStatement(ControlFlowStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitForLoop(ForLoopContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitWhileLoop(WhileLoopContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public Boolean visitWhen(WhenContext ctx) {
		// TODO Auto-generated method stub
		return super.visitWhen(ctx);
	}

	@Override
	public Boolean visitWhenCase(WhenCaseContext ctx) {
		// TODO Auto-generated method stub
		return super.visitWhenCase(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitCondition(ConditionContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitIfCondition(IfConditionContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitElseIfCondition(ElseIfConditionContext ctx) {
		return visitChildren(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitElseCondition(ElseConditionContext ctx) {
		return visitChildren(ctx);
	}

	// --------------------------------------------------------------------------------------------------------------------
	// LOGICAL OPERATIONS----------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitLogicalOperation_Parenthesis(LogicalOperation_ParenthesisContext ctx) {
		boolean valid = visit(ctx.logicalOperation());
		if (debug) {ErrorHandling.printInfo(ctx, "[OP_LOGIC_OP_PAR]");}
		if(valid) {
			return true;
		}
		ErrorHandling.printError(ctx, "Logical operands must have value boolean");
		if(debug) {ErrorHandling.printInfo(ctx, "visited logicalOperation_Parenthesis");}
		return false;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitLogicalOperation_Operation(LogicalOperation_OperationContext ctx) {
		boolean validOp0 = visit(ctx.logicalOperation(0));
		boolean validOp1 = visit(ctx.logicalOperation(1));
		if (debug) {ErrorHandling.printInfo(ctx, "[OP_LOGIC_OP_OP]");}
		if (validOp0 && validOp1) {
			return true;
		}
		ErrorHandling.printError(ctx, "Logical operands must have value boolean");
		return false;

	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitLogicalOperation_logicalOperand(LogicalOperation_logicalOperandContext ctx) {
		boolean valid = visit(ctx.logicalOperand());
		if (debug) {ErrorHandling.printInfo(ctx, "[OP_LOGIC_OP_OPERAND]");}
		mapCtxObj.put(ctx, mapCtxObj.get(ctx.logicalOperand()));
		return valid;
	}
	

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitLogicalOperand_Comparison(LogicalOperand_ComparisonContext ctx) {
		return visit(ctx.comparison());
	}
	
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
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
		boolean valid = visit(ctx.value());
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
		boolean valid = visit(ctx.value());
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
		boolean validOp0 = visit(ctx.compareOperation(0));
		boolean validOp1 = visit(ctx.compareOperation(1));
		if (debug) {ErrorHandling.printInfo(ctx, "[OP_COMPARISON]");}
		if (validOp0 && validOp1) {
			Object obj0 = mapCtxObj.get(ctx.compareOperation(0));
			Object obj1 = mapCtxObj.get(ctx.compareOperation(1));
			
			if(obj0 instanceof Boolean && obj1 instanceof Boolean) {
				return true;
			}
			
			if(obj0 instanceof Variable && obj1 instanceof Variable) {
				Variable a = (Variable) obj0;
				Variable b = (Variable) obj1;
				boolean comparisonIsPossible = a.typeIsCompatible(b.getType());
				return comparisonIsPossible;
			}
		}
		ErrorHandling.printError(ctx, "Types to be compared are not compatible");
		return false;
	}

	@Override
	public Boolean visitCompareOperation_Operation(CompareOperation_OperationContext ctx) {
		boolean valid = visitChildren(ctx);
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

	// --------------------------------------------------------------------------------------------------------------------
	// OPERATIONS------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
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

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitOperation_Parenthesis(Operation_ParenthesisContext ctx) {
		if(!visitChildren(ctx)) {
			return false;
		}
		mapCtxObj.put(ctx, mapCtxObj.get(ctx.operation()));
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitOperation_Mult_Div_Mod(Operation_Mult_Div_ModContext ctx) {
		if(!visitChildren(ctx)) {
			return false;
		}
		Variable temp = (Variable) mapCtxObj.get(ctx.operation(0));
		Variable a = new Variable(temp);
		temp = (Variable) mapCtxObj.get(ctx.operation(1));
		System.out.println("-------------------- " + temp);
		System.out.println("---------------op0 - " + ctx.operation(0).getText());
		System.out.println("---------------op1 - " + ctx.operation(1).getText());
		Variable b = new Variable(temp);
		String op = ctx.op.getText();

		if (debug) {
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
			a.MultDivCheckConvertType(destinationType);
			if(debug) {ErrorHandling.printInfo(ctx, "Variable a was converted to " + a);}
		}
		catch (Exception e) {
			ErrorHandling.printWarning(ctx, "Variable has multiple inheritance. Operation may not be resolved!");
		}

		try {
			b.MultDivCheckConvertType(destinationType);
			if(debug) {ErrorHandling.printInfo(ctx, "Variable b was converted to " + b);}
		}
		catch (Exception e) {
			ErrorHandling.printWarning(ctx, "Variable has multiple inheritance. Operation may not be resolved!");
		}

		// variables are converted, do the operation
		if (op.equals("*")) {
			//mapCtxObj.put(ctx, Variable.multiply(a, b));
			Variable res = Variable.multiply(a, b); 
			Double resCode = res.getType().getCode(); 
			Collection<Type> types = typesTable.values(); 
			for (Type t : types) { 
				if (t.getCode() == resCode) { 
					res.getType().setTypeName(t.getTypeName()); 
					res.getType().setPrintName(t.getPrintName()); 
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
					res.getType().setTypeName(t.getTypeName()); 
					res.getType().setPrintName(t.getPrintName()); 
					break; 
				} 

			} 
			if (debug) { ErrorHandling.printInfo(ctx, "result of division is Variable " + res);}
			mapCtxObj.put(ctx, res);
		}	
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitOperation_Simetric(Operation_SimetricContext ctx) {
		if(!visitChildren(ctx)) {
			return false;
		}
		Variable temp = (Variable) mapCtxObj.get(ctx.operation());
		Variable a = new Variable(temp);
		mapCtxObj.put(ctx, Variable.simetric(a));
		ErrorHandling.printInfo(ctx, "[OP_OP_SIMETRIC]");
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
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

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitOperation_Power(Operation_PowerContext ctx) {
		if(!visitChildren(ctx)) {
			return false;
		}
		Variable temp = (Variable) mapCtxObj.get(ctx.operation(0));
		Variable a = new Variable(temp);
		temp = (Variable) mapCtxObj.get(ctx.operation(1));
		Variable pow = new Variable(temp);

		// verify that power is of type number
		if (!pow.getType().getTypeName().equals("number")) {
			ErrorHandling.printError(ctx, "Power must be a number or a variable with type number!");
			return false;
		}

		Variable res = Variable.power(a, pow);
		mapCtxObj.put(ctx, res);

		if (debug) {
			ErrorHandling.printInfo(ctx, "[OP_POWER] Visited Operation Power");
			ErrorHandling.printInfo(ctx, "--- Powering Variable " + a + "with power " + pow + "and result is " + res);
		}

		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
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
		// verify that var is not of type boolean	
		if (obj instanceof Boolean) {
			ErrorHandling.printError(ctx, "Cannot operate with boolean!");
			return false;
		}

		// var is of type Variable
		mapCtxObj.put(ctx, mapCtxObj.get(ctx.var()));
		return true;
	}

	@Override
	public Boolean visitOperation_FunctionCall(Operation_FunctionCallContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_FunctionCall(ctx);
	}

	@Override
	public Boolean visitOperation_ArrayAccess(Operation_ArrayAccessContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_ArrayAccess(ctx);
	}

	@Override
	public Boolean visitOperation_ArrayLength(Operation_ArrayLengthContext ctx) {
		// TODO Auto-generated method stub
		return super.visitOperation_ArrayLength(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
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

	// --------------------------------------------------------------------------------------------------------------------
	// STRUCTURES - ARRAYS------------------------------------------------------------------	
	// --------------------------------------------------------------------------------------------------------------------
	@Override
	public Boolean visitArrayDeclaration(ArrayDeclarationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitArrayDeclaration(ctx);
	}

	@Override
	public Boolean visitArrayType(ArrayTypeContext ctx) {
		// TODO Auto-generated method stub
		return super.visitArrayType(ctx);
	}

	@Override
	public Boolean visitArrayAccess(ArrayAccessContext ctx) {
		// TODO Auto-generated method stub
		return super.visitArrayAccess(ctx);
	}

	@Override
	public Boolean visitArrayLength(ArrayLengthContext ctx) {
		// TODO Auto-generated method stub
		return super.visitArrayLength(ctx);
	}

	// --------------------------------------------------------------------------------------------------------------------
	// VARS AND TYPES------------------------------------------------------------------------ 
	// --------------------------------------------------------------------------------------------------------------------
	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitVar(VarContext ctx) {
		mapCtxObj.put(ctx, symbolTable.get(ctx.ID().getText()));
		return true;
	}

	@Override  // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitVarDeclaration(VarDeclarationContext ctx) {
		return visit(ctx.type());
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitType_Number_Type(Type_Number_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.NUMBER_TYPE().getText());
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitType_Boolean_Type(Type_Boolean_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.BOOLEAN_TYPE().getText());
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitType_String_Type(Type_String_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.STRING_TYPE().getText());
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitType_Void_Type(Type_Void_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.VOID_TYPE().getText());
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitType_ID_Type(Type_ID_TypeContext ctx) {
		mapCtxObj.put(ctx, ctx.ID().getText());
		return true;
	}

	@Override
	public Boolean visitType_ArrayType(Type_ArrayTypeContext ctx) {
		// TODO Auto-generated method stub
		return super.visitType_ArrayType(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitValue_Cast_Number(Value_Cast_NumberContext ctx) {
		visit(ctx.cast());
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

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitValue_Number(Value_NumberContext ctx) {
		Double number = Double.parseDouble(ctx.NUMBER().getText());
		Variable a = new Variable(typesTable.get("number"), number);
		mapCtxObj.put(ctx, a);
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitValue_Boolean(Value_BooleanContext ctx) {
		Boolean b = Boolean.parseBoolean(ctx.BOOLEAN().getText());
		mapCtxObj.put(ctx, b);
		return true;
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitValue_String(Value_StringContext ctx) {
		String str = getStringText(ctx.STRING().getText());
		mapCtxObj.put(ctx, str);
		return true;
	}

	@Override
	public Boolean visitValuesList(ValuesListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitValuesList(ctx);
	}

	@Override // [LM] Done - DON'T DELETE FROM THIS FILE
	public Boolean visitCast(CastContext ctx) {
		mapCtxObj.put(ctx, ctx.ID().getText());
		return true;
	}
	// --------------------------------------------------------------------------------------------------------------------
	// AUXILIAR FUNCTIONS ---------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------	

	private static String getStringText(String str) {
		str = str.substring(1, str.length() -1);
		// FIXME escapes still need to be removed from antlr STRING token to have correct text.
		return str;
	}

}