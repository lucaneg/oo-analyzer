class Loop {
	void main() {
		int i = 0;
		int j = 0;

		while (i < 10)
			i = i + 1;

		for (i = 0; i < 10; i = i + 2) {
			j = 1;
			while (j < 3)
				j = j + 1;
			i = i + 3;
		}
	}
}
