package gorskima.z80emu;

public class Registers {

	private static final int REG_OFFSET = 8;

	private int[] mem;
	private int exSwitch = 0;
	private int exxSwitch = 0;

	public Registers() {
		mem = new int[26];
	}

	public int getRegister(final Register r) {
		if (r.size == 1) {
			return getRegister8(r);
		} else {
			return getRegister16(r);
		}
	}

	public void setRegister(final Register r, final int value) {
		if (r.size == 1) {
			setRegister8(r, value);
		} else {
			setRegister16(r, value);
		}
	}

	private int getRegister8(final Register r) {
		int addr = calculateAddr(r);
		return mem[addr];
	}

	private void setRegister8(final Register r, final int value) {
		int addr = calculateAddr(r);
		mem[addr] = value;
	}

	private int getRegister16(final Register r) {
		int addr = calculateAddr(r);
		int h = mem[addr];
		int l = mem[addr + 1];
		return ((h << 8) + l);
	}

	private void setRegister16(final Register r, final int value) {
		int addr = calculateAddr(r);
		int h = value >> 8;
		int l = value & 0xFF;
		mem[addr] = h;
		mem[addr + 1] = l;
	}

	public void incPC() {
		int pc = getRegister16(Register.PC);
		pc = (pc + 1) & 0xFFFF;
		setRegister16(Register.PC, pc);
	}

	public boolean testFlag(final Flag flag) {
		return ((getRegister(Register.F) & flag.mask) > 0);
	}

	public void setFlag(final Flag flag, final boolean value) {
		int f = getRegister8(Register.F);

		if (value) {
			setRegister8(Register.F, f | flag.mask);
		} else {
			setRegister8(Register.F, f & ~flag.mask);
		}
	}

	private int calculateAddr(final Register r) {
		int regSwitch = 0;

		// TODO clean up
		switch (r) {
		case A:
		case F:
		case AF:
			regSwitch = exSwitch;
			break;
		case B:
		case C:
		case D:
		case E:
		case H:
		case L:
		case BC:
		case DE:
		case HL:
			regSwitch = exxSwitch;
			break;
		}

		return r.value + regSwitch * REG_OFFSET;
	}

}
