package gorskima.z80emu;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ALUTest {

	private ALU alu;
	private Registers reg;
	
	@Before
	public void setup() {
		reg = new Registers();
		alu = new ALU(reg);
	}
	
	@Test
	public void testAdd() {
		reg.setRegister(Register.A, 0x1FF);
		alu.add(0);
		assertEquals(0xFF, reg.getRegister(Register.A));
	}
	
	@Test
	public void testDecA() {
		reg.setRegister(Register.A, 7);
		alu.dec(Register.A);
		assertEquals(6, reg.getRegister(Register.A));
	}
	
	@Test
	public void testDec() {
		reg.setRegister(Register.B, 9);
		alu.dec(Register.B);
		assertEquals(8, reg.getRegister(Register.B));
	}

}
