package com.tsc9526.monalisa.core.tools;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Primitives {
	private static final Class<?>[] PRIMITIVE_TYPES = { int.class, long.class, short.class, float.class, double.class, byte.class, boolean.class, char.class,
			Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class };

	public static boolean isPrimitiveOrString(Object target) {
		if (target instanceof String) {
			return true;
		}

		Class<?> classOfPrimitive = target.getClass();
		for (Class<?> standardPrimitive : PRIMITIVE_TYPES) {
			if (standardPrimitive.isAssignableFrom(classOfPrimitive)) {
				return true;
			}
		}
		return false;
	}
}
