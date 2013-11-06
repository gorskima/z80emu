package gorskima.z80emu;

public interface IOPort {

	int read();
	void write(int n);
	
}
