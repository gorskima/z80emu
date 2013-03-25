package gorskima.z80emu;

public class Registers {

	private int[] mem;
	private int EXswitch = 0;
	private int EXXswitch = 0;
	private final int regOffset = 8;

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

		switch (r) {
		case A:
		case F:
		case AF:
			regSwitch = EXswitch;
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
			regSwitch = EXXswitch;
			break;
		}

		return r.value + regSwitch * regOffset;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("A:" + getRegister(Register.A));
		buf.append("  B:" + getRegister(Register.B));
		buf.append("  C:" + getRegister(Register.C));
		buf.append("  D:" + getRegister(Register.D));
		buf.append("  E:" + getRegister(Register.E));
		buf.append("  H:" + getRegister(Register.H));
		buf.append("  L:" + getRegister(Register.L));
		buf.append("  F:" + getRegister(Register.F));
		buf.append("\nS:" + bool2int(testFlag(Flag.S)));
		buf.append("  Z:" + bool2int(testFlag(Flag.Z)));
		buf.append("  H:" + bool2int(testFlag(Flag.H)));
		buf.append("  PV:" + bool2int(testFlag(Flag.PV)));
		buf.append("  N:" + bool2int(testFlag(Flag.N)));
		buf.append("  C:" + bool2int(testFlag(Flag.C)));
		buf.append("\nBC:" + getRegister(Register.BC));
		buf.append("  DE:" + getRegister(Register.DE));
		buf.append("  HL:" + getRegister(Register.HL));
		buf.append("  I:" + getRegister(Register.I));
		buf.append("  R:" + getRegister(Register.R));
		buf.append("  IX:" + getRegister(Register.IX));
		buf.append("  IY:" + getRegister(Register.IY));
		buf.append("  SP:" + getRegister(Register.SP));
		buf.append("  PC:" + getRegister(Register.PC));

		return buf.toString();
	}

	private int bool2int(final boolean b) {
		return b ? 1 : 0;
	}

}
