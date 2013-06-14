package gorskima.z80emu;

import static gorskima.z80emu.Register.F;
import static gorskima.z80emu.Register.PC;

public class Registers {

	private static final int REG_OFFSET = 8;

	private int[] mem = new int[26];
	private boolean exSwitch = false;
	private boolean exxSwitch = false;

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
		int pc = getRegister16(PC);
		int newPc = (pc + 1) & 0xFFFF;
		setRegister16(PC, newPc);
	}

	public boolean testFlag(final Flag flag) {
		return (getRegister(F) & flag.mask) > 0;
	}

	public void setFlag(final Flag flag, final boolean value) {
		int f = getRegister8(F);

		if (value) {
			setRegister8(F, f | flag.mask);
		} else {
			setRegister8(F, f & ~flag.mask);
		}
	}

	private int calculateAddr(final Register r) {
		switch (r) {
		case A:
		case F:
		case AF:
			return r.offset + (exSwitch ? 1 : 0) * REG_OFFSET;
		case B:
		case C:
		case D:
		case E:
		case H:
		case L:
		case BC:
		case DE:
		case HL:
			return r.offset + (exxSwitch ? 1 : 0) * REG_OFFSET;
		default:
			return r.offset;
		}
	}

}
