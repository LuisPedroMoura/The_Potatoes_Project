package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <b>Type</b><p>
 * 
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */

public class Type {

	/* Legacy Code ------------------------------------------------------------------------
	private double value;	
	private int prefix = 0;
	private int numCode, denCode;
	private String dimention; // prefix and units.

	public Type(double value, int prefix, int numCode, int denCode) {
		this.value = value;
		adjustValueToBaseUnits();
		this.prefix = prefix;
		this.numCode = numCode;
		this.denCode = denCode;
	}

	// -------------------------------
	// ------ AUXILIAR METHODS -------
	// -------------------------------
	private void adjustValueToBaseUnits(){
		if (prefix > 1) {
			while (prefix > 1) {
				value *= 3;
				prefix -= 3;
			}
			prefix = 1;
		}

		if (prefix < 1) {
			while (prefix < 1) {
				value /= 3;
				prefix += 3;
			}
		}
	}

	private static int generateCode(List<Integer> factors) {	// new method to generate codes

		int code = 0;
		for (int i = 0; i < factors.size(); i ++) {
			code *= factors.get(i);
		}

		return code;
	}

	public static Type subtract(Type a, Type b) {		//no subtract operation

		if (compareDimention(a,b)) {
			double value = a.getValue() - b.getValue();
			return new Type(value, a.getNumCode(), a.getNumCode(), 0);
		}
		System.out.println("Incompatible Dimentions Exception");
		throw new UnsupportedOperationException();

	}

	 */

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
	private final String name;
	private final String unit;
	private final double factor;
	private Code code = new Code();

	// CTORS -------------------------------------------------------------------------------

	/**
	 * Constructor<p>
	 * Create a new basic unit
	 * @param name	for example 'distance'
	 */
	public Type(String name) {
		this(name, "");
	}

	/**
	 * Constructor<p>
	 * Create a new basic unit
	 * @param name	for example 'distance'
	 * @param unit	for example 'm' (meter)
	 */
	public Type(String name, String unit) {
		this(name, unit, 1, primes.get(index));
		index++;
	}

	/**
	 * Constructor<p>
	 * Create a new unit based on another unit 
	 * @param name	for example 'distance'
	 * @param unit	for example 'm' (meter)
	 * @param code	the code. can be the result of an operation between the codes of several types
	 */
	public Type(String name, double code) {
		this(name, "", 1, code);
	}
	
	public Type(String name, double factor, double code) {
		this(name, "", factor, code);
	}

	/**
	 * Constructor<p>
	 * Create a new unit based on another unit 
	 * @param name	for example 'distance'
	 * @param unit	for example 'm' (meter)
	 * @param code	the code. can be the result of an operation between the codes of several types
	 */
	public Type(String name, String unit, double factor, double code) {
		this.name    = name;
		this.unit    = unit;
		this.factor  = factor;
		this.code.add(code);
	}
	
	public Type(String name, String unit, double factor, Code code) {
		this.name    = name;
		this.unit    = unit;
		this.factor  = factor;
		this.code	 = code;
	}

	// -------------------------------
	// ---------- GETTERS ------------
	// -------------------------------
	public String getName() {
		return name;
	}

	public String getUnit() {
		return unit;
	}
	
	public double getFactor() {
		return factor;
	}
	public Code getCode() {
		return code;
	}

	// -------------------------------
	// ---- OPERATIONS WITH TYPES ----
	// -------------------------------
	public static Code or(Type a, Type b) {
		return Code.or(a, b);
	}
	
	// used for generating the codes List for multiplication of totally different Types
	public static Type multiply(Type a, Type b) {
		return new Type("","", 1, Code.multiply(a.getCode(), b.getCode()));
	}
	
	public static Type divide(Type a, Type b) {
		return new Type("","", 1, Code.divide(a.getCode(), b.getCode()));
	}
	
//	public static Type add(Type a, Type b) {
//		return new Type("","", a.getCode());
//	}
//	
//	public static Type subtract(Type a, Type b) {
//		return new Type("","", Code.subtract(a.getCode(), b.getCode()));
//	}

	
	public static boolean isCompatible(Type a, Type b) {
		return Code.isCompatible(a.getCode(), b.getCode());
	}
	
	public static Type isBaseType(Type a, Type b) {
		if (a.getFactor() <= b.getFactor()){
			return a;
		}
		return b;
	}
	


	// -------------------------------
	// ------ AUXILIAR METHODS -------
	// -------------------------------
//	private static List<Integer> factorize(int code) {
//
//		List<Integer> factors = new ArrayList<>();
//
//		for (int i = 0; i <= code / i; i++) {
//			while (code % primes.get(i) == 0) {
//				factors.add(i);
//				code /= primes.get(i);
//			}
//		}
//		if (code > 1) {
//			factors.add(code);
//		}
//
//		return factors;
//
//	}
//
//	private static boolean compareDimention(Type a, Type b) {
//		return a.getNumCode() == b.getNumCode() && a.getDenCode() == b.getDenCode();
//	}

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

	// -------------------------------
	// ------ STANDARD METHODS -------
	// -------------------------------

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + denCode;
//		result = prime * result + prefix;
//		result = prime * result + numCode;
//		long temp;
//		temp = Double.doubleToLongBits(value);
//		result = prime * result + (int) (temp ^ (temp >>> 32));
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Type other = (Type) obj;
//		if (denCode != other.denCode)
//			return false;
//		if (prefix != other.prefix)
//			return false;
//		if (numCode != other.numCode)
//			return false;
//		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
//			return false;
//		return true;
//	}
//
//	@Override
//	public String toString() {
//		return value + " " + dimention;
//	}

}
