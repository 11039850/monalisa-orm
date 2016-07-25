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
package com.tsc9526.monalisa.orm.meta;

import java.io.Serializable;

import com.tsc9526.monalisa.orm.tools.helper.JavaBeansHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Name implements Serializable{	 
	private static final long serialVersionUID = 5069827028195702145L;
 
	/**
	 * 名称
	 */
	protected String name;
	
	/**
	 * Java命名
	 */
	protected String javaName;
	
	
	/**
	 * 备注
	 */
	protected String remarks;

	protected boolean firstCharacterUppercase=false;
	 
	public Name(boolean firstCharacterUppercase){
		this.firstCharacterUppercase=firstCharacterUppercase;
	}
	
	public String getJavaName(){
		if(javaName==null){			 
			javaName=JavaBeansHelper.getJavaName(getName(),firstCharacterUppercase);			 
		}
		return javaName;
	}
   
	public String nameToJava(){
		return JavaBeansHelper.getJavaName(getName(),firstCharacterUppercase);
	}

	public String getName() {
		return name;
	}

	public Name setName(String name) {
		this.name = name;
		return this;
	}
 
	public Name setJavaName(String javaName) {
		this.javaName = javaName;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public Name setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}
}
