package typesGrammar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import typesGrammar.TypesParser.PrefixContext;
import typesGrammar.TypesParser.PrefixDeclarContext;
import typesGrammar.TypesParser.TypeBasicContext;
import typesGrammar.TypesParser.TypeContext;
import typesGrammar.TypesParser.TypeDerivedContext;
import typesGrammar.TypesParser.TypeDerivedOrContext;
import typesGrammar.TypesParser.TypeOpIDContext;
import typesGrammar.TypesParser.TypeOpMultDivContext;
import typesGrammar.TypesParser.TypeOpOrAltContext;
import typesGrammar.TypesParser.TypeOpOrContext;
import typesGrammar.TypesParser.TypeOpParenthesisContext;
import typesGrammar.TypesParser.TypeOpPowerContext;
import typesGrammar.TypesParser.TypesDeclarContext;
import typesGrammar.TypesParser.TypesFileContext;
import typesGrammar.TypesParser.ValueOpAddSubContext;
import typesGrammar.TypesParser.ValueOpMultDivContext;
import typesGrammar.TypesParser.ValueOpNumberContext;
import typesGrammar.TypesParser.ValueOpOpPowerContext;
import typesGrammar.TypesParser.ValueOpParenthesisContext;
import utils.Factor;
import utils.Prefix;
import utils.Type;
import utils.errorHandling.ErrorHandling;

/**
 * 
 * <b>TypesInterpreter</b><p>
 * 
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class TypesInterpreter extends TypesBaseVisitor<Boolean> {

	// Static Field (Debug Only)
	private static final boolean debug = false; 

	// --------------------------------------------------------------------------
	// Instance Fields 
	private Map<String, Type>    typesTable    = new HashMap<>();
	private Graph<Type, Factor>  typesGraph    = new DirectedSparseGraph<>();
	private Map<String, Prefix>  prefixesTable = new HashMap<>();	

	private ParseTreeProperty<Type>   types    = new ParseTreeProperty<>();
	private ParseTreeProperty<Double> values   = new ParseTreeProperty<>();

	// --------------------------------------------------------------------------
	// Getters
	public Map<String, Type> getTypesTable() {
		return typesTable;
	}

	public Graph<Type, Factor> getTypesGraph() {
		return typesGraph;
	}

	public Map<String, Prefix> getPrefixesTable() {
		return prefixesTable;
	}

	// --------------------------------------------------------------------------
	// Callbacks 
	@Override
	public Boolean visitTypesFile(TypesFileContext ctx) {
		// Rule NEW_LINE* prefixDeclar?  typesDeclar NEW_LINE*
		return ctx.prefixDeclar() != null ? visit(ctx.prefixDeclar()) && visit(ctx.typesDeclar()) 
				: visit(ctx.typesDeclar());
	}

	// --------------------------------------------------------------
	// Types Callbacks 
	@Override
	public Boolean visitTypesDeclar(TypesDeclarContext ctx) {
		// Rule ... (type NEW_LINE+)* ...

		Boolean valid = true;

		List<TypeContext> typesDeclared = ctx.type(); 
		for (TypeContext type : typesDeclared) {
			//if (debug) ErrorHandling.printInfo(ctx, "Processing type " + type.getText() + "...");
			valid = valid && visit(type);	// visit all declared types
			//System.out.println();
		}	

		return valid;
	}

	@Override
	public Boolean visitTypeBasic(TypeBasicContext ctx) {
		// Rule ID STRING

		String typeName = ctx.ID().getText();

		// Semantic Analysis : Types can't be redefined
		//if (debug) {
		//ErrorHandling.printInfo(ctx, "Type is basic.");
		//ErrorHandling.printWarning(ctx, "[PVT Debug] Types Table: " + typesTable);
		//}
		if (typesTable.containsKey(typeName)) {
			ErrorHandling.printError(ctx, "Type \"" + typeName +"\" already defined!");
			return false;
		}

		// Create basic type
		Type t = new Type(typeName, ctx.STRING().getText().replaceAll("\"", ""));
		typesTable.put(typeName, t);

		// [PVT Current Work] Update the types graph
		//typesGraph.addEdge(1.0, t, null);	// Edge to vertex base type (considered the base type equivalent to FIXME null for now)

		if (debug) ErrorHandling.printInfo(ctx, "Added Basic Type " + t + "\n\tOriginal line: " + ctx.getText() + ")\n");
		return true;
	}

	@Override
	public Boolean visitTypeDerived(TypeDerivedContext ctx) {
		// Rule ID STRING COLON typeOp 						

		String typeName = ctx.ID().getText();

		// Semantic Analysis : Types can't be redefined
		//if (debug) {
		//ErrorHandling.printInfo(ctx, "Type is derived.");

		Boolean valid = visit(ctx.typeOp());

		if (valid) {
			//ErrorHandling.printWarning(ctx, "[PVT Debug] Types Table: " + typesTable);
			//}
			if (typesTable.containsKey(typeName)) {
				ErrorHandling.printError(ctx, "Type \"" + typeName +"\" already defined!");
				return false;
			}

			// Create derived type based on typeOp
			Type t = new Type(typeName, ctx.STRING().getText().replaceAll("\"", ""), types.get(ctx.typeOp()).getCode());

			typesTable.put(typeName, t);
			types.put(ctx, t);

			if (debug) ErrorHandling.printInfo(ctx, "Added Derived Type " + t + "\n\tOriginal line: " + ctx.getText() + ")\n");
		}
		return valid;
	}

	@Override
	public Boolean visitTypeDerivedOr(TypeDerivedOrContext ctx) {
		// Rule ID STRING COLON typeOpOr							

		String typeName = ctx.ID().getText();

		// Temporary type (reference is needed for the visitor of rule typeOpOr)
		Type t = new Type(0.0);				
		t.setPrintName(ctx.STRING().getText().replaceAll("\"", ""));
		t.setTypeName(typeName);
		types.put(ctx, t);

		Boolean valid = visit(ctx.typeOpOr());

		if (valid) {
			// Semantic Analysis : Types can't be redefined
			if (typesTable.containsKey(typeName)) {
				ErrorHandling.printError(ctx, "Type \"" + typeName +"\" already defined!");
				return false;
			}


			// Create derived type based on typeOpOr
			//t = new Type(typeName, , types.get(ctx.typeOpOr()).getCode());

			// Update the types graph : done in each visitor for each alternative

			// Update the Types Table
			typesTable.put(typeName, t);

			if (debug) 
				ErrorHandling.printInfo(ctx, "Added Or Derived Type " + t + "\n\tOriginal line: " + ctx.getText() + ")\n");
		}

		return valid;
	}

	// Type Operations -------------------------------
	@Override
	public Boolean visitTypeOpOr(TypeOpOrContext ctx) {
		// Rule typeOpOrAlt (OR typeOpOrAlt)*	

		Boolean localDebug = true;
		if (localDebug) ErrorHandling.printInfo(ctx, "[PVT Debug] Parsing " + ctx.getText());

		Boolean valid = true;

		Type t = types.get(ctx.parent);

		List<TypeOpOrAltContext> orTypeAlternatives = ctx.typeOpOrAlt(); 
		for (TypeOpOrAltContext orAlt : orTypeAlternatives) {
			// visit all types declared in the Or 
			valid = valid && visit(orAlt);	

			if (valid) {
				// Create the Type with a new alternative
				Type alternative = types.get(orAlt);
				Double factor    = values.get(orAlt);
				t.addOpType(alternative);

				// Debug
				if (localDebug) {
					ErrorHandling.printInfo(ctx, "[PVT Debug]\tParent Type: " 			  + t);
					ErrorHandling.printInfo(ctx, "[PVT Debug]\tThis alternative Type: "   + alternative);
					ErrorHandling.printInfo(ctx, "[PVT Debug]\tThis alternative Factor: " + factor);
					ErrorHandling.printInfo(ctx, "[PVT Debug]\tGoing to update the graph with an edge " 
							+ factor + ", " + alternative + ", " + factor);
				}

				// [PVT Current Work] Update the types graph
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
	public Boolean visitTypeOpOrAlt(TypeOpOrAltContext ctx) {
		// Rule valueOp ID
		Boolean valid = visit(ctx.valueOp());

		String typeName = ctx.ID().getText();

		// Semantic Analysis : Verify it the type already exists
		if (!typesTable.containsKey(typeName)) {
			ErrorHandling.printError(ctx, "Type \"" + typeName + "\" does not exists!");
			return false;
		}

		types.put(ctx, typesTable.get(typeName));
		values.put(ctx, values.get(ctx.valueOp()));

		return valid;
	}

	@Override
	public Boolean visitTypeOpParenthesis(TypeOpParenthesisContext ctx) {
		// Rule PAR_OPEN typeOp PAR_CLOSE					

		Boolean valid = visit(ctx.typeOp());
		if (valid) {
			types.put(ctx, types.get(ctx.typeOp()));
		}

		return valid;
	}

	@Override
	public Boolean visitTypeOpMultDiv(TypeOpMultDivContext ctx) {
		// Rule typeOp op=(MULTIPLY | DIVIDE) typeOp	

		Boolean valid = visit(ctx.typeOp(0)) && visit(ctx.typeOp(1));

		Type a = types.get(ctx.typeOp(0));
		Type b = types.get(ctx.typeOp(1));

		types.put(ctx, Type.multiply(a, b));

		return valid;
	}

	@Override
	public Boolean visitTypeOpPower(TypeOpPowerContext ctx) {
		// Rule <assoc=right> ID POWER INT					
		String typeName = ctx.ID().getText();
		boolean localDebug = debug && false;
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
				t = Type.divide(t, t);
			}
		} 
		else {
			for (int i = 0; i < power - 1; i++) {
				t = Type.multiply(t, t);
			}
		}

		types.put(ctx, t);
		return true;
	}

	@Override
	public Boolean visitTypeOpID(TypeOpIDContext ctx) {
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
	public Boolean visitPrefixDeclar(PrefixDeclarContext ctx) {
		// Rule ... (prefix NEW_LINE+)*  ...

		Boolean valid = true;
		List<PrefixContext> prefixesDeclared = ctx.prefix(); 
		for (PrefixContext prefix : prefixesDeclared) {
			//if (debug) ErrorHandling.printInfo(ctx, "Processing prefix " + prefix.getText() + "...");			
			valid = valid && visit(prefix);	// visit all declared prefixes
			//System.out.println();
		}	
		return valid;
	}

	@Override
	public Boolean visitPrefix(PrefixContext ctx) {
		// Rule ID STRING COLON valueOp

		String prefixName = ctx.ID().getText();

		// Semantic Analysis 
		Boolean value = true;

		// Prefixes can't be redefined
		if (prefixesTable.containsKey(prefixName)) {
			ErrorHandling.printError(ctx, "Prefix \"" + prefixName +"\" already defined!");
			value = false;
		}

		value = value && visit(ctx.valueOp());

		// Create prefix
		Prefix p = new Prefix(prefixName, ctx.STRING().getText().replaceAll("\"", ""), values.get(ctx.valueOp()));
		prefixesTable.put(prefixName, p);

		if (debug) ErrorHandling.printInfo(ctx, "Added " + p + "\n\tOriginal line: " + ctx.getText() + "\n");
		return value;
	}

	// --------------------------------------------------------------
	// Value Callbacks 
	@Override
	public Boolean visitValueOpParenthesis(ValueOpParenthesisContext ctx) {
		Boolean valid = visit(ctx.valueOp());
		if (valid) {
			values.put(ctx, values.get(ctx.valueOp()));
		}
		return valid;
	}

	@Override
	public Boolean visitValueOpAddSub(ValueOpAddSubContext ctx) {
		Boolean valid = visit(ctx.valueOp(0)) && visit(ctx.valueOp(1));
		if (valid) {
			Double op1 = values.get(ctx.valueOp(0));
			Double op2 = values.get(ctx.valueOp(1));
			Double result;
			if (ctx.op.getText().equals("+")) result = op1 + op2; 
			else result = op1 - op2;
			values.put(ctx, result);	
		}
		return valid;
	}

	@Override
	public Boolean visitValueOpOpPower(ValueOpOpPowerContext ctx) {
		Boolean valid = visit(ctx.valueOp(0)) && visit(ctx.valueOp(1));
		if (valid) {
			Double op 	 = values.get(ctx.valueOp(0));
			Double power = values.get(ctx.valueOp(1));
			values.put(ctx, Math.pow(op, power));	
		}
		return valid;
	}

	@Override
	public Boolean visitValueOpMultDiv(ValueOpMultDivContext ctx) {
		Boolean valid = visit(ctx.valueOp(0)) && visit(ctx.valueOp(1));
		if (valid) {
			Double op1 = values.get(ctx.valueOp(0));
			Double op2 = values.get(ctx.valueOp(1));
			Double result;
			if (ctx.op.getText().equals("*")) result = op1 * op2; 
			else result = op1 / op2;
			values.put(ctx, result);	
		}
		return valid;
	}

	@Override
	public Boolean visitValueOpNumber(ValueOpNumberContext ctx) {
		try {
			Double result = Double.parseDouble(ctx.NUMBER().getText());
			values.put(ctx, result);
			return true;
		} catch (Exception e) {
			ErrorHandling.printError(ctx, "Value \"" + ctx.NUMBER().getText() + "\" is not a valid number!");
			return false;
		}
	}

}
