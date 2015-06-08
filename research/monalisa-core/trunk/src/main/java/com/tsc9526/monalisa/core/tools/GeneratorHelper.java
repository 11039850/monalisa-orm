package com.tsc9526.monalisa.core.tools;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.core.meta.MetaColumn;
import com.tsc9526.monalisa.core.meta.MetaTable;

public class GeneratorHelper {	 
		
	public static void generatorJavaProperties(MetaTable table,PrintWriter pw){
		for(MetaColumn c:table.getColumns()){	
			generatorColumnComment(table,c,pw);
			pw.println("private "+c.getJavaType()+" "+c.getJavaName()+";");
			pw.println("");
		}
	}
	
	public static void generatorJavaGet(MetaTable table,PrintWriter pw){
		for(MetaColumn c:table.getColumns()){
			pw.println("");
			
			generatorColumnComment(table,c,pw);
			
			String get=JavaBeansHelper.getGetterMethodName(c.getJavaName(), c.getJavaType());
			pw.println("public "+c.getJavaType()+" "+get+"(){");
			pw.println("return this."+c.getJavaName()+";");
			pw.println("}");
		}
	}
	
	public static void generatorJavaSet(MetaTable table,PrintWriter pw){
		for(MetaColumn c:table.getColumns()){
			pw.println("");
			
			generatorColumnComment(table,c,pw);
			
			String set=JavaBeansHelper.getSetterMethodName(c.getJavaName());
			pw.println("public "+table.getJavaName()+" "+set+"("+c.getJavaType()+" "+c.getJavaName()+"){");
			pw.println("this."+c.getJavaName()+" = "+c.getJavaName()+";");
			pw.println("return this;");
			pw.println("}");
		}
	}
	
	 
	public static void generatorColumnComment(MetaTable table,MetaColumn c,PrintWriter pw){
		if(!Helper.isEmpty(c.getName()) && c.getTable()!=null){
			pw.println("/**");
			pw.println(" * @Column ");
			 
			StringBuffer nsb=new StringBuffer();
			nsb.append(" * <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> "+c.getTable().getName()+" ");
			nsb.append("&nbsp;<B>name:</B> "+c.getName());
			
			StringBuffer x=new StringBuffer();
			if(c.isKey()){
				if(x.length()>0){
					x.append("|");
				}
				x.append("<font color=red>KEY</font>");
			}			
			if(c.isAuto()){
				if(x.length()>0){
					x.append("|");
				}
				x.append("<font color=red>AUTO</font>");
			}
			if(c.isNotnull()){
				if(x.length()>0){
					x.append("|");
				}
				x.append("<font color=red>NOTNULL</font>");
			}			
			if(x.length()>0){
				nsb.append(" [").append(x).append("]");
			}
			nsb.append(" <BR>");
			pw.println(nsb);
			
			nsb.delete(0, nsb.length());
			if(c.getLength()>0){
				nsb.append(" * <li>&nbsp;&nbsp;&nbsp;<B>length:</B> "+c.getLength()+" ");
			}						 
			if(c.getValue()!=null){
				nsb.append("&nbsp;<B>value:</B> "+c.getValue()+" <BR>");
			}
			if(nsb.length()>0){
				pw.println(nsb.toString());
			}
			
			if(c.getRemarks()!=null){
				pw.println(" * <li><B>remarks:</B> "+c.getRemarks());
			}
			pw.println(" */");
			
			
			
			String f=c.getTable().getJavaName()+".Metadata.";
			if(c.getTable().getJavaPackage().equals(table.getJavaPackage())){
				f="Metadata.";
			}
			
			List<String> names=new ArrayList<String>();			 
			names.add("name");
			names.add("key");
			names.add("auto");
			names.add("notnull");
			names.add("length");
			names.add("value");
			names.add("remarks");
			
			StringBuffer sb=new StringBuffer();
			for(String n:names){			 
				String colname=c.getJavaName();
				int p=colname.indexOf("$");
				if(p>0){
					colname=colname.substring(p+1);
				}
				
				sb.append(", ").append(n).append("=").append(f+colname+"$").append(n);
			}
			sb.insert(0, "@Column(table="+f+"table");
			sb.append(")");  
			
			pw.println(sb.toString());
		}
	}
}
