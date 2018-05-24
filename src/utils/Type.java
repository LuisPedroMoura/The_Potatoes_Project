package utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <b>Type</b><p>
 * 
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */

public class Type {



	// Static Fields -----------------------------------------------------------------------
	// Queue because each time a new Type is created, the next prime number is needed 
	// --> thus, we need a FIFO structure
	//private static Queue<Integer> primes = new LinkedList<>();
	private static List<Integer> primes = new LinkedList<>();	
	private static int index;

	// Static CTOR	------------------------------------------------------------------------
	static { 
		index = 0;
		initPrimeNumbersList(1000);
	}

	// Instance Fields ---------------------------------------------------------------------
	private final String typeName;
	private final String printName;
	private Map<Double, Double> code = new HashMap<>();

	// CTORS -------------------------------------------------------------------------------

	/**
	 * Constructor<p>
	 * Create a new basic type
	 * @param name	for example 'distance'
	 */
	public Type(String name) {
		this(name, "");
	}

	/**
	 * Constructor<p>
	 * Create a new basic type
	 * @param typeName	for example 'distance'
	 * @param printName	for example 'm' (meter)
	 */
	public Type(String typeName, String printName) {
		this(typeName, printName, primes.get(index), 1);
		index++;
	}

	/**
	 * Constructor<p>
	 * Create a new type based on another type 
	 * @param name	for example 'distance'
	 * @param type	for example 'm' (meter)
	 * @param code	the code. can be the result of an operation between the codes of several types
	 */
	public Type(String typeName, double code, double factor) {
		this(typeName, "", code, factor);
	}


	/**
	 * Constructor<p>
	 * Create a new type based on another type 
	 * @param name	for example 'distance'
	 * @param type	for example 'm' (meter)
	 * @param code	the code. can be the result of an operation between the codes of several types
	 */
	public Type(String typeName, String printName, double code, double factor) {
		this.typeName    = typeName;
		this.printName    = printName;
		this.code.put(code, factor);
	}

	public Type(String typeName, String printName, Map<Double, Double> code) {
		this.typeName    = typeName;
		this.printName    = printName;
		this.code	 = code;
	}

	// -------------------------------
	// ---------- GETTERS ------------
	// -------------------------------
	public String getTypeName() {
		return typeName;
	}

	public String getPrintName() {
		return printName;
	}

	public Map<Double, Double> getCode() {
		return code;
	}

	// -------------------------------
	// ---- OPERATIONS WITH TYPES ----
	// -------------------------------


	// used for generating the codes List for multiplication of totally different Types
	public static Type multiply(Type a, Type b) {
		return new Type("temporary","", multiplyCodes(a, b));
	}

	public static Type divide(Type a, Type b) {
		return new Type("temporary","", divideCodes(a, b));
	}

	//	public static Type add(Type a, Type b) {
	//		return new Type("","", a.getCode());
	//	}
	//	
	//	public static Type subtract(Type a, Type b) {
	//		return new Type("","", Code.subtract(a.getCode(), b.getCode()));
	//	}

	
	public boolean isCompatible(Type t) {
		Set<Double> tKeySet = t.getCode().keySet();
		Set<Double> thisKeySet = code.keySet();
		for (Double codeA : tKeySet) {
			for (Double codeB : thisKeySet) {
				if (codeA != codeB) {
					return false;
				}
			}
		}
		return true;		
	}
	
	
//	public static boolean isCompatible(Type a, Type b) {
//		return Code.isCompatible(a.getCode(), b.getCode());
//	}
	
	
	/**
	 * Multiplies the codes of two Types, which is the distributive of the codes wit multiplication
	 * Corresponding factors are also multiplied but the result is inverted
	 * @param a
	 * @param b
	 * @return new map of codes
	 */
	private static Map<Double, Double> multiplyCodes(Type a, Type b) {
		Map<Double, Double> auxCode = new HashMap<>();
		Set<Double> aKeySet = a.getCode().keySet();
		Set<Double> bKeySet = b.getCode().keySet();
		for (Double codeA : aKeySet) {
			for (Double codeB : bKeySet) {
				Double factorA = a.getCode().get(codeA);
				Double factorB = b.getCode().get(codeB);
				auxCode.put(codeA*codeB, 1/(factorA*factorB));
			}
		}
		return auxCode;
	}
	
	/**
	 * Divides the codes of two Types, which is the distributive of the codes with division
	 * Corresponding factors are also divided but in inverted position
	 * @param a
	 * @param b
	 * @return new map of codes
	 */
	private static Map<Double, Double> divideCodes(Type a, Type b) {
		Map<Double, Double> auxCode = new HashMap<>();
		Set<Double> aKeySet = a.getCode().keySet();
		Set<Double> bKeySet = b.getCode().keySet();
		for (Double codeA : aKeySet) {
			for (Double codeB : bKeySet) {
				Double factorA = a.getCode().get(codeA);
				Double factorB = b.getCode().get(codeB);
				auxCode.put(codeA/codeB, factorB/factorA); // factor division is inverted from code division
			}
		}
		return auxCode;
	}
	
	
	
	
	public static Map<Double, Double> orCodes(Type a, Type b){
		Map<Double, Double> auxCode = new HashMap<>();
		Set<Double> aKeySet = a.getCode().keySet();
		Set<Double> bKeySet = b.getCode().keySet();
		for (Double codeA : aKeySet) {
			auxCode.put(codeA, a.getCode().get(codeA));
		}
		for (Double codeB : bKeySet) {
			auxCode.put(codeB, b.getCode().get(codeB));
		}
		return auxCode;
	}




	/**
	 * 
	 * @param n number of prime numbers to be generated
	 * @see   {@link http://www.baeldung.com/java-generate-prime-numbers}
	 */
	private static void initPrimeNumbersList(int n) {
		boolean prime[] = new boolean[n + 1];
		Arrays.fill(prime, true);
		for (int p = 2; p * p <= n; p++) {
			if (prime[p]) {
				for (int i = p * 2; i <= n; i += p) {
					prime[i] = false;
				}
			}
		}
		for (int i = 2; i <= n; i++) {
			if (prime[i]) {
				primes.add(i);
			}
		}
	}


}
