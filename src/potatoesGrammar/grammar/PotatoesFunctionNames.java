package potatoesGrammar.grammar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import potatoesGrammar.grammar.PotatoesParser.FunctionIDContext;
import utils.errorHandling.ErrorHandling;
import utils.errorHandling.ErrorHandlingListener;


public class PotatoesFunctionNames {
	
	Map<String, FunctionIDContext> functions;
	Map<String, List<String>> functionsArgs;
	
	/**
	 * @return the functions
	 */
	public Map<String, FunctionIDContext> getFunctions() {
		return functions;
	}
	
	public Map<String, List<String>> getFunctionsArgs() {
		return functionsArgs;
	}
	

	public PotatoesFunctionNames(String path) {
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
			ErrorHandling.printError("Internal error reading the Types file! Please check if the file exists and can be read.");
			System.exit(2);
		}

		// create a lexer that feeds off of input CharStream:
		PotatoesLexer lexer = new PotatoesLexer(input);

		// create a buffer of tokens pulled from the lexer:
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// create a parser that feeds off the tokens buffer:
		PotatoesParser parser = new PotatoesParser(tokens);

		// replace error listener:
		parser.removeErrorListeners(); // remove ConsoleErrorListener
		parser.addErrorListener(new ErrorHandlingListener());

		// begin parsing at typesFile rule:
		ParseTree tree = parser.program();

		if (parser.getNumberOfSyntaxErrors() == 0) {
			// print LISP-style tree:
			// System.out.println(tree.toStringTree(parser));
			PotatoesFunctionsCheck visitor0 = new PotatoesFunctionsCheck();
			visitor0.visit(tree);
//			Don't care if there are errors, just want to create the list o function names
//			Potatoes Semantic Check will evaluate errors
//			if (!visitor0.visit(tree)) {
//				System.exit(3);  
//			}

			// Information to be transmited to the Potatoes Semantic Checker
			this.functions = visitor0.getFunctionsCtx();
			this.functionsArgs = visitor0.getFunctionsArgs();

		}
		else {
			// this code should be unreachable but is needed 
			// since the fields are final
			this.functions = null;
			this.functionsArgs = null;
			System.exit(3);
		}
	}

}
