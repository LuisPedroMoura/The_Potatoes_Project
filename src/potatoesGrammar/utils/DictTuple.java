package potatoesGrammar.utils;

/**
 * <b>DictTuple</b><p>
 * 
 * @author Luis Moura
 * @version August 2018
 */
public class DictTuple {
	
	private Object key;
	private Object value;
	
	public DictTuple(Object key, Object value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public Object getKey() {
		return key;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	
	
}
