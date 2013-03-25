package gorskima.z80emu;

import java.io.IOException;
import java.net.URL;

import com.google.common.io.Resources;

public class CpuRunner {

	public static Z80 run(final String programPath) {
		Z80 cpu = initCpu(programPath);
		while (!cpu.isHalt()) {
			cpu.step();
		}
		return cpu;
	}

	private static Z80 initCpu(final String programPath) {
		URL resource = Resources.getResource(programPath);
		Memory memory = new Memory();
		try {
			writeMemory(memory, Resources.toByteArray(resource));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new Z80(memory);
	}

	private static void writeMemory(final Memory memory, final byte[] code) {
		for (int addr = 0; addr < code.length; addr++) {
			memory.writeWord8(addr, 0xFF & code[addr]);
		}
	}

}
