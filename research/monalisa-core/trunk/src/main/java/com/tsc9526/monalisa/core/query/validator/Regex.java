package com.tsc9526.monalisa.core.query.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Regex {
	 
	/**
	 * 匹配的正则表达式
	 */
	String value();
	
	/**
	 * 
	 * @return 匹配错误时提示的信息
	 */
	String message();	
}
