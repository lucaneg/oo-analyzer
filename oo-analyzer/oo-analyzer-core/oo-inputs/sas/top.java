class Top {
	void top(String value, int nondet) {
		String result = "Repeat: ";
		for (int i = 0; i < nondet; i = i + 1) 
			result = result + value  + "!";
		
		assert (result.contains("t")); 
		assert (result.contains("!")); // might fail
		assert (result.contains("f")); // might fail
		assert (result.contains("w")); // might fail
	}
}