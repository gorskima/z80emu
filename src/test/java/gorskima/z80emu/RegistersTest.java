package gorskima.z80emu;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class RegistersTest {

	private Registers registers;
	
	@Before
	public void setup() {
		registers = new Registers();
	}
	
	@Test
	public void testSingleRegisters() {
		registers.setRegister(Register.A, 123);
		assertEquals(123,registers.getRegister(Register.A));
	}
	
	@Test
	public void testDoubleRegisters() {
		registers.setRegister(Register.BC, 10000);
		assertEquals(10000,registers.getRegister(Register.BC));
		
		registers.setRegister(Register.B, 0x12);
		registers.setRegister(Register.C, 0x73);
		assertEquals(0x1273,registers.getRegister(Register.BC));
	}
	
	@Test
	public void testIncPC() {
		assertEquals(0, registers.getRegister(Register.PC));
		
		registers.incPC();
		assertEquals(1, registers.getRegister(Register.PC));
		
		registers.setRegister(Register.PC, 30000);
		registers.incPC();
		registers.incPC();
		registers.incPC();
		assertEquals(30003, registers.getRegister(Register.PC));
		
		registers.setRegister(Register.PC, 65535);
		registers.incPC();
		assertEquals(0, registers.getRegister(Register.PC));
	}
	
	@Test
	public void testFlag() {
		registers.setFlag(Flag.C, true);
		assertEquals(true, registers.testFlag(Flag.C));
		
		registers.setFlag(Flag.C, false);
		assertEquals(false, registers.testFlag(Flag.C));
	}

}
