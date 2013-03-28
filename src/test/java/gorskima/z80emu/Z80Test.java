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
		mem.writeWord8(2, 7);
		mem.writeWord8(107, 86);
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
		mem.writeWord8(0, 0x72);
		cpu.step();
		assertThat(mem.readWord8(105), is(9));
	}
	
	@Test
	public void test_LD_HL_n() {
		reg.setRegister(HL, 200);
		mem.writeWord8(0, 0x36);
		mem.writeWord8(1, 55);
		cpu.step();
		assertThat(mem.readWord8(200), is(55));
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
		mem.writeWord8(0, 0x3A);
		mem.writeWord16(1, 25000);
		mem.writeWord8(25000, 7);
		cpu.step();
		assertThat(reg.getRegister(A), is(7));
	}

	@Test
	public void test_LD_BC_A() {
		reg.setRegister(A, 59);
		reg.setRegister(BC, 13300);
		mem.writeWord8(0, 0x02);
		cpu.step();
		assertThat(mem.readWord8(13300), is(59));
	}

	@Test
	public void test_LD_DE_A() {
		reg.setRegister(A, 17);
		reg.setRegister(DE, 5000);
		mem.writeWord8(0, 0x12);
		cpu.step();
		assertThat(mem.readWord8(5000), is(17));
	}

	@Test
	public void test_LD_nn_A() {
		reg.setRegister(A, 15);
		mem.writeWord8(0, 0x32);
		mem.writeWord16(1, 43000);
		cpu.step();
		assertThat(mem.readWord8(43000), is(15));
	}

	@Test
	public void test_LD_A_I() {
		reg.setRegister(I, 190);
		mem.writeWord8(0, 0xED);
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
		mem.writeWord8(0, 0xED);
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
		mem.writeWord8(0, 0xED);
		mem.writeWord8(1, 0x47);
		cpu.step();
		assertThat(reg.getRegister(I), is(117));
	}

	@Test
	public void test_LD_R_A() {
		reg.setRegister(A, 98);
		mem.writeWord8(0, 0xED);
		mem.writeWord8(1, 0x4F);
		cpu.step();
		assertThat(reg.getRegister(R), is(98));
	}

}
