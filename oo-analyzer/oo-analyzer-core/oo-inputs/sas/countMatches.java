class CountMatches {
	
	void countMatches(String sub, boolean nondet) {
		String str = "";
		if (nondet) 
			str = "this is the thing";
		else 
			str = "the thing";
		String sub = "th";
        int count = 0;
        int len = sub.length(); // the match
        while (str.contains(sub)) { // (th + )e match
        	int idx = str.indexOf(sub);
            count = count + 1;
            int start = idx + len;
            int end = str.length();
            str = str.substring(start, end); // e match + match
        }
        
		assert (count > 0); 
		assert (count == 0); // always fails
		assert (count == 3); // might fail
    }
}