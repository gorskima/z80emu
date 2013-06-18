package gorskima.z80emu;

import static gorskima.z80emu.Flag.H;
import static gorskima.z80emu.Flag.N;
import static gorskima.z80emu.Flag.PV;
import static gorskima.z80emu.Flag.S;
import static gorskima.z80emu.Flag.Z;
import static gorskima.z80emu.Register.A;
import static gorskima.z80emu.Register.BC;
import static gorskima.z80emu.Register.HL;
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

	@Test
	public void testInc() {
		reg.setRegister(A, 255);
		alu.inc(A);
		assertThat(reg.getRegister(A), is(0));
		assertThat(reg.testFlag(S), is(false));
		assertThat(reg.testFlag(Z), is(true));
		assertThat(reg.testFlag(Flag.H), is(true));
		assertThat(reg.testFlag(PV), is(false));
		assertThat(reg.testFlag(N), is(false));
	}

	@Test
	public void testDec() {
		reg.setRegister(A, 0);
		alu.dec(A);
		assertThat(reg.getRegister(A), is(255));
		assertThat(reg.testFlag(S), is(true));
		assertThat(reg.testFlag(Z), is(false));
		assertThat(reg.testFlag(Flag.H), is(true));
		assertThat(reg.testFlag(PV), is(false));
		assertThat(reg.testFlag(N), is(true));
	}

	@Test
	public void testCp() {
		reg.setRegister(A, 7);
		alu.cp(5);
		assertThat(reg.getRegister(A), is(7));
		assertThat(reg.testFlag(S), is(false));
		assertThat(reg.testFlag(Z), is(false));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(PV), is(false));
		assertThat(reg.testFlag(N), is(true));
		assertThat(reg.testFlag(Flag.C), is(false));
	}

	@Test
	public void testCp_whenEqual() {
		reg.setRegister(A, 8);
		alu.cp(8);
		assertThat(reg.getRegister(A), is(8));
		assertThat(reg.testFlag(S), is(false));
		assertThat(reg.testFlag(Z), is(true));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(PV), is(false));
		assertThat(reg.testFlag(N), is(true));
		assertThat(reg.testFlag(Flag.C), is(false));
	}

	@Test
	public void testNeg() {
		reg.setRegister(A, 7);
		alu.neg();
		assertThat(reg.getRegister(A), is(249));
		assertThat(reg.testFlag(S), is(true));
		assertThat(reg.testFlag(Z), is(false));
		assertThat(reg.testFlag(Flag.H), is(true));
		assertThat(reg.testFlag(PV), is(false));
		assertThat(reg.testFlag(N), is(true));
		assertThat(reg.testFlag(Flag.C), is(true));
	}

	@Test
	public void testAnd() {
		reg.setRegister(A, 70);
		alu.and(200);
		assertThat(reg.getRegister(A), is(64));
		assertThat(reg.testFlag(S), is(false));
		assertThat(reg.testFlag(Z), is(false));
		assertThat(reg.testFlag(Flag.H), is(true));
		assertThat(reg.testFlag(PV), is(false));
		assertThat(reg.testFlag(N), is(false));
		assertThat(reg.testFlag(Flag.C), is(false));
	}

	@Test
	public void testOr() {
		reg.setRegister(A, 70);
		alu.or(200);
		assertThat(reg.getRegister(A), is(206));
		assertThat(reg.testFlag(S), is(true));
		assertThat(reg.testFlag(Z), is(false));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(PV), is(false));
		assertThat(reg.testFlag(N), is(false));
		assertThat(reg.testFlag(Flag.C), is(false));
	}

	@Test
	public void testXor() {
		reg.setRegister(A, 70);
		alu.xor(200);
		assertThat(reg.getRegister(A), is(142));
		assertThat(reg.testFlag(S), is(true));
		assertThat(reg.testFlag(Z), is(false));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(PV), is(true));
		assertThat(reg.testFlag(N), is(false));
		assertThat(reg.testFlag(Flag.C), is(false));
	}

	@Test
	public void testCpl() {
		reg.setRegister(A, 3);
		alu.cpl();
		assertThat(reg.getRegister(A), is(252));
		assertThat(reg.testFlag(Flag.H), is(true));
		assertThat(reg.testFlag(N), is(true));
	}

	@Test
	public void testAdd16() {
		reg.setRegister(HL, 40000);
		alu.add16(30000);
		assertThat(reg.getRegister(HL), is(4464));
		assertThat(reg.testFlag(Flag.C), is(true));
		assertThat(reg.testFlag(Flag.H), is(true));
		assertThat(reg.testFlag(N), is(false));
	}

	@Test
	public void testAdc16() {
		reg.setRegister(HL, 30000);
		reg.setFlag(Flag.C, true);
		alu.adc16(35538);
		assertThat(reg.getRegister(HL), is(3));
		assertThat(reg.testFlag(Flag.S), is(false));
		assertThat(reg.testFlag(Flag.Z), is(false));
		assertThat(reg.testFlag(Flag.H), is(true));
		assertThat(reg.testFlag(Flag.PV), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
		assertThat(reg.testFlag(Flag.C), is(true));
	}
	
	@Test
	public void testSbc16() {
		reg.setRegister(HL, 40000);
		reg.setFlag(Flag.C, true);
		alu.sbc16(20000);
		assertThat(reg.getRegister(HL), is(19999));
		assertThat(reg.testFlag(Flag.S), is(false));
		assertThat(reg.testFlag(Flag.Z), is(false));
		assertThat(reg.testFlag(Flag.H), is(true));
		assertThat(reg.testFlag(Flag.PV), is(true));
		assertThat(reg.testFlag(Flag.N), is(true));
		assertThat(reg.testFlag(Flag.C), is(false));
	}
	
	@Test
	public void testIncExtern() {
		int result = alu.incExtern(8);
		assertThat(result, is(9));
		assertThat(reg.testFlag(S), is(false));
		assertThat(reg.testFlag(Z), is(false));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(PV), is(false));
		assertThat(reg.testFlag(N), is(false));
	}
	
	@Test
	public void testDecExtern() {
		int result = alu.decExtern(128);
		assertThat(result, is(127));
		assertThat(reg.testFlag(S), is(false));
		assertThat(reg.testFlag(Z), is(false));
		assertThat(reg.testFlag(Flag.H), is(true));
		assertThat(reg.testFlag(PV), is(true));
		assertThat(reg.testFlag(N), is(true));
	}
	
	@Test
	public void testInc16() {
		reg.setRegister(HL, 40000);
		alu.inc16(HL);
		assertThat(reg.getRegister(HL), is(40001));
	}
	
	@Test
	public void testDec16() {
		reg.setRegister(BC, 40000);
		alu.dec16(BC);
		assertThat(reg.getRegister(BC), is(39999));
	}
	
	@Test
	public void testRlca() {
		reg.setRegister(A, 0x81); // 10000001
		reg.setFlag(Flag.C, false);
		reg.setFlag(H, true);
		reg.setFlag(N, true);
		
		alu.rlca();
		assertThat(reg.getRegister(A), is(0x03)); // 00000011
		assertThat(reg.testFlag(Flag.C), is(true));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
		
		alu.rlca();
		assertThat(reg.getRegister(A), is(0x06)); // 00000110
		assertThat(reg.testFlag(Flag.C), is(false));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
	}
	
	@Test
	public void testRrca() {
		reg.setRegister(A, 0x02); // 00000010
		reg.setFlag(Flag.C, false);
		reg.setFlag(H, true);
		reg.setFlag(N, true);
		
		alu.rrca();
		assertThat(reg.getRegister(A), is(0x01)); // 00000001
		assertThat(reg.testFlag(Flag.C), is(false));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
		
		alu.rrca();
		assertThat(reg.getRegister(A), is(0x80)); // 10000000
		assertThat(reg.testFlag(Flag.C), is(true));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
	}
	
	@Test
	public void testRla() {
		reg.setFlag(Flag.C, false);
		reg.setRegister(A, 0x85); // 10000101
		reg.setFlag(H, true);
		reg.setFlag(N, true);
		
		alu.rla();
		assertThat(reg.getRegister(A), is(0x0A)); // 00001010
		assertThat(reg.testFlag(Flag.C), is(true));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
		
		alu.rla();
		assertThat(reg.getRegister(A), is(0x15)); // 00010101
		assertThat(reg.testFlag(Flag.C), is(false));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
	}
	
	@Test
	public void testRca() {
		reg.setFlag(Flag.C, true);
		reg.setRegister(A, 0x06); // 00000110
		reg.setFlag(H, true);
		reg.setFlag(N, true);
		
		alu.rra();
		assertThat(reg.getRegister(A), is(0x83)); // 10000011
		assertThat(reg.testFlag(Flag.C), is(false));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
		
		alu.rra();
		assertThat(reg.getRegister(A), is(0x41)); // 01000001
		assertThat(reg.testFlag(Flag.C), is(true));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
	}

}
