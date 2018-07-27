/***************************************************************************************
*	Title: PotatoesProject - Type Class Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Co-author in version 1.0: Pedro Teixeira (https://pedrovt.github.io)
*	Acknowledgments for version 1.0: Maria Joao Lavoura (https://github.com/mariajoaolavoura),
*	for the help in brainstorming the concepts needed to create the first working version
*	of this Class that could deal with different type Variables operations.
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <b>Type</b><p>
 * 
 * @author Luis Moura
 * @version 2.0 - July 2018
 */
public class Type {

	// TODO corrigir javadocs dos metodos. está tudo errado quase.
	
	// Static Fields
	private static int newCode = 1;
	private static Map<Integer, Type> basicTypesCodesTable = new HashMap<>();

	// --------------------------------------------------------------------------
	// Instance Fields
	private String typeName;
	private String printName;
	private Code code;
	private boolean isClass;
	private boolean isStructure;
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
		this(typeName, printName, new Code(++newCode));
		// TODO does this even work?? because this is not created yet, is it???
		basicTypesCodesTable.put(newCode, this);
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
		this.isClass = false;
		this.isStructure = false;
	}
	
	/**
	 * 
	 * Constructor
	 * @param typeName
	 * @param printName
	 * @param code
	 * @param isClass
	 * @param isStructure
	 */
	public Type(String typeName, String printName, Code code, boolean isClass, boolean isStructure) {
		this.typeName = typeName;
		this.printName = printName;
		this.code = code;
		this.isClass = isClass;
		this.isStructure = isStructure;
	}

	/**
	 * Constructor for temporary types<p>
	 * Create a new type based on another type 
	 * @param code a unique prime number, or the result of operating with other codes
	 * @throws ArithmeticException if the code can't be represented by a double (overflow).
	 * See {@link https://stackoverflow.com/questions/31974837/can-doubles-or-bigdecimal-overflow}.
	 */
	public Type(Code calculatedCode) {
		this.typeName  = "temp";
		this.printName = "";
		this.code = calculatedCode;
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
	 * @return
	 */
	public static Map<Integer, Type> getBasicTypesCodesTable() {
		return basicTypesCodesTable;
	}
	
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
	
	/**
	 * @return
	 */
	public boolean isClass() {
		return isClass;
	}
	/**
	 * 
	 */
	public void setAsClass() {
		this.isClass = true;
	}
	
	/**
	 * @return
	 */
	public boolean isStructure() {
		return isStructure;
	}
	
	/**
	 * 
	 */
	public void setAsStructure() {
		this.isStructure = true;
	}

	// --------------------------------------------------------------------------
	// Operations with Types (Multiplication and Division)

	/**
	 * @return new Type with correspondent code
	 */
	public static Type multiply(Type a, Type b) {
		// FIXME add verifications, try to come up with idea to give correct type Name
		return new Type(Code.multiply(a.getCode(), b.getCode()));
	}

	/**
	 * @return new Type with correspondent code
	 */
	public static Type divide(Type a, Type b) {
		return new Type(Code.divide(a.getCode(), b.getCode()));
	}
	
	/**
	 * @return new Type with correspondent code
	 */
	public static Type power(Type a, int exponent) {
		return new Type(Code.power(a.getCode(), exponent));
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