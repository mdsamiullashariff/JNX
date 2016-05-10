package org.smartechz.jnx.formater.writer.interfaces;

import java.io.IOException;

public interface JNXWriter {
	void write(String str) throws IOException;

	void write(char c) throws IOException;

	void write(Object obj) throws IOException;;
}
