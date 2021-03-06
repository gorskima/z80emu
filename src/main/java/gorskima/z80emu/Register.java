package gorskima.z80emu;

public enum Register {
	A(1,0), F(1,1), B(1,2), C(1,3), D(1,4), E(1,5), H(1,6), L(1,7), AF(2,A.offset), BC(2,B.offset), DE(2,D.offset), HL(2,H.offset),
	I(1,16), R(1,17), IX(2,18), IY(2,20), SP(2,22), PC(2,24);
	
	public final int size;
	public final int offset;

	Register(final int size, final int addr) {
		this.size = size;
		this.offset = addr;
	}
}
