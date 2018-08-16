package potatoesGrammar.utils;

public enum varType {
	
	BOOLEAN, STRING, LIST, TUPLE, DICT, NUMERIC, VOID;
	
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
	
	public boolean isVoid() {
		if (this.ordinal() == 6)
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		switch (this) {
		case BOOLEAN	:	return "boolean";
		case STRING		:	return "string";
		case LIST		:	return "list";
		case DICT		:	return "dict";
		case TUPLE		:	return "tuple";
		case VOID		:	return "void";
		default			:	return "numeric";
		}
	}
	
}
