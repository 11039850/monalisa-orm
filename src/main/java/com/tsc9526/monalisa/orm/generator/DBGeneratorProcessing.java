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
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import com.tsc9526.monalisa.orm.Version;
import com.tsc9526.monalisa.orm.annotation.DB;
import com.tsc9526.monalisa.orm.datasource.ConfigClass;
import com.tsc9526.monalisa.orm.datasource.DataSourceManager;
import com.tsc9526.monalisa.orm.datasource.DbProp;
import com.tsc9526.monalisa.orm.meta.MetaTable;
import com.tsc9526.monalisa.orm.model.Model;
import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.io.JavaWriter;
import com.tsc9526.monalisa.tools.misc.MelpEclipse;

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
		
		this.dbi=typeElement.getQualifiedName().toString();
		
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
		if(MelpEclipse.inEclipseIDE()){
			if(processingEnv instanceof org.eclipse.jdt.internal.apt.pluggable.core.dispatch.IdeBuildProcessingEnvImpl) {			
				org.eclipse.jdt.core.IJavaProject project = ((org.eclipse.jdt.internal.apt.pluggable.core.dispatch.IdeBuildProcessingEnvImpl) processingEnv).getJavaProject();
				projectPath=project.getProject().getLocation().toString();
				 
				inEclipseIDE=true;
				
				DbProp.SET_CFG_ROOT_PATH(projectPath);
				
				plogger.info("Generate("+Version.getVersion()+") files from eclipse project: "+projectPath+" ...");
				plogger.info("Database "+dbKey+", "+ (db.configFile().length()>0?("Config-file: "+db.configFile()):(db.url()) ));		
			}
		}
		
		if(!inEclipseIDE){
			plogger.info("Generate("+Version.getVersion()+") files from dbkey: "+dbKey+"("+ (db.configFile().length()>0?db.configFile():db.url() )+"): "+new File(DbProp.CFG_ROOT_PATH).getAbsolutePath()+" ...");
		}
		
		System.setProperty("DB@"+dbKey,projectPath);				 
		
		
		
		String name=typeElement.getQualifiedName().toString();
		String pkg=name.toLowerCase();
		int p=name.lastIndexOf(".");
		if(p>0){
			pkg=name.substring(0,p)+name.substring(p).toLowerCase();
		}		
		
		initDbcfg(dbKey,db);
		
		this.javaPackage=pkg;		
		this.resourcePackage="resources."+pkg;
		this.dbmetadata=new DBMetadata(projectPath,javaPackage,dbcfg);		
	}	 
	
	protected void initDbcfg(String dbKey,DB db){
		DataSourceManager dsm=DataSourceManager.getInstance();
		this.dbcfg=dsm.getDBConfig(dbKey, db, null);
		 
		String cff=db.configFile();
		if(cff!=null && cff.startsWith("classpath:")){
			String resource=cff.substring("classpath:".length());
			if(resource.startsWith("/")){
				resource=resource.substring(1);
			}
			
			String pkg="";
			String relativeName=resource;
			int p=resource.lastIndexOf("/");
			if(p>0){
				pkg=resource.substring(0,p).replace('/','.');
				relativeName=resource.substring(p+1);
			}
			
			
			try{
				File f=tryGetResourceFile(pkg,relativeName);
			 	 
				String basepath=f.getAbsolutePath().replace('\\','/');
				basepath=basepath.substring(0,basepath.length()-resource.length());
				dbcfg.setCfgBasePath(basepath);
				dsm.getDBConfig(dbKey, db, true);
			}catch(IOException e){
				throw new RuntimeException("Failed to read classpath resource: "+resource,e);
			}
		}
	}
	
	protected File tryGetResourceFile(String pkg,String relativeName)throws IOException{
		Filer filer=processingEnv.getFiler();
		
		Location[] ls=new Location[]{StandardLocation.CLASS_OUTPUT,StandardLocation.SOURCE_PATH};
		for(Location l:ls){
			FileObject fo=filer.getResource(l,pkg,relativeName);
			File f=new File(fo.toUri());
			if(f.exists()){
				return f;
			} 
		}
		
		return null;
	}
	  
	protected OutputStream getResourceOutputStream(String pkg,String filename){		
		try{			 					
			FileObject res = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, pkg, filename, typeElement);
			OutputStream out=res.openOutputStream();
			
			return out;
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
				clazz=(Class<? extends ConfigClass>)MelpClass.forName(className);
				plogger.info("Loaded db config from class: "+className);
			}catch(ClassNotFoundException e){
				plogger.info("Class not found, try load class  from project path: "+className);
				
				return loadClassFromProject(className);
			}
		}
		
		return clazz;
	}
	 
	
	private static Class<? extends ConfigClass> loadClassFromProject(String className){
		throw new RuntimeException("Class not found: "+className);
	}
}
