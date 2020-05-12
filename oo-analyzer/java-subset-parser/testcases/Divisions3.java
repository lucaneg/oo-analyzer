class Divisions3 {

	Divisions3() {
	}

	void main() {
    Divisions3 d = new Divisions3();

    d.foo(3,4);
    d.foo(5,0);
    d.foo(0,-7);
    d.foo(0,0);
  }

	void foo(int a, int b) {
		int c = 0;

		if (a * b > 0)
			c = 5 / a + 2 / b;

		if (a + b > 0)
			if (a == 0)
				c = 5 / b;

		if (this.sub(a, b) != 0)
			if (b == 0)
				c = 5 / a;
	}

	int sub(int a, int b) {
		return a - b;
	}
}