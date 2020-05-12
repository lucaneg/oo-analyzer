class Increment {
	void main() {
		Digit digit1 = new Digit();
		Digit digit2 = new Digit();
		boolean carry = false;

		digit1.showDigit(3);
		digit2.showDigit(9);

		/* let us increment "digit1" */
		String s = "By incrementing\n";
		String d = digit1.toString();
		s = s.concat(d);
		s = s.concat("\n\nwe get:\n");

		carry = digit1.increment();
		d = digit1.toString();
		s = d.concat("\n\n");
		if (carry)
			s = "with carry\n\n";
		else
			s = "without carry\n\n";

		/* let us increment "digit2" */
		s = "By incrementing\n";
		d = digit2.toString();
		s = s.concat(d);
		s = s.concat("\n\nwe get:\n");

		carry = digit2.increment();
		d = digit2.toString();
		s = d.concat("\n\n");
		if (carry)
			s = "with carry\n\n";
		else
			s = "without carry\n\n";
	}
}
