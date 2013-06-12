package gorskima.z80emu;

public class Adder {

	private final int size;
	private final int mask;
	private boolean[] carries;

	private Adder(final int size) {
		this.size = size;
		this.mask = 0xFFFFFFFF >>> (31 - size);
		this.carries = new boolean[size];
	}

	public static Adder newAdder8() {
		return new Adder(8);
	}

	public static Adder newAdder16() {
		return new Adder(16);
	}

	public int add(final int op1, final int op2, final int carryIn) {
		int result = 0;
		int carry = carryIn;

		for (int shift = 0; shift < size; shift++) {
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
		return ~value & mask;
	}

	private int invertBit(final int value) {
		return ~value & 0x01;
	}

	public boolean isCarry() {
		return carries[size - 1];
	}

	public boolean isBorrow() {
		return !isCarry();
	}

	public boolean isHalfCarry() {
		return carries[size - 5];
	}

	public boolean isHalfBorrow() {
		return !isHalfCarry();
	}

	public boolean isOverflow() {
		return carries[size - 1] ^ carries[size - 2];
	}

}
