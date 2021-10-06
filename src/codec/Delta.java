package codec;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.BitSet;

import utils.Bitwise;
import utils.IO;
import utils.StringUtils;

public class Delta implements Codec {

	@Override
	public void encode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		BitSet bitSet = new BitSet();
		String binary;
		int currentNumber;
		int lastNumber = 0;
		int difference;
		int index = 0;

		while ((currentNumber = inputStream.read()) != -1) {
			difference = currentNumber - lastNumber;
			binary = getCodeWord(difference);
			index = Bitwise.updateBitSet(bitSet, binary, index);
			lastNumber = currentNumber;
		}

		IO.write(bitSet, outputStream);
		IO.closeStream(inputStream, outputStream);
	}

	@Override
	public void decode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		BitSet bitSet = IO.read(inputStream);
		StringBuilder binary;
		int index = 0;
		int number = 0;
		int multiplier;

		while (index < bitSet.length()) {

			if (bitSet.get(index++)) {
				multiplier = !bitSet.get(index++) ? 1 : -1;
				binary = new StringBuilder();

				for (int j = 0; j < 8; j++)
					binary.append(bitSet.get(index++) ? 1 : 0);

				number += Integer.parseInt(binary.toString(), 2) * multiplier;
			}

			outputStream.write(number);
		}

		IO.closeStream(inputStream, outputStream);
	}

	public String getCodeWord(int difference) {
		StringBuilder binary = new StringBuilder();
		if (difference == 0) {
			binary.append("0");
		} else {
			binary.append(difference > 0 ? "10" : "11");
			binary.append(StringUtils.padLeft(Integer.toBinaryString(Math.abs(difference)), 8));
		}
		return binary.toString();
	}

}
