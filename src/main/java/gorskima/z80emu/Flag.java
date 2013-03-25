package gorskima.z80emu;

public enum Flag {
	S(0x80), Z(0x40), H(0x10), PV(0x04), N(0x02), C(0x01);

	public final int mask;

	Flag(final int value) {
		this.mask = value;
	}

}
