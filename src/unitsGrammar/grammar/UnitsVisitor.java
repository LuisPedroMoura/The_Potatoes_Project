// Generated from Units.g4 by ANTLR 4.7.1

package unitsGrammar.grammar;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link UnitsParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface UnitsVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link UnitsParser#unitsFile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitsFile(UnitsParser.UnitsFileContext ctx);
	/**
	 * Visit a parse tree produced by {@link UnitsParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(UnitsParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link UnitsParser#unitsDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitsDeclaration(UnitsParser.UnitsDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link UnitsParser#defineDimensionless}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefineDimensionless(UnitsParser.DefineDimensionlessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Unit_Basic}
	 * labeled alternative in {@link UnitsParser#unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit_Basic(UnitsParser.Unit_BasicContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Unit_Derived}
	 * labeled alternative in {@link UnitsParser#unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit_Derived(UnitsParser.Unit_DerivedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Unit_Equivalent}
	 * labeled alternative in {@link UnitsParser#unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit_Equivalent(UnitsParser.Unit_EquivalentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Unit_Class}
	 * labeled alternative in {@link UnitsParser#unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit_Class(UnitsParser.Unit_ClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link UnitsParser#unitsEquivalence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitsEquivalence(UnitsParser.UnitsEquivalenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link UnitsParser#equivalentUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquivalentUnit(UnitsParser.EquivalentUnitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Unit_Op_Parenthesis}
	 * labeled alternative in {@link UnitsParser#unitsDerivation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit_Op_Parenthesis(UnitsParser.Unit_Op_ParenthesisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Unit_Op_MultDiv}
	 * labeled alternative in {@link UnitsParser#unitsDerivation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit_Op_MultDiv(UnitsParser.Unit_Op_MultDivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Unit_Op_ID}
	 * labeled alternative in {@link UnitsParser#unitsDerivation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit_Op_ID(UnitsParser.Unit_Op_IDContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Unit_Op_Power}
	 * labeled alternative in {@link UnitsParser#unitsDerivation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit_Op_Power(UnitsParser.Unit_Op_PowerContext ctx);
	/**
	 * Visit a parse tree produced by {@link UnitsParser#structureDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructureDeclaration(UnitsParser.StructureDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link UnitsParser#structure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructure(UnitsParser.StructureContext ctx);
	/**
	 * Visit a parse tree produced by {@link UnitsParser#unitsAssociation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitsAssociation(UnitsParser.UnitsAssociationContext ctx);
	/**
	 * Visit a parse tree produced by {@link UnitsParser#prefixDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixDeclaration(UnitsParser.PrefixDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link UnitsParser#prefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefix(UnitsParser.PrefixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Value_AddSub}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_AddSub(UnitsParser.Value_AddSubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Value_Simetric}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_Simetric(UnitsParser.Value_SimetricContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Value_Power}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_Power(UnitsParser.Value_PowerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Value_Number}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_Number(UnitsParser.Value_NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Value_Parenthesis}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_Parenthesis(UnitsParser.Value_ParenthesisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Value_MultDiv}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_MultDiv(UnitsParser.Value_MultDivContext ctx);
}