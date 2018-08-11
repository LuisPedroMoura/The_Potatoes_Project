package potatoesGrammar.utils;

public enum varType {
	
	BOOLEAN, STRING, LIST, TUPLE, DICT, NUMERIC;
	
	public boolean isBoolean() {
		if (this.ordinal() == 0)
			return true;
		return false;
	}
	
	public boolean isString() {
		if (this.ordinal() == 1)
			return true;
		return false;
	}
	
	public boolean isList() {
		if (this.ordinal() == 2)
			return true;
		return false;
	}
	
	public boolean isTuple() {
		if (this.ordinal() == 3)
			return true;
		return false;
	}
	
	public boolean isDict() {
		if (this.ordinal() == 4)
			return true;
		return false;
	}
	
	public boolean isNumeric() {
		if (this.ordinal() == 5)
			return true;
		return false;
	}

	}
	
}
