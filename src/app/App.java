package app;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import codec.Binary;
import codec.Delta;
import codec.EliasGamma;
import codec.Fibonacci;
import codec.Golomb;
import codec.Noise;
import codec.Unary;
import utils.IO;

public class App {
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		String[] commandSteps;
		String command;

		Log.welcome();
		Log.path();

		while (!(command = scanner.nextLine()).equals("q")) {

			commandSteps = command.split(" ");

			try {
				router(commandSteps);
			} catch (Exception e) {
				Log.log(e.getMessage());
			}

			Log.path();
		}

		Log.log("End of execution.\n");
		scanner.close();
	}

	public static void router(String[] commandSteps) throws IOException {
		String response;

		switch (commandSteps[0]) {
			case "encode":
				Log.log("Encoding...");
				response = encode(commandSteps[1], commandSteps[2]);
				Log.log("Encoded: " + response);
				break;

			case "decode":
				Log.log("Decoding...");
				response = decode(commandSteps[1]);
				Log.log("Decoded: " + response);
				break;

			case "noise":
				Log.log("Applying CRC8...");
				response = generateCRC8(commandSteps[1]);
				Log.log("CRC8 applied: " + response);
				break;

			case "unnoise":
				Log.log("Removing CRC8...");
				response = removeCRC8(commandSteps[1]);
				Log.log("CRC8 removed: " + response);
				break;

			case "help":
				Log.help();
				break;

			default:
				Log.log("Invalid command: " + commandSteps[0]);
		}
	}

	public static String encode(String type, String filePath) throws IOException {
		String filePathEncoded = filePath + ".cod";

		InputStreamReader inputStream = IO.getReader(filePath);
		OutputStreamWriter outputStream = IO.getWriter(filePathEncoded);

		switch (type) {
			case "unary":
				outputStream.write(0);
				outputStream.write(0);
				new Unary().encode(inputStream, outputStream);
				break;

			case "golomb":
				Log.log("Inform the divisor <k> for Golomb encoding: ");
				int k = new Scanner(System.in).nextInt();
				outputStream.write(1);
				outputStream.write(k);
				new Golomb(k).encode(inputStream, outputStream);
				break;

			case "eliasgamma":
				outputStream.write(2);
				outputStream.write(0);
				new EliasGamma().encode(inputStream, outputStream);
				break;

			case "fibonacci":
				outputStream.write(3);
				outputStream.write(0);
				new Fibonacci().encode(inputStream, outputStream);
				break;

			case "delta":
				outputStream.write(4);
				outputStream.write(0);
				new Delta().encode(inputStream, outputStream);
				break;

			case "binary":
				outputStream.write(5);
				outputStream.write(0);
				new Binary().encode(inputStream, outputStream);
				break;

			default:
				return "Invalid type: " + type;
		}

		return filePathEncoded;
	}

	public static String decode(String filePath) throws IOException {
		String filePathWithoutType = filePath.replace(".cod", "");
		String[] pathSplited = filePathWithoutType.split("\\.");
		String filePathDecoded = pathSplited[0] + "_decoded";

		for (int i = 1; i < pathSplited.length; i++)
			filePathDecoded += "." + pathSplited[i];

		InputStreamReader inputStream = IO.getReader(filePath);
		OutputStreamWriter outputStream = IO.getWriter(filePathDecoded);

		int type = inputStream.read();
		int parameter = inputStream.read();

		switch (type) {
			case 0:
				new Unary().decode(inputStream, outputStream);
				break;

			case 1:
				new Golomb(parameter).decode(inputStream, outputStream);
				break;

			case 2:
				new EliasGamma().decode(inputStream, outputStream);
				break;

			case 3:
				new Fibonacci().decode(inputStream, outputStream);
				break;

			case 4:
				new Delta().decode(inputStream, outputStream);
				break;

			case 5:
				new Binary().decode(inputStream, outputStream);
				break;

			default:
				return "Invalid type: " + type;
		}

		return filePathDecoded;
	}

	public static String generateCRC8(String filePath) throws IOException {
		String filePathEncoded = filePath + ".ecc";

		InputStreamReader inputStream = IO.getReader(filePath);
		OutputStreamWriter outputStream = IO.getWriter(filePathEncoded);

		new Noise().encode(inputStream, outputStream);

		return filePathEncoded;
	}

	public static String removeCRC8(String filePath) throws IOException {
		String filePathWithoutType = filePath.replace(".ecc", "");
		String[] pathSplited = filePathWithoutType.split("\\.");
		String filePathDecoded = pathSplited[0] + "_noiseRemoved";

		for (int i = 1; i < pathSplited.length; i++)
			filePathDecoded += "." + pathSplited[i];

		InputStreamReader inputStream = IO.getReader(filePath);
		OutputStreamWriter outputStream = IO.getWriter(filePathDecoded);

		new Noise().decode(inputStream, outputStream);

		return filePathDecoded;
	}
}
