package com.tsc9526.monalisa.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

	/**
	 * @return 表名
	 */
	String name() default "";
	
	/**
	 * @return 备注
	 */
	String remarks() default "";
}
