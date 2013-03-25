package gorskima.z80emu;

import static gorskima.z80emu.CpuRunner.run;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class Z80IntegrationTest {

	@Test
	public void testHalting() {
		Z80 cpu = run("halt/code.bin");
		assertThat(cpu.isHalt(), is(true));
	}

	@Test
	public void testTwoNOPsAndThenHALT() {
		Z80 cpu = run("nop_halt/code.bin");
		assertThat(cpu.isHalt(), is(true));
	}

	@Test
	public void testLoadingNumberIntoRegister() {
		Z80 cpu = run("ld_r_n/code.bin");
		assertThat(cpu.getReg().getRegister(Register.A), is(5));
	}

}
