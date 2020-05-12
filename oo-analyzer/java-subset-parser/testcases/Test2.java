class Test2 {
	Test2 f;

	Test2(Test2 f) {
		this.f = f;
	}

	Test2() {
	}

	void main() {
		Test2 inner = new Test2();
		Test2 t = new Test2(inner);
		inner = t.f;
		inner.f = t;
	}
}