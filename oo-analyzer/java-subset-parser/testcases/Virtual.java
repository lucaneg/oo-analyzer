class Virtual {
	Virtual() {
	}

	void main() {
    Virtual v = new Virtual();
    A a = new A();
    B b = new B();
    S s = new S();
    v.print(a);
    v.print(b);
    v.print(s);
  }

	void print(S s) {
		String ss = s.toString();
	}
}
