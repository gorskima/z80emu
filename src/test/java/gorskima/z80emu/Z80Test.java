package gorskima.z80emu;

import static gorskima.z80emu.Flag.H;
import static gorskima.z80emu.Flag.N;
import static gorskima.z80emu.Flag.S;
import static gorskima.z80emu.Flag.Z;
import static gorskima.z80emu.Register.A;
import static gorskima.z80emu.Register.B;
import static gorskima.z80emu.Register.BC;
import static gorskima.z80emu.Register.C;
import static gorskima.z80emu.Register.D;
import static gorskima.z80emu.Register.DE;
import static gorskima.z80emu.Register.E;
import static gorskima.z80emu.Register.HL;
import static gorskima.z80emu.Register.I;
import static gorskima.z80emu.Register.IX;
import static gorskima.z80emu.Register.IY;
import static gorskima.z80emu.Register.L;
import static gorskima.z80emu.Register.R;
import static gorskima.z80emu.Register.SP;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class Z80Test {

	private Registers reg = new Registers();
	private Memory mem = new Memory();
	private Z80 cpu = new Z80(reg, mem);

	@Test
	public void test_LD_r_r() {
		reg.setRegister(B, 58);
		mem.writeWord8(0, 0x78); // LD A,B
		cpu.step();
		assertThat(reg.getRegister(A), is(58));
	}

	@Test
	public void test_LD_r_n() {
		mem.writeWord8(0, 0x06); // LD B,123
		mem.writeWord8(1, 123);
		cpu.step();
		assertThat(reg.getRegister(B), is(123));
	}

	@Test
	public void test_LD_r_HL() {
		reg.setRegister(HL, 350);
		mem.writeWord8(0, 0x4E); // LD C,(HL)
		mem.writeWord8(350, 718);
		cpu.step();
		assertThat(reg.getRegister(C), is(718));
	}

	@Test
	public void test_LD_r_IX_plus_d() {
		reg.setRegister(IX, 100);
		mem.writeWord8(0, 0xDD); // LD E,(IX+7)
		mem.writeWord8(1, 0x5E);
		mem.writeWord8(2, 250);
		mem.writeWord8(94, 86);
		cpu.step();
		assertThat(reg.getRegister(E), is(86));
	}

	@Test
	public void test_LD_r_IY_plus_d() {
		reg.setRegister(IY, 75);
		mem.writeWord8(0, 0xFD); // LD L,(IY+7)
		mem.writeWord8(1, 0x6E);
		mem.writeWord8(2, 8);
		mem.writeWord8(83, 37);
		cpu.step();
		assertThat(reg.getRegister(L), is(37));
	}

	@Test
	public void test_LD_HL_r() {
		reg.setRegister(D, 9);
		reg.setRegister(HL, 105);
		mem.writeWord8(0, 0x72); // LD (HL),D
		cpu.step();
		assertThat(mem.readWord8(105), is(9));
	}
	
	@Test
	public void test_LD_IX_plus_d_r() {
		reg.setRegister(IX, 100);
		reg.setRegister(E, 55);
		mem.writeWord8(0, 0xDD); // LD (IX+(-3)),E
		mem.writeWord8(1, 0x73);
		mem.writeWord8(2, 253);
		cpu.step();
		assertThat(mem.readWord8(97), is(55));
	}

	@Test
	public void test_LD_IY_plus_d_r() {
		reg.setRegister(IY, 500);
		reg.setRegister(B, 71);
		mem.writeWord8(0, 0xFD); // LD (IX+75),B
		mem.writeWord8(1, 0x70);
		mem.writeWord8(2, 75);
		cpu.step();
		assertThat(mem.readWord8(575), is(71));
	}

	@Test
	public void test_LD_HL_n() {
		reg.setRegister(HL, 200);
		mem.writeWord8(0, 0x36); // LD (HL),55
		mem.writeWord8(1, 55);
		cpu.step();
		assertThat(mem.readWord8(200), is(55));
	}

	@Test
	public void test_LD_IX_plus_d_n() {
		reg.setRegister(IX, 100);
		mem.writeWord8(0, 0xDD); // LD (IX+88),7
		mem.writeWord8(1, 0x36);
		mem.writeWord8(2, 88);
		mem.writeWord8(3, 7);
		cpu.step();
		assertThat(mem.readWord8(188), is(7));
	}

	@Test
	public void test_LD_IY_plus_d_n() {
		reg.setRegister(IY, 30000);
		mem.writeWord8(0, 0xFD); // LD (IY+(-10)),21
		mem.writeWord8(1, 0x36);
		mem.writeWord8(2, 246);
		mem.writeWord8(3, 21);
		cpu.step();
		assertThat(mem.readWord8(29990), is(21));
	}

	@Test
	public void test_LD_A_BC() {
		reg.setRegister(BC, 2000);
		mem.writeWord16(0, 0x0A); // LD A,(BC)
		mem.writeWord8(2000, 33);
		cpu.step();
		assertThat(reg.getRegister(A), is(33));
	}

	@Test
	public void test_LD_A_DE() {
		reg.setRegister(DE, 5000);
		mem.writeWord16(0, 0x1A); // LD A,(DE)
		mem.writeWord8(5000, 123);
		cpu.step();
		assertThat(reg.getRegister(A), is(123));
	}

	@Test
	public void test_LD_A_nn() {
		mem.writeWord8(0, 0x3A); // LD A,(25000)
		mem.writeWord16(1, 25000);
		mem.writeWord8(25000, 7);
		cpu.step();
		assertThat(reg.getRegister(A), is(7));
	}

	@Test
	public void test_LD_BC_A() {
		reg.setRegister(A, 59);
		reg.setRegister(BC, 13300);
		mem.writeWord8(0, 0x02); // LD (BC),A
		cpu.step();
		assertThat(mem.readWord8(13300), is(59));
	}

	@Test
	public void test_LD_DE_A() {
		reg.setRegister(A, 17);
		reg.setRegister(DE, 5000);
		mem.writeWord8(0, 0x12); // LD (DE),A
		cpu.step();
		assertThat(mem.readWord8(5000), is(17));
	}

	@Test
	public void test_LD_nn_A() {
		reg.setRegister(A, 15);
		mem.writeWord8(0, 0x32); // LD (43000),A
		mem.writeWord16(1, 43000);
		cpu.step();
		assertThat(mem.readWord8(43000), is(15));
	}

	@Test
	public void test_LD_A_I() {
		reg.setRegister(I, 190);
		mem.writeWord8(0, 0xED); // LD A,I
		mem.writeWord8(1, 0x57);
		cpu.step();
		assertThat(reg.getRegister(A), is(190));
		assertThat(reg.testFlag(S), is(true));
		assertThat(reg.testFlag(Z), is(false));
		assertThat(reg.testFlag(H), is(false));
		// TODO test PV flag (interrupt related)
		assertThat(reg.testFlag(N), is(false));
		assertThat(reg.testFlag(Flag.C), is(false));
	}

	@Test
	public void test_LD_A_R() {
		reg.setRegister(R, 215);
		mem.writeWord8(0, 0xED); // LD A,R
		mem.writeWord8(1, 0x5F);
		cpu.step();
		assertThat(reg.getRegister(A), is(215));
		assertThat(reg.testFlag(S), is(true));
		assertThat(reg.testFlag(Z), is(false));
		assertThat(reg.testFlag(H), is(false));
		// TODO test PV flag (interrupt related)
		assertThat(reg.testFlag(N), is(false));
		assertThat(reg.testFlag(Flag.C), is(false));
	}

	@Test
	public void test_LD_I_A() {
		reg.setRegister(A, 117);
		mem.writeWord8(0, 0xED); // LD I,A
		mem.writeWord8(1, 0x47);
		cpu.step();
		assertThat(reg.getRegister(I), is(117));
	}

	@Test
	public void test_LD_R_A() {
		reg.setRegister(A, 98);
		mem.writeWord8(0, 0xED); // LD R,A
		mem.writeWord8(1, 0x4F);
		cpu.step();
		assertThat(reg.getRegister(R), is(98));
	}

	@Test
	public void test_LD_dd_nn() {
		mem.writeWord8(0, 0x11); // LD DE,12345
		mem.writeWord16(1, 12345);
		cpu.step();
		assertThat(reg.getRegister(DE), is(12345));
	}

	@Test
	public void test_LD_HL_nn() {
		mem.writeWord8(0, 0x2A); // LD HL,(nn)
		mem.writeWord16(1, 0x5577);
		mem.writeWord16(0x5577, 0x1234);
		cpu.step();
		assertThat(reg.getRegister(HL), is(0x1234));
	}

	@Test
	public void test_LD_dd__nn() { // TODO change name
		mem.writeWord8(0, 0xED);
		mem.writeWord8(1, 0x4B); // LD BC,(1000)
		mem.writeWord16(2, 1000);
		mem.writeWord8(1000, 123);
		cpu.step();
		assertThat(reg.getRegister(BC), is(123));
	}

	@Test
	public void test_LD_nn_HL() {
		reg.setRegister(HL, 0x1520);
		mem.writeWord8(0, 0x22); // LD (nn),HL
		mem.writeWord16(1, 0x0040);
		cpu.step();
		assertThat(mem.readWord16(0x0040), is(0x1520));
	}

	@Test
	public void test_LD_SP_HL() {
		reg.setRegister(HL, 25000);
		mem.writeWord8(0, 0xF9); // LD SP,HL
		cpu.step();
		assertThat(reg.getRegister(SP), is(25000));
	}

	@Test
	public void test_PUSH_qq() {
		reg.setRegister(SP, 0xFFFF);
		reg.setRegister(BC, 0x2277);
		mem.writeWord8(0, 0xC5); // PUSH BC
		cpu.step();
		assertThat(reg.getRegister(SP), is(0xFFFD));
		assertThat(mem.readWord8(0xFFFD), is(0x77));
		assertThat(mem.readWord8(0xFFFE), is(0x22));
	}

	@Test
	public void test_POP_qq() {
		reg.setRegister(SP, 0xFFF0);
		mem.writeWord8(0, 0xE1); // PUSH HL
		mem.writeWord8(0xFFF0, 0x34);
		mem.writeWord8(0xFFF1, 0x12);
		cpu.step();
		assertThat(reg.getRegister(HL), is(0x1234));
		assertThat(reg.getRegister(SP), is(0xFFF2));
	}
	
	@Test
	public void test_ADD_HL_ss() {
		reg.setRegister(HL, 10000);
		mem.writeWord16(0, 0x29); // ADD HL,HL
		cpu.step();
		assertThat(reg.getRegister(HL), is(20000));
	}
	
	@Test
	public void test_ADC_HL_ss() {
		reg.setRegister(BC, 30000);
		reg.setRegister(HL, 20000);
		reg.setFlag(Flag.C, true);
		mem.writeWord8(0, 0xED);
		mem.writeWord8(1, 0x4A);
		cpu.step();
		assertThat(reg.getRegister(HL), is(50001));
	}
	
	@Test
	public void test_SBC_HL_ss() {
		reg.setRegister(HL, 10000);
		reg.setRegister(SP, 10000);
		reg.setFlag(Flag.C, true);
		mem.writeWord8(0, 0xED);
		mem.writeWord8(1, 0x72);
		cpu.step();
		assertThat(reg.getRegister(HL), is(65535));
	}

	@Test
	public void test_ADD_IX_pp() {
		reg.setRegister(IX, 1000);
		reg.setRegister(SP, 500);
		mem.writeWord8(0, 0xDD); // ADD IX,SP
		mem.writeWord8(1, 0x39);
		cpu.step();
		assertThat(reg.getRegister(IX), is(1500));
	}

}
