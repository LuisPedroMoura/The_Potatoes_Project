package unitsGrammar;

import java.util.HashMap;
import java.util.Map;

import unitsGrammar.UnitsParser.Unit_basic_with_idContext;
import unitsGrammar.UnitsParser.Unit_basic_without_idContext;
import utils.Type;

/**
 * 
 * <b>UnitsImport</b><p>
 * 
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */

public class UnitsImport extends UnitsParserBaseVisitor<Type> {

	private Map<String, Type> types = new HashMap<>();


	@Override public Type visitProgram(UnitsParser.ProgramContext ctx) { 
		return visitChildren(ctx); 
	}

	// Constants -----------------------------------------------------------------------
	// TODO
	@Override public Type visitConst_declaration(UnitsParser.Const_declarationContext ctx) { 

		return visitChildren(ctx); 
	}

	// TODO
	@Override public Type visitConstant_with_id(UnitsParser.Constant_with_idContext ctx) { 
		return visitChildren(ctx); 
	}

	// TODO
	@Override public Type visitConstant_without_id(UnitsParser.Constant_without_idContext ctx) { 
		return visitChildren(ctx); 
	}

	// TODO
	@Override public Type visitConst_op_NUMBER(UnitsParser.Const_op_NUMBERContext ctx) { 
		return visitChildren(ctx); 
	}

	// TODO
	@Override public Type visitConst_op_parenthesis(UnitsParser.Const_op_parenthesisContext ctx) { 
		return visitChildren(ctx); 
	}

	// TODO
	@Override public Type visitConst_op_add_sub(UnitsParser.Const_op_add_subContext ctx) { 
		return visitChildren(ctx); 
	}

	// TODO
	@Override public Type visitConst_op_ID(UnitsParser.Const_op_IDContext ctx) { 
		return visitChildren(ctx); 
	}

	// TODO
	@Override public Type visitConst_op_power(UnitsParser.Const_op_powerContext ctx) { 
		return visitChildren(ctx); 
	}

	// TODO
	@Override public Type visitConst_op_mult_div(UnitsParser.Const_op_mult_divContext ctx) { 
		return visitChildren(ctx); 
	}

	// Units -----------------------------------------------------------------------
	@Override public Type visitUnits_declaration(UnitsParser.Units_declarationContext ctx) { 
		ctx.unit().forEach(unit -> visit(unit));
		return visitChildren(ctx); 
	}

	@Override
	public Type visitUnit_basic_with_id(Unit_basic_with_idContext ctx) {
		Type t = new Type(ctx.ID(0).getText(), ctx.ID(1).getText());
		types.put(ctx.ID(0).getText(), t);
		return t;
	}

	@Override
	public Type visitUnit_basic_without_id(Unit_basic_without_idContext ctx) {
		Type t = new Type(ctx.ID().getText());
		types.put(ctx.ID().getText(), t);
		return t;
	}

	@Override public Type visitUnit_with_id(UnitsParser.Unit_with_idContext ctx) { 
		Type t = new Type(ctx.ID(0).getText(), ctx.ID(1).getText(), visit(ctx.units_op()).getCode());
		types.put(ctx.ID(0).getText(), t);
		return t;
	}

	@Override public Type visitUnit_without_id(UnitsParser.Unit_without_idContext ctx) { 
		Type t = new Type(ctx.ID().getText(), visit(ctx.units_op()).getCode());
		types.put(ctx.ID().getText(), t);
		return t;
	}

	// TODO
	@Override public Type visitUnits_op_power(UnitsParser.Units_op_powerContext ctx) 	{ 
		return visitChildren(ctx); 
	}

	@Override public Type visitUnits_op_or(UnitsParser.Units_op_orContext ctx) { 
		return Type.or(visit(ctx.units_op(0)), visit(ctx.units_op(1)));
	}

	@Override public Type visitUnits_op_mult_div(UnitsParser.Units_op_mult_divContext ctx) { 
		if (ctx.op.getText().equals("*")) {
			return Type.multiply(visit(ctx.units_op(0)), visit(ctx.units_op(1)));
		}
		return Type.divide(visit(ctx.units_op(0)), visit(ctx.units_op(1)));
	}

	@Override public Type visitUnits_op_parenthesis(UnitsParser.Units_op_parenthesisContext ctx) { 
		return visit(ctx.units_op()); 
	}

	// TODO
	@Override public Type visitUnits_op_ID(UnitsParser.Units_op_IDContext ctx) { 
		return visitChildren(ctx); 
	}

	// TODO
	@Override public Type visitUnits_op_NUMBER(UnitsParser.Units_op_NUMBERContext ctx) { 
		return visitChildren(ctx); 
	}



}