package codec;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public interface Codec {
	public void encode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException;

	public void decode(InputStreamReader inputStream, OutputStreamWriter outputStream) throws IOException;
}
