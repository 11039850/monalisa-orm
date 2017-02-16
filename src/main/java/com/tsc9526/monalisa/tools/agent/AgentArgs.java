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
package com.tsc9526.monalisa.tools.agent;

import java.util.concurrent.atomic.AtomicLong;

import com.tsc9526.monalisa.tools.io.MelpFile;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class AgentArgs {
	private static AtomicLong serial=new AtomicLong(1);
	
	public final static String FLAG_LOADING="***LOADING***";
	
	private long id=serial.getAndIncrement();
	
	private AgentArgClassInfo[] classes;
	private String sourceFilePathRoot;
	private String classFilePathRoot;
	
	private String reloadFlag=FLAG_LOADING;
	
	public AgentArgs(){
	}
	
	public AgentArgs(String sourceFilePathRoot,String classFilePathRoot,AgentArgClassInfo[] classes){
		this.sourceFilePathRoot=sourceFilePathRoot;
		this.classFilePathRoot=classFilePathRoot;
		this.classes=classes;	
	}
  
	public String getClassFilePathRoot() {
		return classFilePathRoot;
	}

	public void setClassFilePathRoot(String classFilePathRoot) {
		this.classFilePathRoot = classFilePathRoot;
	}
	 
	public AgentArgClassInfo[] getClasses() {
		return classes;
	}

	public void setClasses(AgentArgClassInfo[] classes) {
		this.classes = classes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSourceFilePathRoot() {
		return sourceFilePathRoot;
	}

	public void setSourceFilePathRoot(String sourceFilePathRoot) {
		this.sourceFilePathRoot = sourceFilePathRoot;
	}
	 
	public static class AgentArgClassInfo{
		String javaFilePath;
		String classFilePath;
		
		String className;
		long   version;
		long   lastModified;
		
		public AgentArgClassInfo(AgentJavaFile ajf){
			this.javaFilePath =MelpFile.combinePath(ajf.getJavaFile().getAbsolutePath()); 
			this.classFilePath=MelpFile.combinePath(ajf.getClassFile().getAbsolutePath());
			
			this.className    =ajf.getClassName();
			this.version      =ajf.getVersion();
			this.lastModified =ajf.getLastModified();
		}
	}

	public String getReloadFlag() {
		return reloadFlag;
	}

	public void setReloadFlag(String reloadFlag) {
		this.reloadFlag = reloadFlag;
	}
}
