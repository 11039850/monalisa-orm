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
package com.tsc9526.monalisa.core.converters;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public interface Conversion<T> {
	public static final String TYPE_OBJECT  = "object"; 
	public static final String TYPE_STRING  = "string"; 
	public static final String TYPE_INT     = "int"; 
	public static final String TYPE_INTEGER = "integer"; 
	public static final String TYPE_LONG    = "long"; 
	public static final String TYPE_FLOAT   = "float";
	public static final String TYPE_DOUBLE  = "double";
	public static final String TYPE_SHORT   = "short";
	public static final String TYPE_BOOLEAN = "boolean";
	public static final String TYPE_BYTE    = "byte";
	public static final String TYPE_CHAR    = "char";
	public static final String TYPE_CHARACTER   = "character";
	public static final String TYPE_BIG_DECIMAL = "bigdecimal";
	public static final String TYPE_DATETIME    = "datetime";
	
	public Object[] getTypeKeys();
 
	public T convert(Object value);
}