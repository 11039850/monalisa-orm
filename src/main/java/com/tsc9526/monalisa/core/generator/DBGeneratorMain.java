package com.tsc9526.monalisa.core.generator;

import java.io.File;

import com.tsc9526.monalisa.core.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBGeneratorMain {
	static Logger logger=Logger.getLogger(DBGeneratorMain.class);
	
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
		DBGeneratorLocal g=new DBGeneratorLocal(clazzWithDBAnnotation,outputJavaDir,outputResourceDir);
		g.generateFiles();
	}
  
}
