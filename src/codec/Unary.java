package codec;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.BitSet;

import utils.IO;

public class Unary implements Codec {

	// @Override
	public void encode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		BitSet bitSet = new BitSet();
		int index = 0;
		int number;

		while ((number = inputStream.read()) != -1) {
			index += number;
			bitSet.set(index++);
		}

		IO.write(bitSet, outputStream);
		IO.closeStream(inputStream, outputStream);
	}

	@Override
	public void decode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		BitSet bitSet = IO.read(inputStream);
		int index = 0;
		int count = 0;

		while (index < bitSet.length()) {

			while (!bitSet.get(index++))
				count++;

			outputStream.write(count);
			count = 0;
		}

		IO.closeStream(inputStream, outputStream);
	}

}
