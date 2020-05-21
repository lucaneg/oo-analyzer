class Top {
	void top(String value) {
		String result = "10 times: ";
		for (int i = 0; i < 10; i = i + 1) { 
			result = result.concat(value);
			result = result.concat("!");
		}
		
		assert (result.contains("t")); 
		assert (result.contains("!")); 
		assert (result.contains("f")); // KO - migh
		assert (result.contains("w")); // KO - migh
	}
}