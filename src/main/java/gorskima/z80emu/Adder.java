package gorskima.z80emu;

import java.util.ArrayList;
import java.util.List;

//TODO refactor
public class Adder {

	private List<Boolean> carries = new ArrayList<Boolean>();

	public int add(final int op1, final int op2, final int carryIn) {
		int result = 0;
		int carry = carryIn;

		for (int i = 0; i < 8; i++) {
			int a = (op1 >> i) & 0x01;
			int b = (op2 >> i) & 0x01;
			int sum = a + b + carry;

			result >>= 1;
			if ((sum & 0x01) == 1) {
				result |= 0x80;
			} else {
				result &= 0x7F;
			}
			carry = ((sum >> 1) & 0x01);
			carries.add(carry == 1);
		}

		return result;
	}

	public int sub(final int op1, int op2, int carryIn) {
		op2 = negateByte(op2);
		carryIn = negateBit(carryIn);
		return add(op1, op2, carryIn);
	}
	
	public boolean isCarry() {
		return carries.get(7);
	}
	
	public boolean isBorrow() {
		return ! carries.get(7);
	}
	
	public boolean isHalfCarry() {
		return carries.get(3);
	}
	
	public boolean isHalfBorrow() {
		return ! carries.get(3);
	}
	
	public boolean isOverflow() {
		return carries.get(7) ^ carries.get(6);
	}

	private int negateByte(final int a) {
		return ~a & 0xFF;
	}

	private int negateBit(final int a) {
		return ~a & 0x01;
	}

}
