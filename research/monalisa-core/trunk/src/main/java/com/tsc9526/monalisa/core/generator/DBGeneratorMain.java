package com.tsc9526.monalisa.core.generator;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsc9526.monalisa.core.parser.executor.SQLGenerator;

/**
 * 数据库访问接口代码生成三种方式：<br>
 * <li>1. 使用Maven编译插件： <br> 
 * 	 &nbsp;&nbsp;&nbsp;&nbsp;&lt;annotationProcessor&gt;<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;com.tsc9526.monalisa.core.processor.DBAnnotationProcessor<br>
 *   &nbsp;&nbsp;&nbsp;&nbsp;&lt;/annotationProcessor&gt; </li>
 * <li>2. 使用Eclipse插件：<br> </li>
 * <li>3. 手工调用本类的静态方法生成数据库访问的接口代码</li>
 * <br>
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBGeneratorMain {
	static Log logger=LogFactory.getLog(DBGeneratorMain.class.getName());
	
	public static void main(String[] args) throws Exception{
		if(args.length<2){			
			String usage="Usage: \r\n"
					+"com.tsc9526.monalisa.core.generator.DBGeneratorMain <class_with_db_annotation> <output_java_path> [out_resource_path = output_java_path]";
			System.out.println(usage);
		}else{
			generateModelClass(Class.forName(args[0]),args[1],args.length>2?args[2]:args[1]);	
			
			generateSqlQueryClass(args[1],args.length>2?args[2]:args[1]);
		}
	}
	
	/**
	 * 生成数据库基本表的查询类<br>
	 * 输出代码目录优先级： "./src/main/java" -> "./src"
	 * 
	 * @param clazzWithDBAnnotation 指定的数据库连接信息类
	 */
	public static void generateModelClass(Class<?> clazzWithDBAnnotation){		 
		if(new File("./src/main/java").exists()){
			generateModelClass(clazzWithDBAnnotation,"./src/main/java","./src/main/resources");
		}else{ 
			File src=new File("./src");
			if(!src.exists()){
				src.mkdir();
			} 
			
			generateModelClass(clazzWithDBAnnotation,"./src","./src");
		}
	}
	
	/**
	 * 生成SQL文件定义的查询类（SQL目录的默认读取位置为： "./sql"， 包含子目录）<br>
	 * 输出代码目录的优先级： "./src/main/java" -> "./src"
	 */
	public static void generateSqlQueryClass(){
		if(new File("./src/main/java").exists()){
			generateSqlQueryClass("./src/main/java","./src/main/resources");
		}else{ 
			File src=new File("./src");
			if(!src.exists()){
				src.mkdir();
			} 
			
			generateSqlQueryClass("./src","./src");
		}
	}
	
	public static void generateModelClass(Class<?> clazzWithDBAnnotation,String outputJavaDir){
		generateModelClass(clazzWithDBAnnotation,outputJavaDir,outputJavaDir);
	}

	public static void generateModelClass(Class<?> clazzWithDBAnnotation,String outputJavaDir,String outputResourceDir){
		DBGeneratorLocal dbGen=new DBGeneratorLocal(clazzWithDBAnnotation,outputJavaDir,outputResourceDir);
		dbGen.generateFiles();
	}
	
	
	public static void generateSqlQueryClass(String outputJavaDir){
		generateSqlQueryClass(outputJavaDir, outputJavaDir);
	}
	
	public static void generateSqlQueryClass(String outputJavaDir,String outputResourceDir){
		SQLGenerator sqlGen=new SQLGenerator(outputJavaDir, outputResourceDir);
		sqlGen.generateFiles();
	}
  
}
