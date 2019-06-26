package potatoesGrammar.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <b>DictVar</b><p>
 * 
 * @author Luis Moura
 * @version August 2018
 */
public class DictVar {
	
	private Map<Variable, Variable> dict = new LinkedHashMap<>();
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
	
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("[");
		
		Iterator<Variable> it = dict.keySet().iterator();
		while (it.hasNext()) {
			
			Variable key = it.next();
			Variable val = dict.get(key);
			
			if (key.isNumeric()) {
				str.append(((Double) key.getValue()) + " " + key.getUnit().getSymbol() );
			}
			else if (key.isString()){
				str.append(((String) key.getValue()));
			}
			else {
				str.append(((Boolean) key.getValue()));
			}
			
			str.append(" -> ");
			
			if (val.isNumeric()) {
				str.append(((Double) val.getValue()) + " " + val.getUnit().getSymbol() );
			}
			else if (val.isString()){
				str.append(((String) val.getValue()));
			}
			else {
				str.append(((Boolean) val.getValue()));
			}
			
			if (it.hasNext()) {
				str.append(", ");
			}
		}
		
		str.append("]");
		
		return str.toString();
	}

}
