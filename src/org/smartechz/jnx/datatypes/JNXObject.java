package org.smartechz.jnx.datatypes;

import java.util.LinkedHashMap;

import org.smartechz.jnx.JNX;
import org.smartechz.jnx.datatypes.interfaces.JNXInterface;

@SuppressWarnings({ "rawtypes", "serial" })
public class JNXObject extends LinkedHashMap implements JNXInterface {

	public Long getLong(String key, Long def) {
		Object obj = get(key);
		if (obj == null)
			return def;
		if (obj instanceof String) {
			String value = (String) obj;
			try {
				return Long.parseLong(value);
			} catch (Exception e) {
				return def;
			}
		} else {
			return (Long) obj;
		}
	}

	public Boolean getBoolean(String key, Boolean def) {
		Object obj = get(key);
		if (obj == null)
			return def;
		if (obj instanceof String) {
			String value = (String) obj;
			if (value.equalsIgnoreCase("true"))
				return true;
			if (value.equalsIgnoreCase("false"))
				return false;
			return def;
		} else {
			return (Boolean) obj;
		}
	}

	public String getString(String key, String def) {
		Object obj = get(key);
		if (obj == null)
			return def;
		String value = (String) obj;
		if (value.isEmpty())
			return def;
		return (String) obj;
	}

	public int getInt(String key, int def) {
		Object obj = get(key);
		if (obj == null)
			return def;
		if (obj instanceof String) {
			String value = (String) obj;
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
				return def;
			}
		} else if (obj instanceof Long) {
			return (int) (long) (Long) obj;
		} else {
			return (Integer) obj;
		}
	}

	@Override
	public Object get(String key) {
		return super.get(key);
	}

	@Override
	public String toString() {
		try {
			return toJSONString();
		} catch (Exception e) {
			return e.getMessage();

		}
	}

	@Override
	public String toJSONString() throws Exception {
		return JNX.format(this);
	}

	@Override
	public Object getEx(String exp) {
		return JNX.getEx(this, exp);
	}

}
