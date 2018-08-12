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
