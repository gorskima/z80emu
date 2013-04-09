package gorskima.z80emu;

import static gorskima.z80emu.Decoder.RegisterType.dd;
import static gorskima.z80emu.Decoder.RegisterType.pp;
import static gorskima.z80emu.Decoder.RegisterType.qq;
import static gorskima.z80emu.Decoder.RegisterType.r;
import static gorskima.z80emu.Decoder.RegisterType.rr;
import static gorskima.z80emu.Register.A;
import static gorskima.z80emu.Register.AF;
import static gorskima.z80emu.Register.B;
import static gorskima.z80emu.Register.BC;
import static gorskima.z80emu.Register.C;
import static gorskima.z80emu.Register.D;
import static gorskima.z80emu.Register.DE;
import static gorskima.z80emu.Register.E;
import static gorskima.z80emu.Register.H;
import static gorskima.z80emu.Register.HL;
import static gorskima.z80emu.Register.IX;
import static gorskima.z80emu.Register.IY;
import static gorskima.z80emu.Register.L;
import static gorskima.z80emu.Register.SP;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class DecoderTest {

	private Decoder decoder = new Decoder();

	@Test
	public void testDecodingR() {
		assertThat(decoder.decodeReg(r, 0x00), is(B));
		assertThat(decoder.decodeReg(r, 0x08), is(C));
		assertThat(decoder.decodeReg(r, 0x10), is(D));
		assertThat(decoder.decodeReg(r, 0x18), is(E));
		assertThat(decoder.decodeReg(r, 0x20), is(H));
		assertThat(decoder.decodeReg(r, 0x28), is(L));
		assertThat(decoder.decodeReg(r, 0x38), is(A));
		// TODO add r'
	}
	
	@Test
	public void testDecodingDD() {
		assertThat(decoder.decodeReg(dd, 0x00), is(BC));
		assertThat(decoder.decodeReg(dd, 0x10), is(DE));
		assertThat(decoder.decodeReg(dd, 0x20), is(HL));
		assertThat(decoder.decodeReg(dd, 0x30), is(SP));
	}

	@Test
	public void testDecodingSS() {
		assertThat(decoder.decodeReg(dd, 0x00), is(BC));
		assertThat(decoder.decodeReg(dd, 0x10), is(DE));
		assertThat(decoder.decodeReg(dd, 0x20), is(HL));
		assertThat(decoder.decodeReg(dd, 0x30), is(SP));
	}

	@Test
	public void testDecodingQQ() {
		assertThat(decoder.decodeReg(qq, 0x00), is(BC));
		assertThat(decoder.decodeReg(qq, 0x10), is(DE));
		assertThat(decoder.decodeReg(qq, 0x20), is(HL));
		assertThat(decoder.decodeReg(qq, 0x30), is(AF));
	}

	@Test
	public void testDecodingPP() {
		assertThat(decoder.decodeReg(pp, 0x00), is(BC));
		assertThat(decoder.decodeReg(pp, 0x10), is(DE));
		assertThat(decoder.decodeReg(pp, 0x20), is(IX));
		assertThat(decoder.decodeReg(pp, 0x30), is(SP));
	}

	@Test
	public void testDecodingRR() {
		assertThat(decoder.decodeReg(rr, 0x00), is(BC));
		assertThat(decoder.decodeReg(rr, 0x10), is(DE));
		assertThat(decoder.decodeReg(rr, 0x20), is(IY));
		assertThat(decoder.decodeReg(rr, 0x30), is(SP));
	}

}
