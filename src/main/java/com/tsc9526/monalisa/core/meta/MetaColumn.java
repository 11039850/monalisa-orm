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
package com.tsc9526.monalisa.core.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;
import com.tsc9526.monalisa.core.query.validator.Max;
import com.tsc9526.monalisa.core.query.validator.Min;
import com.tsc9526.monalisa.core.query.validator.Regex;
import com.tsc9526.monalisa.core.tools.Helper;
import com.tsc9526.monalisa.core.tools.JavaBeansHelper;
import com.tsc9526.monalisa.core.tools.TypeHelper;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
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
	 
 	
	protected Map<String, String> code=new HashMap<String, String>();
	
	protected Set<String> imports=new HashSet<String>();
	
	
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
	
	/**
	 * @return 是否主键
	 */
	public boolean isKey() {
		return key;
	}

	/**
	 * @param key 是否主键
	 */
	public void setKey(boolean key) {
		this.key = key;
	}

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}
	
	static Map<String, String> annotations=new HashMap<String, String>(){		 
		private static final long serialVersionUID = 5658760303891954974L;
		{
			put("@Regex",Regex.class.getName());
			put("@Max"  ,Max.class.getName());
			put("@Min"  ,Min.class.getName());
		}
	};
	protected void processRemarkAnnotation(){
		String as=getCode("annotation");
		if(as!=null){
			StringBuffer sb=new StringBuffer();
			for(String a:as.split("\n")){
				String x=a.trim();
				if(x.startsWith("@")){
					String px="";						
					int p=x.indexOf("(");
					if(p>0){
						x=x.substring(0,p).trim();
						px=x.substring(p);
					}
					
					p=x.lastIndexOf(".");
					if(p>0){
						imports.add(x);
						x=x.substring(p+1);
					}else{
						String i=annotations.get(x);
						if(i!=null){
							imports.add(i);
						}
					}
					
					sb.append(x+px);
				}else{
					sb.append(x).append("\r\n");
				}
			}
		}
	}
	
	protected void processRemarkBoolean() {
		if(getCode("bool")!=null || getCode("boolean")!=null){
			setJavaType("Boolean");
		}		
	}
	
	protected void processRemarkEnum() {
		String enumClass=getCode("enum");
		if(enumClass!=null){
			int x=enumClass.indexOf("{");
			if(x>=0){
				if(x==0){
					String jtype=JavaBeansHelper.getJavaName(getName(),true);
					setJavaType(jtype);
					code.put("enum",jtype+enumClass);
				}else{
					setJavaType(enumClass.substring(0,x).trim());
				}
			}else{
				int p=enumClass.lastIndexOf(".");
				if(p>0){
					imports.add(enumClass);
					setJavaType(enumClass.substring(p+1));
				}else{			
					setJavaType(enumClass);
				}
			}
		}		
	}

	protected void processRemarkArray() {
		String array=getCode("array");
		if(array!=null){
			array=array.trim().toLowerCase();
			if(array.equals("int") || array.equals("integer")){
				setJavaType("int[]");
			}else if(array.equals("long") || array.equals("number")){
				setJavaType("long[]");
			}else if(array.equals("float") || array.equals("Float")){
				setJavaType("float[]");
			}else if(array.equals("double") || array.equals("Double")){
				setJavaType("double[]");
			}else{
				setJavaType("String[]");
			}
		}		
	}
	
	protected void processRemarkList() {
		String list=getCode("list");
		if(list!=null){
			imports.add(List.class.getName());
			imports.add(ArrayList.class.getName());
			
			list=list.trim().toLowerCase();
			if(list.equals("int") || list.equals("integer")){
				setJavaType("List<Integer>");
			}else if(list.equals("long") || list.equals("number")){
				setJavaType("List<Long>");
			}else if(list.equals("float")){
				setJavaType("List<Float>");
			}else if(list.equals("double")){
				setJavaType("List<Double>");
			}else{
				setJavaType("List<String>");
			}
		}		
	}
	
	protected void processRemarkJson() {
		String json=getCode("json");
		if(json!=null){
			if(json.trim().length()>0){
				int p=json.lastIndexOf(".");
				if(p>0){
					setJavaType(json.substring(p+1));					
					imports.add(json);
				}else{				
					setJavaType(json);
				}
			}else{
				setJavaType("JsonObject");
			
				imports.add(JsonObject.class.getName());
			}
		}		
	}
	
	protected void processRemarks(){
		processRemarkBoolean();
		processRemarkEnum();
		processRemarkAnnotation();
		processRemarkArray();
		processRemarkList();
		processRemarkJson();
	}
	 
	
	public Name setRemarks(String remarks) {
		if(remarks!=null){
			//#annotation{...}
			this.code=Helper.parseRemarks(remarks);
			
			processRemarks();			 
		}
		return super.setRemarks(remarks);
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

	public Set<String> getImports(){
		return imports;
	}
	
	public String getCode(String name){
		return code.get(name);
	}
	
	public Map<String, String> getCode() {
		return code;
	}

	public void setCode(Map<String, String> code) {
		this.code = code;
	}
	
	public boolean isEnum(){
		String enumClass=getCode("enum");
		if(enumClass!=null){
			return true;
		}
		
		return false;
	}
 
}
