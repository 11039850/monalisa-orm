package com.tsc9526.monalisa.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *  
 * @author zzg.zhou(11039850@qq.com)
 */
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {
	/**
	 * @return 索引名
	 */
	String name() default "";
	
	/**
	 * @return 索引类型
	 */
	int type() default 0;	
	
	/**
	 * @return 是否唯一键
	 */
	boolean unique() default false;
	
	/**
	 * @return 索引字段名
	 */
	String[] fields() default {} ;
}
