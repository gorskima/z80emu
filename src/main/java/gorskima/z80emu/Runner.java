package gorskima.z80emu;

import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;

public class Runner {

	public static void main(final String[] args) {
		Z80 z80 = initCpu("boot.bin");
		while (true)
			z80.step();
	}

	private static Z80 initCpu(final String programPath) {
		URL resource = Resources.getResource(programPath);
		Memory memory = new Memory(1 << 14);
		try {
			writeMemory(memory, Resources.toByteArray(resource));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new Z80(new Registers(), memory);
	}

	private static void writeMemory(final Memory memory, final byte[] code) {
		for (int addr = 0; addr < code.length; addr++) {
			memory.writeWord8(addr, 0xFF & code[addr]);
		}
	}

}
