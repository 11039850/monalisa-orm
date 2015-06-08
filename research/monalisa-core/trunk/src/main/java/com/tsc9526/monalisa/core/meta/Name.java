package com.tsc9526.monalisa.core.meta;

import java.io.Serializable;

import com.tsc9526.monalisa.core.tools.JavaBeansHelper;

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
