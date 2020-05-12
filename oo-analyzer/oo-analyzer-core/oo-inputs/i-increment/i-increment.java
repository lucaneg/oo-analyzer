class Increment {
	void main() {
		int i = 0;
		i = i + 1;
		assert (i == 1);
		assert (i == 2);
		i = (i + 1) * 2;
		assert (i == 3);
	}

	void minus() {
		int i = 5;
		int j = -i;
		assert (i != j);
		assert (i > 0);
		assert (j < 0);
		assert (j > 0);
	}

	void branch() {
		int i = 5;
		int j = -i;

		if (j > 0) {
			assert (j > 0);
			assert (i == 0);
			i = 9;
			assert (i == 9);
		} else
			assert (j < 0);
	}

	void forloop() {
		int i = 5;
		int j = -i;

		for (int k = 0; k < 100; k = k + 1) {
			assert (j > 0);
			assert (i == 0);
			i = 9;
			assert (i == 9);
			j = j + k;
		}

		assert (j < 0);
	}

	void whileloop() {
		int i = 5;
		int j = -i;

		while (j > 0) {
			assert (j > 0);
			assert (i == 0);
			i = 9;
			assert (i == 9);
			j = j + 1;
		}

		assert (j < 0);
	}
}