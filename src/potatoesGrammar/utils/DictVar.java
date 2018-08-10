package potatoesGrammar.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>DictVar</b><p>
 * 
 * @author Luis Moura
 * @version August 2018
 */
public class DictVar {
	
	private Map<Object, Object> dict = new HashMap<>();
	private String keyType;
	private String valueType;
	
	public DictVar(String keyType, String valueType) {
		this.keyType = keyType;
		this.valueType = valueType;
	}

	/**
	 * @return the dict
	 */
	public Map<Object, Object> getDict() {
		return dict;
	}

	/**
	 * @return the keyType
	 */
	public String getKeyType() {
		return keyType;
	}

	/**
	 * @return the valueType
	 */
	public String getValueType() {
		return valueType;
	}
	
	

}
