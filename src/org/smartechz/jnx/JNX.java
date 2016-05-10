package org.smartechz.jnx;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import org.smartechz.jnx.datatypes.interfaces.JNXInterface;
import org.smartechz.jnx.formater.JNXFormater;
import org.smartechz.jnx.formater.writer.JNXFileWriter;
import org.smartechz.jnx.parser.JNXParser;

public class JNX {
	public static final short DEFAULT_FORMAT = 0;
	public static final short MINIFIED_FORMAT = 1;
	public static final short SIMPLE_FORMAT = 2;
	public static final short COMPACT_FORMAT = 3;

	public static Object parse(File file) throws Exception {
		return (new JNXParser()).parse(file);
	}

	public static Object parse(String str) throws Exception {
		return (new JNXParser()).parse(str);
	}

	public static Object parse(InputStream stream) throws Exception {
		return (new JNXParser()).parse(stream);
	}

	public static String format(Object jnxo) throws Exception {
		JNXFormater frmt = new JNXFormater(DEFAULT_FORMAT);
		return frmt.format(jnxo);
	}

	public static void format(Object jnxo, File file) throws Exception {
		JNXFileWriter writer = new JNXFileWriter(file);
		JNXFormater frmt = new JNXFormater(MINIFIED_FORMAT);
		frmt.format(jnxo, writer);
		writer.close();

	}

	// merge getEx tokens and parsekeys - optimize
	public static Object getEx(Object obj, String path) {
		return getEx(obj, parseKeys(path));
	}

	public static Object getEx(Object obj, ArrayList<String> tokens) {
		Object tobj = null;
		for (int i = 0; i < tokens.size(); i++) {
			String token = tokens.get(i).trim();
			if (obj instanceof JNXInterface) {
				tobj = ((JNXInterface) obj).get(token);
				if (tobj == null) {
					return null;
				}
				obj = tobj;
			} else {
				return null;
			}
		}
		return obj;
	}

	private static ArrayList<String> parseKeys(String path) {
		String repPath = path;
		try {
			repPath = path.replaceAll("\\[([^\\[\\]]*)\\]", ".$1");
		} catch (Exception e) {
		}

		String[] tokens = repPath.split("\\.");

		ArrayList<String> ptokens = new ArrayList<String>();
		boolean start = false;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tokens.length; i++) {
			if (start) {
				sb.append("." + tokens[i]);
				if (tokens[i].charAt(tokens[i].length() - 1) == '\'') {
					String key = sb.toString();
					ptokens.add(key.substring(1, key.length() - 1));
					start = false;
				}
			} else {
				if (tokens[i].charAt(0) == '\'') {
					if (tokens[i].charAt(tokens[i].length() - 1) == '\'') {
						ptokens.add(tokens[i].substring(1,
								tokens[i].length() - 1));
					} else {
						sb.append(tokens[i]);
						start = true;
					}
				} else {
					ptokens.add(tokens[i]);
				}
			}
		}
		return ptokens;
	}
}
