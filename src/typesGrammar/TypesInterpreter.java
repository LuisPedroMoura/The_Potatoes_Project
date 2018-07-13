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
	
	//private Code newCode = null; // used as global variable to calculate new Code with multiple methods
	private Type newType = null; // used as global variable to calculate new Code with multiple methods

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

		// Create basic type with new auto Code
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
		
		// visits typesComposition to create the new Code for this Type
		newType = new Type(typeName, printName, new Code());
		Boolean valid = visit(ctx.typesComposition());
		
		// FIXME I think the adaptation below works. Sill needs to be tested
			
		// Create derived type based on typesComposition
		if (valid) {

			// Update Types & Symbol Tables
			typesTable.put(typeName, newType);
			types.put(ctx, newType);		

			if (debug) {
				ErrorHandling.printInfo(ctx, "Added Derived Type " + newType + "\n\tOriginal line: " + ctx.getText() + ")\n");
			}
		}
		return valid;
	}

	@Override
	public Boolean visitType_Equivalent(Type_EquivalentContext ctx) {

		String typeName = ctx.ID().getText();
		String printName = getStringText(ctx.STRING().getText());
		
		if(!isValidNewTypeName(typeName, ctx)) return false;
		
		// FIXME when will this Type be added to the graph??
		
		// Create new Type with its Code
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

		Boolean localDebug = debug && true;
		if (localDebug) {
			ErrorHandling.printInfo(ctx, "[PVT Debug] Parsing " + ctx.getText());
		}

		Boolean valid = true;
		Type parentType = types.get(ctx.parent);

		List<EquivalentTypeContext> equivTypeAlternatives = ctx.equivalentType(); 
		for (EquivalentTypeContext equivAlt : equivTypeAlternatives) {
			// visit all types declared in the equivalence 
			Boolean visit = visit(equivAlt);
			valid = valid && visit;	

			if (valid) {
				// Create the Type with a new alternative
				Type alternative = types.get(equivAlt);
				Double factor    = values.get(equivAlt);
				parentType.addOpType(alternative);

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

		types.put(ctx, parentType);

		return valid;
	}
	
	// FIXME I dont think this is really necessary, the grammar garantees value exists. check what false return means
	@Override
	public Boolean visitEquivalentType(EquivalentTypeContext ctx) {

		Boolean validFactor = visit(ctx.value());
		String typeName = ctx.ID().getText();
		
		// Verify that factor exists
		if (!validFactor) {
			ErrorHandling.printError(ctx, "Equivalent types must have a conversion factor");
			return false;
		}
		
		// Conversion factor must be different than zero
		if (Double.parseDouble(ctx.value().getText()) == 0.0) {
			ErrorHandling.printError(ctx, "Convertion factor has to be different than zero");
			return false;
		}
		
		// Verify that the type already exists (it must)
		if (!typesTable.containsKey(typeName)) {
			ErrorHandling.printError(ctx, "Type \"" + typeName + "\" does not exists!");
			return false;
		}

		types.put(ctx, typesTable.get(typeName));
		values.put(ctx, values.get(ctx.value()));
		
		return true;
	}
	
	// DONE
	@Override
	public Boolean visitType_Op_Parenthesis(Type_Op_ParenthesisContext ctx) {

		Boolean valid = visit(ctx.typesComposition());
		if (valid) {
			types.put(ctx, types.get(ctx.typesComposition()));
		}

		return valid;
	}

	@Override
	public Boolean visitType_Op_MultDiv(Type_Op_MultDivContext ctx) {

		Boolean valid1 = visit(ctx.typesComposition(0));
		Boolean valid2 = visit(ctx.typesComposition(1));
		Boolean valid  = valid1 && valid2;

		if (valid) {

			Type a = types.get(ctx.typesComposition(0));
			Type b = types.get(ctx.typesComposition(1));

			Type res;
			if (ctx.op.getText().equals("*"))
				res = Type.multiply(a, b);
			else
				res = Type.divide(a, b);

			types.put(ctx, res);
			// FIXME verify that some method above deal with temp type and converts to correct name
		}

		return valid;
	}

	@Override
	public Boolean visitType_Op_Power(Type_Op_PowerContext ctx) {			

		String typeName = ctx.ID().getText();

		// Type must exist
		if (!typeExists(typeName, ctx)) return false;

		// Create power of the type
		int power = 0;
		String numberToParse = ctx.NUMBER().getText();
		
		// power cannot be decimal
		if (numberToParse.contains(".")) {
			ErrorHandling.printError(ctx, "Value \"" + numberToParse + "\" is not a valid value for a power of a type!");
			return false;
		}
		
		// try parsing the exponent value
		try {
			power = Integer.parseInt(numberToParse);
		} catch (Exception e) {
			ErrorHandling.printError(ctx, "Value \"" + numberToParse + "\" is not a valid value for a power of a type!");
			return false;
		}
		
		// calculate the powered type
		Type t = typesTable.get(typeName);
		Type res = Type.power(t, power);
		
		types.put(ctx, res);
		return true;

	}

	@Override
	public Boolean visitType_Op_ID(Type_Op_IDContext ctx) {

		String typeName = ctx.ID().getText();

		// Type must exist
		if (!typeExists(typeName, ctx)) return false;

		types.put(ctx, typesTable.get(typeName));
		return true;
	}

	// --------------------------------------------------------------
	// Prefixes Callbacks
	@Override
	public Boolean visitPrefixDeclaration(PrefixDeclarationContext ctx) {

		Boolean valid = true;

		List<PrefixContext> prefixesDeclared = ctx.prefix(); 
		for (PrefixContext prefix : prefixesDeclared) {		
			valid = valid && visit(prefix);	// visit all declared prefixes
		}	
		return valid;
	}

	@Override
	public Boolean visitPrefix(PrefixContext ctx) {

		String prefixName = ctx.ID().getText();
		String printName = getStringText(ctx.STRING().getText());
		
		// Prefixes can't be redefined
		if (prefixesTable.containsKey(prefixName)) {
			ErrorHandling.printError(ctx, "Prefix \"" + prefixName +"\" already defined!");
			return false;
		}
		
		Boolean validValue = visit(ctx.value());
		
		if (validValue) {

			double value = values.get(ctx.value());

			// Create prefix
			Prefix p = new Prefix(prefixName, printName, value);
			prefixesTable.put(prefixName, p);

			if (debug) {
				ErrorHandling.printInfo(ctx, "Added " + p + "\n\tOriginal line: " + ctx.getText() + "\n");
			}
		}
		
		return validValue;
	}
	
	// TODO continue from here
	
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
	
	private static String getStringText(String str) {
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
	
	private boolean typeExists(String typeName, ParserRuleContext ctx) {
		if (!typesTable.containsKey(typeName)) {
			ErrorHandling.printError(ctx, "Type \"" + typeName + "\" does not exists!");
			return false;
		}
		return true;
	}
	
}
