package gorskima.z80emu;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class Adder16Test {

	@Test
	public void testAddWithCarry() {
		addWithCarry(0, 0, 0, 0, 0);
		addWithCarry(1, 0, 0, 1, 0);
		addWithCarry(1, 0, 1, 2, 0);
		addWithCarry(1, 1, 1, 3, 0);

		addWithCarry(65535, 0, 0, 65535, 0);
		addWithCarry(65535, 1, 0, 0, 1);
		addWithCarry(65535, 0, 1, 0, 1);
		addWithCarry(65535, 1, 1, 1, 1);

		addWithCarry(65535, 65535, 1, 65535, 1);
	}

	@Test
	public void testSubWithBorrow() {
		subWithBorrow(0, 0, 0, 0, 0);
		subWithBorrow(0, 1, 0, 65535, 1);
		subWithBorrow(0, 1, 1, 65534, 1);
	}

	@Test
	public void testAddWithHalfCarry() {
		addWithHalfCarry(4095, 0, 0, 4095, 0);
		addWithHalfCarry(4095, 1, 0, 4096, 1);
		addWithHalfCarry(4095, 0, 1, 4096, 1);
		addWithHalfCarry(4095, 1, 1, 4097, 1);
	}
	
	@Test
	public void testSubWithHalfCarry() {
		subWithHalfBorrow(0, 0, 0, 0, 0);
		subWithHalfBorrow(4096, 0, 0, 4096, 0);
		subWithHalfBorrow(4096, 1, 0, 4095, 1);
	}

	@Test
	public void testAddWithOverflow() {
		addWithOverflow(32677, 0, 0, 32677, 0);
		addWithOverflow(32767, 1, 0, 32768, 1);
		addWithOverflow(30000, 20000, 0, 50000, 1);
		addWithOverflow(65535, 2, 0, 1, 0);
	}

	@Test
	public void testSubWithOverflow() {
		subWithOverflow(0, 0, 0, 0, 0);
		subWithOverflow(32768, 1, 0, 32767, 1);
	}

	private void addWithCarry(final int op1, final int op2, final int carry, final int expectedResult,
			final int expectedCarry) {

		Adder adder = Adder.newAdder16();
		int result = adder.add(op1, op2, carry);
		assertThat(result, is(expectedResult));
		assertThat(adder.isCarry(), is(expectedCarry == 1));
	}

	private void subWithBorrow(final int op1, final int op2, final int carry, final int expectedResult,
			final int expectedBorrow) {

		Adder adder = Adder.newAdder16();
		int result = adder.sub(op1, op2, carry);
		assertThat(result, is(expectedResult));
		assertThat(adder.isBorrow(), is(expectedBorrow == 1));
	}

	private void addWithHalfCarry(final int op1, final int op2, final int carry, final int expectedResult,
			final int expectedCarry) {

		Adder adder = Adder.newAdder16();
		int result = adder.add(op1, op2, carry);
		assertThat(result, is(expectedResult));
		assertThat(adder.isHalfCarry(), is(expectedCarry == 1));
	}
	
	private void subWithHalfBorrow(final int op1, final int op2, final int carry, final int expectedResult,
			final int expectedCarry) {
		
		Adder adder = Adder.newAdder16();
		int result = adder.sub(op1, op2, carry);
		assertThat(result, is(expectedResult));
		assertThat(adder.isHalfBorrow(), is(expectedCarry == 1));
	}

	private void addWithOverflow(final int op1, final int op2, final int carry, final int expectedResult,
			final int expectedOverflow) {

		Adder adder = Adder.newAdder16();
		int result = adder.add(op1, op2, carry);
		assertThat(result, is(expectedResult));
		assertThat(adder.isOverflow(), is(expectedOverflow == 1));
	}

	private void subWithOverflow(final int op1, final int op2, final int carry, final int expectedResult,
			final int expectedOverflow) {

		Adder adder = Adder.newAdder16();
		int result = adder.sub(op1, op2, carry);
		assertThat(result, is(expectedResult));
		assertThat(adder.isOverflow(), is(expectedOverflow == 1));
	}

}
