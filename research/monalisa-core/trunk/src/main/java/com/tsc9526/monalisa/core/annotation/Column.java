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
package com.tsc9526.monalisa.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Types;


/**
 *  
 * @author zzg.zhou(11039850@qq.com)
 */
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	/**
	 * @return 表名
	 */
	String table() default "";
	
	/**
	 * @return 列名
	 */
	String name() default "";
	
	/**
	 * @return 是否主键
	 */
	boolean key() default false;
	
	/**
	 * @return 是否自动增长
	 */
	boolean auto() default false;
	
	/**
	 * @return 非空字段
	 */
	boolean notnull() default false;
			
	
	/**
	 * @return 长度 (0-未知)
	 */
	int length() default 0;
	
	/**
	 * @return 备注
	 */
	String remarks() default "";
	
	/**
	 * @return 缺省值, 用字符串"NULL", 来表示NULL字段
	 */
	String value() default "";
	
	/**
	 * @return JDBC type, Default is INTEGER
	 */
	int jdbcType() default Types.INTEGER;
		
}
