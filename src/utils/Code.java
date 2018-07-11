package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Code</b><p>
 * 
 * @author Luis Moura (https://github.com/LuisPedroMoura)
 * @version July 2018
 */
public class Code {

	private List<Integer> numCodes = new ArrayList<>();;		// numerator codes
	private List<Integer> denCodes = new ArrayList<>();;		// denominator codes

	/**
	 * Constructor for Code of basic Types
	 * @param primeNumber
	 */
	// TODO verify if primeNumbers are still necessary, I believe that this new structure will allow consecutive numbers
	public Code (Integer primeNumber) {
		numCodes.add(primeNumber);
	}
	
	public Code () {}
	
	/**
	 * Copy Constructor
	 * @param code
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

	public List<Integer> getNumCodes() {
		return numCodes;
	}

	public List<Integer> getDenCodes() {
		return denCodes;
	}
	
	private void multiplyCode(Code code) {
		for (int numCode : code.getNumCodes()) {
			this.numCodes.add(numCode);
		}
		for (int denCode : code.getDenCodes()) {
			this.denCodes.add(denCode);
		}
	}
	
	public static Code multiply(Code a, Code b) {
		Code newCode = new Code();
		newCode.multiplyCode(a);
		newCode.multiplyCode(b);		
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
	
	public static Code divide(Code a, Code b) {
		Code newCode = new Code();
		newCode.multiplyCode(a);
		newCode.divideCode(b);		
		return newCode;
	}
	
	public void simplifyCode() {
		for (Integer numCode : numCodes) {
			if (denCodes.contains(numCode)) {
				numCodes.remove(numCode);
				denCodes.remove(numCode);
			}
		}
	}

	
	
	
	
	@Override
	public String toString() {
		
		// For Debug Purposes Only
		StringBuilder builder = new StringBuilder();
		builder.append("\nType [");
		if (numCodes != null) {
			builder.append("numCodes=");
			builder.append(numCodes);
			builder.append(", ");
		}
		if (denCodes != null) {
			builder.append("denCodes=");
			builder.append(denCodes);
			builder.append(", ");
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
