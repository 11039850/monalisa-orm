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
package com.tsc9526.monalisa.orm.tools.generator;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.datasource.ConfigClass;
import com.tsc9526.monalisa.orm.datasource.DataSourceManager;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.meta.MetaTable;
import com.tsc9526.monalisa.orm.meta.MetaTable.CreateTable;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.orm.tools.helper.ClassHelper;
import com.tsc9526.monalisa.orm.tools.helper.Helper;
import com.tsc9526.monalisa.orm.tools.helper.JavaWriter;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBGeneratorProcessing extends DBGenerator{	 
	private ProcessingEnvironment processingEnv;	 
	private TypeElement typeElement;
	
	DBGeneratorProcessing(){
	}
	
	public DBGeneratorProcessing(ProcessingEnvironment processingEnv,TypeElement typeElement) {
		super();
		 
		this.processingEnv = processingEnv;		 
		this.typeElement = typeElement;
		
		DB db=typeElement.getAnnotation(DB.class);
		if(db==null){
			throw new RuntimeException("TypeElement without @DB: "+typeElement.toString());
		}		
		String dbKey=db.key();
		if(dbKey==null || dbKey.length()<1){
			dbKey=typeElement.toString();
		}
		
		boolean inEclipseIDE=false;
		String projectPath=DbProp.CFG_ROOT_PATH;
		if(Helper.inEclipseIDE()){
			if(processingEnv instanceof org.eclipse.jdt.internal.apt.pluggable.core.dispatch.IdeBuildProcessingEnvImpl) {			
				org.eclipse.jdt.core.IJavaProject project = ((org.eclipse.jdt.internal.apt.pluggable.core.dispatch.IdeBuildProcessingEnvImpl) processingEnv).getJavaProject();
				projectPath=project.getProject().getLocation().toString();
				 
				inEclipseIDE=true;
				
				DbProp.SET_CFG_ROOT_PATH(projectPath);
				
				plogger.info("Building eclipse project: "+projectPath+" ...");
				plogger.info("Database "+dbKey+", "+ (db.configFile().length()>0?("Config-file: "+db.configFile()):(db.url()) ));		
			}
		}
		
		if(!inEclipseIDE){
			plogger.info("Building "+dbKey+"("+ (db.configFile().length()>0?db.configFile():db.url() )+"): "+new File(DbProp.CFG_ROOT_PATH).getAbsolutePath()+" ...");
		}
		
		System.setProperty("DB@"+dbKey,projectPath);				 
		 
		this.dbcfg=DataSourceManager.getInstance().getDBConfig(dbKey,db,true);
				
		String name=typeElement.getQualifiedName().toString();
		String pkg=name.toLowerCase();
		int p=name.lastIndexOf(".");
		if(p>0){
			pkg=name.substring(0,p)+name.substring(p).toLowerCase();
		}		
		
		this.javaPackage=pkg;		
		this.resourcePackage="resources."+pkg;
		this.dbi=typeElement.getQualifiedName().toString();
		this.dbmetadata=new DBMetadata(projectPath,javaPackage,dbcfg);		
	}	 
	  
	protected Writer getResourceWriter(){		
		try{			 					
			FileObject res = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, resourcePackage, CreateTable.FILE_NAME, typeElement);
			OutputStream out=res.openOutputStream();
			
			Writer w = new OutputStreamWriter(out,"UTF-8");
			return w;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	protected Writer getJavaWriter(MetaTable table){		
		try{			 
			String className=table.getJavaName();
			JavaFileObject java = processingEnv.getFiler().createSourceFile(javaPackage+"."+className, typeElement);
			Writer os = java.openWriter();			
			return new JavaWriter(os);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	
	protected String getModelClassValue(MetaTable table){
		String modelClass=null;
		try{
			modelClass=super.getModelClassValue(table);
			
		}catch(MirroredTypeException mte ){
			TypeMirror typeMirror=mte.getTypeMirror();
			Types TypeUtils = processingEnv.getTypeUtils();
			TypeElement te= (TypeElement)TypeUtils.asElement(typeMirror);
			modelClass=te.getQualifiedName().toString();
		}
		
		if(modelClass==null || modelClass.trim().length()==0){
			modelClass=Model.class.getName(); 
		}
		
		return modelClass;
	}
	
	@SuppressWarnings("unchecked")
	public static Class<? extends ConfigClass> getDBConfigClass(DB db){
		Class<? extends ConfigClass> clazz=null;
		try{
			clazz=db.configClass();
		}catch(MirroredTypeException mte){
			MirroredTypeException x=(MirroredTypeException)mte;
			DeclaredType classTypeMirror = (DeclaredType) x.getTypeMirror();
			TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
			
			String className=classTypeElement.getQualifiedName().toString();
			try{
				clazz=(Class<? extends ConfigClass>)ClassHelper.forClassName(className);
			}catch(ClassNotFoundException e){
				plogger.info("Class not found, try load class: "+className+" from project path.");
				
				return loadClassFromProject(className);
				
			}
		}
		
		return clazz;
	}
	
	private static Class<? extends ConfigClass> loadClassFromProject(String className){
		throw new RuntimeException("Class not found: "+className);
	}
}
