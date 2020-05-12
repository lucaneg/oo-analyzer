class List {

	int head;
	List tail;

	List(int head, List tail) {
		this.head = head;
		this.tail = tail;
	}

	/* yields a list which is the concatenation of this and other */
	List append(List l1) {
		if (this.tail == null)
			return new List(this.head, l1);
		else {
			List l = this.tail;
			l = l.append(l1);
			return new List(this.head, l);
		}
	}

	/* naive reverse */
	List reverse() {
		if (this.tail == null)
			return this;
		else {
			List l = this.tail;
			l = l.reverse();
			List l1 = new List(this.head, null);
			return l.append(l1);
		}
	}

	/* accumulator reverse */
	List reverseAcc() {
		List l1 = new List(this.head, null);
		return this.reverseAux(l1);
	}

	List reverseAux(List acc) {
		if (this.tail == null)
			return acc;
		else {
			List l = this.tail;
			List l1 = new List(this.head, acc);
			return l.reverseAux(l1);
		}
	}

	/* iterative length */
	int length() {
		List l1 = this;
		int l2 = 0;

		while (l1 != null) {
			l2 = l2 + 1;
			l1 = l1.tail;
		}

		return l2;
	}

	List clone() {
		if (this.tail == null)
			return new List(this.head, null);
		else {
			List l = this.tail;
			l = l.clone();
			return new List(this.head, l);
		}
	}

	void main() {
		List l0 = new List(3, null);
		List l1 = new List(5, null);
		l1.tail = l1;
		List l2 = l0.append(l1);
		List l3 = l0.reverse();
		List l4 = l0.reverseAcc();
		int l5 = l0.length();
		List l6 = l0.clone();
	}
}
