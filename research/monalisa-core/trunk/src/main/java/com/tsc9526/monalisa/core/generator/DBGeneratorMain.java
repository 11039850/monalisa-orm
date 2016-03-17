package com.tsc9526.monalisa.core.generator;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsc9526.monalisa.core.parser.executor.SQLGenerator;

/**
 * 
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
			generate(Class.forName(args[0]),args[1],args.length>2?args[2]:args[1]);			 
		}
	}
	
	public static void generate(Class<?> clazzWithDBAnnotation){		 
		if(new File("./src/main/java").exists()){
			generate(clazzWithDBAnnotation,"./src/main/java","./src/main/resources");
		}else{ 
			File src=new File("./src");
			if(!src.exists()){
				src.mkdir();
			} 
			
			generate(clazzWithDBAnnotation,"./src","./src");
		}
	}
	
	public static void generate(Class<?> clazzWithDBAnnotation,String outputJavaDir){
		generate(clazzWithDBAnnotation,outputJavaDir,outputJavaDir);
	}

	public static void generate(Class<?> clazzWithDBAnnotation,String outputJavaDir,String outputResourceDir){
		if(clazzWithDBAnnotation!=null){
			DBGeneratorLocal dbGen=new DBGeneratorLocal(clazzWithDBAnnotation,outputJavaDir,outputResourceDir);
			dbGen.generateFiles();
		}else{
			logger.info("Ignore generate class with annotation, because the parameter: clazzWithDBAnnotation is null!");
		}
		
		SQLGenerator sqlGen=new SQLGenerator(outputJavaDir, outputResourceDir);
		sqlGen.generateFiles();
	}
  
}
