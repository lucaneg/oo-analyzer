class Digit {
	/*
	 * 7 leds let us show all digits as it follows:
	 * 
	 * led1 led2 led3 led4 led5 led6 led7
	 */

	Led led1;
	Led led2;
	Led led3;
	Led led4;
	Led led5;
	Led led6;
	Led led7;
	int digit;

	Digit() {
		this.led1 = new Led();
		this.led2 = new Led();
		this.led3 = new Led();
		this.led4 = new Led();
		this.led5 = new Led();
		this.led6 = new Led();
		this.led7 = new Led();

		this.showDigit(0);
	}

	/* turns leds on or off in order to show a digit */
	void showDigit(int digit) {
		this.digit = digit;

		if (this.digit == 0) {
			Led l = this.led1;
			l.on();
			l = this.led2;
			l.on();
			l = this.led3;
			l.on();
			l = this.led4;
			l.off();
			l = this.led5;
			l.on();
			l = this.led6;
			l.on();
			l = this.led7;
			l.on();
		} else if (this.digit == 1) {
			Led l = this.led1;
			l.off();
			l = this.led2;
			l.off();
			l = this.led3;
			l.on();
			l = this.led4;
			l.off();
			l = this.led5;
			l.off();
			l = this.led6;
			l.on();
			l = this.led7;
			l.off();
		} else if (this.digit == 2) {
			Led l = this.led1;
			l.on();
			l = this.led2;
			l.off();
			l = this.led3;
			l.on();
			l = this.led4;
			l.on();
			l = this.led5;
			l.on();
			l = this.led6;
			l.off();
			l = this.led7;
			l.on();
		} else if (this.digit == 3) {
			Led l = this.led1;
			l.on();
			l = this.led2;
			l.off();
			l = this.led3;
			l.on();
			l = this.led4;
			l.on();
			l = this.led5;
			l.off();
			l = this.led6;
			l.on();
			l = this.led7;
			l.on();
		} else if (this.digit == 4) {
			Led l = this.led1;
			l.off();
			l = this.led2;
			l.on();
			l = this.led3;
			l.on();
			l = this.led4;
			l.on();
			l = this.led5;
			l.off();
			l = this.led6;
			l.on();
			l = this.led7;
			l.off();
		} else if (this.digit == 5) {
			Led l = this.led1;
			l.on();
			l = this.led2;
			l.on();
			l = this.led3;
			l.off();
			l = this.led4;
			l.on();
			l = this.led5;
			l.off();
			l = this.led6;
			l.on();
			l = this.led7;
			l.on();
		} else if (this.digit == 6) {
			Led l = this.led1;
			l.on();
			l = this.led2;
			l.on();
			l = this.led3;
			l.off();
			l = this.led4;
			l.on();
			l = this.led5;
			l.on();
			l = this.led6;
			l.on();
			l = this.led7;
			l.on();
		} else if (this.digit == 7) {
			Led l = this.led1;
			l.on();
			l = this.led2;
			l.off();
			l = this.led3;
			l.on();
			l = this.led4;
			l.off();
			l = this.led5;
			l.off();
			l = this.led6;
			l.on();
			l = this.led7;
			l.off();
		} else if (this.digit == 8) {
			Led l = this.led1;
			l.on();
			l = this.led2;
			l.on();
			l = this.led3;
			l.on();
			l = this.led4;
			l.on();
			l = this.led5;
			l.on();
			l = this.led6;
			l.on();
			l = this.led7;
			l.on();
		} else if (this.digit == 9) {
			Led l = this.led1;
			l.on();
			l = this.led2;
			l.on();
			l = this.led3;
			l.on();
			l = this.led4;
			l.on();
			l = this.led5;
			l.off();
			l = this.led6;
			l.on();
			l = this.led7;
			l.on();
		} else {
			String s = "A digit must be between 0 and 9";
		}
	}

	boolean increment() {
		this.digit = (this.digit + 1);
		if (this.digit == 10)
			this.digit = 0;

		this.showDigit(this.digit);

		return this.digit == 0;
	}

	String toString() {
		String result = "";

		Led l = this.led1;
		if (l.isOn())
			result = result.concat(" _\n");
		else
			result = result.concat("  \n");

		l = this.led2;
		if (l.isOn())
			result = result.concat("|");
		else
			result = result.concat(" ");

		l = this.led4;
		if (l.isOn())
			result = result.concat("_");
		else
			result = result.concat(" ");

		l = this.led3;
		if (l.isOn())
			result = result.concat("|\n");
		else
			result = result.concat(" \n");

		l = this.led5;
		if (l.isOn())
			result = result.concat("|");
		else
			result = result.concat(" ");

		l = this.led7;
		if (l.isOn())
			result = result.concat("_");
		else
			result = result.concat(" ");

		l = this.led6;
		if (l.isOn())
			result = result.concat("|");
		else
			result = result.concat(" ");

		return result;
	}

	/* a test method */
	void main() {
		Digit digit = new Digit();

		for (int i = 0; i <= 9; i = i + 1) {
			digit.showDigit(i);
			String s = digit.toString();
			s = "\n";
		}
	}
}
