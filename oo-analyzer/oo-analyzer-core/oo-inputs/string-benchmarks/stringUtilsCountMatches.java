class StringUtilsCountMatches {
	
	int countMatchesWithEquals() {
		String str = "this is a search string that we use to test the matches count";
		String sub = "th";
        int count = 0;
        int len = sub.length();
        while (!str.equals("")) {
        	int idx = str.indexOf(sub);
        	if (idx == -1)
        		return count;
            count = count + 1;
            int start = idx + len;
            int end = str.length();
            str = str.substring(start, end);
        }
        return count;
    }
	
	int countMatchesWithContains(String sub, boolean b) {
		String str = "this is a search string that we use to test the matches count";
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
        return count;
    }
}