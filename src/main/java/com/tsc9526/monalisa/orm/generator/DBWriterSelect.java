/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *   Monalisa is free software: you can redistribute it and/or modify
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
package com.tsc9526.monalisa.orm.generator;


import java.io.PrintWriter;
import com.tsc9526.monalisa.tools.template.jsp.JspContext;
import com.tsc9526.monalisa.orm.Version;
import com.tsc9526.monalisa.orm.meta.MetaTable;
import com.tsc9526.monalisa.orm.meta.MetaColumn;
import java.util.Set;

/**
 * Auto generate code from jsp: src\main\resources\com\tsc9526\monalisa\orm\resources\template\select.jsp
 * 
 * @author zzg.zhou(11039850@qq.com)  
 */

public class DBWriterSelect{
	
	String toComments(String remarks){
		return remarks==null?"": remarks.replace("*/","**");
	}
	
	String toJavaString(String s){
		if(s==null)return "";
		
		return s.trim().replace("\"","\\\"").replace("\r","\\r").replace("\n","\\n");
	}
 
	String getComments(MetaTable table,MetaColumn c,String params,String leftPadding){
		String cname=c.getName();
		
		if(cname!=null && cname.length()>0 && c.getTable()!=null){	
			String r="/**\r\n"+leftPadding;
			r+="* @Column\r\n"+leftPadding; 
			r+="* <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<B>table:</B> "+c.getTable().getName()+"&nbsp;<B>name:</B> "+cname;
			
			if(c.isKey() || c.isAuto() || c.isNotnull() || c.isEnum()){
				boolean b=false;
				r+=" &nbsp;[";
				if(c.isKey()){
					r+=(b?"|":"")+"<font color=red>KEY</font>";
					b=true;
				}
				if(c.isAuto()){
					r+=(b?"|":"")+"<font color=red>AUTO</font>";
					b=true;
				}
				if(c.isNotnull()){
					r+=(b?"|":"")+"<font color=red>NOTNULL</font>";
					b=true;
				}
				if(c.isEnum()){
					r+=(b?"|":"")+"<font color=red>ENUM</font>";
					b=true;
				}
				r+="]";
			}
			r+="\r\n"+leftPadding;
			
			if(c.getLength()>0 || c.getValue()!=null){
				r+="* <li>&nbsp;&nbsp;&nbsp;";
			
				if(c.getLength()>0){
					r+="<B>length:</B> "+c.getLength();
				}
				if(c.getValue()!=null){
					r+=" &nbsp;<B>value:</B> "+toJavaString(c.getValue());
				}
				r+="<br>\r\n"+leftPadding;
			}
			
			if(c.getRemarks()!=null){
				r+="* <li><B>remarks:</B> "+toComments(c.getRemarks())+"\r\n"+leftPadding;
			}
			 
			if(params==null){
				params="";
			}
			params=params.trim();
			if(params.length()>0){
				r+="* "+params;
			}
			
		 	r+="*/\r\n"+leftPadding;	
		 
		 	String f=c.getTable().getJavaName()+".M.";
		 	if(c.getTable().getJavaPackage().equals(table.getJavaPackage())){
		 		f="M.";
		 	}
			 	
			String[] names=new String[]{"name","key","auto","seq","notnull","length","decimalDigits","value","remarks"};
			
			r+="@Column(table="+f+"TABLE, jdbcType="+c.getJdbcType();
			for(String n:names){
				String colname=c.nameToJava();
				int p=colname.indexOf("$");
				if(p>0){
					colname=colname.substring(p+1);
				}
			
				r+=", "+n+"="+f+colname+"$"+n;
			}
			r+=")";
			 
			return r;
		}else{
			return "";
		}
	}

	String firstUpper(String s){
		return s.substring(0,1).toUpperCase()+s.substring(1);
	}

	String html(Object v){
		if(v==null){
			return "";
		}else{
			return v.toString().trim();
		}
	}
	public void service(JspContext request,PrintWriter out){
		
MetaTable    table  =(MetaTable)request.getAttribute("table");
@SuppressWarnings("unchecked")
Set<String> imports =(Set<String>)request.getAttribute("imports");
String   fingerprint=(String)request.getAttribute("fingerprint");
String   see        =(String)request.getAttribute("see");
		out.print("package ");
			out.print(table.getJavaPackage());
			out.println(";");
			
	for(MetaColumn c:table.getColumns()){
		if(c.getTable()!=null && c.getCode("file")!=null){
			imports.add("java.io.File");
			imports.add("com.tsc9526.monalisa.tools.io.MelpFile");
			imports.add("com.tsc9526.monalisa.orm.datasource.DBConfig");
			
			break;
		}
	}
		
		out.println(" ");
			for(String i:imports){ 		out.println("");
			out.print("import ");
			out.print(i);
			out.print(";");
			} 		out.println("");
			out.println("  ");
			out.println("/**");
			out.print(" * Auto generated code by monalisa ");
			out.print(Version.getVersion());
			out.println("");
			out.println(" *");
			out.print(" * @see ");
			out.print(see);
			out.println("");
			out.println(" */");
			out.print("public class ");
			out.print(table.getJavaName());
			out.println(" implements java.io.Serializable{");
			out.print("	private static final long serialVersionUID = ");
			out.print(table.getSerialID());
			out.println("L;	");
			out.print("	final static String  FINGERPRINT = \"");
			out.print(fingerprint );
			out.println("\";");
			out.println("	  ");
			out.println("	 ");
			out.print("	");
			for(MetaColumn f:table.getColumns()){ 		out.println("");
			out.print("	");
			out.print(getComments(table, f, "	","\t") );
			out.println("");
			out.print("	private ");
			out.print(f.getJavaType());
			out.print(" ");
			out.print(f.getJavaName());
			out.println(";	");
			out.println("	");
			out.print("	");
			}		out.println("");
			out.println("	");
			out.println("	");
			out.print("	");
			for(MetaColumn f:table.getColumns()){ 		out.println("");
			out.print("	");
			out.print(getComments(table, f, "	","\t") );
			out.println("");
			out.print("	public ");
			out.print(table.getJavaName());
			out.print(" ");
			out.print(f.getJavaNameSet());
			out.print("(");
			out.print(f.getJavaType());
			out.print(" ");
			out.print(f.getJavaName());
			out.println("){");
			out.print("		this.");
			out.print(f.getJavaName());
			out.print(" = ");
			out.print(f.getJavaName());
			out.println(";");
			out.println("		return this;");
			out.println("	}");
			out.println("	");
			out.print("	");
			}		out.println("");
			out.println("	");
			out.println("	 ");
			out.print("	");
			for(MetaColumn f:table.getColumns()){ 		out.println("");
			out.print("	");
			out.print(getComments(table, f, "	","\t") );
			out.println("");
			out.print("	public ");
			out.print(f.getJavaType());
			out.print(" ");
			out.print(f.getJavaNameGet());
			out.println("(){");
			out.print("		return this.");
			out.print(f.getJavaName());
			out.println(";		");
			out.println("	}");
			out.println("	");
			out.print("	");
			out.print(getComments(table, f, "@param defaultValue  Return the default value if "+f.getJavaName()+" is null.","\t") );
			out.println("");
			out.print("	public ");
			out.print(f.getJavaType());
			out.print(" ");
			out.print(f.getJavaNameGet());
			out.print("(");
			out.print(f.getJavaType());
			out.println(" defaultValue){");
			out.print("		");
			out.print(f.getJavaType());
			out.print(" r=this.");
			out.print(f.getJavaNameGet());
			out.println("();");
			out.println("		if(r==null){");
			out.println("			r=defaultValue;");
			out.println("		}		");
			out.println("		");
			out.println("		return r;");
			out.println("	}");
			out.println("	");
			out.print("	");
			String file=f.getCode("file"); if(f.getTable()!=null && file!=null){		out.println("");
			out.print("	");
			out.print(getComments(table,f,"@param charset  read file content using this charset.","\t"));
			out.println(" ");
			out.print("	public String ");
			out.print(f.getJavaNameGet());
			out.println("AsString(String charset){");
			out.print("		");
			out.print(f.getJavaType());
			out.print(" r=this.");
			out.print(f.getJavaNameGet());
			out.println("();");
			out.println("		");
			out.println("		if(r==null){");
			out.println("			return null;");
			out.println("		}");
			out.println("		");
			out.print("		DBConfig db=DBConfig.fromClass(");
			out.print(f.getTable().getJavaName());
			out.println(".class);");
			out.print("		String filepath=MelpFile.combinePath(\"");
			out.print(file);
			out.println("\",r);");
			out.println("		filepath=db.getCfg().parseFilePath(filepath);");
			out.println("		return MelpFile.readToString(new File(filepath),charset);");
			out.println("	}");
			out.println("	");
			out.print("	");
			out.print(getComments(table,f,"	","\t"));
			out.println(" ");
			out.print("	public String ");
			out.print(f.getJavaNameGet());
			out.println("AsStringUTF8(){");
			out.print("		return ");
			out.print(f.getJavaNameGet());
			out.println("AsString(\"utf-8\");");
			out.println("	}");
			out.println("	");
			out.print("	");
			out.print(getComments(table,f,"	","\t"));
			out.println(" ");
			out.print("	public byte[] ");
			out.print(f.getJavaNameGet());
			out.println("AsBytes(){");
			out.print("		");
			out.print(f.getJavaType());
			out.print(" r=this.");
			out.print(f.getJavaNameGet());
			out.println("();");
			out.println("		");
			out.println("		if(r==null){");
			out.println("			return null;");
			out.println("		}");
			out.println("		");
			out.print("		DBConfig db=DBConfig.fromClass(");
			out.print(f.getTable().getJavaName());
			out.println(".class);");
			out.print("		String filepath=MelpFile.combinePath(\"");
			out.print(file);
			out.println("\",r);");
			out.println("		filepath=db.getCfg().parseFilePath(filepath);");
			out.println("		return MelpFile.readFile(new File(filepath));");
			out.println("	}");
			out.println("	");
			out.print("	public File ");
			out.print(f.getJavaNameGet());
			out.println("AsFile(){");
			out.print("		");
			out.print(f.getJavaType());
			out.print(" r=this.");
			out.print(f.getJavaNameGet());
			out.println("();");
			out.println("		");
			out.println("		if(r==null){");
			out.println("			return null;");
			out.println("		}");
			out.println("		");
			out.print("		DBConfig db=DBConfig.fromClass(");
			out.print(f.getTable().getJavaName());
			out.println(".class);");
			out.print("		String filepath=MelpFile.combinePath(\"");
			out.print(file);
			out.println("\",r);");
			out.println("		filepath=db.getCfg().parseFilePath(filepath);");
			out.println("		return new File(filepath);");
			out.println("	}");
			out.print("	");
			}		out.println("");
			out.print("	");
			}		out.println("");
			out.println("		 ");
			out.println("}");
			out.println(" ");
		out.flush();

		}
}