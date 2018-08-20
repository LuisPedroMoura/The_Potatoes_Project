/***************************************************************************************
*	Title: PotatoesProject - Code Class Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Acknowledgments for version 1.0: Maria Joao Lavoura (https://github.com/mariajoaolavoura),
*	for the help in brainstorming the concepts needed to create the first working version
*	of this Class.
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package unitsGrammar.grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <b>Code</b><p>
 * 
 * @author Luis Moura (https://github.com/LuisPedroMoura)
 * @version 2.0 - July 2018
 */
public class Code {
	
	private List<Integer> numCodes = new ArrayList<>();;		// numerator codes
	private List<Integer> denCodes = new ArrayList<>();;		// denominator codes
	
	/**
	 * Constructor of empty Code, to use in Units operations
	 */
	public Code () {}
	
	/**
	 * Constructor for Code of basic Units
	 * @param primeNumber
	 */
	public Code (Integer number) {
		numCodes.add(number);
	}
	
	/**
	 * Copy Constructor
	 * @param code
	 * @throws NullPointerException if <b>code<b> is null.
	 */
	public Code (Code code) {
		this.numCodes = new ArrayList<>();
		for (int numCode : code.getNumCodes()) {
			this.numCodes.add(numCode);
		}
		this.denCodes = new ArrayList<>();
		for (int denCode : code.getDenCodes()) {
			this.denCodes.add(denCode);
		}
	}
	
	/**
	 * @return numCodes
	 */
	public List<Integer> getNumCodes() {
		return numCodes;
	}
	
	/**
	 * @return denCodes
	 */
	public List<Integer> getDenCodes() {
		return denCodes;
	}
	
	private void addNumCode(Integer code) {
		this.numCodes.add(code);
	}
	
	private void addDenCode(Integer code) {
		this.numCodes.add(code);
	}
	
	private void remNumCode(Integer code) {
		this.numCodes.add(code);
	}
	
	private void remDenCode(Integer code) {
		this.numCodes.add(code);
	}
	
	
	/**
	 * @param a
	 * @param b
	 * @return if the two codes are equivalent returns the Code of <b>a<b>
	 * @throws IllegalArgumentException if the Codes are not equivalent
	 */
	public static Code add(Code a, Code b) throws IllegalArgumentException {
		if (!a.equals(b)) {
			throw new IllegalArgumentException();
		}
		return a;
	}
	
	/**
	 * @param a
	 * @param b
	 * @return a new Code resulting of the multiplication of the two Codes
	 */
	public static Code subtract(Code a, Code b) throws IllegalArgumentException {
		if (!a.equals(b)) {
			throw new IllegalArgumentException();
		}
		return a;
	}
	
	/**
	 * @param a
	 * @param b
	 * @return a new Code resulting of the multiplication of the two Codes
	 */
	public static Code multiply(Code a, Code b) {
		Code newCode = new Code();
		newCode.multiplyCode(a);
		newCode.multiplyCode(b);
		newCode.simplifyCode();
		newCode.simplifyCodeWithConvertions(Units.getConversionTable(), Units.getBasicUnitsCodesTable());
		return newCode;
	}
	private void multiplyCode(Code code) {
		for (int numCode : code.getNumCodes()) {
			this.numCodes.add(numCode);
		}
		for (int denCode : code.getDenCodes()) {
			this.denCodes.add(denCode);
		}
	}
	
	/** 
	 * @param a
	 * @param b
	 * @return a new Code resulting of the division of the two Codes
	 */
	public static Code divide(Code a, Code b) {
		Code newCode = new Code();
		newCode.multiplyCode(a);
		newCode.divideCode(b);
		newCode.simplifyCodeWithConvertions(Units.getConversionTable(), Units.getBasicUnitsCodesTable());
		return newCode;
	}
	private void divideCode(Code code) {
		for (int numCode : code.getNumCodes()) {
			this.denCodes.add(numCode);
		}
		for (int denCode : code.getDenCodes()) {
			this.numCodes.add(denCode);
		}
	}
	
	/**
	 * @param a
	 * @param exponent
	 * @return a new Code resulting of the power of the Code
	 */
	public static Code power(Code a, int exponent) {
		Code newCode = new Code();
		for (int i = 0; i < exponent; i++) {
			newCode.multiplyCode(a);
		}
		newCode.simplifyCodeWithConvertions(Units.getConversionTable(), Units.getBasicUnitsCodesTable());
		return newCode;
	}
	
	/**
	 * After operating with Units, because every Unit's Code is added to the Code structure, the generated code needs to be simplified. 
	 * This method does a simple simplification by removing duplicate codes in the Code numerator and denominator.
	 */
	private void simplifyCode() {
		for (Integer numCode : numCodes) {
			if (denCodes.contains(numCode)) {
				numCodes.remove(numCode);
				denCodes.remove(numCode);
			}
		}
	}
	
	/**
	 * After operating with Units, because every Unit's Code is added to the Code structure, the generated code needs to be simplified.
	 * This method uses a table with conversion factors between equivalent units in order to simplify 'this' Code completely.
	 * Example: m^2/yd -> m^2/m, to obtain m.
	 * @param conversionTable, a Table with all the possible conversions
	 * @param basicUnitsCodesTable, a Table with all the basic codes that compose a derivated Code
	 * @return the conversion factor obtained from the Code simplification. To be used if a Quantity is associated with the Unit.
	 */
	protected double simplifyCodeWithConvertions(Map<Unit, Map<Unit, Double>> conversionTable, Map<Integer, Unit> basicUnitsCodesTable) {
		Double factor = 1.0;
		Double conversionFactor = 1.0;
		this.simplifyCode();
		while (conversionFactor != null) {
			factor *= conversionFactor;
			conversionFactor = simplifyCodeWithConvertionsPrivate(conversionTable, basicUnitsCodesTable);
		}
		return factor;
	}
	// TODO not very efficient (think of what structure to use)
	private double simplifyCodeWithConvertionsPrivate(Map<Unit, Map<Unit, Double>> conversionTable, Map<Integer, Unit> basicUnitsCodesTable) {

		for (int numCode : this.numCodes) {
			Unit numUnit = basicUnitsCodesTable.get(numCode);
			for (int denCode : this.denCodes) {
				Unit denUnit = basicUnitsCodesTable.get(denCode);
				double conversionFactor = conversionTable.get(numUnit).get(denUnit);
				if (conversionFactor != Double.POSITIVE_INFINITY){
					numCodes.remove(numCode);
					denCodes.remove(denCode);
					return conversionFactor;
				}
			}
		}
		return 1.0;
	}
	
	/**
	 * When adding, subtracting, assigning or casting Units, some conversions might be necessary. The Code is the main identity of the Unit.
	 * This method tries to convert 'this' to the Code of another Unit.
	 * Example: m*yd -> m^2
	 * @param a, the destination Code of 'this' conversion
	 * @param conversionTable, a Table with all the possible conversions
	 * @param basicUnitsCodesTable, a Table with all the basic codes that compose a derivated Code
	 * @return the conversion factor obtained from the Code conversion. To be used if a Quantity is associated with the Unit.
	 */
	protected double matchCodes(Code a, Map<Unit, Map<Unit, Double>> conversionTable, Map<Integer, Unit> codesTable) {
		
		// Codes are equal, no matching is needed. Quantity conversion factor is neutral.
		if (this.equals(a)) {
			return 1.0;
		}
		
		// Codes size does not match -> Codes are not equivalent
		if (this.numCodes.size() != a.getNumCodes().size() || this.denCodes.size() != a.getDenCodes().size()) {
			throw new IllegalArgumentException();
		}
		
		a = new Code(a); // deep copy
		Code b = new Code(this); // deep copy
		int codeSize = this.numCodes.size() + this.denCodes.size();

		Double conversionFactor = 1.0;
		Double localFactor = 1.0;
		
		// First simplify codes
		a.simplifyCode();
		b.simplifyCode();
		
		List<Integer> AnumCodes = a.getNumCodes();
		List<Integer> BnumCodes = b.getNumCodes();
		List<Integer> AdenCodes = a.getDenCodes();
		List<Integer> BdenCodes = b.getDenCodes();
		
		// remove all numB from A
		for (Integer numB : BnumCodes) {
			AnumCodes.remove(numB);
		}
		// remove all numA from B
		for (Integer numA : AnumCodes) {
			BnumCodes.remove(numA);
		}
		
		// remove all denB from A
		for (Integer denB : BdenCodes) {
			AdenCodes.remove(denB);
		}
		// remove all denA from B
		for (Integer denA : AdenCodes) {
			BdenCodes.remove(denA);
		}
		
		
		// Now, if Codes are simplified if they are equivalent only codes that need conversion remain
		// Code this (on the right) is always converted to Code a (on the left)
		for (Integer numB : b.getNumCodes()) {
			for (Integer numA : a.getNumCodes()) {
				localFactor = conversionTable.get(codesTable.get(numB)).get(codesTable.get(numA));
				if (localFactor != null) {
					conversionFactor *= localFactor;
					codeSize--;
					break;
				}
			}
		}
		
		for (Integer denB : b.getDenCodes()) {
			for (Integer denA : a.getDenCodes()) {
				localFactor = conversionTable.get(codesTable.get(denB)).get(codesTable.get(denA));
				if (localFactor != Double.POSITIVE_INFINITY) {
					conversionFactor /= localFactor;
					codeSize--;
					break;
				}
			}
		}
		
		// conversions where made but not all codes where matched -> Codes are not equivalent
		if (codeSize != 0) {
			throw new IllegalArgumentException();
		}
		
		return conversionFactor;
	}
	
	
	
	@Override
	public String toString() {
		
		// For Debug Purposes Only
		StringBuilder builder = new StringBuilder();
		builder.append("Code [");
		if (numCodes != null) {
			builder.append(numCodes);
			builder.append(" / ");
		}
		if (denCodes != null) {
			builder.append(denCodes);
		}
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((denCodes == null) ? 0 : denCodes.hashCode());
		result = prime * result + ((numCodes == null) ? 0 : numCodes.hashCode());
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
		Code other = (Code) obj;
		if (denCodes == null) {
			if (other.denCodes != null)
				return false;
		} else if (!denCodes.equals(other.denCodes))
			return false;
		if (numCodes == null) {
			if (other.numCodes != null)
				return false;
		} else if (!numCodes.equals(other.numCodes))
			return false;
		return true;
	}
	
}
