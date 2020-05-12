class Hanoi {
	Hanoi() {
	}

	void solve(int h, int from, int to, int support) {
		if (h < 1)
			return;
		else if (h == 1) {
			String s = "from ";
			s = s.concat(from);
			s = s.concat(" to ");
			s = s.concat(to);
			s = s.concat("\n");
		} else {
			h = h - 1;
			this.solve(h, from, support, to);
			String s = "from ";
			s = s.concat(from);
			s = s.concat(" to ");
			s = s.concat(to);
			s = s.concat("\n");
			this.solve(h, support, to, from);
		}
	}

	void main() {
		Hanoi h = new Hanoi();
		h.solve(5, 1, 2, 3);
	}
}
