package org.smartechz.jnx.datatypes;

import java.util.ArrayList;

import org.smartechz.jnx.JNX;
import org.smartechz.jnx.datatypes.interfaces.JNXInterface;

@SuppressWarnings({ "rawtypes", "serial" })
public class JNXArray extends ArrayList implements JNXInterface {

	@Override
	public Object get(String key) {
		try {
			return super.get(Integer.parseInt(key.trim()));
		} catch (Exception e) {
			return null;
		}
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
