package com.tsc9526.monalisa.core.tools;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class JavaLoader {

	public static void main(String[] args) {
		JavaLoader.load();
	}
	
	public static void load(){
		JavaCompiler javac=ToolProvider.getSystemJavaCompiler();
		
		String dir="src/test/java/test/com/tsc9526/monalisa/core/mysql/mysqldb";
		
		javac.run(System.in, System.out,System.err,
				"-encoding", "utf-8",
				"-classpath","target/classes;target/test-classes", "-d","target/testing",dir+"/TestTable1.java");
		
	}
}
