package gorskima.z80emu;

import static gorskima.z80emu.Register.A;
import static gorskima.z80emu.Register.B;
import static gorskima.z80emu.Register.C;
import static gorskima.z80emu.Register.D;
import static gorskima.z80emu.Register.E;
import static gorskima.z80emu.Register.HL;
import static gorskima.z80emu.Register.IX;
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
	public void test_LD_HL_r() {
		reg.setRegister(D, 9);
		reg.setRegister(HL, 105);
		mem.writeWord8(0, 0x72);
		cpu.step();
		assertThat(mem.readWord8(105), is(9));
	}

}
