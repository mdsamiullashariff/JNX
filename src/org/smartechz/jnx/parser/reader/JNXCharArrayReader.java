package org.smartechz.jnx.parser.reader;

import java.io.IOException;

import org.smartechz.jnx.parser.reader.interfaces.JNXReader;

public class JNXCharArrayReader implements JNXReader {

	private char[] data;
	private int pos = 0;

	public JNXCharArrayReader(char[] data) throws Exception {
		this.data = data;
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
