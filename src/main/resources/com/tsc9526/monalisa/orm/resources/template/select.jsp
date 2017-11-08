<%@page import="com.tsc9526.monalisa.orm.Version"
%><%@page import="com.tsc9526.monalisa.orm.meta.MetaTable"
%><%@page import="com.tsc9526.monalisa.orm.meta.MetaColumn"
%><%@page import="java.util.Set"%><%
MetaTable    table  =(MetaTable)request.getAttribute("table");
@SuppressWarnings("unchecked")
Set<String> imports =(Set<String>)request.getAttribute("imports");
String   fingerprint=(String)request.getAttribute("fingerprint");
String   see        =(String)request.getAttribute("see");
%>package <%=table.getJavaPackage()%>;
<%
	for(MetaColumn c:table.getColumns()){
		if(c.getTable()!=null && c.getCode("file")!=null){
			imports.add("java.io.File");
			imports.add("com.tsc9526.monalisa.tools.io.MelpFile");
			imports.add("com.tsc9526.monalisa.orm.datasource.DBConfig");
			
			break;
		}
	}
		
%> 
<%for(String i:imports){ %>
import <%=i%>;<%} %>
  
/**
 * Auto generated code by monalisa <%=Version.getVersion()%>
 *
 * @see <%=see%>
 */
public class <%=table.getJavaName()%> implements java.io.Serializable{
	private static final long serialVersionUID = <%=table.getSerialID()%>L;	
	final static String  FINGERPRINT = "<%=fingerprint %>";
	  
	 
	<%for(MetaColumn f:table.getColumns()){ %>
	<%=getComments(table, f, "	","\t") %>
	private <%=f.getJavaType()%> <%=f.getJavaName()%>;	
	
	<%}%>
	
	
	<%for(MetaColumn f:table.getColumns()){ %>
	<%=getComments(table, f, "	","\t") %>
	public <%=table.getJavaName()%> <%=f.getJavaNameSet()%>(<%=f.getJavaType()%> <%=f.getJavaName()%>){
		this.<%=f.getJavaName()%> = <%=f.getJavaName()%>;
		return this;
	}
	
	<%}%>
	
	 
	<%for(MetaColumn f:table.getColumns()){ %>
	<%=getComments(table, f, "	","\t") %>
	public <%=f.getJavaType()%> <%=f.getJavaNameGet()%>(){
		return this.<%=f.getJavaName()%>;		
	}
	
	<%=getComments(table, f, "@param defaultValue  Return the default value if "+f.getJavaName()+" is null.","\t") %>
	public <%=f.getJavaType()%> <%=f.getJavaNameGet()%>(<%=f.getJavaType()%> defaultValue){
		<%=f.getJavaType()%> r=this.<%=f.getJavaNameGet()%>();
		if(r==null){
			r=defaultValue;
		}		
		
		return r;
	}
	
	<%String file=f.getCode("file"); if(f.getTable()!=null && file!=null){%>
	<%=getComments(table,f,"@param charset  read file content using this charset.","\t")%> 
	public String <%=f.getJavaNameGet()%>AsString(String charset){
		<%=f.getJavaType()%> r=this.<%=f.getJavaNameGet()%>();
		
		if(r==null){
			return null;
		}
		
		DBConfig db=DBConfig.fromClass(<%=f.getTable().getJavaName()%>.class);
		String filepath=MelpFile.combinePath("<%=file%>",r);
		filepath=db.getCfg().parseFilePath(filepath);
		return MelpFile.readToString(new File(filepath),charset);
	}
	
	<%=getComments(table,f,"	","\t")%> 
	public String <%=f.getJavaNameGet()%>AsStringUTF8(){
		return <%=f.getJavaNameGet()%>AsString("utf-8");
	}
	
	<%=getComments(table,f,"	","\t")%> 
	public byte[] <%=f.getJavaNameGet()%>AsBytes(){
		<%=f.getJavaType()%> r=this.<%=f.getJavaNameGet()%>();
		
		if(r==null){
			return null;
		}
		
		DBConfig db=DBConfig.fromClass(<%=f.getTable().getJavaName()%>.class);
		String filepath=MelpFile.combinePath("<%=file%>",r);
		filepath=db.getCfg().parseFilePath(filepath);
		return MelpFile.readFile(new File(filepath));
	}
	
	public File <%=f.getJavaNameGet()%>AsFile(){
		<%=f.getJavaType()%> r=this.<%=f.getJavaNameGet()%>();
		
		if(r==null){
			return null;
		}
		
		DBConfig db=DBConfig.fromClass(<%=f.getTable().getJavaName()%>.class);
		String filepath=MelpFile.combinePath("<%=file%>",r);
		filepath=db.getCfg().parseFilePath(filepath);
		return new File(filepath);
	}
	<%}%>
	<%}%>
		 
}
 
<%!
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
%>