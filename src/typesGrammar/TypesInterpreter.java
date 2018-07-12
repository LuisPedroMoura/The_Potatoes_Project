package typesGrammar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import typesGrammar.TypesParser.*;
import utils.Code;
import utils.Factor;
import utils.Graph;
import utils.Prefix;
import utils.Type;
import utils.errorHandling.ErrorHandling;


public class TypesInterpreter extends TypesBaseVisitor<Boolean> {

	// Static Field (Debug Only)
	private static final boolean debug = false; 

	// --------------------------------------------------------------------------
	// Instance Fields 
	private Map<String, Type>    typesTable    = new HashMap<>();
	private Map<String, Prefix>  prefixesTable = new HashMap<>();
	private Graph typesGraph = new Graph();
		
	private ParseTreeProperty<Type>   types    = new ParseTreeProperty<>();
	private ParseTreeProperty<Double> values   = new ParseTreeProperty<>();
	
	private Code newCode = null; // used as global variable to calculate new Code with multiple methods

	// --------------------------------------------------------------------------
	// Getters
	public Map<String, Type> getTypesTable() {
		return typesTable;
	}

	public Graph getTypesGraph() {
		return typesGraph;
	}

	public Map<String, Prefix> getPrefixesTable() {
		return prefixesTable;
	}
	
	// --------------------------------------------------------------------------
	// Callbacks 
	@Override
	public Boolean visitTypesFile(TypesFileContext ctx) {
		// add dimentionless Type number
		typesTable.put("number", new Type("number", "", new Code(1)));

		return visitChildren(ctx);
	}
	
	@Override
	public Boolean visitDeclaration(DeclarationContext ctx) {
		return visitChildren(ctx);
	}

	// --------------------------------------------------------------
	// Types Callbacks
	@Override
	public Boolean visitTypesDeclaration(TypesDeclarationContext ctx) {

		Boolean valid = true;

		List<TypeContext> typesDeclared = ctx.type(); 
		for (TypeContext type : typesDeclared) {
			if (debug) ErrorHandling.printInfo(ctx, "Processing type " + type.getText() + "...");
			valid = visit(type);		// visit all declared types
			if (!valid) return false;
		}	

		return valid;
	}
	
	@Override
	public Boolean visitType_Basic(Type_BasicContext ctx) {
		
		String typeName = ctx.ID().getText();
		String printName = getStringText(ctx.STRING().getText());

		if(!isValidNewTypeName(typeName, ctx)) return false;

		// Create basic type
		Type t = new Type(typeName, printName);
		typesTable.put(typeName, t);

		if (debug) {
			ErrorHandling.printInfo(ctx, "Added Basic Type " + t + "\n\tOriginal line: " + ctx.getText() + ")\n");
		}

		return true;
	}

	@Override
	public Boolean visitType_Compounded(Type_CompoundedContext ctx) {

		String typeName = ctx.ID().getText();
		String printName = getStringText(ctx.STRING().getText());

		if(!isValidNewTypeName(typeName, ctx)) return false;
			
		newCode = new Code();
		Boolean valid = visit(ctx.typesComposition());
		
		// FIXME I think the adaptation below works. Sill needs to be tested
			
		// Create derived type based on typesComposition
		if (valid) {
			
			Type t = new Type(typeName, printName, newCode);

			// Update Types & Symbol Tables
			typesTable.put(typeName, t);
			types.put(ctx, t);		

			if (debug) {
				ErrorHandling.printInfo(ctx, "Added Derived Type " + t + "\n\tOriginal line: " + ctx.getText() + ")\n");
			}
		}
		return valid;
	}

	@Override
	public Boolean visitType_Equivalent(Type_EquivalentContext ctx) {

		String typeName = ctx.ID().getText();
		String printName = getStringText(ctx.STRING().getText());
		
		if(!isValidNewTypeName(typeName, ctx)) return false;

		// Temporary type (reference is needed for the visitor of rule typeOpOr)
		Type t = new Type(typeName, printName);
		types.put(ctx, t);

		Boolean valid = visit(ctx.typesEquivalence());

		if (valid) {

			typesTable.put(typeName, t);

			if (debug) {
				ErrorHandling.printInfo(ctx, "Added Or Derived Type " + t + "\n\tOriginal line: " + ctx.getText() + ")\n");
			}
		}

		return valid;
	}

	// Type Operations -------------------------------
	@Override
	public Boolean visitTypesEquivalence(TypesEquivalenceContext ctx) {
		// Rule typeOpOrAlt (OR typeOpOrAlt)*	

		Boolean localDebug = debug && true;
		if (localDebug) {
			ErrorHandling.printInfo(ctx, "[PVT Debug] Parsing " + ctx.getText());
		}

		Boolean valid = true;
		Type t = types.get(ctx.parent);

		List<EquivalentTypeContext> equivTypeAlternatives = ctx.equivalentType(); 
		for (EquivalentTypeContext equivAlt : equivTypeAlternatives) {
			// visit all types declared in the equivalence 
			Boolean visit = visit(equivAlt);
			valid = valid && visit;	

			if (valid) {
				// Create the Type with a new alternative
				Type alternative = types.get(equivAlt);
				Double factor    = values.get(equivAlt);
				t.addOpType(alternative);

				// Debug
				if (localDebug) {
					ErrorHandling.printInfo(ctx, "[PVT Debug]\tParent Type: " 			  + t);
					ErrorHandling.printInfo(ctx, "[PVT Debug]\tThis alternative Type: "   + alternative);
					ErrorHandling.printInfo(ctx, "[PVT Debug]\tThis alternative Factor: " + factor);
					ErrorHandling.printInfo(ctx, "[PVT Debug]\tGoing to update the graph with an edge " 
							+ factor + ", " + alternative + ", " + factor);
				}

				// Update the types graph
				typesGraph.addEdge(new Factor(factor, true), alternative, t);
				typesGraph.addEdge(new Factor(factor, false), t, alternative);
			}
		}	

		if (localDebug) {
			ErrorHandling.printInfo(ctx, "[PVT Debug]\tObtained type: " + t);
		}

		types.put(ctx, t);

		return valid;
	}

	@Override
	public Boolean visitEquivalentType(EquivalentTypeContext ctx) {
		// Rule value ID

		Boolean valid = visit(ctx.value());

		String typeName = ctx.ID().getText();

		if (valid) {
			// Semantic Analysis : Verify it the type already exists
			if (!typesTable.containsKey(typeName)) {
				ErrorHandling.printError(ctx, "Type \"" + typeName + "\" does not exists!");
				return false;
			}

			types.put(ctx, typesTable.get(typeName));
			values.put(ctx, values.get(ctx.value()));
		}

		return valid;
	}

	@Override
	public Boolean visitType_Op_Parenthesis(Type_Op_ParenthesisContext ctx) {
		// Rule PAR_OPEN typeOp PAR_CLOSE					

		Boolean valid = visit(ctx.typeOp());
		if (valid) {
			types.put(ctx, types.get(ctx.typeOp()));
		}

		return valid;
	}

	@Override
	public Boolean visitType_Op_MultDiv(Type_Op_MultDivContext ctx) {
		// Rule typeOp op=(MULTIPLY | DIVIDE) typeOp	

		Boolean valid1 = visit(ctx.typeOp(0));
		Boolean valid2 = visit(ctx.typeOp(1));
		Boolean valid  = valid1 && valid2;

		if (valid) {

			Type a = types.get(ctx.typeOp(0));

			Type b = types.get(ctx.typeOp(1));

			Type res;
			if (ctx.op.getText().equals("*"))
				res = Type.multiply(a, b);
			else
				res = Type.divide(a, b);

			if (a.getTypeName().equals("temp")) a.getOpTypes().forEach(c -> res.addOpType(c));
			else res.addOpType(a);

			if (b.getTypeName().equals("temp")) b.getOpTypes().forEach(d -> res.addOpType(d));
			else res.addOpType(b);

			types.put(ctx, res);

			//System.out.println("visit_mult_div a: " + a);
			//System.out.println("visit_mult_div b: " + b);
			//System.out.println("visit_mult_div res: " + res);
			//if (res.getTypeName().equals("temp")) System.out.println("ATTENTION!! ADDED TEMP at visit_mult_div " + ctx.getText());
		}

		return valid;
	}

	@Override
	public Boolean visitType_Op_Power(Type_Op_PowerContext ctx) {
		// Rule <assoc=right> ID POWER INT					
		Boolean localDebug = debug && false;

		String typeName = ctx.ID().getText();

		// Semantic Analysis : Verify it the type already exists
		if (!typesTable.containsKey(typeName)) {
			ErrorHandling.printError(ctx, "Type \"" + typeName + "\" does not exists!");
			return false;
		}

		// Create power of the type
		int power = 0;
		String numberToParse = ctx.NUMBER().getText();
		if (numberToParse.contains(".")) {
			ErrorHandling.printError(ctx, "Value \"" + numberToParse + "\" is not a valid value for a power of a type!");
			return false;
		}

		try {
			power = Integer.parseInt(numberToParse);
			if (localDebug) ErrorHandling.printInfo(ctx, "[PVT Debug]\tObtained power: " + power);
		} catch (Exception e) {
			ErrorHandling.printError(ctx, "Value \"" + numberToParse + "\" is not a valid value for a power of a type!");
			return false;
		}

		Type t = typesTable.get(typeName);
		if (power < 0) {
			power = Math.abs(power);
			for (int i = 0; i < power - 1; i++) {
				Type res = Type.divide(t, t);
				if (t.getTypeName().equals("temp")) t.getOpTypes().forEach(opType -> res.addOpType(opType));
				else res.addOpType(t);
				t = res;
			}
		} 
		else {
			for (int i = 0; i < power - 1; i++) {			
				Type res = Type.multiply(t, t);
				if (t.getTypeName().equals("temp")) t.getOpTypes().forEach(opType -> res.addOpType(opType));
				else res.addOpType(t);
				t = res;
			}
		}

		types.put(ctx, t);
		return true;
	}

	@Override
	public Boolean visitType_Op_ID(Type_Op_IDContext ctx) {
		// Rule ID										

		// Creating an aux method to verify the ID (thus eliminating the need to 
		// duplicate the code from visitTypeOpOrAlt) would mean no access to ctx
		// context, needed to print the error message.

		String typeName = ctx.ID().getText();

		// Semantic Analysis : Verify it the type already exists
		if (!typesTable.containsKey(typeName)) {
			ErrorHandling.printError(ctx, "Type \"" + typeName + "\" does not exists!");
			return false;
		}

		types.put(ctx, typesTable.get(typeName));
		return true;
	}

	// --------------------------------------------------------------
	// Prefixes Callbacks
	@Override
	public Boolean visitPrefixDeclaration(PrefixDeclarationContext ctx) {
		// Rule ... (prefix NEW_LINE+)*  ...

		Boolean valid = true;

		List<PrefixContext> prefixesDeclared = ctx.prefix(); 
		for (PrefixContext prefix : prefixesDeclared) {
			//if (debug) ErrorHandling.printInfo(ctx, "Processing prefix " + prefix.getText() + "...");			
			valid = valid && visit(prefix);	// visit all declared prefixes
		}	
		return valid;
	}

	@Override
	public Boolean visitPrefix(PrefixContext ctx) {
		// Rule ID STRING COLON value

		String prefixName = ctx.ID().getText();

		// Semantic Analysis 
		Boolean value = true;

		// Prefixes can't be redefined
		if (prefixesTable.containsKey(prefixName)) {
			ErrorHandling.printError(ctx, "Prefix \"" + prefixName +"\" already defined!");
			value = false;
		}

		value = value && visit(ctx.value());

		// Create prefix
		Prefix p = new Prefix(prefixName, ctx.STRING().getText().replaceAll("\"", ""), values.get(ctx.value()));
		prefixesTable.put(prefixName, p);

		if (debug) {
			ErrorHandling.printInfo(ctx, "Added " + p + "\n\tOriginal line: " + ctx.getText() + "\n");
		}

		return value;
	}

	// --------------------------------------------------------------
	// Value Callbacks 
	@Override
	public Boolean visitValue_Parenthesis(Value_ParenthesisContext ctx) {
		Boolean valid = visit(ctx.value());

		if (valid) {
			values.put(ctx, values.get(ctx.value()));
		}

		return valid;
	}
	
	@Override
	public Boolean visitValue_Simetric(Value_SimetricContext ctx) {
		// TODO Auto-generated method stub
		return super.visitValue_Simetric(ctx);
	}
	
	@Override
	public Boolean visitValue_Power(Value_PowerContext ctx) {
		Boolean valid = visit(ctx.value(0)) && visit(ctx.value(1));

		if (valid) {
			Double op 	 = values.get(ctx.value(0));
			Double power = values.get(ctx.value(1));
			values.put(ctx, Math.pow(op, power));	
		}

		return valid;
	}
	
	@Override
	public Boolean visitValue_MultDiv(Value_MultDivContext ctx) {
		Boolean valid = visit(ctx.value(0)) && visit(ctx.value(1));

		if (valid) {
			Double op1 = values.get(ctx.value(0));
			Double op2 = values.get(ctx.value(1));
			Double result;
			if (ctx.op.getText().equals("*")) result = op1 * op2; 
			else result = op1 / op2;
			values.put(ctx, result);	
		}

		return valid;
	}
	
	@Override
	public Boolean visitValue_AddSub(Value_AddSubContext ctx) {
		Boolean valid = visit(ctx.value(0)) && visit(ctx.value(1));

		if (valid) {
			Double op1 = values.get(ctx.value(0));
			Double op2 = values.get(ctx.value(1));
			Double result;
			if (ctx.op.getText().equals("+")) result = op1 + op2; 
			else result = op1 - op2;
			values.put(ctx, result);	
		}

		return valid;
	}

	@Override
	public Boolean visitValue_Number(Value_NumberContext ctx) {
		try {
			Double result = Double.parseDouble(ctx.NUMBER().getText());
			values.put(ctx, result);
			return true;
		} catch (Exception e) {
			ErrorHandling.printError(ctx, "Value \"" + ctx.NUMBER().getText() + "\" is not a valid number!");
			return false;
		}
	}
	
	// --------------------------------------------------------------
	// Auxiliar Methods
	
	private String getStringText(String str) {
		str = str.substring(1, str.length()-1);
		return str;
	}
	
	private boolean isValidNewTypeName(String typeName, ParserRuleContext ctx) {
		// Semantic Analysis : Types can't be redefined
		if (typesTable.containsKey(typeName)) {
			ErrorHandling.printError(ctx, "Type \"" + typeName +"\" already defined!");
			return false;
		}
		
		// temp, number, boolean, string and void are reserved types
		String temp = typeName.toLowerCase();
		if (temp.equals("temp") || temp.equals("number") || temp.equals("boolean") || temp.equals("string") || temp.equals("void")) {
			ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is reserved and can't be defined!");
			return false;
		}
		
		return true;
	}
	
}
