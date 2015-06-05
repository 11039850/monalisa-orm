package com.tsc9526.monalisa.core.meta;

import com.tsc9526.monalisa.core.tools.JavaBeansHelper;
import com.tsc9526.monalisa.core.tools.TypeHelper;


public class MetaColumn extends Name{
	private static final long serialVersionUID = 5069827028195702146L;
	
	private MetaTable table;
 
	private int  jdbcType;
	
	private String javaType;
 	 
	private boolean notnull;

	private int length; 	
  
	private String value;
	
	/**
	 * 是否主键
	 */
	private boolean key;

	/**
	 * 是否自动增长
	 */
	private boolean auto;
	
	
	/**
	 * Set方法
	 */
	protected String javaNameSet;
	
	/**
	 * Get方法
	 */
	protected String javaNameGet;
	
	
	public MetaColumn(){
		super(false);
	}	

	public String getJavaNameGet(){
		String get=JavaBeansHelper.getGetterMethodName(getJavaName(), getJavaType());
		return get;
	}
	
	public String getJavaNameSet(){
		String set=JavaBeansHelper.getSetterMethodName(getJavaName());
		return set;
	}
	
	public MetaTable getTable() {
		return table;
	}

	public void setTable(MetaTable table) {
		this.table = table;
	}
 

	public int getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(int jdbcType) {
		this.jdbcType = jdbcType;
	}

 
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
  

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	 
	public String getJavaType(){
		if(javaType==null){
			return TypeHelper.getJavaType(jdbcType);
		}else{
			return javaType;
		}
	}
	
	public void setJavaType(String javaType){
		this.javaType=javaType;
	}
	 
	public boolean isNotnull() {
		return notnull;
	}

	public void setNotnull(boolean notnull) {
		this.notnull = notnull;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("NAME: ").append(name)
		  .append(", KEY: ").append(key)
		  .append(", TYPE: ").append(TypeHelper.getTypeName(jdbcType))
		  .append(", LENGTH: ").append(length)
		  .append(", NOTNULL: ").append(notnull)		  
		  .append(", AUTO: ").append(auto)
		  .append(", DEFAULT: ").append(value)
		  .append(", REMARKS: ").append(remarks);
		return sb.toString();
	}
 
}
