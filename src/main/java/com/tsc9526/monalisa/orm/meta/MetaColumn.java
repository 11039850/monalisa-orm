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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;
import com.tsc9526.monalisa.tools.clazz.MelpJavaBeans;
import com.tsc9526.monalisa.tools.datatable.CaseInsensitiveMap;
import com.tsc9526.monalisa.tools.io.MelpFile;
import com.tsc9526.monalisa.tools.string.MelpTypes;
import com.tsc9526.monalisa.tools.validator.Max;
import com.tsc9526.monalisa.tools.validator.Min;
import com.tsc9526.monalisa.tools.validator.Regex;

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
	
	
	private String seq;
	
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
		String get=MelpJavaBeans.getGetterMethodName(getJavaName(), getJavaType());
		return get;
	}
	
	public String getJavaNameSet(){
		String set=MelpJavaBeans.getSetterMethodName(getJavaName());
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
			return MelpTypes.getJavaType(jdbcType);
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
					String jtype=MelpJavaBeans.getJavaName(getName(),true);
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
	
	protected void processRemarkFile() {
		String file=getCode("file");
		if(file!=null){
			imports.add(File.class.getName());
			imports.add(MelpFile.class.getName());
		}
	}
	
	protected void processRemarks(){
		processRemarkBoolean();
		processRemarkEnum();
		processRemarkAnnotation();
		processRemarkArray();
		processRemarkList();
		processRemarkJson();
		processRemarkFile();
	}
	 
	
	public Name setRemarks(String remarks) {
		if(remarks!=null){
			//#annotation{...}
			this.code=parseRemarks(remarks);
			
			processRemarks();			 
		}
		return super.setRemarks(remarks);
	}
	

	protected Map<String, String> parseRemarks(String remark) {
		CaseInsensitiveMap<String> map = new CaseInsensitiveMap<String>();

		int len = remark.length();
		for (int i = 0; i < len; i++) {
			char c = remark.charAt(i);
			if (c == '#') {
				StringBuffer n = new StringBuffer();
				StringBuffer v = new StringBuffer();

				while (++i < len) {
					c = remark.charAt(i);

					if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
						n.append(c);
					} else if (c == ' ' || c == '\r' || c == '\n' || c == '\t' || c == '{') {
						if (c == '{') {
							while (++i < len) {
								c = remark.charAt(i);
								if (c == '}') {
									break;
								} else {
									v.append(c);
									if (c == '{') {
										while (++i < len) {
											c = remark.charAt(i);
											v.append(c);
											if (c == '}') {
												break;
											}
										}
									}
								}
							}
							break;
						} else {
							n.append(" ");
						}
					} else {
						n.delete(0, n.length());
						i--;
						break;
					}
				}

				String name = n.toString().trim();
				if (name.length() > 0) {
					map.put(name.toLowerCase(), v.toString().trim());
				}
			}
		}

		return map;

	}
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("NAME: ").append(name)
		  .append(", KEY: ").append(key)
		  .append(", TYPE: ").append(MelpTypes.getTypeName(jdbcType))
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

	public void setSeq(String seq) {
		this.seq=seq;
	}
	
	public String getSeq() {
		return seq;
	}
 
}
