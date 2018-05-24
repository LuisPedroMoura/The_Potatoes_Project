package typesGrammar;

import java.util.HashMap;
import java.util.Map;

import typesGrammar.TypesParser.FactorAddSubContext;
import typesGrammar.TypesParser.FactorMultDivContext;
import typesGrammar.TypesParser.FactorNUMBERContext;
import typesGrammar.TypesParser.FactorParenthesisContext;
import typesGrammar.TypesParser.TypeBasicContext;
import typesGrammar.TypesParser.TypeDerivedContext;
import typesGrammar.TypesParser.TypeOpIDContext;
import typesGrammar.TypesParser.TypeOpMultDivContext;
import typesGrammar.TypesParser.TypeOpNUMBERContext;
import typesGrammar.TypesParser.TypeOpOrContext;
import typesGrammar.TypesParser.TypeOpParenthesisContext;
import typesGrammar.TypesParser.TypeOpPowerContext;
import typesGrammar.TypesParser.TypesDeclarContext;
import typesGrammar.TypesParser.TypesFileContext;
import utils.Code;
import utils.Type;
import utils.errorHandling.ErrorHandling;

/**
 * <b>TypesInterpreter</b><p>
 * 
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class TypesInterpreter extends TypesParserBaseVisitor<Type> {
	// TODO what to return? boolean? the map (i'll need it...)?

	// Instance Fields -------------------------------------------------------
	private Map<String, Type> types = new HashMap<>();

	@Override
	public Type visitTypesFile(TypesFileContext ctx) {
		// TODO Auto-generated method stub
		return super.visitTypesFile(ctx);
	}

	// TODO Prefixes Callbacks -----------------------------------------------


	// Types Callbacks -------------------------------------------------------
	@Override
	public Type visitTypesDeclar(TypesDeclarContext ctx) {
		ctx.type().forEach(type -> visit(type));	// visit all declared types
		return null;
	}

	@Override
	public Type visitTypeBasic(TypeBasicContext ctx) {
		String typeName = ctx.ID().getText();
		Type t = new Type(typeName, ctx.STRING().getText());
		types.put(typeName, t);
		return t;
	}

	@Override
	public Type visitTypeDerived(TypeDerivedContext ctx) {
		String typeName = ctx.ID().getText();
		Type t = new Type(typeName, ctx.STRING().getText());
		types.put(typeName, t);
		return t;
	}

	@Override
	public Type visitTypeOpParenthesis(TypeOpParenthesisContext ctx) {
		return visit(ctx.typeOp());
	}

	@Override
	public Type visitTypeOpNUMBER(TypeOpNUMBERContext ctx) {
		// TODO Delete?
		return super.visitTypeOpNUMBER(ctx);
	}

	@Override
	public Type visitTypeOpPower(TypeOpPowerContext ctx) {
		// TODO Auto-generated method stub
		return super.visitTypeOpPower(ctx);
	}

	@Override
	public Type visitTypeOpOr(TypeOpOrContext ctx) {
		Code c = new Code();
		ctx.typeOp().forEach(typeOp -> {
			Type type = visit(typeOp);
			c.add(type.getCode());
		});
		return super.visitTypeOpOr(ctx);
	}

	@Override
	public Type visitTypeOpMultDiv(TypeOpMultDivContext ctx) {
		/*Type a = visit(ctx.typeOp(0));
		Type b = visit(ctx.typeOp(1));
		if (!Type.isCompatible(a, b)) {
			ErrorHandling.printError(ctx, "Type \"" + a.getName() + "\" can't be multiplied/divided by type \"" + a.getName() + "\"!");
			return null;
		}*/
		return super.visitTypeOpMultDiv(ctx);
	}

	@Override
	public Type visitTypeOpID(TypeOpIDContext ctx) {
		String typeName = ctx.ID().getText();

		// Verify it the types already exist
		if (!types.containsKey(typeName)) {
			ErrorHandling.printError(ctx, "Type \"" + typeName + "\" does not exists!");
			return null;
		}

		return types.get(typeName);
	}

	// TODO Factor Callbacks ------------------------------------------------
	@Override
	public Type visitFactorNUMBER(FactorNUMBERContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFactorNUMBER(ctx);
	}

	@Override
	public Type visitFactorParenthesis(FactorParenthesisContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFactorParenthesis(ctx);
	}

	@Override
	public Type visitFactorAddSub(FactorAddSubContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFactorAddSub(ctx);
	}

	@Override
	public Type visitFactorMultDiv(FactorMultDivContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFactorMultDiv(ctx);
	}
}
