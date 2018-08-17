/***************************************************************************************
*	Title: PotatoesProject - Units Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Author of version 1.0: Pedro Teixeira (https://pedrovt.github.io),
*	Date: August-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package unitsGrammar.grammar;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import unitsGrammar.utils.Code;
import unitsGrammar.utils.Graph;
import unitsGrammar.utils.GraphInfo;
import unitsGrammar.utils.Unit;
import utils.errorHandling.ErrorHandling;
import utils.errorHandling.ErrorHandlingListener;

public class Units {

	// Instance Fields
	private static Map<String, Unit>	unitsTable		= new HashMap<>();
	private static Graph				unitsGraph		= new Graph();
	private static List<String>			reservedWords	= new ArrayList<>();
	
	// --------------------------------------------------------------------------
	// CTOR
	
	/**
	 * Constructor
	 * @param path path to the Units file to be read
	 */
	@SuppressWarnings("resource")
	public Units(String path) {
		// create a stream from the file
		InputStream fileStream = null;

		// create a CharStream that reads from the file:		
		CharStream input = null;

		try {
			File f = new File(path);
			fileStream = new FileInputStream(f);
			input = CharStreams.fromStream(fileStream);
			fileStream.close();
		} catch(FileNotFoundException e) {
			ErrorHandling.printError("Units file could not be found! Please check if the file exists and can be read.");
			System.exit(1);
		} catch (IOException e) {
			ErrorHandling.printError("Internal error reading the Units file! Please check if the file exists and can be read.");
			System.exit(2);
		}

		// create a lexer that feeds off of input CharStream:
		UnitsLexer lexer = new UnitsLexer(input);

		// create a buffer of tokens pulled from the lexer:
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// create a parser that feeds off the tokens buffer:
		UnitsParser parser = new UnitsParser(tokens);

		// replace error listener:
		parser.removeErrorListeners(); // remove ConsoleErrorListener
		parser.addErrorListener(new ErrorHandlingListener());

		// begin parsing at UnitsFile rule:
		ParseTree tree = parser.unitsFile();

		if (parser.getNumberOfSyntaxErrors() == 0) {
			// print LISP-style tree:
			// System.out.println(tree.toStringTree(parser));
			UnitsInterpreter visitor0 = new UnitsInterpreter();

			if (!visitor0.visit(tree)) {
				System.exit(3);  
			}

			// Information to be transmitted to the Potatoes Semantic Checker
			Units.unitsTable		= visitor0.getAllUnits();
			Units.unitsGraph			= (new GraphInfo(visitor0.getUnitsGraph())).getStraightfowardPathsCostsGraph();
			Units.reservedWords		= visitor0.getReservedWords();
		}
		else {
			System.exit(3);
		}
	}

	// --------------------------------------------------------------------------
	// Getters
	
	/**
	 * @return	unitsTable, the table of Units defined in the file.
	 * 			Can be an empty table (if no Units were declared in the file).
	 */
	private static Map<String, Unit> getUnitsTable() {
		return unitsTable;
	}
	
	/**
	 * @return reservedWords, the list of all Unit names, prefixed names, symbols, and Class of Units names
	 */
	public static List<String> getReservedWords(){
		return reservedWords;
	}
	
	// --------------------------------------------------------------------------
	// Static Methods
	
	/**
	 * @param a String that is the name or symbol of the Unit
	 * @return an instance of Unit Class
	 */
	public static Unit instanceOf(String name) {
		if (unitsTable.containsKey(name)) {
			return getUnitsTable().get(name);
		}
		else if (reservedWords.contains(name)){
			for (String key : unitsTable.keySet()) {
				if (unitsTable.get(key).getSymbol().equals(name)) {
					return unitsTable.get(key);
				}
			}
		}
		return null;
	}
	
	/**
	 * @return new Unit with correspondent code resulting of the multiplication of two Units.
	 */
	public static Unit multiply(Unit a, Unit b) {
		// FIXME add verifications, try to come up with idea to give correct unit Name
		return tryToFindUnitNameAndSymbolWithoutCodeConversions(new Unit(Code.multiply(a.getCode(), b.getCode())));
	}
	
	/**
	 * @return new Unit with correspondent code resulting of the multiplication of two Units.
	 */
	public static Unit multiplySimplifyCode(Unit a, Unit b) {
		// FIXME add verifications, try to come up with idea to give correct unit Name
		return tryToFindUnitNameAndSymbolWithoutCodeConversions(new Unit(Code.multiply(a.getCode(), b.getCode())));
	}
	
	/**
	 * @return new Unit with correspondent code resulting of the multiplication of two Units.
	 */
	public static Unit multiplySimplifyCodeUsingGraph(Unit a, Unit b) {
		// FIXME add verifications, try to come up with idea to give correct unit Name
		return tryToFindUnitNameAndSymbolWithoutCodeConversions(new Unit(Code.multiply(a.getCode(), b.getCode())));
	}

	/**
	 * @return new Unit with correspondent code resulting of the division of two Units.
	 */
	public static Unit divide(Unit a, Unit b) {
		return tryToFindUnitNameAndSymbolWithoutCodeConversions(new Unit(Code.divide(a.getCode(), b.getCode())));
	}
	
	/**
	 * @return new Unit with correspondent code resulting of the power of the Unit.
	 */
	public static Unit power(Unit a, int exponent) {
		return tryToFindUnitNameAndSymbolWithoutCodeConversions(new Unit(Code.power(a.getCode(), exponent)));
	}
	
	// --------------------------------------------------------------------------
	// Private Methods
	
	// TODO is this function necessary, and is calling it for every operation a good idea?? Its not efficient
	private static Unit tryToFindUnitNameAndSymbolWithoutCodeConversions(Unit unit) {
		Code code = unit.getCode();
		for (String key : unitsTable.keySet()) {
			Unit value = unitsTable.get(key);
			if (value.getCode().equals(code)) {
				return value;
			}
		}
		return unit;
	}
	
	
	// --------------------------------------------------------------------------
	// Other Methods
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UnitsMain [");
		if (unitsTable != null) {
			builder.append("\n################################################################\nUnits Table: ");
			builder.append(unitsTable);
		}
		if (unitsGraph != null) {
			builder.append("\n################################################################\nUnits Graph: ");
			builder.append(unitsGraph);
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reservedWords == null) ? 0 : reservedWords.hashCode());
		result = prime * result + ((unitsGraph == null) ? 0 : unitsGraph.hashCode());
		result = prime * result + ((unitsTable == null) ? 0 : unitsTable.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Units other = (Units) obj;
		if (reservedWords == null) {
			if (other.reservedWords != null)
				return false;
		} else if (!reservedWords.equals(other.reservedWords))
			return false;
		if (unitsGraph == null) {
			if (other.unitsGraph != null)
				return false;
		} else if (!unitsGraph.equals(other.unitsGraph))
			return false;
		if (unitsTable == null) {
			if (other.unitsTable != null)
				return false;
		} else if (!unitsTable.equals(other.unitsTable))
			return false;
		return true;
	}

}
