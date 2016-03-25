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
package com.tsc9526.monalisa.core.converters.impl;

import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.core.converters.Conversion;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class ArrayTypeConversion implements Conversion<Object[]>{

	public Object[] getTypeKeys() {
		return new Object[] {
			byte[].class,Byte[].class,
			char[].class,Character[].class,
			short[].class,Short[].class,
			int[].class,Integer[].class,
			long[].class,Long[].class,
			float[].class,Float[].class,
			double[].class,Double[].class,
			boolean[].class,Boolean[].class,

			Object[].class,
			TYPE_ARRAYS
		};
	}
	
	public Object[] convert(Object value) {
		if (!(value instanceof Object[])) {
			List<Object> vs = new ArrayList<Object>();

			if (value instanceof byte[]) {
				for (byte i : (byte[]) value) {
					vs.add(new Byte(i));
				}
			}else if (value instanceof char[]) {
				for (char i : (char[]) value) {
					vs.add(new Character(i));
				}
			}else if (value instanceof short[]) {
				for (short i : (short[]) value) {
					vs.add(new Short(i));
				}
			}else if (value instanceof int[]) {
				for (int i : (int[]) value) {
					vs.add(new Integer(i));
				}
			}else if (value instanceof long[]) {
				for (long i : (long[]) value) {
					vs.add(new Long(i));
				}
			}else if (value instanceof float[]) {
				for (float i : (float[]) value) {
					vs.add(new Float(i));
				}
			}else if (value instanceof double[]) {
				for (double i : (double[]) value) {
					vs.add(new Double(i));
				}
			}else if (value instanceof boolean[]) {
				for (boolean i:(boolean[]) value) {
					vs.add(new Boolean(i));
				}
			}else{
				vs.add(value);
			}
			
			return vs.toArray(new Object[] {});
		} else {
			return (Object[]) value;
		}
	}
}
