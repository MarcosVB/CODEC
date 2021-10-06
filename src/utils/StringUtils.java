package utils;

public class StringUtils {

	private StringUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String padLeft(String source, int size) {
		if (source.length() == size)
			return source;
		StringBuilder leftPaddedSource = new StringBuilder(source);
		while (leftPaddedSource.length() < size)
			leftPaddedSource.insert(0, '0');
		return leftPaddedSource.toString();
	}

}
