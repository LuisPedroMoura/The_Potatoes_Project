package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Code</b><p>
 * 
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class Code {
	
	private List<Double> codes;
	
	public Code () {
		codes = new ArrayList<>();
	}
	
	/**
	 * Adds new code to list
	 * @param code
	 */
	public void add(double code) {
		codes.add(code);
	}
	
	/**
	 * @return codes List
	 */
	public List<Double> getCodes(){
		return codes;
	}
	
	/**
	 * 
	 * @return first element of codes List
	 */
	public double getBaseCode() {
		return codes.get(0);
	}
	
	/**
	 * Checks for compatibility of codes. At least one code in each List must be equal
	 * @param a Type to have its code compared
	 * @param b Type to have its code compared
	 * @return true if compatible
	 */
	public static boolean isCompatible(Code a, Code b) {
		for (Double code : a.getCodes()) {
			if (b.getCodes().contains(code)) {
				return false;
			}
		}
		return true;
	}
	

	public static Code multiply(Code a, Code b){
		Code newCode = new Code();
		for (Double codeA : a.getCodes()) {
			for (Double codeB : b.getCodes()) {
				newCode.add(codeA * codeB);
			}
		}
		return newCode;
	}
	
	public static Code divide(Code a, Code b){
		Code newCode = new Code();
		for (Double codeA : a.getCodes()) {
			for (Double codeB : b.getCodes()) {
				newCode.add(codeA / codeB);
			}
		}
		return newCode;
	}
	

	
	public static Code or(Type a, Type b){
		Code newCode = new Code();
		for (Double codeA : a.getCode().getCodes()) {
			newCode.add(codeA);
		}
		for (Double codeB : b.getCode().getCodes()) {
			newCode.add(codeB);
		}
		return newCode;
	}
	
	

}
