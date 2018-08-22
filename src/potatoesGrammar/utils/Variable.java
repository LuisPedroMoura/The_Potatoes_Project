/***************************************************************************************
*	Title: PotatoesProject - Variable Class Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Acknowledgments for version 1.0: Pedro Teixeira (https://pedrovt.github.io),
*	Maria Jo�o Lavoura (https://github.com/mariajoaolavoura), for the help in
*	brainstorming the concepts needed to create the first working version of this Class
*	that could deal with different unit Variables operations.
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package potatoesGrammar.utils;

import compiler.PotatoesSemanticCheck;
import unitsGrammar.grammar.Graph;
import unitsGrammar.grammar.Unit;
import unitsGrammar.grammar.Units;
import utils.errorHandling.ErrorHandling;

/**
 * <b>Variable</b><p>
 * To be used on the general purpose language<p>
 * For example, an instruction like {@code distance x = (distance) 5} will create an instance of this object with 
 * Unit {@code distance} (if the unit exists in the Units table) and value {@code 5}.<p>
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class Variable {

	// Static Constant (Debug Only)
	private static final boolean debug = false;
	
	

	// --------------------------------------------------------------------------
	// Static Fields
	// FIXME colocar aqui o Grafo que já vem tratado no GraphInfo. Verificar onde tem de se ir buscar.
	//private static Graph unitsGraph =  PotatoesSemanticCheck.getUnitsFileInfo().getGraphInfo().getStraightfowardPathsCostsGraph();
	
	// --------------------------------------------------------------------------
	// Instance Fields
	private Unit unit;
	private varType varType;
	private Object value;

	// --------------------------------------------------------------------------
	// CTORS

	/**
	 * @param unit
	 * @param value
	 */
	public Variable(Unit unit, varType varType, Object value) {
		this.unit = unit;
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
		
		// deep copy Unit
		if (a.getUnit() == null){
			this.unit = null;
		}
		else {
			this.unit = Units.instanceOf(a.getUnit().getName());
		}
		
		// deep copy varType Enum (immutable)
		this.varType = a.getVarUnit();
		
		// deep copy value object
		if (a.getValue() == null) {
			this.value = null;
		}
		if (value instanceof Double) {
			this.value = a.getValue();
		}
		if (value instanceof String) {
			this.value = a.getValue();
		}
		if (value instanceof Boolean) {
			this.value = a.getValue();
		}
		if (value instanceof DictVar) {
			this.value = new DictVar((DictVar) a.getValue());
		}
		if (value instanceof ListVar) {
			this.value = new ListVar((ListVar) a.getValue());
		}
		if (value instanceof DictTuple) {
			this.value = new DictTuple((DictTuple) a.getValue());
		}
	}

	// --------------------------------------------------------------------------
	// Getters & Setters

	/**
	 * @return unit
	 */
	public Unit getUnit() {
		return unit;
	}

	/**
	 * @return unit
	 */
	public Object getValue() {
		return value;
	}
	
	public varType getVarUnit() {
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
			Unit newUnit = Units.multiply(a.getUnit(), b.getUnit());
			double codeSimplificationFactor = newUnit.adjustUnitOperationResultToKnowUnit(unitsGraph);
			double newValue = (double) a.getValue() * (double) b.getValue() * codeSimplificationFactor;
			return new Variable(newUnit, potatoesGrammar.utils.varType.NUMERIC, newValue);
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
			Unit newUnit = Unit.divide(a.getUnit(), b.getUnit());
			double codeSimplificationFactor = newUnit.adjustUnitOperationResultToKnowUnit(unitsGraph);
			double newValue = (double) a.getValue() / (double)b.getValue() * codeSimplificationFactor;
			
			return new Variable(newUnit, potatoesGrammar.utils.varType.NUMERIC, newValue);
		}
		throw new IllegalArgumentException();
	}
	
	
	public static Variable mod(Variable a, Variable b) {
		if (a.isNumeric() && b.isNumeric()) {
			// verify that Variable b is of Unit number
			if (!b.getUnit().getUnitName().equals("number")) {
				throw new IllegalArgumentException();
			}
			double newValue = (double) a.getValue() % (double) b.getValue();
			return new Variable(a.getUnit(), potatoesGrammar.utils.varType.NUMERIC, newValue);
		}
		throw new IllegalArgumentException();
	}

	/**
	 * @return new Variable with same code and value
	 */
	public static Variable add(Variable a, Variable b) {
		if (a.isNumeric() && b.isNumeric()) {
			if (!a.getUnit().equals(b.getUnit())) {
				double conversionFactor = unitsGraph.getEdge(a.getUnit(), b.getUnit());
				if (conversionFactor == Double.POSITIVE_INFINITY) {
					throw new IllegalArgumentException();
				}
				double newValue = ((double) a.getValue() * conversionFactor) + (double) b.getValue();
				return new Variable(b.getUnit(), potatoesGrammar.utils.varType.NUMERIC, newValue);
			}
			double newValue = (double) a.getValue() + (double) b.getValue();
			return new Variable(b.getUnit(), potatoesGrammar.utils.varType.NUMERIC, newValue);
		}
		throw new IllegalArgumentException();
	}

	/**
	 * @return new Variable with same code and value
	 */
	public static Variable subtract(Variable a, Variable b) {
		if (a.isNumeric() && b.isNumeric()) {
			if (!a.getUnit().equals(b.getUnit())) {
				double conversionFactor = unitsGraph.getEdge(a.getUnit(), b.getUnit());
				if (conversionFactor == Double.POSITIVE_INFINITY) {
					throw new IllegalArgumentException();
				}
				double newValue = ((double) a.getValue() * conversionFactor) - (double) b.getValue();
				return new Variable(b.getUnit(), potatoesGrammar.utils.varType.NUMERIC, newValue);
			}
			double newValue = (double) a.getValue() - (double) b.getValue();
			return new Variable(a.getUnit(), potatoesGrammar.utils.varType.NUMERIC, newValue);
		}
		throw new IllegalArgumentException();
	}

	/**
	 * @return new Variable with same code and multiplied value
	 */
	public static Variable simetric(Variable a) {
		if (a.isNumeric()) {
			double newValue = (double) a.getValue() * -1;
			return new Variable(a.getUnit(), potatoesGrammar.utils.varType.NUMERIC, newValue);
		}
		throw new IllegalArgumentException();
	}

	/**
	 * @return new Variable with new code and multiplied value
	 */
	public static Variable power(Variable a, Variable b) {
		if (a.isNumeric() && b.isNumeric()) {
			if (b.getUnit().getUnitName().equals("number")){
				Unit newUnit = Unit.power(a.getUnit(), (int) b.getValue());
				Double newValue = Math.pow((double) a.getValue(), (double) b.getValue());
				return new Variable(newUnit, potatoesGrammar.utils.varType.NUMERIC, newValue);
			}
		}
		throw new IllegalArgumentException();
	}

	/**
	 * @return true if unit is compatible with this.unit
	 */
	public boolean unitIsCompatible(Variable a){
		
		if (this.isNumeric() && a.isNumeric()) {
			
			// if units are equals -> compatible
			if(this.getUnit().equals(a.getUnit())) {
				return true;
			}
			
			// if one of the units in not in graph -> not compatible
			if(!unitsGraph.containsVertex(this.unit) || !unitsGraph.containsVertex(unit)) {
				return false;
			}
			
			// if there is no path between the units. It means the conversion is not possible -> not compatible
			double conversionFactor = unitsGraph.getEdge(this.unit, unit);
			if (!(conversionFactor == Double.POSITIVE_INFINITY)) {
				return true;
			}
			
		}
		
		else if (this.varType == a.getVarUnit()) {
			return true;
		}
		
		return false;
	}

	/**
	 * @param	newUnit the Unit that is Variable is to be converted to
	 * @return	true if this.unit is converted to newUnit
	 * 		 	false if the conversion is not possible (there's no path that connects the Vertices in the Graph)
	 */
	public Double convertUnitTo(Unit newUnit) {
		
		// if unit is numeric, attempting conversion is possible -> verify
		if (this.isNumeric()) {
			
			// variable unit is already the one we're trying to convert to -> conversion not needed
			if (newUnit.equals(this.unit)){
				if(debug) {ErrorHandling.printInfo("CONVERT_TYPE_TO - same unit no convertion needed");}
				return 1.0;
			}
			
			// if one of the units is number, conversion is straightfoward. unit number is not on the graph. 
			if (this.getUnit().getUnitName().equals("number") || newUnit.getUnitName().equals("number")) {
				this.unit = newUnit;
				return 1.0;
			}
			
			// if one of the units is not in the Graph -> conversion not possible
			System.out.println("\n" + unitsGraph + "\n");
			System.out.println(this.unit + "\n");
			System.out.println(newUnit + "\n");
			System.out.println(unitsGraph.containsVertex(newUnit));
			System.out.println(unitsGraph.containsVertex(this.unit));
			if (!unitsGraph.containsVertex(newUnit) || !unitsGraph.containsVertex(this.unit)) {
				if(debug) {ErrorHandling.printInfo("CONVERT_TYPE_TO - not contained in graph");}			
				throw new IllegalArgumentException();
			}
	
			if (debug) {
				ErrorHandling.printInfo("BEFORE TRYING TO FIND PATH");
				ErrorHandling.printInfo("Trying to convert " + this.unit.getUnitName() + " to " + newUnit.getUnitName());
			}
			
			// if there is no path between the units -> conversion not possible
			double conversionFactor = unitsGraph.getEdge(this.unit, newUnit);
			if (conversionFactor == Double.POSITIVE_INFINITY) {
				throw new IllegalArgumentException();
			}
	
			if (debug) {
				ErrorHandling.printInfo("AFTER TRYING TO FIND PATH");
				ErrorHandling.printInfo(unitsGraph.toString());
			}
			
			// at this point, conversion is possible -> do necessary calculations
			Double val = (Double) this.value;
			val *= conversionFactor;
			value = val;
			this.unit = newUnit;
	
			if(debug) {ErrorHandling.printInfo("CONVERT_TYPE_TO - converted");}	
	
			return conversionFactor;
		}
		
		// this varType is not NUMERIC -> conversion not possible
		throw new IllegalArgumentException();

	}
	
	/**
	 * @param a, a Variable
	 * @param b, a VAriable
	 * @return the convertion factor between a unit and b unit if compatible
	 */
	public static double pathCost(Variable a, Variable b) {
		double conversionFactor = unitsGraph.getEdge(a.getUnit(), b.getUnit());
		if (conversionFactor == Double.POSITIVE_INFINITY) {
			throw new IllegalArgumentException();
		}
		return conversionFactor;
	}

	// --------------------------------------------------------------------------
	// Other Methods	

	@Override
	public String toString() {
		return "value " + value + ", unit " + unit;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
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
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	
}
