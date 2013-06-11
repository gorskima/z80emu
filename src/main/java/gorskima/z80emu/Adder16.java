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

	public int sub(final int op1, final int op2, final int carryIn) {
		int invertedOp2 = invertByte(op2);
		int invertedCarryIn = invertBit(carryIn);
		return add(op1, invertedOp2, invertedCarryIn);
	}

	private int invertByte(final int value) {
		return ~value & 0xFFFF;
	}

	private int invertBit(final int value) {
		return ~value & 0x01;
	}

	public boolean isCarry() {
		return carries[15];
	}

	public boolean isHalfCarry() {
		return carries[11];
	}

	public boolean isBorrow() {
		return !isCarry();
	}

	public boolean isHalfBorrow() {
		return !isHalfCarry();
	}

	public boolean isOverflow() {
		return carries[15] ^ carries[14];
	}

}
