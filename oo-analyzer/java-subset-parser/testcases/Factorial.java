class Factorial {
	Factorial() {
	}

	void main() {
		Factorial l0 = new Factorial();
		l0.fact(10);
		int l1 = l0.test(5);
	}

	int fact(int x) {
		if (x <= 0)
			return 1;
		else {
			x = x - 1;
			return 1 + this.fact(x);
		}
	}

	int test(int x) {
		x = x + 5;
		return x;
	}
}