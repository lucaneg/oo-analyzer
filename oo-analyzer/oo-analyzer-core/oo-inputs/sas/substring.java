class Substring {
	void substring(boolean nondet) {
		String result = "substring test";
		if (nondet)
			result = result + " passed";
		else
			result = result + " failed";
		
		result = result.substring(5, 18);
		assert (result.contains("g"));
		assert (result.contains("p")); // might fail
		assert (result.contains("f")); // might fail
		assert (result.contains("d")); // always fails
	}
}