/***************************************************************************************
*	Title: PotatoesProject - TypesInterpreter Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Author of version 1.0: Pedro Teixeira (https://pedrovt.github.io),
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package typesGrammar.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import typesGrammar.grammar.TypesParser.*;
import typesGrammar.utils.Code;
import typesGrammar.utils.HierarchyDiGraph;
import typesGrammar.utils.Type;
import utils.errorHandling.ErrorHandling;

/* HOW TO
 * 
 * Unidades
 * Cada unidade é adicionada ao grafo com o respetivo caminho/ factor de conversao.
 * As dimensoes ou classes tambem sao adicionadas ao grafo como unidades que terao fator de conversao
 * 1 para a sua unidade base.
 * As estruturas tambem sao aicionadas ao grafo como unidades, mas terao de ter um tag que inidica
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
public class TypesInterpreter extends TypesBaseVisitor<Boolean> {

	// Static Field (Debug Only)
	private static final boolean debug = false; 

	// --------------------------------------------------------------------------
	// Instance Fields	
	private Map<String, Type>	typesTable    			= new HashMap<>();
	private Map<String, Type>	prefixedTypesTable		= new HashMap<>();
	private Map<String, Type>	classesTable			= new HashMap<>();
	private HierarchyDiGraph<Type, Double>	typesGraph	= new HierarchyDiGraph<>();
	//private Graph<Double, Type>	inheritanceGraph	= new Graph(); // may be needed to guarantee logic in dimension
	private List<String> 		reservedWords 			= new ArrayList<>();
	
	private ParseTreeProperty<Type>		types	= new ParseTreeProperty<>();
	private ParseTreeProperty<Double>	values	= new ParseTreeProperty<>();

	// --------------------------------------------------------------------------
	// Getters
	protected Map<String, Type> getTypesTable() {
		return typesTable;
	}
	
	protected Map<String, Type> getPrefixedTypesTable() {
		return prefixedTypesTable;
	}
	
	protected Map<String, Type> getClassesTable() {
		return classesTable;
	}

	protected HierarchyDiGraph<Type,Double> getTypesGraph() {
		return typesGraph;
	}

	protected List<String> getReservedWords(){
		return reservedWords;
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
			if (debug) ErrorHandling.printInfo(ctx, "--- Processing type " + type.getText() + "...");
			valid = visit(type);		// visit all declared types
			if (!valid) return false;
		}	

		return valid;
	}
	
	@Override
	public Boolean visitType_Basic(Type_BasicContext ctx) {
		
		String typeName = ctx.ID().getText();
		String printName = getStringText(ctx.STRING().getText());
		if (printName.equals("")) {
			ErrorHandling.printError(ctx, "Type declaration symbol cannot be empty");
			return false;
		}

		if(!isValidNewTypeName(typeName, ctx)) return false;

		// Create basic type with new auto Code
		Type t = new Type(typeName, printName);
		typesTable.put(typeName, t);
		reservedWords.add(typeName);
		reservedWords.add(printName);

		if (debug) {
			ErrorHandling.printInfo(ctx, "Added Basic Type " + t + "\n\tOriginal line: " + ctx.getText() + ")\n");
		}

		return true;
	}

	@Override
	public Boolean visitType_Derived(Type_DerivedContext ctx) {

		String typeName = ctx.ID().getText();
		String printName = getStringText(ctx.STRING().getText());
		if (printName.equals("")) {
			ErrorHandling.printError(ctx, "Type declaration symbol cannot be empty");
			return false;
		}
		
		// Type is already declared as a Basic Type
		if (!isValidNewTypeName(typeName, ctx)) {
			ErrorHandling.printError(ctx, "The Type \"" + typeName + "\" is already declared as Basic Type");
			return false;
		}
		
		// New Type is declared
		// visits typesDerivation to create the new Code for this Type
		Boolean valid = visit(ctx.typesDerivation());
		
		// Create derived type based on typesDerivation
		if (valid) {

			Code code = types.get(ctx.typesDerivation()).getCode();
			Type t = new Type(typeName, printName, new Code(code));
			// Update Types & Symbol Tables
			typesTable.put(typeName, t);
			types.put(ctx, t);
			reservedWords.add(typeName);
			reservedWords.add(printName);

			if (debug) {
				ErrorHandling.printInfo(ctx, "Added Derived Type " + t + "\n\tOriginal line: " + ctx.getText() + ")\n");
			}
		}
		
		return true;
	}

	@Override
	public Boolean visitType_Equivalent(Type_EquivalentContext ctx) {

		String typeName = ctx.ID().getText();
		String printName;
		if (ctx.STRING() != null) {
			printName = getStringText(ctx.STRING().getText());
			if (printName.equals("")) {
				ErrorHandling.printError(ctx, "Type declaration symbol cannot be empty");
				return false;
			}
		}
		else {
			printName = null;
		}
		
		Boolean validNewTypeName = isValidNewTypeName(typeName, ctx);
		
		// Type was already declared. A new Symbol is being declared.
		if (!validNewTypeName && !getStringText(ctx.STRING().getText()).equals(typesTable.get(typeName).getPrintName())) {
			ErrorHandling.printError(ctx, "The Type \"" + typeName + "\" is already declared with another symbol");
			return false;
		}
		
		// Type was already declared. The same Symbol is being re-declared.
		if (!validNewTypeName && !getStringText(ctx.STRING().getText()).equals(typesTable.get(typeName).getPrintName())) {
			ErrorHandling.printWarning(ctx, "The Type \"" + typeName + "\" is already declared. Symbol is not needed in assignments");
		}
		
		// New Type declared without Symbol
		if (validNewTypeName && ctx.STRING() == null) {
			ErrorHandling.printError(ctx, "A Symbol for this Type must be declared");
			return false;
		}
		
		// New Type declared correctly
		// Create new Type with its Code
		Type t = new Type(typeName, printName);
		
		Boolean valid = visit(ctx.typesEquivalence());

		if (valid) {
			// Add Types to the graph
			for (EquivalentTypeContext typeCtx : ctx.typesEquivalence().equivalentType()) {
				typesGraph.addEdge(values.get(typeCtx), t, types.get(typeCtx));
				typesGraph.addEdge(1/values.get(typeCtx), types.get(typeCtx), t);
			}

			if (debug) {
				ErrorHandling.printInfo(ctx, "Added Or Derived Type " + t + "\n\tOriginal line: " + ctx.getText() + ")\n");
			}
		}
		
		typesTable.put(typeName, t);
		types.put(ctx, t);
		reservedWords.add(typeName);
		reservedWords.add(printName);
		
		return true;
	}
	
	@Override
	public Boolean visitType_Class(Type_ClassContext ctx) {
		
		String className = ctx.ID(0).getText();
		String baseTypeName = ctx.ID(1).getText();
		
		Boolean validNewClassName = isValidNewTypeName(className, ctx);
		Boolean validTypeName = isValidNewTypeName(baseTypeName, ctx);
		
		// The Class of Types already exists.
		if (!validNewClassName) {
			ErrorHandling.printError(ctx, "The Class of Types \"" + className + "\" already exist");
			return false;
		}
		
		// The  Class Base Type is not declared.
		if (validTypeName) {
			ErrorHandling.printError(ctx, "The Base Type \"" + baseTypeName + "\" is not declared");
			return false;
		}
		
		// New Class declared correctly
		// Create new Type with its Code. The Symbol is the same as the Base Type
		Type t = new Type(className, typesTable.get(baseTypeName).getPrintName());
		t.setAsClass();
		
		// add the Class and Base Type to the Graph
		typesGraph.addEdge(1.0, t, typesTable.get(baseTypeName));
		typesGraph.addEdge(1.0, typesTable.get(baseTypeName), t);
		
		Boolean valid = visit(ctx.typesEquivalence());

		if (valid) {
			// Add Types to the graph
			for (EquivalentTypeContext typeCtx : ctx.typesEquivalence().equivalentType()) {
				typesGraph.addEdge(values.get(typeCtx), t, types.get(typeCtx));
				typesGraph.addEdge(1/values.get(typeCtx), types.get(typeCtx), t);
			}

			if (debug) {
				ErrorHandling.printInfo(ctx, "Added Or Derived Type " + t + "\n\tOriginal line: " + ctx.getText() + ")\n");
			}
		}
		
		classesTable.put(className, t); // goes to own table so its not prefixed later
		types.put(ctx, t);				// goes to types ParseTree because it is a Type.
		reservedWords.add(className);
		
		return true;
	}

	// Type Operations -------------------------------
	@Override
	public Boolean visitTypesEquivalence(TypesEquivalenceContext ctx) {
		Boolean valid = true;
		for (EquivalentTypeContext equivAlt : ctx.equivalentType()) {
			Boolean visit = visit(equivAlt);
			valid = valid && visit;
		}
		return valid;
	}
	
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

		if (visit(ctx.typesDerivation())){
			types.put(ctx, types.get(ctx.typesDerivation()));
			return true;
		}
		return false;
	}

	@Override
	public Boolean visitType_Op_MultDiv(Type_Op_MultDivContext ctx) {

		Boolean valid = visit(ctx.typesDerivation(0)) && visit(ctx.typesDerivation(1));

		if (valid) {

			Type a = types.get(ctx.typesDerivation(0));
			Type b = types.get(ctx.typesDerivation(1));

			Type res;
			if (ctx.op.getText().equals("*"))
				res = Type.multiply(a, b);
			else
				res = Type.divide(a, b);

			types.put(ctx, res);
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
	public Boolean visitTypesAssociation(TypesAssociationContext ctx) {
		// TODO Auto-generated method stub
		return super.visitTypesAssociation(ctx);
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
		if (prefixedTypesTable.containsKey(prefixName)) {
			ErrorHandling.printError(ctx, "Prefix \"" + prefixName +"\" already defined!");
			return false;
		}
		
		Boolean validValue = visit(ctx.value());
		
		if (validValue) {

			double value = values.get(ctx.value());
			
			if (value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY || value == 0.0) {
				ErrorHandling.printError(ctx, "Prefix \"" + prefixName +"\" value is not a valid value");
				return false;
			}

			// Create prefixed Types and add them to the Graph linked to all units
			for (String key : typesTable.keySet()) {
				Type t = typesTable.get(key);
				String prefixedName = prefixName + t.getTypeName();
				String prefixedSymbol = printName + t.getPrintName();
				Type prefix = new Type(prefixedName, prefixedSymbol);
				typesGraph.addEdge(value, t, prefix);
				typesGraph.addEdge(1/value, prefix, t);
				prefixedTypesTable.put(prefixedName, prefix);
				reservedWords.add(prefixedName);
				reservedWords.add(prefixedSymbol);
				
				if (debug) {
					ErrorHandling.printInfo(ctx, "Added " + prefix + "\n\tOriginal line: " + ctx.getText() + "\n");
				}
			}
		}
		
		return validValue;
	}
	
	// --------------------------------------------------------------
	// Value Callbacks 
	@Override
	public Boolean visitValue_Parenthesis(Value_ParenthesisContext ctx) {
		if (visit(ctx.value())) {
			values.put(ctx, values.get(ctx.value()));
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean visitValue_Simetric(Value_SimetricContext ctx) {
		if (visit(ctx.value())) {
			values.put(ctx, -values.get(ctx.value()));
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean visitValue_Power(Value_PowerContext ctx) {
		Boolean valid = visit(ctx.value(0)) && visit(ctx.value(1));

		if (valid) {
			Double base = values.get(ctx.value(0));
			Double power = values.get(ctx.value(1));
			
			Double res = Math.pow(base, power);
			if (res == Double.POSITIVE_INFINITY || res == Double.NEGATIVE_INFINITY) {
				ErrorHandling.printError(ctx, "Expression overflows");
				return false;
			}
			
			values.put(ctx, res);	
		}

		return valid;
	}
	
	@Override
	public Boolean visitValue_MultDiv(Value_MultDivContext ctx) {
		Boolean valid = visit(ctx.value(0)) && visit(ctx.value(1));

		if (valid) {
			Double op1 = values.get(ctx.value(0));
			Double op2 = values.get(ctx.value(1));
			Double res;
			if (ctx.op.getText().equals("*")) {
				res = op1 * op2; 
			}
			else res = op1 / op2;
			
			if (res == Double.POSITIVE_INFINITY || res == Double.NEGATIVE_INFINITY) {
				ErrorHandling.printError(ctx, "Expression overflows");
				return false;
			}
			
			values.put(ctx, res);	
		}

		return valid;
	}
	
	@Override
	public Boolean visitValue_AddSub(Value_AddSubContext ctx) {
		Boolean valid = visit(ctx.value(0)) && visit(ctx.value(1));

		if (valid) {
			Double op1 = values.get(ctx.value(0));
			Double op2 = values.get(ctx.value(1));
			Double res;
			if (ctx.op.getText().equals("+")) {
				res = op1 + op2;
			}
			else res = op1 - op2;
			
			if (res == Double.POSITIVE_INFINITY || res == Double.NEGATIVE_INFINITY) {
				ErrorHandling.printError(ctx, "Expression overflows");
				return false;
			}
			
			values.put(ctx, res);	
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
	
	private boolean isValidNewTypeName(String typeName, ParserRuleContext ctx) {
		// Semantic Analysis : Types can't be redefined
		if (typesTable.containsKey(typeName)) {
			ErrorHandling.printError(ctx, "Type \"" + typeName +"\" already defined");
			return false;
		}
		
		if (classesTable.containsKey(typeName)) {
			ErrorHandling.printError(ctx, "The name \"" + typeName +"\" is already defined as a Class of Types");
			return false;
		}
		
		// temp, number, boolean, string and void are reserved types
		String temp = typeName.toLowerCase();
		if (temp.equals("temp") || temp.equals("number") || temp.equals("boolean") || temp.equals("string") || temp.equals("void")) {
			ErrorHandling.printError(ctx, "Type \"" + typeName + "\" is reserved and can't be defined");
			return false;
		}
		
		return true;
	}
	
	private boolean typeExists(String typeName, ParserRuleContext ctx) {
		if (!typesTable.containsKey(typeName)) {
			ErrorHandling.printError(ctx, "Type \"" + typeName + "\" does not exists");
			return false;
		}
		return true;
	}
	
}
