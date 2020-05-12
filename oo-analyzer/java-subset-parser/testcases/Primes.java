class Primes {
	int next;

	Primes() {
		this.next = 2;
	}

	Primes(int next) {
		this.next = next;
	}

	int getHead() {
		return this.next;
	}

	Primes getTail() {
		int next = this.next + 1;

		while (!this.isPrime(next))
			next = next + 1;

		return new Primes(next);
	}

	boolean isPrime(int n) {
		int divisor = 2;

		while (divisor < n)
			if ((n / divisor) * divisor == n)
				return false;
			else
				divisor = divisor + 1;

		return true;
	}

	void main() {
		Primes p = new Primes();

		for (int count = 1; count <= 100; count = count + 1) {
			String s = "";
			int i = p.getHead();
			s = s.concat(i);
			s = s.concat("\n");
			p = p.getTail();
		}
	}
}