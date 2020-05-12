class ForeignSubs extends Subs {
	ForeignSubs() {
	}

	int monthlyCost() {
		return this.channels * 2;
	}

	ForeignSubs foreigners() {
		ForeignSubs out = new ForeignSubs();
		Subs temp = this.next;
		if (temp != null)
			out.next = temp.foreigners();
		out.channels = this.channels;
		return out;
	}
}
