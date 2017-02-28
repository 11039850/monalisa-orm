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
package com.tsc9526.monalisa.tools.xml;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.tsc9526.monalisa.tools.clazz.MelpClass;
import com.tsc9526.monalisa.tools.clazz.MelpClass.ClassHelper;
import com.tsc9526.monalisa.tools.clazz.MelpClass.FGS;
import com.tsc9526.monalisa.tools.string.MelpString;
import com.tsc9526.monalisa.tools.string.MelpTypes;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class XMLObject {
	private boolean withXmlHeader=true;
	private boolean ignoreNullFields=true;
	private boolean pretty = true;

	private String CRLN      = "\r\n";
	private String TAB       = "\t";
	private int   initIndent = 0;
	
	private Object  bean;
	
	public XMLObject(Object bean){
		this.bean=bean;
	}
	
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
 
		if (withXmlHeader) {
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			if (pretty) {
				sb.append(CRLN);
			}
		}
	 	
		String topTag = bean.getClass().getSimpleName();
		add(sb,initIndent,topTag,bean);
		 
		return sb.toString();
	}
	
	private void add(StringBuilder sb,int indent,String name,Object v){
		if(v==null){
			if (!ignoreNullFields) {
				if (pretty) {
					sb.append(padding(indent+1));
				}
				sb.append('<').append(name).append("/>");
			}
			return;
		}
		
		if(isPrimitive(v)){
			String value = (String) MelpClass.convert(v, String.class);
			addString(sb,indent,name,value);
		}else{
			if (pretty) {
				sb.append(padding(indent));
				sb.append('<').append(name).append('>');
				sb.append(CRLN);
			}else{
				sb.append('<').append(name).append('>');
			}
			
			if(isset(v)){
				addSet(sb,indent+1,v);
			}else{
				ClassHelper mc=MelpClass.getClassHelper(v);
				for (FGS fgs : mc.getFields()) {
					String fn = fgs.getFieldName();
					Object fv = fgs.getObject(v);
					add(sb, indent+1, fn, fv); 
				}
			}
			
			if (pretty) {
				sb.append(padding(indent));
				sb.append("</").append(name).append('>');
				if(v!=bean){
					sb.append(CRLN);
				}
			}else{
				sb.append("</").append(name).append('>');
			}
		}
	}
	
	private void addSet(StringBuilder sb,int indent,Object v){
		if(v.getClass().isArray()){
			for(Object x: (Object[])v){
				add(sb, indent, "item",x);
			}
		}else if(v instanceof Map<?,?>){
			Map<?,?> m=(Map<?,?>)v;
			for(Object x: m.keySet()){
				add(sb,indent,""+x,m.get(x));
			}
		}else if(v instanceof Collection<?>){
			for(Object x: (Collection<?>)v){
				add(sb, indent, "item",x);
			}
		}
	}
	
	private boolean isset(Object v){
		return v.getClass().isArray()|| v instanceof Map<?,?> || v instanceof Collection<?>;
	}
	
	private boolean isPrimitive(Object v){
		return MelpTypes.isPrimitiveOrString(v) || v instanceof Date || v.getClass().isEnum();
	}
	
	private void addString(StringBuilder sb,int indent,String name,String value){
		if (pretty) {
			sb.append(padding(indent));
		}
		sb.append('<').append(name).append('>');
		 
		sb.append(value.replace("&","&amp;").replaceAll("<", "&lt;").replaceAll(">","&gt;"));

		sb.append("</").append(name).append('>');
		
		if (pretty) {
			sb.append(CRLN);
		}
	}
	
	
	private String padding(int indent){
		return MelpString.repeat(TAB, indent);
	}


	public boolean isIgnoreNullFields() {
		return ignoreNullFields;
	}


	public XMLObject setIgnoreNullFields(boolean ignoreNullFields) {
		this.ignoreNullFields = ignoreNullFields;
		return this;
	}


	public boolean isPretty() {
		return pretty;
	}


	public XMLObject setPretty(boolean pretty) {
		this.pretty = pretty;
		return this;
	}


	public int getInitIndent() {
		return initIndent;
	}


	public XMLObject setInitIndent(int initIndent) {
		this.initIndent = initIndent;
		return this;
	}


	public boolean isWithXmlHeader() {
		return withXmlHeader;
	}


	public XMLObject setWithXmlHeader(boolean withXmlHeader) {
		this.withXmlHeader = withXmlHeader;
		return this;
	}
	 
}
