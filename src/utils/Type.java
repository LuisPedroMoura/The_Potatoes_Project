package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * <b>Type</b><p>
 * 
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 * @param <V>
 */
public class Type {

	// Static Fields
	private static List<Integer> primes;	
	private static int index;

	// --------------------------------------------------------------------------
	// Static CTOR
	static { 
		primes = new LinkedList<>();
		index  = 0;
		initPrimeNumbersList(1000);
	}

	// --------------------------------------------------------------------------
	// Instance Fields

	private String typeName;
	private String printName;
	private final double code;
	private List<Type>	  opTypes 	= new ArrayList<>();
	private List<Boolean> checkList = new ArrayList<>();

	// --------------------------------------------------------------------------
	// CTORs
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
	 * @throws ArithmeticException if the code can't be represented by a double (overflow). See {@link https://stackoverflow.com/questions/31974837/can-doubles-or-bigdecimal-overflow}.
	 */
	public Type(String typeName, String printName, Double code) {
		this.typeName = typeName;
		this.printName = printName;
		if (Double.isInfinite(code) || Double.isNaN(code)) throw new ArithmeticException("overflow");
		this.code = code; 
	}

	/**
	 * Constructor for temporary types<p>
	 * Create a new type based on another type 
	 * @param code a unique prime number, or the result of operating with other codes
	 * @throws ArithmeticException if the code can't be represented by a double (overflow). See {@link https://stackoverflow.com/questions/31974837/can-doubles-or-bigdecimal-overflow}.
	 */
	public Type(Double code) {
		this.typeName  = "temp";
		this.printName = "";
		if (Double.isInfinite(code) || Double.isNaN(code)) throw new ArithmeticException("overflow");
		this.code = code;
	}

	// --------------------------------------------------------------------------
	// Getters / Setters

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

	/**
	 * @return opTypes List
	 */
	public List<Type> getOpTypes() {
		return opTypes;
	}

	public void setOpTypes(List<Type> opTypes) {
		this.opTypes = opTypes;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setPrintName(String printName) {
		this.printName = printName;
	}

	// --------------------------------------------------------------------------
	// Op Types & Checklist Manipulations
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

	public List<Type> getUncheckedOpTypes(){
		List<Type> list = new LinkedList<>();
		for (int i = 0; i < checkList.size(); i++) {
			if (checkList.get(i) == false) {
				list.add(opTypes.get(i));
			}
		}
		return list;
	}

	public boolean clearCheckList() {
		for(int i = 0; i < checkList.size(); i++) {
			checkList.set(i, false);
		}
		return true;
	}

	// --------------------------------------------------------------------------
	// Operations with Types (Multiplication and Division)

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
	// Auxiliar Methods

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
	// Other Methods

	@Override
	public String toString() {
		
		// For Debug Purposes Only
		StringBuilder builder = new StringBuilder();
		builder.append("Type [");
		if (typeName != null) {
			builder.append("typeName=");
			builder.append(typeName);
			builder.append(", ");
		}
		if (printName != null) {
			builder.append("printName=");
			builder.append(printName);
			builder.append(", ");
		}
		builder.append("code=");
		builder.append(code);
		builder.append(", ");
		if (opTypes != null) {
			builder.append("opTypes=");
			builder.append(opTypes);
			builder.append(", ");
		}
		if (checkList != null) {
			builder.append("checkList=");
			builder.append(checkList);
		}
		builder.append("]");
		return builder.toString();
		 
		//return typeName;
		// return printName;
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