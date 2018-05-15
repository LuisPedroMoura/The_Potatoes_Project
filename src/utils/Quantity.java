package projeto.si_units_grammar;

import java.util.ArrayList;
import java.util.List;

public class Quantity {
	
	private static int[] primes = {2,3,5,7,11,13,17,19,23};
	
	private double value;
	private int prefix = 0;
	private int numCode, denCode;
	private String dimention; // prefix and units.
	
	/**
	 * CONSTRCTOR
	 * @param value
	 * @param num_code
	 * @param den_code
	 * @param factor
	 */
	public Quantity(double value, int prefix, int numCode, int denCode) {
		this.value = value;
		adjustValueToBaseUnits();
		this.prefix = prefix;
		this.numCode = numCode;
		this.denCode = denCode;
	}

	// -------------------------------
	// ----- GETTERS AND SETTERS -----
	// -------------------------------
	
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getNumCode() {
		return numCode;
	}

	public void setNumCode(int num_code) {
		this.numCode = num_code;
	}

	public int getDenCode() {
		return denCode;
	}

	public void setDenCode(int den_code) {
		this.denCode = den_code;
	}

	public int getPrefix() {
		return prefix;
	}

	public void setPrefix(int factor) {
		this.prefix = factor;
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
	
	
	// -------------------------------
	// ------- STATIC METHODS --------
	// -------------------------------
	
	private static List<Integer> factorize(int code) {
		
		List<Integer> factors = new ArrayList<>();

		for (int i = 0; i <= code / i; i++) {
            while (code % primes[i] == 0) {
                factors.add(i);
                code /= primes[i];
            }
        }
        if (code > 1) {
            factors.add(code);
        }
        
        return factors;
		
	}
	
	
	private static int generateCode(List<Integer> factors) {
		
		int code = 0;
		for (int i = 0; i < factors.size(); i ++) {
			code *= factors.get(i);
		}
		
		return code;
	}
	
	
	private static boolean compareDimention(Quantity a, Quantity b) {
		return a.getNumCode() == b.getNumCode() && a.getDenCode() == b.getDenCode();
	}
	
	
	public static Quantity sum(Quantity a, Quantity b) {
		
		if (compareDimention(a,b)) {
			double value = a.getValue() + b.getValue();
			return new Quantity(value, a.getNumCode(), a.getNumCode(), 0);
		}
		else {
			System.out.println("Incompatible Dimentions Exception");
			throw new UnsupportedOperationException();
		}

	}
	
	
	public static Quantity subtract(Quantity a, Quantity b) {
		
		if (compareDimention(a,b)) {
			double value = a.getValue() - b.getValue();
			return new Quantity(value, a.getNumCode(), a.getNumCode(), 0);
		}
		else {
			System.out.println("Incompatible Dimentions Exception");
			throw new UnsupportedOperationException();
		}

	}
	
	
	public static Quantity multiply(Quantity a, Quantity b) {
		
		double value = a.getValue() * b.getValue();
		
		int numCode = generateCode(factorize(a.getNumCode())) * generateCode(factorize(b.getNumCode()));
		int denCode = generateCode(factorize(a.getDenCode())) * generateCode(factorize(b.getDenCode()));
		
		return new Quantity(value, numCode, denCode, 0);

	}
	
	
	public static Quantity divide(Quantity a, Quantity b) {
		
		double value = a.getValue() * b.getValue();
		
		int numCode = generateCode(factorize(a.getNumCode())) / generateCode(factorize(b.getNumCode()));
		int denCode = generateCode(factorize(a.getDenCode())) / generateCode(factorize(b.getDenCode()));
		
		if (numCode < 1 || denCode < 1) {
			System.out.println("Incompatible Dimentions Exception");
			throw new UnsupportedOperationException();
		}
		
		return new Quantity(value, numCode, denCode, 0);

	}
	
	
	// -------------------------------
	// ------ STANDARD METHODS -------
	// -------------------------------
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + denCode;
		result = prime * result + prefix;
		result = prime * result + numCode;
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Quantity other = (Quantity) obj;
		if (denCode != other.denCode)
			return false;
		if (prefix != other.prefix)
			return false;
		if (numCode != other.numCode)
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return value + " " + dimention;
	}
	

}
