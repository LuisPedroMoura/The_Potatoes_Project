package utils;

/**
 * <b>Factor</b><p>
 * 
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class Factor {

	// Instance Fields
	private final Double factor;
	private final Boolean isParentToChild;
	/**
	 * Constructor
	 * @param string 
	 * @param factor 
	 */
	public Factor(Double factor, String string) {
		if (string.trim().toLowerCase().equals("parent")) {
			this.factor = factor;
			isParentToChild = true;
		}
		else {
			this.factor = 1/factor;
			isParentToChild = false;
		}
	}

	// Getters
	public Double getFactor() {
		return factor;
	}

	public Boolean getIsParentToChild() {
		return isParentToChild;
	}

	// Other Methods
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Factor [");
		if (factor != null) {
			builder.append("factor=");
			builder.append(factor);
			builder.append(", ");
		}
		if (isParentToChild != null) {
			builder.append("isParentToChild=");
			builder.append(isParentToChild);
		}
		builder.append("]");
		return builder.toString();
	}




}
