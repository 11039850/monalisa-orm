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
package com.tsc9526.monalisa.core.agent;

import java.io.File;

import com.tsc9526.monalisa.core.tools.ClassHelper;
import com.tsc9526.monalisa.core.tools.ClassPathHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class AgentJavaFile{
	private String className;
	private File javaFile;
	private File classFile;
	private long version;
	private long lastLoadedTime;
	
	public AgentJavaFile(String className,long version,long lastLoadedTime,File javaFile,File classFile){
		this.className=className;
		this.version=version;
		this.javaFile=javaFile;
		this.classFile=classFile;
		
		this.lastLoadedTime=lastLoadedTime;
	}
	
	public boolean isCompileRequired(){
		return !classFile.exists() || classFile.lastModified() < javaFile.lastModified();
	}
	
	public boolean isReloadRequired(){
		long classVersion=ClassHelper.getVersion(className);
		
		if(version>0 ){
			return version>classVersion;
		}else{
			long tm=AgentClass.getAgentClassLoadTime(className);
		 
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

	public long getLastLoadedTime() {
		return lastLoadedTime;
	}

	public void setLastLoadedTime(long lastLoadedTime) {
		this.lastLoadedTime = lastLoadedTime;
	}
}