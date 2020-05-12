class Strings {
	void main() {
		String s = "ciao";
		s = s.concat("ciao");
		assert (s.equals("ciaociao"));
		s = s.concat("miao"); 
		assert (s.equals("ciaociao")); // KO
		assert (s.contains("ci"));
		assert (s.contains("pi")); // KO
		assert (s.contains("oc"));
	}
	
	void foo1(int par, String init) {
		String result = "result";
		for (int i = 0; i < 10; i = i + 1) { 
			result = result.concat("id=");
			result = result.concat(init);
		}
		
		assert (result.contains("f")); // KO
	}
	
	void foo2(int par) {
		String result = "";
		for (int i = 0; i < 10; i = i + 1) { 
			if (i == 3)
				result = result.concat("foo");
			else
				result = result.concat("faa");
		}
		
		assert (result.contains("f")); // KO
	}
	
	void foo3(int par) {
		String result = "f";
		for (int i = 0; i < 10; i = i + 1) { 
			if (i == 3)
				result = result.concat("foo");
			else
				result = result.concat("faa");
		}
		
		assert (result.contains("f"));
	}
	
	void whileloop(int par) {
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
		assert(result.contains("ri")); // KO
		assert(result.startsWith("ova"));
		assert(result.startsWith("ovari")); // KO
		assert(result.startsWith("ovafa")); // KO
		
		result = result.concat("ok");
		assert(result.endsWith("riuok")); // KO
		assert(result.endsWith("falok")); // KO
		assert(result.endsWith("ok"));
	}
}