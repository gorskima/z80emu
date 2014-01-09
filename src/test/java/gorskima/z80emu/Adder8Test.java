package gorskima.z80emu;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class Adder8Test {

	@Test
	@Parameters
	public void testAddWithCarry(final int op1, final int op2, final int carryIn, final int expectedResult,
			final int expectedCarry) {
		
		Adder adder = Adder.newAdder8();
		int actualResult = adder.add(op1, op2, carryIn);
		int actualCarry = adder.isCarry() ? 1 : 0;
		assertEquals("Add result mismatch", expectedResult, actualResult);
		assertEquals("Add carry mismatch", expectedCarry, actualCarry);
	}
	
	private Object[] parametersForTestAddWithCarry() {
		return $(
			$(0, 0, 0, 0, 0),
			$(1, 0, 0, 1, 0),
			$(0, 1, 0, 1, 0),
			$(1, 0, 1, 2, 0),
			$(0, 1, 1, 2, 0),
	
			$(255, 0, 0, 255, 0),
			$(0, 255, 0, 255, 0),
			$(255, 0, 1, 0, 1),
			$(0, 255, 1, 0, 1),
			$(255, 1, 0, 0, 1),
	
			$(128, 128, 0, 0, 1),
			$(128, 128, 1, 1, 1),
			$(127, 127, 0, 254, 0),
	
			$(255, 255, 0, 254, 1),
			$(255, 255, 1, 255, 1));
	}

	@Test
	@Parameters
	public void testSubWithBorrow(final int op1, final int op2, final int carryIn, final int expectedResult,
			final int expectedCarry) {
		Adder adder = Adder.newAdder8();
		int actualResult = adder.sub(op1, op2, carryIn);
		int actualCarry = adder.isBorrow() ? 1 : 0;
		assertEquals("Sub result mismatch", expectedResult, actualResult);
		assertEquals("Sub carry mismatch", expectedCarry, actualCarry);
	}
	
	private Object[] parametersForTestSubWithBorrow() {
		return $(
			$(0, 0, 0, 0, 0),
			$(1, 0, 0, 1, 0),
			$(1, 1, 0, 0, 0),
			$(1, 0, 1, 0, 0),

			$(255, 254, 1, 0, 0),
			$(255, 255, 0, 0, 0),
			$(255, 255, 1, 255, 1),

			$(0, 0, 1, 255, 1),
			$(0, 1, 0, 255, 1),
			$(0, 1, 1, 254, 1),

			$(0, 255, 0, 1, 1),
			$(0, 255, 1, 0, 1),

			$(128, 128, 0, 0, 0),
			$(128, 128, 1, 255, 1));
	}

	@Test
	@Parameters
	public void testAddWithOverflow(final int op1, final int op2, final int carryIn, final int expectedOverflow) {
		Adder adder = Adder.newAdder8();
		adder.add(op1, op2, carryIn);
		int actualOverflow = adder.isOverflow() ? 1 : 0;
		assertEquals("Add overflow mismatch", expectedOverflow, actualOverflow);
	}
	
	private Object[] parametersForTestAddWithOverflow() {
		return $(
			$(1, 1, 0, 0),
			$(1, -1, 0, 0),
			$(127, 1, 0, 1),
			$(-128, 1, 0, 0),
			$(-128, -1, 0, 1));
	}

	@Test
	@Parameters
	public void testSubWithOverflow(final int op1, final int op2, final int carryIn, final int expectedOverflow) {
		Adder adder = Adder.newAdder8();
		adder.sub(op1, op2, carryIn);
		int actualOverflow = adder.isOverflow() ? 1 : 0;
		assertEquals("Sub overflow mismatch", expectedOverflow, actualOverflow);
	}
	
	private Object[] parametersForTestSubWithOverflow() {
		return $(
			$(0, 1, 0, 0),
			$(-128, 1, 0, 1),
			$(127, -1, 0, 1));
	}

	@Test
	@Parameters
	public void testAddWithHalfCarry(final int op1, final int op2, final int carryIn, final int expectedHalfCarry) {
		Adder adder = Adder.newAdder8();
		adder.add(op1, op2, carryIn);
		int actualHalfCarry = adder.isHalfCarry() ? 1 : 0;
		assertEquals("Add half-carry mismatch", expectedHalfCarry,
				actualHalfCarry);
	}
	
	private Object[] parametersForTestAddWithHalfCarry() {
		return $($(0, 0, 0, 0),
			$(7, 7, 0, 0),
			$(7, 8, 0, 0),
			$(8, 8, 0, 1),
			$(10, 10, 0, 1),
			$(15, 15, 0, 1),
			$(16, 15, 0, 0),
			$(16, 16, 0, 0),
			$(15, 14, 1, 1),
			$(14, 15, 1, 1),
			$(15, 1, 1, 1),
			$(7, 7, 1, 0),
			$(7, 8, 1, 1),
			$(8, 7, 1, 1));
	}

	@Test
	@Parameters
	public void testSubWithHalfBorrow(final int op1, final int op2, final int carryIn, final int expectedHalfCarry) {
		Adder adder = Adder.newAdder8();
		adder.sub(op1, op2, carryIn);
		int actualHalfCarry = adder.isHalfBorrow() ? 1 : 0;
		assertEquals("Add half-carry mismatch", expectedHalfCarry,
				actualHalfCarry);
	}
	
	private Object[] parametersForTestSubWithHalfBorrow() {
		return $(
			$(0, 0, 0, 0),
			$(16, 0, 0, 0),
			$(16, 1, 0, 1),
			$(0, 1, 0, 1));
	}

}
