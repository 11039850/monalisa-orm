package com.tsc9526.monalisa.core.meta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tsc9526.monalisa.core.query.validator.Max;
import com.tsc9526.monalisa.core.query.validator.Min;
import com.tsc9526.monalisa.core.query.validator.Regex;
import com.tsc9526.monalisa.core.tools.Helper;
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

	protected void processRemarks(){
		processRemarkBoolean();
		processRemarkEnum();
		processRemarkAnnotation();
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
 
}
