package org.smartechz.jnx.formater.writer;

import java.io.IOException;

import org.smartechz.jnx.formater.writer.interfaces.JNXWriter;

public class JNXStringWriter implements JNXWriter {
	private StringBuilder builder;

	public JNXStringWriter() {
		this.builder = new StringBuilder();
	}

	@Override
	public void write(Object obj) {
		builder.append(obj);
	}
	
	@Override
	public String toString() {
		return builder.toString();
	}

	@Override
	public void write(String str) throws IOException {
		builder.append(str);
		
	}

	@Override
	public void write(char c) throws IOException {
		builder.append(c);
		
	}

}
