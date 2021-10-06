package utils;

import java.util.BitSet;

public class Bitwise {

	private Bitwise() {
		throw new IllegalStateException("Utility class");
	}

	public static boolean getBit(int number, int index) {
		return ((number >> index) & 1) == 1;
	}

	public static int setBit(int number, int index) {
		return number | (1 << index);
	}

	public static int clearBit(int number, int index) {
		return number & ~(1 << index);
	}

	public static int flipBit(int number, int index) {
		return getBit(number, index) ? clearBit(number, index) : setBit(number, index);
	}

	public static int getMostSignificantBit(int bits) {
		int index = 31;

		while (!Bitwise.getBit(bits, index) && index > 0)
			index--;

		return index;
	}

	public static int updateBitSet(BitSet bitSet, int number, int bitSetIndex, int bitIndex) {
		for (int i = bitIndex; i > -1; i--) {
			if (getBit(number, i))
				bitSet.set(bitSetIndex);
			bitSetIndex++;
		}

		return bitSetIndex;
	}

	public static int updateBitSet(BitSet bitSet, String binary, int index) {
		for (int i = 0; i < binary.length(); i++) {
			if (binary.charAt(i) == '1')
				bitSet.set(index);
			index++;
		}

		return index;
	}

	public static int readBinary(BitSet bitSet, int index, int bitSize) {
		int number = 0;

		for (int i = 0; i < bitSize - 1; i++) {
			if (bitSet.get(index++))
				number = setBit(number, 0);
			number = number << 1;
		}

		if (bitSet.get(index))
			number = setBit(number, 0);

		return number;
	}
}
