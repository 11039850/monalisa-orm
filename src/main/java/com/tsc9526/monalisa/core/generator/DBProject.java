package com.tsc9526.monalisa.core.generator;

import java.io.File;
import java.lang.reflect.Constructor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.datasource.DBConfig;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBProject {
	public final static String EclipseDBProjectClass="com.tsc9526.monalisa.plugin.eclipse.generator.EclipseDBProject";
	
	static Log logger=LogFactory.getLog(DBProject.class);
	
	@SuppressWarnings("unchecked")
	public static DBProject getProject(ProcessingEnvironment processingEnv,TypeElement typeElement) {
		DB db=typeElement.getAnnotation(DB.class);		 
		String dbKey=db.key();
		if(dbKey==null || dbKey.length()<1){
			dbKey=typeElement.toString();
		}
		
		try{
			Class<DBProject> clazz=(Class<DBProject>)Class.forName(EclipseDBProjectClass);
			Constructor<DBProject> c=clazz.getConstructor(ProcessingEnvironment.class,TypeElement.class);
			DBProject project= c.newInstance(processingEnv,typeElement);
			
			logger.info("Building "+dbKey+": "+project.getProjectPath()+" within eclipse environment ...");			
			return project;
		}catch(Exception e){		
			logger.info("Building "+dbKey+": "+new File(".").getAbsolutePath()+" without eclipse environment ...");
			
			return new DBProject(processingEnv,typeElement);		
		}
	}
	
	
	protected ProcessingEnvironment processingEnv;
	protected TypeElement typeElement;
	public DBProject(ProcessingEnvironment processingEnv,TypeElement typeElement){
		this.processingEnv=processingEnv;
		this.typeElement=typeElement;
	}
	
	
	public  String getProjectPath(){		
		return DBConfig.DEFAULT_PATH;
	}
	 
}
