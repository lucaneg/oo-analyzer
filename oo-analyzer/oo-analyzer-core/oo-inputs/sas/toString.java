class ToString {
	void toString(String[] names) {
		String result = "People: {";
		int i = 0;
		while (i < this.length(names)) {
			String name = names[i];
			result = result + name;
			if (i != this.length(names) - 1)
				result = result + ",";
			i = i + 1;
		}
		result = result + "}";
		assert(result.contains("People"));
		assert(result.contains(",")); // might fail
		assert(result.contains("not")); // might fail
	}
}
