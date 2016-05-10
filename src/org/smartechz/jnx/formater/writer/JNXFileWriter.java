package org.smartechz.jnx.formater.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.smartechz.jnx.formater.writer.interfaces.JNXWriter;

public class JNXFileWriter implements JNXWriter {

	private BufferedWriter writer;

	public JNXFileWriter(File file) throws IOException {
		this.writer = new BufferedWriter(new FileWriter(file, false));
	}

	@Override
	public void write(Object obj) throws IOException {
		writer.write(obj + "");

	}

	@Override
	public void write(String str) throws IOException {
		writer.write(str);
	}

	@Override
	public void write(char c) throws IOException {
		writer.write(c);
	}

	public void close() throws Exception {
		writer.close();
	}

}
