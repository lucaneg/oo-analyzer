class Substring {
	void substring(int i) {
		String result = "substring test";
		if (i % 2 == 0)
			result = result.concat(" passed");
		else
			result = result.concat(" failed");
		
		result = result.substring(5, 18);
		assert (result.contains("g"));
		assert (result.contains("p")); // KO - might
		assert (result.contains("f")); // KO - might
		assert (result.contains("d")); // KO
	}
}