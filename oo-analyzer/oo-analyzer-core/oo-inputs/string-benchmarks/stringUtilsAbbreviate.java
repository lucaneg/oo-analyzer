class StringUtilsAbbreviate {
	void doAbbreviate() {
		String stm = "this is an example string";
		int max = 10;
		if (stm.length() > max) {
			int lim = max - 3;
			stm = stm.substring(0, lim);
			stm = stm.concat("...");
		}
		
		assert (stm.endsWith("..."));
		assert (stm.endsWith("string")); // KO
	}
	
	void doNotAbbreviate() {
		String stm = "this is an example string";
		int max = 100;
		if (stm.length() > max) {
			int lim = max - 3;
			stm = stm.substring(0, lim);
			stm = stm.concat("...");
		}
		
		assert (stm.endsWith("...")); // KO
		assert (stm.endsWith("string"));
	}
	
	void abbreviate(String stm) {
		int max = 10;
		if (stm.length() > max) {
			int lim = max - 3;
			stm = stm.substring(0, lim);
			stm = stm.concat("...");
		}
		
		assert (stm.endsWith("...")); // KO - might
		assert (stm.endsWith("string")); // KO - might
	}
}