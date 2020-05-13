class PreparedStatement {
	String prepare() {
		String stm = "UPDATE user_table SET name=%1 WHERE id=%2";
		stm = stm.replace("%1", "john");
		stm = stm.replace("%2", "345");
		return stm;
	}
	
	String prepare(String first, String second) {
		String stm = "UPDATE user_table SET name=%1 WHERE id=%2";
		stm = stm.replace("%1", first);
		stm = stm.replace("%2", second);
		return stm;
	}
}