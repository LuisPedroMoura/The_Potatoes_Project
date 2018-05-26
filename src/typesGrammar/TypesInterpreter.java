package typesGrammar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTreeProperty;

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
import utils.Prefix;
import utils.Type;
import utils.errorHandling.ErrorHandling;

/**
 * <b>TypesInterpreter</b><p>
 * 
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class TypesInterpreter extends TypesParserBaseVisitor<Boolean> {

	// SOLVED what to return? boolean? the map (i'll need it...)?

	// --------------------------------------------------------------------------
	// Instance Fields 
	private Map<String, Type>   typesTable 	  = new HashMap<>();
	private Map<String, Prefix> prefixesTable = new HashMap<>();	// TODO separeted or not? i have to think
	private ParseTreeProperty<Type>   types   = new ParseTreeProperty<>();
	private ParseTreeProperty<Double> values  = new ParseTreeProperty<>();

	// --------------------------------------------------------------------------
	// Getters
	public Map<String, Type> getTypesTable() {
		return typesTable;
	}

	public Map<String, Prefix> getPrefixesTable() {
		return prefixesTable;
	}

	// --------------------------------------------------------------------------
	// Callbacks 
	@Override
	public Boolean visitTypesFile(TypesFileContext ctx) {
		return visit(ctx.prefixDeclar()) && visit(ctx.typesDeclar());
	}

	// --------------------------------------------------------------
	// Types Callbacks 
	@Override
	public Boolean visitTypesDeclar(TypesDeclarContext ctx) {
		Boolean valid = true;
		List<TypeContext> typesDeclared = ctx.type(); 
		for (TypeContext type : typesDeclared) {
			valid = valid && visit(type);	// visit all declared types
		}	
		return valid;
	}

	@Override
	public Boolean visitTypeBasic(TypeBasicContext ctx) {
		// Rule ID STRING (COLON typeOp) 

		String typeName = ctx.ID().getText();

		// Semantic Analysis : Types can't be redefined
		if (typesTable.containsKey(typeName)) {
			ErrorHandling.printError(ctx, "Type \"" + typeName +"\" already defined!");
			return false;
		}

		// Create basic type
		Type t = new Type(typeName, ctx.STRING().getText().replaceAll("\"", ""));
		typesTable.put(typeName, t);
		return true;
	}

	@Override
	public Boolean visitTypeDerived(TypeDerivedContext ctx) {
		// Rule ID STRING (COLON typeOp) 						

		String typeName = ctx.ID().getText();

		// Semantic Analysis : Types can't be redefined
		if (typesTable.containsKey(typeName)) {
			ErrorHandling.printError(ctx, "Type \"" + typeName +"\" already defined!");
			return false;
		}

		// Create derived type based on typeOp
		Type t = new Type(typeName, ctx.STRING().getText().replaceAll("\"", ""), types.get(ctx.typeOp()).getCodes());

		typesTable.put(typeName, t);
		types.put(ctx, t);

		return true;
	}

	@Override
	public Boolean visitTypeDerivedOr(TypeDerivedOrContext ctx) {
		// Rule ID STRING (COLON typeOpOr) 							
		String typeName = ctx.ID().getText();

		// Semantic Analysis : Types can't be redefined
		if (typesTable.containsKey(typeName)) {
			ErrorHandling.printError(ctx, "Type \"" + typeName +"\" already defined!");
			return false;
		}

		// Create derived type based on typeOpOr
		Type t = new Type(typeName, ctx.STRING().getText().replaceAll("\"", ""), types.get(ctx.typeOpOr()).getCodes());

		typesTable.put(typeName, t);
		types.put(ctx, t);

		return true;
	}

	// Type Operations -------------------------------
	@Override
	public Boolean visitTypeOpOr(TypeOpOrContext ctx) {
		// Rule typeOpOrAlt (OR typeOpOrAlt)*	

		Boolean valid = true;
		List<TypeOpOrAltContext> orTypeAlternatives = ctx.typeOpOrAlt(); 
		for (TypeOpOrAltContext type : orTypeAlternatives) {
			valid = valid && visit(type);	// visit all declared types
		}	

		if (valid) {
			Map<Double, Double> codes = new HashMap<>();
			orTypeAlternatives.forEach(typeOp -> {
				codes.putAll(types.get(typeOp).getCodes());
			});
		}

		//return Type.or(visit(ctx.units_op(0)), visit(ctx.units_op(1)));
		return valid;

	}

	@Override
	public Boolean visitTypeOpOrAlt(TypeOpOrAltContext ctx) {
		// Rule valueOp ID
		// TODO

		// CALL Type.orCodes 
		// figure out how to use factors --> add an argument to the method orCodes?
		return super.visitTypeOpOrAlt(ctx);
	}

	@Override
	public Boolean visitTypeOpParenthesis(TypeOpParenthesisContext ctx) {
		// Rule PAR_OPEN typeOp PAR_CLOSE					

		Boolean valid = visit(ctx.typeOp());
		if (valid) {
			types.put(ctx, types.get(ctx.typeOp()));
		}

		return valid;
		//return visit(ctx.units_op()); 
	}

	@Override
	public Boolean visitTypeOpMultDiv(TypeOpMultDivContext ctx) {
		// Rule typeOp op=(MULTIPLY | DIVIDE) typeOp	
		// TODO

		/*
		Type a = visit(ctx.typeOp(0));
		Type b = visit(ctx.typeOp(1));
		if (!Type.isCompatible(a, b)) {
			ErrorHandling.printError(ctx, "Type \"" + a.getName() + "\" can't be multiplied/divided by type \"" + a.getName() + "\"!");
			return null;
		}

		if (ctx.op.getText().equals("*")) {
			return Type.multiply(visit(ctx.units_op(0)), visit(ctx.units_op(1)));
		}
		return Type.divide(visit(ctx.units_op(0)), visit(ctx.units_op(1)));
		 */

		return super.visitTypeOpMultDiv(ctx);
	}

	@Override
	public Boolean visitTypeOpPower(TypeOpPowerContext ctx) {
		// Rule <assoc=right> ID POWER NUMBER					
		// TODO

		return super.visitTypeOpPower(ctx);
	}

	@Override
	public Boolean visitTypeOpID(TypeOpIDContext ctx) {
		// Rule ID										

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
			valid = valid && visit(prefix);	// visit all declared prefixes
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
		Prefix t = new Prefix(prefixName, ctx.STRING().getText().replaceAll("\"", ""), values.get(ctx.valueOp()));
		prefixesTable.put(prefixName, t);
		return value;
	}

	// // --------------------------------------------------------------
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
