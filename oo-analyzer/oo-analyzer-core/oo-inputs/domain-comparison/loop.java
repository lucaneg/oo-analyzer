class Loop {
	void loop(int i) {
		String result = "aa";
		while (i < 10) {
			result = result.concat("bb");
			i = i + 1;
		}

		assert (result.contains("a"));
		assert (result.contains("b")); // KO - might
		assert (result.contains("c")); // KO
	}
}