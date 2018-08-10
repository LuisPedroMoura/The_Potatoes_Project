/***************************************************************************************
*	Title: PotatoesProject - Variable Class Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Acknowledgments for version 1.0: Pedro Teixeira (https://pedrovt.github.io),
*	Maria Jo�o Lavoura (https://github.com/mariajoaolavoura), for the help in
*	brainstorming the concepts needed to create the first working version of this Class
*	that could deal with different type Variables operations.
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package potatoesGrammar.utils;

import compiler.PotatoesSemanticCheck;
import typesGrammar.utils.HierarchyDiGraph;
import typesGrammar.utils.Type;
import utils.errorHandling.ErrorHandling;

/**
 * <b>Variable</b><p>
 * To be used on the general purpose language<p>
 * For example, an instruction like {@code distance x = (distance) 5} will create an instance of this object with 
 * Type {@code distance} (if the type exists in the Types table) and value {@code 5}.<p>
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class Variable {

	// Static Constant (Debug Only)
	private static final boolean debug = false;

	// --------------------------------------------------------------------------
	// Static Fields
	// FIXME colocar aqui o Grafo que já vem tratado no GraphInfo. Verificar onde tem de se ir buscar.
	private static HierarchyDiGraph<Type,Double> typesGraph =  PotatoesSemanticCheck.getTypesFileInfo()
																					.getGraphInfo()
																					.getShortestPathsGraph();

	// --------------------------------------------------------------------------
	// Instance Fields
	private Type type;
	private Double value;

	// --------------------------------------------------------------------------
	// CTORS

	/**
	 * @param type
	 * @param value
	 */
	public Variable(Type type, Double value) {
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
	// Getters & Setters

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
	// Operations with Variables

	/**
	 * @return new Variable with new code and value
	 */
	public static Variable multiply(Variable a, Variable b) {
		Type newType = Type.multiply(a.getType(), b.getType());
		double codeSimplificationFactor = newType.adjustTypeOperationResultToKnowType(typesGraph);
		double newValue = a.getValue() * b.getValue() * codeSimplificationFactor;
		return new Variable(newType, newValue);
	}

	/**
	 * @return new Variable with new code and value
	 */
	public static Variable divide(Variable a, Variable b) {
		if (b.getValue() == 0.0) {
			throw new ArithmeticException();
		}
		Type newType = Type.divide(a.getType(), b.getType());
		double codeSimplificationFactor = newType.adjustTypeOperationResultToKnowType(typesGraph);
		double newValue = a.getValue() / b.getValue() * codeSimplificationFactor;
		
		return new Variable(newType, newValue);
	}
	
	
	public static Variable mod(Variable a, Variable b) {
		// verify that Variable b is of Type number
		if (!b.getType().getTypeName().equals("number")) {
			throw new IllegalArgumentException();
		}
		return new Variable(a.getType(), a.getValue()%b.getValue());
	}

	/**
	 * @return new Variable with same code and value
	 */
	public static Variable add(Variable a, Variable b) {
		if (!a.getType().equals(b.getType())) {
			double conversionFactor = typesGraph.getEdgeCost(typesGraph.getEdge(a.getType(), b.getType()));
			if (conversionFactor == Double.POSITIVE_INFINITY) {
				throw new IllegalArgumentException();
			}
			double newValue = (a.getValue() * conversionFactor) + b.getValue();
			return new Variable(b.getType(), newValue);
		}
		double newValue = a.getValue() + b.getValue();
		return new Variable(b.getType(), newValue);
	}

	/**
	 * @return new Variable with same code and value
	 */
	public static Variable subtract(Variable a, Variable b) {
		if (!a.getType().equals(b.getType())) {
			double conversionFactor = typesGraph.getEdgeCost(typesGraph.getEdge(a.getType(), b.getType()));
			if (conversionFactor == Double.POSITIVE_INFINITY) {
				throw new IllegalArgumentException();
			}
			double newValue = (a.getValue() * conversionFactor) - b.getValue();
			return new Variable(b.getType(), newValue);
		}
		double newValue = a.getValue() - b.getValue();
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

		// Variable b type==number is already checked in Syntactic Analysis
		Type newType = Type.power(a.getType(), (int)b.getValue());
		Double newValue = Math.pow(a.getValue(), b.getValue());
		return new Variable(newType, newValue);
	}

	/**
	 * @return true if type is compatible with this.type
	 */
	public boolean typeIsCompatible(Type t){
		if(this.getType().getCode() == t.getCode()) {
			return true;
		}

		// verify that types exist in graph
		if(!typesGraph.containsVertex(this.type) || !typesGraph.containsVertex(type)) {
			return false;
		}

		double conversionFactor = typesGraph.getEdgeCost(typesGraph.getEdge(this.type, type));
		// if there is no path between the types. It means the conversion is not possible
		if (conversionFactor == Double.POSITIVE_INFINITY) {
			return false;
		}
		return true;
	}

	/**
	 * @param	newType the Type that is Variable is to be converted to
	 * @return	true if this.type is converted to newType
	 * 		 	false if one of the Types is not defined in the typesTable
	 * @throws	NullPointerException if the conversion is not possible (there's no path that connects the Vertices in the Graph)
	 */
	public boolean convertTypeTo(Type newType) {

		// variable type is already the one we're trying to convert to
		if (newType.getCode().equals(this.type.getCode())){
			if(debug) {ErrorHandling.printInfo("CONVERT_TYPE_TO - same type no convertion needed");}
			return true;
		}

		// verify that this and newType exist in the typesGraph
		if (!typesGraph.containsVertex(newType) || !typesGraph.containsVertex(this.type)) {
			if(debug) {ErrorHandling.printInfo("CONVERT_TYPE_TO - not contained in graph");}			
			throw new NullPointerException();
		}

		if (debug) {
			ErrorHandling.printInfo("BEFORE TRYING TO FIND PATH");
			ErrorHandling.printInfo("Trying to convert " + this.type.getTypeName() + " to " + newType.getTypeName());
		}

		double conversionFactor = typesGraph.getEdgeCost(typesGraph.getEdge(this.type, newType));
		
		// if there is no path between the types. It means the conversion is not possible
		if (conversionFactor == Double.POSITIVE_INFINITY) {
			return false;
		}

		if (debug) {
			ErrorHandling.printInfo("AFTER TRYING TO FIND PATH");
			ErrorHandling.printInfo(typesGraph.toString());
		}
		
		// calculate new value using conversion factor
		this.value *= conversionFactor;

		// convert code to type code
		this.type = newType;

		if(debug) {ErrorHandling.printInfo("CONVERT_TYPE_TO - converted");}	

		return true;

	}
	
	/**
	 * @param a, a Variable
	 * @param b, a VAriable
	 * @return the convertion factor between a type and b type if compatible
	 */
	public static double pathCost(Variable a, Variable b) {
		double conversionFactor = typesGraph.getEdgeCost(typesGraph.getEdge(a.getType(), b.getType()));
		if (conversionFactor == Double.POSITIVE_INFINITY) {
			throw new IllegalArgumentException();
		}
		return conversionFactor;
	}

	// --------------------------------------------------------------------------
	// Other Methods	

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
