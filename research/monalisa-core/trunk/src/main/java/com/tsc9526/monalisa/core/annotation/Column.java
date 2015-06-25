package com.tsc9526.monalisa.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
		
}
