/***************************************************************************************
*	Title: PotatoesProject - UnitsInterpreter Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Author of version 1.0: Pedro Teixeira (https://pedrovt.github.io),
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package unitsGrammar.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import unitsGrammar.grammar.UnitsParser.*;
import unitsGrammar.utils.*;
import utils.errorHandling.ErrorHandling;

/* HOW TO
 * 
 * Unidades
 * Cada unidade é adicionada ao grafo com o respetivo caminho/ factor de conversao.
 * As dimensoes ou classes tambem sao adicionadas ao grafo como unidades que terao fator de conversao
 * 1 para a sua unidade base.
 * As estruturas tambem sao adicionadas ao grafo como unidades, mas terao de ter um tag que inidica
 * que nao podem servir para construir caminhos de conversao.
 * O caminho a ser considerado par aas conversoes tem de ser de custo 1 para garantir a conversao
 * mais direta entre as declaradas.
 * 
 * Prefixos
 * possibilidade 1 - criar automaticamente todos os tipos que sao a combinao cao dos prefixos com todas as unidades
 * depois e so correr a arvore e reconhecer os simbolos ja existentes. mais processamente inicial, simplifica depois.
 * possibilidade 2 - criar uma tabela de prefixos e  ir lendo e convertendo a medida do possivel.
 * 
 * 
 * NO final de tudo, é possivel criar um grafo completo ou uma tabela de maneira a acelerar a compilacao
 * de programas mais complexos e exigentes a nivel das conversoes, para evitar estar constantemente a correr
 * o algoritmo de dijkstra. seria mais puxado no inicio, mas apenas uma vez.
 */
public class UnitsInterpreter extends UnitsBaseVisitor<Boolean> {

	// Static Field (Debug Only)
	private static final boolean debug = false; 

	// --------------------------------------------------------------------------
	// Instance Fields	
	private Map<String, Unit>	basicUnitsTable			= new HashMap<>();
	private Map<String, Unit>	unitsTable    			= new HashMap<>();
	private Map<String, Unit>	prefixedUnitsTable		= new HashMap<>();
	private Map<String, Unit>	classesTable			= new HashMap<>();
	
	private List<String> 		reservedWords 			= new ArrayList<>();
	private Graph				unitsGraph				= new Graph();
	
	private ParseTreeProperty<Unit>		unitsCtx	= new ParseTreeProperty<>();
	private ParseTreeProperty<Double>	valuesCtx	= new ParseTreeProperty<>();

	// --------------------------------------------------------------------------
	// Getters

	/**
	 * @return basicUnitsTable
	 */
	protected Map<String, Unit> getBasicUnitsTable() {
		return basicUnitsTable;
	}
	
	/**
	 * @return unitsTable
	 */
	protected Map<String, Unit> getUnitsTable() {
		return unitsTable;
	}
	
	/**
	 * @return prefixesUnitsTable
	 */
	protected Map<String, Unit> getPrefixedUnitsTable() {
		return prefixedUnitsTable;
	}
	
	/**
	 * @return classesTable
	 */
	protected Map<String, Unit> getClassesTable() {
		return classesTable;
	}
	
	/**
	 * @return unitsGraph
	 */
	protected Graph getUnitsGraph() {
		return unitsGraph;
	}
	
	/**
	 * @return reservedWords
	 */
	protected List<String> getReservedWords(){
		return reservedWords;
	}
	
	// --------------------------------------------------------------------------
	// Callbacks
	
	@Override
	public Boolean visitUnitsFile(UnitsFileContext ctx) {
		
		// add dimensionless Unit number
		unitsTable.put("number", new Unit("number", "", new Code(1)));
		
		Boolean valid = true;
		for (DeclarationContext dec : ctx.declaration()) {
			valid = visit(dec);
			if (!valid) return false;
		}
		
		return valid;
	}
	
	@Override
	public Boolean visitDeclaration(DeclarationContext ctx) {
		return visitChildren(ctx);
	}

	// --------------------------------------------------------------
	// Units Callbacks
	
	@Override
	public Boolean visitUnitsDeclaration(UnitsDeclarationContext ctx) {

		Boolean valid = true;

		List<UnitContext> UnitsDeclared = ctx.unit(); 
		for (UnitContext unit : UnitsDeclared) {
			if (debug) ErrorHandling.printInfo(ctx, "--- Processing unit " + unit.getText() + "...");
			valid = visit(unit);		// visit all declared Units
			if (!valid) return false;
		}	

		return valid;
	}
	
	@Override
	public Boolean visitUnit_Basic(Unit_BasicContext ctx) {
		
		String name = ctx.ID().getText();
		String symbol = getStringText(ctx.STRING().getText());

		if(!isValidNewUnitNameAndSymbol(name, symbol, ctx)) return false;

		// Create basic unit with new auto Code
		Unit u = new Unit(name, symbol);
		unitsTable.put(name, u);
		basicUnitsTable.put(name, u);
		reservedWords.add(name);
		reservedWords.add(symbol);
		unitsGraph.addVertex(u);
		unitsCtx.put(ctx, u);

		if (debug) {
			ErrorHandling.printInfo(ctx, "Added Basic Unit " + u + "\n\tOriginal line: " + ctx.getText() + ")\n");
		}

		return true;
	}

	@Override
	public Boolean visitUnit_Derived(Unit_DerivedContext ctx) {
		if (!visit(ctx.unitsDerivation())) {
			return false;
		}
		
		String name = ctx.ID().getText();
		String symbol = getStringText(ctx.STRING().getText());
		Unit unitToAssign = unitsCtx.get(ctx.unitsDerivation());
		
		if (!isValidNewUnitNameAndSymbol(name, symbol, ctx)) return false;
		
		// New Unit is declared
		
		// Create derived unit based on UnitsDerivation
		Code code = unitToAssign.getCode();
		Unit u = new Unit(name, symbol, new Code(code));
		// Update Units & Symbol Tables
		unitsTable.put(name, u);
		reservedWords.add(name);
		reservedWords.add(symbol);
		unitsGraph.addVertex(u);
		unitsCtx.put(ctx, u);
		

		if (debug) {
			ErrorHandling.printInfo(ctx, "Added Derived Unit " + u + "\n\tOriginal line: " + ctx.getText() + ")\n");
		}
		
		return true;
	}

	@Override
	public Boolean visitUnit_Equivalent(Unit_EquivalentContext ctx) {
		if (!visit(ctx.unitsEquivalence())) {
			return false;
		}
		
		// get Unit info
		String name = ctx.ID().getText();
		Unit u = unitsTable.get(name);
		
		// Unit must have been already created
		if (!reservedWords.contains(name)) {
			ErrorHandling.printError(ctx, "Unit '" + name + "' is not declared");
			return false;
		}
		
		// Add Units to the graph
		for (EquivalentUnitContext unitCtx : ctx.unitsEquivalence().equivalentUnit()) {
			unitsGraph.addEdge(valuesCtx.get(unitCtx), u, unitsCtx.get(unitCtx));
			unitsGraph.addEdge(1/valuesCtx.get(unitCtx), unitsCtx.get(unitCtx), u);
		}

		if (debug) {
			ErrorHandling.printInfo(ctx, "Added Or Derived Unit " + u + "\n\tOriginal line: " + ctx.getText() + ")\n");
		}
		
		return true;
	}
	
	@Override
	public Boolean visitUnit_Class(Unit_ClassContext ctx) {
		if (!visit(ctx.unitsEquivalence())) {
			return false;
		}
		
		String className = ctx.ID(0).getText();
		String baseUnitName = ctx.ID(1).getText();
		
		if (!unitsTable.containsKey(baseUnitName)) {
			ErrorHandling.printError(ctx, "Base Unit '" + baseUnitName + "' is not declared");
			return false;
		}
		
		if (!isValidNewUnitNameAndSymbol(className, null,  ctx)) return false;
		
		// New Class declared correctly
		// Create new Unit with its Code. The Symbol is the same as the Base Unit
		Unit u = new Unit(className, unitsTable.get(baseUnitName).getSymbol());
		u.setAsClass();
		
		// add the Class and Base Unit to the Graph
		unitsGraph.addEdge(1.0, u, unitsTable.get(baseUnitName));
		unitsGraph.addEdge(1.0, unitsTable.get(baseUnitName), u);
		
		// Add Units to the graph
		for (EquivalentUnitContext unitCtx : ctx.unitsEquivalence().equivalentUnit()) {
			unitsGraph.addEdge(valuesCtx.get(unitCtx), u, unitsCtx.get(unitCtx));
			unitsGraph.addEdge(1/valuesCtx.get(unitCtx), unitsCtx.get(unitCtx), u);
		}

		if (debug) {
			ErrorHandling.printInfo(ctx, "Added Or Derived Unit " + u + "\n\tOriginal line: " + ctx.getText() + ")\n");
		}

		classesTable.put(className, u); 	// goes to own table so its not prefixed later
		unitsCtx.put(ctx, u);				// goes to Units ParseTree because it is a Unit.
		reservedWords.add(className);
		
		return true;
	}

	// --------------------------------------------------------------
	// Unit Operations
	
	@Override
	public Boolean visitUnitsEquivalence(UnitsEquivalenceContext ctx) {
		Boolean valid = true;
		for (EquivalentUnitContext equivAlt : ctx.equivalentUnit()) {
			boolean visit = visit(equivAlt);
			valid = valid && visit;
		}
		return valid;
	}
	
	@Override
	public Boolean visitEquivalentUnit(EquivalentUnitContext ctx) {
		if (!visit(ctx.value()) ) {
			return false;
		}
		
		String unitName = ctx.ID().getText();
		
		// Conversion factor must be different than zero
		if (valuesCtx.get(ctx.value()) == 0.0) {
			ErrorHandling.printError(ctx, "Convertion factor has to be different than zero");
			return false;
		}
		
		// Verify that the unit already exists (it must)
		if (!unitsTable.containsKey(unitName)) {
			ErrorHandling.printError(ctx, "Unit \"" + unitName + "\" does not exists!");
			return false;
		}

		unitsCtx.put(ctx, unitsTable.get(unitName));
		valuesCtx.put(ctx, valuesCtx.get(ctx.value()));
		
		return true;
	}
	
	@Override
	public Boolean visitUnit_Op_Parenthesis(Unit_Op_ParenthesisContext ctx) {
		if (!visit(ctx.unitsDerivation())){
			return false;
		}
		
		unitsCtx.put(ctx, unitsCtx.get(ctx.unitsDerivation()));

		return true;
	}

	@Override
	public Boolean visitUnit_Op_MultDiv(Unit_Op_MultDivContext ctx) {
		if (!visit(ctx.unitsDerivation(0)) || !visit(ctx.unitsDerivation(1))) {
			return false;
		}

		Unit a = unitsCtx.get(ctx.unitsDerivation(0));
		Unit b = unitsCtx.get(ctx.unitsDerivation(1));

		Unit res;
		if (ctx.op.getText().equals("*"))
			res = Unit.multiply(a, b);
		else
			res = Unit.divide(a, b);

		unitsCtx.put(ctx, res);

		return true;
	}

	@Override
	public Boolean visitUnit_Op_Power(Unit_Op_PowerContext ctx) {
		if (!visit(ctx.unitsDerivation())) {
			return false;
		}

		Unit u = unitsCtx.get(ctx.unitsDerivation());
		int power = 0;
		
		// Create power of the unit
		String numberToParse = ctx.NUMBER().getText();
		
		// power cannot be decimal
		if (numberToParse.contains(".")) {
			ErrorHandling.printError(ctx, "Value \"" + numberToParse + "\" is not a valid value for a power of a unit!");
			return false;
		}
		
		// try parsing the exponent value
		try {
			power = Integer.parseInt(numberToParse);
		} catch (Exception e) {
			ErrorHandling.printError(ctx, "Value \"" + numberToParse + "\" is not a valid value for a power of a unit!");
			return false;
		}
		
		// calculate the powered unit
		Unit res = Unit.power(u, power);
		
		unitsCtx.put(ctx, res);
		return true;

	}

	@Override
	public Boolean visitUnit_Op_ID(Unit_Op_IDContext ctx) {

		String unitName = ctx.ID().getText();

		// Unit must exist
		if (!unitExists(unitName, ctx)) return false;

		unitsCtx.put(ctx, unitsTable.get(unitName));
		return true;
	}
	
	// --------------------------------------------------------------
	// Structures Callbacks
	
	@Override
	public Boolean visitStructureDeclaration(StructureDeclarationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStructureDeclaration(ctx);
	}

	@Override
	public Boolean visitStructure(StructureContext ctx) {
		// TODO Auto-generated method stub
		return super.visitStructure(ctx);
	}

	@Override
	public Boolean visitUnitsAssociation(UnitsAssociationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitUnitsAssociation(ctx);
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
		if (!visit(ctx.value())) {
			return false;
		}

		String prefixName = ctx.ID().getText();
		String prefixSymbol = getStringText(ctx.STRING().getText());
		double value = valuesCtx.get(ctx.value());
		
		if (!isValidNewUnitNameAndSymbol(prefixName, prefixSymbol,  ctx)) return false;
		
		if (value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY || value == 0.0) {
			ErrorHandling.printError(ctx, "Prefix \"" + prefixName +"\" value is not a valid value");
			return false;
		}

		// Create prefixed Units and add them to the Graph linked to all units
		for (String key : unitsTable.keySet()) {
			
			Unit u = unitsTable.get(key);
			String prefixedName = prefixName + u.getName();
			String prefixedSymbol = prefixSymbol + u.getSymbol();
			
			Unit prefix = new Unit(prefixedName, prefixedSymbol);
			
			unitsGraph.addEdge(value, u, prefix);
			unitsGraph.addEdge(1/value, prefix, u);
			prefixedUnitsTable.put(prefixedName, prefix);
			reservedWords.add(prefixedName);
			reservedWords.add(prefixedSymbol);
			
			if (debug) {
				ErrorHandling.printInfo(ctx, "Added " + prefix + "\n\tOriginal line: " + ctx.getText() + "\n");
			}
		}
		
		return true;
	}
	
	// --------------------------------------------------------------
	// Value Callbacks 
	
	@Override
	public Boolean visitValue_Parenthesis(Value_ParenthesisContext ctx) {
		if (!visit(ctx.value())) {
			return false;
		}
		
		valuesCtx.put(ctx, valuesCtx.get(ctx.value()));

		return true;
	}
	
	@Override
	public Boolean visitValue_Simetric(Value_SimetricContext ctx) {
		if (!visit(ctx.value())) {
			return false;
		}
		
		valuesCtx.put(ctx, -valuesCtx.get(ctx.value()));
		
		return true;
	}
	
	@Override
	public Boolean visitValue_Power(Value_PowerContext ctx) {
		if (!visit(ctx.value(0)) || !visit(ctx.value(1))) {
			return false;
		}

		Double base = valuesCtx.get(ctx.value(0));
		Double power = valuesCtx.get(ctx.value(1));
		Double res = Math.pow(base, power);
		
		if (res == Double.POSITIVE_INFINITY || res == Double.NEGATIVE_INFINITY) {
			ErrorHandling.printError(ctx, "Expression overflows");
			return false;
		}
		
		valuesCtx.put(ctx, res);	
	
		return true;
	}
	
	@Override
	public Boolean visitValue_MultDiv(Value_MultDivContext ctx) {
		if (!visit(ctx.value(0)) || !visit(ctx.value(1))) {
			return false;
		}

		Double op1 = valuesCtx.get(ctx.value(0));
		Double op2 = valuesCtx.get(ctx.value(1));
		Double res;
		if (ctx.op.getText().equals("*")) {
			res = op1 * op2; 
		}
		else {
			res = op1 / op2;
		}
		
		if (res == Double.POSITIVE_INFINITY || res == Double.NEGATIVE_INFINITY) {
			ErrorHandling.printError(ctx, "Expression overflows");
			return false;
		}
		
		valuesCtx.put(ctx, res);	

		return true;
	}
	
	@Override
	public Boolean visitValue_AddSub(Value_AddSubContext ctx) {
		if (!visit(ctx.value(0)) || !visit(ctx.value(1))) {
			return false;
		}

		Double op1 = valuesCtx.get(ctx.value(0));
		Double op2 = valuesCtx.get(ctx.value(1));
		Double res;
		if (ctx.op.getText().equals("+")) {
			res = op1 + op2;
		}
		else {
			res = op1 - op2;
		}
		
		if (res == Double.POSITIVE_INFINITY || res == Double.NEGATIVE_INFINITY) {
			ErrorHandling.printError(ctx, "Expression overflows");
			return false;
		}
		
		valuesCtx.put(ctx, res);	

		return true;
	}

	@Override
	public Boolean visitValue_Number(Value_NumberContext ctx) {
		
		try {
			Double number = Double.parseDouble(ctx.NUMBER().getText());
			valuesCtx.put(ctx, number);
			return true;
		} catch (NumberFormatException e) {
			ErrorHandling.printError(ctx, "Value \"" + ctx.NUMBER().getText() + "\" is not a valid number");
			return false;
		}
	}
	
	// --------------------------------------------------------------
	// Auxiliar Methods
	
	private static String getStringText(String str) {
		str = str.substring(1, str.length()-1);
		return str;
	}
	
	/**
	 * Auxiliar private method
	 * Verifies is <b>name<b> and <b>symbol<b> exist
	 * To check only ofr <b>name<b> make <b>symbol = null<b> 
	 * @param name
	 * @param symbol
	 * @param ctx
	 * @return true if both name and symbol are valid
	 */
	private boolean isValidNewUnitNameAndSymbol(String name, String symbol, ParserRuleContext ctx) {
		// Semantic Analysis : Units can't be redefined
		
		if (reservedWords.contains(name)) {
			ErrorHandling.printError(ctx, "Unit '" + name +"' already defined");
			return false;
		}
		
		name = name.toLowerCase();
		
		if (name.equals("temp") || name.equals("number")) {
			ErrorHandling.printError(ctx, "Unit name '" + name + "' is a reserved word and cannot be defined");
			return false;
		}
		
		
		if (symbol != null) {
			if (reservedWords.contains(symbol)) {
				ErrorHandling.printError(ctx, "Symbol '" + symbol + "' already defined");
				return false;
			}
			
			if (symbol.equals("")) {
				ErrorHandling.printError(ctx, "Unit symbol cannot be empty");
				return false;
			}
			
			symbol = symbol.toLowerCase();
			
			if (symbol.equals("temp") || symbol.equals("number")) {
				ErrorHandling.printError(ctx, "Symbol '" + symbol + "' is a reserved word and cannot be defined");
				return false;
			}
		}

		return true;
	}
	
	private boolean unitExists(String unitName, ParserRuleContext ctx) {
		if (!unitsTable.containsKey(unitName)) {
			ErrorHandling.printError(ctx, "Unit \"" + unitName + "\" does not exists");
			return false;
		}
		return true;
	}
	
}
