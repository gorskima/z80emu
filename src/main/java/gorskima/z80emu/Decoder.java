package gorskima.z80emu;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class Decoder {

	public enum RegisterType {
		r, dd, ss, qq, pp, rr
	};

	private final Map<RegisterType, Map<Integer, Register>> registerMap;
	private final Map<Integer, Flag> flagMap;

	public Decoder() {
		Map<Integer, Register> r = ImmutableMap.<Integer, Register>builder()
			.put(0, Register.B).put(1, Register.C).put(2, Register.D).put(3, Register.E)
			.put(4, Register.H).put(5, Register.L).put(7, Register.A)
			.build();

		Map<Integer, Register> dd = ImmutableMap.<Integer, Register>builder()
			.put(0, Register.BC).put(1, Register.DE).put(2, Register.HL).put(3, Register.SP)
			.build();

		Map<Integer, Register> qq = ImmutableMap.<Integer, Register>builder()
			.put(0, Register.BC).put(1, Register.DE).put(2, Register.HL).put(3, Register.AF)
			.build();

		Map<Integer, Register> pp = ImmutableMap.<Integer, Register>builder()
			.put(0, Register.BC).put(1, Register.DE).put(2, Register.IX).put(3, Register.SP)
			.build();

		Map<Integer, Register> rr = ImmutableMap.<Integer, Register>builder()
			.put(0, Register.BC).put(1, Register.DE).put(2, Register.IY).put(3, Register.SP)
			.build();
		
		registerMap = ImmutableMap.<Decoder.RegisterType, Map<Integer, Register>> builder()
			.put(RegisterType.r, r).put(RegisterType.dd, dd).put(RegisterType.ss, dd)
			.put(RegisterType.qq, qq).put(RegisterType.pp, pp).put(RegisterType.rr, rr)
			.build();

		flagMap = ImmutableMap.<Integer, Flag> builder()
			.put(0, Flag.Z).put(1, Flag.C).put(2, Flag.PV).put(3, Flag.S)
			.build();
	}

	public Register decodeRegister(final RegisterType type, final int opCode) {
		if (type == RegisterType.r) {
			throw new IllegalArgumentException("Call decodeUpperR or decodeLowerR");
		}
		return decode(type, extractDoubleRegisterCode(opCode));
	}

	public Register decodeUpperR(final int opCode) {
		return decode(RegisterType.r, extractHigherRegisterCode(opCode));
	}

	public Register decodeLowerR(final int opCode) {
		return decode(RegisterType.r, opCode & 0x07);
	}

	private Register decode(final RegisterType type, final int code) {
		return registerMap.get(type).get(code);
	}

	private int extractHigherRegisterCode(final int opCode) {
		return (opCode >> 3) & 0x07;
	}

	private int extractDoubleRegisterCode(final int opCode) {
		return (opCode >> 4) & 0x03;
	}

	public Condition decodeCondition(final int opCode) {
		return new Condition(flagMap.get(extractFlagCode(opCode)), extractExpectedFlagValue(opCode));
	}

	private int extractFlagCode(final int opCode) {
		return (opCode >> 4) & 0x03;
	}

	private boolean extractExpectedFlagValue(final int opCode) {
		return (opCode & 0x08) > 0;
	}

	public int decodePage(final int opCode) {
		return opCode & 0x38;
	}

}
