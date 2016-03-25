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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.tsc9526.monalisa.core.converters.Conversion;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class StringTypeConversion implements Conversion<String> {
 
	public Object[] getTypeKeys() {
		return new Object[] {
			String.class,
			String.class.getName(),
			TYPE_STRING
		};
	}

	public String convert(Object value) {
		if (value.getClass().isArray()) {
			if (value.getClass().getComponentType()==Byte.TYPE) {
				value=new String((byte[])value);
			}else if (value.getClass().getComponentType()==Character.TYPE) {
				value=new String((char[])value);
			}else{
				value=""+value;
			}
		}else if (!(value instanceof String)) {
			if(value instanceof Date){
				SimpleDateFormat sdf=new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
				value=sdf.format((Date)value);
			}
			
			value=value.toString();
		}
		return (String)value;
	}
}
