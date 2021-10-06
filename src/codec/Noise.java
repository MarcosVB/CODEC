package codec;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.BitSet;
import java.util.concurrent.RejectedExecutionException;

import app.Log;
import utils.Bitwise;
import utils.IO;
import utils.StringUtils;

public class Noise implements Codec {

	@Override
	public void encode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		BitSet inputBitSet = IO.read(inputStream);
		BitSet outputBitSet = new BitSet();

		int inputIndex = 0;
		int outputIndex = 0;
		int number = Bitwise.readBinary(inputBitSet, inputIndex, 16);
		int header = generateCRC8(number);

		inputIndex += 16;
		outputIndex = Bitwise.updateBitSet(outputBitSet, header, outputIndex, 23);

		int hamming;

		while (inputIndex < inputBitSet.length()) {
			number = Bitwise.readBinary(inputBitSet, inputIndex, 4);
			hamming = hamming74(number);
			inputIndex += 4;
			outputIndex = Bitwise.updateBitSet(outputBitSet, hamming, outputIndex, 6);
		}

		IO.write(outputBitSet, outputStream);
		IO.closeStream(inputStream, outputStream);
	}

	@Override
	public void decode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		BitSet inputBitSet = IO.read(inputStream);
		BitSet outputBitSet = new BitSet();

		int inputIndex = 0;
		int outputIndex = 0;
		int headerWithCRC8 = Bitwise.readBinary(inputBitSet, inputIndex, 24);
		int headerWithoutCRC8 = headerWithCRC8 >> 8;

		inputIndex += 24;

		if (headerWithCRC8 != generateCRC8(headerWithoutCRC8)) {
			Log.log("Error reading header data (CRC-8)");
			Log.log("Readed    : " + StringUtils.padLeft(Integer.toBinaryString(headerWithCRC8), 24));
			Log.log("Calculated: " + StringUtils.padLeft(Integer.toBinaryString(generateCRC8(headerWithoutCRC8)), 24));
			throw new RejectedExecutionException("Header is modified.");
		}

		outputIndex = Bitwise.updateBitSet(outputBitSet, headerWithoutCRC8, outputIndex, 15);

		int number;
		int hamming;

		while (inputIndex < inputBitSet.length()) {
			hamming = Bitwise.readBinary(inputBitSet, inputIndex, 7);
			number = hamming >> 3;
			inputIndex += 7;

			if (hamming != hamming74(number)) {
				Log.log("Error reading Hamming codeword");
				Log.log("Readed    : " + StringUtils.padLeft(Integer.toBinaryString(hamming), 7));
				Log.log("Calculated: " + StringUtils.padLeft(Integer.toBinaryString(hamming74(number)), 7));
				hamming = fixHamming74(hamming);
				Log.log("Fixed     : " + StringUtils.padLeft(Integer.toBinaryString(hamming), 7));
				number = 0 | (hamming >> 3);
			}

			outputIndex = Bitwise.updateBitSet(outputBitSet, number, outputIndex, 3);
		}

		IO.write(outputBitSet, outputStream);
		IO.closeStream(inputStream, outputStream);
	}

	public int generateCRC8(int bits) {
		int shifted = bits << 16;
		int shiftedIndex;

		while ((shiftedIndex = Bitwise.getMostSignificantBit(shifted)) > 7)
			shifted ^= 263 << (shiftedIndex - 8);

		return (bits << 8) | shifted;
	}

	public int hamming74(int bits) {
		int[] bitValues = new int[4];

		for (int i = 0; i < 4; i++)
			bitValues[i] = Bitwise.getBit(bits, 3 - i) ? 1 : 0;

		int hamming = bits;

		hamming = (hamming << 1);

		if ((bitValues[0] + bitValues[1] + bitValues[2]) % 2 != 0)
			hamming = Bitwise.setBit(hamming, 0);

		hamming = (hamming << 1);

		if ((bitValues[1] + bitValues[2] + bitValues[3]) % 2 != 0)
			hamming = Bitwise.setBit(hamming, 0);

		hamming = (hamming << 1);

		if ((bitValues[0] + bitValues[2] + bitValues[3]) % 2 != 0)
			hamming = Bitwise.setBit(hamming, 0);

		return hamming;
	}

	public int fixHamming74(int hamming) {
		int[] bitValues = new int[7];

		for (int i = 0; i < 7; i++)
			bitValues[i] = Bitwise.getBit(hamming, 6 - i) ? 1 : 0;

		boolean[] bitFlags = new boolean[3];

		if ((bitValues[0] + bitValues[1] + bitValues[2]) % 2 != bitValues[4])
			bitFlags[0] = true;

		if ((bitValues[1] + bitValues[2] + bitValues[3]) % 2 != bitValues[5])
			bitFlags[1] = true;

		if ((bitValues[0] + bitValues[2] + bitValues[3]) % 2 != bitValues[6])
			bitFlags[2] = true;

		if (bitFlags[0] && bitFlags[1] && bitFlags[2])
			return Bitwise.flipBit(hamming, 4);

		if (bitFlags[0] && bitFlags[1])
			return Bitwise.flipBit(hamming, 5);

		if (bitFlags[0] && bitFlags[2])
			return Bitwise.flipBit(hamming, 6);

		if (bitFlags[1] && bitFlags[2])
			return Bitwise.flipBit(hamming, 3);

		return hamming;
	}

}
