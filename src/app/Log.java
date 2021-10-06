package app;

import java.io.File;

public class Log {

	private static final File file = new File(Log.class.getProtectionDomain().getCodeSource().getLocation().getPath());

	public static void path() {
		System.out.println("\n" + file.getPath());
	}

	public static void log(String log) {
		System.out.println(log);
	}

	public static void welcome() {
		System.out.println("\n\n****TEORIA DA INFORMAÇÃO 202102 - T1 e T2 - CODEC W/ CRC-8****");
		System.out.println("\nUse command <help> to learn how to use it");
	}

	public static void help() {
		System.out.println("\nUse command line to call features");
		commandStructure();
		examples();
		supported();
		System.out.println("\nPath of the file is relative to the .jar location.");
		System.out.println("\nUse command <q> to leave");
	}

	public static void commandStructure() {
		System.out.println("\nCommand structure:");
		System.out.println("<encode> <encodeType> <filePath>");
		System.out.println("<decode> <filePath>");
		System.out.println("<noise> <filePath>");
		System.out.println("<unnoise> <filePath>");
	}

	public static void examples() {
		System.out.println("\nExamples:");
		System.out.println("> encode delta alice29.txt");
		System.out.println("> decode alice29.txt.cod");
		System.out.println("> encode fibonacci ../folder/alice29.txt");
		System.out.println("> noise alice29.txt.cod");
		System.out.println("> unnoise alice29.txt.cod.ecc");
	}

	public static void supported() {
		System.out.println("\nSupported encoding types:\nbinary, delta, eliasgamma, fibonacci, golomb, unary.");
		System.out.println("\nSupported noise treatment:\nCRC-8");
	}
}
