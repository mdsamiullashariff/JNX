package org.smartechz.jnx.parser.reader;

import java.io.IOException;
import java.io.InputStream;

import org.smartechz.jnx.parser.reader.interfaces.JNXReader;

public class JNXStreamReader implements JNXReader {
	private int pushedChar = -1;

	private InputStream stream;

	private int EmptyChar = -1;

	public JNXStreamReader(InputStream stream) {
		this.stream = stream;
	}

	public int next() throws IOException {
		if (pushedChar != EmptyChar) {
			int returnchar = pushedChar;
			pushedChar = EmptyChar;
			return returnchar;
		} else {
			return stream.read();
		}
	}

	public void push(int c) {
		pushedChar = c;
	}

	@Override
	public void close() throws IOException {
		stream.close();
	}

	@Override
	public int nextNonWhiteChar() throws IOException {
		int c;
		if (pushedChar != EmptyChar) {
			c = pushedChar;
			pushedChar = EmptyChar;
		} else {
			c = stream.read();
		}

		while (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
			c = stream.read();
		}
		return c;
	}

}
