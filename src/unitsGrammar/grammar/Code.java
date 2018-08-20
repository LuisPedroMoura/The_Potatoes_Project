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
import java.util.stream.Collectors;

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
	public static Code add(Code a, Code b, Map<Unit, Map<Unit, Double>> conversionTable, Map<Integer, Unit> codesTable) throws IllegalArgumentException {
		
		b.matchCodes(a, conversionTable, codesTable);
		return a;
	}
	
	/**
	 * @param a
	 * @param b
	 * @return a new Code resulting of the multiplication of the two Codes
	 */
	public static Code subtract(Code a, Code b, Map<Unit, Map<Unit, Double>> conversionTable, Map<Integer, Unit> codesTable) throws IllegalArgumentException {
		
		b.matchCodes(a, conversionTable, codesTable);
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
		newCode.simplifyCode();
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
			newCode.simplifyCode();
		}
		return newCode;
	}
	
	/**
	 * simplifies Code by removing duplicate codes in numCodes and denCodes
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
	 * To simplify a Code using conversion from the give Graph of Units some conversions might be necessary, ie:
	 * m^2/yd -> m^2/m, to obtain m.
	 * @param unitsGraph
	 * @return 	the conversion factor obtained from the Code simplification.
	 * 			So when Unit containing this Code is used in with an associated quantity,
	 * 			this conversion factor must be applied to the quantity value.
	 */
	protected double simplifyCodeWithConvertions(Graph unitsGraph, Map<Integer, Unit> basicUnitsCodesTable) {
		Double factor = 1.0;
		Double conversionFactor = 1.0;
		while (conversionFactor != null) {
			factor *= conversionFactor;
			conversionFactor = simplifyCodeWithConvertionsPrivate(unitsGraph, basicUnitsCodesTable);
		}
		return factor;
	}
	// TODO not very efficient (think of what structure to use)
	private double simplifyCodeWithConvertionsPrivate(Graph unitsGraph, Map<Integer, Unit> basicUnitsCodesTable) {

		for (int numCode : this.numCodes) {
			Unit numUnit = basicUnitsCodesTable.get(numCode);
			for (int denCode : this.denCodes) {
				Unit denUnit = basicUnitsCodesTable.get(denCode);
				double conversionFactor = unitsGraph.getEdge(numUnit, denUnit);
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
	 * Convertes Code <b>a<b> to Code <b>b<b> and returns the conversion factor for the associated quantity
	 * MANDATORY: Codes <b>a<b> and <b>b<b> must simplified first with simplifyCode();
	 * Example: m*yd -> m^2
	 * @param a
	 * @param b
	 * @param unitsGraph
	 * @param codesTable
	 * @return
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
