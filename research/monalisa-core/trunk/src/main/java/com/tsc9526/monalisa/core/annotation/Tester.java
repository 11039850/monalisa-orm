package com.tsc9526.monalisa.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Tester{
	TestFramework framework() default TestFramework.TESTNG;
	 
	public static enum TestFramework{
		TESTNG,
		JUNIT;
	}
}