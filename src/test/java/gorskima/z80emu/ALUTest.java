package gorskima.z80emu;

import static gorskima.z80emu.Flag.N;
import static gorskima.z80emu.Flag.PV;
import static gorskima.z80emu.Flag.S;
import static gorskima.z80emu.Flag.Z;
import static gorskima.z80emu.Register.A;
import static gorskima.z80emu.Register.BC;
import static gorskima.z80emu.Register.HL;
import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.base.Preconditions;

@RunWith(JUnitParamsRunner.class)
public class ALUTest {

	private Registers reg = new Registers();
	private ALU alu = new ALU(reg);

	@Test
	@Parameters
	public void testAdd(final int op1, final int op2, final int result,
			final boolean s, final boolean z, final boolean h, final boolean pv, final boolean c) {
		
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
	public void testAdc(final int op1, final boolean carry, final int op2, final int result,
			final boolean s, final boolean z, final boolean h, final boolean pv, final boolean c) {
		
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
	public void testSub(final int op1, final int op2, final int result,
			final boolean s, final boolean z, final boolean h, final boolean pv, final boolean c) {
		
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
	public void testSbc(final int op1, final boolean carry, final int op2, final int result,
			final boolean s, final boolean z, final boolean h, final boolean pv, final boolean c) {
		
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
	public void testInc(final int op, final int result,
			final boolean s, final boolean z, final boolean h, final boolean pv) {
		
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
	public void testDec(final int op, final int result,
			final boolean s, final boolean z, final boolean h, final boolean pv) {
		
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
	@Parameters
	public void testCp(final int op1, final int op2,
			final boolean s, final boolean z, final boolean h, final boolean pv, final boolean c) {
		
		reg.setRegister(A, op1);
		alu.cp(op2);
		assertThat(reg.getRegister(A), is(op1)); // always
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(true)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestCp() {
		return $(
			$(7, 5, false, false, false, false, false),
			$(8, 8, false, true, false, false, false));
	}

	@Test
	@Parameters
	public void testNeg(final int op, final int result,
			final boolean s, final boolean z, final boolean h, final boolean pv, final boolean c) {
		
		reg.setRegister(A, op);
		alu.neg();
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(true)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestNeg() {
		return $(
			$(7, 249, true, false, true, false, true));
	}

	@Test
	@Parameters
	public void testAnd(final int op1, final int op2, final int result,
			final boolean s, final boolean z, final boolean pv) {
		
		reg.setRegister(A, op1);
		alu.and(op2);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(true)); // always
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(false)); // always
	}

	private Object[] parametersForTestAnd() {
		return $(
			$(70, 200, 64, false, false, false));
	}
	
	@Test
	@Parameters
	public void testOr(final int op1, final int op2, final int result,
			final boolean s, final boolean z, final boolean pv) {
		
		reg.setRegister(A, op1);
		alu.or(op2);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(false)); // always
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(false)); // always
	}
	
	private Object[] parametersForTestOr() {
		return $(
			$(70, 200, 206, true, false, false));
	}

	@Test
	@Parameters
	public void testXor(final int op1, final int op2, final int result,
			final boolean s, final boolean z, final boolean pv) {
		
		reg.setRegister(A, op1);
		alu.xor(op2);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(false)); // always
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(false)); // always
	}
	
	private Object[] parametersForTestXor() {
		return $(
			$(70, 200, 142, true, false, true));
	}

	@Test
	@Parameters
	public void testCpl(final int op, final int result) {
		reg.setRegister(A, op);
		alu.cpl();
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(Flag.H), is(true)); // always
		assertThat(reg.testFlag(N), is(true)); // always
		// TODO check that S, Z, PV and C are unaffected
	}
	
	private Object[] parametersForTestCpl() {
		return $(
			$(3, 252),
			$(100, 155));
	}
	
	@Test
	@Parameters
	public void testAdd16(final int op1, final int op2, final int result,
			final boolean c, final boolean h) {
		
		reg.setRegister(HL, op1);
		alu.add16(op2);
		assertThat(reg.getRegister(HL), is(result));
		assertThat(reg.testFlag(Flag.C), is(c));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(N), is(false)); // always
		// TODO check that S, Z and PV are unaffected
	}
	
	private Object[] parametersForTestAdd16() {
		return $(
			$(40000, 30000, 4464, true, true));
	}

	@Test
	@Parameters
	public void testAdc16(final int op1, final boolean carry, final int op2, final int result,
			final boolean s, final boolean z, final boolean h, final boolean pv, final boolean c) {
		
		reg.setRegister(HL, op1);
		reg.setFlag(Flag.C, carry);
		alu.adc16(op2);
		assertThat(reg.getRegister(HL), is(result));
		assertThat(reg.testFlag(Flag.S), is(s));
		assertThat(reg.testFlag(Flag.Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(Flag.PV), is(pv));
		assertThat(reg.testFlag(Flag.N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestAdc16() {
		return $(
			$(30000, true, 35538, 3, false, false, true, false, true));
	}
	
	@Test
	@Parameters
	public void testSbc16(final int op1, final boolean carry, final int op2, final int result,
			final boolean s, final boolean z, final boolean h, final boolean pv, final boolean c) {
		
		reg.setRegister(HL, op1);
		reg.setFlag(Flag.C, carry);
		alu.sbc16(op2);
		assertThat(reg.getRegister(HL), is(result));
		assertThat(reg.testFlag(Flag.S), is(s));
		assertThat(reg.testFlag(Flag.Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(Flag.PV), is(pv));
		assertThat(reg.testFlag(Flag.N), is(true)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestSbc16() {
		return $(
			$(40000, true, 20000, 19999, false, false, true, true, false));
	}
	
	@Test
	@Parameters
	public void testIncExtern(final int op, final int result,
			final boolean s, final boolean z, final boolean h, final boolean pv) {
		
		int incremented = alu.incExtern(op);
		assertThat(incremented, is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(false)); // always
		// TODO check that C is unaffected
	}
	
	private Object[] parametersForTestIncExtern() {
		return $(
			$(8, 9, false, false, false, false));
	}
	
	@Test
	@Parameters
	public void testDecExtern(final int op, final int result,
			final boolean s, final boolean z, final boolean h, final boolean pv) {
		
		int decremented = alu.decExtern(op);
		assertThat(decremented, is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(true)); // always
		// TODO check that C is unaffected
	}
	
	private Object[] parametersForTestDecExtern() {
		return $(
			$(128, 127, false, false, true, true));
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
	@Parameters
	public void testRlca(final int op, final int result, final boolean c) {
		reg.setRegister(A, op);
		alu.rlca();
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(Flag.H), is(false)); // always
		assertThat(reg.testFlag(Flag.N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
		// TODO check that S, Z and PV are unaffected
	}
	
	private Object[] parametersForTestRlca() {
		return $(
			$(b("10000001"), b("00000011"), true),
			$(b("00000011"), b("00000110"), false));
	}
	
	@Test
	@Parameters
	public void testRrca(final int op, final int result, final boolean c) {
		reg.setRegister(A, op);
		alu.rrca();
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(Flag.H), is(false)); // always
		assertThat(reg.testFlag(Flag.N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
		// TODO check that S, Z and PV are unaffected
	}
	
	private Object[] parametersForTestRrca() {
		return $(
			$(b("00000010"), b("00000001"), false),
			$(b("00001101"), b("10000110"), true));
	}
	
	@Test
	@Parameters
	public void testRla(final int op, final boolean carry, final int result, final boolean c) {
		reg.setRegister(A, op);
		reg.setFlag(Flag.C, carry);
		alu.rla();
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(Flag.H), is(false)); // always
		assertThat(reg.testFlag(Flag.N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
		// TODO check that S, Z and PV are unaffected
	}
	
	private Object[] parametersForTestRla() {
		return $(
			$(b("10000101"), false, b("00001010"), true),
			$(b("00001010"), true, b("00010101"), false));
	}
	
	@Test
	@Parameters
	public void testRra(final int op, final boolean carry, final int result, final boolean c) {
		reg.setRegister(A, op); 
		reg.setFlag(Flag.C, carry);
		alu.rra();
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(Flag.H), is(false)); // always
		assertThat(reg.testFlag(Flag.N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
		// TODO check that S, Z and PV are unaffected
	}
	
	private Object[] parametersForTestRra() {
		return $(
			$(b("00000110"), true, b("10000011"), false),
			$(b("10000011"), false, b("01000001"), true));
	}

	private static int b(final String bits) {
		Preconditions.checkArgument(bits.length() == 8);
		int b = 0;
		for (byte c : bits.getBytes()) {
			b <<= 1;
			if (c == '1') {
				b |= 0x01;
			}
		}
		return b;
	}
	
}
