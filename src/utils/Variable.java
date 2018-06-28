package utils;

import java.util.List;

import compiler.PotatoesSemanticCheck;

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

	// Static Field (Debug Only)
	private static final boolean debug = true;
	
	private Type type;
	private double value;
	private static Graph typesGraph = PotatoesSemanticCheck.getTypesFileInfo().getTypesGraph();


	// --------------------------------------------------------------------------
	// CTORS

	/**
	 * @param type
	 * @param value
	 */
	public Variable(Type type, double value) {
		this.type = type;
		this.value = value;
	}
	
	/** 
	   * 
	   * Copy Constructor 
	   * @param a 
	   * @throws NullPointerException if a is null (ie new Variable (null)) 
	   */ 
	  public Variable(Variable a) { 
	    this.type = new Type(a.type); 
	    this.value  = a.value; 
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
	
	/**
	 * @return new Variable with new code and multiplied value
	 */
	public static Variable power(Variable a, Variable b) {

		// Variable b type==number is already checked in Visitor

		Double newValue = Math.pow(a.getValue(), b.getValue());
		Double newCode = Math.pow(a.getType().getCode(), b.getValue());
		Type newType = new Type ("temp", "", newCode); // Type names don't need to be corrected, assignment will resolve
		Variable res = new Variable(newType, newValue);
		return res;
	}
	
	/**
	 * @return true if type is compatible with this.type
	 */
	public boolean typeIsCompatible(Type type){
		if(this.getType().getCode() == type.getCode()) {
			return true;
		}
		
		// verify thar types exist in graph
		if(!typesGraph.containsVertex(this.type) || !typesGraph.containsVertex(type)) {
			return false;
		}
		
		return typesGraph.isCompatible(this.type, type);
	}
	
	/**
	 * @return true if this.type is converted to newType, false
	 */
	public boolean convertTypeTo(Type newType) {

		// variable type is already the one we're trying to convert to
		if (newType.getCode() == this.type.getCode()){
		if(debug) {System.out.println("CONVERT_TYPE_TO - same type no convertion needed");}	
			return true;
		}

		// verify that newType exists and its not number
		if (!newType.getTypeName().equals("number")) {
			if (!typesGraph.containsVertex(newType) || ! typesGraph.containsVertex(this.getType())) {
				if(debug) {System.out.println("CONVERT_TYPE_TO - not contained in graph");	}			
				return false;
			}
		}

		// get path from graph
		double factors;
		
		// verify thar types exist in graph
		if(!typesGraph.containsVertex(this.type) || !typesGraph.containsVertex(newType)) {
			return false;
		}
		
		// always reset factor before calculating path
		Graph.resetFactor();
		factors = typesGraph.getPath(this.type, newType);
		factors = Graph.getPathFactor();
		typesGraph.clearVisited();
		System.err.println("Final Factor is: "+ factors);
		typesGraph.printGraph();
		
		// calculate new value using convertion factors
		this.value *= factors;

		// convert code to type code
		this.type = newType;
		if(debug) {System.out.println("CONVERT_TYPE_TO - converted");	}		
		return true;
	}
	
	/**
	 * When making operations with different dimensions its necessary
	 * to convert the variables type to the ones that compose the type
	 * they are being assign to. 
	 * @param type
	 * @return true if converted
	 */
	public boolean convertTypeToFirstUncheckedTypeInOpTypeArray(Type type) {
		List<Type> unchecked = type.getUncheckedOpTypes();
		if(debug) { System.out.print("Trying to check an unchecked type. List is ");
			for (Type utype : unchecked) {
				System.out.print(utype.getTypeName());
			}
			System.out.println();
		}
		for (Type t : unchecked) {
			if(convertTypeTo(t)) {
				type.checkType(t);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * When a variable is not converted in the previous methods, its necessary
	 * to try to convert all extra variables to the same type. So converting to the
	 * first possible case, guarantees that alike types will cancel if the oportunity
	 * is presented 
	 * @param destinationType
	 * @return
	 */
	public boolean convertTypeToFirstPossibleTypeInOpTypeArrayOf(Type destinationType) {
		List<Type> opTypes = destinationType.getOpTypes();
		if(debug) { System.out.print("Trying to convert to first possible checked. List is ");
			for (Type utype : opTypes) {
				System.out.print(utype.getTypeName());
			}
			System.out.println();
		}
		for (Type t : opTypes) {
			if (convertTypeTo(t)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * When the previous two methods fail to convert a variable type, it means
	 * the dimention is not compatible with neither the variable to be assigned
	 * nor any of the types that compose it.
	 * In that case, the conversion is done to the highest hieralchical parent.
	 * The attempt is made hopping to maximize the change of cancelation futher 
	 * in the calculations.
	 * The result is garanteed when there is simple inheritance, but may or may not
	 * be resolved if there is multiple inheritance.
	 * @param destinationType
	 * @return
	 * @throws Exception
	 */
//	public boolean convertTypeToMaxParentType() {
//		Type parent = this.type;
//		boolean hasParent = true;
//
//		while(hasParent) {
//			List<Factor> edges = (List<Factor>) typesGraph.getOutEdges(parent);
//			for (Factor f : edges) {
//				if (f.getIsChildToParent() == true) {
//					parent = typesGraph.getDest(f);
//					if(debug) {System.out.println(parent + " -> ");}
//					break;
//				}
//			}
//			hasParent = false;	
//		}
//		return true;
//	}
	
	/**
	 * This method manages the ink bewtween the previous three methods in order
	 * to maximize correct results in calculations
	 * @param destinationType
	 * @return
	 * @throws Exception
	 */
	public boolean MultDivCheckConvertType(Type destinationType) throws Exception {
		boolean typeIsCompatible = false;
		boolean checkType = false;
		boolean convertToUnchecked = false;
		boolean convertToFirstPossible = false;

		typeIsCompatible = convertTypeTo(destinationType);

		// check if destinatioType and variable are of the same dimention
		// as the asignement may be to a OR Type, and a cast might be applies,
		// converting to the destinationType is necessary
		if (typeIsCompatible == true) {
			return true;
		}

		checkType = destinationType.checkType(this.type);

		// check this type in destinationType checkList. This type does not change
		// else, if direct check is not possible try to convert this type to one of the unchecked types
		if (checkType == true) {
			return true;
		}
		convertToUnchecked = this.convertTypeToFirstUncheckedTypeInOpTypeArray(destinationType);

		// if this type was converted to an unchecked type, return true
		// else try to to convert to first possible checked type in destinationType checkList
		// this step warrants that COMPATIBLE units with multiple inheritance can be resolved
		if (convertToUnchecked == true) {
			return true;
		}
		convertToFirstPossible = this.convertTypeToFirstPossibleTypeInOpTypeArrayOf(destinationType);

		// if this type was converted to an checked type, retrun true
		// else, means that the type is NOT COMPATIBLE with any destinationType type in checklist
		// still, instances with simple inheritance will still be resolved
		// instances with multiple inheritance may or may not be resolved correctly (but errors will be detected)
		if (convertToFirstPossible == true) {
			return true;
		}
		
		
		
		//this.convertTypeToMaxParentType();
		
		throw new Exception();
	}



	// --------------------------------------------------------------------------
	// OTHER METHODS	

	@Override
	public String toString() {
		return "value " + value + ", type " + type;
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
