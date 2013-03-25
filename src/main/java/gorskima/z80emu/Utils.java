package gorskima.z80emu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

	public static int byte2int(final byte b) {
		return (b & 0x000000FF);
	}
	
	public static void loadMemory(final Memory mem, final String filename) {
		InputStream in = null;
		try {
			 in = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		byte[] b = new byte[65536];
		int read = 0;
		try {
			// read = in.read(b);
			read = in.read(b, 0, b.length);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		for (int addr = 0; addr < read; addr++) {
			int word = byte2int(b[addr]);
			mem.writeWord8(addr, word);
		}
		
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(final String[] args) {
		int a = 65535;
		System.out.println(a);
		System.out.println(shuf(a));
	}
	
	private static int shuf(final int a) {
		return (a << 16) >> 16; 
	}
	
}
