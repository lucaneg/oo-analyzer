class StringUtilsAbbreviate {
	String doAbbreviate() {
		String stm = "this is an example string";
		int max = 10;
		if (stm.length() <= max)
			return stm;
		else { 
			int lim = max - 3;
			stm = stm.substring(0, lim);
			stm = stm.concat("...");
			return stm;
		}
	}
	
	String doNotAbbreviate() {
		String stm = "this is an example string";
		int max = 100;
		if (stm.length() <= max)
			return stm;
		else { 
			int lim = max - 3;
			stm = stm.substring(0, lim);
			stm = stm.concat("...");
			return stm;
		}
	}
	
	String abbreviate(String stm) {
		int max = 10;
		if (stm.length() <= max)
			return stm;
		else { 
			int lim = max - 3;
			stm = stm.substring(0, lim);
			stm = stm.concat("...");
			return stm;
		}
	}
}