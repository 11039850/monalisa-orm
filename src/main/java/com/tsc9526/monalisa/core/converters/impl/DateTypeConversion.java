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
public class DateTypeConversion implements Conversion<Date> {
	public static String[] datePattern = {DEFAULT_DATETIME_FORMAT,"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss.SSS"};  
	
	public Object[] getTypeKeys() {
		return new Object[] {
			java.util.Date.class,
			java.util.Date.class.getName(),
			TYPE_DATETIME
		};
	}

	public Date convert(Object value) {
		if (!(value instanceof java.util.Date)) {
			String v=(""+value).trim();
			 
			if (v.length()==0) {
				value=null;
			}else {
				value=tryLongToDate(v);
				if(value==null){
					value=tryStringToDate(v);
				}
			}
		}
		
		return (Date)value;
	}
		
	private Date tryLongToDate(String v){
		try{
			long ts=Long.parseLong(v);
			
			return new Date(ts);
		}catch(Exception e){
			return null;
		}
	}
	
	private Date tryStringToDate(String v){
		try{
			for(String f:datePattern){
				SimpleDateFormat sdf=new SimpleDateFormat(f);
				return sdf.parse(v);
			}
			return null;
		}catch(Exception e){
			return null;
		}
	}
}
