package codec;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.BitSet;

import utils.Bitwise;
import utils.IO;
import utils.StringUtils;

public class Binary implements Codec {

	@Override
	public void encode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		BitSet bitSet = new BitSet();
		String binary;
		int number;
		int index = 0;

		while ((number = inputStream.read()) != -1) {
			binary = StringUtils.padLeft(Integer.toBinaryString(number), 8);
			index = Bitwise.updateBitSet(bitSet, binary, index);
		}

		IO.write(bitSet, outputStream);
		IO.closeStream(inputStream, outputStream);
	}

	@Override
	public void decode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		BitSet bitSet = IO.read(inputStream);
		StringBuilder binary;
		int index = 0;

		while (index < bitSet.length()) {
			binary = new StringBuilder();

			for (int i = 0; i < 8; i++)
				binary.append(bitSet.get(index++) ? 1 : 0);

			outputStream.write(Integer.parseInt(binary.toString(), 2));
		}

		IO.closeStream(inputStream, outputStream);
	}

}
