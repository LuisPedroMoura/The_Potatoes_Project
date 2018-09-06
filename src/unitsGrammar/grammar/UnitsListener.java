// Generated from Units.g4 by ANTLR 4.7.1

package unitsGrammar.grammar;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link UnitsParser}.
 */
public interface UnitsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link UnitsParser#unitsFile}.
	 * @param ctx the parse tree
	 */
	void enterUnitsFile(UnitsParser.UnitsFileContext ctx);
	/**
	 * Exit a parse tree produced by {@link UnitsParser#unitsFile}.
	 * @param ctx the parse tree
	 */
	void exitUnitsFile(UnitsParser.UnitsFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link UnitsParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(UnitsParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link UnitsParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(UnitsParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link UnitsParser#unitsDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterUnitsDeclaration(UnitsParser.UnitsDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link UnitsParser#unitsDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitUnitsDeclaration(UnitsParser.UnitsDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link UnitsParser#defineDimensionless}.
	 * @param ctx the parse tree
	 */
	void enterDefineDimensionless(UnitsParser.DefineDimensionlessContext ctx);
	/**
	 * Exit a parse tree produced by {@link UnitsParser#defineDimensionless}.
	 * @param ctx the parse tree
	 */
	void exitDefineDimensionless(UnitsParser.DefineDimensionlessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Unit_Basic}
	 * labeled alternative in {@link UnitsParser#unit}.
	 * @param ctx the parse tree
	 */
	void enterUnit_Basic(UnitsParser.Unit_BasicContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Unit_Basic}
	 * labeled alternative in {@link UnitsParser#unit}.
	 * @param ctx the parse tree
	 */
	void exitUnit_Basic(UnitsParser.Unit_BasicContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Unit_Derived}
	 * labeled alternative in {@link UnitsParser#unit}.
	 * @param ctx the parse tree
	 */
	void enterUnit_Derived(UnitsParser.Unit_DerivedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Unit_Derived}
	 * labeled alternative in {@link UnitsParser#unit}.
	 * @param ctx the parse tree
	 */
	void exitUnit_Derived(UnitsParser.Unit_DerivedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Unit_Equivalent}
	 * labeled alternative in {@link UnitsParser#unit}.
	 * @param ctx the parse tree
	 */
	void enterUnit_Equivalent(UnitsParser.Unit_EquivalentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Unit_Equivalent}
	 * labeled alternative in {@link UnitsParser#unit}.
	 * @param ctx the parse tree
	 */
	void exitUnit_Equivalent(UnitsParser.Unit_EquivalentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Unit_Class}
	 * labeled alternative in {@link UnitsParser#unit}.
	 * @param ctx the parse tree
	 */
	void enterUnit_Class(UnitsParser.Unit_ClassContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Unit_Class}
	 * labeled alternative in {@link UnitsParser#unit}.
	 * @param ctx the parse tree
	 */
	void exitUnit_Class(UnitsParser.Unit_ClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link UnitsParser#unitsEquivalence}.
	 * @param ctx the parse tree
	 */
	void enterUnitsEquivalence(UnitsParser.UnitsEquivalenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link UnitsParser#unitsEquivalence}.
	 * @param ctx the parse tree
	 */
	void exitUnitsEquivalence(UnitsParser.UnitsEquivalenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link UnitsParser#equivalentUnit}.
	 * @param ctx the parse tree
	 */
	void enterEquivalentUnit(UnitsParser.EquivalentUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link UnitsParser#equivalentUnit}.
	 * @param ctx the parse tree
	 */
	void exitEquivalentUnit(UnitsParser.EquivalentUnitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Unit_Op_Parenthesis}
	 * labeled alternative in {@link UnitsParser#unitsDerivation}.
	 * @param ctx the parse tree
	 */
	void enterUnit_Op_Parenthesis(UnitsParser.Unit_Op_ParenthesisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Unit_Op_Parenthesis}
	 * labeled alternative in {@link UnitsParser#unitsDerivation}.
	 * @param ctx the parse tree
	 */
	void exitUnit_Op_Parenthesis(UnitsParser.Unit_Op_ParenthesisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Unit_Op_MultDiv}
	 * labeled alternative in {@link UnitsParser#unitsDerivation}.
	 * @param ctx the parse tree
	 */
	void enterUnit_Op_MultDiv(UnitsParser.Unit_Op_MultDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Unit_Op_MultDiv}
	 * labeled alternative in {@link UnitsParser#unitsDerivation}.
	 * @param ctx the parse tree
	 */
	void exitUnit_Op_MultDiv(UnitsParser.Unit_Op_MultDivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Unit_Op_ID}
	 * labeled alternative in {@link UnitsParser#unitsDerivation}.
	 * @param ctx the parse tree
	 */
	void enterUnit_Op_ID(UnitsParser.Unit_Op_IDContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Unit_Op_ID}
	 * labeled alternative in {@link UnitsParser#unitsDerivation}.
	 * @param ctx the parse tree
	 */
	void exitUnit_Op_ID(UnitsParser.Unit_Op_IDContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Unit_Op_Power}
	 * labeled alternative in {@link UnitsParser#unitsDerivation}.
	 * @param ctx the parse tree
	 */
	void enterUnit_Op_Power(UnitsParser.Unit_Op_PowerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Unit_Op_Power}
	 * labeled alternative in {@link UnitsParser#unitsDerivation}.
	 * @param ctx the parse tree
	 */
	void exitUnit_Op_Power(UnitsParser.Unit_Op_PowerContext ctx);
	/**
	 * Enter a parse tree produced by {@link UnitsParser#structureDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterStructureDeclaration(UnitsParser.StructureDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link UnitsParser#structureDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitStructureDeclaration(UnitsParser.StructureDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link UnitsParser#structure}.
	 * @param ctx the parse tree
	 */
	void enterStructure(UnitsParser.StructureContext ctx);
	/**
	 * Exit a parse tree produced by {@link UnitsParser#structure}.
	 * @param ctx the parse tree
	 */
	void exitStructure(UnitsParser.StructureContext ctx);
	/**
	 * Enter a parse tree produced by {@link UnitsParser#unitsAssociation}.
	 * @param ctx the parse tree
	 */
	void enterUnitsAssociation(UnitsParser.UnitsAssociationContext ctx);
	/**
	 * Exit a parse tree produced by {@link UnitsParser#unitsAssociation}.
	 * @param ctx the parse tree
	 */
	void exitUnitsAssociation(UnitsParser.UnitsAssociationContext ctx);
	/**
	 * Enter a parse tree produced by {@link UnitsParser#prefixDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterPrefixDeclaration(UnitsParser.PrefixDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link UnitsParser#prefixDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitPrefixDeclaration(UnitsParser.PrefixDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link UnitsParser#prefix}.
	 * @param ctx the parse tree
	 */
	void enterPrefix(UnitsParser.PrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link UnitsParser#prefix}.
	 * @param ctx the parse tree
	 */
	void exitPrefix(UnitsParser.PrefixContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Value_AddSub}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue_AddSub(UnitsParser.Value_AddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Value_AddSub}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue_AddSub(UnitsParser.Value_AddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Value_Simetric}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue_Simetric(UnitsParser.Value_SimetricContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Value_Simetric}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue_Simetric(UnitsParser.Value_SimetricContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Value_Power}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue_Power(UnitsParser.Value_PowerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Value_Power}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue_Power(UnitsParser.Value_PowerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Value_Number}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue_Number(UnitsParser.Value_NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Value_Number}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue_Number(UnitsParser.Value_NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Value_Parenthesis}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue_Parenthesis(UnitsParser.Value_ParenthesisContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Value_Parenthesis}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue_Parenthesis(UnitsParser.Value_ParenthesisContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Value_MultDiv}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue_MultDiv(UnitsParser.Value_MultDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Value_MultDiv}
	 * labeled alternative in {@link UnitsParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue_MultDiv(UnitsParser.Value_MultDivContext ctx);
}