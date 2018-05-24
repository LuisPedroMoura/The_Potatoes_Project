/**
 * 
 * <b>TypesMain</b><p>
 * 
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */

package typesGrammar;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import utils.errorHandling.ErrorHandlingListener;

public class TypesMain {
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
