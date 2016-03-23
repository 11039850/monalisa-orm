/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
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
