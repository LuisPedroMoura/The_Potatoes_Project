package compiler;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import potatoesGrammar.PotatoesLexer;
import potatoesGrammar.PotatoesParser;

public class PotatoesMain {
   public static void main(String[] args) throws Exception {
      // create a CharStream that reads from standard input:
      CharStream input = CharStreams.fromStream(System.in);
      // create a lexer that feeds off of input CharStream:
      PotatoesLexer lexer = new PotatoesLexer(input);
      // create a buffer of tokens pulled from the lexer:
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      // create a parser that feeds off the tokens buffer:
      PotatoesParser parser = new PotatoesParser(tokens);
      // replace error listener:
      //parser.removeErrorListeners(); // remove ConsoleErrorListener
      //parser.addErrorListener(new ErrorHandlingListener());
      // begin parsing at program rule:
      ParseTree tree = parser.program();
      if (parser.getNumberOfSyntaxErrors() == 0) {
         // print LISP-style tree:
         // System.out.println(tree.toStringTree(parser));
         PotatoesVisitorSemanticAnalysis visitor0 = new PotatoesVisitorSemanticAnalysis();
         visitor0.visit(tree);
      }
   }
}
