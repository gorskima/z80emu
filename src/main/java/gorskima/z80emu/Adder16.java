package gorskima.z80emu;

public class Adder16 {

	private final static int SIZE = 16;
	private boolean[] carries = new boolean[SIZE];

	public int add(final int op1, final int op2, final int carryIn) {
		int result = 0;
		int carry = carryIn;

		for (int shift = 0; shift < SIZE; shift++) {
			int a = (op1 >> shift) & 1;
			int b = (op2 >> shift) & 1;
			int r = a ^ b ^ carry;
			result = r == 1 ? result | 1 << shift : result & ~(1 << shift);
			carry = (a & b) | (carry & (a ^ b));
			carries[shift] = carry == 1;
		}
		return result;
	}

	public boolean isCarry() {
		return carries[15];
	}

	public boolean isHalfCarry() {
		return carries[11];
	}

	public boolean isOverflow() {
		return carries[15] ^ carries[14];
	}

}
