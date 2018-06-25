import static java.lang.System.err;

import java.io.*;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import compiler.PotatoesSemanticCheck;
import potatoesGrammar.PotatoesLexer;
import potatoesGrammar.PotatoesParser;
import utils.errorHandling.ErrorHandling;
import utils.errorHandling.ErrorHandlingListener;

/**
 * 
 * <b>PotatoesMain</b><p>
 * 
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class PotatoesMain {
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			err.println("Usage: PotatoesMain <file to compile>");
			System.exit(10);
		}

		// create a stream from the file
		InputStream fileStream = null;

		// create a CharStream that reads from the file:		
		CharStream input = null;

		try {
			fileStream = new FileInputStream(new File(args[0])); 
			input = CharStreams.fromStream(fileStream);
			fileStream.close();
		} catch(FileNotFoundException e) {
			ErrorHandling.printError("File \"" + args[0] + "\" could not be found! Please check if the file exists and can be read.");
			System.exit(1);
		} catch (IOException e) {
			err.println("Internal error reading the Types file! Please check if the file exists and can be read.");
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

		// begin parsing at program rule:
		ParseTree tree = parser.program();
		if (parser.getNumberOfSyntaxErrors() == 0) {
			// print LISP-style tree:
			// System.out.println(tree.toStringTree(parser));
			PotatoesSemanticCheck visitor0 = new PotatoesSemanticCheck();
			if (visitor0.visit(tree)) {
				ErrorHandling.printInfo("Semantic Analyzis Completed Sucessfully");
				ErrorHandling.printInfo("Compiling \"" + args[0] + "\"...");
				//PotatoesCompiler visitor1 = new PotatoesCompiler();
				//visitor1.visit(tree);
			}
		}
	}
}
