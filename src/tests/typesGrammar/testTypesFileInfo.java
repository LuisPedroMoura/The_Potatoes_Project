package tests.typesGrammar;

import typesGrammar.TypesFileInfo;

/**
 * <b>TestTypesFileInfo</b><p>
 * 
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class TestTypesFileInfo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//".\\src\\tests\\typesGrammar\\TypesExample1.txt"
		System.out.println("Testing File " + args[0]);
		TypesFileInfo file = new TypesFileInfo(args[0]);
		System.out.println(file);

	}

}
