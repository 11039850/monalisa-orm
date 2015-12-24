package com.tsc9526.monalisa.core.tools;

import java.io.ObjectStreamClass;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class JavaLoader {

	public static void main(String[] args) {
		JavaLoader loader=JavaLoader.load(JavaLoader.class);
	}
	public static <T> T load(Class<T> clazz){
		long version=0;		
		ObjectStreamClass osc=ObjectStreamClass.lookup(clazz);
		if(osc!=null){
			version=osc.getSerialVersionUID();
		}
		
		JavaCompiler javac=ToolProvider.getSystemJavaCompiler();
		javac.run(System.in, System.out,System.err, "-d",".","temp.java");
		return null;
	}
}
