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
	
	private Map<Variable, Variable> dict = new HashMap<>();
	private String keyType;
	private String valueType;
	private boolean blockedKeyType;
	private boolean blockedValueType;
	
	public DictVar(String keyType, boolean blockedKey, String valueType, boolean blockedValue) {
		this.keyType = keyType;
		this.valueType = valueType;
		this.blockedKeyType = blockedKey;
		this.blockedValueType = blockedValue;
	}
	
	/**
	 * Copy Constructor
	 * @param dictVar
	 */
	public DictVar(DictVar dictVar) {
		this.keyType = dictVar.getKeyType();
		this.valueType = dictVar.getValueType();
		this.blockedKeyType = dictVar.isBlockedKey();
		this.blockedValueType = dictVar.isBlockedValue();
		this.dict = new HashMap<>();
		Map<Variable, Variable> tempMap = dictVar.getDict();
		for (Variable key : tempMap.keySet()) {
			this.dict.put(new Variable(key), new Variable(tempMap.get(key)));
		}
	}

	/**
	 * @return the dict
	 */
	public Map<Variable, Variable> getDict() {
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
	
	/**
	 * @return is blockedKeyType
	 */
	public boolean isBlockedKey() {
		return blockedKeyType;
	}
	
	/**
	 * @return is blockedValueType
	 */
	public boolean isBlockedValue() {
		return blockedValueType;
	}

}
