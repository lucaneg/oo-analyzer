class PreparedStatement {
	void prepare() {
		String stm = "UPDATE user_table SET name=%1 WHERE id=%2";
		stm = stm.replace("%1", "john");
		stm = stm.replace("%2", "345");
		
		assert(stm.contains("%1")); // KO
		assert(stm.contains("345"));
		assert(stm.contains("surely not contained")); // KO
	}
	
	void prepare(String first, String second) {
		String stm = "UPDATE user_table SET name=%1 WHERE id=%2";
		stm = stm.replace("%1", first);
		stm = stm.replace("%2", second);
		
		assert(stm.contains("%1")); // KO - might
		assert(stm.contains("345")); // KO - might
		assert(stm.contains("surely not contained")); // KO - might
	}
}