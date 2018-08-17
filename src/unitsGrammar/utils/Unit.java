/***************************************************************************************
*	Title: PotatoesProject - Unit Class Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Co-author in version 1.0: Pedro Teixeira (https://pedrovt.github.io)
*	Acknowledgments for version 1.0: Maria Joao Lavoura (https://github.com/mariajoaolavoura),
*	for the help in brainstorming the concepts needed to create the first working version
*	of this Class that could deal with different unit Variables operations.
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package unitsGrammar.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <b>Unit</b><p>
 * 
 * @author Luis Moura (https://github.com/LuisPedroMoura)
 * @version 2.0 - July 2018
 */
public class Unit {

	// Static Fields
	private static int newCode = 1;
	private static Map<Integer, Unit> basicUnitsCodesTable = new HashMap<>();

	// --------------------------------------------------------------------------
	// Instance Fields
	private String name;
	private String symbol;
	private Code code;
	private boolean isClass;
	private boolean isStructure;

	// --------------------------------------------------------------------------
	// CTORs
	
	/**
	 * Constructor for basic units<p>
	 * Create a new basic unit - code is generated automatically
	 * @param name		for example 'meter'
	 * @param symbol	for example 'm'
	 */
	public Unit(String name, String symbol) {
		this(name, symbol, new Code(++newCode)); // ++ operator before variable ensures that code 1 is never used and can be saved for dimensionless unit 'number'
		// TODO does this even work?? because this is not created yet, is it???
		basicUnitsCodesTable.put(newCode, this);
	}

	/**
	 * Constructor for derived units<p>
	 * Create a new unit based on another unit - usually @code will be obtained by a units operation
	 * @param name	for example 'meter'
	 * @param symbol	for example 'm'
	 * @param code usually obtained in a Unit operation
	 */
	public Unit(String name, String symbol, Code code) {
		this(name, symbol, code, false, false);
	}
	
	/**
	 * Constructor for special Units<p>
	 * Classes o Units and Structures are treated internally as Units.
	 * They have to be tagged to be distinguished in graph traversals and path calculations
	 * @param name
	 * @param symbol
	 * @param code
	 * @param isClass
	 * @param isStructure
	 */
	public Unit(String name, String symbol, Code code, boolean isClass, boolean isStructure) {
		this.name = name;
		this.symbol = symbol;
		this.code = code;
		this.isClass = isClass;
		this.isStructure = isStructure;
	}

	/**
	 * Constructor for temporary units<p>
	 * Creates a new unit with name = "temp" and symbol = "". The code will be deep copied.
	 * Create a new unit based on another unit 
	 * @param code a unique prime number, or the result of operating with other codes
	 */
	public Unit(Code calculatedCode) {
		this.name  = "temp";
		this.symbol = "";
		this.code = new Code(calculatedCode);
	}

	/** 
	 * Copy Constructor 
	 * @param unit 
	 * @throws NullPointerException if unit is null
	 */ 
	public Unit(Unit unit) { 
		this.name  = unit.name; 
		this.symbol = unit.symbol;
		this.code = new Code(unit.getCode());
	}

	// --------------------------------------------------------------------------
	// Getters 
	
	/**
	 * @return 
	 */
	public static Map<Integer, Unit> getBasicUnitsCodesTable() {
		return basicUnitsCodesTable;
	}
	
	/**
	 * @return name, the name of this Unit.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return symbol, the symbol of this Unit.
	 */
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * @return the Code of this Unit.
	 */
	public Code getCode() {
		return code;
	}
	
	/**
	 * @return true if this Unit represents a Class of Units ("dimension").
	 */
	public boolean isClass() {
		return isClass;
	}
	
	/**
	 * sets this Unit as a Class of Units ("dimension") (this is not reversible).
	 */
	public void setAsClass() {
		this.isClass = true;
	}
	
	/**
	 * @return true is this Unit represents a structure ("multi-dimensional unit").
	 */
	public boolean isStructure() {
		return isStructure;
	}
	
	/**
	 * sets this Unit as Structure (this is not reversible).
	 */
	public void setAsStructure() {
		this.isStructure = true;
	}

	// --------------------------------------------------------------------------
	// Operations with Units (Multiplication and Division)

	/**
	 * @return new Unit with correspondent code resulting of the multiplication of two Units.
	 */
	public static Unit multiply(Unit a, Unit b) {
		// FIXME add verifications, try to come up with idea to give correct unit Name
		return new Unit(Code.multiply(a.getCode(), b.getCode()));
	}

	/**
	 * @return new Unit with correspondent code resulting of the division of two Units.
	 */
	public static Unit divide(Unit a, Unit b) {
		return new Unit(Code.divide(a.getCode(), b.getCode()));
	}
	
	/**
	 * @return new Unit with correspondent code resulting of the power of the Unit.
	 */
	public static Unit power(Unit a, int exponent) {
		return new Unit(Code.power(a.getCode(), exponent));
	}
	
	/**
	 * If a Unit is the result of an operation it's <b>name<b> and <b>symbol<b> will be random.
	 * Also, to get know types, some conversion might be needed, ie:
	 * meter * yard -> meter * meter, to obtain m^2.
	 * @param 	unitsGraph a Graph containing all the defined Units.
	 * @return 	the conversion factor obtained from the Unit conversion.
	 * 			So when Unit is used in with an associated quantity,
	 * 			this conversion factor must be applied to the quantity value.
	 */
	public double adjustUnitOperationResultToKnowUnit(Graph unitsGraph) {
		return this.code.simplifyCodeWithConvertions(unitsGraph);
	}

	// --------------------------------------------------------------------------
	// Other Methods

	@Override
	public String toString() {

		// For Debug Purposes Only
		StringBuilder builder = new StringBuilder();
		builder.append("Unit [");
		if (name != null) {
			builder.append(name);
			builder.append(", ");
		}
		if (symbol != null) {
			builder.append(symbol);
			builder.append(", ");
		}
		builder.append(code);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Unit other = (Unit) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}