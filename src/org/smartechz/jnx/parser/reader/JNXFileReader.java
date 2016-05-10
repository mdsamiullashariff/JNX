package org.smartechz.jnx.parser.reader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.smartechz.jnx.parser.reader.interfaces.JNXReader;

public class JNXFileReader implements JNXReader {
	private static final int BLOCKSIZE = 1024 * 1024;

	private char[] data;
	private int curr = 0;
	private long filesize;
	private long blockend = 0;

	private FileReader fr;

	public JNXFileReader(File file) throws Exception {
		fr = new FileReader(file);
		filesize = file.length();
		readNextBlock();

	}

	private void readNextBlock() throws IOException {
		long remaining = filesize - blockend;
		if (remaining > 0) {
			if (remaining > BLOCKSIZE) {
				blockend += BLOCKSIZE;
				data = new char[BLOCKSIZE];
				fr.read(data);
			} else {
				blockend = filesize;
				data = new char[(int) remaining];
				fr.read(data);
			}

		} else {
			data = new char[0];
		}
		curr = 0;
	}

	public int next() throws IOException {
		if (curr >= data.length) {
			readNextBlock();
		}
		return data[curr++];
	}

	public void push(int c) {
		curr--;
	}

	@Override
	public void close() {
		try {
			fr.close();
		} catch (IOException e) {
		}
		data = null;
	}

	@Override
	public int nextNonWhiteChar() throws IOException {
		int c = next();
		while (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
			c = next();
		}
		return c;
	}

}
