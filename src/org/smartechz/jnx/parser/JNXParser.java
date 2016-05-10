package org.smartechz.jnx.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;

import org.smartechz.jnx.datatypes.JNXArray;
import org.smartechz.jnx.datatypes.JNXObject;
import org.smartechz.jnx.parser.reader.JNXCharArrayReader;
import org.smartechz.jnx.parser.reader.JNXFileReader;
import org.smartechz.jnx.parser.reader.JNXStreamReader;
import org.smartechz.jnx.parser.reader.JNXStringReader;
import org.smartechz.jnx.parser.reader.interfaces.JNXReader;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class JNXParser {

	private JNXReader cr = null;
	private HashMap classMap = null;

	public Object parse(String str) throws Exception {
		cr = new JNXStringReader(str);
		Object val = getValue();
		cr.close();
		return val;
	}

	public Object parse(File file) throws Exception {
		// cr = new JNXStreamReader(new BufferedInputStream(new
		// FileInputStream(file)));
		cr = new JNXFileReader(file);
		// cr = new CopyOfJNXFileReader(file);
		Object val = getValue();
		cr.close();
		return val;

	}

	public Object parse(InputStream istr) throws Exception {
		cr = new JNXStreamReader(istr);
		Object val = getValue();
		cr.close();
		return val;
	}

	public Object parse(char[] data) throws Exception {
		cr = new JNXCharArrayReader(data);
		Object val = getValue();
		cr.close();
		return val;
	}

	private Object getValue() throws IOException, Exception {
		int c;

		while (true) {
			c = cr.nextNonWhiteChar();

			if (c == '\"') {
				return getString();
			} else if (c == '{') {
				JNXObject returnVal = new JNXObject();

				int c2 = cr.nextNonWhiteChar();

				while (true) {
					if (c2 == '\"'
							|| ((c2 >= 65 && c2 <= 90) || (c2 >= 97 && c2 <= 122))) {
						String key;
						Object val;

						if (c2 == '\"') {
							key = getString();
							c2 = cr.nextNonWhiteChar();
							if (c2 != ':') {
								throw parseError("Illegal Char, ':' expected",
										c2);
							}
						} else {
							key = getObjectKey(c2);
						}

						// getValue
						val = getValue();

						// put value
						returnVal.put(key, val);

						// confirm object closure
						c2 = cr.nextNonWhiteChar();

						if (c2 == '}') {
							return returnVal;
						} else if (c2 == ',') {
							c2 = cr.nextNonWhiteChar();
						} else {
							throw parseError(
									"Object not closed properly, '}' expected",
									c2);
						}
					} else if (c2 == '}') {
						return returnVal;
					} else if (c2 == '/') {
						ignoreComment();
					} else {
						throw parseError(
								"Object not closed properly, '}' expected", c2);
					}
				}
			} else if (c == '[') {
				JNXArray returnVal = new JNXArray();

				int c2 = cr.nextNonWhiteChar();

				while (true) {
					if (c2 == ']') {
						return returnVal;
					} else if (c2 == '/') {
						ignoreComment();
					} else {
						cr.push(c2);
						returnVal.add(getValue());
						c2 = cr.nextNonWhiteChar();
						if (c2 == ']') {
							return returnVal;
						} else if (c2 == ',') {
							c2 = cr.nextNonWhiteChar();
						} else {
							throw parseError(
									"Array not closed properly, ']' expected",
									c2);
						}
					}
				}

			} else if ((c >= '0' && c <= '9') || c == '.' || c == '-'
					|| c == '+') {
				return getNumeral(c);
			} else if (c == 'n') {
				if (lookupString('u', 'l', 'l')) {
					return null;
				} else {
					throw parseError("Ilegal value: expected 'null'", null);
				}
			} else if (c == 'f') {
				if (lookupString('a', 'l', 's', 'e')) {
					return new Boolean(false);
				} else {
					throw parseError("Ilegal value: expected 'false'", null);
				}
			} else if (c == 't') {
				if (lookupString('r', 'u', 'e')) {
					return new Boolean(true);
				} else {
					throw parseError("Ilegal value: expected 'true'", null);
				}
			} else if (c == '@') {
				return new Date((Long) getIntegerNumeral());
			} else if (c == '#') {
				return getCustomObject();
			} else if (c == '/') {
				ignoreComment();
			} else if (c == -1) {
				throw parseError("Cannot parse empty file.", null);
			} else {
				throw parseError("Illegal Value", c);
			}
		}
	}

	private String getString() throws Exception {
		StringBuilder sb = new StringBuilder(32);
		int c = cr.next();
		while (c != '\"') {
			if (c == '\\') {
				c = cr.next();
				if (c == 'b') {
					sb.append('\b');
				} else if (c == 't') {
					sb.append('\t');
				} else if (c == 'n') {
					sb.append('\n');
				} else if (c == 'r') {
					sb.append('\r');
				} else if (c == 'f') {
					sb.append('\f');
				} else if (c == '\'') {
					sb.append('\'');
				} else if (c == '\"') {
					sb.append('\"');
				} else if (c == '\\') {
					sb.append('\\');
				} else if (c == 'u') {
					int hexValue = 0;
					for (int count = 4; count > 0; count--) {
						int hexChar = cr.next();
						hexValue = hexValue * 16;
						if (hexChar >= 48 && hexChar <= 57) {
							hexValue += (hexChar - 48);
						} else if (hexChar >= 65 && hexChar <= 70) {
							// A(10)=65 so A-55=10
							hexValue += (hexChar - 55);
						}
					}
					sb.append(Character.toChars(hexValue));
				} else {
					throw parseError("Invalid String literal '\\" + (char) c
							+ "'", "\\" + (char) c);
				}
			} else {
				sb.append((char) c);
			}
			c = cr.next();
		}
		return sb.toString();
	}

	private Object getNumeral(int c) throws Exception {
		Long integralNumber = (long) 0;
		boolean isNegative = false;

		if (c == '+') {
			// default number is + so do nothing
		} else if (c == '-') {
			// add minus sign in the beginning
			isNegative = true;
		} else if (c == '.') {
			return getFractalNumeral(isNegative, integralNumber);
		} else {
			integralNumber = (long) (c - 48);
		}
		while (true) {
			c = cr.next();
			if (c >= 48 && c <= 57) {
				integralNumber = integralNumber * 10 + (long) (c - 48);
			} else if (c == '.') {
				return getFractalNumeral(isNegative, integralNumber);
			} else {
				break;
			}
		}
		cr.push(c);

		return isNegative ? -integralNumber : integralNumber;
	}

	private Object getFractalNumeral(boolean isNegative, Long integralNumber)
			throws IOException {
		Double fractalNumber = new Double(integralNumber);
		Double fractal = 0.1;
		int c;
		while (true) {
			c = cr.next();
			if (c >= 48 && c <= 57) {
				fractalNumber = fractalNumber + ((c - 48) * fractal);
				fractal = fractal / 10;
			} else {
				break;
			}
		}
		cr.push(c);
		return isNegative ? -fractalNumber : fractalNumber;
	}

	private Object getIntegerNumeral() throws Exception {
		StringBuilder number = new StringBuilder();
		int c = cr.next();

		while (c >= 48 && c <= 57) {
			number.append((char) c);
			c = cr.next();
		}
		cr.push(c);
		return Long.parseLong(number.toString());
	}

	private Object getCustomObject() throws Exception {
		String cname = getClassToken();
		int c;
		boolean notClosed = true;
		Object val;

		JNXArray params = new JNXArray();
		while (notClosed) {
			c = cr.nextNonWhiteChar();
			switch (c) {
			case '/':
				ignoreComment();
				break;
			case ',':
				params.add((Object) null);
				break;
			case ')':
				notClosed = false;
				break;
			case -1:
				throw parseError("Array not closed properly, ']' expected", c);
			default:
				cr.push(c);
				val = getValue();
				params.add(val);
				c = cr.nextNonWhiteChar();
				if (c == ')') {
					notClosed = false;
				} else if (c != ',') {
					throw parseError("Array not closed properly, ']' expected",
							c);
				}
			}
		}
		try {
			String clname = (String) classMap.get(cname);

			if (params == null || params.size() < 1) {
				return Class.forName(clname).getConstructor().newInstance();
			}
			int size = params.size();
			Class[] types = new Class[size];
			Object[] values = new Object[size];

			for (int i = 0; i < size; i++) {
				types[i] = (values[i] = params.get(i)).getClass();
			}

			return Class.forName(clname).getConstructor(types)
					.newInstance(values);
		} catch (Exception e) {
			if (classMap.get(cname) == null) {
				throw new IllegalArgumentException(
						"Instance mapping not found for #" + cname);
			} else {
				throw new IllegalArgumentException(
						"Invalid constructor or illegal arguments : #" + cname
								+ params.toJSONString());
			}
		}
	}

	private void ignoreComment() throws Exception {
		int c = cr.next();
		if (c == '/') {
			do {
				c = cr.next();
			} while (c != '\n');
			return;
		} else if (c == '*') {
			do {
				c = cr.next();
				if (c == '*') {
					c = cr.next();
					if (c == '/')
						return;
					else
						cr.push(c);
				}
			} while (true);
		}

		throw parseError("Comments not properly ended", c);
	}

	private boolean lookupString(char... chars) throws IOException {
		int len = chars.length;
		for (int i = 0; i < len; i++) {
			if (cr.next() != chars[i])
				return false;
		}
		return true;
	}

	private Exception parseError(String err, int c) throws Exception {
		return parseError(err, (char) c + "");
	}

	private Exception parseError(String err, String c) throws Exception {
		String s = (c != null ? c : "");
		int i = 0, limit = 50;
		for (; i < limit; i++) {
			s += (char) cr.next();
		}
		if (i < limit) {
			s += "\n[#END]";
		} else {
			s += "\n...";
		}

		return new Exception(err + "\n...\n" + s);

	}

	private String getObjectKey(int c) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append((char) c);
		c = cr.next();
		while ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
				|| (c <= '0' && c >= '9')) {
			sb.append((char) c);
		}
		if (c != ':' && cr.nextNonWhiteChar() != ':') {
			throw parseError("Illegal Char, ':' expected", c);
		}

		return sb.toString();
	}

	private String getClassToken() throws Exception {
		int c = -1;
		StringBuilder sb = new StringBuilder();
		while ((Character.isAlphabetic((c = cr.next())) || Character.isDigit(c))) {
			sb.append((char) c);
		}
		if (c != '(' && cr.nextNonWhiteChar() != '(') {
			throw parseError("Illegal Char, '(' expected", c);
		}

		return sb.toString();
	}

	public void register(String tag, Class cls) {
		if (classMap == null) {
			classMap = new HashMap<String, String>();
		}
		classMap.put(tag, cls.getCanonicalName());
	}

}
