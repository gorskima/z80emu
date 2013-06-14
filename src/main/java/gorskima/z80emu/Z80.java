package gorskima.z80emu;

import gorskima.z80emu.Decoder.RegisterType;

public class Z80 {

	private final Registers registers;
	private final ALU alu;
	private final Decoder decoder = new Decoder();
	private final Memory memory;

	private boolean halt = false;
	
	// TODO add new constructor without registers
	public Z80(final Registers registers, final Memory memory) {
		this.registers = registers;
		this.memory = memory;
		this.alu = new ALU(registers);
	}

	public void step() {
		int opCode = fetchWord8();
		switch (opCode) {
			case 0xDD: stepPrefixedWithDD(); break;
			case 0xFD: stepPrefixedWithFD(); break;
			case 0xED: stepPrefixedWithED(); break;
			default: stepUnprefixed(opCode);
		}
	}

	private void stepUnprefixed(final int opCode) {
		// Unprefixed, single-byte opCodes, binary compatible with i8080
		
		switch (opCode) {

		/*
		 * 8-bit load group
		 */

		// LD r,r
		case 0x7F: // LD A,r
		case 0x78:
		case 0x79:
		case 0x7A:
		case 0x7B:
		case 0x7C:
		case 0x7D:
		case 0x47: // LD B,r
		case 0x40:
		case 0x41:
		case 0x42:
		case 0x43:
		case 0x44:
		case 0x45:
		case 0x4F: // LD C,r
		case 0x48:
		case 0x49:
		case 0x4A:
		case 0x4B:
		case 0x4C:
		case 0x4D:
		case 0x57: // LD D,r
		case 0x50:
		case 0x51:
		case 0x52:
		case 0x53:
		case 0x54:
		case 0x55:
		case 0x5F: // LD E,r
		case 0x58:
		case 0x59:
		case 0x5A:
		case 0x5B:
		case 0x5C:
		case 0x5D:
		case 0x67: // LD H,r
		case 0x60:
		case 0x61:
		case 0x62:
		case 0x63:
		case 0x64:
		case 0x65:
		case 0x6F: // LD L,r
		case 0x68:
		case 0x69:
		case 0x6A:
		case 0x6B:
		case 0x6C:
		case 0x6D: {
			Register destReg = decoder.decodeUpperR(opCode);
			Register srcReg = decoder.decodeLowerR(opCode);
			int n = registers.getRegister(srcReg);
			registers.setRegister(destReg, n);
			break;
		}

		// LD r,n
		case 0x3E:
		case 0x06:
		case 0x0E:
		case 0x16:
		case 0x1E:
		case 0x26:
		case 0x2E: {
			int n = fetchWord8();
			Register destReg = decoder.decodeUpperR(opCode);
			registers.setRegister(destReg, n);
			break;
		}

		// LD r,(HL)
		case 0x7E:
		case 0x46:
		case 0x4E:
		case 0x56:
		case 0x5E:
		case 0x66:
		case 0x6E: {
			int addr = registers.getRegister(Register.HL);
			int n = memory.readWord8(addr);
			Register destReg = decoder.decodeUpperR(opCode);
			registers.setRegister(destReg, n);
			break;
		}

		// LD (HL),r
		case 0x70:
		case 0x71:
		case 0x72:
		case 0x73:
		case 0x74:
		case 0x75:
		case 0x77: {
			Register srcReg = decoder.decodeLowerR(opCode);
			int n = registers.getRegister(srcReg);
			int addr = registers.getRegister(Register.HL);
			memory.writeWord8(addr, n);
			break;
		}

		// LD (HL),n
		case 0x36: {
			int n = fetchWord8();
			int addr = registers.getRegister(Register.HL);
			memory.writeWord8(addr, n);
			break;
		}

		// LD A,(BC)
		case 0x0A: {
			int addr = registers.getRegister(Register.BC);
			int n = memory.readWord8(addr);
			registers.setRegister(Register.A, n);
			break;
		}

		// LD A,(DE)
		case 0x1A: {
			int addr = registers.getRegister(Register.DE);
			int n = memory.readWord8(addr);
			registers.setRegister(Register.A, n);
			break;
		}

		// LD A,(nn)
		case 0x3A: {
			int addr = fetchWord16();
			int n = memory.readWord8(addr);
			registers.setRegister(Register.A, n);
			break;
		}

		// LD (BC),A
		case 0x02: {
			int addr = registers.getRegister(Register.BC);
			int n = registers.getRegister(Register.A);
			memory.writeWord8(addr, n);
			break;
		}

		// LD (DE),A
		case 0x12: {
			int addr = registers.getRegister(Register.DE);
			int n = registers.getRegister(Register.A);
			memory.writeWord8(addr, n);
			break;
		}

		// LD (nn),A
		case 0x32: {
			int addr = fetchWord16();
			int n = registers.getRegister(Register.A);
			memory.writeWord8(addr, n);
			break;
		}

		/*
		 * 16-bit load group
		 */

		// LD dd,nn
		case 0x01:
		case 0x11:
		case 0x21:
		case 0x31: {
			int nn = fetchWord16();
			Register destReg = decoder.decodeRegister(RegisterType.dd, opCode);
			registers.setRegister(destReg, nn);
			break;
		}

		// LD HL,(nn)
		case 0x2A: {
			int addr = fetchWord16();
			int nn = memory.readWord16(addr);
			registers.setRegister(Register.HL, nn);
			break;
		}

		// LD (nn),HL
		case 0x22: {
			int addr = fetchWord16();
			int nn = registers.getRegister(Register.HL);
			memory.writeWord16(addr, nn);
			break;
		}

		// LD SP,HL
		case 0xF9: {
			int nn = registers.getRegister(Register.HL);
			registers.setRegister(Register.SP, nn);
			break;
		}

		// PUSH qq
		case 0xC5:
		case 0xD5:
		case 0xE5:
		case 0xF5: {
			Register srcReg = decoder.decodeRegister(RegisterType.qq, opCode);
			int value = registers.getRegister(srcReg);
			int sp = registers.getRegister(Register.SP);
			memory.writeWord16(sp - 2, value);
			registers.setRegister(Register.SP, sp - 2);
			break;
		}

		// POP qq
		case 0xC1:
		case 0xD1:
		case 0xE1:
		case 0xF1: {
			Register dstReg = decoder.decodeRegister(RegisterType.qq, opCode);
			int sp = registers.getRegister(Register.SP);
			int value = memory.readWord16(sp);
			registers.setRegister(dstReg, value);
			registers.setRegister(Register.SP, sp + 2);
			break;
		}
		
		/*
		 * Exchange, Block Transfer, and Search Group
		 */

		/*
		 * 8-bit arithmetic group
		 */

		// ADD A,r
		case 0x80:
		case 0x81:
		case 0x82:
		case 0x83:
		case 0x84:
		case 0x85:
		case 0x87: {
			Register srcReg = decoder.decodeLowerR(opCode);
			int n = registers.getRegister(srcReg);
			alu.add(n);
			break;
		}

		// ADD A,n
		case 0xC6: {
			int n = fetchWord8();
			alu.add(n);
			break;
		}

		// ADD A,(HL)
		case 0x86: {
			int hl = registers.getRegister(Register.HL);
			int n = memory.readWord8(hl);
			alu.add(n);
			break;
		}
		
		// TODO implement ADC A,r

		// ADC A,n
		case 0xCE: {
			int n = fetchWord8();
			alu.adc(n);
			break;
		}
		
		// TODO implement ADC A,(HL)
		
		// SUB r
		case 0x90:
		case 0x91:
		case 0x92:
		case 0x93:
		case 0x94:
		case 0x95:
		case 0x97: {
			Register srcReg = decoder.decodeLowerR(opCode);
			int n = registers.getRegister(srcReg);
			alu.sub(n);
			break;
		}

		// SUB n
		case 0xD6: {
			int n = fetchWord8();
			alu.sub(n);
			break;
		}
		
		// TODO implement SUB (HL)
		
		// TODO implement SBC A,r

		// SBC A,n
		case 0xDE: {
			int n = fetchWord8();
			alu.sbc(n);
			break;
		}
		
		// TODO implement SBC A,(HL)
		
		// TODO implement AND r

		// AND n
		case 0xE6: {
			int n = fetchWord8();
			alu.and(n);
			break;
		}
		
		// TODO implement AND (HL)

		// OR r
		case 0xB0:
		case 0xB1:
		case 0xB2:
		case 0xB3:
		case 0xB4:
		case 0xB5:
		case 0xB6:
		case 0xB7: {
			Register srcReg = decoder.decodeLowerR(opCode);
			int n = registers.getRegister(srcReg);
			alu.or(n);
			break;
		}

		// OR n
		case 0xF6: {
			int n = fetchWord8();
			alu.or(n);
			break;
		}
		
		// TODO implement OR (HL)
		
		// TODO implement XOR r

		// XOR n
		case 0xEE: {
			int n = fetchWord8();
			alu.xor(n);
			break;
		}
		
		// TODO implement XOR (HL)

		// TODO implement CP r
		
		// CP n
		case 0xFE: {
			int n = fetchWord8();
			alu.cp(n);
			break;
		}
		
		// TODO implement CP (HL)

		// INC r
		case 0x04:
		case 0x0C:
		case 0x14:
		case 0x1C:
		case 0x24:
		case 0x2C:
		case 0x3C: {
			Register r = decoder.decodeUpperR(opCode);
			alu.inc(r);
			break;
		}

		// INC (HL) // TODO fix
		case 0x34: {
			int hl = registers.getRegister(Register.HL);
			int value = memory.readWord8(hl);
			// word = alu.inc(word);
			memory.writeWord8(hl, value);
			break;
		}

		// DEC r
		case 0x05:
		case 0x0D:
		case 0x15:
		case 0x1D:
		case 0x25:
		case 0x2D:
		case 0x3D: {
			Register r = decoder.decodeUpperR(opCode);
			alu.dec(r);
			break;
		}
		
		// TODO implement DEC (HL)

		/*
		 * General purpose arithmetic and CPU control
		 */

		// CPL
		case 0x2F: {
			alu.cpl();
			break;
		}
		
		// TODO implement CCF
		
		// TODO implement SCF

		// NOP
		case 0x00: {
			// do nothing :)
			break;
		}

		// HALT
		case 0x76: {
			halt = true;
			break;
		}
		
		// TODO implement DI
		
		// TODO implement EI
		
		/*
		 * 16-Bit Arithmetic Group
		 */
		
		// ADD HL,ss
		case 0x09:
		case 0x19:
		case 0x29:
		case 0x39: {
			Register reg = decoder.decodeRegister(RegisterType.ss, opCode);
			int value = registers.getRegister(reg);
			alu.add16(value);
			break;
		}
		
		// TODO implement INC ss
		
		// TODO implement DEC ss
		
		/*
		 * Rotate and Shift Grouop
		 */
		
		// TODO implement all
		
		/*
		 * Bit Set, Reset, and Test Group
		 */
		
		// TODO implement all

		/*
		 * Jump group
		 */

		// JP nn
		case 0xC3: {
			int nn = fetchWord16();
			registers.setRegister(Register.PC, nn);
			break;
		}

		// JP cc,nn
		case 0xC2:
		case 0xCA:
		case 0xD2:
		case 0xDA:
		case 0xE2:
		case 0xEA:
		case 0xF2:
		case 0xFA: {
			int nn = fetchWord16();
			Condition cond = decoder.decodeCondition(opCode);
			if (registers.testFlag(cond.getFlag()) == cond.getExpectedValue()) {
				registers.setRegister(Register.PC, nn);
			}
			break;
		}
		
		// TODO implement JR e
		
		// consider implementing the four below in a generic way
		
		// TODO implement JR C,e
		
		// TODO implement JR NC,e
		
		// TODO implement JR Z,e
		
		// TODO implement JR NZ,e
		
		// TODO implement JP (HL)
		
		// TODO implement DJNZ

		/*
		 * Call and return group
		 */

		// CALL nn
		case 0xCD: {
			int addr = fetchWord16();
			int pc = registers.getRegister(Register.PC);
			int sp = registers.getRegister(Register.SP);
			memory.writeWord16(sp - 2, pc);
			registers.setRegister(Register.SP, sp - 2);
			registers.setRegister(Register.PC, addr);
			break;
		}
		
		// TODO implement CALL cc,nn

		// RET
		case 0xC9: {
			int sp = registers.getRegister(Register.SP);
			int addr = memory.readWord16(sp);
			registers.setRegister(Register.SP, sp + 2);
			registers.setRegister(Register.PC, addr);
			break;
		}
		
		// TODO implement RET cc
		
		// TODO implement RST p

		/*
		 * Input and output group
		 */

		// IN A,(n) // TODO implement
		case 0xDB: {
			throw new UnsupportedOperationException("IN op is not yet implemented");
		}
		
		// TODO implement IN r,(C)

		// OUT (n),A // TODO implement
		case 0xD3: {
			throw new UnsupportedOperationException("OUT op is not yet implemented");
		}

		default:
			handleUnsupportedOpCode(opCode);
		}
	}

	private void stepPrefixedWithDD() {
		/*
		 * IX operations
		 */

		int opCode = fetchWord8();

		switch (opCode) {

		/*
		 * 8-bit load group
		 */

		// LD r,(IX+d)
		case 0x46:
		case 0x4E:
		case 0x56:
		case 0x5E:
		case 0x66:
		case 0x6E:
		case 0x7E: {
			int d = fetchWord8();
			int ix = registers.getRegister(Register.IX);
			int n = memory.readWord8(ix + d);
			Register destReg = decoder.decodeUpperR(opCode);
			registers.setRegister(destReg, n);
			break;
		}

		// LD (IX+d),r
		case 0x70:
		case 0x71:
		case 0x72:
		case 0x73:
		case 0x74:
		case 0x75:
		case 0x77: {
			int d = fetchWord8();
			int ix = registers.getRegister(Register.IX);
			Register srcReg = decoder.decodeLowerR(opCode);
			int n = registers.getRegister(srcReg);
			int addr = displace(ix, d);
			memory.writeWord8(addr, n);
			break;
		}

		// LD (IX+d),n
		case 0x36: {
			int d = fetchWord8();
			int n = fetchWord8();
			int ix = registers.getRegister(Register.IX);
			int addr = displace(ix, d);
			memory.writeWord8(addr, n);
			break;
		}

		/*
		 * 16-bit load group
		 */

		// LD IX,nn
		case 0x21: {
			int nn = fetchWord16();
			registers.setRegister(Register.IX, nn);
			break;
		}

		/*
		 * 8-bit arithmetic group
		 */

		// ADD A,(IX+d)
		case 0x86: {
			int d = fetchWord8();
			int ix = registers.getRegister(Register.IX);
			int n = memory.readWord8(ix + d);
			alu.add(n);
			break;
		}

		/*
		 * 16-bit arithmetic group
		 */
		case 0x09:
		case 0x19:
		case 0x29:
		case 0x39: {
			Register register = decoder.decodeRegister(RegisterType.pp, opCode);
			int nn = registers.getRegister(register);
			// TODO move addition to ALU / 16-bit adder
			registers.setRegister(Register.IX, registers.getRegister(Register.IX) + nn);
			break;
		}

		default:
			handleUnsupportedOpCode(opCode);

		}
	}

	private void stepPrefixedWithFD() {
		/*
		 * IY operations
		 */

		int opCode = fetchWord8();

		switch (opCode) {

		/*
		 * 8-bit load group
		 */

		// LD r,(IY+d)
		case 0x46:
		case 0x4E:
		case 0x56:
		case 0x5E:
		case 0x66:
		case 0x6E:
		case 0x7E: {
			int d = fetchWord8();
			int iy = registers.getRegister(Register.IY);
			int n = memory.readWord8(iy + d);
			Register destReg = decoder.decodeUpperR(opCode);
			registers.setRegister(destReg, n);
			break;
		}

		// LD (IY+d),r
		case 0x70:
		case 0x71:
		case 0x72:
		case 0x73:
		case 0x74:
		case 0x75:
		case 0x77: {
			int d = fetchWord8();
			int iy = registers.getRegister(Register.IY);
			Register srcReg = decoder.decodeLowerR(opCode);
			int n = registers.getRegister(srcReg);
			int addr = displace(iy, d);
			memory.writeWord8(addr, n);
			break;
		}

		// LD (IY+d),n
		case 0x36: {
			int d = fetchWord8();
			int n = fetchWord8();
			int iy = registers.getRegister(Register.IY);
			int addr = displace(iy, d);
			memory.writeWord8(addr, n);
			break;
		}

		default:
			handleUnsupportedOpCode(opCode);

		}
	}

	private void stepPrefixedWithED() {
		/*
		 * (nn) operations?
		 */
	
		int opCode = fetchWord8();
	
		switch (opCode) {
		
		/*
		 * 8-bit load group
		 */
	
		// LD A,I
		case 0x57: {
			int n = registers.getRegister(Register.I);
			registers.setRegister(Register.A, n);
			alu.setSignAndZeroFlags(n);
			registers.setFlag(Flag.H, false);
			registers.setFlag(Flag.N, false);
			// TODO set PV flag (interrupt related)
			break;
		}
	
		// LD A,R
		case 0x5F: {
			int n = registers.getRegister(Register.R);
			registers.setRegister(Register.A, n);
			alu.setSignAndZeroFlags(n);
			registers.setFlag(Flag.H, false);
			registers.setFlag(Flag.N, false);
			// TODO set PV flag (interrupt related)
			break;
		}
	
		// LD I,A
		case 0x47: {
			int n = registers.getRegister(Register.A);
			registers.setRegister(Register.I, n);
			break;
		}
	
		// LD R,A
		case 0x4F: {
			int n = registers.getRegister(Register.A);
			registers.setRegister(Register.R, n);
			break;
		}
	
		/*
		 * 16-bit load group
		 */

		// LD dd,(nn)
		case 0x4B:
		case 0x5B:
		case 0x6B:
		case 0x7B: {
			int nn = fetchWord16();
			int value = memory.readWord16(nn);
			Register destReg = decoder.decodeRegister(RegisterType.dd, opCode);
			registers.setRegister(destReg, value);
			break;
		}

		/*
		 * General-purpose arithmetic and CPU control groups
		 */
	
		// NEG
		case 0x44: {
			alu.neg();
			break;
		}
		
		/*
		 * 16-Bit Arithmetic Group
		 */
		
		// ADC HL,ss
		case 0x4A:
		case 0x5A:
		case 0x6A:
		case 0x7A: {
			Register reg = decoder.decodeRegister(RegisterType.ss, opCode);
			int value = registers.getRegister(reg);
			alu.adc16(value);
			break;
		}
		
		// SBC HL,ss
		case 0x42:
		case 0x52:
		case 0x62:
		case 0x72: {
			Register reg = decoder.decodeRegister(RegisterType.ss, opCode);
			int value = registers.getRegister(reg);
			alu.sbc16(value);
			break;
		}

		default:
			handleUnsupportedOpCode(opCode);
	
		}
	}

	private int fetchWord8() {
		int pc = registers.getRegister(Register.PC);
		int word = memory.readWord8(pc);
		registers.incPC();
		return word;
	}

	private int fetchWord16() {
		int pc = registers.getRegister(Register.PC);
		int word = memory.readWord16(pc);
		registers.incPC();
		registers.incPC();
		return word;
	}

	private int displace(final int addr, final int d) {
		return addr + (byte) d;
	}

	private void handleUnsupportedOpCode(final int opCode) {
		throw new IllegalArgumentException(String.format("OpCode 0x%x not supported", opCode));
	}

	public Memory getMemory() {
		return memory;
	}

	public Registers getRegisters() {
		return registers;
	}

	public boolean isHalt() {
		return halt;
	}

}
