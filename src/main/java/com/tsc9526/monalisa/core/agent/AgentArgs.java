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

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class AgentArgs {
	private String[] classNames;
	private String classFilePathRoot;
	
	public AgentArgs(){
	}
	
	public AgentArgs(String classFilePathRoot,String[] classNames){
		this.classFilePathRoot=classFilePathRoot;
		this.classNames=classNames;
		
	}

	public String[] getClassNames() {
		return classNames;
	}

	public void setClassNames(String[] classNames) {
		this.classNames = classNames;
	}

	public String getClassFilePathRoot() {
		return classFilePathRoot;
	}

	public void setClassFilePathRoot(String classFilePathRoot) {
		this.classFilePathRoot = classFilePathRoot;
	}
	 
}
