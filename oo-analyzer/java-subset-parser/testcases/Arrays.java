class Arrays {
	void main() {
		S[] arr = new S[10];
		for (int i = 0; i < 10; i = i + 1)
			if (i - (i / 3) * 3 == 0)
				arr[i] = new A();
			else if (i - (i / 3) * 3 == 1)
				arr[i] = new B();
			else
				arr[i] = new S();

		int i = 0;
		while (i < 10) {
			S var = arr[i];
			String s = var.toString();
			s = s.concat("\n");
			i = i + 1;
		}
	}
}