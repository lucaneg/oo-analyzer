class Test {
	Test() {
	}

	int loop(int x) {
		if (x < 0)
			return x;
		/* else if (x > 10) then return 1 + this.loop(x - 2) */
		else {
			x = x - 1;
			this.loop(x);
			x = x - 2;
			return 1 + this.loop(x);
		}
	}

	int loop2(int x) {
		int i = 0;

		for (i = 0; i < x; i = i + 1)
			i = i + 1;

		return i;
	}

	void main() {
		Test t = new Test();
		t.loop(10);
	}
}
