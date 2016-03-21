package com.tsc9526.monalisa.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Select{
	/**
	 * @return 查询输出结果的类名. 默认值为: Result+方法名
	 */
	String name()  default "";
	
	/**
	 * @return 指定运行方法时, 参数初始化代码. 默认由系统自动产生.
	 */
	String build() default "";
}