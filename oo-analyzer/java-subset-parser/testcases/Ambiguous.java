class Ambiguous {
	Ambiguous() {
	}

	Ambiguous(int i, float d) {
	}

	Ambiguous(float d, int i) {
	}

	/*
	 * if you comment the following line, the constructor call becomes unambiguous
	 */
	Ambiguous(int i2, int i2) {
	}

	void main() {
		Object o = new Ambiguous(3, 4);
	}
}
