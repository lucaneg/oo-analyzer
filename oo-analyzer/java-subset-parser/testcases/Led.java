class Led {
	boolean state;

	Led() {
	}

	void on() {
		this.state = true;
	}

	void off() {
		this.state = false;
	}

	boolean isOn() {
		return this.state;
	}

	boolean isOff() {
		return !this.state;
	}
}
