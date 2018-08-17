/***************************************************************************************
*	Title: PotatoesProject - UnitsFileInfo Source Code
*	Code version: 1.0
*	Author: Pedro Teixeira (https://pedrovt.github.io)
*	Date: June-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package unitsGrammar.grammar;

import java.io.*;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import unitsGrammar.utils.Graph;
import unitsGrammar.utils.GraphInfo;
import unitsGrammar.utils.Unit;
import utils.errorHandling.ErrorHandling;
import utils.errorHandling.ErrorHandlingListener;

public class Units {

	// Instance Fields
	private final Map<String, Unit>		unitsTable;
	private final Map<String, Unit>		prefixedunitsTable;
	private final Map<String, Unit>		classesTable;
	private final Graph					UnitsGraph;
	private final List<String>			reservedWords;
	private final GraphInfo				graphInfo;
	
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
			this.unitsTable			= visitor0.getUnitsTable();
			this.prefixedunitsTable = visitor0.getPrefixedUnitsTable();
			this.classesTable		= visitor0.getClassesTable();
			this.UnitsGraph			= visitor0.getUnitsGraph();
			this.reservedWords		= visitor0.getReservedWords();
			this.graphInfo			= new GraphInfo(UnitsGraph);
		}
		else {
			// this code should be unreachable but is needed 
			// since the fields are final
			this.unitsTable			= null;
			this.prefixedunitsTable = null;
			this.classesTable		= null;
			this.UnitsGraph			= null;
			this.reservedWords		= null;
			this.graphInfo			= null;
			System.exit(3);
		}
	}

	// --------------------------------------------------------------------------
	// Getters
	/**
	 * Returns the table of Units defined in the file. 
	 * Can be an empty table (if no Units were declared in the file).
	 * @return unitsTable
	 */
	public Map<String, Unit> getunitsTable() {
		return unitsTable;
	}
	
	/**
	 * Returns the table of prefixes defined in the file. 
	 * Can be an empty table (if no prefixes were declared in the file).
	 * @return prefixesTable
	 */
	public Map<String,Unit> getPrefixesTable() {
		return prefixedunitsTable;
	}

	/**
	 * @return the classesTable
	 */
	public Map<String, Unit> getClassesTable() {
		return classesTable;
	}

	
	
	public List<String> getReservedWords(){
		return reservedWords;
	}

	/**
	 * @return the graphInfo
	 */
	public GraphInfo getGraphInfo() {
		return graphInfo;
	}

	// --------------------------------------------------------------------------
	// Other Methods
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prefixedunitsTable == null) ? 0 : prefixedunitsTable.hashCode());
		result = prime * result + ((unitsTable == null) ? 0 : unitsTable.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Units other = (Units) obj;
		if (prefixedunitsTable == null) {
			if (other.prefixedunitsTable != null) {
				return false;
			}
		} else if (!prefixedunitsTable.equals(other.prefixedunitsTable)) {
			return false;
		}
		if (unitsTable == null) {
			if (other.unitsTable != null) {
				return false;
			}
		} else if (!unitsTable.equals(other.unitsTable)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UnitsMain [");
		if (prefixedunitsTable != null) {
			builder.append("\n################################################################\nPrefixes Table: ");
			builder.append(prefixedunitsTable);
			builder.append(", ");
		}
		if (unitsTable != null) {
			builder.append("\n################################################################\nUnits Table: ");
			builder.append(unitsTable);
		}
		if (UnitsGraph != null) {
			builder.append("\n################################################################\nUnits Graph: ");
			builder.append(UnitsGraph);
		}
		builder.append("]");
		return builder.toString();
	}

}
