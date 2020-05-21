class Strings {
	void concat() {
		String s = "ciao";
		s = s.concat("ciao");
		assert (s.equals("ciaociao"));
		s = s.concat("miao"); 
		assert (s.equals("ciaociao")); // KO
		assert (s.contains("ci"));
		assert (s.contains("pi")); // KO
		assert (s.contains("oc"));
	}
	
	void replace() {
		String stm = "test string";
		stm = stm.replace("str", "fff");
		assert(stm.contains("str")); // KO
		assert(stm.contains("fff"));
	}
	
	void loopWithTop(String init) {
		String result = "result";
		for (int i = 0; i < 10; i = i + 1) { 
			result = result.concat("id=");
			result = result.concat(init);
		}
		
		assert (result.contains("f")); // KO
	}
	
	void loopStartingAtEmpty() {
		String result = "";
		int y = 0;
		for (int i = 0; i < 10; i = i + 1) {
			y = y + 1;
			if (i == 3)
				result = result.concat("foo");
			else
				result = result.concat("faa");
		}
		
		assert (result.contains("f")); // KO - might
	}
	
	void loopStartingAtNonEmpty() {
		String result = "f";
		int i = 0;
		while (i < 10) { 
			if (i == 3)
				result = result.concat("foo");
			else
				result = result.concat("faa");
			
			i = i + 1;
		}
		
		assert (result.contains("f"));
	}
	
	void substring(int i) {
		String result = "prova";
		if (i % 2 == 0)
			result = result.concat("riuscita");
		else
			result = result.concat("fallita");
		
		result = result.substring(2, 8);
		assert(result.contains("ova"));
		assert(result.contains("ri")); // KO - might
		assert(result.startsWith("ova"));
		assert(result.startsWith("ovari")); // KO - might
		assert(result.startsWith("ovafa")); // KO - might
		
		result = result.concat("ok");
		assert(result.endsWith("riuok")); // KO - might
		assert(result.endsWith("falok")); // KO - might
		assert(result.endsWith("ok"));
	}
}