package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class IO {

	private IO() {
		throw new IllegalStateException("Utility class");
	}

	public static InputStreamReader getReader(String filePath) throws FileNotFoundException {
		return new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8);
	}

	public static OutputStreamWriter getWriter(String filePath) throws IOException {
		return new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8);
	}

	public static BitSet read(InputStreamReader inputStream) throws IOException {
		BitSet bitSet = new BitSet();
		int index = 0;
		int bit;

		while ((bit = inputStream.read()) != -1) {
			for (int i = 0; i < 8; i++)
				bitSet.set(index++, Bitwise.getBit(bit, i));
		}

		return bitSet;
	}

	public static void write(BitSet bitSet, OutputStreamWriter outputStream) throws IOException {
		byte[] bytes = bitSet.toByteArray();

		for (int i = 0; i < bytes.length; i++)
			outputStream.write(Byte.toUnsignedInt(bytes[i]));
	}

	public static void closeStream(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException {
		inputStream.close();
		outputStream.close();
	}
}
