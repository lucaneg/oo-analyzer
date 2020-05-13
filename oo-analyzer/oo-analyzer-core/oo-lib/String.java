class String {
	/* yields the empty string */
	String() {
	}

	/* clones this string */
	String(String other) {
	}

	/* yields the length of this string */
	int length() {
		return 0;
	}

	/* translates this string into an integer */
	int toInt() {
		return 0;
	}

	/* translates this string into a float */
	float toFloat() {
		return 0;
	}

	/* tests string equality */
	boolean equals(String other) {
		return false;
	}
	
	/* tests string contains */
	boolean contains(String other) {
		return false;
	}
	
	/* tests string startsWith */
	boolean startsWith(String other) {
		return false;
	}
	
	/* tests string endsWith */
	boolean endsWith(String other) {
		return false;
	}

	/* yields the concatenation of this and then s */
	String concat(String s) {
		return "";
	}

	/* yields the concatenation of this and then f */
	String concat(float f) {
		return "";
	}

	/* yields the concatenation of this and then i */
	String concat(int i) {
		return "";
	}

	/* yields the concatenation of this and then b */
	String concat(boolean b) {
		return "";
	}

	/* yields the substring from start (included) {to end (excluded) { */
	String substring(int start, int end) {
		return "";
	}
	
	String replace(String toReplace, String str) {
		return "";
	}
	
	int indexOf(String sub) {
		return 0;
	}
}