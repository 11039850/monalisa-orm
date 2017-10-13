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
package com.tsc9526.monalisa.tools.string;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpDate {
	/**
	 * @return Current time(yyyy-MM-dd HH:mm:ss)
	 */
	public static String now() {
		return toTime(new Date());
	}

	/**
	 * 
	 * @return current day(yyyy-MM-dd)
	 */
	public static String today(){
		return toDay(new Date());
	}
	
	/**
	 * 
	 * @param date the date time
	 * @return  date format: yyyy-MM-dd HH:mm:ss
	 */
	public static String toTime(Date date){
		return toString(date,"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 
	 * @param date  the date time
	 * @return  date format: yyyy-MM-dd
	 */
	public static String toDay(Date date){
		return toString(date,"yyyy-MM-dd");
	}
	
	/**
	 * 
	 * @param date the date time
	 * @param format date format 
	 * @return date string 
	 * 
	 * @see java.text.SimpleDateFormat
	 */
	public static String toString(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		return sdf.format(date);
	}	
	 
	/**
	 * Auto detect the date format:<br>
	 * yyyy-MM-dd<br>
	 * yyyy-MM-dd HH<br>
	 * yyyy-MM-dd HH:mm<br>
	 * yyyy-MM-dd HH:mm:ss<br>
	 * yyyy-MM-dd HH:mm:ss.SSS<br>
	 *   
	 * @param date the date string
	 * 
	 * @return date
	 */
	public static Date toDate(String date){
		return toDate(date,null,null);
	}
	
	/**
	 * @param date the date string
	 * @param defaultValue default value
	 * @return date
	 * 
	 * @see #toDate(String)
	 */
	public static Date toDate(String date, Date defaultValue){
		return toDate(date,null,defaultValue);		  
	}
	
	/**
	 * 
	 * @param date the date string
	 * @param format  new SimpleDateFormat(format): auto detect date format if null or ''
	 * @param defaultValue default value
	 * @return date
	 * 
	 * @see #toDate(String)
	 */
	public static Date toDate(String date,String format,Date defaultValue){		 
		return getDate(date, format, defaultValue); 
	}
 
	/**
	 * @param v date object[java.util.Date|Long|String] 
	 * @param format new SimpleDateFormat(format): auto detect date format if null or ''
	 * @param defaultValue default value if v is null
	 * @return date
	 * 
	 * @see #toDate(String)
	 */
	public static Date getDate(Object v, String format, Date defaultValue) {
		if (v == null) {
			return defaultValue;
		} else {
			if (v instanceof Date) {
				return (Date) v;
			} else if (v.getClass() == Long.class || v.getClass() == long.class) {
				return new Date((Long) v);
			} else {
				String x = "" + v;

				try {
					if (format != null && format.length() > 0) {
						SimpleDateFormat sdf = new SimpleDateFormat(format);
						return sdf.parse(x);
					} else {
						int m = x.indexOf(":");
						if (m > 0) {
							if (x.indexOf(":", m + 1) > 0) {
								if(x.indexOf(".",m+1)>0){
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
									return sdf.parse(x);
								}else{
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									return sdf.parse(x);
								}
							} else {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
								return sdf.parse(x);
							}
						} else {
							if (x.indexOf(" ") > 0) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
								return sdf.parse(x);
							} else {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								return sdf.parse(x);
							}
						}
					}
				} catch (ParseException e) {
					throw new RuntimeException("Invalid date: " + x, e);
				}
			}
		}
	}
}
