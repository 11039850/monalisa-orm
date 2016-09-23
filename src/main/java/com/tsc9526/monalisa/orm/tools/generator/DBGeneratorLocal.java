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
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Writer;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.meta.MetaTable;
import com.tsc9526.monalisa.orm.tools.helper.JavaWriter;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBGeneratorLocal extends DBGenerator{
	static Logger logger=Logger.getLogger(DBGeneratorLocal.class);
 	
	private String outputJavaDir;
	private String outputResourceDir;
	
	public DBGeneratorLocal(Class<?> clazzWithDBAnnotation){
		this(clazzWithDBAnnotation, "./src/main/java","./src/main/resources");
	}
	
	public DBGeneratorLocal(Class<?> clazzWithDBAnnotation,String outputJavaDir,String outputResourceDir) {
		super();
	  	
		if(!clazzWithDBAnnotation.isInterface()){
			throw new RuntimeException("Class: "+clazzWithDBAnnotation.getName()+" must be interface!");
		}
		
		logger.info("Generate files, DB-CLASS: "+clazzWithDBAnnotation.getName()+", SOURCE-DIR: "+outputJavaDir+", RESOURCE-DIR: "+outputResourceDir);
		
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
	  
 
	protected OutputStream getResourceOutputStream(String pkg,String filename){		
		try{			 			
			String resdir=outputResourceDir+"/"+pkg.replace('.','/');
			File dirRoot=new File(resdir);
			if(!dirRoot.exists()){
				dirRoot.mkdirs();
			}
		 
			FileOutputStream out=new FileOutputStream(new File(dirRoot,filename));
			 
			return out;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	protected Writer getJavaWriter(MetaTable table){		
		try{
			String javadir=outputJavaDir+"/"+javaPackage.replace('.','/');
			File dir=new File(javadir);
			if(!dir.exists()){
				dir.mkdirs();
			}
			
			String className=table.getJavaName();
			FileOutputStream out=new FileOutputStream(new File(dir,className+".java"));
			JavaWriter writer=new JavaWriter(out);
			return writer;
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
  
}
