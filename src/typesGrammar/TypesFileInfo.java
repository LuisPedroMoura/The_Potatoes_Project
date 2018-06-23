package typesGrammar;

import static java.lang.System.err;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import edu.uci.ics.jung.graph.Graph;
import utils.Factor;
import utils.Prefix;
import utils.Type;
import utils.errorHandling.ErrorHandling;
import utils.errorHandling.ErrorHandlingListener;

/**
 * 
 * <b>TypesFileInfo</b><p>
 * 
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class TypesFileInfo {

	// Instance Fields
	private final Map<String, Prefix>  prefixesTable;
	private final Map<String, Type>    typesTable;
	private final Graph<Type, Factor>  typesGraph;

	// --------------------------------------------------------------------------
	// CTOR
	/**
	 * 
	 * Constructor
	 * @param path path to the types file to be read
	 */
	@SuppressWarnings("resource")
	public TypesFileInfo(String path) {
		// create a stream from the file
		InputStream fileStream = null;

		// create a CharStream that reads from the file:		
		CharStream input = null;

		try {
			fileStream = new FileInputStream(new File(path)); 
			input = CharStreams.fromStream(fileStream);
			fileStream.close();
		} catch(FileNotFoundException e) {
			ErrorHandling.printError("Types file could not be found! Please check if the file exists and can be read.");
			System.exit(1);
		} catch (IOException e) {
			err.println("Internal error reading the Types file! Please check if the file exists and can be read.");
			System.exit(2);
		}

		// create a lexer that feeds off of input CharStream:
		TypesLexer lexer = new TypesLexer(input);

		// create a buffer of tokens pulled from the lexer:
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// create a parser that feeds off the tokens buffer:
		TypesParser parser = new TypesParser(tokens);

		// replace error listener:
		parser.removeErrorListeners(); // remove ConsoleErrorListener
		parser.addErrorListener(new ErrorHandlingListener());

		// begin parsing at typesFile rule:
		ParseTree tree = parser.typesFile();

		if (parser.getNumberOfSyntaxErrors() == 0) {
			// print LISP-style tree:
			// System.out.println(tree.toStringTree(parser));
			TypesInterpreter visitor0 = new TypesInterpreter();

			if (!visitor0.visit(tree)) {
				System.exit(3);  
			}

			// Information to be transmited to the Potatoes Semantic Checker
			this.prefixesTable = visitor0.getPrefixesTable();
			this.typesTable    = visitor0.getTypesTable();
			this.typesGraph    = visitor0.getTypesGraph();
		}
		else {
			// this code should be unreachable but is needed 
			// since the fields are final
			this.prefixesTable = null;
			this.typesTable    = null;
			this.typesGraph	   = null;
		}
	}

	// --------------------------------------------------------------------------
	// Getters
	/**
	 * Returns the table of prefixes defined in the file. 
	 * Can be an empty table (if no prefixes were declared in the file).
	 * @return prefixesTable
	 */
	public Map<String, Prefix> getPrefixesTable() {
		return prefixesTable;
	}

	/**
	 * Returns the graph of types defined in the file. 
	 * Can be an empty graph (if no types were declared in the file).
	 * @return typesGraph
	 */
	public Graph<Type, Factor> getTypesGraph() {
		return typesGraph;
	}

	/**
	 * Returns the table of types defined in the file. 
	 * Can be an empty table (if no types were declared in the file).
	 * @return typesTable
	 */
	public Map<String, Type> getTypesTable() {
		return typesTable;
	}

	// --------------------------------------------------------------------------
	// Other Methods
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prefixesTable == null) ? 0 : prefixesTable.hashCode());
		result = prime * result + ((typesTable == null) ? 0 : typesTable.hashCode());
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
		TypesFileInfo other = (TypesFileInfo) obj;
		if (prefixesTable == null) {
			if (other.prefixesTable != null) {
				return false;
			}
		} else if (!prefixesTable.equals(other.prefixesTable)) {
			return false;
		}
		if (typesTable == null) {
			if (other.typesTable != null) {
				return false;
			}
		} else if (!typesTable.equals(other.typesTable)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TypesMain [");
		if (prefixesTable != null) {
			builder.append("\n################################################################\nPrefixes Table: ");
			builder.append(prefixesTable);
			builder.append(", ");
		}
		if (typesTable != null) {
			builder.append("\n################################################################\nTypes Table: ");
			builder.append(typesTable);
		}
		if (typesGraph != null) {
			builder.append("\n################################################################\nTypes Graph: ");
			builder.append(typesGraph);
		}
		builder.append("]");
		return builder.toString();
	}

	// --------------------------------------------------------------------------
	// Main (for testing purposes)
	public static void main(String[] args) throws Exception {
		// create a CharStream that reads from standard input:
		CharStream input = CharStreams.fromStream(System.in);
		// create a lexer that feeds off of input CharStream:
		TypesLexer lexer = new TypesLexer(input);
		// create a buffer of tokens pulled from the lexer:
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		// create a parser that feeds off the tokens buffer:
		TypesParser parser = new TypesParser(tokens);
		// replace error listener:
		parser.removeErrorListeners(); // remove ConsoleErrorListener
		parser.addErrorListener(new ErrorHandlingListener());
		// begin parsing at typesFile rule:
		ParseTree tree = parser.typesFile();
		if (parser.getNumberOfSyntaxErrors() == 0) {
			// print LISP-style tree:
			// System.out.println(tree.toStringTree(parser));
			TypesInterpreter visitor0 = new TypesInterpreter();
			visitor0.visit(tree);
		}
	}
}
