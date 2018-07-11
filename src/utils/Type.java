/***************************************************************************************
*	Title: PotatoesProject - Type Class Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Co-author in version 1.0: Pedro Teixeira (https://pedrovt.github.io)
*	Acknowledgments for version 1.0: Maria João Lavoura (https://github.com/mariajoaolavoura),
*	for the help in brainstorming the concepts needed to create the first working version
*	of this Class that could deal with different type Variables operations.
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * <b>Type</b><p>
 * 
 * @author Luis Moura
 * @version 2.0 - July 2018
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
	private Code code;
	//private List<Integer> numCodes = new ArrayList<>();		// numerator codes
	//private List<Integer> denCodes = new ArrayList<>();		// denominator codes

	// --------------------------------------------------------------------------
	// CTORs
	/**
	 * Constructor for basic types<p>
	 * Create a new basic type
	 * @param typeName	for example 'distance'
	 * @param printName	for example 'm' (meter)
	 */
	public Type(String typeName, String printName) {
		this(typeName, printName, new Code(primes.get(index)));
		index++;
	}

	/**
	 * Constructor for derived types<p>
	 * Create a new type based on another type 
	 * @param name	for example 'distance'
	 * @param printName	for example 'm' (meter)
	 * @param code a unique prime number, or the result of operating with other codes
	 * @throws ArithmeticException if the code can't be represented by a double (overflow).
	 * See {@link https://stackoverflow.com/questions/31974837/can-doubles-or-bigdecimal-overflow}.
	 */
	public Type(String typeName, String printName, Code code) {
		this.typeName = typeName;
		this.printName = printName;
		this.code = code;
		//this.numCodes.add(numCode); 	//TODO verify in TypesGrammar that Math.multiplyExact() is used to garantee a valid numCode
		//this.denCodes.add(denCode);
	}

	/**
	 * Constructor for temporary types<p>
	 * Create a new type based on another type 
	 * @param code a unique prime number, or the result of operating with other codes
	 * @throws ArithmeticException if the code can't be represented by a double (overflow).
	 * See {@link https://stackoverflow.com/questions/31974837/can-doubles-or-bigdecimal-overflow}.
	 */
	public Type(Code code) {
		//TODO verify that this method is still necessary. Is only used in TypesGrammar
		this.typeName  = "temp";
		this.printName = "";
		this.code = code;
		//this.numCodes.add(numCode); 	//TODO verify in TypesGrammar that Math.multiplyExact() is used to garantee a valid numCode
		//this.denCodes.add(denCode);
	}

	/** 
	 * Copy Constructor 
	 * @param type 
	 * @throws NullPointerException if a is null (ie new Type (null)) 
	 */ 
	public Type(Type type) { 
		this.typeName  = type.typeName; 
		this.printName = type.printName;
		this.code = new Code(type.getCode());
	}

	// --------------------------------------------------------------------------
	// Getters 

	/**
	 * @return typeName, the name of this Type
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @return printName, the name to be printed in Variables with this Type
	 * example: Type meter prints "m"
	 */
	public String getPrintName() {
		return printName;
	}
	
	/**
	 * @return the Code of this Type
	 */
	public Code getCode() {
		return code;
	}

	// --------------------------------------------------------------------------
	// Setters 
	// TODO, decide if these setter methods will be available in final version
	/**
	 * @param typeName
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @param printName
	 */
	public void setPrintName(String printName) {
		this.printName = printName;
	}

	// --------------------------------------------------------------------------
	// Op Types & Checklist Manipulations
//	public void addNumCode() {
//		opTypes.add(type);
//		checkList.add(false);
//	}
//
//	public boolean checkType(Type type) {
//		Double code = type.getCode();
//
//		for (int i = 0; i < checkList.size(); i++) {
//			if (checkList.get(i) == false) {
//				if (opTypes.get(i).getCode() == code) {
//					checkList.set(i, true);
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//
//	public List<Type> getUncheckedOpTypes(){
//		List<Type> unchecked = new LinkedList<>();
//		for (int i = 0; i < checkList.size(); i++) {
//			if (checkList.get(i) == false) {
//				unchecked.add(opTypes.get(i));
//			}
//		}
//		return unchecked;
//	}
//
//	public boolean clearCheckList() {
//		for(int i = 0; i < checkList.size(); i++) {
//			checkList.set(i, false);
//		}
//		return true;
//	}

	// --------------------------------------------------------------------------
	// Operations with Types (Multiplication and Division)

	/**
	 * @return new Type with correspondent code
	 */
	public static Type multiply(Type a, Type b) {
		// FIXME add verifications, try to come up with idea to give corret type Name
		return new Type(Code.multiply(a.getCode(), b.getCode()));
	}

	/**
	 * @return new Type with correspondent code
	 */
	public static Type divide(Type a, Type b) {
		return new Type(Code.divide(a.getCode(), b.getCode()));
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
		builder.append("\nType [");
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
		builder.append("]");
		return builder.toString();

		// return typeName;
		// return printName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
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