package gorskima.z80emu;

import com.google.common.base.Preconditions;


public class Memory {

	private static final int DEFAULT_SIZE = 1 << 16;
	private int[] mem = new int[DEFAULT_SIZE];
	
	public int readWord8(final int addr) {
		return mem[addr];
	}
	
	public void writeWord8(final int addr, final int word) {
		Preconditions.checkArgument((word & 0xFFFFFF00) == 0, "Value may use only 1 least significant byte");
		mem[addr] = word;
	}
	
	public int readWord16(final int addr) {
		int l = mem[addr];
		int h = mem[addr + 1];
		return ((h << 8) + l);
	}
	
	public void writeWord16(final int addr, final int word) {
		Preconditions.checkArgument((word & 0xFFFF0000) == 0, "Value may use only 2 least significant bytes");
		mem[addr] = word & 0xFF;
		mem[addr + 1] = word >> 8;
	}
	
}
