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
	private varType varType;
	private Object value;

	// --------------------------------------------------------------------------
	// CTORS

	/**
	 * @param type
	 * @param value
	 */
	public Variable(Type type, varType varType, Object value) {
		this.type = type;
		this.varType = varType;
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
		this.varType = a.getVarType();
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
	public Object getValue() {
		String typeName = type.getTypeName();
		switch (typeName) {
			case "boolean"	:	return (Boolean) value;
			case "string"	:	return (String) value;
			case "list"		:	return (ListVar) value;
			case "tuple"	:	return (DictTuple) value;
			case "dict"		:	return (DictVar) value;
			default			:	return (Double) value;
		}
	}
	
	public varType getVarType() {
		return this.varType;
	}
	
	public boolean isBoolean() {
		if (varType == potatoesGrammar.utils.varType.BOOLEAN)
			return true;
		return false;
	}
	
	public boolean isString() {
		if (varType == potatoesGrammar.utils.varType.STRING)
			return true;
		return false;
	}
	
	public boolean isList() {
		if (varType == potatoesGrammar.utils.varType.LIST)
			return true;
		return false;
	}
	
	public boolean isTuple() {
		if (varType == potatoesGrammar.utils.varType.TUPLE)
			return true;
		return false;
	}
	
	public boolean isDict() {
		if (varType == potatoesGrammar.utils.varType.DICT)
			return true;
		return false;
	}
	
	public boolean isNumeric() {
		if (varType == potatoesGrammar.utils.varType.NUMERIC)
			return true;
		return false;
	}

	// --------------------------------------------------------------------------
	// Operations with Variables

	/**
	 * @return new Variable with new code and value
	 */
	public static Variable multiply(Variable a, Variable b) {
		if (a.isNumeric() && b.isNumeric()) {
			Type newType = Type.multiply(a.getType(), b.getType());
			double codeSimplificationFactor = newType.adjustTypeOperationResultToKnowType(typesGraph);
			double newValue = (double) a.getValue() * (double) b.getValue() * codeSimplificationFactor;
			return new Variable(newType, potatoesGrammar.utils.varType.NUMERIC, newValue);
		}
		throw new IllegalArgumentException();
	}

	/**
	 * @return new Variable with new code and value
	 */
	public static Variable divide(Variable a, Variable b) {
		if (a.isNumeric() && b.isNumeric()) {
			if ((double)b.getValue() == 0.0) {
				throw new ArithmeticException();
			}
			Type newType = Type.divide(a.getType(), b.getType());
			double codeSimplificationFactor = newType.adjustTypeOperationResultToKnowType(typesGraph);
			double newValue = (double) a.getValue() / (double)b.getValue() * codeSimplificationFactor;
			
			return new Variable(newType, potatoesGrammar.utils.varType.NUMERIC, newValue);
		}
		throw new IllegalArgumentException();
	}
	
	
	public static Variable mod(Variable a, Variable b) {
		if (a.isNumeric() && b.isNumeric()) {
			// verify that Variable b is of Type number
			if (!b.getType().getTypeName().equals("number")) {
				throw new IllegalArgumentException();
			}
			double newValue = (double) a.getValue() % (double) b.getValue();
			return new Variable(a.getType(), potatoesGrammar.utils.varType.NUMERIC, newValue);
		}
		throw new IllegalArgumentException();
	}

	/**
	 * @return new Variable with same code and value
	 */
	public static Variable add(Variable a, Variable b) {
		if (a.isNumeric() && b.isNumeric()) {
			if (!a.getType().equals(b.getType())) {
				double conversionFactor = typesGraph.getEdgeCost(typesGraph.getEdge(a.getType(), b.getType()));
				if (conversionFactor == Double.POSITIVE_INFINITY) {
					throw new IllegalArgumentException();
				}
				double newValue = ((double) a.getValue() * conversionFactor) + (double) b.getValue();
				return new Variable(b.getType(), potatoesGrammar.utils.varType.NUMERIC, newValue);
			}
			double newValue = (double) a.getValue() + (double) b.getValue();
			return new Variable(b.getType(), potatoesGrammar.utils.varType.NUMERIC, newValue);
		}
		throw new IllegalArgumentException();
	}

	/**
	 * @return new Variable with same code and value
	 */
	public static Variable subtract(Variable a, Variable b) {
		if (a.isNumeric() && b.isNumeric()) {
			if (!a.getType().equals(b.getType())) {
				double conversionFactor = typesGraph.getEdgeCost(typesGraph.getEdge(a.getType(), b.getType()));
				if (conversionFactor == Double.POSITIVE_INFINITY) {
					throw new IllegalArgumentException();
				}
				double newValue = ((double) a.getValue() * conversionFactor) - (double) b.getValue();
				return new Variable(b.getType(), potatoesGrammar.utils.varType.NUMERIC, newValue);
			}
			double newValue = (double) a.getValue() - (double) b.getValue();
			return new Variable(a.getType(), potatoesGrammar.utils.varType.NUMERIC, newValue);
		}
		throw new IllegalArgumentException();
	}

	/**
	 * @return new Variable with same code and multiplied value
	 */
	public static Variable simetric(Variable a) {
		if (a.isNumeric()) {
			double newValue = (double) a.getValue() * -1;
			return new Variable(a.getType(), potatoesGrammar.utils.varType.NUMERIC, newValue);
		}
		throw new IllegalArgumentException();
	}

	/**
	 * @return new Variable with new code and multiplied value
	 */
	public static Variable power(Variable a, Variable b) {
		if (a.isNumeric() && b.isNumeric()) {
			if (b.getType().getTypeName().equals("number")){
				Type newType = Type.power(a.getType(), (int) b.getValue());
				Double newValue = Math.pow((double) a.getValue(), (double) b.getValue());
				return new Variable(newType, potatoesGrammar.utils.varType.NUMERIC, newValue);
			}
		}
		throw new IllegalArgumentException();
	}

	/**
	 * @return true if type is compatible with this.type
	 */
	public boolean typeIsCompatible(Variable a){
		
		if (this.isNumeric() && a.isNumeric()) {
			
			// if types are equals -> compatible
			if(this.getType().equals(a.getType())) {
				return true;
			}
			
			// if one of the types in not in graph -> not compatible
			if(!typesGraph.containsVertex(this.type) || !typesGraph.containsVertex(type)) {
				return false;
			}
			
			// if there is no path between the types. It means the conversion is not possible -> not compatible
			double conversionFactor = typesGraph.getEdgeCost(typesGraph.getEdge(this.type, type));
			if (!(conversionFactor == Double.POSITIVE_INFINITY)) {
				return true;
			}
			
		}
		
		else if (this.varType == a.getVarType()) {
			return true;
		}
		
		return false;
	}

	/**
	 * @param	newType the Type that is Variable is to be converted to
	 * @return	true if this.type is converted to newType
	 * 		 	false if the conversion is not possible (there's no path that connects the Vertices in the Graph)
	 */
	public Double convertTypeTo(Type newType) {
		
		// if type is numeric, attempting conversion is possible -> verify
		if (this.isNumeric()) {
			
			// variable type is already the one we're trying to convert to -> conversion not needed
			if (newType.equals(this.type)){
				if(debug) {ErrorHandling.printInfo("CONVERT_TYPE_TO - same type no convertion needed");}
				return 1.0;
			}
			
			// if one of the types is number, conversion is straightfoward. type number is not on the graph. 
			if (this.getType().getTypeName().equals("number") || newType.getTypeName().equals("number")) {
				this.type = newType;
				return 1.0;
			}
			
			// if one of the types is not in the Graph -> conversion not possible
			if (!typesGraph.containsVertex(newType) || !typesGraph.containsVertex(this.type)) {
				if(debug) {ErrorHandling.printInfo("CONVERT_TYPE_TO - not contained in graph");}			
				throw new IllegalArgumentException();
			}
	
			if (debug) {
				ErrorHandling.printInfo("BEFORE TRYING TO FIND PATH");
				ErrorHandling.printInfo("Trying to convert " + this.type.getTypeName() + " to " + newType.getTypeName());
			}
			
			// if there is no path between the types -> conversion not possible
			double conversionFactor = typesGraph.getEdgeCost(typesGraph.getEdge(this.type, newType));
			if (conversionFactor == Double.POSITIVE_INFINITY) {
				throw new IllegalArgumentException();
			}
	
			if (debug) {
				ErrorHandling.printInfo("AFTER TRYING TO FIND PATH");
				ErrorHandling.printInfo(typesGraph.toString());
			}
			
			// at this point, conversion is possible -> do necessary calculations
			Double val = (Double) this.value;
			val *= conversionFactor;
			value = val;
			this.type = newType;
	
			if(debug) {ErrorHandling.printInfo("CONVERT_TYPE_TO - converted");}	
	
			return conversionFactor;
		}
		
		// this varType is not NUMERIC -> conversion not possible
		throw new IllegalArgumentException();

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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable other = (Variable) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	
}
