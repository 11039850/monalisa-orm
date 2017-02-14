/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.orm.generator;

import java.io.File;

import com.tsc9526.monalisa.orm.mqs.SQLGenerator;
import com.tsc9526.monalisa.tools.logger.Logger;

/**
 * 数据库访问接口代码生成三种方式：<br>
 * 1. 使用Maven编译插件： <br> 
 * 	 &nbsp;&nbsp;&nbsp;&nbsp;&lt;annotationProcessor&gt;<br>
 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;com.tsc9526.monalisa.orm.processor.DBAnnotationProcessor<br>
 *   &nbsp;&nbsp;&nbsp;&nbsp;&lt;/annotationProcessor&gt; <br>
 * 2. 使用Eclipse插件<br> 
 * 3. 手工调用本类提供的静态方法
 * <br>
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBGeneratorMain {
	static Logger logger=Logger.getLogger(DBGeneratorMain.class.getName());
	
	public static void main(String[] args) throws Exception{
		if(args.length<2){			
			String usage="Usage: \r\n"
					+DBGeneratorMain.class.getName()+" <class_with_db_annotation> <output_java_path> [out_resource_path = output_java_path]";
			System.out.println(usage);
		}else{
			generateModelClass(Class.forName(args[0]),args[1],args.length>2?args[2]:args[1]);	
			
			generateSqlQueryClass(args[1],args.length>2?args[2]:args[1]);
		}
	}
	
	/**
	 * 生成数据库基本表的查询类<br>
	 * 输出代码目录优先级： "./src/main/java" &gt; "./src"
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
	 * 输出代码目录的优先级： "./src/main/java" &gt; "./src"
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
