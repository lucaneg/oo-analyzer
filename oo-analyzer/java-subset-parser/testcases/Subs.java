class Subs {
	int channels;
	Subs next;

	Subs() {
	}

	Subs(int channels, Subs next) {
		this.channels = channels;
		this.next = next;
	}

	int monthlyCost() {
		return this.channels / 2;
	}

	ForeignSubs foreigners() {
		Subs temp = this.next;
		if (temp == null)
			return null;
		else
			return temp.foreigners();
	}

	void main() {
		Subs copy = new Subs();
		copy = copy.foreigners();
	}
}
