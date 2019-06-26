package potatoesGrammar.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <b>ListVar</b><p>
 */
public class ListVar {
	
	private List<Variable> list = new ArrayList<>();
	private String type;
	private boolean blocked;
	
	public ListVar(String type, boolean blocked) {
		this.type = type;
		this.blocked = blocked;
	}
	
	/**
	 * 
	 * Copy Constructor
	 * @param listVar
	 */
	public ListVar(ListVar listVar) {
		this.type = listVar.getType();
		this.blocked = listVar.isBlocked();
		this.list = new ArrayList<>();
		for (Variable var : listVar.getList()) {
			this.list.add(new Variable(var));
		}
	}

	/**
	 * @return the list
	 */
	public List<Variable> getList() {
		return list;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		if (type.charAt(0) == '?') {
			return type.substring(1, type.length());
		}
		return type;
	}
	
	public String getType_() {
		return type;
	}
	
	/**
	 * @return is blocked
	 */
	public boolean isBlocked() {
		return blocked;
	}


	@Override
	public String toString() {
		
		StringBuilder str = new StringBuilder();
		
		str.append("[");
		
		Iterator<Variable> it = list.iterator();
		while (it.hasNext()) {
			
			Variable var = it.next();
			if (var.isNumeric()) {
				str.append((Double) var.getValue() + " " + var.getUnit().getSymbol());
			}
			else if (var.isString()) {
				str.append(((String) var.getValue()).toString());
			}
			else if (var.isList()) {
				str.append(((ListVar) var.getValue()).toString());
			}
			else if (var.isDict()) {
				str.append(((DictVar) var.getValue()).toString());
			}
			else {
				str.append(((Boolean) var.getValue()).toString());
			}
			
			if (it.hasNext()) {
				str.append(", ");
			}
		}
		
		str.append("]");
		
		return str.toString();
	}
}
