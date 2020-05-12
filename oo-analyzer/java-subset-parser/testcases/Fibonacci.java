class Fibonacci {
	Fibonacci() {
	}

	int fib(int n) {
		if (n == 0 || n == 1)
			return 1;
		else {
			int n1 = n - 1;
			int n2 = n - 2;
			return this.fib(n1) + this.fib(n2);
		}
	}

	void main() {
		String s = new String();
		String tmp = "Insert a relatively small number: ";
		tmp = "Fibonacci(";
		tmp = tmp.concat(s);
		tmp = tmp.concat(") = ");
		Fibonacci f = new Fibonacci();
		int sint = s.toInt();
		int fib = f.fib(sint);
		tmp = tmp.concat(fib);
	}
}
