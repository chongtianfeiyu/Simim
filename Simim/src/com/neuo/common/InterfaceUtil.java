package com.neuo.common;

public class InterfaceUtil {
	public static boolean isInterface(Class<?> c, String interfaceName) {
		Class<?>[] interfaces = c.getInterfaces();
		for (int i = 0, j = interfaces.length; i < j; i++) {
			if (interfaces[i].getName().equals(interfaceName)) {
				return true;
			} else {
				Class<?>[] interfaces2 = interfaces[i].getInterfaces();
				for (int x = 0; x < interfaces2.length; x++)
				{
					if (interfaces2[x].getName().equals(interfaceName))
					{
						return true;
					}
					else if (isInterface(interfaces2[x], interfaceName))
					{
						return true;
					}
				}
			}
		}
		
		if (null != c.getSuperclass()) {
			return isInterface(c.getSuperclass(), interfaceName);
		}
		return false;
	}
}
