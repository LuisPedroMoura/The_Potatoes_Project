package utils;

/**
 * <b>Variable</b><p>
 * To be used on the general purpose language<p>
 * For example, an instruction like {@code distance x = (distance) 5} will create an instance of this object with Type {@code distance}
 * (if the type exists in the Types table) and value {@code 5}.<p>
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class Variable {

// --------------------------------------------------------------------------
// INSTANCE FIELDS
	
	private final Type type;
	private final double value;

// --------------------------------------------------------------------------
// INSTANCE FIELDS
	
	/**
	 * @param type
	 * @param value
	 */
	public Variable(Type type, double value) {
		this.type = type;
		this.value = value;
	}

// --------------------------------------------------------------------------
// GETTERS / SETTERS
	
	/**
	 * @return type
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * @return type
	 */
	public double getValue() {
		return value;
	}
	
// --------------------------------------------------------------------------
// OPERATIONS WITH VARIABLES
	
	/**
	 * @return new Variable with new code and value
	 */
	public static Variable multiply(Variable a, Variable b) {
		Type newType = Type.multiply(a.getType(), b.getType());
		double newValue = a.getValue() * b.getValue();
		return new Variable(newType, newValue);
	}
	
	/**
	 * @return new Variable with new code and value
	 */
	public static Variable divide(Variable a, Variable b) {
		Type newType = Type.divide(a.getType(), b.getType());
		double newValue = a.getValue() / b.getValue();
		return new Variable(newType, newValue);
	}

	/**
	 * @return new Variable with same code and value
	 */
	public static Variable add(Variable a, Variable b) {
		if (!a.getType().equals(b.getType())) {
			throw new IllegalArgumentException();
		}
		double newValue = a.getValue() + b.getValue();
		return new Variable(a.getType(), newValue);
	}
	
	/**
	 * @return new Variable with same code and value
	 */
	public static Variable subtract(Variable a, Variable b) {
		if (!a.getType().equals(b.getType())) {
			throw new IllegalArgumentException();
		}
		double newValue = a.getValue() + b.getValue();
		return new Variable(a.getType(), newValue);
	}
	
	/**
	 * @return new Variable with same code and multiplied value
	 */
	public static Variable simetric(Variable a) {
		double newValue = a.getValue() * -1;
		return new Variable(a.getType(), newValue);
	}
	
	
	public boolean convertTypeTo(Type type) {
		
		// Variable type is already the one we're trying to convert to
		if (this.type == type){
			return true;
		}
		
		// get path from graph
		// convert value using edges cost
		// convert code to type code
		return true;
	}
	
	

// --------------------------------------------------------------------------
// OTHER METHODS	
	
	@Override
	public String toString() {
		return value + " " + type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Variable other = (Variable) obj;
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value)) {
			return false;
		}
		return true;
	}

}
