package unitsGrammar.grammar;


public class Tuple<T,V> extends Pair<T,V>{
	
	

	/**
	 * Constructor
	 * @param first
	 * @param second
	 */
	public Tuple(T first, V second) {
		super(first, second);
	}
	
	
	public T getName() {
		return getFirst();
	}
	
	public V getFactor() {
		return getSecond();
	}


	@Override
	public String toString() {
		return "Tuple []";
	}
	
	

}
