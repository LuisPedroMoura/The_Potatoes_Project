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
	
	
	public boolean convertTypeTo(Type newType) {
		// verify that newType exists
		if (!typesGraph.containsVertex(newType)) {
			return false;
		}
		
		// Variable type is already the one we're trying to convert to
		if (newType.getCode() == this.type.getCode()){
			return true;
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
	
	public boolean MultDivCheckConvertType(Type destinationType) {
		boolean checkType = false;
		boolean convertToUnchecked = false;
		boolean convertToFirstPossible = false;
		
		checkType = destinationType.checkType(this.type);
		
		// check if destinationType has opType of v.getType. If yes Variable is not changed
		// Check destinationType checkList if available. If not, try to convert to unchecked type
		if (checkType == true) {
			return true;
		}
		convertToUnchecked = this.convertTypeToFirstUncheckedTypeInOpTypeArray(destinationType);

		// if it was possible to convert to unchecked type return Variable
		// else to to convert to first possible in list
		// this step warrants that compatible units with multiple Inheritance can be resolved
		if (convertToUnchecked == true) {
			return true;
		}
		convertToFirstPossible = this.convertTypeToFirstPossibleTypeInOpTypeArrayOf(destinationType);
		
		// if method got this far, then teh Variable type is not compatible at all with destinationType
		// still, instances with simple Inheritance will still be resolved
		// instances with multiple inheritance may or may not be resolved correctly (error will be detected)
		if (convertToFirstPossible == true) {
			return true;
		}
		this.convertTypeToMaxParentType();
		
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
