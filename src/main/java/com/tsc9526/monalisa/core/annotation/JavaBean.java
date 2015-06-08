package com.tsc9526.monalisa.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface JavaBean{
	boolean equals() default true;
	boolean hash()   default true;
	boolean get()    default true;
	boolean set()    default true;
}