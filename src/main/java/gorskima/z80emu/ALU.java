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

		Adder adder = Adder.newAdder8();
		int result = adder.add(op1, op2, 0);
		registers.setRegister(Register.A, result);

		setAdditionFlags(adder, result);
	}

	public void adc(final int op2) {
		int op1 = registers.getRegister(Register.A);
		int carry = registers.testFlag(Flag.C) ? 1 : 0;

		Adder adder = Adder.newAdder8();
		int result = adder.add(op1, op2, carry);
		registers.setRegister(Register.A, result);

		setAdditionFlags(adder, result);
	}

	public void sub(final int op2) {
		int op1 = registers.getRegister(Register.A);

		Adder adder = Adder.newAdder8();
		int result = adder.sub(op1, op2, 0);
		registers.setRegister(Register.A, result);

		setSubstractionFlags(adder, result);
	}

	public void sbc(final int op2) {
		int op1 = registers.getRegister(Register.A);
		int carry = registers.testFlag(Flag.C) ? 1 : 0;

		Adder adder = Adder.newAdder8();
		int result = adder.sub(op1, op2, carry);
		registers.setRegister(Register.A, result);

		setSubstractionFlags(adder, result);
	}

	public void inc(final Register r) {
		int op1 = registers.getRegister(r);

		Adder adder = Adder.newAdder8();
		int result = adder.add(op1, 1, 0);
		registers.setRegister(r, result);

		setAdditionFlags(adder, result);
	}

	public void dec(final Register r) {
		int op1 = registers.getRegister(r);

		Adder adder = Adder.newAdder8();
		int result = adder.sub(op1, 1, 0);
		registers.setRegister(r, result);

		setSubstractionFlags(adder, result);
	}

	public void cp(final int op2) {
		int op1 = registers.getRegister(Register.A);

		Adder adder = Adder.newAdder8();
		int result = adder.sub(op1, op2, 0);

		setSubstractionFlags(adder, result);
	}

	public void neg() {
		int op2 = registers.getRegister(Register.A);

		Adder adder = Adder.newAdder8();
		int result = adder.sub(0, op2, 0);
		registers.setRegister(Register.A, result);

		setSubstractionFlags(adder, result);
	}

	private void setAdditionFlags(final Adder adder, final int result) {
		registers.setFlag(Flag.S, getSign(result));
		registers.setFlag(Flag.Z, isZero(result));
		registers.setFlag(Flag.H, adder.isHalfCarry());
		registers.setFlag(Flag.PV, adder.isOverflow());
		registers.setFlag(Flag.N, false);
		registers.setFlag(Flag.C, adder.isCarry());
	}

	private void setSubstractionFlags(final Adder adder, final int result) {
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

		setLogicalFlags(result);
		registers.setFlag(Flag.H, true);
	}

	public void or(final int op2) {
		int op1 = registers.getRegister(Register.A);
		int result = (op1 | op2) & 0xFF;
		registers.setRegister(Register.A, result);

		setLogicalFlags(result);
		registers.setFlag(Flag.H, false);
	}

	public void xor(final int op2) {
		int op1 = registers.getRegister(Register.A);
		int result = (op1 ^ op2) & 0xFF;
		registers.setRegister(Register.A, result);

		setLogicalFlags(result);
		registers.setFlag(Flag.H, false);
	}

	private void setLogicalFlags(final int result) {
		registers.setFlag(Flag.S, getSign(result));
		registers.setFlag(Flag.Z, isZero(result));
		registers.setFlag(Flag.PV, getParity(result));
		registers.setFlag(Flag.N, false);
		registers.setFlag(Flag.C, false);
	}

	public void setSignAndZeroFlags(final int result) {
		registers.setFlag(Flag.S, getSign(result));
		registers.setFlag(Flag.Z, isZero(result));
	}

	// TODO not sure if CPL belongs to ALU, let's leave it here for now
	public void cpl() {
		int op = registers.getRegister(Register.A);
		int result = invert(op);
		registers.setRegister(Register.A, result);

		registers.setFlag(Flag.H, true);
		registers.setFlag(Flag.N, true);
	}

	private int invert(final int op) {
		return ~op & 0xFF;
	}

	private boolean getSign(final int op) {
		return ((op >> 7) & 0x01) == 1;
	}

	private boolean isZero(final int op) {
		return op == 0;
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

	public void add16(final int op2) {
		int op1 = registers.getRegister(Register.HL);
		Adder adder = Adder.newAdder16();
		int result = adder.add(op1, op2, 0);
		registers.setRegister(Register.HL, result);

		registers.setFlag(Flag.C, adder.isCarry());
		registers.setFlag(Flag.H, adder.isHalfCarry());
		registers.setFlag(Flag.N, false);
	}

	public void adc16(final int op2) {
		int op1 = registers.getRegister(Register.HL);
		int carry = registers.testFlag(Flag.C) ? 1 : 0;
		Adder adder = Adder.newAdder16();
		int result = adder.add(op1, op2, carry);
		registers.setRegister(Register.HL, result);

		// TODO refactor getting sign
		registers.setFlag(Flag.S, ((result >> 15) & 0x01) == 1);
		registers.setFlag(Flag.Z, isZero(result));
		registers.setFlag(Flag.H, adder.isHalfCarry());
		registers.setFlag(Flag.PV, adder.isOverflow());
		registers.setFlag(Flag.N, false);
		registers.setFlag(Flag.C, true);
	}

}
