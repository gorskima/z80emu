package gorskima.z80emu;

import static gorskima.z80emu.Flag.N;
import static gorskima.z80emu.Flag.PV;
import static gorskima.z80emu.Flag.S;
import static gorskima.z80emu.Flag.Z;
import static gorskima.z80emu.Register.A;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class ALUTest {

	private Registers reg = new Registers();
	private ALU alu = new ALU(reg);

	@Test
	public void testAdd() {
		reg.setRegister(A, 102);
		alu.add(38);
		assertThat(reg.getRegister(A), is(140));
		assertThat(reg.testFlag(S), is(true));
		assertThat(reg.testFlag(Z), is(false));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(PV), is(true));
		assertThat(reg.testFlag(N), is(false));
		assertThat(reg.testFlag(Flag.C), is(false));
	}
	
	@Test
	public void testAdc() {
		reg.setRegister(A, 75);
		reg.setFlag(Flag.C, true);
		alu.adc(200);
		assertThat(reg.getRegister(A), is(20));
		assertThat(reg.testFlag(S), is(false));
		assertThat(reg.testFlag(Z), is(false));
		assertThat(reg.testFlag(Flag.H), is(true));
		assertThat(reg.testFlag(PV), is(false));
		assertThat(reg.testFlag(N), is(false));
		assertThat(reg.testFlag(Flag.C), is(true));
	}

	@Test
	public void testSub() {
		reg.setRegister(A, 80);
		alu.sub(95);
		assertThat(reg.getRegister(A), is(241));
		assertThat(reg.testFlag(S), is(true));
		assertThat(reg.testFlag(Z), is(false));
		assertThat(reg.testFlag(Flag.H), is(true));
		assertThat(reg.testFlag(PV), is(false));
		assertThat(reg.testFlag(N), is(true));
		assertThat(reg.testFlag(Flag.C), is(true));
	}

	@Test
	public void testSbc() {
		reg.setRegister(A, 200);
		reg.setFlag(Flag.C, true);
		alu.sbc(100);
		assertThat(reg.getRegister(A), is(99));
		assertThat(reg.testFlag(S), is(false));
		assertThat(reg.testFlag(Z), is(false));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(PV), is(true));
		assertThat(reg.testFlag(N), is(true));
		assertThat(reg.testFlag(Flag.C), is(false));
	}

}
