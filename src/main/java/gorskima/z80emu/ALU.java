package gorskima.z80emu;


public class ALU {

	private final Registers registers;

	public ALU(final Registers registers) {
		this.registers = registers;
	}

	public ALU() {
		this(new Registers());
	}

	public void add(final int op2) {
		int op1 = registers.getRegister(Register.A);

		Adder adder = new Adder();
		int result = adder.add(op1, op2, 0);
		registers.setRegister(Register.A, result);

		registers.setFlag(Flag.S, getSign(result));
		registers.setFlag(Flag.Z, isZero(result));
		registers.setFlag(Flag.H, adder.isHalfCarry());
		registers.setFlag(Flag.PV, adder.isOverflow());
		registers.setFlag(Flag.N, false);
		registers.setFlag(Flag.C, adder.isCarry());
	}

	public void adc(final int op2) {
		int op1 = registers.getRegister(Register.A);
		int carry = registers.testFlag(Flag.C) ? 1 : 0;

		Adder adder = new Adder();
		int result = adder.add(op1, op2, carry);
		registers.setRegister(Register.A, result);

		registers.setFlag(Flag.S, getSign(result));
		registers.setFlag(Flag.Z, isZero(result));
		registers.setFlag(Flag.H, adder.isHalfCarry());
		registers.setFlag(Flag.PV, adder.isOverflow());
		registers.setFlag(Flag.N, false);
		registers.setFlag(Flag.C, adder.isCarry());
	}

	public void sub(final int op2) {
		int op1 = registers.getRegister(Register.A);

		Adder adder = new Adder();
		int result = adder.sub(op1, op2, 0);
		registers.setRegister(Register.A, result);

		registers.setFlag(Flag.S, getSign(result));
		registers.setFlag(Flag.Z, isZero(result));
		registers.setFlag(Flag.H, adder.isHalfBorrow());
		registers.setFlag(Flag.PV, adder.isOverflow());
		registers.setFlag(Flag.N, true);
		registers.setFlag(Flag.C, adder.isBorrow());
	}

	public void sbc(final int op2) {
		int op1 = registers.getRegister(Register.A);
		int carry = registers.testFlag(Flag.C) ? 1 : 0;

		Adder adder = new Adder();
		int result = adder.sub(op1, op2, carry);
		registers.setRegister(Register.A, result);

		registers.setFlag(Flag.S, getSign(result));
		registers.setFlag(Flag.Z, isZero(result));
		registers.setFlag(Flag.H, adder.isHalfBorrow());
		registers.setFlag(Flag.PV, adder.isOverflow());
		registers.setFlag(Flag.N, true);
		registers.setFlag(Flag.C, adder.isBorrow());
	}

	public void and(final int op2) {
		int op1 = registers.getRegister(Register.A);
		int result = (op1 & op2) & 0xFF;
		registers.setRegister(Register.A, result);

		registers.setFlag(Flag.S, getSign(result));
		registers.setFlag(Flag.Z, isZero(result));
		registers.setFlag(Flag.H, true);
		registers.setFlag(Flag.PV, getParity(result));
		registers.setFlag(Flag.N, false);
		registers.setFlag(Flag.C, false);
	}

	public void or(final int op2) {
		int op1 = registers.getRegister(Register.A);
		int result = (op1 | op2) & 0xFF;
		registers.setRegister(Register.A, result);

		registers.setFlag(Flag.S, getSign(result));
		registers.setFlag(Flag.Z, isZero(result));
		registers.setFlag(Flag.H, false);
		registers.setFlag(Flag.PV, getParity(result));
		registers.setFlag(Flag.N, false);
		registers.setFlag(Flag.C, false);
	}

	public void xor(final int op2) {
		int op1 = registers.getRegister(Register.A);
		int result = (op1 ^ op2) & 0xFF;
		registers.setRegister(Register.A, result);

		registers.setFlag(Flag.S, getSign(result));
		registers.setFlag(Flag.Z, isZero(result));
		registers.setFlag(Flag.H, false);
		registers.setFlag(Flag.PV, getParity(result));
		registers.setFlag(Flag.N, false);
		registers.setFlag(Flag.C, false);
	}

	public void cp(final int op2) {
		int op1 = registers.getRegister(Register.A);

		Adder adder = new Adder();
		int result = adder.sub(op1, op2, 0);

		registers.setFlag(Flag.S, getSign(result));
		registers.setFlag(Flag.Z, isZero(result));
		registers.setFlag(Flag.H, adder.isHalfBorrow());
		registers.setFlag(Flag.PV, adder.isOverflow());
		registers.setFlag(Flag.N, true);
		registers.setFlag(Flag.C, adder.isBorrow());
	}

	public void inc(final Register r) {
		int op1 = registers.getRegister(r);

		Adder adder = new Adder();
		int result = adder.add(op1, 1, 0);
		registers.setRegister(r, result);

		registers.setFlag(Flag.S, getSign(result));
		registers.setFlag(Flag.Z, isZero(result));
		registers.setFlag(Flag.H, adder.isHalfCarry());
		registers.setFlag(Flag.PV, adder.isOverflow());
		registers.setFlag(Flag.N, false);
		registers.setFlag(Flag.C, adder.isCarry());
	}

	public void dec(final Register r) {
		int op1 = registers.getRegister(r);

		Adder adder = new Adder();
		int result = adder.sub(op1, 1, 0);
		registers.setRegister(r, result);

		registers.setFlag(Flag.S, getSign(result));
		registers.setFlag(Flag.Z, isZero(result));
		registers.setFlag(Flag.H, adder.isHalfBorrow());
		registers.setFlag(Flag.PV, adder.isOverflow());
		registers.setFlag(Flag.N, true);
		registers.setFlag(Flag.C, adder.isBorrow());
	}

	public void cpl() {
		int op = registers.getRegister(Register.A);
		int result = invert(op);
		registers.setRegister(Register.A, result);

		registers.setFlag(Flag.H, true);
		registers.setFlag(Flag.N, true);
	}

	public void neg() {
		int op2 = registers.getRegister(Register.A);

		Adder adder = new Adder();
		int result = adder.sub(0, op2, 0);
		registers.setRegister(Register.A, result);

		registers.setFlag(Flag.S, getSign(result));
		registers.setFlag(Flag.Z, isZero(result));
		registers.setFlag(Flag.H, adder.isHalfBorrow());
		registers.setFlag(Flag.PV, adder.isOverflow());
		registers.setFlag(Flag.N, true);
		registers.setFlag(Flag.C, adder.isBorrow());
	}

	private boolean getSign(final int op) {
		return ((op >> 7) & 0x01) == 1;
	}

	private boolean isZero(final int op) {
		return op == 0;
	}

	private int invert(final int op) {
		return ~op & 0xFF;
	}

	private boolean getParity(final int op) {
		int c = 0;
		for (int i = 0; i < 8; i++) {
			if (((op >> i) & 0x01) == 1) {
				c++;
			}
		}
		return (c % 2) == 0;
	}

}
