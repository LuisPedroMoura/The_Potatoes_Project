package utils;

/**
 * <b>Variable</b><p>
 * To be used on the general purpose language<p>
 * For example, an instruction like {@code distance x = 5} will create an instance of this object with Type {@code distance}
 * (if the type exists in the Types table) and value {@code 5}.<p>
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class Variable {

	// Instance Fields
	private final Type type;
	private final double value;

	/**
	 * Constructor
	 * @param type
	 * @param value
	 */
	public Variable(Type type, double value) {
		this.type = type;
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public double getValue() {
		return value;
	}
	/**
	 * 
	 * @return Variable value converted to the defined Base units
	 */
	public double getBaseValue() {
		return value * this.getType().getFactor();
	}
	
	
	public static Variable multiply(Variable a, Variable b) {
		// Example meters * inch, has to be converted to same base before multiplication for correct area
		if (isCompatible(a, b)) {
			Type newType = Type.isBaseType(a.getType(), b.getType());
			double newValue = a.getBaseValue() * b.getBaseValue();
			return new Variable(newType, newValue);
		}
		// Example kg * meter, has to create a new unit/Type
		else {
			Type newType = Type.multiply(a.getType(), b.getType());
			double newValue = a.getBaseValue() / b.getBaseValue();
			return new Variable(newType, newValue);
		}
	}
	
	
	public static Variable divide(Variable a, Variable b) {
		// Example meters / second, has to be converted to same base before multiplication for correct velocity
		if (isCompatible(a, b)) {
			Type newType = Type.isBaseType(a.getType(), b.getType());
			double newValue = a.getBaseValue() / b.getBaseValue();
			return new Variable(newType, newValue);
		}
		// Example kg / meter, has to create a new unit/Type
		else {
			Type newType = Type.divide(a.getType(), b.getType());
			double newValue = a.getBaseValue() / b.getBaseValue();
			return new Variable(newType, newValue);
		}
	}
	
	public static Variable add(Variable a, Variable b) {
		if (isCompatible(a, b)) {
			Type newType = Type.isBaseType(a.getType(), b.getType());
			double newValue = a.getBaseValue() + b.getBaseValue();
			return new Variable(newType, newValue);
		}
		// NULL flags this operation for Visitor to check. Might be correct if ( 1 redPotatoe + 1 bluePotatoe = 2 Potatoes)
		else {
			double newValue = a.getBaseValue() + b.getBaseValue();
			return new Variable(null, newValue);
		}
	}
		
		
	public static Variable subtract(Variable a, Variable b) {
		if (isCompatible(a, b)) {
			Type newType = Type.isBaseType(a.getType(), b.getType());
			double newValue = a.getBaseValue() - b.getBaseValue();
			return new Variable(newType, newValue);
		}
		// NULL flags this operation for Visitor to check. Might be correct if ( 1 redPotatoe + 1 bluePotatoe = 2 Potatoes)
		else {
			double newValue = a.getBaseValue() - b.getBaseValue();
			return new Variable(null, newValue);
		}
	}
	
	
	public static boolean isCompatible(Variable a, Variable b) {
		return Type.isCompatible(a.getType(), b.getType());
	}


	
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Variable [");
		if (type != null) {
			builder.append("type=");
			builder.append(type);
			builder.append(", ");
		}
		builder.append("value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
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
