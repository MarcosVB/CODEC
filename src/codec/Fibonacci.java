package codec;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.BitSet;

import utils.Bitwise;
import utils.IO;

public class Fibonacci implements Codec {
	private static final int[] FIBONACCI_NUMBERS = { 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233 };

	@Override
	public void encode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		BitSet bitSet = new BitSet();
		String codeWord;
		int number;
		int index = 0;

		while ((number = inputStream.read()) != -1) {
			codeWord = getFibonacciCodeWord(number + 1);
			index = Bitwise.updateBitSet(bitSet, codeWord, index);
			bitSet.set(index++);
		}

		IO.write(bitSet, outputStream);
		IO.closeStream(inputStream, outputStream);
	}

	@Override
	public void decode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		BitSet bitSet = IO.read(inputStream);
		int index = 0;

		while (index < bitSet.length()) {
			int number = 0;
			int fibonacciIndex = 0;
			int count = 0;

			while (count < 2) {
				if (bitSet.get(index++)) {
					count++;
					if (count < 2)
						number += FIBONACCI_NUMBERS[fibonacciIndex];
				} else {
					if (count > 0)
						count--;
				}
				fibonacciIndex++;
			}

			outputStream.write(number - 1);
		}

		IO.closeStream(inputStream, outputStream);
	}

	public String getFibonacciCodeWord(int number) {
		StringBuilder codeWord = new StringBuilder();

		for (int i = 11; i > -1; i--) {
			if (FIBONACCI_NUMBERS[i] <= number) {
				number -= FIBONACCI_NUMBERS[i];
				codeWord.insert(0, "1");
			} else {
				if (codeWord.length() > 0)
					codeWord.insert(0, "0");
			}
		}

		return codeWord.toString();
	}

}
