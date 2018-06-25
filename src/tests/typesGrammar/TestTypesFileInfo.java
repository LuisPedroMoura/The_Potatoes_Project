package tests.typesGrammar;
import typesGrammar.TypesFileInfo;

/**
 * 
 * <b>TestTypesFileInfo</b><p>
 * Allows testing of the Types Grammar & Intepreter and analyzis of the generated information.<p>
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 * @see {@link https://stackoverflow.com/questions/37740057/jung-large-graph-visualization?noredirect=1&lq=1}
 */
public class TestTypesFileInfo {

	public static void main(String[] args) {
		System.out.println("LFA Project | TestTypesFileInfo\nTesting File " + args[0] + "...\n");
		TypesFileInfo file = new TypesFileInfo(args[0]);
		System.out.println(file);

		new TestGraph(file.getTypesGraph());
	}


}
