package com.tsc9526.monalisa.core.generator;

import java.lang.reflect.Constructor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import com.tsc9526.monalisa.core.datasource.DBConfig;

public class DBProject {
	
	@SuppressWarnings("unchecked")
	public static DBProject getProject(ProcessingEnvironment processingEnv,TypeElement typeElement) {
		try{
			Class<DBProject> clazz=(Class<DBProject>)Class.forName("com.tsc9526.monalisa.plugin.eclipse.generator.EclipseDBProject");
			Constructor<DBProject> c=clazz.getConstructor(ProcessingEnvironment.class,TypeElement.class);
			DBProject project= c.newInstance();
			
			System.out.println("Building project within eclipse environment ...");			
			return project;
		}catch(Exception e){		
			System.out.println("Building project without eclipse environment ...");
			
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
