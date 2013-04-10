package gorskima.z80emu;

public class Condition {

	private final Flag flag;
	private final boolean expectedValue;

	public Condition(final Flag flag, final boolean expectedValue) {
		this.flag = flag;
		this.expectedValue = expectedValue;
	}

	public Flag getFlag() {
		return flag;
	}

	public boolean getExpectedValue() {
		return expectedValue;
	}

}
