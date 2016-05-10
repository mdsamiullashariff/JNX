package org.smartechz.jnx.formater;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.smartechz.jnx.JNX;
import org.smartechz.jnx.datatypes.JNXArray;
import org.smartechz.jnx.datatypes.JNXObject;
import org.smartechz.jnx.datatypes.interfaces.JNXInterface;
import org.smartechz.jnx.formater.writer.JNXStringWriter;
import org.smartechz.jnx.formater.writer.interfaces.JNXWriter;

@SuppressWarnings("rawtypes")
public class JNXFormater {
	private String indentation = "";
	private short type;

	public JNXFormater() {
		this(JNX.DEFAULT_FORMAT);
	}

	public JNXFormater(short type) {
		this.type = type;
	}

	public String format(Object obj) throws Exception {
		JNXStringWriter writer = new JNXStringWriter();
		format(obj, writer);
		return writer.toString();
	}

	public void format(Object obj, JNXWriter writer) throws Exception {
		if (type == JNX.DEFAULT_FORMAT) {
			defaultFormat(obj, writer);
		} else if (type == JNX.MINIFIED_FORMAT) {
			minifiedFormat(obj, writer);
		} else if (type == JNX.SIMPLE_FORMAT) {
			simpleFormat(obj, writer);
		} else if (type == JNX.COMPACT_FORMAT) {
			compactFormat(obj, writer);
		}
	}

	private void defaultFormat(Object obj, JNXWriter writer) throws Exception {
		if (obj instanceof Long || obj instanceof Integer
				|| obj instanceof Float || obj instanceof Double
				|| obj instanceof Boolean) {
			writer.write(obj);
		} else if (obj instanceof JNXArray) {
			JNXArray sjarr = (JNXArray) obj;
			int size = sjarr.size();
			if (size == 0) {
				writer.write("[]");
			} else {
				String pervIndentation = indentation;
				indentation += "\t";
				writer.write("[\n" + indentation);

				defaultFormat(sjarr.get(0), writer);

				for (int i = 1; i < size; i++) {
					writer.write(",\n" + indentation);
					defaultFormat(sjarr.get(i), writer);
				}

				indentation = pervIndentation;
				writer.write("\n" + indentation + "]");
			}
		} else if (obj instanceof JNXObject) {

			JNXObject sjobj = (JNXObject) obj;
			Set ks = sjobj.keySet();
			Iterator itr = ks.iterator();

			int size = ks.size();
			if (size == 0) {
				writer.write("{}");
			} else {
				String pervIndentation = indentation;
				indentation += "\t";
				String key = (String) itr.next();

				writer.write("{\n" + indentation + "\"" + key + "\":");

				defaultFormat(sjobj.get(key), writer);

				while (itr.hasNext()) {
					key = (String) itr.next();
					writer.write(",\n" + indentation + "\"" + key + "\":");
					defaultFormat(sjobj.get(key), writer);
				}

				indentation = pervIndentation;
				writer.write("\n" + indentation + "}");
			}
		} else if (obj instanceof String) {
			writeString((String) obj, writer);
		} else if (obj instanceof Date) {
			writer.write("@" + ((Date) obj).getTime());
		} else if (obj instanceof JNXInterface) {
			writer.write(((JNXInterface) obj).toJSONString());
		} else if (obj == null) {
			writer.write("null");
		} else {
			writer.write("\"" + obj.toString() + "\"");
		}
	}

	private void minifiedFormat(Object obj, JNXWriter writer) throws Exception {
		if (obj instanceof Long || obj instanceof Integer
				|| obj instanceof Float || obj instanceof Double
				|| obj instanceof Boolean) {
			writer.write(obj);
		} else if (obj instanceof JNXArray) {
			JNXArray sjarr = (JNXArray) obj;
			int size = sjarr.size();
			if (size == 0) {
				writer.write("[]");
			} else {
				writer.write("[");

				minifiedFormat(sjarr.get(0), writer);

				for (int i = 1; i < size; i++) {
					writer.write(",");
					minifiedFormat(sjarr.get(i), writer);
				}

				writer.write("]");
			}
		} else if (obj instanceof JNXObject) {

			JNXObject sjobj = (JNXObject) obj;
			Set ks = sjobj.keySet();
			Iterator itr = ks.iterator();

			int size = ks.size();
			if (size == 0) {
				writer.write("{}");
			} else {
				String key = (String) itr.next();

				writer.write("{\"" + key + "\":");

				minifiedFormat(sjobj.get(key), writer);

				while (itr.hasNext()) {
					key = (String) itr.next();
					writer.write(",\"" + key + "\":");
					minifiedFormat(sjobj.get(key), writer);
				}

				writer.write("}");
			}
		} else if (obj instanceof String) {
			writeString((String) obj, writer);
		} else if (obj instanceof Date) {
			writer.write("@" + ((Date) obj).getTime());
		} else if (obj instanceof JNXInterface) {
			writer.write(((JNXInterface) obj).toJSONString());
		} else if (obj == null) {
			writer.write("null");
		} else {
			writer.write("\"" + obj.toString() + "\"");
		}
	}

	private void simpleFormat(Object obj, JNXWriter writer) throws Exception {
		if (obj instanceof Long || obj instanceof Integer
				|| obj instanceof Float || obj instanceof Double
				|| obj instanceof Boolean) {
			writer.write(obj);
		} else if (obj instanceof JNXArray) {
			JNXArray sjarr = (JNXArray) obj;
			int size = sjarr.size();
			if (size == 0) {
				writer.write("[]");
			} else {
				String pervIndentation = indentation;
				indentation += "\t";
				writer.write("[\n" + indentation);

				simpleFormat(sjarr.get(0), writer);

				for (int i = 1; i < size; i++) {
					writer.write(",\n" + indentation);
					simpleFormat(sjarr.get(i), writer);
				}

				indentation = pervIndentation;
				writer.write("\n" + indentation + "]");
			}
		} else if (obj instanceof JNXObject) {

			JNXObject sjobj = (JNXObject) obj;
			Set ks = sjobj.keySet();
			Iterator itr = ks.iterator();

			int size = ks.size();
			if (size == 0) {
				writer.write("{}");
			} else {
				String pervIndentation = indentation;
				indentation += "\t";
				String key = (String) itr.next();

				writer.write("{\n" + indentation + key + ":");

				simpleFormat(sjobj.get(key), writer);

				while (itr.hasNext()) {
					key = (String) itr.next();
					writer.write(",\n" + indentation + key + ":");
					simpleFormat(sjobj.get(key), writer);
				}

				indentation = pervIndentation;
				writer.write("\n" + indentation + "}");
			}
		} else if (obj instanceof String) {
			writeString((String) obj, writer);
		} else if (obj instanceof Date) {
			writer.write("@" + ((Date) obj).getTime());
		} else if (obj instanceof JNXInterface) {
			writer.write(((JNXInterface) obj).toJSONString());
		} else if (obj == null) {
			writer.write("null");
		} else {
			writer.write("\"" + obj.toString() + "\"");
		}
	}

	private void compactFormat(Object obj, JNXWriter writer) throws Exception {
		if (obj instanceof Long || obj instanceof Integer
				|| obj instanceof Float || obj instanceof Double
				|| obj instanceof Boolean) {
			writer.write(obj);
		} else if (obj instanceof JNXArray) {
			JNXArray sjarr = (JNXArray) obj;
			int size = sjarr.size();
			if (size == 0) {
				writer.write("[]");
			} else {
				writer.write("[");

				compactFormat(sjarr.get(0), writer);

				for (int i = 1; i < size; i++) {
					writer.write(",");
					compactFormat(sjarr.get(i), writer);
				}

				writer.write("]");
			}
		} else if (obj instanceof JNXObject) {

			JNXObject sjobj = (JNXObject) obj;
			Set ks = sjobj.keySet();
			Iterator itr = ks.iterator();

			int size = ks.size();
			if (size == 0) {
				writer.write("{}");
			} else {
				String key = (String) itr.next();

				writer.write("{" + key + ":");

				compactFormat(sjobj.get(key), writer);

				while (itr.hasNext()) {
					key = (String) itr.next();
					writer.write("," + key + ":");
					compactFormat(sjobj.get(key), writer);
				}

				writer.write("}");
			}
		} else if (obj instanceof String) {
			writeString((String) obj, writer);
		} else if (obj instanceof Date) {
			writer.write("@" + ((Date) obj).getTime());
		} else if (obj instanceof JNXInterface) {
			writer.write(((JNXInterface) obj).toJSONString());
		} else if (obj == null) {
			writer.write("null");
		} else {
			writer.write("\"" + obj.toString() + "\"");
		}
	}

	private void writeString(String obj, JNXWriter writer) throws IOException {
		String str = (String) obj;
		int len = str.length();
		writer.write("\"");
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			switch (c) {
			case '\b':
				writer.write("\\b");
				break;
			case '\t':
				writer.write("\\t");
				break;
			case '\n':
				writer.write("\\n");
				break;
			case '\f':
				writer.write("\\f");
				break;
			case '\r':
				writer.write("\\r");
				break;
			case '\'':
				writer.write("\\\'");
				break;
			case '\"':
				writer.write("\\\"");
				break;
			case '\\':
				writer.write("\\\\");
				break;
			default:
				writer.write(c);
			}
		}
		writer.write("\"");
	}

}
