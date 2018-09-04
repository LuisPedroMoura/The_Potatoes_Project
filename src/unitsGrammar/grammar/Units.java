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

import static java.lang.System.out;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import utils.errorHandling.ErrorHandling;
import utils.errorHandling.ErrorHandlingListener;

public class Units {
	
	private static final boolean debug = false;

	// Instance Fields
	private static Map<Integer, Unit>	basicUnitsCodesTable	= new HashMap<>();
	private static Map<String, Unit>	unitsTable				= new HashMap<>();
	private static Map<Unit, Map<Unit, Double>>	conversionTable	= new HashMap<>();
	private static List<String>			reservedWords			= new ArrayList<>();
	
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
			//System.out.println("PATH: " + path);
			File f = new File(path);
			//System.out.println("FILE: " + f.getAbsolutePath());
			fileStream = new FileInputStream(f);
			//out.println("Compiling \"" + f.getAbsolutePath() + "\"...");
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
			Units.basicUnitsCodesTable	= visitor0.getBasicUnitsCodesTable();
			Units.unitsTable			= visitor0.getAllUnits();
			Units.reservedWords			= visitor0.getReservedWords();
			Graph unitsGraph			= visitor0.getUnitsGraph();
			GraphInfo graphInfo			= new GraphInfo(unitsGraph);
			Units.conversionTable		= graphInfo.getAllMinJumpsPathCostsTable();
			
			// update conversion Table with Unit 'number' which cannot be put in the graph
			// (because it connects to everything and would allow conversion between all unrelated units)
			Map<Unit, Double> map = new HashMap<>();
			Unit number = new Unit("number", "", new Code(1));
			unitsTable.put("number", number);
			for (String key : unitsTable.keySet()) {
				map.put(unitsTable.get(key), 1.0);
				if (conversionTable.containsKey(unitsTable.get(key))) {
					conversionTable.get(unitsTable.get(key)).put(number, 1.0);
				}
			}
			conversionTable.put(number, map);
			
			if (debug) {
				System.out.println("####################################\n####################################\n");
				System.out.println("UNITS GRAPH\n");
				System.out.println(unitsGraph);
				System.out.println("####################################\n####################################\n");
				System.out.println("CONVERSION TABLE\n");
				for (Unit key : conversionTable.keySet()) {
					System.out.println("\n" + key + "->->->");
					for (Unit key2 : conversionTable.get(key).keySet()) {
						System.out.println("\t" + key2 + "->->" + conversionTable.get(key).get(key2));
					}
				}
				System.out.println("####################################\n####################################\n");
			}
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
	protected static Map<String, Unit> getUnitsTable() {
		return unitsTable;
	}
	
	/**
	 * @return the basicUnitsCodesTable
	 */
	protected  static Map<Integer, Unit> getBasicUnitsCodesTable() {
		return basicUnitsCodesTable;
	}

	/**
	 * @return the conversionTable
	 */
	protected static Map<Unit, Map<Unit, Double>> getConversionTable() {
		return conversionTable;
	}

	/**
	 * @return reservedWords, the list of all Unit names, prefixed names, symbols, and Class of Units names
	 */
	protected static List<String> getReservedWords(){
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
			return new Unit(getUnitsTable().get(name));
		}
		else if (reservedWords.contains(name)){
			for (String key : unitsTable.keySet()) {
				if (unitsTable.get(key).getSymbol().equals(name)) {
					return new Unit(unitsTable.get(key));
				}
			}
		}
		return null;
	}
	
	/**
	 * @param a String that is the name or symbol of the Unit
	 * @return an instance of Unit Class
	 */
	public static boolean exists(String name) {
		if (unitsTable.containsKey(name)) {
			return true;
		}
		else if (reservedWords.contains(name)){
			for (String key : unitsTable.keySet()) {
				if (unitsTable.get(key).getSymbol().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isReservedWord(String name) {
		return reservedWords.contains(name);
	}
	
	/**
	 * Addition of units implies that the two units are <b>equal<b> (equivalence is not enough)
	 * This method only guarantees that the two units are equal.
	 * @return new Unit equal to both arguments if they are equal.
	 * @throws IllegalArgumentException if the two Unit are not compatible
	 */
	public static Tuple add(Unit a, Unit b) throws IllegalArgumentException {
		double factor = Code.add(a.getCode(), b.getCode());
		return new Tuple(new Unit(a.getCode()), factor);
	}

	/**
	 * Subtraction of units implies that the two units are <b>equal<b> (equivalence is not enough)
	 * This method only guarantees that the two units are equal.
	 * @return new Unit equal to both arguments if they are equal.
	 * @throws IllegalArgumentException if the two Unit are not compatible
	 */
	public static Tuple subtract(Unit a, Unit b) {
		double factor = Code.subtract(a.getCode(), b.getCode());
		return new Tuple(new Unit(a.getCode()), factor);
	}
	
	/**
	 * @return new Unit with correspondent code resulting of the multiplication of two Units.
	 */
	public static Tuple multiply(Unit a, Unit b) {
		Code mult = Code.multiply(a.getCode(), b.getCode());
		double factor = mult.simplifyCodeWithConvertions(conversionTable, basicUnitsCodesTable);
		return new Tuple(new Unit(mult), factor);
	}

	/**
	 * @return new Unit with correspondent code resulting of the division of two Units.
	 */
	public static Tuple divide(Unit a, Unit b) {
		Code div = Code.divide(a.getCode(), b.getCode());
		double factor = div.simplifyCodeWithConvertions(conversionTable, basicUnitsCodesTable);
		return new Tuple(new Unit(div), factor);
	}
	
	/**
	 * @return new Unit with correspondent code resulting of the power of the Unit.
	 */
	public static Tuple power(Unit a, int exponent) {
		Code pow = Code.power(a.getCode(), exponent);
		double factor = pow.simplifyCodeWithConvertions(conversionTable, basicUnitsCodesTable);
		return new Tuple(new Unit(pow), factor);
	}
	
	// --------------------------------------------------------------------------
	// Private Methods
	
	


}
