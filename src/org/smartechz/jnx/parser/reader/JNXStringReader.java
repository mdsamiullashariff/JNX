package org.smartechz.jnx.parser.reader;

import java.io.IOException;

import org.smartechz.jnx.parser.reader.interfaces.JNXReader;

public class JNXStringReader implements JNXReader {

	private char[] data;
	private int pos = 0;

	public JNXStringReader(String str) throws Exception {
		data = str.toCharArray();
	}

	public int next() throws IOException {
		return data[pos++];
	}

	public void push(int c) {
		pos--;
	}

	@Override
	public void close() {
		data = null;
	}

	@Override
	public int nextNonWhiteChar() throws IOException {
		int c = data[pos++];
		while (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
			c = data[pos++];
		}
		return c;
	}

}
