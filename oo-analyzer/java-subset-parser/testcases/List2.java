class List2 {

	Object head;

	List2 tail;

	List2(Object head, List2 tail) {
		this.head = head;
		this.tail = tail;
	}

	/* yields a list which is the concatenation of this and other */
	List2 append(List2 l1) {
		if (this.tail == null)
			return new List2(this.head, l1);
		else {
			/* this.share(l1); */
			List2 l = this.tail;
			l = l.append(l1);
			return new List2(this.head, l);
			/* return this.tail.append(l1) */
		}
	}

	void share(List2 other) {
		other.tail = this;
	}

	/* naive reverse */
	List2 reverse() {
		if (this.tail == null)
			return this;
		else {
			List2 l = this.tail;
			l = l.reverse();
			List2 l1 = new List2(this.head, null);
			return l.append(l1);
		}
	}

	/* accumulator reverse */
	List2 reverseAcc() {
		List2 l1 = new List2(this.head, null);
		return this.reverseAux(l1);
	}

	List2 reverseAux(List2 acc) {
		if (this.tail == null)
			return acc;
		else {
			List2 l = this.tail;
			List2 l1 = new List2(this.head, acc);
			return l.reverseAux(l1);
		}
	}

	/* iterative length */
	int length() {
		List2 l1 = this;
		int l2 = 0;

		while (l1 != null) {
			l2 = l2 + 1;
			l1 = l1.tail;
		}

		return l2;
	}

	List2 clone() {
		if (this.tail == null)
			return new List2(this.head, null);
		else {
			List2 l = this.tail;
			l = l.clone();
			return new List2(this.head, l);
		}
	}

	void main() {
		Object o1 = new Object();
		Object o2 = new Object();
		List2 l0 = new List2(o1, null);
		List2 l1 = new List2(o2, null);
		l1.tail = l1;
		List2 l2 = l0.append(l1);
		List2 l3 = l0.reverse();
		List2 l4 = l0.reverseAcc();
		int l5 = l0.length();
		List2 l6 = l0.clone();
	}
}
