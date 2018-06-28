package utils;

/**
 * 
 * <b>Prefix</b><p>
 * 
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class Prefix {

	// Instance Fields
	private String prefixName;
	private String printName;
	private Double value;

	// --------------------------------------------------------------------------
	// CTOR
	/**
	 * 
	 * Constructor
	 * @param prefixName
	 * @param printName
	 * @param value
	 */
	public Prefix(String prefixName, String printName, Double value) {
		this.prefixName = prefixName;
		this.printName  = printName;
		this.value		= value;
	}

	// --------------------------------------------------------------------------
	// Getters
	public String getPrefixName() {
		return prefixName;
	}

	public String getPrintName() {
		return printName;
	}

	public Double getValue() {
		return value;
	}

	// --------------------------------------------------------------------------
	// Other Methods
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prefixName == null) ? 0 : prefixName.hashCode());
		result = prime * result + ((printName == null) ? 0 : printName.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Prefix other = (Prefix) obj;
		if (prefixName == null) {
			if (other.prefixName != null) {
				return false;
			}
		} else if (!prefixName.equals(other.prefixName)) {
			return false;
		}
		if (printName == null) {
			if (other.printName != null) {
				return false;
			}
		} else if (!printName.equals(other.printName)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Prefix [");
		if (prefixName != null) {
			builder.append("prefixName=");
			builder.append(prefixName);
			builder.append(", ");
		}
		if (printName != null) {
			builder.append("printName=");
			builder.append(printName);
			builder.append(", ");
		}
		if (value != null) {
			builder.append("value=");
			builder.append(value);
		}
		builder.append("]");
		return builder.toString();
	}


}
