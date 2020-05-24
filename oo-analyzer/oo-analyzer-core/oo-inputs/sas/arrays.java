class Arrays {
	void toString(String[] names) {
		String result = "People: {";
		
		for (int i = 0; i < this.length(names); i = i + 1) {
			String name = names[i];
			result = result + name;
			
			if (i != this.length(names) - 1)
				result = result + ",";
		}
		result = result + "}";
		
		assert(result.contains("People"));
		assert(result.contains("}"));
		assert(result.contains(",")); // might fail
		assert(result.contains("not")); // might fail
	}
}