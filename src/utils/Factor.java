package utils;

/**
 * 
 * <b>Factor</b><p>
 * Each instance of this class contains a Double representing the convertion factor between two 
 * types and a flag indicating whether that factor represents the convertion from Type a to Type b
 * (Parent -> Child) or from Type b to Type a (Child -> Parent)
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class Factor {

	// Instance Fields
	private final Double factor;
	private final Boolean isParentToChild;

	// CTORs
	/**
	 * Constructor
	 * @param string {@code parent} it's a factor from {@code Type a} to {@code Type b}, else {@code child}
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
	/**
	 * 
	 * @return factor
	 */
	public Double getFactor() {
		return factor;
	}

	/**
	 * 
	 * @return {@code true} if factor is for Parent -> Child, else {@code false}
	 */
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
