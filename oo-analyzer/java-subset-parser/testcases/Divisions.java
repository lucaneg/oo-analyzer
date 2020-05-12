class Divisions {

	void main() {
		int a = 2;
		int b = 3;

		for (int c = 0; c < b; c = c + 1)
			a = a / b;

		if (a != 0)
			a = b / a;
		else
			a = b / a;
	}
}