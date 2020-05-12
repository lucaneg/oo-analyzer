class Binary {
	boolean head;
	Binary tail;

	Binary(boolean head, Binary tail) {
		this.head = head;
		this.tail = tail;
	}

	Binary(int n) {
		this.head = n / 2 * 2 != n;
		if (n > 1) {
			int m = n / 2;
			this.tail = new Binary(m);
		}
	}

	String toString() {
		if (this.tail != null && this.head) {
			Binary b = this.tail;
			String s = b.toString();
			return s.concat(1);
		} else if (this.tail != null) {
			Binary b = this.tail;
			String s = b.toString();
			return s.concat(0);
		} else if (this.head)
			return "1";
		else
			return "0";
	}

	void main() {
		Binary b = new Binary(815);
		String s = b.toString();
		s = s.concat("\n");
	}
}