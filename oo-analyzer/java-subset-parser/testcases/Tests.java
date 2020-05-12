class Tests {

	Tests() {
	}

	void main() {
		Tests t = new Tests();
		int l1 = t.test1(10);
		int l2 = t.test2(20);
		int l3 = t.test3(8, 9);
		int l4 = t.fact(8);
	}

	int test1(int x) {
		return x + 5;
	}

	int test2(int x) {
		if (x <= 0)
			return 1;
		else {
			x = x - 1;
			return 1 + this.test2(x);
		}
	}

	int test3(int x, int y) {
		if (x == 0 || y == 0)
			return x + y;
		else {
			x = x - 1;
			y = y - 1;
			return 2 + this.test3(x, y);
		}
	}

	int fact(int x) {
		if (x == 0)
			return x + 5;
		else {
			x = x - 1;
			return 3 + this.test3(x, x);
		}
	}
}