class StringUtilsCountMatches {
	
	int countMatches() {
		String str = "this is a search string that we use to test the mathces count";
		String sub = "th";
        int count = 0;
        int idx = 0;
        int len = sub.length();
        while (!str.equals("")) {
        	idx = str.indexOf(sub);
        	if (idx == -1)
        		return count;
            count = count + 1;
            str = str.substring(0, idx);
            idx = idx + len;
        }
        return count;
    }
}