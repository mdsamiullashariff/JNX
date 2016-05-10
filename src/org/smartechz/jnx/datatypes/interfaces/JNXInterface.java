package org.smartechz.jnx.datatypes.interfaces;

public interface JNXInterface {

	public Object get(String key);

	public Object getEx(String exp);

	public String toJSONString() throws Exception;
}
