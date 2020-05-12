class Divisions2 {

	void main() {
		int a = 4;
		int b = 6;

		for (int c = a; c > 0; c = c - 1)
			if (c - 1 != 0)
				a = b / (c - 1);

		if (a == 0)
			a = b / (a - b);
		else
			a = b / (a - b);
	}
}