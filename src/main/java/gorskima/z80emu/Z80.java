package gorskima.z80emu;

import gorskima.z80emu.Decoder.RegisterType;

import java.util.Scanner;

public class Z80 {

	private final Registers registers = new Registers();
	private final ALU alu = new ALU(registers);
	private final Decoder decoder = new Decoder();
	private final Memory memory;

	private boolean halt = false;
	
	public Z80(final Memory memory) {
		this.memory = memory;
	}

	public Z80() {
		this(new Memory());
	}

	public void step() {
		int opCode = fetchWord8();

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
			int destRegCode = extractHigherRegisterCode(opCode);
			int srcRegCode = extractLowerRegisterCode(opCode);
			Register destReg = decoder.decode(RegisterType.r, destRegCode);
			Register srcReg = decoder.decode(RegisterType.r, srcRegCode);
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
			int destRegCode = extractHigherRegisterCode(opCode);
			Register destReg = decoder.decode(RegisterType.r, destRegCode);
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
			int hl = registers.getRegister(Register.HL);
			int n = memory.readWord8(hl);
			int destRegCode = extractHigherRegisterCode(opCode);
			Register destReg = decoder.decode(RegisterType.r, destRegCode);
			registers.setRegister(destReg, n);
			break;
		}

		// LD A,(nn)
		case 0x3A: {
			int addr = fetchWord16();
			int n = memory.readWord8(addr);
			registers.setRegister(Register.A, n);
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

			int destRegCode = (opCode >> 4) & 0x03;
			Register destReg = decoder.decode(RegisterType.dd, destRegCode);
			registers.setRegister(destReg, nn);
			break;
		}

		// PUSH qq
		case 0xC5:
		case 0xD5:
		case 0xE5:
		case 0xF5: {
			int srcRegCode = (opCode >> 4) & 0x03;
			Register srcReg = decoder.decode(RegisterType.qq, srcRegCode);
			int value = registers.getRegister(srcReg);
			int sp = registers.getRegister(Register.SP);
			sp -= 2;
			memory.writeWord16(sp, value);
			registers.setRegister(Register.SP, sp);
			break;
		}

		// POP qq
		case 0xC1:
		case 0xD1:
		case 0xE1:
		case 0xF1: {
			int dstRegCode = (opCode >> 4) & 0x03;
			Register dstReg = decoder.decode(RegisterType.qq, dstRegCode);
			int sp = registers.getRegister(Register.SP);
			int value = memory.readWord16(sp);
			registers.setRegister(dstReg, value);
			registers.setRegister(Register.SP, sp + 2);
			break;
		}

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
			int srcRegCode = extractLowerRegisterCode(opCode);
			Register srcReg = decoder.decode(RegisterType.r, srcRegCode);
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

		// ADC A,n
		case 0xCE: {
			int n = fetchWord8();
			boolean carry = registers.testFlag(Flag.C);
			if (carry) {
				alu.adc(n);
			} else {
				alu.add(n);
			}
			break;
		}

		// SUB r
		case 0x90:
		case 0x91:
		case 0x92:
		case 0x93:
		case 0x94:
		case 0x95:
		case 0x97: {
			int srcRegCode = extractLowerRegisterCode(opCode);
			Register srcReg = decoder.decode(RegisterType.r, srcRegCode);
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

		// SBC A,n
		case 0xDE: {
			int n = fetchWord8();
			alu.sbc(n);
			break;
		}

		// AND n
		case 0xE6: {
			int n = fetchWord8();
			alu.and(n);
			break;
		}

		// OR r
		case 0xB0:
		case 0xB1:
		case 0xB2:
		case 0xB3:
		case 0xB4:
		case 0xB5:
		case 0xB6:
		case 0xB7: {
			int srcRegCode = extractLowerRegisterCode(opCode);
			Register srcReg = decoder.decode(RegisterType.r, srcRegCode);
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

		// XOR n
		case 0xEE: {
			int n = fetchWord8();
			alu.xor(n);
			break;
		}

		// CP n
		case 0xFE: {
			int n = fetchWord8();
			alu.cp(n);
			break;
		}

		// INC r
		case 0x04:
		case 0x0C:
		case 0x14:
		case 0x1C:
		case 0x24:
		case 0x2C:
		case 0x3C: {
			int regCode = extractHigherRegisterCode(opCode);
			Register r = decoder.decode(RegisterType.r, regCode);
			alu.inc(r);
			break;
		}

		// INC (HL)
		case 0x34: {
			int hl = registers.getRegister(Register.HL);
			int word = memory.readWord8(hl);
			// word = alu.inc(word);
			memory.writeWord8(hl, word);
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
			int regCode = (opCode >> 3) & 0x07;
			Register r = decoder.decode(RegisterType.r, regCode);
			alu.dec(r);
			break;
		}

		/*
		 * General purpose arithmetic and CPU control
		 */

		// CPL
		case 0x2F: {
			alu.cpl();
			break;
		}

		// NOP
		case 0x00:
			break;

		// HALT
		case 0x76:
			halt = true;

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
			int flagCode = (opCode >> 3) & 0x07;
			Flag flag = decoder.decodeFlag(flagCode);
			int desiredValue = flagCode & 0x01;

			// if (reg.getFlag(flag) == desiredValue) {
			// reg.setRegister(Register.PC, nn);
			// }

			if (registers.testFlag(flag) && (desiredValue != 0 ? true : false)) {
				registers.setRegister(Register.PC, nn);
			}

			break;
		}

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

		// RET
		case 0xC9: {
			int sp = registers.getRegister(Register.SP);
			int addr = memory.readWord16(sp);
			registers.setRegister(Register.SP, sp + 2);
			registers.setRegister(Register.PC, addr);
			break;
		}

		/*
		 * Input and output group
		 */

		// IN A,(n)
		case 0xDB: {
			int n = fetchWord8();
			// tmp hack
			if (n == 0) {
				System.out.print("In: ");
				int value = new Scanner(System.in).nextInt();
				registers.setRegister(Register.A, value);
			}
			break;
		}

		// OUT (n),A
		case 0xD3: {
			int n = fetchWord8();
			// tmp hack
			if (n == 1) {
				System.out.println("Out: " + registers.getRegister(Register.A));
			}
			break;
		}

		case 0xDD: {
			/*
			 * IX operations
			 */

			opCode = fetchWord8();

			switch (opCode) {

			/*
			 * 16-bit load groups
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

			}

			break;
		}

		// case 0xFD: {
		// /*
		// * IY operations
		// */
		//
		// opCode = fetchWord8();
		//
		// switch (opCode) {
		//
		// /*
		// * 8-bit arithmetic group
		// */
		//
		// // ADD A,(IY+d)
		// case 0x86: {
		// int d = fetchWord8();
		// int iy = reg.getRegister(Register.IY);
		// int n = mem.readWord8(iy + d);
		// alu.add(n);
		// break;
		// }
		//
		// }
		//
		// break;
		// }

		case 0xED: {
			/*
			 * (nn) operations?
			 */

			opCode = fetchWord8();

			switch (opCode) {

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
				int destRegCode = (opCode >> 4) & 0x03;
				Register destReg = decoder.decode(RegisterType.dd, destRegCode);
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

			}

			break;
		}

		default:
			throw new IllegalArgumentException(String.format("OpCode 0x%x not supported", opCode));
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

	private int extractLowerRegisterCode(final int opCode) {
		return opCode & 0x07;
	}

	private int extractHigherRegisterCode(final int opCode) {
		return (opCode >> 3) & 0x07;
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
