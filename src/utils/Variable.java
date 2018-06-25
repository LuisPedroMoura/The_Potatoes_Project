package utils;

import java.util.Collection;
import java.util.List;

import compiler.PotatoesVisitorSemanticAnalysis;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;

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
	
	private Type type;
	private double value;
	private static Graph<Type, Factor> typesGraph = PotatoesVisitorSemanticAnalysis.getTypesFileInfo().getTypesGraph();
	private static DijkstraShortestPath<Type, Factor> dijkstra = new DijkstraShortestPath<>(typesGraph);

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
	
	public static Variable power(Variable a, Variable b) {
		
		// Variable b type==number is already checked in Visitor
		
		Double newValue = Math.pow(a.getValue(), b.getValue());
		Double newCode = Math.pow(a.getType().getCode(), b.getValue());
		Type newType = new Type ("", "", newCode); // Type names don't need to be corrected, assignment will resolve
		Variable res = new Variable(newType, newValue);
		return res;
	}
	
	
	public boolean convertTypeTo(Type newType) {
		// verify that newType exists
		if (!typesGraph.containsVertex(newType)) {
			return false;
		}
		
		// Variable type is already the one we're trying to convert to
		if (newType.getCode() == this.type.getCode()){
			return true;
		}
		
		// get path from graph
		List<Factor> factors;
		try {
			factors = dijkstra.getPath(this.type, newType);
		}
		catch (IllegalArgumentException e) {
			return false;
		}
		
		// convert value using edges cost
		for(Factor f : factors) {
			this.value *= f.getFactor();
		}
		
		// convert code to type code
		this.type = newType;
		return true;
	}
	
	public boolean convertTypeToFirstUncheckedTypeInOpTypeArray(Type type) {
		List<Type> unchecked = type.getUncheckedOpTypes();
		
		for (Type t : unchecked) {
			if(convertTypeTo(t)) {
				type.checkType(t);
				return true;
			}
		}
		return false;
	}

	public boolean convertTypeToFirstPossibleTypeInOpTypeArrayOf(Type destinationType) {
		List<Type> opTypes = destinationType.getOpTypes();
		
		for (Type t : opTypes) {
			if (convertTypeTo(t)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean convertTypeToMaxParentType() {
		Type parent = this.type;
		boolean hasParent = true;
		
		while(hasParent) {
			List<Factor> edges = (List<Factor>) typesGraph.getOutEdges(parent);
			for (Factor f : edges) {
				if (f.getIsChildToParent() == true) {
					parent = typesGraph.getDest(f);
					break;
				}
			}
			hasParent = false;	
		}
		return true;
	}
	
	public boolean MultDivCheckConvertType(Type destinationType) throws Exception {
		boolean checkType = false;
		boolean convertToUnchecked = false;
		boolean convertToFirstPossible = false;
		
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
		this.convertTypeToMaxParentType();
		
		throw new Exception();
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
