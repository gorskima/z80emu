package gorskima.z80emu;

import static gorskima.z80emu.Flag.H;
import static junitparams.JUnitParamsRunner.$;
import static gorskima.z80emu.Flag.N;
import static gorskima.z80emu.Flag.PV;
import static gorskima.z80emu.Flag.S;
import static gorskima.z80emu.Flag.Z;
import static gorskima.z80emu.Register.A;
import static gorskima.z80emu.Register.BC;
import static gorskima.z80emu.Register.HL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class ALUTest {

	private Registers reg = new Registers();
	private ALU alu = new ALU(reg);

	@Test
	@Parameters
	public void testAdd(int op1, int op2, int result,
			boolean s, boolean z, boolean h, boolean pv, boolean c) {
		
		reg.setRegister(A, op1);
		alu.add(op2);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestAdd() {
		return $(
			$(102, 38, 140, true, false, false, true, false));
	}

	@Test
	@Parameters
	public void testAdc(int op1, boolean carry, int op2, int result,
			boolean s, boolean z, boolean h, boolean pv, boolean c) {
		
		reg.setRegister(A, op1);
		reg.setFlag(Flag.C, carry);
		alu.adc(op2);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestAdc() {
		return $(
			$(75, true, 200, 20, false, false, true, false, true));
	}

	@Test
	@Parameters
	public void testSub(int op1, int op2, int result,
			boolean s, boolean z, boolean h, boolean pv, boolean c) {
		
		reg.setRegister(A, op1);
		alu.sub(op2);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(true)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestSub() {
		return $(
			$(80, 95, 241, true, false, true, false, true));
	}

	@Test
	@Parameters
	public void testSbc(int op1, boolean carry, int op2, int result,
			boolean s, boolean z, boolean h, boolean pv, boolean c) {
		
		reg.setRegister(A, op1);
		reg.setFlag(Flag.C, carry);
		alu.sbc(op2);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(true)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestSbc() {
		return $(
			$(200, true, 100, 99, false, false, false, true, false));
	}

	@Test
	@Parameters
	public void testInc(int op, int result,
			boolean s, boolean z, boolean h, boolean pv) {
		
		reg.setRegister(A, op);
		alu.inc(A);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(false)); // always
		// TODO check that C is unaffected
	}
	
	private Object[] parametersForTestInc() {
		return $(
			$(255, 0, false, true, true, false));
	}

	@Test
	@Parameters
	public void testDec(int op, int result,
			boolean s, boolean z, boolean h, boolean pv) {
		
		reg.setRegister(A, op);
		alu.dec(A);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(true)); // always
		// TODO check that C is unaffected
	}

	private Object[] parametersForTestDec() {
		return $(
			$(0, 255, true, false, true, false));
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
		alu.inc(HL);
		assertThat(reg.getRegister(HL), is(40001));
	}
	
	@Test
	public void testDec16() {
		reg.setRegister(BC, 40000);
		alu.dec(BC);
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
	public void testRra() {
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
