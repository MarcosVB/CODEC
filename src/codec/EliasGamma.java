package codec;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.BitSet;

import utils.Bitwise;
import utils.IO;
import utils.StringUtils;

public class EliasGamma implements Codec {

	@Override
	public void encode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		BitSet bitSet = new BitSet();
		String restBinary;
		int number;
		int power;
		int index = 0;

		while ((number = inputStream.read()) != -1) {
			number++;
			power = getBiggestPower2Inside(number);
			index += power;
			bitSet.set(index++);
			restBinary = StringUtils.padLeft(Integer.toBinaryString(number - (int) Math.pow(2, power)), power);
			index = Bitwise.updateBitSet(bitSet, restBinary, index);
		}

		IO.write(bitSet, outputStream);
		IO.closeStream(inputStream, outputStream);
	}

	@Override
	public void decode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		BitSet bitSet = IO.read(inputStream);
		int index = 0;
		int bitSize = 0;

		while (index < bitSet.length()) {

			while (!bitSet.get(index++))
				bitSize++;

			outputStream.write((int) (Math.pow(2, bitSize) + Bitwise.readBinary(bitSet, index, bitSize) - 1));
			index += bitSize;
			bitSize = 0;
		}

		IO.closeStream(inputStream, outputStream);
	}

	public int getBiggestPower2Inside(int number) {
		int power = 0;
		while (Math.pow(2, power) <= number)
			power++;
		return power - 1;
	}

}
