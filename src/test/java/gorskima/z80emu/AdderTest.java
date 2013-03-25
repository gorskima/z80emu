package gorskima.z80emu;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AdderTest {

	@Test
	public void testAdd() {
		addResultAndCarry(0, 0, 0, 0, 0);
		addResultAndCarry(1, 0, 0, 1, 0);
		addResultAndCarry(0, 1, 0, 1, 0);
		addResultAndCarry(1, 0, 1, 2, 0);
		addResultAndCarry(0, 1, 1, 2, 0);

		addResultAndCarry(255, 0, 0, 255, 0);
		addResultAndCarry(0, 255, 0, 255, 0);
		addResultAndCarry(255, 0, 1, 0, 1);
		addResultAndCarry(0, 255, 1, 0, 1);
		addResultAndCarry(255, 1, 0, 0, 1);

		addResultAndCarry(128, 128, 0, 0, 1);
		addResultAndCarry(128, 128, 1, 1, 1);
		addResultAndCarry(127, 127, 0, 254, 0);

		addResultAndCarry(255, 255, 0, 254, 1);
		addResultAndCarry(255, 255, 1, 255, 1);
	}

	@Test
	public void testSub() {
		sub(0, 0, 0, 0, 0);
		sub(1, 0, 0, 1, 0);
		sub(1, 1, 0, 0, 0);
		sub(1, 0, 1, 0, 0);

		sub(255, 254, 1, 0, 0);
		sub(255, 255, 0, 0, 0);
		sub(255, 255, 1, 255, 1);

		sub(0, 0, 1, 255, 1);
		sub(0, 1, 0, 255, 1);
		sub(0, 1, 1, 254, 1);

		sub(0, 255, 0, 1, 1);
		sub(0, 255, 1, 0, 1);

		sub(128, 128, 0, 0, 0);
		sub(128, 128, 1, 255, 1);
	}

	@Test
	public void testAddOverflow() {
		addOver(1, 1, 0, 0);
		addOver(1, -1, 0, 0);
		addOver(127, 1, 0, 1);
		addOver(-128, 1, 0, 0);
		addOver(-128, -1, 0, 1);
	}

	@Test
	public void testSubOverflow() {
		subOverflow(0, 1, 0, 0);
		subOverflow(-128, 1, 0, 1);
		subOverflow(127, -1, 0, 1);
	}

	@Test
	public void testAddHalfCarry() {
		addHalfCarry(0, 0, 0, 0);
		addHalfCarry(7, 7, 0, 0);
		addHalfCarry(7, 8, 0, 0);
		addHalfCarry(8, 8, 0, 1);
		addHalfCarry(10, 10, 0, 1);
		addHalfCarry(15, 15, 0, 1);
		addHalfCarry(16, 15, 0, 0);
		addHalfCarry(16, 16, 0, 0);
		addHalfCarry(15, 14, 1, 1);
		addHalfCarry(14, 15, 1, 1);
		addHalfCarry(15, 1, 1, 1);
		addHalfCarry(7, 7, 1, 0);
		addHalfCarry(7, 8, 1, 1);
		addHalfCarry(8, 7, 1, 1);
	}

	@Test
	public void testSubHalfBorrow() {
		subHalfCarry(0, 0, 0, 0);
		subHalfCarry(16, 0, 0, 0);
		subHalfCarry(16, 1, 0, 1);
		subHalfCarry(0, 1, 0, 1);
	}

	private void addResultAndCarry(final int op1, final int op2, final int carryIn,
			final int expectedResult, final int expectedCarry) {
		Adder adder = new Adder();
		int actualResult = adder.add(op1, op2, carryIn);
		int actualCarry = adder.isCarry() ? 1 : 0;
		assertEquals("Add result mismatch", expectedResult, actualResult);
		assertEquals("Add carry mismatch", expectedCarry, actualCarry);
	}

	private void sub(final int op1, final int op2, final int carryIn, final int expectedResult,
			final int expectedCarry) {
		Adder adder = new Adder();
		int actualResult = adder.sub(op1, op2, carryIn);
		int actualCarry = adder.isBorrow() ? 1 : 0;
		assertEquals("Sub result mismatch", expectedResult, actualResult);
		assertEquals("Sub carry mismatch", expectedCarry, actualCarry);
	}

	private void addOver(final int op1, final int op2, final int carryIn, final int expectedOverflow) {
		Adder adder = new Adder();
		adder.add(op1, op2, carryIn);
		int actualOverflow = adder.isOverflow() ? 1 : 0;
		assertEquals("Add overflow mismatch", expectedOverflow, actualOverflow);
	}

	private void subOverflow(final int op1, final int op2, final int carryIn, final int expectedOverflow) {
		Adder adder = new Adder();
		adder.sub(op1, op2, carryIn);
		int actualOverflow = adder.isOverflow() ? 1 : 0;
		assertEquals("Sub overflow mismatch", expectedOverflow, actualOverflow);
	}

	private void addHalfCarry(final int op1, final int op2, final int carryIn,
			final int expectedHalfCarry) {
		Adder adder = new Adder();
		adder.add(op1, op2, carryIn);
		int actualHalfCarry = adder.isHalfCarry() ? 1 : 0;
		assertEquals("Add half-carry mismatch", expectedHalfCarry,
				actualHalfCarry);
	}

	private void subHalfCarry(final int op1, final int op2, final int carryIn,
			final int expectedHalfCarry) {
		Adder adder = new Adder();
		adder.sub(op1, op2, carryIn);
		int actualHalfCarry = adder.isHalfBorrow() ? 1 : 0;
		assertEquals("Add half-carry mismatch", expectedHalfCarry,
				actualHalfCarry);
	}

}
