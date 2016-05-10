package org.smartechz.jnx.parser.reader.interfaces;

import java.io.IOException;

public interface JNXReader {
	public int next() throws IOException;

	public void push(int c);

	public void close() throws IOException;

	public int nextNonWhiteChar() throws IOException;
}
