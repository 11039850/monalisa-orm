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
package com.tsc9526.monalisa.orm.tools.generator;


import java.io.PrintWriter;
import com.tsc9526.monalisa.orm.parser.jsp.JspContext;
import com.tsc9526.monalisa.orm.meta.MetaColumn;
import java.util.Set;
import com.tsc9526.monalisa.orm.meta.MetaTable;
/**  @author zzg.zhou(11039850@qq.com)  */
public class DBWriterSelect{
	
	String toComments(String remarks){
		return remarks==null?"": remarks.replace("*/","**");
	}
	
	String toJavaString(String s){
		if(s==null)return "";
		
		return s.trim().replace("\"","\\\"").replace("\r","\\r").replace("\n","\\n");
	}

	
	String getComments(MetaTable table,MetaColumn c,String params){
		String cname=c.getName();
		
		if(cname!=null && cname.length()>0 && c.getTable()!=null){	
			String r="/**\r\n";
			r+="* @Column\r\n"; 
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
			r+="\r\n";
			
			if(c.getLength()>0 || c.getValue()!=null){
				r+="* <li>&nbsp;&nbsp;&nbsp;";
			
				if(c.getLength()>0){
					r+="<B>length:</B> "+c.getLength();
				}
				if(c.getValue()!=null){
					r+=" &nbsp;<B>value:</B> "+toJavaString(c.getValue());
				}
				r+="<br>\r\n";
			}
			
			if(c.getRemarks()!=null){
				r+="* <li><B>remarks:</B> "+toComments(c.getRemarks())+"\r\n";
			}
			 
			if(params==null){
				params="";
			}
			params=params.trim();
			if(params.length()>0){
				r+="* "+params;
			}
			
		 	r+="*/\r\n";	
		 
		 	String f=c.getTable().getJavaName()+".M.";
		 	if(c.getTable().getJavaPackage().equals(table.getJavaPackage())){
		 		f="M.";
		 	}
			 	
			String[] names=new String[]{"name","key","auto","notnull","length","value","remarks"};
			
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
		out.println("");
			out.println("");
			out.println("");
			
MetaTable    table =(MetaTable)request.getAttribute("table");
Set<?>     imports =(Set<?>)request.getAttribute("imports");
String   fingerprint=(String)request.getAttribute("fingerprint");
String   see        =(String)request.getAttribute("see");
		out.println("");
			out.print("package ");
			out.print(table.getJavaPackage());
			out.println(";");
			out.println(" ");
			for(Object i:imports){ 		out.println("");
			out.print("import ");
			out.print(i);
			out.println(";");
			} 		out.println("");
			out.println(" ");
			out.println("/**");
			out.println(" * ");
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
			out.print(getComments(table, f, "	") );
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
			out.print(getComments(table, f, "	") );
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
			out.print(getComments(table, f, "	") );
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
			out.print(getComments(table, f, "@param defaultValue  Return the default value if "+f.getJavaName()+" is null.") );
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
			}		out.println("");
			out.println("		 ");
			out.println("}");
			out.println(" ");
		out.flush();

		}
}