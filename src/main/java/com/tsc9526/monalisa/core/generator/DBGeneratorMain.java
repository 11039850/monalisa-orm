package com.tsc9526.monalisa.core.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.meta.MetaTable;
import com.tsc9526.monalisa.core.tools.JavaWriter;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBGeneratorMain extends DBGenerator{
	
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
		DBGeneratorMain g=new DBGeneratorMain(clazzWithDBAnnotation,outputJavaDir,outputResourceDir);
		g.generateFiles();
	}
	
	private String outputJavaDir;
	private String outputResourceDir;
	
	public DBGeneratorMain(Class<?> clazzWithDBAnnotation){
		this(clazzWithDBAnnotation, "./src/main/java","./src/main/resources");
	}
	
	public DBGeneratorMain(Class<?> clazzWithDBAnnotation,String outputJavaDir,String outputResourceDir) {
		super();
	  	
		System.out.println("Generate files, DB-CLASS: "+clazzWithDBAnnotation.getName()+", SOURCE-DIR: "+outputJavaDir+", RESOURCE-DIR: "+outputResourceDir);
		
		String projectPath=".";
		 		
		String name=clazzWithDBAnnotation.getName();
		String pkg=name.toLowerCase();
		int p=name.lastIndexOf(".");
		if(p>0){
			pkg=name.substring(0,p)+name.substring(p).toLowerCase();
		}	
		
		this.outputJavaDir=outputJavaDir;
		this.outputResourceDir=outputResourceDir;
		
		this.dbcfg=DBConfig.fromClass(clazzWithDBAnnotation);		
		this.javaPackage=pkg;		
		this.resourcePackage="resources."+pkg;
		this.dbi=clazzWithDBAnnotation.getName();
		this.dbmetadata=new DBMetadata(projectPath,javaPackage,dbcfg);
		
	}	 
	  
	
	protected void generateResources(List<MetaTable> tables){		
		try{			 			
			String resdir=outputResourceDir+"/"+resourcePackage.replace('.','/');
			File dir=new File(resdir);
			if(!dir.exists()){
				dir.mkdirs();
			}
		 
			FileOutputStream out=new FileOutputStream(new File(dir,"create_table.sql"));
			 
			Writer w = new OutputStreamWriter(out,"UTF-8");
			for(MetaTable table:tables){
				if(table.getCreateTable()!=null){
					w.write("/***CREATE TABLE: "+table.getNamePrefix()+" :: "+table.getName()+"***/\r\n");
					w.write(table.getCreateTable().getCreateSQL()); 
					w.write("\r\n\r\n\r\n");
				}
			}
			w.close();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	protected void generateJavaFile(MetaTable table){		
		try{			 
			MetaTable clone=table.clone();
			clone.setJavaName(null).setName(clone.getNamePrefix());
			 
			String javadir=outputJavaDir+"/"+javaPackage.replace('.','/');
			File dir=new File(javadir);
			if(!dir.exists()){
				dir.mkdirs();
			}
			
			String className=clone.getJavaName();	
			String modelClass=getModelClassValue(clone);
			
			FileOutputStream out=new FileOutputStream(new File(dir,className+".java"));
			JavaWriter writer=new JavaWriter(out);
			DBTableGenerator g2=new DBTableGenerator(clone, modelClass, dbi);
			g2.generate(writer);
			
			verifyPartition(table);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
  
}
