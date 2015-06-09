package ${table.javaPackage};
<#include "functions.ftl">
<#list imports as i>
import ${i};
</#list>
/**
 * Created by monalisa at ${.now}
 * 
 * @see ${see}
 */
public class ${table.javaName} implements java.io.Serializable{
	private static final long serialVersionUID = ${table.serialID?c}L;	
	final static String  FINGERPRINT = "${fingerprint}";
	
	 
	<#list table.columns as f>
	<@comments table=table c=f align="	"/> 
	private ${f.javaType} ${f.javaName};	
	
	</#list>
	
	
	<#list table.columns as f>   
	<@comments table=table c=f align="	" /> 
	public ${table.javaName} ${f.javaNameSet}(${f.javaType} ${f.javaName}){
		this.${f.javaName} = ${f.javaName};
		return this;
	}
		
	</#list>
	
	
	<#list table.columns as f>   
	<@comments table=table c=f align="	" /> 
	public ${f.javaType} ${f.javaNameGet}(){
		return this.${f.javaName};		
	}
		
	</#list>	
}