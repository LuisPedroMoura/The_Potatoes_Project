/***************************************************************************************
*	Title: PotatoesProject - Variable Class Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Acknowledgments for version 1.0: Pedro Teixeira (https://pedrovt.github.io),
*	Maria Joao Lavoura (https://github.com/mariajoaolavoura), for the help in
*	brainstorming the concepts needed to create the first working version of this Class
*	that could deal with different unit Variables operations.
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package potatoesGrammar.utils;

import unitsGrammar.grammar.Tuple;
import unitsGrammar.grammar.Unit;
import unitsGrammar.grammar.Units;


public class Variable implements Comparable<Variable>{

	// Static Constant (Debug Only)
	private static final boolean debug = false;
	
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
			this.unit = new Unit(a.getUnit());
		}
		
		// deep copy varType Enum (immutable)
		this.varType = a.getVarType();
		
		// deep copy value object
		Object value = a.getValue();
		if (value == null) {
			this.value = null;
		}
		else if (value instanceof Double) {
			this.value = a.getValue();
		}
		else if (value instanceof String) {
			this.value = a.getValue();
		}
		else if (value instanceof Boolean) {
			this.value = a.getValue();
		}
		else if (value instanceof DictVar) {
			this.value = new DictVar((DictVar) a.getValue());
		}
		else if (value instanceof ListVar) {
			this.value = new ListVar((ListVar) a.getValue());
		}
		else if (value instanceof DictTuple) {
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
	public static Variable multiply(Variable a, Variable b) throws IllegalArgumentException{
		if (a.isNumeric() && b.isNumeric()) {
			
			Tuple res = Units.multiply(a.getUnit(), b.getUnit());
			Unit newUnit = res.getUnit();
			double factor = res.getFactor();
			double newValue = (double) a.getValue() * (double) b.getValue() * factor;
			
			factor = newUnit.adjustToKnownUnit();
			newValue *= factor;
			
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
			
			Tuple res = Units.divide(a.getUnit(), b.getUnit());
			Unit newUnit = res.getUnit();
			double factor = res.getFactor();
			double newValue = (double) a.getValue() / (double)b.getValue() * factor;
			
			factor = newUnit.adjustToKnownUnit();
			newValue *= factor;
			
			return new Variable(newUnit, potatoesGrammar.utils.varType.NUMERIC, newValue);
		}
		throw new IllegalArgumentException();
	}
	
	
	public static Variable mod(Variable a, Variable b) {
		if (a.isNumeric() && b.isNumeric()) {
			
			// verify that Variable b is of Unit number
			if (!b.getUnit().getName().equals("number")) {
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

			Tuple res = Units.add(a.getUnit(), b.getUnit());
			Unit newUnit = res.getUnit();
			double factor = res.getFactor();
			double newValue = (double) a.getValue() + (double) b.getValue() * factor;
			
			factor = newUnit.adjustToKnownUnit();
			newValue *= factor;

			return new Variable(newUnit, potatoesGrammar.utils.varType.NUMERIC, newValue);
		}
		throw new IllegalArgumentException();
	}

	/**
	 * @return new Variable with same code and value
	 */
	public static Variable subtract(Variable a, Variable b) {
		if (a.isNumeric() && b.isNumeric()) {
			
			Tuple res = Units.subtract(a.getUnit(), b.getUnit());
			Unit newUnit = res.getUnit();
			double factor = res.getFactor();
			double newValue = (double) a.getValue() - (double) b.getValue() * factor;
			
			factor = newUnit.adjustToKnownUnit();
			newValue *= factor;
			
			return new Variable(newUnit, potatoesGrammar.utils.varType.NUMERIC, newValue);
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
			
			if (b.getUnit().getName().equals("number")){
				
				Tuple res = Units.power(a.getUnit(), ((Double) b.getValue()).intValue());
				Unit newUnit = res.getUnit();
				double factor = res.getFactor();
				Double newValue = Math.pow((double) a.getValue(), (double) b.getValue()) * factor;
				
				factor = newUnit.adjustToKnownUnit();
				newValue *= factor;
				
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
			
			if (this.getUnit().isCompatible(a.getUnit())) {
				return true;
			}
		}
		
		else if (this.varType == a.getVarType()) {
			return true;
		}
		
		return false;
	}

	/**
	 * @param	newUnit the Unit that is Variable is to be converted to
	 * @return	the conversion factor obtained with the conversion
	 * @throws	IllegalArgumentException if the conversion is not possible
	 */
	public Double convertUnitTo(Unit newUnit) throws IllegalArgumentException{
		
		// if unit is numeric, attempting conversion is possible -> verify
		if (this.isNumeric()) {
			
			// if conversion is possible returns conversion factor, else throws IllegalArgumentException
			Double factor = this.unit.matchUnitTo(newUnit);
			if (factor == Double.POSITIVE_INFINITY) {
				throw new IllegalArgumentException();
			}
			this.value = (Double) value * factor;
			return factor;
		}
		
		// this varType is not NUMERIC -> conversion not possible
		throw new IllegalArgumentException();

	}
	

	// --------------------------------------------------------------------------
	// Other Methods	

	@Override
	public String toString() throws IllegalArgumentException{
		
		if (isBoolean()) {
			return ((Boolean) value).toString();
		}
		else if (isString()) {
			return (String) value;
		}
		else if (isNumeric()) {
			return (Double) value + " " + unit.getSymbol();
		}
		else if (isList()) {
			return ((ListVar) value).toString();
		}
		else if (isDict()) {
			return ((DictVar) value).toString();
		}
		else {
			throw new IllegalArgumentException();
		}
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}


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


	@Override
	public int compareTo(Variable other) {
		
		if (this.value instanceof String && other.getValue() instanceof String) {
			return ((String) this.value).compareTo((String) other.getValue());
		}
		
		else if (this.value instanceof Boolean && other.getValue() instanceof Boolean) {
			return ((Boolean) this.value).compareTo((Boolean) other.getValue());
		}
		
		else if (this.value instanceof Double && other.getValue() instanceof Double) {
			
			Variable auxOther = new Variable(other);
			auxOther.convertUnitTo(this.getUnit());
			return ((Double) this.getValue()).compareTo((Double) auxOther.getValue());
		}
		
		else {
			throw new IllegalArgumentException();
		}
	}

	
}
