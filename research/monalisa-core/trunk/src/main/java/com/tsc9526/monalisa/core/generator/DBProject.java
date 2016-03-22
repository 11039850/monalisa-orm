package com.tsc9526.monalisa.core.generator;

import java.io.File;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.apt.pluggable.core.dispatch.IdeBuildProcessingEnvImpl;

import com.tsc9526.monalisa.core.annotation.DB;
import com.tsc9526.monalisa.core.datasource.DBConfig;
import com.tsc9526.monalisa.core.logger.Logger;
import com.tsc9526.monalisa.core.tools.Helper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBProject {
	static Logger logger=Logger.getLogger(DBProject.class);
	 
	public static DBProject getProject(ProcessingEnvironment processingEnv,TypeElement typeElement) {
		 return new DBProject(processingEnv,typeElement);
	}
	
	protected ProcessingEnvironment processingEnv;
	protected TypeElement typeElement;
	
	protected String dbKey;
	protected String projectPath;
	
	private DBProject(ProcessingEnvironment processingEnv,TypeElement typeElement){
		this.processingEnv=processingEnv;
		this.typeElement=typeElement;
		
		DB db=typeElement.getAnnotation(DB.class);		 
		dbKey=db.key();
		if(dbKey==null || dbKey.length()<1){
			dbKey=typeElement.toString();
		}
		
		if(Helper.inEclipseIDE() && processingEnv instanceof IdeBuildProcessingEnvImpl) {			
			IJavaProject project = ((IdeBuildProcessingEnvImpl) processingEnv).getJavaProject();
			projectPath=project.getProject().getLocation().toString();
				 
			logger.info("Building "+dbKey+": "+projectPath+" within eclipse environment ...");			
		}else{
			projectPath=DBConfig.DEFAULT_PATH;
			logger.info("Building "+dbKey+": "+new File(DBConfig.DEFAULT_PATH).getAbsolutePath()+" without eclipse environment ...");
		}
	}
	
	public String getDbKey(){
		return this.dbKey;
	}
	
	public  String getProjectPath(){		
		return projectPath;
	}
	 
}
