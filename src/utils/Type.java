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

	// Static Fields 
	// Queue because each time a new Type is created, the next prime number is needed 
	// --> thus, we need a FIFO structure
	//private static Queue<Integer> primes = new LinkedList<>();
	private static List<Integer> primes = new LinkedList<>();	
	private static int index;

	// --------------------------------------------------------------------------
	// Static CTOR
	static { 
		index = 0;
		initPrimeNumbersList(1000);
	}

	// --------------------------------------------------------------------------
	// Instance Fields 
	private final String typeName;
	private final String printName;
	private final double codeID;
	private final Map<Double, Double> codes;

	// --------------------------------------------------------------------------
	// CTORS 
	/**
	 * Constructor for basic types<p>
	 * Create a new basic type
	 * @param typeName	for example 'distance'
	 * @param printName	for example 'm' (meter)
	 */
	public Type(String typeName, String printName) {
		this.typeName  = typeName;
		this.printName = printName;

		this.codeID	   = primes.get(index);
		index++;

		codes = new HashMap<>();
		codes.put(codeID, 1.0);		// a type is compatible with itself, with a factor of 1
		codes.put(1.0, 1.0);		// all types are compatible with the basic numeric type (represented by code 1.0)
	}

	/**
	 * Constructor for derived types<p>
	 * Create a new type based on another type 
	 * @param name	for example 'distance'
	 * @param printName	for example 'm' (meter)
	 * @param codes	for example 'm' (meter)
	 */
	public Type(String typeName, String printName, Map<Double, Double> codes) {
		this(typeName, printName);
		this.codes.putAll(codes);
	}

	/**
	 * Constructor for temporary types<p>
	 * Create a new type based on another type 
	 * @param codes	for example 'm' (meter)
	 */
	private Type(Map<Double, Double> codes) {
		this.typeName  = "temp";
		this.printName = "";
		this.codeID = 0;
		this.codes = codes;
		codes.put(1.0, 1.0);		
		// all types are compatible with the basic numeric type (represented by code 1.0)
	}

	// --------------------------------------------------------------------------
	// Getters
	/**
	 * 
	 * @return typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * 
	 * @return printName
	 */
	public String getPrintName() {
		return printName;
	}

	/**
	 * 
	 * @return codes the type compatible types' codes
	 */ 
	public Map<Double, Double> getCodes() {
		return codes;
	}

	/**
	 * 
	 * @return codeID the type unique code
	 */
	public double getCodeID() {
		return codeID;
	}

	// --------------------------------------------------------------------------
	// Operations With Types (Multiplication, Division)

	// used for generating the codes List for multiplication of totally different Types
	public static Type multiply(Type a, Type b) {
		return new Type(multiplyCodes(a, b));
	}

	public static Type divide(Type a, Type b) {
		return new Type(divideCodes(a, b));
	}

	/**
	 * @param a
	 * @param factor of the type a
	 * @return new map of codes
	 */
	public static Type or(Type original, Type a, double factor){
		return new Type(orCodes(original, a, factor));
	}

	// --------------------------------------------------------------------------
	// Verify Compatibility
	public boolean isCompatible(Type t) {
		Set<Double> tKeySet = t.getCodes().keySet();
		Set<Double> thisKeySet = codes.keySet();
		for (Double codeA : tKeySet) {
			for (Double codeB : thisKeySet) {
				if (codeA != codeB) {
					return false;
				}
			}
		}
		return true;		
	}

	// --------------------------------------------------------------------------
	// Aux Methods
	/**
	 * Multiplies the codes of two Types, which is the distributive of the 
	 * codes with multiplication.
	 * Corresponding factors are also multiplied but the result is inverted.
	 * @param a
	 * @param b
	 * @return new map of codes
	 */
	private static Map<Double, Double> multiplyCodes(Type a, Type b) {
		Map<Double, Double> newCodes = new HashMap<>();
		Set<Double> aKeySet = a.getCodes().keySet();
		Set<Double> bKeySet = b.getCodes().keySet();
		for (Double codeA : aKeySet) {
			for (Double codeB : bKeySet) {
				Double factorA = a.getCodes().get(codeA);
				Double factorB = b.getCodes().get(codeB);
				newCodes.put(codeA*codeB, 1/(factorA*factorB));
			}
		}

		return newCodes;
	}

	/**
	 * Divides the codes of two Types, which is the distributive of the 
	 * codes with division.
	 * Corresponding factors are also divided but in inverted position.
	 * @param a
	 * @param b
	 * @return new map of codes
	 */
	private static Map<Double, Double> divideCodes(Type a, Type b) {
		Map<Double, Double> newCodes = new HashMap<>();
		Set<Double> aKeySet = a.getCodes().keySet();
		Set<Double> bKeySet = b.getCodes().keySet();
		for (Double codeA : aKeySet) {
			for (Double codeB : bKeySet) {
				Double factorA = a.getCodes().get(codeA);
				Double factorB = b.getCodes().get(codeB);
				newCodes.put(codeA/codeB, factorB/factorA); // factor division is inverted from code division
			}
		}

		return newCodes;
	}

	/**
	 * @param original
	 * @param a
	 * @param factor 
	 * @return new map of codes
	 */
	private static Map<Double, Double> orCodes(Type original, Type a, double factor){
		Map<Double, Double> auxCode = new HashMap<>();
		Set<Double> originalKeySet  = original.getCodes().keySet();
		Set<Double> toAddKeySet     = a.getCodes().keySet();
		for (Double codeOriginal : originalKeySet) {
			auxCode.put(codeOriginal, original.getCodes().get(codeOriginal));
		}
		for (Double codeA : toAddKeySet) {
			auxCode.put(codeA, a.getCodes().get(codeA) * factor);
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

	// --------------------------------------------------------------------------
	// Other Methods
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[Type ");
		if (typeName != null) {
			builder.append(typeName);
		}
		if (printName != null) {
			builder.append(", Print Name: ");
			if (printName.equals("")) builder.append("undefined");
			else builder.append(printName);
		}
		builder.append(", Code ID: ");
		builder.append(codeID);
		if (codes != null) {
			builder.append(", Codes: ");
			builder.append(codes);
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(codeID);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((codes == null) ? 0 : codes.hashCode());
		result = prime * result + ((printName == null) ? 0 : printName.hashCode());
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
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
		Type other = (Type) obj;
		if (Double.doubleToLongBits(codeID) != Double.doubleToLongBits(other.codeID)) {
			return false;
		}
		if (codes == null) {
			if (other.codes != null) {
				return false;
			}
		} else if (!codes.equals(other.codes)) {
			return false;
		}
		if (printName == null) {
			if (other.printName != null) {
				return false;
			}
		} else if (!printName.equals(other.printName)) {
			return false;
		}
		if (typeName == null) {
			if (other.typeName != null) {
				return false;
			}
		} else if (!typeName.equals(other.typeName)) {
			return false;
		}
		return true;
	}
}