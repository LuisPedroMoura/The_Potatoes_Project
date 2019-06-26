package potatoesGrammar.utils;

/**
 * <b>DictTuple</b><p>
 * 
 * @author Luis Moura
 * @version August 2018
 */
public class DictTuple {
	
	private Variable key;
	private Variable value;
	
	public DictTuple(Variable key, Variable value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Copy Constructor
	 * @param tuple
	 */
	public DictTuple(DictTuple tuple) {
		this.key = new Variable(tuple.getKey());
		this.value = new Variable(tuple.getValue());
	}

	/**
	 * @return the key
	 */
	public Variable getKey() {
		return key;
	}

	/**
	 * @return the value
	 */
	public Variable getValue() {
		return value;
	}
	
	
}
