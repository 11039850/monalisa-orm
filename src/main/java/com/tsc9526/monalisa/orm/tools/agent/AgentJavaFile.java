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
package com.tsc9526.monalisa.orm.tools.agent;

import java.io.File;

import com.tsc9526.monalisa.orm.tools.helper.ClassHelper;
import com.tsc9526.monalisa.orm.tools.helper.ClassPathHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class AgentJavaFile{
	private String className;
	private File javaFile;
	private File classFile;
	private long version;
	private long lastModified;
	
	public AgentJavaFile(String className,long version,long lastModified,File javaFile,File classFile){
		this.className=className;
		this.version=version;
		this.javaFile=javaFile;
		this.classFile=classFile;
		
		this.lastModified=lastModified;
	}
	
	public boolean isCompileRequired(){
		return !classFile.exists() || classFile.lastModified() < javaFile.lastModified();
	}
	
	public boolean isReloadRequired(){
		AgentArgs.AgentArgClassInfo ci=AgentClass.getAgentLoadClassInfo(className);
		long classVersion=ClassHelper.getVersion(className);
		 	
		if(version>0 ){
			if(ci==null){
				return version >classVersion;
			}else{
				return version >ci.version;
			}
		}else if(classVersion>0){
			return false;
		}else{
			long tm= ci==null?0:ci.lastModified;
		 
			if(tm==0){
				File f=ClassPathHelper.getClassOrJarFile(className);
				tm=f.lastModified();
			} 
			
			return tm < javaFile.lastModified();
		}
	}
	  
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public File getJavaFile() {
		return javaFile;
	}

	public void setJavaFile(File javaFile) {
		this.javaFile = javaFile;
	}

	public File getClassFile() {
		return classFile;
	}

	public void setClassFile(File classFile) {
		this.classFile = classFile;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
 
}