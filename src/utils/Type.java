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

	private static List<Integer> primes = new LinkedList<>();	
	private static int index;

// --------------------------------------------------------------------------
// STATIC CONSTRUCTOR
	static { 
		index = 0;
		initPrimeNumbersList(1000);
	}

// --------------------------------------------------------------------------
// INSTANCE FIELDS
	
	private final String typeName;
	private final String printName;
	private final double code;
	private List<Type> opTypes = new ArrayList<>();
	private List<Boolean> checkList = new ArrayList<>();

// --------------------------------------------------------------------------
// CONTRUCTORS
	
	/**
	 * Constructor for basic types<p>
	 * Create a new basic type
	 * @param typeName	for example 'distance'
	 * @param printName	for example 'm' (meter)
	 */
	public Type(String typeName, String printName) {
		this(typeName, printName, (double) primes.get(index));
		index++;
		
	}

	/**
	 * Constructor for derived types<p>
	 * Create a new type based on another type 
	 * @param name	for example 'distance'
	 * @param printName	for example 'm' (meter)
	 * @param code a unique prime number, or the result of operating with other codes
	 */
	public Type(String typeName, String printName, Double code) {
		this.typeName = typeName;
		this.printName = printName;
		this.code = code;
	}

	/**
	 * Constructor for temporary types<p>
	 * Create a new type based on another type 
	 * @param code a unique prime number, or the result of operating with other codes
	 */
	private Type(Double code) {
		this.typeName  = "temp";
		this.printName = "";
		this.code = code;
	}

// --------------------------------------------------------------------------
// GETTERS / SETTERS
	
	/**
	 * @return typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @return printName
	 */
	public String getPrintName() {
		return printName;
	}

	/**
	 * @return codeID the type unique code
	 */
	public double getCode() {
		return code;
	}

// --------------------------------------------------------------------------
// OPTYPES AND CHECKLIST MANIPULATIONS
	
	public void addOpType(Type type) {
		opTypes.add(type);
		checkList.add(false);
	}
	
	public boolean checkType(Type type) {
		Double code = type.getCode();
		
		for (int i = 0; i < checkList.size(); i++) {
			if (checkList.get(i) == false) {
				if (opTypes.get(i).getCode() == code) {
					checkList.set(i, true);
					return true;
				}
			}
		}
		return false;
	}
	
	public Variable convertVariableToFirstPossibleTypeInOpTypeArray(Variable a) {
		for (int i = 0; i < checkList.size(); i++) {
			if (a.convertTypeTo(opTypes.get(i))) {
				break;
			}
		}
		return a;
	}
	
	public Variable convertToMaxParent(Variable a) {
		// while has parent, convert a.Type to parent.Type
		return a;
	}
	
	public boolean clearCheckList() {
		for(int i = 0; i < checkList.size(); i++) {
			checkList.set(i, false);
		}
		return true;
	}
	
// --------------------------------------------------------------------------
// OPERATIONS WITH TYPES (Multiplication and Division)

	/**
	 * @return new Type with correspondent code
	 */
	public static Type multiply(Type a, Type b) {
		return new Type(a.getCode() * b.getCode());
	}

	/**
	 * @return new Type with correspondent code
	 */
	public static Type divide(Type a, Type b) {
		return new Type(a.getCode() / b.getCode());
	}


// --------------------------------------------------------------------------
// AUXILIAR METHODS

	/**
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
// OTHER METHODS

	@Override
	public String toString() {
		return printName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((checkList == null) ? 0 : checkList.hashCode());
		long temp;
		temp = Double.doubleToLongBits(code);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((opTypes == null) ? 0 : opTypes.hashCode());
		result = prime * result + ((printName == null) ? 0 : printName.hashCode());
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Type other = (Type) obj;
		if (checkList == null) {
			if (other.checkList != null)
				return false;
		} else if (!checkList.equals(other.checkList))
			return false;
		if (Double.doubleToLongBits(code) != Double.doubleToLongBits(other.code))
			return false;
		if (opTypes == null) {
			if (other.opTypes != null)
				return false;
		} else if (!opTypes.equals(other.opTypes))
			return false;
		if (printName == null) {
			if (other.printName != null)
				return false;
		} else if (!printName.equals(other.printName))
			return false;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}


	
	
}