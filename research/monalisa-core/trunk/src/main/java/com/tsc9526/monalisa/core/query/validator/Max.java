package com.tsc9526.monalisa.core.query.validator;

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
public @interface Max {	 
	/**
	 * @return 最大值
	 */
	long value();
	
	/**
	 * 
	 * @return 匹配错误时提示的信息
	 */
	String message();	
		 
}
