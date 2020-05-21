class Choice {
	void choice(int i) {
		String result = "test ";
		if (i % 2 == 0)
			result = result.concat("passed");
		else
			result = result.concat("failed");

		assert (result.contains("t"));
		assert (result.contains("f")); // KO - might
		assert (result.contains("p")); // KO - might
		assert (result.contains("z")); // KO
	}
}