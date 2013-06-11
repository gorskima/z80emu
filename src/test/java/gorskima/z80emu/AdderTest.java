package gorskima.z80emu;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AdderTest {

	@Test
	public void testAddWithCarry() {
		addWithCarry(0, 0, 0, 0, 0);
		addWithCarry(1, 0, 0, 1, 0);
		addWithCarry(0, 1, 0, 1, 0);
		addWithCarry(1, 0, 1, 2, 0);
		addWithCarry(0, 1, 1, 2, 0);

		addWithCarry(255, 0, 0, 255, 0);
		addWithCarry(0, 255, 0, 255, 0);
		addWithCarry(255, 0, 1, 0, 1);
		addWithCarry(0, 255, 1, 0, 1);
		addWithCarry(255, 1, 0, 0, 1);

		addWithCarry(128, 128, 0, 0, 1);
		addWithCarry(128, 128, 1, 1, 1);
		addWithCarry(127, 127, 0, 254, 0);

		addWithCarry(255, 255, 0, 254, 1);
		addWithCarry(255, 255, 1, 255, 1);
	}

	@Test
	public void testSubWithBorrow() {
		subWithBorrow(0, 0, 0, 0, 0);
		subWithBorrow(1, 0, 0, 1, 0);
		subWithBorrow(1, 1, 0, 0, 0);
		subWithBorrow(1, 0, 1, 0, 0);

		subWithBorrow(255, 254, 1, 0, 0);
		subWithBorrow(255, 255, 0, 0, 0);
		subWithBorrow(255, 255, 1, 255, 1);

		subWithBorrow(0, 0, 1, 255, 1);
		subWithBorrow(0, 1, 0, 255, 1);
		subWithBorrow(0, 1, 1, 254, 1);

		subWithBorrow(0, 255, 0, 1, 1);
		subWithBorrow(0, 255, 1, 0, 1);

		subWithBorrow(128, 128, 0, 0, 0);
		subWithBorrow(128, 128, 1, 255, 1);
	}

	@Test
	public void testAddWithOverflow() {
		addWithOverflow(1, 1, 0, 0);
		addWithOverflow(1, -1, 0, 0);
		addWithOverflow(127, 1, 0, 1);
		addWithOverflow(-128, 1, 0, 0);
		addWithOverflow(-128, -1, 0, 1);
	}

	@Test
	public void testSubWithOverflow() {
		subWithOverflow(0, 1, 0, 0);
		subWithOverflow(-128, 1, 0, 1);
		subWithOverflow(127, -1, 0, 1);
	}

	@Test
	public void testAddWithHalfCarry() {
		addWithHalfCarry(0, 0, 0, 0);
		addWithHalfCarry(7, 7, 0, 0);
		addWithHalfCarry(7, 8, 0, 0);
		addWithHalfCarry(8, 8, 0, 1);
		addWithHalfCarry(10, 10, 0, 1);
		addWithHalfCarry(15, 15, 0, 1);
		addWithHalfCarry(16, 15, 0, 0);
		addWithHalfCarry(16, 16, 0, 0);
		addWithHalfCarry(15, 14, 1, 1);
		addWithHalfCarry(14, 15, 1, 1);
		addWithHalfCarry(15, 1, 1, 1);
		addWithHalfCarry(7, 7, 1, 0);
		addWithHalfCarry(7, 8, 1, 1);
		addWithHalfCarry(8, 7, 1, 1);
	}

	@Test
	public void testSubWithHalfBorrow() {
		subWithHalfCarry(0, 0, 0, 0);
		subWithHalfCarry(16, 0, 0, 0);
		subWithHalfCarry(16, 1, 0, 1);
		subWithHalfCarry(0, 1, 0, 1);
	}

	private void addWithCarry(final int op1, final int op2, final int carryIn,
			final int expectedResult, final int expectedCarry) {
		Adder adder = new Adder();
		int actualResult = adder.add(op1, op2, carryIn);
		int actualCarry = adder.isCarry() ? 1 : 0;
		assertEquals("Add result mismatch", expectedResult, actualResult);
		assertEquals("Add carry mismatch", expectedCarry, actualCarry);
	}

	private void subWithBorrow(final int op1, final int op2, final int carryIn, final int expectedResult,
			final int expectedCarry) {
		Adder adder = new Adder();
		int actualResult = adder.sub(op1, op2, carryIn);
		int actualCarry = adder.isBorrow() ? 1 : 0;
		assertEquals("Sub result mismatch", expectedResult, actualResult);
		assertEquals("Sub carry mismatch", expectedCarry, actualCarry);
	}

	private void addWithOverflow(final int op1, final int op2, final int carryIn, final int expectedOverflow) {
		Adder adder = new Adder();
		adder.add(op1, op2, carryIn);
		int actualOverflow = adder.isOverflow() ? 1 : 0;
		assertEquals("Add overflow mismatch", expectedOverflow, actualOverflow);
	}

	private void subWithOverflow(final int op1, final int op2, final int carryIn, final int expectedOverflow) {
		Adder adder = new Adder();
		adder.sub(op1, op2, carryIn);
		int actualOverflow = adder.isOverflow() ? 1 : 0;
		assertEquals("Sub overflow mismatch", expectedOverflow, actualOverflow);
	}

	private void addWithHalfCarry(final int op1, final int op2, final int carryIn,
			final int expectedHalfCarry) {
		Adder adder = new Adder();
		adder.add(op1, op2, carryIn);
		int actualHalfCarry = adder.isHalfCarry() ? 1 : 0;
		assertEquals("Add half-carry mismatch", expectedHalfCarry,
				actualHalfCarry);
	}

	private void subWithHalfCarry(final int op1, final int op2, final int carryIn,
			final int expectedHalfCarry) {
		Adder adder = new Adder();
		adder.sub(op1, op2, carryIn);
		int actualHalfCarry = adder.isHalfBorrow() ? 1 : 0;
		assertEquals("Add half-carry mismatch", expectedHalfCarry,
				actualHalfCarry);
	}

}
