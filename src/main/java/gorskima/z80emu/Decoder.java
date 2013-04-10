package gorskima.z80emu;

import java.util.HashMap;
import java.util.Map;

public class Decoder {

	public enum RegisterType {
		r, dd, ss, qq, pp, rr
	};

	private Map<RegisterType, Map<Integer, Register>> map;
	private Map<Integer, Flag> flagMap;

	public Decoder() {
		map = new HashMap<Decoder.RegisterType, Map<Integer, Register>>();

		Map<Integer, Register> r = new HashMap<Integer, Register>();
		r.put(0, Register.B);
		r.put(1, Register.C);
		r.put(2, Register.D);
		r.put(3, Register.E);
		r.put(4, Register.H);
		r.put(5, Register.L);
		r.put(7, Register.A);
		map.put(RegisterType.r, r);

		Map<Integer, Register> dd = new HashMap<Integer, Register>();
		dd.put(0, Register.BC);
		dd.put(1, Register.DE);
		dd.put(2, Register.HL);
		dd.put(3, Register.SP);
		map.put(RegisterType.dd, dd);

		map.put(RegisterType.ss, dd);

		Map<Integer, Register> qq = new HashMap<Integer, Register>();
		qq.put(0, Register.BC);
		qq.put(1, Register.DE);
		qq.put(2, Register.HL);
		qq.put(3, Register.AF);
		map.put(RegisterType.qq, qq);

		Map<Integer, Register> pp = new HashMap<Integer, Register>();
		pp.put(0, Register.BC);
		pp.put(1, Register.DE);
		pp.put(2, Register.IX);
		pp.put(3, Register.SP);
		map.put(RegisterType.pp, pp);

		Map<Integer, Register> rr = new HashMap<Integer, Register>();
		rr.put(0, Register.BC);
		rr.put(1, Register.DE);
		rr.put(2, Register.IY);
		rr.put(3, Register.SP);
		map.put(RegisterType.rr, rr);

		flagMap = new HashMap<Integer, Flag>();
		flagMap.put(0, Flag.Z);
		flagMap.put(1, Flag.C);
		flagMap.put(2, Flag.PV);
		flagMap.put(3, Flag.S);
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
		return map.get(type).get(code);
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

}
