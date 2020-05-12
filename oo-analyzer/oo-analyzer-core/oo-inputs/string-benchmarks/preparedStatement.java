class PreparedStatement {
	String prepare(String first, String second) {
		String stm = "UPDATE user_table SET name=%1 WHERE id=%2";
		stm = stm.replace("%1", "the name");
		stm = stm.replace("%2", "345");
		return stm;
	}
}