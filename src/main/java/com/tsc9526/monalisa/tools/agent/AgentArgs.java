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

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class AgentArgs {
	private static AtomicLong serial=new AtomicLong(1);
	
	private long id=serial.getAndIncrement();
	
	private AgentArgClassInfo[] classes;
	private String classFilePathRoot;
	
	public AgentArgs(){
	}
	
	public AgentArgs(String classFilePathRoot,AgentArgClassInfo[] classes){
		this.classFilePathRoot=classFilePathRoot;
		this.classes=classes;	
	}
  
	public String getClassFilePathRoot() {
		return classFilePathRoot;
	}

	public void setClassFilePathRoot(String classFilePathRoot) {
		this.classFilePathRoot = classFilePathRoot;
	}
	
	public static class AgentArgClassInfo{
		String className;
		long   version;
		long   lastModified;
		
		public AgentArgClassInfo(String className,long version,long lastModified){
			this.className=className;
			this.version=version;
			this.lastModified=lastModified;
		}
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
	 
}
