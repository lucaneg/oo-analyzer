class Nullness {
	Nullness f;
	Nullness g;

	Nullness() {
	}

	Nullness getF() {
		return this.f;
	}

	int test1() {
		return 13;
	}

	int test2() {
		return 17;
	}

	int test3() {
		return 30;
	}

	int test4() {
		return 50;
	}

	int test5() {
		return 60;
	}

	void test6(Nullness n) {
		Nullness p = new Nullness();
		p.f = n;
		Nullness field = p.f;
		field.test1();
	}

	void main() {
		Nullness n = new Nullness();
		Nullness field;
		if (n.f != null) {
			field = n.f;
			field.test1();
		}
		if (n.getF() != null) {
			field = n.getF();
			field.test2();
		}
		n.f = new Nullness();
		field = n.f;
		field.test3();
		field.g = new Nullness();
		field = field.g;
		field.test4();
		field = n.g;
		field = field.f;
		field.test4();
		field = n.g;
		field = field.g;
		field = field.f;
		Nullness result = n.test7();
		field.test8(result);
		result = n.test7();
		result.test9();
		n.f = null;
		field = n.f;
		field.test5();
		result = new Nullness();
		n.test6(result);
	}

	Nullness test7() {
		this.f = new Nullness();
		return this.f;
	}

	void test8(Nullness n) {
	}

	void test9() {
	}
}