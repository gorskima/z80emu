package gorskima.z80emu;

import static gorskima.z80emu.Decoder.RegisterType.dd;
import static gorskima.z80emu.Decoder.RegisterType.pp;
import static gorskima.z80emu.Decoder.RegisterType.qq;
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
	public void testDecodingUpperR() {
		assertThat(decoder.decodeUpperR(0x00), is(B));
		assertThat(decoder.decodeUpperR(0x08), is(C));
		assertThat(decoder.decodeUpperR(0x10), is(D));
		assertThat(decoder.decodeUpperR(0x18), is(E));
		assertThat(decoder.decodeUpperR(0x20), is(H));
		assertThat(decoder.decodeUpperR(0x28), is(L));
		assertThat(decoder.decodeUpperR(0x38), is(A));
	}
	
	@Test
	public void testDecodingLowerR() {
		assertThat(decoder.decodeLowerR(0x00), is(B));
		assertThat(decoder.decodeLowerR(0x01), is(C));
		assertThat(decoder.decodeLowerR(0x02), is(D));
		assertThat(decoder.decodeLowerR(0x03), is(E));
		assertThat(decoder.decodeLowerR(0x04), is(H));
		assertThat(decoder.decodeLowerR(0x05), is(L));
		assertThat(decoder.decodeLowerR(0x07), is(A));
	}
	
	@Test
	public void testDecodingDD() {
		assertThat(decoder.decode(dd, 0x00), is(BC));
		assertThat(decoder.decode(dd, 0x10), is(DE));
		assertThat(decoder.decode(dd, 0x20), is(HL));
		assertThat(decoder.decode(dd, 0x30), is(SP));
	}

	@Test
	public void testDecodingSS() {
		assertThat(decoder.decode(dd, 0x00), is(BC));
		assertThat(decoder.decode(dd, 0x10), is(DE));
		assertThat(decoder.decode(dd, 0x20), is(HL));
		assertThat(decoder.decode(dd, 0x30), is(SP));
	}

	@Test
	public void testDecodingQQ() {
		assertThat(decoder.decode(qq, 0x00), is(BC));
		assertThat(decoder.decode(qq, 0x10), is(DE));
		assertThat(decoder.decode(qq, 0x20), is(HL));
		assertThat(decoder.decode(qq, 0x30), is(AF));
	}

	@Test
	public void testDecodingPP() {
		assertThat(decoder.decode(pp, 0x00), is(BC));
		assertThat(decoder.decode(pp, 0x10), is(DE));
		assertThat(decoder.decode(pp, 0x20), is(IX));
		assertThat(decoder.decode(pp, 0x30), is(SP));
	}

	@Test
	public void testDecodingRR() {
		assertThat(decoder.decode(rr, 0x00), is(BC));
		assertThat(decoder.decode(rr, 0x10), is(DE));
		assertThat(decoder.decode(rr, 0x20), is(IY));
		assertThat(decoder.decode(rr, 0x30), is(SP));
	}

	@Test
	public void testDecodingCC() {
		assertCondition(decoder.decodeCondition(0x00), Flag.Z, false);
		assertCondition(decoder.decodeCondition(0x08), Flag.Z, true);
		assertCondition(decoder.decodeCondition(0x10), Flag.C, false);
		assertCondition(decoder.decodeCondition(0x18), Flag.C, true);
		assertCondition(decoder.decodeCondition(0x20), Flag.PV, false);
		assertCondition(decoder.decodeCondition(0x28), Flag.PV, true);
		assertCondition(decoder.decodeCondition(0x30), Flag.S, false);
		assertCondition(decoder.decodeCondition(0x38), Flag.S, true);
	}

	// TODO why not writing hamcrest matcher?
	private void assertCondition(final Condition decodeCondition, final Flag flag, final boolean expectedValue) {
		assertThat(decodeCondition.getFlag(), is(flag));
		assertThat(decodeCondition.getExpectedValue(), is(expectedValue));
	}

}
