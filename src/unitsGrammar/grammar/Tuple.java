package unitsGrammar.grammar;


public class Tuple extends Pair<Unit,Double>{
	

	public Tuple(Unit unit, Double factor) {
		super(unit, factor);
	}
	
	
	public Unit getUnit() {
		return getFirst();
	}
	
	public Double getFactor() {
		return getSecond();
	}

	@Override
	public String toString() {
		return "Tuple [first=" + getFirst() + ", second=" + getSecond() + "]";
	}

	
	
	

}
