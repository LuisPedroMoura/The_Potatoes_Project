package potatoesGrammar.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>ListVar</b><p>
 * 
 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class ListVar {
	
	private List<Object> list = new ArrayList<>();
	private String type;
	
	public ListVar(String type) {
		this.type = type;
	}

	/**
	 * @return the list
	 */
	public List<Object> getList() {
		return list;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	
	

}
