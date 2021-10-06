package codec;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.BitSet;

import utils.Bitwise;
import utils.IO;
import utils.StringUtils;

public class Golomb implements Codec {
	private int divisor;
	private int bitSize;

	public Golomb() {
		this(2);
	}

	public Golomb(int divisor) {
		this.divisor = divisor;
		this.bitSize = (int) (Math.log(divisor) / Math.log(2));
	}

	@Override
	public void encode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		BitSet bitSet = new BitSet();
		String restBinary;
		int number;
		int index = 0;

		while ((number = inputStream.read()) != -1) {
			index += number / divisor;
			bitSet.set(index++);
			restBinary = StringUtils.padLeft(Integer.toBinaryString(number % divisor), bitSize);
			index = Bitwise.updateBitSet(bitSet, restBinary, index);
		}

		IO.write(bitSet, outputStream);
		IO.closeStream(inputStream, outputStream);
	}

	@Override
	public void decode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		BitSet bitSet = IO.read(inputStream);
		int index = 0;
		int binary;
		int count = 0;

		while (index < bitSet.length()) {

			while (!bitSet.get(index++))
				count++;

			binary = Bitwise.readBinary(bitSet, index, bitSize);
			outputStream.write(count * divisor + binary);
			index += bitSize;
			count = 0;
		}

		IO.closeStream(inputStream, outputStream);
	}

}
