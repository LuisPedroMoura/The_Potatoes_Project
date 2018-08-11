package potatoesGrammar.utils;

import java.util.ArrayList;
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
	 * @return the list
	 */
	public List<Variable> getList() {
		return list;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @return is blocked
	 */
	public boolean isBlocked() {
		return blocked;
	}
	
	
	

}
